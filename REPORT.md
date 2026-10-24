# Expense Management System using Object Oriented Programming in Java

**Course:** 23CSR303 - Object Oriented Programming with Java  
**Academic year:** 2026-2027  **Assignment:** II  **Question number:** 9  
**Name:** Cibi K.  **Register No.:** 717825F112  **Dept/Year/Sem/Section:** II Year / IT / Sem III / Section A

---

## 1. Title

Console-based Expense Management System (submit, approve, reject and reimburse employee expenses) using OOP in Java.

## 2. Aim

To design and implement a console-based Expense Management application in Java that uses classes, inheritance, method overloading,
method overriding, polymorphism and encapsulation to submit, approve, reject and reimburse employee expenses, and to report
per-employee totals and monthly summaries.

## 3. Assignment question

> Design and implement a console-based Expense Management app to submit, approve, and reimburse employee expenses using OOP in Java.
>
> Requirements:
>
> 1. Create at least 4 classes:
>    - Employee — empId, name, dept, email.
>    - Manager — extends Employee, approvalLimit.
>    - Expense — expenseId, type (Travel/Meals), amount, status, requester.
>    - ExpenseService — submission, approval, reimbursement processing.
>
> 2. Each class must include:
>    - At least 4 instance/static variables.
>    - A constructor to initialize values.
>    - At least 5 methods (getters/setters, submit(), approve(), reject(), listByStatus(), calculateTotal()).
>
> 3. Demonstrate OOP concepts:
>    - Inheritance → Manager overrides approval behavior.
>    - Method Overloading → submit() with/without attachments/cost center.
>    - Method Overriding → custom approve() logic by manager grade.
>    - Polymorphism → handle Employee/Manager via Employee references.
>    - Encapsulation → restrict direct status/amount edits.
>
> 4. Write a Main class (ExpenseAppMain) to test:
>    - Create employees/managers, submit expenses.
>    - Approve/reject, mark reimbursed.
>    - Print per-employee totals and monthly summaries.

## 4. Introduction

Organisations need a controlled way for employees to claim business expenses and for managers to approve them. This project models that process.
An **Employee** submits an **Expense** (Travel or Meals). A **Manager** (a special kind of Employee) can approve or reject it, but only
within the manager's **approval limit** and according to the manager's **grade**. An **ExpenseService** coordinates the process: it stores
expenses, validates input, performs approvals, marks approved expenses as reimbursed, and produces summaries.
`ExpenseAppMain` runs the whole workflow automatically using built-in sample data in Indian rupees, so it can be demonstrated without any input.

**Class summary (declared members, including private helper methods):**

| Class | Role | Fields | Constructors | Methods |
|---|---|---|---|---|
| `Employee` | Base class: id, name, dept, email, own expense list | 7 | 1 | 24 |
| `Manager` | Extends `Employee`: approval limit, grade, grade-based approval | 7 | 1 | 15 |
| `Expense` | One claim: id, type, amount, status, requester, date, attachment, cost center | 13 | 1 | 30 |
| `ExpenseService` | Submit / approve / reject / reimburse / list / totals / reports | 5 | 1 | 23 |
| `ExpenseAppMain` | Demo and automatic self-checks | 8 | 1 (private) | 23 |

(Counts were obtained by running a small Java reflection program against the compiled classes.)

## 5. Software and hardware requirements

**Software**

- Windows 10/11 (any OS with a JDK works)
- JDK 21 (code is compatible with Java 17+)
- Eclipse IDE for Java Developers (used for the final run and screenshots)
- Visual Studio Code (used for development)
- Git and a GitHub account

**Hardware (minimum)**

- Processor: dual-core, 1.5 GHz or better
- RAM: 4 GB (8 GB recommended for Eclipse)
- Disk space: 1 GB free
- Display: 1366 x 768 or better

## 6. OOP concepts used

### 6.1 Where each concept appears

| Concept | Implementation |
|---|---|
| **Inheritance** | `Manager extends Employee`; constructor calls `super(...)`; inherits `getName()`, `getEmail()`, `calculateTotal()`, `reportExpenses()` |
| **Method overloading** | `ExpenseService.submit()` with 6 / 7 / 8 parameters (basic / + attachment / + attachment + cost center); `calculateTotal()` and `calculateMonthlyTotal()` also have several versions |
| **Method overriding** | `Manager` overrides `getApprovalBlocker()`, `approve()`, `reject()`, `getRole()`, `displayDetails()`; `approve()` follows grade-specific rules (Junior: Meals only; Senior: receipt above Rs. 5,000; Director: limit only) and always checks the approval limit |
| **Polymorphism** | `List<Employee>` holds `Manager` objects; `ExpenseService.approve(String, Employee)` takes an `Employee`; the same call `p.getApprovalBlocker(expense)` gives different results for Employee, Junior, Senior and Director (dynamic method dispatch) |
| **Encapsulation** | Private fields; no `setStatus()`/`setAmount()`; state changes only through package-private `approve()`/`reject()`/`reimburse()` in `Expense` that enforce legal transitions; `updateAmount()` only for `PENDING` claims; read-only list views |

### 6.2 Design notes and trade-offs

- **`Rs.` instead of the rupee sign.** The Windows Eclipse Console often uses a code page that cannot display the rupee sign and shows `?`. Plain `Rs.` works everywhere. All source files are pure ASCII for the same reason.
- **`double` for money.** Chosen because the assignment is a beginner OOP exercise and sample amounts have at most two decimals. A production system should use `BigDecimal` or integer paise.
- **Package-private state changes.** `Expense.approve()/reject()/reimburse()` are not public, so code outside the package cannot call them. `ExpenseAppMain` is in the same package (as the assignment requires `com.expensemanagement`), so it *could* call them; it deliberately goes through `Manager`/`ExpenseService` instead. A larger system would put the demo in a separate package.
- **Approval result message stored in the approver.** `approve()` returns a boolean and the explanation is read with `getLastMessage()`. This keeps the code simple for beginners but is not thread-safe; a separate result object would be cleaner.
- **Two ways to compute totals.** `Employee.calculateTotal()` (from the employee's own list) and `ExpenseService.calculateTotal(Employee)` (from the service's list) are computed independently and compared in a self-check; this is intentional redundancy for verification.
- **`Manager` validates before `super()`.** A static helper inside the `super(...)` argument validates the limit and grade first, so an invalid manager is never counted in `Employee`'s static counter.
- **Grade as a nested enum.** `Manager.Grade` is nested in `Manager` instead of being a separate file, keeping the required 5-file structure.

## 7. Algorithm / program workflow

1. Start `ExpenseAppMain.main()`.
2. Create 3 `Employee` and 3 `Manager` objects (stored as `Employee` references) and display them.
3. Submit 8 expenses using the three `submit()` overloads. For each: validate ID format, reject duplicates, validate type and amount, create a `PENDING` expense, add it to the service list and to the requester's list.
4. For every approval attempt, `ExpenseService.approve(id, approver)` calls `approver.approve(expense)`:
   - Plain `Employee` -> denied (no authority).
   - `Manager.getApprovalBlocker()` checks, in order: status is `PENDING`; not own expense; Junior -> Meals only; amount <= approval limit; Senior -> receipt needed above Rs. 5,000.
   - If a rule fails, the expense stays `PENDING` and the message says it needs **escalation** or **another authorized manager**.
   - Otherwise the expense becomes `APPROVED`.
5. A manager may reject a `PENDING` expense (not their own) with a mandatory reason -> `REJECTED`.
6. `reimburse(id, date)` is allowed only for `APPROVED` expenses -> `REIMBURSED`. Pending, rejected and already reimbursed expenses are blocked.
7. Print expenses grouped by status, per-employee totals, and monthly summaries (sorted by month).
8. Demonstrate each OOP concept with real calls and run validation tests with invalid input.
9. Run 18 self-checks and print `Self-checks passed: n / m`.

## 8. Source code

The same files are in `src/com/expensemanagement/`.

### 8.1 Employee.java

