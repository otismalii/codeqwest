# CodeQuest LDX Nexus Architecture Contract

**Product Identity:** CodeQuest — Offline Digital Literacy & Computing Learning Engine for Kids  
**App Mode:** `LOCAL` (Fully offline, zero external telemetry, local Room persistence, biometric/parent-lock security)  
**Target Audience:** Kids (Ages 7–14)  
**Contract Version:** `1.0.0`

---

## 1. Product Invariants

1. **Local-Only Authority:**
   All domain state, curriculum progress, attempts, evidence records, and learner models reside exclusively in the on-device Room database. No network permissions are requested or used.

2. **Immutable Reconstructable Learner State:**
   Learner mastery is derived from an append-only sequence of `ActivityAttempt` and `AssessmentEvidence` records. UI components never mutate scores directly; state flows deterministically:
   $$\text{ActivityAttempt} \longrightarrow \text{AssessmentEvidence} \longrightarrow \text{ConceptMasteryProjection} \longrightarrow \text{AdaptiveDecision}$$

3. **Deterministic & Explainable Adaptation:**
   No black-box recommendation logic. Every recommended quest has an explicit reason code (e.g., `RETRY_WITH_SCAFFOLD`, `ADVANCE_CHALLENGE`, `INTERLEAVE_REVIEW`).

4. **Child Safety & Privacy:**
   - Zero telemetry, COPPA-compliant local data storage.
   - Parent / Educator dashboard is secured via Biometric Prompt or a 4-digit master PIN.

---

## 2. Canonical Domain Schema

### 2.1 Concept
- `id`: String (e.g. `basics.input_output`, `hardware.cpu`, `safety.passwords`, `code.loops`)
- `moduleId`: String
- `title`: String
- `description`: String
- `targetMastery`: Float (default `0.75f`)

### 2.2 Activity & Types
- `id`: String
- `moduleId`: String
- `conceptId`: String
- `title`: String
- `type`: `INPUT_OUTPUT_SORTER`, `PASSWORD_SHIELD`, `MOTHERBOARD_BUILDER`, `BINARY_SWITCH`, `BUG_HUNTER`, `PHISHING_INSPECTOR`, `CODE_BLOCK_SEQUENCER`, `LOOP_COMMANDER`
- `difficulty`: Int (1..5)
- `contentJson`: Serialized activity configuration and challenges

### 2.3 Attempt & Evidence
- `id`: String (UUID)
- `profileId`: String
- `activityId`: String
- `timestamp`: Long
- `isSuccess`: Boolean
- `score`: Float (0.0f .. 1.0f)
- `hintsUsed`: Int
- `timeSpentMs`: Long

---

## 3. Curriculum Structure
- **Module 1: Computer Basics** (Input/Output, Digital Footprint, File Systems, Passwords)
- **Module 2: Hardware Lab** (Motherboard, CPU/RAM/Storage, Bits & Binary, Ports & Cables)
- **Module 3: Software World** (Operating Systems, Apps vs OS, Glitch Buster, Scam & Phish Detective)
- **Module 4: Code Academy** (Sequencing, Repeat Loops, Conditionals, Live Debugger)

---

## 4. State-Driven UI & Architecture Rules
- Jetpack Compose with unidirectional data flow (UDF).
- ViewModels expose immutable `StateFlow<UiState>`.
- Interactive widgets enforce touch target $\ge 48\text{dp}$ and dark mode high-contrast cyber styling.
