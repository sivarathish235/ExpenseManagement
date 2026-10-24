# Expense Management System (Java, OOP)

A console-based **Expense Management** application written in plain Java. Employees submit
Travel/Meals expenses, managers approve or reject them according to their **approval limit and grade**,
and approved expenses are marked **reimbursed**. The program prints per-employee totals and monthly
summaries.

> **Course:** 23CSR303 - Object Oriented Programming with Java | **Academic year:** 2026-2027 | **Assignment II, Question 9**

---

## Problem statement

The exact question is stored in [`02_Assignment_Question.txt`](02_Assignment_Question.txt).

> Design and implement a console-based Expense Management app to submit, approve, and reimburse employee expenses using OOP in Java.
> Requirements: (1) at least 4 classes - Employee, Manager, Expense, ExpenseService; (2) each class has at least 4 variables, a constructor and at least 5 methods;
> (3) demonstrate inheritance, overloading, overriding, polymorphism and encapsulation; (4) a Main class `ExpenseAppMain` that creates employees/managers, submits expenses,
> approves/rejects, marks reimbursed, and prints per-employee totals and monthly summaries.

## Features

- Employees and managers (a `Manager` **is an** `Employee`).
- Expense types restricted to **Travel** and **Meals**; amounts must be positive.
- Status life-cycle: `PENDING -> APPROVED -> REIMBURSED`, or `PENDING -> REJECTED`.
- Three overloaded `submit()` methods: basic, with attachment, with attachment + cost center.
- **Grade-based approval rules** (Junior / Senior / Director) plus an approval limit, with clear
  *escalation* messages when a manager is not allowed to approve.
- Rejected or pending expenses can never be reimbursed.
- Validation: duplicate IDs, bad ID format, zero/negative amounts, invalid types, unknown IDs, illegal state changes.
- Reports: expenses grouped by status, totals per employee, monthly summary.
- Runs **automatically with built-in sample data** (no typing, no database, no libraries). Dates are fixed, so output is identical on every run.
- Ends with **18 automatic self-checks** that compare computed totals/statuses with expected values.

## Technologies used

- Java (standard library only) - written for **JDK 21**, language features used are compatible with Java 17+
- Eclipse IDE (for the final run and screenshots) / VS Code (development)
- Git and GitHub

## Project folder structure

```text
ExpenseManagementAssignment2/
├── src/
│   └── com/
│       └── expensemanagement/
│           ├── Employee.java
│           ├── Manager.java
│           ├── Expense.java
│           ├── ExpenseService.java
│           └── ExpenseAppMain.java
├── screenshots/
│   ├── README.md                      <- checklist for the REAL Eclipse Console screenshots
│   └── Eclipse_Output_Preview.png     <- ILLUSTRATIVE preview only (not an Eclipse screenshot)
├── docs/
│   └── REPORT.md
├── 01_Assignment_Front_Sheet.docx
├── 02_Assignment_Question.txt
├── OUTPUT_PREVIEW.txt
├── SUBMISSION_CHECKLIST.md
├── README.md
├── .gitignore
└── LICENSE
```

## OOP concepts demonstrated (where to find each one)

| Concept | Where in the code | Where you see it in the output |
|---|---|---|
| **Inheritance** | `Manager extends Employee` (`Manager.java`); the constructor calls `super(...)`; a Manager uses inherited `getName()`, `getEmail()`, `calculateTotal()`, `reportExpenses()` | STEP 9, part 1 |
| **Method overloading** | `ExpenseService.submit()` has **three** versions (6, 7 and 8 parameters). Also `calculateTotal(...)` (3 versions in `ExpenseService`, 2 in `Employee`) and `calculateMonthlyTotal(...)` (2 versions) | STEP 2 and STEP 9, part 4 |
| **Method overriding** | `Manager` overrides `getApprovalBlocker()`, `approve()`, `reject()`, `getRole()` and `displayDetails()` from `Employee`. `approve()` applies **different rules per grade** (Junior: Meals only; Senior: receipt needed above Rs. 5,000; Director: limit only) and always enforces the approval limit | STEP 3 and STEP 9, parts 2-3 |
| **Polymorphism** | `ExpenseAppMain.people` is a `List<Employee>` that holds `Manager` objects; `ExpenseService.approve(String, Employee)` takes an `Employee` reference; a loop calls `p.getApprovalBlocker(...)` on every `Employee` reference and the JVM runs the Employee **or** Manager version (dynamic method dispatch) | STEP 1 (`displayDetails()`), STEP 3, STEP 9 parts 2-3 |
| **Encapsulation** | All fields are `private`. There is **no** `setStatus()` / `setAmount()`. `Expense.approve()/reject()/reimburse()` are package-private and enforce legal transitions; `updateAmount()` works only while `PENDING` and only for positive values; `Employee.getSubmittedExpenses()` returns an unmodifiable list | STEP 5 (blocked reimbursements), STEP 9 part 5 |

