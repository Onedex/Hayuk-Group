package com.example.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BakeryDining
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.EmojiFoodBeverage
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.LocalBar
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.LocalDrink
import androidx.compose.material.icons.rounded.LocalPizza
import androidx.compose.material.icons.rounded.LunchDining
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.RiceBowl
import androidx.compose.material.icons.rounded.SetMeal
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.BrandCategory
import com.example.model.BrandConfig
import com.example.model.BrandSubCategory
import com.example.ui.theme.BananaLeafGreen
import com.example.ui.theme.CarbonBlack
import com.example.ui.theme.CremaGold
import com.example.ui.theme.CrispyGold
import com.example.ui.theme.EspressoDark
import com.example.ui.theme.EspressoWarm
import com.example.ui.theme.HayukLogoGold
import com.example.ui.theme.HayukLogoRed
import com.example.ui.theme.HayukRed
import com.example.ui.theme.HayukRedDeep
import com.example.ui.theme.HayukRedSoft
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SambalRed
import com.example.ui.theme.SlateText

@SuppressLint("DiscouragedApi")
@Composable
fun HayukBrandLogo(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val customDrawableId = remember(context) {
        context.resources.getIdentifier("logo_hayuk", "drawable", context.packageName)
    }

    if (customDrawableId != 0) {
        Image(
            painter = painterResource(id = customDrawableId),
            contentDescription = stringResource(R.string.cd_hayuk_logo),
            modifier = modifier.height(58.dp),
            contentScale = ContentScale.Fit
        )
    } else {
        Box(
            modifier = modifier.padding(vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(width = 184.dp, height = 18.dp)
                    .align(Alignment.BottomCenter)
            ) {
                val w = size.width
                val h = size.height
                val swooshPath = Path().apply {
                    moveTo(w * 0.04f, h * 0.72f)
                    cubicTo(
                        w * 0.32f, h * 0.14f,
                        w * 0.68f, h * 0.14f,
                        w * 0.92f, h * 0.42f
                    )
                    cubicTo(
                        w * 0.68f, h * 0.34f,
                        w * 0.34f, h * 0.38f,
                        w * 0.04f, h * 0.72f
                    )
                    close()
                }
                drawPath(path = swooshPath, color = HayukLogoGold)
            }
            Text(
                text = "Hayuk!",
                fontFamily = OutfitFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic,
                fontSize = 44.sp,
                letterSpacing = (-1.6).sp,
                color = HayukLogoRed,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .graphicsLayer {
                        rotationZ = -2.2f
                    }
            )
        }
    }
}

