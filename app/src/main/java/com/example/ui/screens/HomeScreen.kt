package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.BrandConfig
import com.example.model.BrandSubCategory
import com.example.model.FoodcourtUiState
import com.example.ui.components.BrandLogoVisual
import com.example.ui.components.HayukBrandLogo
import com.example.ui.components.HeroFoodcourtBannerArtwork
import com.example.ui.components.KopiKulangoProductArtwork
import com.example.ui.theme.CarbonBlack
import com.example.ui.theme.EspressoDark
import com.example.ui.theme.HayukLogoGold
import com.example.ui.theme.HayukRed
import com.example.ui.theme.HayukRedBorder
import com.example.ui.theme.HayukRedDark
import com.example.ui.theme.HayukRedDeep
import com.example.ui.theme.HayukRedSoft
import com.example.ui.theme.MutedGray
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SlateText

@Composable
fun HomeScreen(
    uiState: FoodcourtUiState,
    onOrderFeaturedBrand: (BrandConfig) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onNavigateToDirectory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        contentVisible = true
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureWhite)
            .verticalScroll(scrollState)
            .windowInsetsPadding(WindowInsets.statusBars)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // A. Logo HAYUK! — Top, centered, large, pure white background
        Surface(
            color = PureWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 14.dp, start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HayukBrandLogo(modifier = Modifier.testTag("home_hayuk_logo"))

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    color = HayukRedSoft,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = stringResource(R.string.brand_descriptor).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = HayukRedDeep,
                        letterSpacing = 1.4.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // B. Hero Banner — Warm Appetizing Culinary Visual + ONE PLACE, MANY FLAVORS.
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                    slideInVertically(initialOffsetY = { 24 })
            ) {
                HeroBannerCard(
                    brandCount = uiState.totalCount,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // C. Flagship Featured Brand – KOPI KULANGO (Beverage Flagship)
            uiState.flagshipBrand?.let { flagship ->
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                        slideInVertically(initialOffsetY = { 36 })
                ) {
                    FlagshipKopiKulangoSection(
                        brand = flagship,
                        isFavorite = uiState.isFavorite(flagship.brandId),
                        onToggleFavorite = { onToggleFavorite(flagship.brandId) },
                        onOrderClick = { onOrderFeaturedBrand(flagship) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Secondary Featured Brands: 1 Main Course + 1 Snack
            if (uiState.secondaryFeaturedBrands.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) +
                        slideInVertically(initialOffsetY = { 42 })
                ) {
                    SecondaryFeaturedBrandsSection(
                        brands = uiState.secondaryFeaturedBrands,
                        isFavorite = uiState::isFavorite,
                        onToggleFavorite = onToggleFavorite,
                        onOrderBrand = onOrderFeaturedBrand,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // D. Main Navigation — Single primary CTA button: PILIH BRAND
            Button(
                onClick = onNavigateToDirectory,
                colors = ButtonDefaults.buttonColors(
                    containerColor = HayukRed,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(18.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 1.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .testTag("home_choose_brand_button")
            ) {
                Icon(
                    imageVector = Icons.Rounded.GridView,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.cta_choose_brand),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 15.sp,
                        letterSpacing = 1.1.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.footer_powered_by),
                style = MaterialTheme.typography.bodyMedium,
                color = MutedGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
private fun HeroBannerCard(
    brandCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(216.dp),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 6.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            HeroFoodcourtBannerArtwork(modifier = Modifier.fillMaxSize())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = PureWhite.copy(alpha = 0.14f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, PureWhite.copy(alpha = 0.24f))
                ) {
                    Text(
                        text = stringResource(R.string.official_app_badge).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = PureWhite,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Column(modifier = Modifier.fillMaxWidth(0.74f)) {
                    Text(
                        text = stringResource(R.string.hero_headline),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontSize = 27.sp,
                            lineHeight = 31.sp
                        ),
                        color = PureWhite
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.hero_subheadline, brandCount),
                        style = MaterialTheme.typography.titleMedium,
                        color = HayukLogoGold
                    )
                }
            }
        }
    }
}

@Composable
private fun FlagshipKopiKulangoSection(
    brand: BrandConfig,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOrderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.testTag("home_featured_kopi_kulango_card"),
        shape = RoundedCornerShape(26.dp),
        color = CarbonBlack,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            CarbonBlack,
                            EspressoDark,
                            HayukRedDark
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = HayukRed,
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                tint = HayukLogoGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.featured_brand_badge),
                                style = MaterialTheme.typography.labelSmall,
                                color = PureWhite,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.flagship_brand_badge),
                            style = MaterialTheme.typography.labelSmall,
                            color = HayukLogoGold,
                            letterSpacing = 1.sp
                        )
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("home_favorite_toggle_${brand.brandId}")
                        ) {
                            Icon(
                                imageVector = if (isFavorite) {
                                    Icons.Rounded.Favorite
                                } else {
                                    Icons.Rounded.FavoriteBorder
                                },
                                contentDescription = if (isFavorite) {
                                    stringResource(R.string.cd_favorite_remove, brand.brandName)
                                } else {
                                    stringResource(R.string.cd_favorite_add, brand.brandName)
                                },
                                tint = if (isFavorite) HayukRed else PureWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    KopiKulangoProductArtwork(
                        modifier = Modifier
                            .size(width = 132.dp, height = 148.dp)
                            .clip(RoundedCornerShape(18.dp))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            color = PureWhite,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(modifier = Modifier.padding(6.dp)) {
                                BrandLogoVisual(brand = brand, size = 54.dp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = brand.brandName,
                            style = MaterialTheme.typography.headlineMedium,
                            color = PureWhite
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = brand.shortDescription.ifBlank {
                                stringResource(R.string.category_beverage)
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFFFE5DC),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onOrderClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PureWhite,
                        contentColor = CarbonBlack
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("home_order_kopi_kulango_button")
                ) {
                    Text(
                        text = stringResource(R.string.cta_order_now),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SecondaryFeaturedBrandsSection(
    brands: List<BrandConfig>,
    isFavorite: (String) -> Boolean,
    onToggleFavorite: (String) -> Unit,
    onOrderBrand: (BrandConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.featured_picks_title),
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.sp),
                color = CarbonBlack
            )
            Text(
                text = "${stringResource(R.string.featured_main_course_badge)} • ${stringResource(R.string.featured_snacks_badge)}",
                style = MaterialTheme.typography.labelSmall,
                color = SlateText
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            brands.take(2).forEach { brand ->
                SecondaryFeaturedBrandCard(
                    brand = brand,
                    isFavorite = isFavorite(brand.brandId),
                    onToggleFavorite = { onToggleFavorite(brand.brandId) },
                    onOrderClick = { onOrderBrand(brand) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SecondaryFeaturedBrandCard(
    brand: BrandConfig,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOrderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryLabel = when (brand.subCategory) {
        BrandSubCategory.MAIN_COURSE -> stringResource(R.string.featured_main_course_badge)
        BrandSubCategory.SNACKS -> stringResource(R.string.featured_snacks_badge)
        BrandSubCategory.BEVERAGES -> stringResource(R.string.category_beverage)
    }

    Surface(
        modifier = modifier
            .height(248.dp)
            .testTag("home_featured_card_${brand.brandId}"),
        shape = RoundedCornerShape(22.dp),
        color = PureWhite,
        border = BorderStroke(1.2.dp, HayukRedBorder),
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = HayukRedSoft,
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            tint = HayukRed,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = categoryLabel,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                            color = HayukRedDeep,
                            maxLines = 1
                        )
                    }
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("home_favorite_toggle_${brand.brandId}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) {
                            Icons.Rounded.Favorite
                        } else {
                            Icons.Rounded.FavoriteBorder
                        },
                        contentDescription = if (isFavorite) {
                            stringResource(R.string.cd_favorite_remove, brand.brandName)
                        } else {
                            stringResource(R.string.cd_favorite_add, brand.brandName)
                        },
                        tint = if (isFavorite) HayukRed else SlateText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            BrandLogoVisual(brand = brand, size = 66.dp)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = brand.brandName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp
                    ),
                    color = CarbonBlack,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (brand.shortDescription.isNotBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = brand.shortDescription,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        ),
                        color = SlateText,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Button(
                onClick = onOrderClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CarbonBlack,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("home_order_${brand.brandId}_button")
            ) {
                Text(
                    text = stringResource(R.string.cta_order_now),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}