```java
package com.expensemanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Employee - a person who can submit expenses.
 *
 * This is the BASE class. Manager extends Employee and overrides the
 * approval behaviour (getApprovalBlocker, approve, reject, getRole,
 * displayDetails). A plain Employee has NO authority to approve anything.
 *
 * Encapsulation: every field is private; data is changed only through
 * validated setters or package-private helpers.
 */
public class Employee {

    // ---- static variable (shared by all employees and managers) ----
    private static int employeeCount = 0;

    // ---- instance variables ----
    private final String empId;     // never changes after creation
    private String name;
    private String dept;
    private String email;
    private final List<Expense> submittedExpenses = new ArrayList<>();
    private String lastMessage = "";   // result text of the last approve/reject attempt

    /** Creates an employee. Throws IllegalArgumentException for blank/invalid data. */
    public Employee(String empId, String name, String dept, String email) {
        this.empId = requireText(empId, "Employee ID");
        this.name = requireText(name, "Name");
        this.dept = requireText(dept, "Department");
        this.email = requireEmail(email);
        employeeCount++;
    }

    // ------------------------------------------------------------------
    // Getters and validated setters
    // ------------------------------------------------------------------
    public String getEmpId() { return empId; }
    public String getName()  { return name; }
    public String getDept()  { return dept; }
    public String getEmail() { return email; }

    public void setName(String name)   { this.name = requireText(name, "Name"); }
    public void setDept(String dept)   { this.dept = requireText(dept, "Department"); }
    public void setEmail(String email) { this.email = requireEmail(email); }

    /** Result message of the most recent approve()/reject() call on this object. */
    public String getLastMessage() { return lastMessage; }

    /** Subclasses use this to record the outcome of an approval decision. */
    protected void setLastMessage(String message) { this.lastMessage = message; }

    public static int getEmployeeCount() { return employeeCount; }

    // ------------------------------------------------------------------
    // Behaviour that Manager overrides (runtime polymorphism)
    // ------------------------------------------------------------------

    /** Role label shown in reports. Overridden by Manager. */
    public String getRole() { return "Employee"; }

    /** Prints one line of details. Overridden by Manager (which adds more). */
    public void displayDetails() {
        System.out.printf("  %-9s %-5s %-14s %-12s %s%n",
                getRole(), empId, name, dept, email);
    }

    /**
     * Returns the reason this person CANNOT approve the expense, or null if
     * they can. A plain Employee can never approve. Manager overrides this.
     */
    public String getApprovalBlocker(Expense expense) {
        return name + " (" + getRole() + ") has no authority to approve expenses.";
    }

    /** True when getApprovalBlocker() finds no problem. */
    public boolean canApprove(Expense expense) {
        return getApprovalBlocker(expense) == null;
    }

    /** Employees cannot approve. Manager overrides this with grade-based logic. */
    public boolean approve(Expense expense) {
        lastMessage = "[DENIED]   " + getApprovalBlocker(expense);
        return false;
    }

    /** Employees cannot reject. Manager overrides this. */
    public boolean reject(Expense expense, String reason) {
        lastMessage = "[DENIED]   " + name + " (" + getRole()
                + ") has no authority to reject expenses.";
        return false;
    }

    // ------------------------------------------------------------------
    // Expense reporting for this employee
    // ------------------------------------------------------------------

    /** Called by ExpenseService (same package) when this employee submits a claim. */
    void addExpense(Expense expense) { submittedExpenses.add(expense); }

    /** Read-only view of this employee's expenses (callers cannot modify the list). */
    public List<Expense> getSubmittedExpenses() {
        return Collections.unmodifiableList(submittedExpenses);
    }

    /** Total amount of ALL expenses this employee has submitted. */
    public double calculateTotal() {
        double total = 0;
        for (Expense e : submittedExpenses) {
            total += e.getAmount();
        }
        return total;
    }

    /** Total of only those expenses that are currently in the given status. */
    public double calculateTotal(Expense.Status status) {
        double total = 0;
        for (Expense e : submittedExpenses) {
            if (e.getStatus() == status) {
                total += e.getAmount();
            }
        }
        return total;
    }

    /** Prints every expense this employee has submitted. */
    public void reportExpenses() {
        System.out.println("  Expenses of " + name + " (" + empId + "): "
                + submittedExpenses.size() + " claim(s), total "
                + Expense.formatMoney(calculateTotal()));
        System.out.println("  " + Expense.summaryHeader());
        for (Expense e : submittedExpenses) {
            System.out.println("  " + e.summaryLine());
        }
    }

    @Override
    public String toString() { return name + " (" + empId + ")"; }

    // ------------------------------------------------------------------
    // Private validation helpers
    // ------------------------------------------------------------------
    private static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " must not be blank.");
        }
        return value.trim();
    }

    private static String requireEmail(String value) {
        String text = requireText(value, "Email");
        int at = text.indexOf('@');
        if (at < 1 || text.indexOf('.', at) < 0 || text.contains(" ")) {
            throw new IllegalArgumentException("Invalid email address: " + text);
        }
        return text;
    }
}
```

### 8.2 Manager.java

```java
package com.expensemanagement;

/**
 * Manager - an Employee who can approve or reject expenses.
 *
 * INHERITANCE : Manager extends Employee (name, email, expense list, ... are inherited).
 * OVERRIDING  : getApprovalBlocker(), approve(), reject(), getRole() and
 *               displayDetails() replace the Employee versions.
 *
 * GRADE-BASED APPROVAL RULES (checked in this order):
 *   1. Only PENDING expenses can be approved.
 *   2. A manager can never approve their own expense.
 *   3. JUNIOR   : may approve MEALS only (Travel must be escalated).
 *   4. Every grade: amount must not exceed the manager's approvalLimit.
 *   5. SENIOR   : claims above RECEIPT_THRESHOLD need an attachment (receipt).
 *   6. DIRECTOR : no extra restriction apart from the approval limit.
 */
public class Manager extends Employee {

    /** Manager grade (level). Different grades follow different approval rules. */
    public enum Grade {
        JUNIOR("Junior (L1)"), SENIOR("Senior (L2)"), DIRECTOR("Director (L3)");

        private final String label;

        Grade(String label) { this.label = label; }

        public String getLabel() { return label; }
    }

    // ---- static variables ----
    /** Senior managers need a receipt (attachment) for claims above this amount. */
    public static final double RECEIPT_THRESHOLD = 5000.0;
    private static int managerCount = 0;

    // ---- instance variables ----
    private double approvalLimit;
    private final Grade grade;
    private int approvedCount = 0;
    private int rejectedCount = 0;
    private double approvedTotal = 0;

    /** Calls the Employee constructor with super(), then sets manager-only fields. */
    public Manager(String empId, String name, String dept, String email,
                   double approvalLimit, Grade grade) {
        // Manager-only data is validated BEFORE super() runs, so an invalid Manager
        // is never counted in Employee.employeeCount.
        super(checkManagerArguments(empId, approvalLimit, grade), name, dept, email);
        this.approvalLimit = approvalLimit;
        this.grade = grade;
        managerCount++;
    }

    // ------------------------------------------------------------------
    // Getters / setters
    // ------------------------------------------------------------------
    public double getApprovalLimit() { return approvalLimit; }

    public void setApprovalLimit(double approvalLimit) {
        this.approvalLimit = validateLimit(approvalLimit);
    }

    public Grade getGrade()          { return grade; }
    public int getApprovedCount()    { return approvedCount; }
    public int getRejectedCount()    { return rejectedCount; }
    public double getApprovedTotal() { return approvedTotal; }
    public static int getManagerCount() { return managerCount; }

    /** One-line description of what this manager may approve. */
    public String getApprovalPolicy() {
        switch (grade) {
            case JUNIOR:
                return "Meals only, up to " + Expense.formatMoney(approvalLimit);
            case SENIOR:
                return "Any type up to " + Expense.formatMoney(approvalLimit)
                        + "; receipt above " + Expense.formatMoney(RECEIPT_THRESHOLD);
            default:
                return "Any type up to " + Expense.formatMoney(approvalLimit);
        }
    }

    // ------------------------------------------------------------------
    // OVERRIDDEN methods (method overriding + runtime polymorphism)
    // ------------------------------------------------------------------

    @Override
    public String getRole() { return "Manager"; }

    /** Calls the parent version first (super), then prints the manager-only details. */
    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("            -> Grade: " + grade.getLabel()
                + " | Limit: " + Expense.formatMoney(approvalLimit));
        System.out.println("            -> Rule : " + getApprovalPolicy());
    }

    /**
     * Grade-based authority check. Returns null when this manager may approve
     * the expense, otherwise a message explaining why not (and what to do next).
     */
    @Override
    public String getApprovalBlocker(Expense expense) {
        if (expense == null) {
            return "No expense was supplied.";
        }
        String id = expense.getExpenseId();

        // Rule 1: state check
        if (expense.getStatus() != Expense.Status.PENDING) {
            return "Expense " + id + " is already " + expense.getStatus()
                    + "; only PENDING expenses can be approved.";
        }
        // Rule 2: no self-approval
        if (expense.getRequester().getEmpId().equals(getEmpId())) {
            return getName() + " cannot approve their own expense " + id
                    + ". Needs ANOTHER AUTHORIZED MANAGER.";
        }
        // Rule 3: junior managers handle Meals only
        if (grade == Grade.JUNIOR && expense.getType() != Expense.Type.MEALS) {
            return "Grade rule: " + grade.getLabel() + " managers can approve Meals only. "
                    + id + " (" + expense.getType().getLabel()
                    + ") needs ESCALATION to a Senior or Director manager.";
        }
        // Rule 4: approval limit
        if (expense.getAmount() > approvalLimit) {
            return "Amount " + Expense.formatMoney(expense.getAmount()) + " of " + id
                    + " exceeds " + getName() + "'s approval limit of "
                    + Expense.formatMoney(approvalLimit)
                    + ". Needs ESCALATION to a manager with a higher limit.";
        }
        // Rule 5: senior managers need a receipt for larger claims
        if (grade == Grade.SENIOR && expense.getAmount() > RECEIPT_THRESHOLD
                && !expense.hasAttachment()) {
            return "Grade rule: " + grade.getLabel() + " managers need an attachment (receipt) "
                    + "for claims above " + Expense.formatMoney(RECEIPT_THRESHOLD) + ". "
                    + id + " has none. Ask the employee to attach it, or ESCALATE to a Director.";
        }
        return null;   // allowed
    }

    /** Approves the expense if getApprovalBlocker() allows it; otherwise explains why not. */
    @Override
    public boolean approve(Expense expense) {
        String blocker = getApprovalBlocker(expense);
        if (blocker != null) {
            setLastMessage("[DENIED]   " + blocker);
            return false;
        }
        expense.approve(getName());      // package-private state change
        approvedCount++;
        approvedTotal += expense.getAmount();
        setLastMessage("[APPROVED] " + expense.getExpenseId() + " ("
                + Expense.formatMoney(expense.getAmount()) + ") approved by "
                + getName() + " [" + grade.getLabel() + "].");
        return true;
    }

    /** Any manager may reject a PENDING expense of another employee, with a reason. */
    @Override
    public boolean reject(Expense expense, String reason) {
        if (expense == null) {
            setLastMessage("[DENIED]   No expense was supplied.");
            return false;
        }
        if (expense.getStatus() != Expense.Status.PENDING) {
            setLastMessage("[DENIED]   Expense " + expense.getExpenseId() + " is already "
                    + expense.getStatus() + "; only PENDING expenses can be rejected.");
            return false;
        }
        if (expense.getRequester().getEmpId().equals(getEmpId())) {
            setLastMessage("[DENIED]   " + getName() + " cannot reject their own expense "
                    + expense.getExpenseId() + ".");
            return false;
        }
        expense.reject(getName(), reason);   // throws if reason is blank
        rejectedCount++;
        setLastMessage("[REJECTED] " + expense.getExpenseId() + " rejected by "
                + getName() + ". Reason: " + reason.trim());
        return true;
    }

    // ------------------------------------------------------------------
    /** Validates manager-only arguments and returns empId unchanged (used inside super(...)). */
    private static String checkManagerArguments(String empId, double limit, Grade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("Manager grade must not be null.");
        }
        validateLimit(limit);
        return empId;
    }

    private static double validateLimit(double limit) {
        if (Double.isNaN(limit) || Double.isInfinite(limit) || limit <= 0) {
            throw new IllegalArgumentException("Approval limit must be a positive number.");
        }
        return limit;
    }
}
```