@Composable
fun HeroFoodcourtBannerArtwork(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Warm appetizing dark wood / charred slate-to-crimson backdrop (zero blue!)
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1A0E0C),
                    Color(0xFF3A1210),
                    Color(0xFF700B11)
                ),
                start = Offset.Zero,
                end = Offset(w, h)
            )
        )

        // Warm golden culinary studio glow behind the food & drink spread
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(HayukLogoGold.copy(alpha = 0.36f), Color.Transparent),
                center = Offset(w * 0.77f, h * 0.50f),
                radius = w * 0.42f
            ),
            center = Offset(w * 0.77f, h * 0.50f),
            radius = w * 0.42f
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(HayukRed.copy(alpha = 0.45f), Color.Transparent),
                center = Offset(w * 0.88f, h * 0.25f),
                radius = w * 0.34f
            ),
            center = Offset(w * 0.88f, h * 0.25f),
            radius = w * 0.34f
        )

        // Iced Signature Beverage Cup (back-left of the culinary spread)
        val cupLeft = w * 0.56f
        val cupTop = h * 0.13f
        val cupW = w * 0.13f
        val cupH = h * 0.50f
        // Straw
        drawLine(
            color = HayukLogoGold,
            start = Offset(cupLeft + cupW * 0.65f, cupTop - h * 0.08f),
            end = Offset(cupLeft + cupW * 0.48f, cupTop + cupH * 0.35f),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )
        // Cup body with layered iced palm sugar / chocolate milk gradient
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFFF8EE),
                    CremaGold,
                    Color(0xFF5D2D16),
                    Color(0xFF2D120B)
                ),
                startY = cupTop,
                endY = cupTop + cupH
            ),
            topLeft = Offset(cupLeft, cupTop),
            size = Size(cupW, cupH),
            cornerRadius = CornerRadius(16f, 16f)
        )
        // Ice cube glints & condensation highlights
        drawRoundRect(
            color = PureWhite.copy(alpha = 0.35f),
            topLeft = Offset(cupLeft + cupW * 0.16f, cupTop + cupH * 0.14f),
            size = Size(cupW * 0.32f, cupW * 0.32f),
            cornerRadius = CornerRadius(6f, 6f)
        )
        drawRoundRect(
            color = PureWhite.copy(alpha = 0.28f),
            topLeft = Offset(cupLeft + cupW * 0.52f, cupTop + cupH * 0.22f),
            size = Size(cupW * 0.28f, cupW * 0.28f),
            cornerRadius = CornerRadius(6f, 6f)
        )
        drawRoundRect(
            color = PureWhite.copy(alpha = 0.92f),
            topLeft = Offset(cupLeft - 4f, cupTop - 6f),
            size = Size(cupW + 8f, 11f),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // Hero Main Culinary Platter (Crispy Golden Fried Chicken/Duck + Banana Leaf + Sambal)
        val plateCenter = Offset(w * 0.78f, h * 0.57f)
        val plateRadius = h * 0.36f

        // Plate drop shadow
        drawCircle(
            color = Color.Black.copy(alpha = 0.45f),
            radius = plateRadius * 1.07f,
            center = plateCenter + Offset(4f, 9f)
        )
        // Warm porcelain plate
        drawCircle(
            color = Color(0xFFFFFBF7),
            radius = plateRadius,
            center = plateCenter
        )
        // Banana leaf liner inside plate
        val leafPath = Path().apply {
            moveTo(plateCenter.x - plateRadius * 0.80f, plateCenter.y + plateRadius * 0.10f)
            quadraticTo(
                plateCenter.x - plateRadius * 0.10f, plateCenter.y - plateRadius * 0.82f,
                plateCenter.x + plateRadius * 0.78f, plateCenter.y - plateRadius * 0.18f
            )
            quadraticTo(
                plateCenter.x + plateRadius * 0.35f, plateCenter.y + plateRadius * 0.78f,
                plateCenter.x - plateRadius * 0.80f, plateCenter.y + plateRadius * 0.10f
            )
            close()
        }
        drawPath(path = leafPath, color = BananaLeafGreen)

        // Steamed warm white rice mound
        drawCircle(
            color = Color(0xFFFFFDF9),
            radius = plateRadius * 0.34f,
            center = plateCenter + Offset(-plateRadius * 0.32f, -plateRadius * 0.14f)
        )
        drawCircle(
            color = Color(0xFFF3E9D2),
            radius = plateRadius * 0.34f,
            center = plateCenter + Offset(-plateRadius * 0.32f, -plateRadius * 0.14f),
            style = Stroke(width = 2f)
        )

        // Crispy golden fried duck / chicken drumstick & thigh with crunch flecks
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFBBF24), Color(0xFFD97706), Color(0xFF92400E)),
                center = plateCenter + Offset(plateRadius * 0.22f, -plateRadius * 0.14f),
                radius = plateRadius * 0.42f
            ),
            topLeft = Offset(plateCenter.x - plateRadius * 0.04f, plateCenter.y - plateRadius * 0.42f),
            size = Size(plateRadius * 0.68f, plateRadius * 0.48f)
        )
        // Crispy kremesan golden flakes
        val flakeOffsets = listOf(
            Offset(0.12f, -0.26f),
            Offset(0.28f, -0.18f),
            Offset(0.42f, -0.24f),
            Offset(0.18f, -0.06f),
            Offset(0.36f, -0.04f)
        )
        flakeOffsets.forEach { rel ->
            drawCircle(
                color = HayukLogoGold,
                radius = plateRadius * 0.055f,
                center = plateCenter + Offset(plateRadius * rel.x, plateRadius * rel.y)
            )
        }

        // Stone Cobek Fiery Red Sambal Bowl
        val sambalCenter = plateCenter + Offset(plateRadius * 0.14f, plateRadius * 0.36f)
        drawCircle(
            color = Color(0xFF2B2523),
            radius = plateRadius * 0.30f,
            center = sambalCenter
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFF3B1F), SambalRed, Color(0xFF991B1B)),
                center = sambalCenter,
                radius = plateRadius * 0.23f
            ),
            radius = plateRadius * 0.23f,
            center = sambalCenter
        )
        // Fresh lime wedge & chili seed highlights
        drawCircle(
            color = Color(0xFF84CC16),
            radius = plateRadius * 0.08f,
            center = sambalCenter + Offset(plateRadius * 0.11f, -plateRadius * 0.08f)
        )
        // Fresh cucumber slices
        drawCircle(
            color = Color(0xFF86EFAC),
            radius = plateRadius * 0.14f,
            center = plateCenter + Offset(-plateRadius * 0.32f, plateRadius * 0.34f)
        )
        drawCircle(
            color = Color(0xFF4ADE80),
            radius = plateRadius * 0.14f,
            center = plateCenter + Offset(-plateRadius * 0.32f, plateRadius * 0.34f),
            style = Stroke(width = 3f)
        )

        // Subtle rising steam wisps above warm food
        val steam1 = Path().apply {
            moveTo(plateCenter.x - plateRadius * 0.25f, plateCenter.y - plateRadius * 0.56f)
            cubicTo(
                plateCenter.x - plateRadius * 0.32f, plateCenter.y - plateRadius * 0.74f,
                plateCenter.x - plateRadius * 0.15f, plateCenter.y - plateRadius * 0.88f,
                plateCenter.x - plateRadius * 0.24f, plateCenter.y - plateRadius * 1.04f
            )
        }
        drawPath(
            path = steam1,
            color = PureWhite.copy(alpha = 0.22f),
            style = Stroke(width = 4f, cap = StrokeCap.Round)
        )

        // Editorial warm dark gradient scrim on left side so white headline stays ultra crisp
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF160B0A).copy(alpha = 0.94f),
                    Color(0xFF1F0C0C).copy(alpha = 0.80f),
                    Color(0xFF1F0C0C).copy(alpha = 0.14f)
                )
            )
        )
    }
}

