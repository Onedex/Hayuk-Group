package com.example.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.toArgb
import com.example.ui.theme.HayukRed

object KlikitWebshopLauncher {

    fun openWebshop(context: Context, url: String): Boolean {
        val trimmedUrl = url.trim()
        if (!trimmedUrl.startsWith("https://", ignoreCase = true) &&
            !trimmedUrl.startsWith("http://", ignoreCase = true)
        ) {
            return false
        }

        val uri = Uri.parse(trimmedUrl)
        val colorParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(HayukRed.toArgb())
            .setNavigationBarColor(HayukRed.toArgb())
            .build()

        val customTabsIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(colorParams)
            .setShowTitle(true)
            .setUrlBarHidingEnabled(false)
            .build()

        return try {
            customTabsIntent.launchUrl(context, uri)
            true
        } catch (_: ActivityNotFoundException) {
            launchExternalBrowserFallback(context, uri)
        } catch (_: SecurityException) {
            launchExternalBrowserFallback(context, uri)
        }
    }

    private fun launchExternalBrowserFallback(context: Context, uri: Uri): Boolean {
        return try {
            val viewIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(viewIntent)
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }
}
