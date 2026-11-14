package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.BrandConfig
import com.example.ui.theme.CarbonBlack
import com.example.ui.theme.HayukRed
import com.example.ui.theme.HayukRedSoft
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SlateText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComingSoonBottomSheet(
    brand: BrandConfig,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PureWhite,
        contentColor = CarbonBlack,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        modifier = Modifier.testTag("coming_soon_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BrandLogoVisual(brand = brand, size = 84.dp)

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = HayukRedSoft,
                shape = RoundedCornerShape(50),
            ) {
                Text(
                    text = stringResource(R.string.webshop_coming_soon),
                    style = MaterialTheme.typography.labelSmall,
                    color = HayukRed,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = brand.brandName,
                style = MaterialTheme.typography.headlineSmall,
                color = CarbonBlack,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.webshop_coming_soon_desc, brand.brandName),
                style = MaterialTheme.typography.bodyLarge,
                color = SlateText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CarbonBlack,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("coming_soon_close_button")
            ) {
                Text(
                    text = stringResource(R.string.action_close),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