@Composable
fun CrispyPlatterCravingArtwork(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))
            )
        )
        // Ceramic plate
        val center = Offset(w * 0.5f, h * 0.54f)
        val r = minOf(w, h) * 0.36f
        drawCircle(color = PureWhite, radius = r, center = center)
        drawCircle(color = BananaLeafGreen, radius = r * 0.80f, center = center)
        // Crispy golden fried duck / chicken pieces
        drawOval(
            color = Color(0xFFD97706),
            topLeft = Offset(center.x - r * 0.62f, center.y - r * 0.42f),
            size = Size(r * 0.84f, r * 0.60f)
        )
        drawOval(
            color = CrispyGold,
            topLeft = Offset(center.x - r * 0.10f, center.y - r * 0.52f),
            size = Size(r * 0.72f, r * 0.52f)
        )
        // Fiery sambal drop
        drawCircle(
            color = SambalRed,
            radius = r * 0.26f,
            center = Offset(center.x + r * 0.12f, center.y + r * 0.32f)
        )
        // Fresh cucumber slice
        drawCircle(
            color = Color(0xFF4ADE80),
            radius = r * 0.19f,
            center = Offset(center.x - r * 0.36f, center.y + r * 0.30f)
        )
    }
}

@Composable
fun FierySambalCravingArtwork(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))
            )
        )
        val center = Offset(w * 0.5f, h * 0.54f)
        val r = minOf(w, h) * 0.36f
        // Stone cobek mortar
        drawCircle(color = Color(0xFF2D2727), radius = r, center = center)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFF3D1F), SambalRed, Color(0xFF991B1B)),
                center = center,
                radius = r * 0.78f
            ),
            radius = r * 0.78f,
            center = center
        )
        // Crispy ayam geprek piece topped on sambal
        drawOval(
            color = Color(0xFFF59E0B),
            topLeft = Offset(center.x - r * 0.46f, center.y - r * 0.44f),
            size = Size(r * 0.92f, r * 0.58f)
        )
        // Red chili highlights
        drawOval(
            color = HayukRed,
            topLeft = Offset(center.x - r * 0.32f, center.y + r * 0.18f),
            size = Size(r * 0.56f, r * 0.22f)
        )
    }
}

