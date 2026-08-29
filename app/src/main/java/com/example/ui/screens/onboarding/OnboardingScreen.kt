package com.example.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CadetAvatarView
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberProgressBar
import com.example.ui.components.GlowButton
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCrimson
import com.example.ui.theme.CyberGold
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceLight
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun OnboardingScreen(
    onCompleteOnboarding: (callSign: String, avatarId: String, parentPin: String?, isBiometricEnabled: Boolean) -> Unit
) {
    var step by remember { mutableIntStateOf(1) } // 1: Briefing, 2: Identity, 3: Calibration, 4: Security Gate

    var callSign by remember { mutableStateOf("PixelNova") }
    var selectedAvatar by remember { mutableStateOf("byte_pup") }
    var parentPin by remember { mutableStateOf("1234") }
    var enableBiometrics by remember { mutableStateOf(false) }

    val aliasPresets = listOf("PixelNova", "ByteRanger", "AstroCadet", "CyberFox", "ShadowSpark", "QuantumKid")

    Scaffold(
        containerColor = CyberBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "CADET INITIATION: STEP $step OF 4",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NeonCyan
                )
                Text(
                    text = "${(step * 25)}%",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = NeonEmerald
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            CyberProgressBar(progress = step / 4f, color = NeonCyan, height = 8.dp)

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "onboarding_steps"
            ) { currentStep ->
                when (currentStep) {
                    1 -> Step1Briefing(onNext = { step = 2 })
                    2 -> Step2Identity(
                        callSign = callSign,
                        onCallSignChange = { callSign = it },
                        selectedAvatar = selectedAvatar,
                        onAvatarSelect = { selectedAvatar = it },
                        onRandomize = { callSign = aliasPresets.random() },
                        onNext = { step = 3 }
                    )
                    3 -> Step3Calibration(onNext = { step = 4 })
                    4 -> Step4SecurityGate(
                        parentPin = parentPin,
                        onPinChange = { parentPin = it },
                        enableBiometrics = enableBiometrics,
                        onBiometricsToggle = { enableBiometrics = it },
                        onFinish = {
                            onCompleteOnboarding(callSign, selectedAvatar, parentPin, enableBiometrics)
                        }
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------
// STEP 1: WELCOME & CADET BRIEFING
// ----------------------------------------------------
@Composable
private fun Step1Briefing(onNext: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Mascot Illustration Banner
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(NeonCyan.copy(alpha = 0.1f))
                .border(2.dp, NeonCyan, CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_cadet_mascot),
                contentDescription = "Byte the Cyber Pup",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Welcome to CodeQuest!",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Hi Cadet! I'm Byte the Cyber-Pup. Together, we'll explore computer hardware, conquer logic puzzles, bust glitches, and code real algorithms!",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        CyberCard(borderColor = CyberBorder) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                BriefingPill(emoji = "🛡️", title = "100% Offline & Private", desc = "Zero trackers, safe local data vault")
                BriefingPill(emoji = "⚡", title = "Adaptive Missions", desc = "Personalized quests that adapt to your speed")
                BriefingPill(emoji = "🏆", title = "Cyber Badges", desc = "Earn cool hardware chips and trophies")
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        GlowButton(
            text = "INITIALIZE CADET TERMINAL ➔",
            onClick = onNext,
            accentColor = NeonCyan,
            icon = Icons.Default.RocketLaunch,
            testTag = "onboarding_step1_next"
        )
    }
}

@Composable
private fun BriefingPill(emoji: String, title: String, desc: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 24.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Text(desc, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}

// ----------------------------------------------------
// STEP 2: CADET IDENTITY & AVATAR
// ----------------------------------------------------
@Composable
private fun Step2Identity(
    callSign: String,
    onCallSignChange: (String) -> Unit,
    selectedAvatar: String,
    onAvatarSelect: (String) -> Unit,
    onRandomize: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Choose Your Cadet Identity",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )

        Text(
            text = "Pick a cyber call-sign and your companion avatar:",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Avatar Choices
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AvatarChoiceCard(id = "byte_pup", label = "Byte Pup", emoji = "🐶⚡", isSelected = selectedAvatar == "byte_pup") { onAvatarSelect("byte_pup") }
            AvatarChoiceCard(id = "pixel_bot", label = "Pixel Bot", emoji = "🤖✨", isSelected = selectedAvatar == "pixel_bot") { onAvatarSelect("pixel_bot") }
            AvatarChoiceCard(id = "nova_explorer", label = "Nova Cadet", emoji = "🧑‍🚀🚀", isSelected = selectedAvatar == "nova_explorer") { onAvatarSelect("nova_explorer") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Call-Sign Input
        CyberCard(borderColor = NeonCyan) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("CADET CALL-SIGN:", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = NeonCyan)
                    IconButton(onClick = onRandomize) {
                        Icon(Icons.Default.Refresh, contentDescription = "Randomize", tint = CyberGold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = callSign,
                    onValueChange = { if (it.length <= 16) onCallSignChange(it) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("callsign_input")
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        GlowButton(
            text = "CALIBRATE TERMINAL ➔",
            enabled = callSign.isNotBlank(),
            onClick = onNext,
            accentColor = NeonCyan,
            testTag = "onboarding_step2_next"
        )
    }
}

@Composable
private fun AvatarChoiceCard(id: String, label: String, emoji: String, isSelected: Boolean, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .size(96.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(containerColor = if (isSelected) NeonCyan.copy(alpha = 0.15f) else CyberSurfaceVariant),
        border = BorderStroke(2.dp, if (isSelected) NeonCyan else CyberBorder)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = if (isSelected) NeonCyan else TextSecondary)
        }
    }
}

// ----------------------------------------------------
// STEP 3: INTERACTIVE TERMINAL CALIBRATION MINI-GAME
// ----------------------------------------------------
@Composable
private fun Step3Calibration(onNext: () -> Unit) {
    val powerCells = remember { mutableStateListOf(false, false, false) }
    val isFullyCharged = powerCells.all { it }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Terminal Calibration",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )

        Text(
            text = "Tap all 3 plasma power cells to charge the reactor to 100%:",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Reactor HUD
        CyberCard(borderColor = if (isFullyCharged) NeonEmerald else CyberGold) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = if (isFullyCharged) NeonEmerald else CyberGold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("CADET CORE REACTOR", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    }
                    Text(
                        text = if (isFullyCharged) "ONLINE (100%)" else "${(powerCells.count { it } * 33.3f).toInt()}%",
                        color = if (isFullyCharged) NeonEmerald else CyberGold,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 0..2) {
                        val charged = powerCells[i]
                        Card(
                            modifier = Modifier
                                .size(78.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { powerCells[i] = true },
                            colors = CardDefaults.cardColors(containerColor = if (charged) NeonEmerald.copy(alpha = 0.25f) else CyberSurfaceVariant),
                            border = BorderStroke(2.dp, if (charged) NeonEmerald else CyberBorder)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(if (charged) "⚡" else "🔋", fontSize = 28.sp)
                                Text(if (charged) "ONLINE" else "TAP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (charged) NeonEmerald else TextTertiary)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        GlowButton(
            text = if (isFullyCharged) "SYSTEMS READY ➔" else "CHARGE ALL 3 CELLS",
            enabled = isFullyCharged,
            onClick = onNext,
            accentColor = NeonEmerald,
            testTag = "onboarding_step3_next"
        )
    }
}

// ----------------------------------------------------
// STEP 4: GUARDIAN SECURITY GATE (PIN & BIOMETRIC)
// ----------------------------------------------------
@Composable
private fun Step4SecurityGate(
    parentPin: String,
    onPinChange: (String) -> Unit,
    enableBiometrics: Boolean,
    onBiometricsToggle: (Boolean) -> Unit,
    onFinish: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Guardian Security Setup",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )

        Text(
            text = "Set a 4-digit Parent PIN and enable fingerprint to protect the Parent Dashboard:",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        CyberCard(borderColor = CyberGold) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = CyberGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("4-DIGIT PARENT PIN", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = parentPin,
                    onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) onPinChange(it) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberGold,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("guardian_pin_input"),
                    placeholder = { Text("Default: 1234", color = TextSecondary) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Biometric Toggle Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Fingerprint, contentDescription = null, tint = NeonCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Biometric Fingerprint", style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                            Text("Unlock parent gate instantly", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }

                    Switch(
                        checked = enableBiometrics,
                        onCheckedChange = onBiometricsToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonCyan,
                            checkedTrackColor = NeonCyan.copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        GlowButton(
            text = "LAUNCH CODEQUEST HUB 🚀",
            enabled = parentPin.length == 4,
            onClick = onFinish,
            accentColor = NeonEmerald,
            testTag = "onboarding_finish_button"
        )
    }
}