## Prerequisites

- **JDK 17 or later** (tested with OpenJDK 21). Check with `java -version` and `javac -version`.
- Optional: Eclipse IDE for Java Developers.

## Run in Eclipse

1. Start Eclipse and pick a workspace.
2. **Window > Preferences > Java > Installed JREs** - make sure a **JDK 21** is listed and ticked (use *Add > Standard VM* if not).
3. **File > New > Java Project**.
   - Project name: `ExpenseManagementAssignment2`
   - Untick **Use default location**, click **Browse...** and select this project folder. Eclipse detects the existing `src` folder.
   - JRE: *Use an execution environment JRE* -> **JavaSE-21**.
   - Click **Finish**. If asked to create `module-info.java`, choose **Don't Create**.
4. In **Package Explorer** expand `src > com.expensemanagement`. There must be no red error marks.
5. Open `ExpenseAppMain.java`, right-click in the editor, **Run As > Java Application** (or press `Ctrl+F11`).
6. The output appears in the **Console** view (**Window > Show View > Console** if hidden).

Alternative if you prefer a fresh project: create a normal Java project, then right-click `src` >
**Import > General > File System**, choose this project's `src` folder, tick the `com` folder, **Finish**.

Full screenshot instructions: [`screenshots/README.md`](screenshots/README.md).

## Compile and run from the terminal

From the project root:

```bash
# Windows (Command Prompt / PowerShell), macOS, Linux - the same commands
javac -d bin src/com/expensemanagement/*.java
java -cp bin com.expensemanagement.ExpenseAppMain
```

(On Windows PowerShell, if the wildcard is not expanded, use `javac -d bin (Get-ChildItem src -Recurse -Filter *.java).FullName`.)

## Sample output

The complete expected output is in [`OUTPUT_PREVIEW.txt`](OUTPUT_PREVIEW.txt) (250 lines). It was produced by compiling
and running the project with OpenJDK 21 **from a command line, not from Eclipse**. Excerpts:

**Approvals, grade rules and approval-limit escalation (STEP 3)**

```text
==============================================================================
STEP 3: APPROVE EXPENSES (GRADE AND LIMIT RULES)
==============================================================================
> Aarav Sharma (Employee) tries to approve EXP-001
  [DENIED]   Aarav Sharma (Employee) has no authority to approve expenses.
> Divya Menon (Manager) tries to approve EXP-001
  [DENIED]   Grade rule: Junior (L1) managers can approve Meals only. EXP-001
             (Travel) needs ESCALATION to a Senior or Director manager.
> Meera Iyer (Manager) tries to approve EXP-001
  [APPROVED] EXP-001 (Rs. 4,500.00) approved by Meera Iyer [Senior (L2)].
> Divya Menon (Manager) tries to approve EXP-004
  [APPROVED] EXP-004 (Rs. 850.00) approved by Divya Menon [Junior (L1)].
> Meera Iyer (Manager) tries to approve EXP-002
  [APPROVED] EXP-002 (Rs. 1,200.00) approved by Meera Iyer [Senior (L2)].
> Meera Iyer (Manager) tries to approve EXP-003
  [APPROVED] EXP-003 (Rs. 18,500.00) approved by Meera Iyer [Senior (L2)].
> Meera Iyer (Manager) tries to approve EXP-005
  [DENIED]   Amount Rs. 65,000.00 of EXP-005 exceeds Meera Iyer's approval
             limit of Rs. 20,000.00. Needs ESCALATION to a manager with a
             higher limit.
> Rajesh Kumar (Manager) tries to approve EXP-005
  [APPROVED] EXP-005 (Rs. 65,000.00) approved by Rajesh Kumar [Director (L3)].
> Meera Iyer (Manager) tries to approve EXP-007
  [DENIED]   Grade rule: Senior (L2) managers need an attachment (receipt) for
             claims above Rs. 5,000.00. EXP-007 has none. Ask the employee to
             attach it, or ESCALATE to a Director.
> Meera Iyer (Manager) tries to approve EXP-008
  [DENIED]   Meera Iyer cannot approve their own expense EXP-008. Needs
             ANOTHER AUTHORIZED MANAGER.
> Rajesh Kumar (Manager) tries to approve EXP-008
  [APPROVED] EXP-008 (Rs. 1,800.00) approved by Rajesh Kumar [Director (L3)].
```

**Per-employee totals and monthly summary (STEPS 7-8)**

