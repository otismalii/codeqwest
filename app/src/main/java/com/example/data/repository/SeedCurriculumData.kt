package com.example.data.repository

import com.example.data.local.AchievementEntity
import com.example.data.local.ActivityEntity
import com.example.data.local.ConceptEntity
import com.example.data.local.CurriculumModuleEntity
import com.example.domain.model.ActivityType
import com.example.domain.model.ModuleCategory

object SeedCurriculumData {

    val modules = listOf(
        CurriculumModuleEntity(
            id = "mod_basics",
            title = "Computer Basics",
            description = "Master digital footprints, input & output devices, file systems, and cyber shields.",
            category = ModuleCategory.BASICS,
            orderIndex = 1,
            iconName = "basics"
        ),
        CurriculumModuleEntity(
            id = "mod_hardware",
            title = "Hardware Lab",
            description = "Explore the motherboard, snap together CPU & RAM, decode binary bits, and connect ports.",
            category = ModuleCategory.HARDWARE,
            orderIndex = 2,
            iconName = "hardware"
        ),
        CurriculumModuleEntity(
            id = "mod_software",
            title = "Software World",
            description = "Command the Operating System, bust software bugs, and spot sneaky phishing scams.",
            category = ModuleCategory.SOFTWARE,
            orderIndex = 3,
            iconName = "software"
        ),
        CurriculumModuleEntity(
            id = "mod_code",
            title = "Code Academy",
            description = "Build algorithms with sequences, repeat loops, if-else conditionals, and live code debugging.",
            category = ModuleCategory.CODE,
            orderIndex = 4,
            iconName = "code"
        )
    )

    val concepts = listOf(
        // Basics
        ConceptEntity("basics.input_output", "mod_basics", "Input vs Output", "How data enters and leaves computers", "device", 0.75f),
        ConceptEntity("basics.passwords", "mod_basics", "Password Power", "Creating unbreakable cyber keys", "shield", 0.75f),
        ConceptEntity("basics.digital_footprint", "mod_basics", "Digital Footprint", "Staying safe and private online", "privacy", 0.75f),
        ConceptEntity("basics.files_folders", "mod_basics", "File Systems", "Organizing files, folders, and extensions", "folder", 0.75f),

        // Hardware
        ConceptEntity("hardware.motherboard", "mod_hardware", "Inside the Machine", "The motherboard, CPU, RAM & Storage", "chip", 0.75f),
        ConceptEntity("hardware.binary_bits", "mod_hardware", "Binary & Bits", "How computers talk in 1s and 0s", "binary", 0.75f),
        ConceptEntity("hardware.ports_cables", "mod_hardware", "Ports & Cables", "Connecting USB-C, HDMI, and power", "cable", 0.75f),

        // Software
        ConceptEntity("software.os_vs_apps", "mod_software", "OS vs Apps", "How system software manages hardware", "window", 0.75f),
        ConceptEntity("software.bug_hunter", "mod_software", "Bug Hunting", "Finding glitches and system errors", "bug", 0.75f),
        ConceptEntity("software.phishing_detection", "mod_software", "Cyber Detective", "Spotting fake messages and scams", "search", 0.75f),

        // Code
        ConceptEntity("code.sequences", "mod_code", "Sequencing Steps", "Ordering instructions for machines", "arrow_forward", 0.75f),
        ConceptEntity("code.repeat_loops", "mod_code", "Repeat Loops", "Doing powerful things without repeating code", "replay", 0.75f),
        ConceptEntity("code.conditionals", "mod_code", "If-Else Logic", "Teaching computers to make smart decisions", "call_split", 0.75f),
        ConceptEntity("code.debugging", "mod_code", "Algorithm Debugger", "Fixing broken code scripts like a pro", "build", 0.75f)
    )

