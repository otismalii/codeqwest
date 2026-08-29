package com.example.data.repository

import com.example.domain.model.DocumentSection
import com.example.domain.model.IntuitionQuestion
import com.example.domain.model.ResourceCategory
import com.example.domain.model.ResourceDocument

object ResourceLibraryData {

    fun getAllDocuments(): List<ResourceDocument> {
        return listOf(
            // DOCUMENT 1: Grace Hopper & The First Bug
            ResourceDocument(
                id = "doc_first_bug",
                title = "The Night a Real Moth Broke a Computer",
                subtitle = "How Admiral Grace Hopper turned a relay short into computing history",
                authorOrSource = "CyberQuest Archives (Historical Declassified)",
                category = ResourceCategory.HISTORY_PIONEERS,
                readTimeMinutes = 4,
                publicationYear = "1947 (Harvard University, MA)",
                geoTag = "Cambridge, Massachusetts, USA",
                funMemeQuote = "It is not a bug, it is an undocumented feature!",
                summary = "On September 9, 1947, engineers operating the room-sized Harvard Mark II computer found a dead moth trapped between relay points. Grace Hopper taped it into the logbook as the first actual bug found.",
                sections = listOf(
                    DocumentSection(
                        heading = "1. The Giant Room-Sized Computer",
                        body = "In 1947, computers were not sleek glass slabs in your pocket. They were noisy, room-sized mechanical beasts with thousands of clicking electro-magnetic switches called relays. The Mark II computer at Harvard University filled an entire floor.",
                        realWorldFact = "The Harvard Mark II weighed 25 tons and could perform an addition in 0.125 seconds. Your phone is over 100,000,000 times faster!"
                    ),
                    DocumentSection(
                        heading = "2. The Mysterious Failure on Relay #70",
                        body = "At 3:45 PM on a hot late-summer afternoon, calculations abruptly stopped. Grace Hopper and her crew meticulously probed the machine relay by relay. Finally, inside panel F, they discovered a 2-inch moth that had flown in through an open window and got stuck in Relay 70.",
                        calloutBox = "Historical Artifact: That actual moth is still preserved today in the Smithsonian National Museum of American History, taped to page 11 of the logbook!",
                        memeInsight = "Every programmer today who spends 4 hours searching for a missing semicolon is honoring Grace Hopper's legacy."
                    ),
                    DocumentSection(
                        heading = "3. Hopper's Revolutionary Legacy: Human Words in Code",
                        body = "Grace Hopper did not just debug hardware; she invented the very first compiler (A-0) and co-created COBOL, showing the world that humans should write programs in English words rather than raw binary 1s and 0s.",
                        realWorldFact = "Grace Hopper retired as a US Navy Rear Admiral at age 79, carrying 1-foot pieces of wire to show audiences the distance light travels in one nanosecond."
                    )
                ),
                intuitionChallenge = IntuitionQuestion(
                    question = "Why was Grace Hopper's invention of compilers such a game-changer for human critical thinking?",
                    options = listOf(
                        "It allowed humans to write logic in readable words instead of memorizing thousands of binary numbers",
                        "It physically electrocuted bugs inside the computer",
                        "It replaced all keyboards with Morse code levers",
                        "It made computers run without needing electricity"
                    ),
                    correctIndex = 0,
                    explanation = "Compilers translate human-readable instructions (like 'if score > 10') into machine code (01001010), freeing humans to solve logical problems rather than flipping electrical switches!",
                    memeSnark = "10/10 big brain intuition! No one wants to code Minecraft in raw binary punch cards."
                )
            ),

            // DOCUMENT 2: Undersea Cables Geography
            ResourceDocument(
                id = "doc_undersea_cables",
                title = "Sharks, Volcanoes & Undersea Glass Threads",
                subtitle = "The secret physical geography of how the Internet actually travels between continents",
                authorOrSource = "Global Telecom Geodetic Survey",
                category = ResourceCategory.GEOGRAPHY_INFRA,
                readTimeMinutes = 5,
                publicationYear = "Modern Planetary Infrastructure",
                geoTag = "Atlantic & Pacific Ocean Trenches (8,000m deep)",
                funMemeQuote = "The cloud is just someone else's computer connected by cables in the ocean.",
                summary = "Over 99% of all international internet traffic does not travel via satellites in space. It travels through hair-thin glass fiber optic cables laid across ocean floors, dodged by sharks and underwater earthquakes.",
                sections = listOf(
                    DocumentSection(
                        heading = "1. Myth Busted: The Cloud is Underwater",
                        body = "When you send a message from New York to London or Tokyo, most people imagine it beams up to a satellite in orbit. In reality, satellites have high latency (lag). Instead, your video call shoots as pulses of laser light through garden-hose-thick cables lying in pitch-black ocean trenches 25,000 feet beneath sea level.",
                        realWorldFact = "There are over 550 active submarine cable systems spanning 1.4 million kilometers—enough to wrap around the Earth 35 times!"
                    ),
                    DocumentSection(
                        heading = "2. The Danger Zone: Sharks, Anchors & Subsea Landslides",
                        body = "The biggest threats to the world's internet are not hackers in hoodies; they are fishing boat anchors dragging along the seabed, deep sea shark curiosity bites (sharks sense electromagnetic fields), and volcanic landslides.",
                        calloutBox = "Critical Thinking Check: In 2006, the Hengchun submarine earthquake severed 8 submarine cables simultaneously, cutting off 90% of internet between East Asia and the Americas in 3 seconds!",
                        memeInsight = "Imagine telling a medieval knight that in 2026, humanity's collective memes and banking depend on glass noodles laid on the ocean floor."
                    ),
                    DocumentSection(
                        heading = "3. Speed of Light vs Glass",
                        body = "Lasers pulse through pure silica glass at roughly 200,000 km per second (about 2/3 the speed of light in a vacuum). That means a packet of data crosses the Atlantic Ocean in roughly 60 milliseconds—faster than you can blink your eyes.",
                        realWorldFact = "A single modern undersea fiber pair can carry over 250 Terabits per second—equivalent to streaming 5 million 4K movies at the same instant."
                    )
                ),
                intuitionChallenge = IntuitionQuestion(
                    question = "If an earthquake cuts all undersea cables between two continents, what happens to high-speed video calls between them?",
                    options = listOf(
                        "They will immediately fail or suffer severe lag because satellites have limited bandwidth and higher latency",
                        "The calls automatically teleport through the Earth's molten core",
                        "Nothing happens because all internet is 100% wireless magic",
                        "The water conducts the Wi-Fi signal across the ocean waves"
                    ),
                    correctIndex = 0,
                    explanation = "Undersea physical fiber cables handle 99% of intercontinental throughput. Satellite backups can handle emergency low-bandwidth signals, but lack the bandwidth to carry millions of simultaneous video streams seamlessly!",
                    memeSnark = "Physics always wins. You cannot download more RAM, and you cannot replace 1,000 terabits of glass with thin air."
                )
            ),

            // DOCUMENT 3: Memory Analogy - RAM vs SSD
            ResourceDocument(
                id = "doc_ram_vs_ssd",
                title = "The Messy Desk vs The Giant Warehouse",
                subtitle = "Understanding computer memory without migraine-inducing jargon",
                authorOrSource = "Everyday Computing Intuition Guide",
                category = ResourceCategory.EVERYDAY_ANALOGIES,
                readTimeMinutes = 3,
                publicationYear = "Foundational Architecture",
                geoTag = "Inside your computer / phone motherboard",
                funMemeQuote = "Chrome: I see you have 16GB of RAM... it would be a shame if I ate it all.",
                summary = "Why does your computer need both RAM and an SSD? Think of RAM as your active workspace desk where you keep homework right in front of you, and SSD as the giant bookshelf or school locker down the hall.",
                sections = listOf(
                    DocumentSection(
                        heading = "1. The Desk (RAM - Random Access Memory)",
                        body = "RAM is lightning fast. When you open an app or game, the computer copies the active parts from storage onto your desk so the CPU can grab what it needs in nanoseconds. But here is the catch: when you turn off the power, your desk is wiped 100% clean (volatile memory)!",
                        calloutBox = "Analogy Test: If you have a tiny desk (4GB RAM), you can only keep 2 notebooks open at once. If you try to open a 3rd, you have to pack one away into your backpack, which makes everything feel sluggish.",
                        memeInsight = "That is why opening 47 tabs of video games makes your laptop fan sound like a jet engine preparing for takeoff."
                    ),
                    DocumentSection(
                        heading = "2. The Backpack / Warehouse (SSD / Storage)",
                        body = "Your SSD (Solid State Drive) is your permanent storage. It is much bigger (like 512GB or 1,000GB), and everything stays saved even when the power is completely off (non-volatile). It is slower than RAM, but it remembers your photos, games, and files forever.",
                        realWorldFact = "Early 1950s hard drives used giant magnetic spinning platters the size of washing machines that held just 5 Megabytes (about 1 modern MP3 song)!"
                    )
                ),
                intuitionChallenge = IntuitionQuestion(
                    question = "You are writing an essay in a document editor. Suddenly, your laptop battery dies completely before you hit Save. Why might unsaved changes be lost?",
                    options = listOf(
                        "Unsaved text was held in volatile RAM, which loses its state when electrical power drops",
                        "The SSD ran out of ink",
                        "The keyboard deleted the letters out of spite",
                        "The letters evaporated into the room's atmosphere"
                    ),
                    correctIndex = 0,
                    explanation = "Active edits live in volatile RAM until committed/saved to non-volatile SSD storage. Modern apps auto-save frequently to bridge this exact gap!",
                    memeSnark = "Ctrl+S is the ultimate reflex of someone who learned the hard way in 2004."
                )
            ),

            // DOCUMENT 4: Margaret Hamilton & Apollo 11
            ResourceDocument(
                id = "doc_margaret_hamilton",
                title = "The Code That Saved the Moon Landing",
                subtitle = "Margaret Hamilton, asynchronous priority scheduling, and the famous 1202 alarm",
                authorOrSource = "NASA MIT Instrumentation Laboratory",
                category = ResourceCategory.HISTORY_PIONEERS,
                readTimeMinutes = 5,
                publicationYear = "1969 (Apollo 11 Lunar Mission)",
                geoTag = "Houston, Texas & Sea of Tranquility, Moon",
                funMemeQuote = "She literally stood next to a stack of Apollo source code taller than herself!",
                summary = "Three minutes before Apollo 11 touched down on the Moon, a radar switch error overloaded the computer. Margaret Hamilton's bulletproof priority software saved Neil Armstrong and Buzz Aldrin from aborting the landing.",
                sections = listOf(
                    DocumentSection(
                        heading = "1. The 1202 Program Alarm at 3,000 Feet",
                        body = "As the Lunar Module was descending toward the Moon's cratered surface, alarms 1201 and 1202 flashed on the control panel. The computer was being bombarded with too many radar tasks per second. In ordinary computers of the 1960s, this would cause a total system crash.",
                        realWorldFact = "The Apollo Guidance Computer had only 4 Kilobytes of RAM and 72 Kilobytes of ROM—less computing power than a modern musical greeting card!"
                    ),
                    DocumentSection(
                        heading = "2. Asynchronous Priority Architecture",
                        body = "Margaret Hamilton, Director of the Software Engineering Division at MIT, had designed asynchronous priority scheduling. When overloaded, the software smartly dropped low-priority radar checks and kept the critical thruster and descent-engine commands running without dropping a single beat.",
                        calloutBox = "Critical Thinking Lesson: Great engineering isn't assuming nothing will ever go wrong; it is designing systems that gracefully prioritize safety when things do go wrong.",
                        memeInsight = "Margaret Hamilton coined the very term Software Engineering because people back then thought coding wasn't a real science. She showed them!"
                    )
                ),
                intuitionChallenge = IntuitionQuestion(
                    question = "What was the key engineering intuition behind Margaret Hamilton's Apollo software design?",
                    options = listOf(
                        "Priority scheduling: prioritize life-critical thruster commands and drop non-essential tasks during overload",
                        "Turn off the engine whenever an alarm sounds",
                        "Add more mechanical gears to the rocket thrusters",
                        "Wait for Earth to send a text message before making any decisions"
                    ),
                    correctIndex = 0,
                    explanation = "By prioritizing life-critical tasks over optional telemetry radar cycles, the lunar module kept operating safely through unexpected overload spikes!",
                    memeSnark = "Legendary engineering intuition. That is why they landed with 25 seconds of fuel remaining."
                )
            ),

            // DOCUMENT 5: The Cyber Sleuth & Phishing Detective
            ResourceDocument(
                id = "doc_cyber_sleuth",
                title = "How to Spot a Phish Before You Get Hooked",
                subtitle = "Meme psychology, urgency traps, and how hackers trick human brains instead of firewalls",
                authorOrSource = "Cadet Cyber Defense Archives",
                category = ResourceCategory.CYBER_SLEUTH,
                readTimeMinutes = 4,
                publicationYear = "Modern Digital Literacy",
                geoTag = "Every inbox, DM, and game chat in the world",
                funMemeQuote = "URGENT: Your game account will be deleted in 10 minutes unless you click this totally legit link! 🚩",
                summary = "The easiest way to break into a high-security bank isn't breaking 256-bit encryption math—it's tricking a tired human into giving away their password through emotional panic.",
                sections = listOf(
                    DocumentSection(
                        heading = "1. The Psychology of Social Engineering",
                        body = "Hackers know that human brains panic when faced with fake emergencies ('YOUR ACCOUNT IS COMPROMISED!', 'YOU WON 1,000,000 V-BUCKS!'). When you panic or get overly excited, your critical thinking brain turns off and your impulse reflex takes over.",
                        calloutBox = "Rule of Thumb: If an email or message demands URGENT action or threatens bad consequences if you don't click within 15 minutes, it is 99.9% a scam."
                    ),
                    DocumentSection(
                        heading = "2. The URL Eye Test (Look at the Domain Name)",
                        body = "Scammers create look-alike domains to fool you: 'accounts-google.security-login.net' is NOT google.com. The true domain is always the word right before the last slash or suffix: in 'apple.support-verification.com', the actual owner is support-verification.com, not Apple!",
                        realWorldFact = "Over 85% of successful cybersecurity breaches start with a phishing email that tricked a person, not a supercomputer cracking passwords.",
                        memeInsight = "Never click suspicious links unless you enjoy getting Rickrolled or gifting your Minecraft skin collection to a stranger."
                    )
                ),
                intuitionChallenge = IntuitionQuestion(
                    question = "You receive a DM: 'Roblox Admin: Your account has suspicious activity! Log into www.roblox-security-update.club/claim within 5 mins or get banned!' What is the smartest critical action?",
                    options = listOf(
                        "Ignore and report it: Real companies never threaten 5-minute bans via weird unofficial domains (.club)",
                        "Click immediately and enter your password, email, and home address",
                        "Forward the link to all your friends so they can claim it too",
                        "Change your password to 'password123'"
                    ),
                    correctIndex = 0,
                    explanation = "High urgency + unofficial strange domains (.club) + asking for login credentials = classic phishing attack!",
                    memeSnark = "1000 IQ detective move. No admin will ever slide into your DMs asking for your secret password."
                )
            ),

            // DOCUMENT 6: GPS & Einstein's Clocks in Space
            ResourceDocument(
                id = "doc_gps_relativity",
                title = "Why Satellites Must Obey Einstein to Tell You Where You Are",
                subtitle = "General relativity, atomic clocks, and the cosmic geography of GPS",
                authorOrSource = "Planetary Navigation Bureau",
                category = ResourceCategory.GEOGRAPHY_INFRA,
                readTimeMinutes = 4,
                publicationYear = "Space Age Applied Science",
                geoTag = "20,200 km above Earth (Medium Earth Orbit)",
                funMemeQuote = "Time moves faster for satellites in space than for you sitting on the couch!",
                summary = "GPS in your phone works by measuring the exact time it takes radio waves to travel from 4 satellites in orbit. But because gravity is weaker in space, satellite atomic clocks tick 38 microseconds faster per day—which Einstein predicted in 1915!",
                sections = listOf(
                    DocumentSection(
                        heading = "1. Trilateration: Measuring Spheres of Radio Waves",
                        body = "Your phone doesn't send signals to GPS satellites; it just listens. Each satellite broadcasts its exact timestamp and orbital coordinates. By comparing the arrival times of signals from at least 4 satellites, your phone calculates its exact 3D latitude, longitude, and elevation.",
                        realWorldFact = "Radio waves travel at 300,000 km/s. If a GPS clock is off by just 1 microsecond (one millionth of a second), your phone's location would be wrong by 300 meters!"
                    ),
                    DocumentSection(
                        heading = "2. Einstein's Revenge: Time Dilation in Orbit",
                        body = "Einstein discovered that gravity bends time: clocks in stronger gravity (on Earth's surface) tick slower than clocks in weaker gravity (20,000 km up in space). GPS satellites tick 45 microseconds faster per day due to weak gravity, minus 7 microseconds slower due to their orbital speed.",
                        calloutBox = "Net Effect: Satellite clocks run 38 microseconds fast each day. Without relativistic math corrections programmed into GPS receivers, navigation apps would drift by 11 kilometers every single day!",
                        memeInsight = "Next time someone tells you theoretical physics isn't useful, tell them they couldn't order pizza delivery without Einstein."
                    )
                ),
                intuitionChallenge = IntuitionQuestion(
                    question = "Why does your phone need signals from at least 4 GPS satellites instead of just 1 to find your location?",
                    options = listOf(
                        "To intersect multiple spheres of distance (trilateration) and mathematically calculate 3D coordinates plus clock synchronization",
                        "Because 1 satellite only has enough electricity for 10 seconds of listening",
                        "Because the satellites take turns sleeping every 5 minutes",
                        "So the phone can average the 4 different weather forecasts"
                    ),
                    correctIndex = 0,
                    explanation = "Trilateration uses overlapping spheres from known positions in space to pinpoint your exact intersection point on Earth!",
                    memeSnark = "Geometry and physics working together like Batman and Robin."
                )
            )
        )
    }

    fun getDocumentById(id: String): ResourceDocument? {
        return getAllDocuments().find { it.id == id }
    }
}