### 8.3 Expense.java

```java
package com.expensemanagement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;

/**
 * Expense - one expense claim submitted by an employee.
 *
 * ENCAPSULATION:
 *  - All fields are private; there is NO public setStatus() or setAmount().
 *  - Status can change only through approve(), reject() and reimburse().
 *    These are package-private, so outside code must go through
 *    Manager / ExpenseService, which enforce the business rules.
 *  - The amount can be corrected only via updateAmount(), and only while
 *    the claim is still PENDING.
 */
public class Expense {

    /** Allowed expense types (Travel / Meals only). */
    public enum Type {
        TRAVEL("Travel"), MEALS("Meals");

        private final String label;

        Type(String label) { this.label = label; }

        public String getLabel() { return label; }

        /** Converts text such as "travel" or "Meals" to a Type; rejects anything else. */
        public static Type fromString(String text) {
            if (text != null) {
                for (Type t : values()) {
                    if (t.label.equalsIgnoreCase(text.trim())) {
                        return t;
                    }
                }
            }
            throw new IllegalArgumentException(
                    "Invalid expense type '" + text + "'. Allowed types: Travel, Meals.");
        }
    }

    /** Life-cycle of a claim: PENDING -> APPROVED -> REIMBURSED, or PENDING -> REJECTED. */
    public enum Status { PENDING, APPROVED, REJECTED, REIMBURSED }

    // ---- static variable ----
    private static int totalCreated = 0;

    // ---- instance variables (all private) ----
    private final String expenseId;
    private final Type type;
    private double amount;
    private Status status;
    private final Employee requester;
    private final LocalDate expenseDate;
    private final String description;
    private final String attachmentName;   // may be null
    private final String costCenter;       // may be null
    private String decidedBy;              // manager who approved/rejected
    private String remarks;
    private LocalDate reimbursedOn;

    /**
     * Creates a PENDING expense. Validates the type and amount.
     * attachmentName and costCenter are optional (pass null when not used).
     */
    public Expense(String expenseId, Employee requester, String typeText, double amount,
                   LocalDate expenseDate, String description,
                   String attachmentName, String costCenter) {
        if (expenseId == null || expenseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense ID must not be blank.");
        }
        if (requester == null) {
            throw new IllegalArgumentException("Requester (employee) must not be null.");
        }
        if (expenseDate == null) {
            throw new IllegalArgumentException("Expense date must not be null.");
        }
        this.type = Type.fromString(typeText);
        this.amount = validateAmount(amount);
        this.expenseId = expenseId.trim();
        this.requester = requester;
        this.expenseDate = expenseDate;
        this.description = (description == null) ? "" : description.trim();
        this.attachmentName = blankToNull(attachmentName);
        this.costCenter = blankToNull(costCenter);
        this.status = Status.PENDING;
        this.remarks = "Awaiting approval";
        totalCreated++;
    }

    // ------------------------------------------------------------------
    // Getters (no setters for id, type, status, requester)
    // ------------------------------------------------------------------
    public String getExpenseId()       { return expenseId; }
    public Type getType()              { return type; }
    public double getAmount()          { return amount; }
    public Status getStatus()          { return status; }
    public Employee getRequester()     { return requester; }
    public LocalDate getExpenseDate()  { return expenseDate; }
    public String getDescription()     { return description; }
    public String getAttachmentName()  { return attachmentName; }
    public String getCostCenter()      { return costCenter; }
    public String getDecidedBy()       { return decidedBy; }
    public String getRemarks()         { return remarks; }
    public LocalDate getReimbursedOn() { return reimbursedOn; }
    public static int getTotalCreated() { return totalCreated; }

    public boolean hasAttachment() { return attachmentName != null; }
    public boolean isPending()     { return status == Status.PENDING; }

    /** Month the expense belongs to (used for monthly summaries). */
    public YearMonth getMonth() { return YearMonth.from(expenseDate); }

    // ------------------------------------------------------------------
    // Controlled state transitions (package-private = not public API)
    // ------------------------------------------------------------------

    /** PENDING -> APPROVED. Any other current status is an illegal transition. */
    void approve(String approverName) {
        requireStatus(Status.PENDING, "approve");
        status = Status.APPROVED;
        decidedBy = approverName;
        remarks = "Approved by " + approverName;
    }

    /** PENDING -> REJECTED, with a mandatory reason. */
    void reject(String approverName, String reason) {
        requireStatus(Status.PENDING, "reject");
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("A rejection reason is required.");
        }
        status = Status.REJECTED;
        decidedBy = approverName;
        remarks = "Rejected by " + approverName + ": " + reason.trim();
    }

    /** APPROVED -> REIMBURSED. Rejected or pending expenses can never be reimbursed. */
    void reimburse(LocalDate paymentDate) {
        if (status == Status.REJECTED) {
            throw new IllegalStateException("Expense " + expenseId
                    + " was REJECTED, so it cannot be reimbursed.");
        }
        if (status == Status.PENDING) {
            throw new IllegalStateException("Expense " + expenseId
                    + " is still PENDING; it must be APPROVED before reimbursement.");
        }
        if (status == Status.REIMBURSED) {
            throw new IllegalStateException("Expense " + expenseId
                    + " was already reimbursed on " + reimbursedOn + ".");
        }
        if (paymentDate == null || paymentDate.isBefore(expenseDate)) {
            throw new IllegalArgumentException(
                    "Reimbursement date cannot be missing or before the expense date.");
        }
        status = Status.REIMBURSED;
        reimbursedOn = paymentDate;
        remarks = "Reimbursed on " + paymentDate;
    }

    /**
     * The only way to change the amount: allowed ONLY while PENDING and
     * only to a valid positive value.
     */
    public void updateAmount(double newAmount) {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Amount of expense " + expenseId
                    + " cannot be edited because it is " + status + ".");
        }
        this.amount = validateAmount(newAmount);
    }

    // ------------------------------------------------------------------
    // Display helpers
    // ------------------------------------------------------------------

    /** Column headings that match summaryLine(). */
    public static String summaryHeader() {
        return String.format("%-8s %-7s %12s %-11s %-14s %s",
                "ID", "Type", "Amount(Rs.)", "Status", "Requester", "Date");
    }

    /** One formatted table row for this expense. */
    public String summaryLine() {
        return String.format("%-8s %-7s %12s %-11s %-14s %s",
                expenseId, type.getLabel(), formatAmount(amount), status,
                requester.getName(), expenseDate);
    }

    /** Attachment / cost-center text used when showing a submission. */
    public String describeExtras() {
        return "Attachment: " + (attachmentName == null ? "none" : attachmentName)
                + " | Cost Center: " + (costCenter == null ? "none" : costCenter);
    }

    /** Prints all details of this expense. */
    public void display() {
        System.out.println("  Expense ID   : " + expenseId);
        System.out.println("  Type         : " + type.getLabel());
        System.out.println("  Amount       : " + formatMoney(amount));
        System.out.println("  Status       : " + status);
        System.out.println("  Requester    : " + requester);
        System.out.println("  Date         : " + expenseDate);
        System.out.println("  Description  : " + description);
        System.out.println("  " + describeExtras());
        System.out.println("  Remarks      : " + remarks);
    }

    /** "1,234.50" - fixed Locale so output is identical on every computer. */
    public static String formatAmount(double value) {
        return String.format(Locale.US, "%,.2f", value);
    }

    /** "Rs. 1,234.50" (plain ASCII so every console shows it correctly). */
    public static String formatMoney(double value) {
        return "Rs. " + formatAmount(value);
    }

    @Override
    public String toString() {
        return expenseId + " [" + type.getLabel() + ", " + formatMoney(amount) + ", " + status + "]";
    }

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------
    private void requireStatus(Status required, String action) {
        if (status != required) {
            throw new IllegalStateException("Cannot " + action + " expense " + expenseId
                    + ": current status is " + status + " (required: " + required + ").");
        }
    }

    private static double validateAmount(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value) || value <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be a positive number, but was " + value + ".");
        }
        return value;
    }

    private static String blankToNull(String text) {
        return (text == null || text.trim().isEmpty()) ? null : text.trim();
    }
}
```