    val activities = listOf(
        // MODULE 1: BASICS
        ActivityEntity(
            id = "act_basics_io_1",
            moduleId = "mod_basics",
            conceptId = "basics.input_output",
            title = "Input vs Output Sorter",
            subtitle = "Sort computer gadgets into Input (giving data) or Output (showing data)",
            type = ActivityType.INPUT_OUTPUT_SORTER,
            difficulty = 1,
            xpReward = 60,
            prompt = "Categorize the 6 cadet devices correctly to power up the communication terminal!",
            contentJson = """
                {
                    "items": [
                        {"name": "Microphone", "icon": "mic", "type": "INPUT", "hint": "You speak into it to send sound into the computer"},
                        {"name": "Computer Screen", "icon": "tv", "type": "OUTPUT", "hint": "It displays graphics and pictures to your eyes"},
                        {"name": "Keyboard", "icon": "keyboard", "type": "INPUT", "hint": "You type letters and commands into the computer"},
                        {"name": "Speakers", "icon": "volume_up", "type": "OUTPUT", "hint": "It sends sound waves out into the room"},
                        {"name": "Webcam", "icon": "videocam", "type": "INPUT", "hint": "It captures video of you and feeds it to the PC"},
                        {"name": "Color Printer", "icon": "print", "type": "OUTPUT", "hint": "It puts pictures and documents out onto paper"}
                    ]
                }
            """.trimIndent()
        ),
        ActivityEntity(
            id = "act_basics_pwd_1",
            moduleId = "mod_basics",
            conceptId = "basics.passwords",
            title = "Password Power Shield",
            subtitle = "Forge a fortress-level password to defeat the Hacker-Bot",
            type = ActivityType.PASSWORD_SHIELD,
            difficulty = 2,
            xpReward = 75,
            prompt = "Combine letters, numbers, and special symbols to max out the shield strength before the timer ticks!",
            contentJson = """
                {
                    "requiredLength": 8,
                    "requireUpper": true,
                    "requireNumber": true,
                    "requireSymbol": true,
                    "badExamples": ["password123", "qwerty", "admin", "12345678"],
                    "goodExamples": ["Cyber#Cat99", "Space!Rover24", "Pixel*Quest7"]
                }
            """.trimIndent()
        ),
        ActivityEntity(
            id = "act_basics_footprint_1",
            moduleId = "mod_basics",
            conceptId = "basics.digital_footprint",
            title = "Safe Cyber Footprint",
            subtitle = "Decide what is safe to post and what must stay locked in your private vault",
            type = ActivityType.DIGITAL_FOOTPRINT_CHOPPER,
            difficulty = 1,
            xpReward = 65,
            prompt = "Classify these 4 online actions: Is it SAFE TO SHARE publicly, or DANGEROUS/PRIVATE?",
            contentJson = """
                {
                    "scenarios": [
                        {"text": "Your home address and real phone number", "isSafe": false, "reason": "Never share personal address or phone numbers with strangers online."},
                        {"text": "A cool pixel art drawing you made yourself", "isSafe": true, "reason": "Sharing your creative artwork is awesome and safe!"},
                        {"text": "Your gamer password and secret email recovery code", "isSafe": false, "reason": "Passwords should NEVER be shared with anyone except parents."},
                        {"text": "A book review of your favorite sci-fi space novel", "isSafe": true, "reason": "Sharing thoughts on favorite books and hobbies is great!"}
                    ]
                }
            """.trimIndent()
        ),

        // MODULE 2: HARDWARE LAB
        ActivityEntity(
            id = "act_hw_motherboard_1",
            moduleId = "mod_hardware",
            conceptId = "hardware.motherboard",
            title = "Motherboard Assembly Lab",
            subtitle = "Install the 4 core components into their sockets to boot the cadet supercomputer",
            type = ActivityType.MOTHERBOARD_BUILDER,
            difficulty = 2,
            xpReward = 80,
            prompt = "Match the CPU (Brain), RAM (Fast Memory), SSD (Vault Storage), and GPU (Visual Engine) to their sockets!",
            contentJson = """
                {
                    "components": [
                        {"id": "cpu", "name": "CPU (Processor)", "role": "The Brain that calculates all instructions", "slot": "Socket AM5 Center"},
                        {"id": "ram", "name": "RAM (Memory Sticks)", "role": "Super fast temporary memory for open apps", "slot": "DIMM Slots Right"},
                        {"id": "ssd", "name": "SSD (Storage Drive)", "role": "Long-term vault where games and files sleep", "slot": "M.2 NVMe Slot Bottom"},
                        {"id": "gpu", "name": "GPU (Graphics Card)", "role": "Visual powerhouse for 3D rendering and games", "slot": "PCIe x16 Lane"}
                    ]
                }
            """.trimIndent()
        ),
        ActivityEntity(
            id = "act_hw_binary_1",
            moduleId = "mod_hardware",
            conceptId = "hardware.binary_bits",
            title = "Binary Bit Switch Matrix",
            subtitle = "Flip 4 binary switches (8, 4, 2, 1) to match target secret number values",
            type = ActivityType.BINARY_SWITCH,
            difficulty = 2,
            xpReward = 85,
            prompt = "Turn ON the correct bits (1=ON, 0=OFF) to transmit the target number to the space probe!",
            contentJson = """
                {
                    "challenges": [
                        {"target": 5, "hint": "5 = 4 + 1 -> Binary 0 1 0 1"},
                        {"target": 10, "hint": "10 = 8 + 2 -> Binary 1 0 1 0"},
                        {"target": 13, "hint": "13 = 8 + 4 + 1 -> Binary 1 1 0 1"},
                        {"target": 15, "hint": "15 = 8 + 4 + 2 + 1 -> Binary 1 1 1 1"}
                    ]
                }
            """.trimIndent()
        ),

        // MODULE 3: SOFTWARE WORLD
        ActivityEntity(
            id = "act_sw_os_1",
            moduleId = "mod_software",
            conceptId = "software.os_vs_apps",
            title = "OS Commander vs Apps",
            subtitle = "Learn who does what: The Operating System (Manager) or Applications (Tools)",
            type = ActivityType.OS_VS_APPS,
            difficulty = 1,
            xpReward = 70,
            prompt = "Determine if each responsibility belongs to the OS (Operating System) or an App (Game/Tool)!",
            contentJson = """
                {
                    "questions": [
                        {"task": "Managing battery power and distributing RAM to programs", "isOs": true, "expl": "The Operating System controls system hardware and resource allocation."},
                        {"task": "Letting you edit photos and paint digital drawings", "isOs": false, "expl": "Apps like photo editors provide specific tools for user tasks."},
                        {"task": "Deciding which window is visible on the screen and handling touch input", "isOs": true, "expl": "The OS manages windows, displays, and hardware drivers."},
                        {"task": "Playing a racing game with 3D cars and high scores", "isOs": false, "expl": "Games are application software running on top of the OS."}
                    ]
                }
            """.trimIndent()
        ),
        ActivityEntity(
            id = "act_sw_phish_1",
            moduleId = "mod_software",
            conceptId = "software.phishing_detection",
            title = "Phishing & Scam Sleuth",
            subtitle = "Examine suspicious messages and spot sneaky scam triggers",
            type = ActivityType.PHISHING_INSPECTOR,
            difficulty = 2,
            xpReward = 85,
            prompt = "Read each digital message carefully: Is it a LEGITIMATE MESSAGE or a PHISHING SCAM?",
            contentJson = """
                {
                    "emails": [
                        {
                            "sender": "free-robux-gems@giveaway999-win.xyz",
                            "subject": "YOU WON 1,000,000 FREE GEMS! CLICK NOW!",
                            "body": "Congratulations lucky player! Enter your username and password here right now to claim your prize before it expires in 5 minutes!",
                            "isPhishing": true,
                            "clues": ["Fake sender domain", "Urgent time pressure", "Asking for your password"]
                        },
                        {
                            "sender": "school-library@oakville-academy.org",
                            "subject": "Reminder: Your Science Book is due Friday",
                            "body": "Hello Cadet, just a reminder that 'Space Rovers V2' is due back at the school library on Friday. No action needed if already returned.",
                            "isPhishing": false,
                            "clues": ["Legitimate official domain", "No request for credentials", "Polite reminder without panic"]
                        },
                        {
                            "sender": "security-alert@acc0unt-verify-fast.biz",
                            "subject": "URGENT: Your account was locked! Send credit card to unlock",
                            "body": "We detected suspicious activity. Please download this file virus.exe and send your parent credit card number immediately.",
                            "isPhishing": true,
                            "clues": ["Misspelled domain", "Asking for sensitive credit card numbers", "Suspicious .exe download"]
                        }
                    ]
                }
            """.trimIndent()
        ),

        // MODULE 4: CODE ACADEMY
        ActivityEntity(
            id = "act_code_seq_1",
            moduleId = "mod_code",
            conceptId = "code.sequences",
            title = "Rover Sequence Navigator",
            subtitle = "Command the rover step-by-step through the asteroid field to collect the energy crystal",
            type = ActivityType.CODE_BLOCK_SEQUENCER,
            difficulty = 1,
            xpReward = 90,
            prompt = "Place the motion blocks in the exact sequence to reach the glowing green crystal!",
            contentJson = """
                {
                    "gridSize": 4,
                    "startX": 0,
                    "startY": 0,
                    "startDir": "EAST",
                    "targetX": 2,
                    "targetY": 2,
                    "obstacles": [{"x": 1, "y": 0}, {"x": 2, "y": 1}],
                    "solution": ["MOVE_FORWARD", "TURN_RIGHT", "MOVE_FORWARD", "MOVE_FORWARD", "TURN_LEFT", "MOVE_FORWARD", "COLLECT_CRYSTAL"]
                }
            """.trimIndent()
        ),
        ActivityEntity(
            id = "act_code_loop_1",
            moduleId = "mod_code",
            conceptId = "code.repeat_loops",
            title = "Loop Commander: Repeat Power",
            subtitle = "Use REPEAT loops to patrol the cadet space station perimeter with minimal code blocks",
            type = ActivityType.LOOP_COMMANDER,
            difficulty = 2,
            xpReward = 100,
            prompt = "Construct a compact loop: REPEAT(4) { MOVE_FORWARD, TURN_RIGHT } to patrol the perimeter!",
            contentJson = """
                {
                    "goal": "Walk around a 4-sided square perimeter",
                    "targetIterations": 4,
                    "loopBody": ["MOVE_FORWARD", "TURN_RIGHT"],
                    "hint": "Instead of writing Move+Turn 4 times, put them inside 1 Repeat(4) loop!"
                }
            """.trimIndent()
        ),
        ActivityEntity(
            id = "act_code_debug_1",
            moduleId = "mod_code",
            conceptId = "code.debugging",
            title = "Glitch Buster Code Debugger",
            subtitle = "Find the faulty block that sends the rover into a meteor wall",
            type = ActivityType.BUG_HUNTER,
            difficulty = 2,
            xpReward = 95,
            prompt = "Inspect the buggy script, remove or fix the broken step, and guide the rover safely home!",
            contentJson = """
                {
                    "brokenScript": ["MOVE_FORWARD", "TURN_LEFT", "MOVE_FORWARD", "MOVE_FORWARD"],
                    "errorStepIndex": 1,
                    "correctAction": "TURN_RIGHT",
                    "explanation": "The rover turned LEFT into a meteor wall instead of turning RIGHT toward the beacon!"
                }
            """.trimIndent()
        )
    )

    fun getDefaultAchievements(profileId: String) = listOf(
        AchievementEntity("ach_first_step", profileId, "First Step", "Complete Cadet Onboarding and calibration", "rocket_launch", true, System.currentTimeMillis()),
        AchievementEntity("ach_byte_scout", profileId, "Byte Scout", "Master the Computer Basics module", "memory", false, null),
        AchievementEntity("ach_silicon_architect", profileId, "Silicon Architect", "Assemble all hardware components correctly", "developer_board", false, null),
        AchievementEntity("ach_bug_buster", profileId, "Bug Buster", "Debug and repair your first glitch script", "pest_control", false, null),
        AchievementEntity("ach_cyber_sentinel", profileId, "Cyber Sentinel", "Spot all phishing scams with 100% accuracy", "verified_user", false, null),
        AchievementEntity("ach_loop_master", profileId, "Loop Commander", "Successfully run an optimized repeat loop", "all_inclusive", false, null)
    )
}
