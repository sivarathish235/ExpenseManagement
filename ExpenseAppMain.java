package com.expensemanagement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseAppMain {
    private static final int WIDTH = 78;
    private static final String LINE = "=".repeat(WIDTH);
    private static final String SUB_LINE = "-".repeat(WIDTH);
    private static final ExpenseService service =
            new ExpenseService("Chennai Tech Solutions Pvt Ltd");
    private static final List<Employee> people = new ArrayList<>();
    private static int stepNo = 0;
    private static int checksRun = 0;
    private static int checksPassed = 0;

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

    private static void createPeople() {
        printHeading("CREATE EMPLOYEES AND MANAGERS");
        people.add(new Employee("E101", "Aarav Sharma", "Engineering", "aarav.sharma@chennaitech.in"));
        people.add(new Employee("E102", "Priya Nair", "Marketing", "priya.nair@chennaitech.in"));
        people.add(new Employee("E103", "Karthik Raman", "Finance", "karthik.raman@chennaitech.in"));
        people.add(new Manager("M201", "Meera Iyer", "Operations", "meera.iyer@chennaitech.in",
                20000.00, Manager.Grade.SENIOR));
        people.add(new Manager("M202", "Rajesh Kumar", "Finance", "rajesh.kumar@chennaitech.in",
                100000.00, Manager.Grade.DIRECTOR));
        people.add(new Manager("M203", "Divya Menon", "Sales", "divya.menon@chennaitech.in",
                5000.00, Manager.Grade.JUNIOR));

        System.out.println("Company : " + service.getCompanyName());
        System.out.printf("  %-9s %-5s %-14s %-12s %s%n", "Role", "ID", "Name", "Dept", "Email");
        for (Employee p : people) {
            p.displayDetails();
        }
        System.out.println("Created " + Employee.getEmployeeCount() + " people ("
                + (Employee.getEmployeeCount() - Manager.getManagerCount()) + " employees, "
                + Manager.getManagerCount() + " managers).");
    }

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

    private static void processApprovals() {
        printHeading("APPROVE EXPENSES (GRADE AND LIMIT RULES)");

        attemptApproval("EXP-001", "E101");
        attemptApproval("EXP-001", "M203");
        attemptApproval("EXP-001", "M201");
        attemptApproval("EXP-004", "M203");
        attemptApproval("EXP-002", "M201");
        attemptApproval("EXP-003", "M201");
        attemptApproval("EXP-005", "M201");
        attemptApproval("EXP-005", "M202");
        attemptApproval("EXP-007", "M201");
        attemptApproval("EXP-008", "M201");
        attemptApproval("EXP-008", "M202");
    }

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

    private static void demonstrateOopConcepts() {
        printHeading("OOP CONCEPT DEMONSTRATIONS");

        printSubHeading("1. Inheritance: Manager extends Employee");
        Manager meera = (Manager) person("M201");
        System.out.println("  Meera is a Manager  : " + (meera instanceof Manager));
        System.out.println("  Meera is an Employee: " + (meera instanceof Employee));
        System.out.println("  Inherited getName()      : " + meera.getName());
        System.out.println("  Inherited getEmail()     : " + meera.getEmail());
        System.out.println("  Inherited calculateTotal(): "
                + Expense.formatMoney(meera.calculateTotal()));
        meera.reportExpenses();

        printSubHeading("2 & 3. Overriding + Polymorphism (Employee references)");
        Expense probe = service.findById("EXP-007");
        System.out.println("  Question: can each person approve " + probe + "?");
        for (Employee p : people) {
            String blocker = p.getApprovalBlocker(probe);
            printWrapped(String.format("  %-8s %-14s -> ", p.getRole(), p.getName()),
                    " ".repeat(34), blocker == null ? "YES, can approve" : "NO. " + blocker);
        }

        printSubHeading("4. Overloading: same method name, different parameters");
        Employee aarav = person("E101");
        System.out.println("  service.calculateTotal(aarav)            = "
                + Expense.formatMoney(service.calculateTotal(aarav)));
        System.out.println("  service.calculateTotal(aarav, PENDING)   = "
                + Expense.formatMoney(service.calculateTotal(aarav, Expense.Status.PENDING)));
        System.out.println("  service.calculateTotal(REIMBURSED)       = "
                + Expense.formatMoney(service.calculateTotal(Expense.Status.REIMBURSED)));
        System.out.println("  (submit() overloads were shown in the submission step above)");

        printSubHeading("5. Encapsulation: status and amount cannot be edited directly");
        attempt("updateAmount() on APPROVED EXP-002", () -> service.findById("EXP-002").updateAmount(9999));
        attempt("updateAmount(-50) on PENDING EXP-007", () -> service.findById("EXP-007").updateAmount(-50));
        Expense pending = service.findById("EXP-007");
        pending.updateAmount(7800.00);
        System.out.println("  [OK]      updateAmount(7800) on PENDING EXP-007 accepted ("
                + Expense.formatMoney(pending.getAmount()) + ")");
    }

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

    private static void attempt(String label, Runnable action) {
        try {
            action.run();
            System.out.println("  [UNEXPECTED] " + label + " was NOT blocked!");
            checksRun++;
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println("  [BLOCKED] " + label);
            printWrapped("            -> ", "               ", ex.getMessage());
        }
    }

    private static void printMessage(String message) {
        printWrapped("  ", " ".repeat(13), message);
    }

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
