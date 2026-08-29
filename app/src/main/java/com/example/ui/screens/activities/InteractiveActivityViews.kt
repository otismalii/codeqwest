package com.example.ui.screens.activities

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ActivityEntity
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberProgressBar
import com.example.ui.components.GlowButton
import com.example.ui.components.ScaffoldedHintBox
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

// ==========================================
// 1. INPUT VS OUTPUT SORTER
// ==========================================
@Composable
fun InputOutputSorterView(
    activity: ActivityEntity,
    onComplete: (score: Float, hints: Int, errors: Int) -> Unit
) {
    data class DeviceItem(val name: String, val icon: ImageVector, val isInput: Boolean, val hint: String)

    val devices = remember {
        listOf(
            DeviceItem("Microphone", Icons.Default.Mic, true, "Speaks audio into the PC"),
            DeviceItem("Screen / Monitor", Icons.Default.Tv, false, "Displays graphics out to you"),
            DeviceItem("Keyboard", Icons.Default.Keyboard, true, "Sends key presses into the PC"),
            DeviceItem("Speakers", Icons.Default.VolumeUp, false, "Outputs sound waves out to the room"),
            DeviceItem("Webcam", Icons.Default.Videocam, true, "Captures video feed into the PC"),
            DeviceItem("Printer", Icons.Default.Print, false, "Outputs ink and paper prints")
        )
    }

    val selectedBucket = remember { mutableStateMapOf<String, String>() } // device name -> "INPUT" or "OUTPUT"
    var hintVisible by remember { mutableStateOf(false) }
    var currentHint by remember { mutableStateOf("") }
    var hintsUsed by remember { mutableIntStateOf(0) }
    var errorCount by remember { mutableIntStateOf(0) }
    var feedbackMsg by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Sort all 6 devices into Input (Feeding data IN) or Output (Sending data OUT):",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (hintVisible) {
            ScaffoldedHintBox(hint = currentHint, onDismiss = { hintVisible = false })
        }

        LazyColumn(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(devices) { device ->
                val choice = selectedBucket[device.name]
                CyberCard(
                    borderColor = when (choice) {
                        "INPUT" -> NeonCyan
                        "OUTPUT" -> CyberGold
                        else -> CyberBorder
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(device.icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(device.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        }

                        IconButton(onClick = {
                            currentHint = device.hint
                            hintVisible = true
                            hintsUsed++
                        }) {
                            Icon(Icons.Default.Lightbulb, contentDescription = "Hint", tint = CyberGold)
                        }

                        // Input choice button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (choice == "INPUT") NeonCyan else CyberSurfaceVariant)
                                .clickable { selectedBucket[device.name] = "INPUT" }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text("INPUT", color = if (choice == "INPUT") Color(0xFF00222B) else TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Output choice button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (choice == "OUTPUT") CyberGold else CyberSurfaceVariant)
                                .clickable { selectedBucket[device.name] = "OUTPUT" }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text("OUTPUT", color = if (choice == "OUTPUT") Color(0xFF2B2000) else TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        if (feedbackMsg != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(feedbackMsg!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlowButton(
            text = "VERIFY CLASSIFICATION",
            enabled = selectedBucket.size == devices.size,
            onClick = {
                var errors = 0
                devices.forEach { device ->
                    val chosen = selectedBucket[device.name]
                    val correct = if (device.isInput) "INPUT" else "OUTPUT"
                    if (chosen != correct) errors++
                }

                if (errors == 0) {
                    onComplete(1.0f, hintsUsed, errorCount)
                } else {
                    errorCount += errors
                    feedbackMsg = "$errors devices are misplaced! Remember: Input gives data TO the computer, Output delivers data OUT."
                }
            },
            accentColor = NeonEmerald,
            testTag = "verify_io_button"
        )
    }
}

// ==========================================
// 2. PASSWORD POWER SHIELD
// ==========================================
@Composable
fun PasswordShieldView(
    activity: ActivityEntity,
    onComplete: (score: Float, hints: Int, errors: Int) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var hintVisible by remember { mutableStateOf(false) }
    var hintsUsed by remember { mutableIntStateOf(0) }

    val hasLength = password.length >= 8
    val hasUpper = password.any { it.isUpperCase() }
    val hasNumber = password.any { it.isDigit() }
    val hasSymbol = password.any { !it.isLetterOrDigit() }

    val strengthScore = (if (hasLength) 0.25f else 0f) +
            (if (hasUpper) 0.25f else 0f) +
            (if (hasNumber) 0.25f else 0f) +
            (if (hasSymbol) 0.25f else 0f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Forge a fortress-level cadet password. Fulfill all 4 cyber requirements to unlock maximum shield power!",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Shield Strength Meter
        CyberCard(borderColor = if (strengthScore >= 1.0f) NeonEmerald else CyberBorder) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = if (strengthScore >= 1.0f) NeonEmerald else NeonCyan,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (strengthScore >= 1.0f) "SHIELD AT 100% (MAX)" else "SHIELD: ${(strengthScore * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }

                    IconButton(onClick = {
                        hintVisible = true
                        hintsUsed++
                    }) {
                        Icon(Icons.Default.Lightbulb, contentDescription = "Hint", tint = CyberGold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                CyberProgressBar(
                    progress = strengthScore,
                    color = if (strengthScore >= 1.0f) NeonEmerald else CyberGold,
                    height = 12.dp
                )
            }
        }

        if (hintVisible) {
            Spacer(modifier = Modifier.height(8.dp))
            ScaffoldedHintBox(
                hint = "Try combining a cool word + a Capital letter + numbers + symbols! E.g. 'Space#Rover99'",
                onDismiss = { hintVisible = false }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter your test password", color = TextSecondary) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = CyberBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("password_shield_input")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Requirement Checklist
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            RequirementRow("At least 8 characters long", hasLength)
            RequirementRow("Contains an UPPERCASE letter (A-Z)", hasUpper)
            RequirementRow("Contains a NUMBER (0-9)", hasNumber)
            RequirementRow("Contains a SPECIAL SYMBOL (!, @, #, $, %)", hasSymbol)
        }

        Spacer(modifier = Modifier.height(24.dp))

        GlowButton(
            text = "ACTIVATE POWER SHIELD",
            enabled = strengthScore >= 1.0f,
            onClick = {
                onComplete(1.0f, hintsUsed, 0)
            },
            accentColor = NeonEmerald,
            testTag = "activate_shield_button"
        )
    }
}

@Composable
private fun RequirementRow(text: String, isMet: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isMet) Icons.Default.CheckCircle else Icons.Default.Close,
            contentDescription = null,
            tint = if (isMet) NeonEmerald else CyberSurfaceLight,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isMet) TextPrimary else TextSecondary
        )
    }
}

// ==========================================
// 3. DIGITAL FOOTPRINT CLASSIFIER
// ==========================================
@Composable
fun DigitalFootprintView(
    activity: ActivityEntity,
    onComplete: (score: Float, hints: Int, errors: Int) -> Unit
) {
    data class Scenario(val text: String, val isSafe: Boolean, val reason: String)

    val scenarios = remember {
        listOf(
            Scenario("Posting your home street address and phone number online", false, "Personal address and phone numbers must stay secret!"),
            Scenario("Sharing a digital comic book drawing you made yourself", true, "Artworks and creative projects are safe to share."),
            Scenario("Sending your gaming account password to a friend in chat", false, "Never share passwords with anyone except parents."),
            Scenario("Writing a polite review about your favorite space science book", true, "Book reviews and hobby chats are safe and fun!")
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var errors by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf<String?>(null) }
    var isPassed by remember { mutableStateOf(false) }

    val currentScenario = scenarios[currentIndex]

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Scenario ${currentIndex + 1} of ${scenarios.size}:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = NeonCyan
        )

        Spacer(modifier = Modifier.height(8.dp))

        CyberCard(borderColor = CyberBorder) {
            Column {
                Text(
                    text = currentScenario.text,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        if (feedback != null) {
            Spacer(modifier = Modifier.height(12.dp))
            CyberCard(borderColor = if (isPassed) NeonEmerald else CyberCrimson) {
                Text(feedback!!, color = if (isPassed) NeonEmerald else CyberCrimson, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    if (currentScenario.isSafe) {
                        feedback = "Correct! " + currentScenario.reason
                        isPassed = true
                        if (currentIndex < scenarios.size - 1) {
                            currentIndex++
                        } else {
                            onComplete(1.0f, 0, errors)
                        }
                    } else {
                        errors++
                        isPassed = false
                        feedback = "Not Safe! " + currentScenario.reason
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("safe_to_share_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald, contentColor = Color(0xFF003311))
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("SAFE TO SHARE", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    if (!currentScenario.isSafe) {
                        feedback = "Spot on! " + currentScenario.reason
                        isPassed = true
                        if (currentIndex < scenarios.size - 1) {
                            currentIndex++
                        } else {
                            onComplete(1.0f, 0, errors)
                        }
                    } else {
                        errors++
                        isPassed = false
                        feedback = "Actually Safe! " + currentScenario.reason
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .testTag("private_vault_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyberCrimson, contentColor = Color.White)
            ) {
                Icon(Icons.Default.Close, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("KEEP PRIVATE", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// 4. MOTHERBOARD BUILDER LAB
// ==========================================
@Composable
fun MotherboardBuilderView(
    activity: ActivityEntity,
    onComplete: (score: Float, hints: Int, errors: Int) -> Unit
) {
    data class Component(val id: String, val name: String, val role: String, val slotName: String)

    val components = remember {
        listOf(
            Component("cpu", "CPU Processor", "The Brain: Executes arithmetic & code", "Socket AM5 (Center)"),
            Component("ram", "RAM Memory Sticks", "Fast Cache: Holds open active apps", "DIMM Slots (Right)"),
            Component("ssd", "SSD Storage Drive", "Permanent Vault: Stores games & files", "M.2 NVMe Slot (Bottom)"),
            Component("gpu", "GPU Graphics Card", "Visual Power: Renders 3D frames", "PCIe x16 Lane (Left)")
        )
    }

    val installedComponents = remember { mutableStateListOf<String>() }
    var selectedComp by remember { mutableStateOf<Component?>(null) }
    var feedback by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Assemble the Cadet Supercomputer Motherboard! Tap each component and install it to power the system:",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Motherboard Circuit View
        CyberCard(borderColor = NeonCyan) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("MOTHERBOARD SYSTEM BUS", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = NeonCyan)
                    Text("${installedComponents.size} / ${components.size} ONLINE", color = NeonEmerald, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                components.forEach { comp ->
                    val isInstalled = installedComponents.contains(comp.id)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isInstalled) NeonEmerald.copy(alpha = 0.15f) else CyberSurfaceVariant)
                            .border(1.dp, if (isInstalled) NeonEmerald else CyberBorder, RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(comp.slotName, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text(if (isInstalled) "✔ ${comp.name} INSTALLED" else "EMPTY SOCKET", color = if (isInstalled) NeonEmerald else TextTertiary, fontWeight = FontWeight.Bold)
                        }

                        if (!isInstalled) {
                            Button(
                                onClick = {
                                    installedComponents.add(comp.id)
                                    if (installedComponents.size == components.size) {
                                        onComplete(1.0f, 0, 0)
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF00222B))
                            ) {
                                Text("SNAP IN", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. BINARY BITS SWITCH MATRIX
// ==========================================
@Composable
fun BinarySwitchView(
    activity: ActivityEntity,
    onComplete: (score: Float, hints: Int, errors: Int) -> Unit
) {
    val targetValues = remember { listOf(5, 10, 13, 15) }
    var roundIndex by remember { mutableIntStateOf(0) }
    var errors by remember { mutableIntStateOf(0) }

    val currentTarget = targetValues[roundIndex]

    var bit8 by remember { mutableStateOf(false) }
    var bit4 by remember { mutableStateOf(false) }
    var bit2 by remember { mutableStateOf(false) }
    var bit1 by remember { mutableStateOf(false) }

    val currentValue = (if (bit8) 8 else 0) +
            (if (bit4) 4 else 0) +
            (if (bit2) 2 else 0) +
            (if (bit1) 1 else 0)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Target Number: $currentTarget (Round ${roundIndex + 1}/${targetValues.size})",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = CyberGold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Computers talk in Binary (1=ON, 0=OFF). Flip the 4 bits to make the sum equal $currentTarget!",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Switches Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BitSwitchCard("8", bit8) { bit8 = !bit8 }
            BitSwitchCard("4", bit4) { bit4 = !bit4 }
            BitSwitchCard("2", bit2) { bit2 = !bit2 }
            BitSwitchCard("1", bit1) { bit1 = !bit1 }
        }

        Spacer(modifier = Modifier.height(20.dp))

        CyberCard(borderColor = if (currentValue == currentTarget) NeonEmerald else CyberBorder) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("CURRENT SUM: $currentValue", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Text(
                    text = "BINARY: ${if (bit8) "1" else "0"} ${if (bit4) "1" else "0"} ${if (bit2) "1" else "0"} ${if (bit1) "1" else "0"}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
                    color = NeonCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        GlowButton(
            text = "TRANSMIT BIT CODE",
            enabled = currentValue == currentTarget,
            onClick = {
                if (roundIndex < targetValues.size - 1) {
                    roundIndex++
                    bit8 = false
                    bit4 = false
                    bit2 = false
                    bit1 = false
                } else {
                    onComplete(1.0f, 0, errors)
                }
            },
            accentColor = NeonEmerald,
            testTag = "transmit_binary_button"
        )
    }
}

@Composable
private fun BitSwitchCard(weight: String, isOn: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier
            .width(72.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(containerColor = if (isOn) NeonEmerald.copy(alpha = 0.2f) else CyberSurfaceVariant),
        border = BorderStroke(2.dp, if (isOn) NeonEmerald else CyberBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(weight, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = if (isOn) NeonEmerald else TextSecondary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(if (isOn) "1 (ON)" else "0 (OFF)", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = if (isOn) NeonEmerald else TextTertiary)
        }
    }
}

// ==========================================
// 6. OS VS APPS COMMANDER
// ==========================================
@Composable
fun OsVsAppsView(
    activity: ActivityEntity,
    onComplete: (score: Float, hints: Int, errors: Int) -> Unit
) {
    data class Question(val task: String, val isOs: Boolean, val expl: String)

    val questions = remember {
        listOf(
            Question("Allocating RAM memory and managing battery power for all programs", true, "The OS coordinates hardware and power."),
            Question("Drawing pixel art and applying filters to a funny photo", false, "Photo editors are user applications."),
            Question("Deciding which window is visible on the screen and routing mouse clicks", true, "The OS manages display windows and system drivers."),
            Question("Racing a 3D supercar around a digital space track", false, "Video games are apps running on top of the OS.")
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var errors by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf<String?>(null) }

    val currentQ = questions[currentIndex]

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Task ${currentIndex + 1} of ${questions.size}:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = NeonCyan
        )

        Spacer(modifier = Modifier.height(10.dp))

        CyberCard {
            Text(currentQ.task, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        }

        if (feedback != null) {
            Spacer(modifier = Modifier.height(10.dp))
            CyberCard(borderColor = NeonEmerald) {
                Text(feedback!!, color = NeonEmerald, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    if (currentQ.isOs) {
                        feedback = "Correct! " + currentQ.expl
                        if (currentIndex < questions.size - 1) currentIndex++ else onComplete(1.0f, 0, errors)
                    } else {
                        errors++
                        feedback = "Incorrect! " + currentQ.expl
                    }
                },
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF00222B))
            ) {
                Text("OPERATING SYSTEM", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Button(
                onClick = {
                    if (!currentQ.isOs) {
                        feedback = "Spot on! " + currentQ.expl
                        if (currentIndex < questions.size - 1) currentIndex++ else onComplete(1.0f, 0, errors)
                    } else {
                        errors++
                        feedback = "Incorrect! " + currentQ.expl
                    }
                },
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyberGold, contentColor = Color(0xFF2B2000))
            ) {
                Text("APPLICATION (APP)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

// ==========================================
// 7. PHISHING & SCAM INSPECTOR
// ==========================================
@Composable
fun PhishingInspectorView(
    activity: ActivityEntity,
    onComplete: (score: Float, hints: Int, errors: Int) -> Unit
) {
    data class EmailMessage(val sender: String, val subject: String, val body: String, val isPhishing: Boolean, val clues: List<String>)

    val messages = remember {
        listOf(
            EmailMessage(
                sender = "free-robux-gems@giveaway999-win.xyz",
                subject = "🎉 YOU WON 1,000,000 FREE GEMS! CLICK NOW!",
                body = "Congratulations lucky cadet! Enter your username and password here right now to claim your prize before it expires in 5 minutes!",
                isPhishing = true,
                clues = listOf("Fake sender address", "Urgent pressure (5 minutes!)", "Asking for your secret password")
            ),
            EmailMessage(
                sender = "school-library@oakville-academy.org",
                subject = "Reminder: Your Science Book is due Friday",
                body = "Hello Cadet, just a reminder that 'Space Rovers V2' is due back at the school library on Friday. No action needed if already returned.",
                isPhishing = false,
                clues = listOf("Legitimate official school domain", "No request for credentials", "Polite reminder without panic")
            ),
            EmailMessage(
                sender = "security-alert@acc0unt-verify-fast.biz",
                subject = "URGENT: Your account was locked! Send credit card to unlock",
                body = "We detected suspicious activity. Please download this file virus.exe and send your parent credit card number immediately.",
                isPhishing = true,
                clues = listOf("Misspelled domain 'acc0unt'", "Asking for sensitive credit card numbers", "Suspicious .exe file download")
            )
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var errors by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf<String?>(null) }

    val currentMsg = messages[currentIndex]

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Cyber Sleuth Case ${currentIndex + 1} of ${messages.size}:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = NeonCyan
        )

        Spacer(modifier = Modifier.height(10.dp))

        CyberCard(borderColor = CyberBorder) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("FROM: ", color = TextSecondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(currentMsg.sender, color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(currentMsg.subject, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = CyberGold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(currentMsg.body, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
            }
        }

        if (feedback != null) {
            Spacer(modifier = Modifier.height(10.dp))
            CyberCard(borderColor = NeonEmerald) {
                Text(feedback!!, color = NeonEmerald, style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    if (!currentMsg.isPhishing) {
                        feedback = "Correct! " + currentMsg.clues.joinToString(", ")
                        if (currentIndex < messages.size - 1) currentIndex++ else onComplete(1.0f, 0, errors)
                    } else {
                        errors++
                        feedback = "Caution! This was a Phishing Scam: " + currentMsg.clues.joinToString(", ")
                    }
                },
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald, contentColor = Color(0xFF003311))
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("LEGITIMATE", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    if (currentMsg.isPhishing) {
                        feedback = "Great detective eye! " + currentMsg.clues.joinToString(", ")
                        if (currentIndex < messages.size - 1) currentIndex++ else onComplete(1.0f, 0, errors)
                    } else {
                        errors++
                        feedback = "Actually Legitimate: " + currentMsg.clues.joinToString(", ")
                    }
                },
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyberCrimson, contentColor = Color.White)
            ) {
                Icon(Icons.Default.Warning, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("PHISHING SCAM", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// 8. CODE BLOCK SEQUENCER (ROVER MAZE)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CodeBlockSequencerView(
    activity: ActivityEntity,
    onComplete: (score: Float, hints: Int, errors: Int) -> Unit
) {
    val solution = remember { listOf("MOVE_FORWARD", "TURN_RIGHT", "MOVE_FORWARD", "COLLECT_CRYSTAL") }
    val userScript = remember { mutableStateListOf<String>() }
    var feedback by remember { mutableStateOf<String?>(null) }
    var errors by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Assemble the instruction blocks in order to guide the rover to the green crystal:",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Target Quest Goal
        CyberCard(borderColor = NeonCyan) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🤖", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ROVER MISSION ➔", color = TextSecondary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("💎 CRYSTAL", color = NeonEmerald, fontWeight = FontWeight.Bold)
                }
                Text("${userScript.size} Blocks", color = CyberGold, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Code Script Tray
        CyberCard(borderColor = CyberBorder) {
            Column {
                Text("CADET CODE SCRIPT:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = TextSecondary)
                Spacer(modifier = Modifier.height(8.dp))
                if (userScript.isEmpty()) {
                    Text("Tap blocks below to add instructions to your script...", color = TextTertiary, style = MaterialTheme.typography.bodySmall)
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        userScript.forEachIndexed { index, block ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonCyan.copy(alpha = 0.2f))
                                    .border(1.dp, NeonCyan, RoundedCornerShape(8.dp))
                                    .clickable { userScript.removeAt(index) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${index + 1}. $block", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Close, contentDescription = "Remove", tint = TextSecondary, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Available Blocks Palette
        Text("COMMAND PALETTE:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = TextSecondary)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CommandPaletteButton("MOVE_FORWARD", Icons.Default.ArrowUpward) { userScript.add("MOVE_FORWARD") }
            CommandPaletteButton("TURN_RIGHT", Icons.Default.ArrowForward) { userScript.add("TURN_RIGHT") }
            CommandPaletteButton("COLLECT", Icons.Default.Diamond) { userScript.add("COLLECT_CRYSTAL") }
        }

        if (feedback != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(feedback!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        GlowButton(
            text = "EXECUTE ALGORITHM",
            enabled = userScript.isNotEmpty(),
            onClick = {
                if (userScript == solution) {
                    onComplete(1.0f, 0, errors)
                } else {
                    errors++
                    feedback = "Script did not reach target! Correct sequence: Move Forward ➔ Turn Right ➔ Move Forward ➔ Collect Crystal."
                }
            },
            accentColor = NeonEmerald,
            icon = Icons.Default.PlayArrow,
            testTag = "execute_code_button"
        )
    }
}

@Composable
private fun CommandPaletteButton(title: String, icon: ImageVector, onAdd: () -> Unit) {
    Button(
        onClick = onAdd,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceVariant, contentColor = NeonCyan),
        border = BorderStroke(1.dp, CyberBorder)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

// ==========================================
// 9. LOOP COMMANDER
// ==========================================
@Composable
fun LoopCommanderView(
    activity: ActivityEntity,
    onComplete: (score: Float, hints: Int, errors: Int) -> Unit
) {
    var iterations by remember { mutableIntStateOf(1) }
    var selectedAction by remember { mutableStateOf("MOVE_FORWARD") }
    var errors by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Patrol the 4 sides of the space perimeter. Instead of writing 4 separate commands, use a single REPEAT loop:",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Visual Loop Box
        CyberCard(borderColor = CyberGold) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Replay, contentDescription = null, tint = CyberGold, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("REPEAT ( $iterations TIMES ) {", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace), color = CyberGold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberSurfaceLight)
                        .padding(12.dp)
                ) {
                    Text("➔ $selectedAction and TURN_CORNER", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace), color = NeonCyan)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace), color = CyberGold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loop Counter Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Loop Iterations:", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (iterations > 1) iterations-- }) {
                    Text("-", fontSize = 24.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                }
                Text("$iterations", fontSize = 20.sp, color = CyberGold, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp))
                IconButton(onClick = { if (iterations < 8) iterations++ }) {
                    Text("+", fontSize = 24.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        GlowButton(
            text = "RUN REPEAT LOOP",
            onClick = {
                if (iterations == 4) {
                    onComplete(1.0f, 0, errors)
                } else {
                    errors++
                }
            },
            accentColor = NeonEmerald,
            icon = Icons.Default.PlayArrow,
            testTag = "run_loop_button"
        )
    }
}

// ==========================================
// 10. BUG HUNTER & DEBUGGER
// ==========================================
@Composable
fun BugHunterView(
    activity: ActivityEntity,
    onComplete: (score: Float, hints: Int, errors: Int) -> Unit
) {
    val codeLines = remember {
        listOf(
            "1: START_ROVER()",
            "2: MOVE_FORWARD()",
            "3: TURN_LEFT()   <-- [BUG: Turns into meteor wall!]",
            "4: MOVE_FORWARD()",
            "5: REACH_BEACON()"
        )
    }

    var selectedLineToFix by remember { mutableIntStateOf(-1) }
    var fixedOption by remember { mutableStateOf<String?>(null) }
    var errors by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Find the buggy code line that crashes the rover, and select the correct replacement:",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Code Editor View
        CyberCard(borderColor = CyberCrimson) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                codeLines.forEachIndexed { index, line ->
                    val isSelected = selectedLineToFix == index
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) CyberCrimson.copy(alpha = 0.2f) else CyberSurfaceVariant)
                            .border(1.dp, if (isSelected) CyberCrimson else Color.Transparent, RoundedCornerShape(8.dp))
                            .clickable { selectedLineToFix = index }
                            .padding(8.dp)
                    ) {
                        Text(
                            text = line,
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold),
                            color = if (index == 2) CyberCrimson else TextPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedLineToFix == 2) {
            Text("Select the fix for Line 3:", style = MaterialTheme.typography.titleSmall, color = NeonCyan)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { fixedOption = "TURN_RIGHT" },
                    colors = ButtonDefaults.buttonColors(containerColor = if (fixedOption == "TURN_RIGHT") NeonEmerald else CyberSurfaceVariant)
                ) {
                    Text("TURN_RIGHT()", color = TextPrimary)
                }
                Button(
                    onClick = { fixedOption = "SELF_DESTRUCT" },
                    colors = ButtonDefaults.buttonColors(containerColor = if (fixedOption == "SELF_DESTRUCT") CyberCrimson else CyberSurfaceVariant)
                ) {
                    Text("SELF_DESTRUCT()", color = TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        GlowButton(
            text = "APPLY DEBUG PATCH",
            enabled = selectedLineToFix == 2 && fixedOption == "TURN_RIGHT",
            onClick = {
                onComplete(1.0f, 0, errors)
            },
            accentColor = NeonEmerald,
            icon = Icons.Default.Build,
            testTag = "apply_debug_patch_button"
        )
    }
}