### 8.4 ExpenseService.java

```java
package com.expensemanagement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * ExpenseService - the business logic: submit, approve, reject, reimburse,
 * search, list and summarise expenses.
 *
 * METHOD OVERLOADING : submit() exists in three versions
 *                      (basic / with attachment / with attachment + cost center);
 *                      calculateTotal() and calculateMonthlyTotal() are overloaded too.
 * POLYMORPHISM       : approve() and reject() accept an Employee reference. At run time
 *                      the Employee or Manager version of the method is executed.
 */
public class ExpenseService {

    // ---- static variables ----
    private static int totalSubmissions = 0;
    private static final Pattern ID_PATTERN = Pattern.compile("EXP-\\d{3,}");

    // ---- instance variables ----
    private final String companyName;
    private final List<Expense> expenses = new ArrayList<>();
    private final List<String> auditLog = new ArrayList<>();

    public ExpenseService(String companyName) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name must not be blank.");
        }
        this.companyName = companyName.trim();
    }

    // ------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------
    public String getCompanyName()   { return companyName; }
    public int getExpenseCount()     { return expenses.size(); }
    public static int getTotalSubmissions() { return totalSubmissions; }

    /** Read-only copy of the audit trail. */
    public List<String> getAuditLog() { return Collections.unmodifiableList(auditLog); }

    // ------------------------------------------------------------------
    // SUBMIT - three overloaded versions
    // ------------------------------------------------------------------

    /** Overload 1: basic expense, no attachment and no cost center. */
    public Expense submit(String expenseId, Employee requester, String type,
                          double amount, LocalDate date, String description) {
        return submit(expenseId, requester, type, amount, date, description, null, null);
    }

    /** Overload 2: expense with an attachment (e.g. a scanned receipt). */
    public Expense submit(String expenseId, Employee requester, String type,
                          double amount, LocalDate date, String description,
                          String attachmentName) {
        return submit(expenseId, requester, type, amount, date, description,
                attachmentName, null);
    }

    /**
     * Overload 3: expense with attachment AND cost center. All other overloads end here.
     * Validates the ID format, rejects duplicates, and lets Expense validate type/amount.
     */
    public Expense submit(String expenseId, Employee requester, String type,
                          double amount, LocalDate date, String description,
                          String attachmentName, String costCenter) {
        if (expenseId == null || !ID_PATTERN.matcher(expenseId.trim()).matches()) {
            throw new IllegalArgumentException("Invalid expense ID '" + expenseId
                    + "'. Expected format: EXP-001.");
        }
        if (exists(expenseId)) {
            throw new IllegalArgumentException("Duplicate expense ID: " + expenseId.trim()
                    + " is already in use.");
        }
        Expense expense = new Expense(expenseId, requester, type, amount, date,
                description, attachmentName, costCenter);   // validates type & amount
        expenses.add(expense);
        requester.addExpense(expense);
        totalSubmissions++;
        audit("SUBMITTED  " + expense.getExpenseId() + " by " + requester.getName());
        return expense;
    }

    // ------------------------------------------------------------------
    // APPROVE / REJECT / REIMBURSE
    // ------------------------------------------------------------------

    /**
     * Asks 'approver' to approve the expense. Because the parameter type is Employee,
     * the JVM calls Manager.approve() for managers and Employee.approve() for others
     * (dynamic method dispatch). Returns true only if the expense was approved.
     * Read approver.getLastMessage() for the explanation.
     */
    public boolean approve(String expenseId, Employee approver) {
        requireApprover(approver);
        Expense expense = findById(expenseId);
        boolean approved = approver.approve(expense);
        audit((approved ? "APPROVED   " : "NOT APPROVED ") + expense.getExpenseId()
                + " by " + approver.getName());
        return approved;
    }

    /** Asks 'approver' to reject the expense for the given reason. */
    public boolean reject(String expenseId, Employee approver, String reason) {
        requireApprover(approver);
        Expense expense = findById(expenseId);
        boolean rejected = approver.reject(expense, reason);
        audit((rejected ? "REJECTED   " : "NOT REJECTED ") + expense.getExpenseId()
                + " by " + approver.getName());
        return rejected;
    }

    /**
     * Marks an APPROVED expense as REIMBURSED. Throws IllegalStateException for
     * PENDING, REJECTED or already REIMBURSED expenses.
     */
    public Expense reimburse(String expenseId, LocalDate paymentDate) {
        Expense expense = findById(expenseId);
        expense.reimburse(paymentDate);
        audit("REIMBURSED " + expense.getExpenseId() + " on " + paymentDate);
        return expense;
    }

    // ------------------------------------------------------------------
    // FIND / LIST
    // ------------------------------------------------------------------

    /** True if an expense with this ID already exists. */
    public boolean exists(String expenseId) {
        if (expenseId == null) {
            return false;
        }
        for (Expense e : expenses) {
            if (e.getExpenseId().equalsIgnoreCase(expenseId.trim())) {
                return true;
            }
        }
        return false;
    }

    /** Finds an expense by ID; throws IllegalArgumentException if the ID is blank/unknown. */
    public Expense findById(String expenseId) {
        if (expenseId == null || expenseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense ID must not be blank.");
        }
        for (Expense e : expenses) {
            if (e.getExpenseId().equalsIgnoreCase(expenseId.trim())) {
                return e;
            }
        }
        throw new IllegalArgumentException("No expense found with ID " + expenseId.trim() + ".");
    }

    /** Returns every expense currently in the given status (in submission order). */
    public List<Expense> listByStatus(Expense.Status status) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getStatus() == status) {
                result.add(e);
            }
        }
        return result;
    }

    /** Prints the expenses of one status as a small table. */
    public void printByStatus(Expense.Status status) {
        List<Expense> list = listByStatus(status);
        System.out.println();
        System.out.println("Status: " + status + "  (" + list.size() + " expense(s), total "
                + Expense.formatMoney(calculateTotal(status)) + ")");
        if (list.isEmpty()) {
            System.out.println("  (none)");
            return;
        }
        System.out.println("  " + Expense.summaryHeader());
        for (Expense e : list) {
            System.out.println("  " + e.summaryLine());
        }
    }

    // ------------------------------------------------------------------
    // TOTALS - overloaded calculateTotal()
    // ------------------------------------------------------------------

    /** Total of ALL expenses submitted by one employee (any status). */
    public double calculateTotal(Employee employee) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getRequester().getEmpId().equals(employee.getEmpId())) {
                total += e.getAmount();
            }
        }
        return total;
    }

    /** Total of one employee's expenses that are in a particular status. */
    public double calculateTotal(Employee employee, Expense.Status status) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getRequester().getEmpId().equals(employee.getEmpId())
                    && e.getStatus() == status) {
                total += e.getAmount();
            }
        }
        return total;
    }

    /** Total of all expenses in a particular status (all employees). */
    public double calculateTotal(Expense.Status status) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getStatus() == status) {
                total += e.getAmount();
            }
        }
        return total;
    }

    /** Total of all expenses in a calendar month. */
    public double calculateMonthlyTotal(YearMonth month) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getMonth().equals(month)) {
                total += e.getAmount();
            }
        }
        return total;
    }

    /** Total of one expense type (Travel/Meals) in a calendar month. */
    public double calculateMonthlyTotal(YearMonth month, Expense.Type type) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getMonth().equals(month) && e.getType() == type) {
                total += e.getAmount();
            }
        }
        return total;
    }

    // ------------------------------------------------------------------
    // REPORTS
    // ------------------------------------------------------------------

    /** Prints one row per person: total claimed and the split by status. */
    public void printEmployeeTotals(List<? extends Employee> people) {
        System.out.println("(amounts in Rs.; 'Approved' = approved but not yet reimbursed)");
        System.out.printf("%-5s %-15s %11s %10s %10s %10s %11s%n",
                "ID", "Name", "Total", "Pending", "Approved", "Rejected", "Reimbursed");
        double grand = 0;
        for (Employee p : people) {
            double total = calculateTotal(p);
            grand += total;
            System.out.printf("%-5s %-15s %11s %10s %10s %10s %11s%n",
                    p.getEmpId(), p.getName(), Expense.formatAmount(total),
                    Expense.formatAmount(calculateTotal(p, Expense.Status.PENDING)),
                    Expense.formatAmount(calculateTotal(p, Expense.Status.APPROVED)),
                    Expense.formatAmount(calculateTotal(p, Expense.Status.REJECTED)),
                    Expense.formatAmount(calculateTotal(p, Expense.Status.REIMBURSED)));
        }
        System.out.printf("%-21s %11s%n", "GRAND TOTAL", Expense.formatAmount(grand));
    }

    /** Prints a month-by-month summary (months sorted oldest to newest). */
    public void printMonthlySummary() {
        Map<YearMonth, List<Expense>> byMonth = new TreeMap<>();
        for (Expense e : expenses) {
            byMonth.computeIfAbsent(e.getMonth(), k -> new ArrayList<>()).add(e);
        }
        System.out.println("(amounts in Rs.; 'Paid' = reimbursed so far)");
        System.out.printf("%-8s %6s %12s %12s %12s %12s%n",
                "Month", "Claims", "Travel", "Meals", "Total", "Paid");
        double grandTotal = 0;
        for (Map.Entry<YearMonth, List<Expense>> entry : byMonth.entrySet()) {
            YearMonth month = entry.getKey();
            double paid = 0;
            for (Expense e : entry.getValue()) {
                if (e.getStatus() == Expense.Status.REIMBURSED) {
                    paid += e.getAmount();
                }
            }
            double total = calculateMonthlyTotal(month);
            grandTotal += total;
            System.out.printf("%-8s %6d %12s %12s %12s %12s%n",
                    month, entry.getValue().size(),
                    Expense.formatAmount(calculateMonthlyTotal(month, Expense.Type.TRAVEL)),
                    Expense.formatAmount(calculateMonthlyTotal(month, Expense.Type.MEALS)),
                    Expense.formatAmount(total), Expense.formatAmount(paid));
        }
        System.out.printf("%-8s %6d %38s%n", "ALL", expenses.size(),
                Expense.formatAmount(grandTotal));
    }

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------
    private void requireApprover(Employee approver) {
        if (approver == null) {
            throw new IllegalArgumentException("Approver must not be null.");
        }
    }

    private void audit(String entry) {
        auditLog.add(entry);
    }
}
```

