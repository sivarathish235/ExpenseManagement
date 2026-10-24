# Submission Checklist - Java Assignment 2 (Expense Management System)

Deadline: **24 October 2026**.
Legend: `[x]` = already created for you in this project folder | `[ ]` = **you must do this yourself**.

---

## A. Files already created (in this project folder)

- [x] `src/com/expensemanagement/Employee.java`
- [x] `src/com/expensemanagement/Manager.java`
- [x] `src/com/expensemanagement/Expense.java`
- [x] `src/com/expensemanagement/ExpenseService.java`
- [x] `src/com/expensemanagement/ExpenseAppMain.java`
- [x] `01_Assignment_Front_Sheet.docx` (template preserved; only Date of Submission 24.10.2026 filled)
- [x] `02_Assignment_Question.txt` (exact question text)
- [x] `README.md`
- [x] `docs/REPORT.md`
- [x] `screenshots/README.md` (capture instructions)
- [x] `.gitignore`
- [x] `LICENSE` (MIT, author name is a placeholder)
- [x] `SUBMISSION_CHECKLIST.md` (this file)
- [x] `OUTPUT_PREVIEW.txt` - expected output from a **command-line** run (reference only)
- [x] `screenshots/Eclipse_Output_Preview.png` - **illustrative preview only; NOT an Eclipse screenshot**

## B. Validation already performed (command line, OpenJDK 21, not Eclipse)

- [x] All 5 files compile with `-Xlint:all`, no warnings
- [x] Compiles at language level 17
- [x] Program runs to completion; output identical on two runs
- [x] 18 / 18 automatic self-checks pass
- [x] Output is pure ASCII (no `?` problems in the Eclipse Console)

## C. Sample preview vs. real evidence - do not mix these up

| Item | Type | Use in submission? |
|---|---|---|
| `OUTPUT_PREVIEW.txt` | Expected output from command line | Reference only, **not** evidence |
| `screenshots/Eclipse_Output_Preview.png` | Illustrative picture rendered from the output | **Do NOT print or submit as a screenshot** |
| `screenshots/01_...png` ... `08_...png` | **Actual Eclipse Console screenshots** | **Required** - you must capture them |

## D. Actions you still need to do manually

### Personalise
- [ ] Open `01_Assignment_Front_Sheet.docx` in Word and fill **Name & Register No.** and **Year/Dept./Sem/Section** (and Reference(s) if required). Check the top-left header text "Department of ..." and complete it by hand if your department expects it. Leave **Marks Details** for the faculty.
- [ ] Replace `[YOUR NAME]` / `[YOUR REGISTER NUMBER]` / `[YOUR DETAILS]` in `README.md` and `docs/REPORT.md`
- [ ] Replace `[YOUR NAME - replace this placeholder]` in `LICENSE`

### Run in Eclipse and capture screenshots (see `screenshots/README.md`)
- [ ] Install JDK 21 + Eclipse IDE for Java Developers
- [ ] Create the Java project from this folder (JavaSE-21), no red errors
- [ ] Run `ExpenseAppMain` (Run As > Java Application)
- [ ] Confirm the Console ends with `Self-checks passed: 18 / 18`
- [ ] Capture the screenshots with Windows Snipping Tool (Win + Shift + S) and save them in `screenshots/`:
  - [ ] `01_run_start_employees_managers.png`
  - [ ] `02_submit_overloaded_methods.png`
  - [ ] `03_approvals_limit_grade.png`
  - [ ] `04_rejection_reimbursement.png`
  - [ ] `05_grouped_by_status.png`
  - [ ] `06_totals_and_monthly_summary.png`
  - [ ] `07_oop_demonstrations.png`
  - [ ] `08_validation_and_selfchecks.png` (or `08a` / `08b`)
  - [ ] `09_eclipse_window_overview.png` (optional)
- [ ] Verify every screenshot shows the **Eclipse Console** (tab title visible), is readable, and is not from a terminal
- [ ] Fill the "Eclipse result" column in the test-case table of `docs/REPORT.md`
- [ ] Send the screenshots to Claude to organise them and update README/report (optional)

### GitHub
- [ ] Create a **public** repository named `ExpenseManagementAssignment2` on github.com (no README/licence/.gitignore added by GitHub)
- [ ] Run the Git commands from `README.md` (`git init -b main`, `git status`, `git add .`, `git commit`, `git remote add origin ...`, `git push -u origin main`)
- [ ] Open the repository in the browser and check that `src/`, `docs/`, `screenshots/` (with your real screenshots), `README.md`, `02_Assignment_Question.txt` are all visible
- [ ] Replace the placeholder URL in `docs/REPORT.md` (Section 13) and in `README.md` with the real URL, commit and push again

## E. Printed submission - required order

1. - [ ] **Assignment Front Sheet** - print `01_Assignment_Front_Sheet.docx` (filled in)
2. - [ ] **Assignment Question** - print `02_Assignment_Question.txt` (exact question)
3. - [ ] **Java Source Code - printed from Eclipse IDE** (open each file in Eclipse, **File > Print...** or `Ctrl+P`), in this order:
   - [ ] `Employee.java`
   - [ ] `Manager.java`
   - [ ] `Expense.java`
   - [ ] `ExpenseService.java`
   - [ ] `ExpenseAppMain.java`
4. - [ ] **Output Screenshots - only genuine Eclipse Console captures** (`screenshots/01...` to `08...`)
5. - [ ] **GitHub Repository Link** - last page, with the real URL (not the placeholder)

## F. Files to upload to GitHub

Everything in the project folder **except** compiled/IDE files (the `.gitignore` already excludes `bin/`, `*.class`, `.project`, `.classpath`, `.settings/`):

- [ ] `src/com/expensemanagement/` (5 `.java` files)
- [ ] `docs/REPORT.md`
- [ ] `screenshots/README.md`
- [ ] `screenshots/Eclipse_Output_Preview.png` (allowed; clearly labelled illustrative in the README)
- [ ] `screenshots/01_...png` - `08_...png` (**your real Eclipse screenshots**)
- [ ] `01_Assignment_Front_Sheet.docx`
- [ ] `02_Assignment_Question.txt`
- [ ] `OUTPUT_PREVIEW.txt`
- [ ] `SUBMISSION_CHECKLIST.md`
- [ ] `README.md`, `.gitignore`, `LICENSE`

> Tip: if you do not want your register number public, fill the front sheet only in your printed copy and upload the blank template version.