@Composable
fun ChilledDrinksCravingArtwork(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3))
            )
        )
        // Two appetizing chilled beverage cups side by side
        val cup1Left = w * 0.22f
        val cupW = w * 0.26f
        val cupH = h * 0.56f
        val cupTop = h * 0.24f

        // Strawberry/Smoothie cup
        drawLine(
            color = HayukRed,
            start = Offset(cup1Left + cupW * 0.6f, cupTop - h * 0.12f),
            end = Offset(cup1Left + cupW * 0.45f, cupTop + cupH * 0.3f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFF0F5), Color(0xFFEC407A), Color(0xFFAD1457)),
                startY = cupTop,
                endY = cupTop + cupH
            ),
            topLeft = Offset(cup1Left, cupTop),
            size = Size(cupW, cupH),
            cornerRadius = CornerRadius(12f, 12f)
        )

        // Rich Choco / Brown Sugar Milk Tea cup
        val cup2Left = w * 0.52f
        val cup2Top = h * 0.20f
        drawLine(
            color = HayukLogoGold,
            start = Offset(cup2Left + cupW * 0.6f, cup2Top - h * 0.10f),
            end = Offset(cup2Left + cupW * 0.45f, cup2Top + cupH * 0.3f),
            strokeWidth = 5f,
            cap = StrokeCap.Round
        )
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFF8EE), CremaGold, Color(0xFF4E342E)),
                startY = cup2Top,
                endY = cup2Top + cupH
            ),
            topLeft = Offset(cup2Left, cup2Top),
            size = Size(cupW, cupH + h * 0.04f),
            cornerRadius = CornerRadius(12f, 12f)
        )
    }
}