### 8.5 ExpenseAppMain.java

```java
package com.expensemanagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ExpenseAppMain - runs the complete Expense Management demo with fixed sample
 * data (no keyboard input, no database, no external files).
 *
 * All dates are fixed, so the output is the same every time the program runs.
 * Amounts are in Indian rupees, written "Rs." so that every console
 * (including the Windows Eclipse console) displays them correctly.
 */
public class ExpenseAppMain {

    // ---- static variables ----
    private static final int WIDTH = 78;   // console width used for wrapping long messages
    private static final String LINE = "=".repeat(WIDTH);
    private static final String SUB_LINE = "-".repeat(WIDTH);
    private static final ExpenseService service =
            new ExpenseService("Chennai Tech Solutions Pvt Ltd");
    // Declared as Employee: managers are stored here too (polymorphism).
    private static final List<Employee> people = new ArrayList<>();
    private static int stepNo = 0;
    private static int checksRun = 0;
    private static int checksPassed = 0;

    /** Not meant to be instantiated; everything here is static. */
    private ExpenseAppMain() { }

    public static void main(String[] args) {
        printBanner();
        createPeople();
        submitExpenses();
        processApprovals();
        processRejection();
        processReimbursements();
        printReports();
        demonstrateOopConcepts();
        runValidationTests();
        runSelfChecks();
        printFooter();
    }

    // ==================================================================
    // STEP 1 - create employees and managers
    // ==================================================================
    private static void createPeople() {
        printHeading("CREATE EMPLOYEES AND MANAGERS");
        people.add(new Employee("E101", "Aarav Sharma", "Engineering", "aarav.sharma@chennaitech.in"));
        people.add(new Employee("E102", "Priya Nair", "Marketing", "priya.nair@chennaitech.in"));
        people.add(new Employee("E103", "Karthik Raman", "Finance", "karthik.raman@chennaitech.in"));
        // Manager objects stored in a List<Employee>: an Employee reference can hold a Manager.
        people.add(new Manager("M201", "Meera Iyer", "Operations", "meera.iyer@chennaitech.in",
                20000.00, Manager.Grade.SENIOR));
        people.add(new Manager("M202", "Rajesh Kumar", "Finance", "rajesh.kumar@chennaitech.in",
                100000.00, Manager.Grade.DIRECTOR));
        people.add(new Manager("M203", "Divya Menon", "Sales", "divya.menon@chennaitech.in",
                5000.00, Manager.Grade.JUNIOR));

        System.out.println("Company : " + service.getCompanyName());
        System.out.printf("  %-9s %-5s %-14s %-12s %s%n", "Role", "ID", "Name", "Dept", "Email");
        for (Employee p : people) {
            p.displayDetails();   // Employee version or Manager version, decided at run time
        }
        System.out.println("Created " + Employee.getEmployeeCount() + " people ("
                + (Employee.getEmployeeCount() - Manager.getManagerCount()) + " employees, "
                + Manager.getManagerCount() + " managers).");
    }

    // ==================================================================
    // STEP 2 - submit expenses using the three overloaded submit() methods
    // ==================================================================
    private static void submitExpenses() {
        printHeading("SUBMIT EXPENSES (OVERLOADED submit() METHODS)");

        printSubHeading("Overload 1: submit(id, employee, type, amount, date, description)");
        show(service.submit("EXP-001", person("E101"), "Travel", 4500.00,
                LocalDate.of(2026, 9, 12), "Train tickets Chennai-Bengaluru, client visit"));
        show(service.submit("EXP-004", person("E101"), "Meals", 850.00,
                LocalDate.of(2026, 10, 3), "Team lunch after sprint review"));
        show(service.submit("EXP-007", person("E101"), "Travel", 7800.00,
                LocalDate.of(2026, 10, 10), "Cab and toll charges, vendor audit"));
        show(service.submit("EXP-008", person("M201"), "Meals", 1800.00,
                LocalDate.of(2026, 10, 12), "Working dinner with vendors"));

        printSubHeading("Overload 2: submit(..., attachmentName)");
        show(service.submit("EXP-002", person("E102"), "Meals", 1200.00,
                LocalDate.of(2026, 9, 15), "Client lunch, product launch",
                "lunch_bill_0915.pdf"));

        printSubHeading("Overload 3: submit(..., attachmentName, costCenter)");
        show(service.submit("EXP-003", person("E103"), "Travel", 18500.00,
                LocalDate.of(2026, 9, 20), "Flight Chennai-Delhi, tax meeting",
                "flight_ticket_MAA-DEL.pdf", "CC-FIN-101"));
        show(service.submit("EXP-005", person("E102"), "Travel", 65000.00,
                LocalDate.of(2026, 10, 5), "Conference travel and stay, Singapore",
                "conference_invoice.pdf", "CC-MKT-205"));
        show(service.submit("EXP-006", person("E103"), "Meals", 2400.00,
                LocalDate.of(2026, 10, 8), "Lunch with auditors",
                "restaurant_bill.jpg", "CC-FIN-101"));

        System.out.println();
        System.out.println("Total expenses submitted: " + service.getExpenseCount());
    }

    // ==================================================================
    // STEP 3 - approvals (successful, denied by grade, denied by limit)
    // ==================================================================
    private static void processApprovals() {
        printHeading("APPROVE EXPENSES (GRADE AND LIMIT RULES)");

        attemptApproval("EXP-001", "E101");   // plain employee: no authority
        attemptApproval("EXP-001", "M203");   // junior manager: Travel not allowed
        attemptApproval("EXP-001", "M201");   // senior manager: OK
        attemptApproval("EXP-004", "M203");   // junior manager: Meals OK
        attemptApproval("EXP-002", "M201");   // senior: OK
        attemptApproval("EXP-003", "M201");   // senior: Rs. 18,500 <= limit and has receipt
        attemptApproval("EXP-005", "M201");   // Rs. 65,000 > Rs. 20,000 limit: escalate
        attemptApproval("EXP-005", "M202");   // director: Rs. 65,000 <= Rs. 1,00,000
        attemptApproval("EXP-007", "M201");   // above Rs. 5,000 with no receipt
        attemptApproval("EXP-008", "M201");   // own expense
        attemptApproval("EXP-008", "M202");   // another manager: OK
    }

    // ==================================================================
    // STEP 4 - rejection
    // ==================================================================
    private static void processRejection() {
        printHeading("REJECT AN EXPENSE");
        Employee manager = person("M201");
        System.out.println("> " + manager.getName() + " rejects EXP-006");
        service.reject("EXP-006", manager,
                "Duplicate claim - this lunch was already billed to the client");
        printMessage(manager.getLastMessage());
        System.out.println("> " + person("E102").getName() + " (Employee) tries to reject EXP-007");
        service.reject("EXP-007", person("E102"), "Not needed");
        printMessage(person("E102").getLastMessage());
    }

    // ==================================================================
    // STEP 5 - reimbursement
    // ==================================================================
    private static void processReimbursements() {
        printHeading("REIMBURSE APPROVED EXPENSES");
        LocalDate paymentDate = LocalDate.of(2026, 10, 15);
        reimburseAndShow("EXP-001", paymentDate);
        reimburseAndShow("EXP-005", paymentDate);

        printSubHeading("Invalid reimbursement attempts (must be blocked)");
        attempt("Reimburse REJECTED EXP-006", () -> service.reimburse("EXP-006", paymentDate));
        attempt("Reimburse PENDING  EXP-007", () -> service.reimburse("EXP-007", paymentDate));
        attempt("Reimburse again    EXP-001", () -> service.reimburse("EXP-001", paymentDate));
    }

    // ==================================================================
    // STEP 6 - reports
    // ==================================================================
    private static void printReports() {
        printHeading("EXPENSES GROUPED BY STATUS");
        for (Expense.Status status : Expense.Status.values()) {
            service.printByStatus(status);
        }

        printHeading("PER-EMPLOYEE EXPENSE TOTALS");
        service.printEmployeeTotals(people);

        printHeading("MONTHLY EXPENSE SUMMARY");
        service.printMonthlySummary();
    }

    // ==================================================================
    // STEP 7 - OOP concepts demonstrated with real behaviour
    // ==================================================================
    private static void demonstrateOopConcepts() {
        printHeading("OOP CONCEPT DEMONSTRATIONS");

        // ---- Inheritance ----
        printSubHeading("1. Inheritance: Manager extends Employee");
        Manager meera = (Manager) person("M201");
        System.out.println("  Meera is a Manager  : " + (meera instanceof Manager));
        System.out.println("  Meera is an Employee: " + (meera instanceof Employee));
        System.out.println("  Inherited getName()      : " + meera.getName());
        System.out.println("  Inherited getEmail()     : " + meera.getEmail());
        System.out.println("  Inherited calculateTotal(): "
                + Expense.formatMoney(meera.calculateTotal()));
        meera.reportExpenses();

        // ---- Overriding + Polymorphism ----
        printSubHeading("2 & 3. Overriding + Polymorphism (Employee references)");
        Expense probe = service.findById("EXP-007");
        System.out.println("  Question: can each person approve " + probe + "?");
        for (Employee p : people) {          // every element is typed as Employee
            String blocker = p.getApprovalBlocker(probe);   // dynamic method dispatch
            printWrapped(String.format("  %-8s %-14s -> ", p.getRole(), p.getName()),
                    " ".repeat(34), blocker == null ? "YES, can approve" : "NO. " + blocker);
        }

        // ---- Overloading ----
        printSubHeading("4. Overloading: same method name, different parameters");
        Employee aarav = person("E101");
        System.out.println("  service.calculateTotal(aarav)            = "
                + Expense.formatMoney(service.calculateTotal(aarav)));
        System.out.println("  service.calculateTotal(aarav, PENDING)   = "
                + Expense.formatMoney(service.calculateTotal(aarav, Expense.Status.PENDING)));
        System.out.println("  service.calculateTotal(REIMBURSED)       = "
                + Expense.formatMoney(service.calculateTotal(Expense.Status.REIMBURSED)));
        System.out.println("  (submit() overloads were shown in the submission step above)");

        // ---- Encapsulation ----
        printSubHeading("5. Encapsulation: status and amount cannot be edited directly");
        // The next lines would NOT compile, because the fields are private
        // and no public setStatus()/setAmount() exists:
        //     expense.status = Expense.Status.APPROVED;
        //     expense.amount = 999999;
        attempt("updateAmount() on APPROVED EXP-002", () -> service.findById("EXP-002").updateAmount(9999));
        attempt("updateAmount(-50) on PENDING EXP-007", () -> service.findById("EXP-007").updateAmount(-50));
        Expense pending = service.findById("EXP-007");
        pending.updateAmount(7800.00);   // a valid edit of a PENDING claim is allowed
        System.out.println("  [OK]      updateAmount(7800) on PENDING EXP-007 accepted ("
                + Expense.formatMoney(pending.getAmount()) + ")");
    }

    // ==================================================================
    // STEP 8 - invalid input must be rejected
    // ==================================================================
    private static void runValidationTests() {
        printHeading("VALIDATION TESTS (INVALID INPUT MUST BE BLOCKED)");
        LocalDate day = LocalDate.of(2026, 10, 20);
        Employee aarav = person("E101");
        attempt("Duplicate ID EXP-003",
                () -> service.submit("EXP-003", aarav, "Meals", 500.00, day, "Duplicate"));
        attempt("Invalid ID format 'X1'",
                () -> service.submit("X1", aarav, "Meals", 500.00, day, "Bad id"));
        attempt("Negative amount -500",
                () -> service.submit("EXP-009", aarav, "Meals", -500.00, day, "Negative"));
        attempt("Zero amount 0",
                () -> service.submit("EXP-009", aarav, "Meals", 0, day, "Zero"));
        attempt("Invalid type 'Hotel'",
                () -> service.submit("EXP-009", aarav, "Hotel", 500.00, day, "Wrong type"));
        attempt("Unknown ID EXP-999",
                () -> service.approve("EXP-999", person("M202")));
        attempt("Blank rejection reason",
                () -> service.reject("EXP-007", person("M202"), "  "));
        attempt("Invalid email for new employee",
                () -> new Employee("E999", "Test User", "IT", "not-an-email"));
        attempt("Manager with zero approval limit",
                () -> new Manager("M999", "Test Mgr", "IT", "t@x.in", 0, Manager.Grade.JUNIOR));
        System.out.println();
        System.out.println("Expenses stored after all invalid attempts: " + service.getExpenseCount()
                + " (unchanged)");
    }

    // ==================================================================
    // STEP 9 - automatic self-checks (real comparisons, real results)
    // ==================================================================
    private static void runSelfChecks() {
        printHeading("AUTOMATIC SELF-CHECKS");
        Expense.Status pend = Expense.Status.PENDING;
        Expense.Status appr = Expense.Status.APPROVED;
        Expense.Status rej = Expense.Status.REJECTED;
        Expense.Status paid = Expense.Status.REIMBURSED;

        check(service.getExpenseCount() == 8, "8 expenses stored; none added by invalid attempts");
        check(Employee.getEmployeeCount() == 6 && Manager.getManagerCount() == 3,
                "6 people created, 3 of them managers");
        check(Expense.getTotalCreated() == 8 && ExpenseService.getTotalSubmissions() == 8,
                "static counters match the 8 valid submissions");
        check(near(service.calculateTotal(person("E101")), 13150.00), "Aarav total = 13,150.00");
        check(near(service.calculateTotal(person("E102")), 66200.00), "Priya total = 66,200.00");
        check(near(service.calculateTotal(person("E103")), 20900.00), "Karthik total = 20,900.00");
        check(near(service.calculateTotal(person("M201")), 1800.00), "Meera total = 1,800.00");
        boolean sameTotals = true;
        for (Employee p : people) {
            sameTotals &= near(p.calculateTotal(), service.calculateTotal(p));
        }
        check(sameTotals, "Employee and ExpenseService totals agree for all 6 people");
        check(near(service.calculateMonthlyTotal(java.time.YearMonth.of(2026, 9)), 24200.00),
                "September 2026 total = 24,200.00");
        check(near(service.calculateMonthlyTotal(java.time.YearMonth.of(2026, 10)), 77850.00),
                "October 2026 total = 77,850.00");
        check(service.listByStatus(pend).size() == 1 && service.listByStatus(appr).size() == 4
                && service.listByStatus(rej).size() == 1 && service.listByStatus(paid).size() == 2,
                "status counts: PENDING 1, APPROVED 4, REJECTED 1, REIMBURSED 2");
        check(near(service.calculateTotal(pend) + service.calculateTotal(appr)
                + service.calculateTotal(rej) + service.calculateTotal(paid), 102050.00),
                "status totals add up to 102,050.00");
        check(service.findById("EXP-006").getStatus() == rej, "rejected EXP-006 stayed REJECTED");
        check(service.findById("EXP-007").getStatus() == pend, "EXP-007 (no receipt) stayed PENDING");
        check(service.findById("EXP-005").getStatus() == paid
                && service.findById("EXP-005").getDecidedBy().equals("Rajesh Kumar"),
                "EXP-005 (over Meera's limit): Director approved, then reimbursed");
        Manager m201 = (Manager) person("M201");
        Manager m202 = (Manager) person("M202");
        Manager m203 = (Manager) person("M203");
        check(m201.getApprovedCount() == 3 && m202.getApprovedCount() == 2
                && m203.getApprovedCount() == 1, "approvals per manager: Meera 3, Rajesh 2, Divya 1");
        check(m201.getRejectedCount() == 1, "Meera rejected 1 expense");
        check(service.getAuditLog().size() > 0, "audit log has " + service.getAuditLog().size()
                + " entries");

        System.out.println();
        System.out.println("Self-checks passed: " + checksPassed + " / " + checksRun);
    }

    // ==================================================================
    // Helper methods
    // ==================================================================

    /** Finds a person (Employee or Manager) by ID. Returns an Employee reference. */
    private static Employee person(String empId) {
        for (Employee p : people) {
            if (p.getEmpId().equals(empId)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown person: " + empId);
    }

    private static void attemptApproval(String expenseId, String approverId) {
        Employee approver = person(approverId);
        System.out.println("> " + approver.getName() + " (" + approver.getRole()
                + ") tries to approve " + expenseId);
        service.approve(expenseId, approver);
        printMessage(approver.getLastMessage());
    }

    private static void reimburseAndShow(String expenseId, LocalDate date) {
        Expense e = service.reimburse(expenseId, date);
        System.out.println("[REIMBURSED] " + e.getExpenseId() + " - "
                + Expense.formatMoney(e.getAmount()) + " paid to "
                + e.getRequester().getName() + " on " + e.getReimbursedOn());
    }

    private static void show(Expense e) {
        System.out.println("[SUBMITTED] " + e.getExpenseId() + " | " + e.getType().getLabel()
                + " | " + Expense.formatMoney(e.getAmount()) + " | " + e.getStatus()
                + " | " + e.getRequester().getName());
        System.out.println("            " + e.describeExtras());
    }

    /**
     * Runs an action that MUST fail. Prints the error message when it does;
     * records a failed self-check if the action unexpectedly succeeds.
     */
    private static void attempt(String label, Runnable action) {
        try {
            action.run();
            System.out.println("  [UNEXPECTED] " + label + " was NOT blocked!");
            checksRun++;   // counted as a failed check
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println("  [BLOCKED] " + label);
            printWrapped("            -> ", "               ", ex.getMessage());
        }
    }

    /** Prints a result message such as "[DENIED]   ..." wrapped to the console width. */
    private static void printMessage(String message) {
        printWrapped("  ", " ".repeat(13), message);
    }

    /** Word-wraps text so no console line is longer than WIDTH characters. */
    private static void printWrapped(String firstPrefix, String continuationPrefix, String text) {
        StringBuilder line = new StringBuilder(firstPrefix);
        boolean lineIsEmpty = true;
        for (String word : text.split(" ")) {
            if (!lineIsEmpty && line.length() + 1 + word.length() > WIDTH) {
                System.out.println(line);
                line = new StringBuilder(continuationPrefix);
                lineIsEmpty = true;
            }
            if (!lineIsEmpty) {
                line.append(' ');
            }
            line.append(word);
            lineIsEmpty = false;
        }
        System.out.println(line);
    }

    private static void check(boolean condition, String label) {
        checksRun++;
        if (condition) {
            checksPassed++;
        }
        System.out.println("  [" + (condition ? "PASS" : "FAIL") + "] " + label);
    }

    private static boolean near(double a, double b) {
        return Math.abs(a - b) < 0.005;
    }

    private static void printHeading(String title) {
        stepNo++;
        System.out.println();
        System.out.println(LINE);
        System.out.println("STEP " + stepNo + ": " + title);
        System.out.println(LINE);
    }

    private static void printSubHeading(String title) {
        System.out.println();
        System.out.println("--- " + title);
    }

    private static void printBanner() {
        System.out.println(LINE);
        System.out.println("   EXPENSE MANAGEMENT SYSTEM - 23CSR303 OOP WITH JAVA - ASSIGNMENT 2");
        System.out.println("   Submit -> Approve / Reject -> Reimburse");
        System.out.println(LINE);
    }

    private static void printFooter() {
        System.out.println();
        System.out.println(SUB_LINE);
        System.out.println("Program finished. All steps ran with built-in sample data.");
        System.out.println(SUB_LINE);
    }
}
```

