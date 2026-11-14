package com.example.ui.screens

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.R
import com.example.model.BrandConfig
import com.example.ui.components.BrandLogoVisual
import com.example.ui.theme.CarbonBlack
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.HayukRed
import com.example.ui.theme.HayukRedSoft
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SlateText
import com.example.ui.theme.SnowSurface

private const val TAG = "WebshopScreen"
private const val MAX_AUTO_RENDERER_RECOVERIES = 1

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebshopScreen(
    brand: BrandConfig,
    onBackClick: () -> Unit,
    onOpenExternalBrowser: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var containerRef by remember { mutableStateOf<FrameLayout?>(null) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var canGoBackInWeb by remember { mutableStateOf(false) }
    var loadProgress by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var hasLoadError by remember { mutableStateOf(false) }
    var isRendererCrashError by remember { mutableStateOf(false) }
    var rendererCrashCount by remember(brand.brandId) { mutableIntStateOf(0) }
    var pendingRecovery by remember { mutableStateOf(false) }
    var currentPageUrl by remember(brand.brandId) { mutableStateOf(brand.webshopUrl) }
    var pageTitle by remember(brand.brandId) { mutableStateOf<String?>(null) }

    fun destroyActiveWebView(target: WebView? = webViewRef) {
        target?.let { wv ->
            wv.stopLoading()
            (wv.parent as? ViewGroup)?.removeView(wv)
            wv.removeAllViews()
            wv.destroy()
        }
        if (target == webViewRef) {
            webViewRef = null
        }
        canGoBackInWeb = false
    }

    fun attachFreshWebView(
        container: FrameLayout,
        targetUrl: String,
        useSoftwareLayer: Boolean
    ) {
        destroyActiveWebView()
        container.removeAllViews()

        val webView = WebView(container.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            if (useSoftwareLayer) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_IMPORTANT, true)
            }
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadsImagesAutomatically = true
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
                mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            }
            val webViewInstance = this
            CookieManager.getInstance().apply {
                setAcceptCookie(true)
                setAcceptThirdPartyCookies(webViewInstance, true)
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    loadProgress = newProgress
                    isLoading = newProgress < 100
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    if (!title.isNullOrBlank() && !title.startsWith("http")) {
                        pageTitle = title
                    }
                }
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val uri = request?.url ?: return false
                    return handleWebshopNavigationUri(
                        context = context,
                        webView = view,
                        uri = uri
                    )
                }

                override fun onPageStarted(
                    view: WebView?,
                    url: String?,
                    favicon: Bitmap?
                ) {
                    hasLoadError = false
                    isRendererCrashError = false
                    isLoading = true
                    if (!url.isNullOrBlank()) {
                        currentPageUrl = url
                    }
                    canGoBackInWeb = view?.canGoBack() == true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    isLoading = false
                    loadProgress = 100
                    if (!url.isNullOrBlank()) {
                        currentPageUrl = url
                    }
                    canGoBackInWeb = view?.canGoBack() == true
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    if (request?.isForMainFrame == true) {
                        isLoading = false
                        isRendererCrashError = false
                        hasLoadError = true
                    }
                }

                override fun onRenderProcessGone(
                    view: WebView?,
                    detail: RenderProcessGoneDetail?
                ): Boolean {
                    val didCrash = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        detail?.didCrash() == true
                    } else {
                        true
                    }
                    Log.w(TAG, "WebView renderer terminated (didCrash=$didCrash)")

                    destroyActiveWebView(view)
                    isLoading = false

                    val canAutoRecover = rendererCrashCount < MAX_AUTO_RENDERER_RECOVERIES
                    if (canAutoRecover) {
                        rendererCrashCount += 1
                        val isResumed = lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
                        if (isResumed) {
                            hasLoadError = false
                            isRendererCrashError = false
                            isLoading = true
                            container.post {
                                if (container.isAttachedToWindow &&
                                    lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
                                ) {
                                    attachFreshWebView(
                                        container = container,
                                        targetUrl = currentPageUrl.ifBlank { brand.webshopUrl },
                                        useSoftwareLayer = true
                                    )
                                } else {
                                    pendingRecovery = true
                                }
                            }
                        } else {
                            pendingRecovery = true
                        }
                    } else {
                        isRendererCrashError = true
                        hasLoadError = true
                    }
                    return true
                }
            }
        }

        container.addView(webView)
        webViewRef = webView
        hasLoadError = false
        isRendererCrashError = false
        isLoading = true
        webView.loadUrl(targetUrl)
    }

    val handleNavigateBack: () -> Unit = {
        val wv = webViewRef
        if (wv != null && wv.canGoBack() && !hasLoadError) {
            wv.goBack()
        } else {
            onBackClick()
        }
    }

    BackHandler(onBack = handleNavigateBack)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    webViewRef?.onResume()
                    if (pendingRecovery) {
                        pendingRecovery = false
                        containerRef?.let { container ->
                            attachFreshWebView(
                                container = container,
                                targetUrl = currentPageUrl.ifBlank { brand.webshopUrl },
                                useSoftwareLayer = true
                            )
                        }
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    webViewRef?.onPause()
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            destroyActiveWebView()
            containerRef?.removeAllViews()
            containerRef = null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureWhite)
            .windowInsetsPadding(WindowInsets.statusBars)
            .navigationBarsPadding()
            .imePadding()
    ) {
        // Branded In-App Webshop Header
        Surface(
            color = PureWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = handleNavigateBack,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("webshop_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back_button),
                            tint = CarbonBlack
                        )
                    }

                    BrandLogoVisual(
                        brand = brand,
                        size = 36.dp
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = brand.brandName,
                            style = MaterialTheme.typography.titleMedium,
                            color = CarbonBlack,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Lock,
                                contentDescription = null,
                                tint = HayukRed,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when {
                                    isLoading && loadProgress < 100 ->
                                        stringResource(R.string.webshop_loading)
                                    !pageTitle.isNullOrBlank() -> pageTitle.orEmpty()
                                    else -> stringResource(R.string.webshop_secure_subtitle)
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                                color = SlateText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            hasLoadError = false
                            isRendererCrashError = false
                            isLoading = true
                            val wv = webViewRef
                            if (wv != null && wv.url != null && wv.url != "about:blank") {
                                wv.reload()
                            } else {
                                containerRef?.let { container ->
                                    attachFreshWebView(
                                        container = container,
                                        targetUrl = currentPageUrl.ifBlank { brand.webshopUrl },
                                        useSoftwareLayer = rendererCrashCount > 0
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("webshop_reload_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = stringResource(R.string.cd_reload_webshop),
                            tint = CarbonBlack
                        )
                    }

                    IconButton(
                        onClick = { onOpenExternalBrowser(currentPageUrl.ifBlank { brand.webshopUrl }) },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("webshop_open_external_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                            contentDescription = stringResource(R.string.cd_open_external_browser),
                            tint = CarbonBlack
                        )
                    }
                }

                if (isLoading && loadProgress in 0..99) {
                    LinearProgressIndicator(
                        progress = { (loadProgress.coerceIn(5, 100)) / 100f },
                        color = HayukRed,
                        trackColor = HayukRedSoft,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .testTag("webshop_loading_indicator")
                    )
                } else {
                    HorizontalDivider(color = HairlineBorder, thickness = 1.dp)
                }
            }
        }

        // In-App WebView Body + Error Recovery
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(SnowSurface)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("webshop_webview"),
                factory = { ctx ->
                    FrameLayout(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        containerRef = this
                        attachFreshWebView(
                            container = this,
                            targetUrl = brand.webshopUrl,
                            useSoftwareLayer = false
                        )
                    }
                },
                update = {
                    canGoBackInWeb = webViewRef?.canGoBack() == true
                }
            )

            if (hasLoadError) {
                WebshopLoadErrorOverlay(
                    title = if (isRendererCrashError) {
                        stringResource(R.string.webshop_renderer_error_title)
                    } else {
                        stringResource(R.string.webshop_error_title)
                    },
                    subtitle = if (isRendererCrashError) {
                        stringResource(R.string.webshop_renderer_error_subtitle)
                    } else {
                        stringResource(R.string.webshop_error_subtitle)
                    },
                    onRetry = {
                        hasLoadError = false
                        isRendererCrashError = false
                        isLoading = true
                        val wv = webViewRef
                        if (wv != null) {
                            wv.loadUrl(currentPageUrl.ifBlank { brand.webshopUrl })
                        } else {
                            containerRef?.let { container ->
                                attachFreshWebView(
                                    container = container,
                                    targetUrl = currentPageUrl.ifBlank { brand.webshopUrl },
                                    useSoftwareLayer = true
                                )
                            }
                        }
                    },
                    onOpenExternal = {
                        onOpenExternalBrowser(currentPageUrl.ifBlank { brand.webshopUrl })
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

private fun handleWebshopNavigationUri(
    context: Context,
    webView: WebView?,
    uri: Uri
): Boolean {
    val scheme = uri.scheme?.lowercase().orEmpty()
    if (scheme == "http" || scheme == "https") {
        // Keep standard web navigation inside the in-app WebView
        return false
    }

    if (scheme == "intent") {
        return try {
            val intent = Intent.parseUri(uri.toString(), Intent.URI_INTENT_SCHEME).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(intent)
                true
            } catch (_: ActivityNotFoundException) {
                val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                if (!fallbackUrl.isNullOrBlank() &&
                    (fallbackUrl.startsWith("https://", true) || fallbackUrl.startsWith("http://", true))
                ) {
                    webView?.loadUrl(fallbackUrl)
                }
                true
            }
        } catch (_: Exception) {
            true
        }
    }

    // Handle external app schemes (WhatsApp, e-wallets, tel, mailto) gracefully
    return try {
        val externalIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(externalIntent)
        true
    } catch (_: ActivityNotFoundException) {
        true
    }
}

@Composable
private fun WebshopLoadErrorOverlay(
    title: String,
    subtitle: String,
    onRetry: () -> Unit,
    onOpenExternal: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = PureWhite,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = HayukRedSoft,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.WifiOff,
                        contentDescription = null,
                        tint = HayukRed,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = CarbonBlack,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = SlateText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HayukRed,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("webshop_retry_button")
            ) {
                Text(
                    text = stringResource(R.string.action_retry),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onOpenExternal,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, HairlineBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("webshop_fallback_browser_button")
            ) {
                Text(
                    text = stringResource(R.string.action_open_in_browser),
                    style = MaterialTheme.typography.labelMedium,
                    color = CarbonBlack
                )
            }
        }
    }
}
