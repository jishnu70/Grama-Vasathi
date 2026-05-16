package com.gramavasathi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.GoogleFont.Provider
import androidx.compose.ui.unit.sp
import com.gramavasathi.R

private val provider = Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val lora = FontFamily(Font(googleFont = GoogleFont("Lora"), fontProvider = provider))
private val nunito = FontFamily(Font(googleFont = GoogleFont("Nunito"), fontProvider = provider))

val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = lora, fontSize = 36.sp),
    headlineMedium = TextStyle(fontFamily = lora),
    titleLarge = TextStyle(fontFamily = lora),
    bodyLarge = TextStyle(fontFamily = nunito),
    bodyMedium = TextStyle(fontFamily = nunito),
    labelMedium = TextStyle(fontFamily = nunito)
)