## 9. Sample output

> **Important:** the text below is the output of compiling and running the program with **OpenJDK 21 from a command line**, not from Eclipse.
> It is the *expected* output; your Eclipse Console output should be identical. Genuine Eclipse Console screenshots are listed in Section 9.1.

```text
==============================================================================
   EXPENSE MANAGEMENT SYSTEM - 23CSR303 OOP WITH JAVA - ASSIGNMENT 2
   Submit -> Approve / Reject -> Reimburse
==============================================================================

==============================================================================
STEP 1: CREATE EMPLOYEES AND MANAGERS
==============================================================================
Company : Chennai Tech Solutions Pvt Ltd
  Role      ID    Name           Dept         Email
  Employee  E101  Aarav Sharma   Engineering  aarav.sharma@chennaitech.in
  Employee  E102  Priya Nair     Marketing    priya.nair@chennaitech.in
  Employee  E103  Karthik Raman  Finance      karthik.raman@chennaitech.in
  Manager   M201  Meera Iyer     Operations   meera.iyer@chennaitech.in
            -> Grade: Senior (L2) | Limit: Rs. 20,000.00
            -> Rule : Any type up to Rs. 20,000.00; receipt above Rs. 5,000.00
  Manager   M202  Rajesh Kumar   Finance      rajesh.kumar@chennaitech.in
            -> Grade: Director (L3) | Limit: Rs. 100,000.00
            -> Rule : Any type up to Rs. 100,000.00
  Manager   M203  Divya Menon    Sales        divya.menon@chennaitech.in
            -> Grade: Junior (L1) | Limit: Rs. 5,000.00
            -> Rule : Meals only, up to Rs. 5,000.00
Created 6 people (3 employees, 3 managers).

==============================================================================
STEP 2: SUBMIT EXPENSES (OVERLOADED submit() METHODS)
==============================================================================

--- Overload 1: submit(id, employee, type, amount, date, description)
[SUBMITTED] EXP-001 | Travel | Rs. 4,500.00 | PENDING | Aarav Sharma
            Attachment: none | Cost Center: none
[SUBMITTED] EXP-004 | Meals | Rs. 850.00 | PENDING | Aarav Sharma
            Attachment: none | Cost Center: none
[SUBMITTED] EXP-007 | Travel | Rs. 7,800.00 | PENDING | Aarav Sharma
            Attachment: none | Cost Center: none
[SUBMITTED] EXP-008 | Meals | Rs. 1,800.00 | PENDING | Meera Iyer
            Attachment: none | Cost Center: none

--- Overload 2: submit(..., attachmentName)
[SUBMITTED] EXP-002 | Meals | Rs. 1,200.00 | PENDING | Priya Nair
            Attachment: lunch_bill_0915.pdf | Cost Center: none

--- Overload 3: submit(..., attachmentName, costCenter)
[SUBMITTED] EXP-003 | Travel | Rs. 18,500.00 | PENDING | Karthik Raman
            Attachment: flight_ticket_MAA-DEL.pdf | Cost Center: CC-FIN-101
[SUBMITTED] EXP-005 | Travel | Rs. 65,000.00 | PENDING | Priya Nair
            Attachment: conference_invoice.pdf | Cost Center: CC-MKT-205
[SUBMITTED] EXP-006 | Meals | Rs. 2,400.00 | PENDING | Karthik Raman
            Attachment: restaurant_bill.jpg | Cost Center: CC-FIN-101

Total expenses submitted: 8

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

==============================================================================
STEP 4: REJECT AN EXPENSE
==============================================================================
> Meera Iyer rejects EXP-006
  [REJECTED] EXP-006 rejected by Meera Iyer. Reason: Duplicate claim - this
             lunch was already billed to the client
> Priya Nair (Employee) tries to reject EXP-007
  [DENIED]   Priya Nair (Employee) has no authority to reject expenses.

==============================================================================
STEP 5: REIMBURSE APPROVED EXPENSES
==============================================================================
[REIMBURSED] EXP-001 - Rs. 4,500.00 paid to Aarav Sharma on 2026-10-15
[REIMBURSED] EXP-005 - Rs. 65,000.00 paid to Priya Nair on 2026-10-15

--- Invalid reimbursement attempts (must be blocked)
  [BLOCKED] Reimburse REJECTED EXP-006
            -> Expense EXP-006 was REJECTED, so it cannot be reimbursed.
  [BLOCKED] Reimburse PENDING  EXP-007
            -> Expense EXP-007 is still PENDING; it must be APPROVED before
               reimbursement.
  [BLOCKED] Reimburse again    EXP-001
            -> Expense EXP-001 was already reimbursed on 2026-10-15.

==============================================================================
STEP 6: EXPENSES GROUPED BY STATUS
==============================================================================

Status: PENDING  (1 expense(s), total Rs. 7,800.00)
  ID       Type     Amount(Rs.) Status      Requester      Date
  EXP-007  Travel      7,800.00 PENDING     Aarav Sharma   2026-10-10

Status: APPROVED  (4 expense(s), total Rs. 22,350.00)
  ID       Type     Amount(Rs.) Status      Requester      Date
  EXP-004  Meals         850.00 APPROVED    Aarav Sharma   2026-10-03
  EXP-008  Meals       1,800.00 APPROVED    Meera Iyer     2026-10-12
  EXP-002  Meals       1,200.00 APPROVED    Priya Nair     2026-09-15
  EXP-003  Travel     18,500.00 APPROVED    Karthik Raman  2026-09-20

Status: REJECTED  (1 expense(s), total Rs. 2,400.00)
  ID       Type     Amount(Rs.) Status      Requester      Date
  EXP-006  Meals       2,400.00 REJECTED    Karthik Raman  2026-10-08

Status: REIMBURSED  (2 expense(s), total Rs. 69,500.00)
  ID       Type     Amount(Rs.) Status      Requester      Date
  EXP-001  Travel      4,500.00 REIMBURSED  Aarav Sharma   2026-09-12
  EXP-005  Travel     65,000.00 REIMBURSED  Priya Nair     2026-10-05

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

==============================================================================
STEP 9: OOP CONCEPT DEMONSTRATIONS
==============================================================================

--- 1. Inheritance: Manager extends Employee
  Meera is a Manager  : true
  Meera is an Employee: true
  Inherited getName()      : Meera Iyer
  Inherited getEmail()     : meera.iyer@chennaitech.in
  Inherited calculateTotal(): Rs. 1,800.00
  Expenses of Meera Iyer (M201): 1 claim(s), total Rs. 1,800.00
  ID       Type     Amount(Rs.) Status      Requester      Date
  EXP-008  Meals       1,800.00 APPROVED    Meera Iyer     2026-10-12

--- 2 & 3. Overriding + Polymorphism (Employee references)
  Question: can each person approve EXP-007 [Travel, Rs. 7,800.00, PENDING]?
  Employee Aarav Sharma   -> NO. Aarav Sharma (Employee) has no authority to
                                  approve expenses.
  Employee Priya Nair     -> NO. Priya Nair (Employee) has no authority to
                                  approve expenses.
  Employee Karthik Raman  -> NO. Karthik Raman (Employee) has no authority to
                                  approve expenses.
  Manager  Meera Iyer     -> NO. Grade rule: Senior (L2) managers need an
                                  attachment (receipt) for claims above Rs.
                                  5,000.00. EXP-007 has none. Ask the employee
                                  to attach it, or ESCALATE to a Director.
  Manager  Rajesh Kumar   -> YES, can approve
  Manager  Divya Menon    -> NO. Grade rule: Junior (L1) managers can approve
                                  Meals only. EXP-007 (Travel) needs
                                  ESCALATION to a Senior or Director manager.

--- 4. Overloading: same method name, different parameters
  service.calculateTotal(aarav)            = Rs. 13,150.00
  service.calculateTotal(aarav, PENDING)   = Rs. 7,800.00
  service.calculateTotal(REIMBURSED)       = Rs. 69,500.00
  (submit() overloads were shown in the submission step above)

--- 5. Encapsulation: status and amount cannot be edited directly
  [BLOCKED] updateAmount() on APPROVED EXP-002
            -> Amount of expense EXP-002 cannot be edited because it is
               APPROVED.
  [BLOCKED] updateAmount(-50) on PENDING EXP-007
            -> Amount must be a positive number, but was -50.0.
  [OK]      updateAmount(7800) on PENDING EXP-007 accepted (Rs. 7,800.00)

==============================================================================
STEP 10: VALIDATION TESTS (INVALID INPUT MUST BE BLOCKED)
==============================================================================
  [BLOCKED] Duplicate ID EXP-003
            -> Duplicate expense ID: EXP-003 is already in use.
  [BLOCKED] Invalid ID format 'X1'
            -> Invalid expense ID 'X1'. Expected format: EXP-001.
  [BLOCKED] Negative amount -500
            -> Amount must be a positive number, but was -500.0.
  [BLOCKED] Zero amount 0
            -> Amount must be a positive number, but was 0.0.
  [BLOCKED] Invalid type 'Hotel'
            -> Invalid expense type 'Hotel'. Allowed types: Travel, Meals.
  [BLOCKED] Unknown ID EXP-999
            -> No expense found with ID EXP-999.
  [BLOCKED] Blank rejection reason
            -> A rejection reason is required.
  [BLOCKED] Invalid email for new employee
            -> Invalid email address: not-an-email
  [BLOCKED] Manager with zero approval limit
            -> Approval limit must be a positive number.

Expenses stored after all invalid attempts: 8 (unchanged)

==============================================================================
STEP 11: AUTOMATIC SELF-CHECKS
==============================================================================
  [PASS] 8 expenses stored; none added by invalid attempts
  [PASS] 6 people created, 3 of them managers
  [PASS] static counters match the 8 valid submissions
  [PASS] Aarav total = 13,150.00
  [PASS] Priya total = 66,200.00
  [PASS] Karthik total = 20,900.00
  [PASS] Meera total = 1,800.00
  [PASS] Employee and ExpenseService totals agree for all 6 people
  [PASS] September 2026 total = 24,200.00
  [PASS] October 2026 total = 77,850.00
  [PASS] status counts: PENDING 1, APPROVED 4, REJECTED 1, REIMBURSED 2
  [PASS] status totals add up to 102,050.00
  [PASS] rejected EXP-006 stayed REJECTED
  [PASS] EXP-007 (no receipt) stayed PENDING
  [PASS] EXP-005 (over Meera's limit): Director approved, then reimbursed
  [PASS] approvals per manager: Meera 3, Rajesh 2, Divya 1
  [PASS] Meera rejected 1 expense
  [PASS] audit log has 23 entries

Self-checks passed: 18 / 18

------------------------------------------------------------------------------
Program finished. All steps ran with built-in sample data.
------------------------------------------------------------------------------
```