@Composable
fun KopiKulangoProductArtwork(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Warm roasted espresso studio background with crimson & gold highlight (no blue!)
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(EspressoDark, EspressoWarm, Color(0xFF4D0C10)),
                start = Offset.Zero,
                end = Offset(w, h)
            )
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(CremaGold.copy(alpha = 0.42f), Color.Transparent),
                center = Offset(w * 0.52f, h * 0.48f),
                radius = w * 0.48f
            ),
            center = Offset(w * 0.52f, h * 0.48f),
            radius = w * 0.48f
        )

        // Crystal cup silhouette
        val cupLeft = w * 0.26f
        val cupRight = w * 0.74f
        val cupTop = h * 0.18f
        val cupBottom = h * 0.84f
        val cupBottomInset = w * 0.06f

        val cupPath = Path().apply {
            moveTo(cupLeft, cupTop)
            lineTo(cupRight, cupTop)
            lineTo(cupRight - cupBottomInset, cupBottom)
            lineTo(cupLeft + cupBottomInset, cupBottom)
            close()
        }

        // Cup shadow
        drawOval(
            color = Color.Black.copy(alpha = 0.45f),
            topLeft = Offset(cupLeft + cupBottomInset - 6f, cupBottom - 4f),
            size = Size((cupRight - cupLeft) - cupBottomInset * 2 + 12f, 16f)
        )

        // Layered palm sugar + milk + espresso gradient inside cup
        drawPath(
            path = cupPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFF6EFE6),
                    Color(0xFFD99B4E),
                    Color(0xFF5A311D),
                    Color(0xFF2B140C)
                ),
                startY = cupTop,
                endY = cupBottom
            )
        )

        // Espresso swirl band
        val swirlPath = Path().apply {
            moveTo(cupLeft + 8f, h * 0.40f)
            cubicTo(w * 0.42f, h * 0.32f, w * 0.58f, h * 0.52f, cupRight - 8f, h * 0.42f)
        }
        drawPath(
            path = swirlPath,
            color = Color(0xFF3C1D11).copy(alpha = 0.75f),
            style = Stroke(width = 14f, cap = StrokeCap.Round)
        )

        // Ice cube glints
        drawRoundRect(
            color = PureWhite.copy(alpha = 0.32f),
            topLeft = Offset(w * 0.36f, h * 0.24f),
            size = Size(w * 0.13f, w * 0.13f),
            cornerRadius = CornerRadius(8f, 8f)
        )
        drawRoundRect(
            color = PureWhite.copy(alpha = 0.25f),
            topLeft = Offset(w * 0.52f, h * 0.27f),
            size = Size(w * 0.11f, w * 0.11f),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // Glass rim highlight
        drawLine(
            color = PureWhite.copy(alpha = 0.85f),
            start = Offset(cupLeft - 3f, cupTop),
            end = Offset(cupRight + 3f, cupTop),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )

        // Roasted coffee beans at base
        drawOval(
            color = Color(0xFF23110A),
            topLeft = Offset(w * 0.16f, h * 0.78f),
            size = Size(w * 0.10f, h * 0.07f)
        )
        drawOval(
            color = Color(0xFF2D160D),
            topLeft = Offset(w * 0.75f, h * 0.75f),
            size = Size(w * 0.09f, h * 0.065f)
        )
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun BrandLogoVisual(
    brand: BrandConfig,
    size: Dp = 76.dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val logoTrimmed = brand.logo.trim()
    val isRemoteUrl = logoTrimmed.startsWith("https://", ignoreCase = true) ||
        logoTrimmed.startsWith("http://", ignoreCase = true)

    val localDrawableId = remember(brand.brandId, logoTrimmed) {
        if (isRemoteUrl) {
            0
        } else {
            val candidateName = if (logoTrimmed.isNotEmpty() && !logoTrimmed.startsWith("placeholder_")) {
                logoTrimmed
            } else {
                "logo_${brand.brandId}"
            }
            context.resources.getIdentifier(candidateName, "drawable", context.packageName)
        }
    }

    val accent = remember(brand.accentColorHex) {
        runCatching { Color(android.graphics.Color.parseColor(brand.accentColorHex)) }
            .getOrElse { HayukRed }
    }

    when {
        isRemoteUrl -> {
            AsyncImage(
                model = logoTrimmed,
                contentDescription = stringResource(R.string.cd_brand_logo, brand.brandName),
                modifier = modifier
                    .size(size)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Fit
            )
        }

        localDrawableId != 0 -> {
            Image(
                painter = painterResource(id = localDrawableId),
                contentDescription = stringResource(R.string.cd_brand_logo, brand.brandName),
                modifier = modifier
                    .size(size)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Fit
            )
        }

        brand.brandId == "bebek_bumbu_hitam_barbara" -> {
            PlaceholderBrandLogo(
                monogram = brand.monogram.ifBlank { "BB" },
                size = size,
                modifier = modifier
            )
        }

        else -> {
            val icon = remember(brand.brandId) { brandMotifIcon(brand.brandId, brand.category) }
            Box(
                modifier = modifier
                    .size(size)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                accent.copy(alpha = 0.16f),
                                accent.copy(alpha = 0.06f)
                            )
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        color = accent.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(size * 0.46f)
                            .clip(CircleShape)
                            .background(accent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(R.string.cd_brand_logo, brand.brandName),
                            tint = PureWhite,
                            modifier = Modifier.size(size * 0.26f)
                        )
                    }
                    Text(
                        text = brand.monogram.ifBlank { brand.brandName.take(2).uppercase() },
                        fontFamily = OutfitFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = (size.value * 0.15f).sp,
                        letterSpacing = 0.8.sp,
                        color = CarbonBlack,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceholderBrandLogo(
    monogram: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(20.dp))
            .background(HayukRedSoft),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = HayukRed.copy(alpha = 0.45f),
                style = Stroke(
                    width = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                ),
                cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(6.dp)
        ) {
            Text(
                text = monogram,
                fontFamily = OutfitFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = (size.value * 0.28f).sp,
                color = HayukRedDeep
            )
            Text(
                text = "LOGO",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                color = SlateText,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun brandMotifIcon(brandId: String, category: BrandCategory): ImageVector {
    return when (brandId) {
        "kopi_kulango" -> Icons.Rounded.Coffee
        "penyetan_cak_suro", "sambelan_cak_lam", "geprek_sarasa_jagir" -> Icons.Rounded.Whatshot
        "sego_tempong_mbok_woro", "pawon_ndeso",
        "riceboss_express", "warung_tahu_tektok", "nasi_goreng_salora" -> Icons.Rounded.RiceBowl
        "bebek_goreng_cak_kris", "bebek_purnama_jawara", "bebek_pencit_cak_mad" -> Icons.Rounded.SetMeal
        "kriuk_bro_chicken", "siram_crispy_chicken",
        "burger_barbaron", "al_jazeera_kebab" -> Icons.Rounded.Fastfood
        "mamas_pizza_jagir" -> Icons.Rounded.LocalPizza
        "roti_john_pahlawan", "foca_sanwitch",
        "roti_bakar_bandung_mas_joo", "gogs_keju" -> Icons.Rounded.BakeryDining
        "kalista_cake_snack" -> Icons.Rounded.Cake
        "chocorush" -> Icons.Rounded.LocalCafe
        "torue_drink" -> Icons.Rounded.LocalBar
        "moutea" -> Icons.Rounded.EmojiFoodBeverage
        "moo_milky" -> Icons.Rounded.LocalDrink
        "moo_juice_smoothies" -> Icons.Rounded.Spa
        else -> if (category == BrandCategory.BEVERAGE) {
            Icons.Rounded.LocalDrink
        } else {
            Icons.Rounded.Restaurant
        }
    }
}
