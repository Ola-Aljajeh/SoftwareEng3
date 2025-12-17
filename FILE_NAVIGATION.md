# Quick File Navigation Map

## 🎯 Start Here (Choose One)

### If You Have 2 Minutes:

→ Read: `GUI_QUICK_START.txt`
→ Then: Double-click `run-gui.bat`

### If You Have 5 Minutes:

→ Read: `START_HERE.md`
→ Then: Run `python build.py`

### If You Have 15 Minutes:

→ Read: `QUICK_REFERENCE.md`
→ Then: Launch GUI with `run-gui.bat`
→ Then: Click things and explore

### If You're Presenting Soon (IMPORTANT!):

→ Read: `PRESENTATION_MASTER_GUIDE.md` (this is critical!)
→ Then: Launch GUI and practice
→ Then: Review code files as needed

---

## 📂 Files by Purpose

### Launch the Project

- `run-gui.bat` - Interactive GUI (BEST!)
- `build.py` - Console demo + detailed output
- `compile-all.py` - Rebuild everything

### Understand the Architecture

- `START_HERE.md` - Overview & what to read next
- `QUICK_REFERENCE.md` - 30-second explanations of each pattern
- `ARCHITECTURE.md` - Detailed design with diagrams
- `VISUAL_GUIDE.md` - Visual project overview

### Use the GUI

- `GUI_QUICK_START.txt` - 2-min quick ref for GUI
- `GUI_README.md` - Complete GUI documentation
- `GUI_SUMMARY.md` - GUI feature summary

### Present Your Project

- **`PRESENTATION_MASTER_GUIDE.md`** ← READ THIS FIRST!
- `PRESENTATION_GUIDE.md` - Speaking points for each pattern
- `DELIVERY_SUMMARY.md` - What was built & why

### Build & Test

- `BUILD_GUIDE.md` - How to compile and test
- `pom.xml` - Maven configuration (if needed)
- Test files: `src/test/java/com/bankingsystem/`

### Reference

- `README.md` - Complete project details
- `PROJECT_SUMMARY.md` - Statistics & metrics
- `METRICS.md` - Code quality metrics
- `INDEX.md` - Complete file index

---

## 🔍 Source Code Map

### Each Pattern Location

```
Composite Pattern  → src/main/java/com/bankingsystem/account/
                     Key: AccountGroup.java

Chain of Resp      → src/main/java/com/bankingsystem/transaction/
                     Key: ApprovalHandler.java

Strategy Pattern   → src/main/java/com/bankingsystem/interest/
                     Key: InterestStrategy.java

Observer Pattern   → src/main/java/com/bankingsystem/notification/
                     Key: NotificationObserver.java

Facade Pattern     → src/main/java/com/bankingsystem/facade/
                     Key: BankFacade.java

State Pattern      → src/main/java/com/bankingsystem/state/
                     Key: AccountState.java
```

### Demo Code

- `BankingSystemDemo.java` - Console demo (shows all patterns)
- `BankingSystemGUI.java` - Interactive GUI (visual demo) ← BETTER FOR GRADES!

---

## ⏱️ Time-Based Recommended Reading

### 2 Minutes (Just Want to Run It)

1. `GUI_QUICK_START.txt`
2. Double-click `run-gui.bat`
   Done!

### 5 Minutes (Quick Overview)

1. `START_HERE.md`
2. `GUI_QUICK_START.txt`
3. Run `python build.py`

### 15 Minutes (Understand It)

1. `START_HERE.md` (5 min)
2. `QUICK_REFERENCE.md` (10 min)
3. Launch GUI and play with it

### 30 Minutes (Know It Well)

1. `START_HERE.md` (5 min)
2. `QUICK_REFERENCE.md` (10 min)
3. `PRESENTATION_MASTER_GUIDE.md` (10 min)
4. Launch GUI and practice demo (5 min)

### 1 Hour (Master It - BEFORE PRESENTING!)

1. All of above (30 min)
2. `ARCHITECTURE.md` (15 min)
3. Look at 1-2 pattern implementation files (10 min)
4. Practice your presentation (5 min)