### 9.1 Eclipse Console screenshots

*(To be inserted by the author after running the project in Eclipse. Do not insert any image that was not captured from the Eclipse Console.)*

- Screenshot 1 - Program start, employees and managers: `screenshots/01_run_start_employees_managers.png`  *[insert after capture]*
- Screenshot 2 - Overloaded `submit()` methods: `screenshots/02_submit_overloaded_methods.png`  *[insert after capture]*
- Screenshot 3 - Approvals, grade rules and approval-limit escalation: `screenshots/03_approvals_limit_grade.png`  *[insert after capture]*
- Screenshot 4 - Rejection and reimbursement: `screenshots/04_rejection_reimbursement.png`  *[insert after capture]*
- Screenshot 5 - Expenses grouped by status: `screenshots/05_grouped_by_status.png`  *[insert after capture]*
- Screenshot 6 - Per-employee totals and monthly summary: `screenshots/06_totals_and_monthly_summary.png`  *[insert after capture]*
- Screenshot 7 - OOP demonstrations: `screenshots/07_oop_demonstrations.png`  *[insert after capture]*
- Screenshot 8 - Validation tests and self-checks: `screenshots/08_validation_and_selfchecks.png`  *[insert after capture]*

## 10. Test cases and results

"Command-line result" was observed when the project was compiled and run with JDK 21 outside Eclipse.
The last column is for the author to complete after running in Eclipse.

