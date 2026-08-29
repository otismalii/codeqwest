package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberGold
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceLight
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberBorder,
    glowColor: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Card(
        modifier = modifier
            .clip(shape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = CyberSurface),
        border = BorderStroke(1.5.dp, borderColor)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun GlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accentColor: Color = NeonCyan,
    icon: ImageVector? = null,
    testTag: String = "glow_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(54.dp)
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = accentColor,
            contentColor = Color(0xFF00222B),
            disabledContainerColor = CyberSurfaceLight,
            disabledContentColor = TextSecondary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

@Composable
fun CyberProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = NeonEmerald,
    height: Dp = 10.dp
) {
    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .clip(RoundedCornerShape(height / 2))
            .background(CyberSurfaceLight)
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .clip(RoundedCornerShape(height / 2))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(color.copy(alpha = 0.8f), color)
                    )
                )
        )
    }
}

@Composable
fun CadetAvatarView(
    avatarId: String,
    size: Dp = 56.dp,
    modifier: Modifier = Modifier,
    hasGlow: Boolean = true
) {
    val borderColor = when (avatarId) {
        "byte_pup" -> NeonCyan
        "pixel_bot" -> NeonEmerald
        "nova_explorer" -> CyberGold
        else -> NeonCyan
    }

    val emoji = when (avatarId) {
        "byte_pup" -> "🐶⚡"
        "pixel_bot" -> "🤖✨"
        "nova_explorer" -> "🧑‍🚀🚀"
        "shadow_hacker" -> "🦊🛡️"
        else -> "🤖"
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(CyberSurfaceVariant)
            .border(2.dp, borderColor, CircleShape)
    ) {
        Text(
            text = emoji,
            fontSize = (size.value * 0.42f).sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StarRatingRow(
    stars: Int,
    maxStars: Int = 3,
    size: Dp = 22.dp
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 1..maxStars) {
            val filled = i <= stars
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star $i",
                tint = if (filled) CyberGold else CyberSurfaceLight,
                modifier = Modifier.size(size)
            )
        }
    }
}

@Composable
fun CelebrationDialog(
    title: String,
    xpGained: Int,
    conceptLearned: String,
    onContinue: () -> Unit
) {
    val scaleAnim = remember { Animatable(0.7f) }
    LaunchedEffect(Unit) {
        scaleAnim.animateTo(1.0f, animationSpec = tween(350, easing = FastOutSlowInEasing))
    }

    Dialog(onDismissRequest = onContinue) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CyberSurface,
            border = BorderStroke(2.dp, NeonEmerald),
            modifier = Modifier
                .fillMaxWidth()
                .scale(scaleAnim.value)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(NeonEmerald.copy(alpha = 0.15f))
                        .border(2.dp, NeonEmerald, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonEmerald,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Mission Passed with Cadet Distinction!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // XP & Concept Badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CyberSurfaceVariant)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+$xpGained Cadet XP",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CyberGold
                        )
                    }
                    StarRatingRow(stars = 3)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Concept Mastered: $conceptLearned",
                    style = MaterialTheme.typography.bodySmall,
                    color = NeonCyan,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                GlowButton(
                    text = "NEXT QUEST",
                    onClick = onContinue,
                    accentColor = NeonEmerald,
                    testTag = "celebration_continue_button"
                )
            }
        }
    }
}

@Composable
fun ScaffoldedHintBox(
    hint: String,
    onDismiss: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberSurfaceLight),
        border = BorderStroke(1.dp, CyberGold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = "Hint",
                tint = CyberGold,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
            }
        }
    }
}

@Composable
fun ParentPinDialog(
    storedHash: String?,
    onVerified: () -> Unit,
    onDismiss: () -> Unit,
    onBiometricRequest: (() -> Unit)? = null
) {
    var enteredPin by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CyberSurface,
            border = BorderStroke(1.5.dp, CyberBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = CyberGold,
                    modifier = Modifier.size(36.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Guardian Access Lock",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )

                Text(
                    text = "Enter 4-digit Parent PIN or verify biometrics",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = enteredPin,
                    onValueChange = {
                        if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                            enteredPin = it
                            errorMsg = null
                        }
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("parent_pin_input"),
                    placeholder = { Text("••••", color = TextSecondary) }
                )

                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = errorMsg!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(20.dp))

                GlowButton(
                    text = "UNLOCK DASHBOARD",
                    onClick = {
                        val isDefaultOrMatching = if (storedHash == null) {
                            enteredPin == "1234" || enteredPin.length == 4
                        } else {
                            com.example.security.BiometricAuthManager.verifyPin(enteredPin, storedHash)
                        }

                        if (isDefaultOrMatching) {
                            onVerified()
                        } else {
                            errorMsg = "Incorrect PIN. Try default: 1234"
                        }
                    },
                    accentColor = CyberGold,
                    testTag = "unlock_pin_button"
                )

                if (onBiometricRequest != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = onBiometricRequest,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, NeonCyan)
                    ) {
                        Icon(Icons.Default.Fingerprint, contentDescription = null, tint = NeonCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Use Biometrics / Fingerprint", color = NeonCyan)
                    }
                }
            }
        }
    }
}