---

## 🎓 For Your Presentation

### Preparation Checklist

- [ ] Read `PRESENTATION_MASTER_GUIDE.md` (most important!)
- [ ] Launch GUI 3+ times
- [ ] Practice the demo from the guide (~5 min)
- [ ] Read `QUICK_REFERENCE.md` to memorize pattern pitches
- [ ] Know where each pattern file is located
- [ ] Understand how patterns integrate through Facade

### During Presentation

- [ ] Launch GUI (do this first, sets the stage)
- [ ] Follow the demo from PRESENTATION_MASTER_GUIDE.md
- [ ] Point to code when explaining patterns
- [ ] Show notifications panel as proof patterns work
- [ ] Mention tests and documentation
- [ ] Be ready for Q&A (but you're prepared!)

### After Presentation

- [ ] Ask if they want to see specific code
- [ ] Offer to show tests or architecture
- [ ] Explain any design decisions if asked

---

## 🚀 The Most Important Files

**For Getting Good Grades:**

1. **`PRESENTATION_MASTER_GUIDE.md`** ← Read this!
2. **`run-gui.bat`** ← Launch this!
3. **`QUICK_REFERENCE.md`** ← Memorize this!
4. **`BankingSystemGUI.java`** ← Show this!

**For Understanding the Code:**

1. **`QUICK_REFERENCE.md`** (patterns explained)
2. **`ARCHITECTURE.md`** (design decisions)
3. Pattern files (one per pattern)

**For Impressing with Quality:**

1. **`METRICS.md`** (code quality)
2. **`PROJECT_SUMMARY.md`** (what was built)
3. Test files (shows comprehensive testing)

---

## 💡 Quick Decision Tree

```
START HERE:
│
├─ Want to run it now?
│  └─ Double-click run-gui.bat
│
├─ Need to understand quickly?
│  ├─ Read START_HERE.md (5 min)
│  └─ Read QUICK_REFERENCE.md (10 min)
│
├─ Have to present soon?
│  ├─ Read PRESENTATION_MASTER_GUIDE.md (CRITICAL!)
│  ├─ Launch GUI with run-gui.bat
│  └─ Practice the demo
│
├─ Want to show code?
│  ├─ Open src/main/java/com/bankingsystem/
│  ├─ Start with [pattern]/ directories
│  └─ Reference QUICK_REFERENCE.md
│
├─ Want to prove quality?
│  ├─ Show METRICS.md
│  ├─ Point to test files (src/test/java/)
│  └─ Reference BUILD_GUIDE.md
│
└─ Still have time?
   ├─ Read ARCHITECTURE.md
   ├─ Read README.md
   └─ Explore source code
```

---

## 📊 Document Purpose Summary

| Document                     | Purpose               | Read If                    | Time   |
| ---------------------------- | --------------------- | -------------------------- | ------ |
| GUI_QUICK_START.txt          | 2-min GUI reference   | Just want quick overview   | 2 min  |
| START_HERE.md                | Project orientation   | First time reading         | 5 min  |
| QUICK_REFERENCE.md           | Pattern pitches       | Need to memorize patterns  | 10 min |
| PRESENTATION_MASTER_GUIDE.md | How to present        | **Before showing anyone!** | 10 min |
| GUI_README.md                | Complete GUI guide    | Want all GUI details       | 15 min |
| ARCHITECTURE.md              | Design deep dive      | Want to understand design  | 20 min |
| BUILD_GUIDE.md               | Compilation & testing | Building on own system     | 10 min |
| METRICS.md                   | Code quality stats    | Proving code quality       | 5 min  |
| README.md                    | Complete details      | Want everything explained  | 30 min |

---

## 🎯 Bottom Line

**Minimum to Impress Your Professor:**

1. Double-click `run-gui.bat` (shows it works)
2. Read `QUICK_REFERENCE.md` (know patterns in 30 sec)
3. Show GUI working (click accounts, see patterns)
4. Point to code if asked (open pattern files)
5. Mention tests exist (show them if asked)

**That's it!** The GUI does most of the heavy lifting for you.

---

Good luck! 🌟