| # | Test case | Expected result | Command-line result (JDK 21) | Eclipse result (author to fill) |
|---|---|---|---|---|
| 1 | Create 3 employees + 3 managers | 6 people, 3 managers | Pass | |
| 2 | Submit using all three `submit()` overloads | 8 `PENDING` expenses | Pass | |
| 3 | Employee tries to approve | Denied, no authority | Pass | |
| 4 | Junior manager approves Travel | Denied by grade rule | Pass | |
| 5 | Senior manager approves Rs. 4,500 Travel | Approved | Pass | |
| 6 | Senior manager (limit Rs. 20,000) approves Rs. 65,000 | Denied, escalation needed, stays `PENDING` | Pass | |
| 7 | Director approves Rs. 65,000 | Approved | Pass | |
| 8 | Senior manager approves Rs. 7,800 with no receipt | Denied, stays `PENDING` | Pass | |
| 9 | Manager approves own expense | Denied, another manager needed | Pass | |
| 10 | Manager rejects with reason | `REJECTED` | Pass | |
| 11 | Reimburse approved expense | `REIMBURSED` | Pass | |
| 12 | Reimburse rejected expense | Blocked | Pass | |
| 13 | Reimburse pending expense | Blocked | Pass | |
| 14 | Reimburse twice | Blocked | Pass | |
| 15 | Duplicate ID; invalid ID format | Blocked | Pass | |
| 16 | Negative amount; zero amount | Blocked | Pass | |
| 17 | Invalid type (`Hotel`) | Blocked | Pass | |
| 18 | Unknown expense ID | Blocked | Pass | |
| 19 | Blank rejection reason | Blocked | Pass | |
| 20 | Edit amount of an `APPROVED` expense | Blocked | Pass | |
| 21 | Per-employee totals | Aarav 13,150.00; Priya 66,200.00; Karthik 20,900.00; Meera 1,800.00; total 102,050.00 | Pass | |
| 22 | Monthly summary | 2026-09: 24,200.00 (3); 2026-10: 77,850.00 (5) | Pass | |
| 23 | Automatic self-checks | 18 / 18 passed | Pass | |

## 11. Result

The application was compiled with `javac` (no warnings with `-Xlint:all`) and run with OpenJDK 21 from a command line. All 18 built-in self-checks passed and the output was identical on two consecutive runs.
All required features were implemented: four main classes plus the `ExpenseAppMain` test class, constructors, more than four variables and more than five methods in every class, overloaded `submit()`, grade-based overriding of approval, polymorphic handling through `Employee` references, and encapsulated status/amount.

The run in **Eclipse** and the corresponding Console screenshots are to be added by the author (Section 9.1).

## 12. Conclusion

The Expense Management System shows how object-oriented design models a real approval workflow. Inheritance lets `Manager` reuse `Employee`;
overriding gives managers grade-specific approval rules; overloading offers three convenient ways to submit expenses;
polymorphism lets the same code handle employees and managers; and encapsulation guarantees that an expense's status and amount can change only through validated, legal transitions.

## 13. GitHub repository

**Repository link:** https://github.com/cibi-k/ExpenseManagementAssignment2
