# Screenshots folder

This folder must contain **genuine screenshots of the Eclipse Console**, captured by you after running the project in Eclipse.

| File | Status |
|---|---|
| `Eclipse_Output_Preview.png` | **Illustrative preview only.** Rendered from a command-line run to show what the output looks like. **NOT an Eclipse screenshot. Do not submit it as evidence.** |
| `01_...png` to `09_...png` (listed below) | **You must capture these in Eclipse.** None exist yet. |

## Part A - Run the project in Eclipse

1. Install **JDK 21** and **Eclipse IDE for Java Developers**. Check in Command Prompt: `java -version`.
2. Open Eclipse and choose a workspace folder.
3. **Window > Preferences > Java > Installed JREs**: confirm JDK 21 is listed and ticked (*Add > Standard VM > Directory* if it is not).
4. **File > New > Java Project**:
   - Name: `ExpenseManagementAssignment2`
   - Untick **Use default location** and **Browse...** to the project folder (the one that contains `src`).
   - Execution environment JRE: **JavaSE-21**. Click **Finish**.
   - If Eclipse offers to create `module-info.java`, choose **Don't Create**.
5. In **Package Explorer**, expand `src > com.expensemanagement`. You should see the 5 classes with **no red error icons**.
   (If you see errors, check **Project > Properties > Java Build Path > Libraries** shows JavaSE-21.)
6. Open `ExpenseAppMain.java`. Right-click inside the editor > **Run As > Java Application** (or `Ctrl+F11`).
7. The **Console** view opens at the bottom (if not: **Window > Show View > Console**). Its tab title should read
   `<terminated> ExpenseAppMain [Java Application] ...`.

## Part B - Get the Console ready for screenshots

1. **Increase the font** (optional, improves print quality): **Window > Preferences > General > Appearance > Colors and Fonts > Debug > Console font > Edit...** and pick size 12-14.
2. **Maximize the Console**: double-click the **Console** tab. About 40-50 lines then fit on the screen.
3. Re-run (`Ctrl+F11`) after changing the font so the output is redrawn if needed.
4. Make sure the console is not auto-scrolled to a random place. Use the scroll bar / mouse wheel to reach each section below.
   The output has 250 lines; the *line numbers* below refer to the lines of `OUTPUT_PREVIEW.txt` after its header (i.e. the program output).

## Part C - Screenshots to capture (Windows Snipping Tool)

For each screenshot: scroll the Console to the section, press **Win + Shift + S**, drag a rectangle that includes the **Console tab title bar** and the output text, then save it into this `screenshots/` folder with the exact file name shown.

| # | Save as | What the Console must show | Program output lines |
|---|---|---|---|
| 1 | `01_run_start_employees_managers.png` | Banner + STEP 1 (employees and managers created, grade/limit lines) | 1-23 |
| 2 | `02_submit_overloaded_methods.png` | STEP 2: all three overloads (`Overload 1`, `Overload 2`, `Overload 3`) | 25-52 |
| 3 | `03_approvals_limit_grade.png` | STEP 3: approvals, denials by grade, **approval-limit escalation** (EXP-005 denied for Meera) | 53-83 |
| 4 | `04_rejection_reimbursement.png` | STEP 4 (rejection) and STEP 5 (reimbursement + blocked attempts) | 85-108 |
| 5 | `05_grouped_by_status.png` | STEP 6: expenses grouped by status | 109-132 |
| 6 | `06_totals_and_monthly_summary.png` | STEP 7 (per-employee totals) and STEP 8 (monthly summary) | 133-154 |
| 7 | `07_oop_demonstrations.png` | STEP 9: inheritance, overriding/polymorphism, overloading, encapsulation | 155-198 |
| 8 | `08_validation_and_selfchecks.png` | STEP 10 (validation) and STEP 11 (self-checks, `18 / 18`) - scroll to the very end; use two screenshots (`08a`, `08b`) if it does not fit | 200-250 |
| 9 | `09_eclipse_window_overview.png` | *(optional)* the whole Eclipse window with the Console tab title `<terminated> ExpenseAppMain [Java Application]`, to prove it was run in Eclipse | - |

If the maximized Console is too small for a section, take two screenshots and add `a`/`b` to the name (for example `03a_...png`, `03b_...png`).

## Part D - Verify each saved image

Open every PNG and confirm all of these:

- [ ] The **Console** tab / title (`<terminated> ExpenseAppMain [Java Application] ...`) is visible, so it is clearly the Eclipse Console.
- [ ] Text is sharp and readable when printed (no blur, no cut-off characters on the right).
- [ ] The text matches the expected section (compare with `OUTPUT_PREVIEW.txt`).
- [ ] The image is **not** from the Command Prompt, PowerShell, VS Code terminal, or a web page.
- [ ] No unrelated personal information (e.g. open emails/chats) is visible.
- [ ] File name matches the table above and the file is inside `screenshots/`.

## Part E - After capturing

Tell Claude the file names you saved (or send the images) and ask it to help organise the folder, update the README
screenshot section, and add the images to the report. Then upload with the Git commands in the main `README.md`.