```text
==============================================================================
STEP 7: PER-EMPLOYEE EXPENSE TOTALS
==============================================================================
(amounts in Rs.; 'Approved' = approved but not yet reimbursed)
ID    Name                  Total    Pending   Approved   Rejected  Reimbursed
E101  Aarav Sharma      13,150.00   7,800.00     850.00       0.00    4,500.00
E102  Priya Nair        66,200.00       0.00   1,200.00       0.00   65,000.00
E103  Karthik Raman     20,900.00       0.00  18,500.00   2,400.00        0.00
M201  Meera Iyer         1,800.00       0.00   1,800.00       0.00        0.00
M202  Rajesh Kumar           0.00       0.00       0.00       0.00        0.00
M203  Divya Menon            0.00       0.00       0.00       0.00        0.00
GRAND TOTAL            102,050.00

==============================================================================
STEP 8: MONTHLY EXPENSE SUMMARY
==============================================================================
(amounts in Rs.; 'Paid' = reimbursed so far)
Month    Claims       Travel        Meals        Total         Paid
2026-09       3    23,000.00     1,200.00    24,200.00     4,500.00
2026-10       5    72,800.00     5,050.00    77,850.00    65,000.00
ALL           8                             102,050.00

```

Amounts are printed as `Rs.` (plain ASCII) instead of the rupee sign so that they display correctly in every
console, including the Windows Eclipse Console.

## Test cases

The program runs these cases automatically. "Command-line result" was observed when the project was run with JDK 21
outside Eclipse; **you must still run it in Eclipse and capture your own screenshots.**

| # | Test case | Expected result | Command-line result (JDK 21) |
|---|---|---|---|
| 1 | Create 3 employees + 3 managers | 6 people stored (3 are managers) | Pass |
| 2 | `submit()` basic / with attachment / with attachment + cost center | All 8 expenses created as `PENDING` | Pass |
| 3 | Plain `Employee` tries to approve | Denied: no authority | Pass |
| 4 | Junior manager approves a Travel expense | Denied: grade rule, escalate to Senior/Director | Pass |
| 5 | Senior manager approves Rs. 4,500 Travel | Approved | Pass |
| 6 | Senior manager approves Rs. 65,000 (limit Rs. 20,000) | Denied: exceeds limit, needs escalation; stays `PENDING` | Pass |
| 7 | Director approves the same Rs. 65,000 expense | Approved | Pass |
| 8 | Senior manager approves Rs. 7,800 without receipt | Denied: receipt required, stays `PENDING` | Pass |
| 9 | Manager approves own expense | Denied: needs another authorized manager | Pass |
| 10 | Manager rejects an expense with a reason | `REJECTED` | Pass |
| 11 | Reimburse an approved expense | `REIMBURSED` with date 2026-10-15 | Pass |
| 12 | Reimburse a rejected expense | Blocked | Pass |
| 13 | Reimburse a pending expense | Blocked | Pass |
| 14 | Reimburse the same expense twice | Blocked | Pass |
| 15 | Duplicate expense ID / bad ID format | Blocked | Pass |
| 16 | Negative amount / zero amount | Blocked | Pass |
| 17 | Invalid type (`Hotel`) | Blocked | Pass |
| 18 | Unknown expense ID | Blocked | Pass |
| 19 | Rejection with blank reason | Blocked | Pass |
| 20 | Change amount of an `APPROVED` expense | Blocked (encapsulation) | Pass |
| 21 | Per-employee totals | Aarav 13,150.00; Priya 66,200.00; Karthik 20,900.00; Meera 1,800.00; grand total 102,050.00 | Pass |
| 22 | Monthly summary | 2026-09: 3 claims, 24,200.00; 2026-10: 5 claims, 77,850.00 | Pass |
| 23 | Automatic self-checks | `Self-checks passed: 18 / 18` | Pass |

## Screenshots

- [`screenshots/Eclipse_Output_Preview.png`](screenshots/Eclipse_Output_Preview.png) is an **illustrative preview only**.
  It was rendered from a command-line run to look like a dark console. **It is not a screenshot of Eclipse.**
- The **genuine Eclipse Console screenshots** required for submission have to be captured by the author after running the
  project in Eclipse. The checklist and file names are in [`screenshots/README.md`](screenshots/README.md). *(To be added.)*

## Uploading to GitHub

```bash
git init -b main
git status
git add .
git commit -m "Add Java Assignment 2: Expense Management System"
git remote add origin https://github.com/cibi-k/ExpenseManagementAssignment2.git
git push -u origin main
```

Create the empty repository first on github.com (**+ > New repository**, name `ExpenseManagementAssignment2`, **Public**,
do **not** tick "Add a README"). Copy the HTTPS URL from the green **Code** button and use it in the `git remote add` command.


## Author

- **Name:** Cibi K.
- **Register number:** (given in the printed submission)
- **Department / Year / Section:** II Year / Information Technology / Sem III / Section A
- **GitHub:** https://github.com/cibi-k/ExpenseManagementAssignment2

## License

Released under the MIT License - see [`LICENSE`](LICENSE). 
