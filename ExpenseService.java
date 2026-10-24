package com.expensemanagement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class ExpenseService {
    private static int totalSubmissions = 0;
    private static final Pattern ID_PATTERN = Pattern.compile("EXP-\\d{3,}");

    private final String companyName;
    private final List<Expense> expenses = new ArrayList<>();
    private final List<String> auditLog = new ArrayList<>();

    public ExpenseService(String companyName) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name must not be blank.");
        }
        this.companyName = companyName.trim();
    }

    public String getCompanyName()   { return companyName; }
    public int getExpenseCount()     { return expenses.size(); }
    public static int getTotalSubmissions() { return totalSubmissions; }

    public List<String> getAuditLog() { return Collections.unmodifiableList(auditLog); }

    public Expense submit(String expenseId, Employee requester, String type,
                          double amount, LocalDate date, String description) {
        return submit(expenseId, requester, type, amount, date, description, null, null);
    }

    public Expense submit(String expenseId, Employee requester, String type,
                          double amount, LocalDate date, String description,
                          String attachmentName) {
        return submit(expenseId, requester, type, amount, date, description,
                attachmentName, null);
    }

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
                description, attachmentName, costCenter);
        expenses.add(expense);
        requester.addExpense(expense);
        totalSubmissions++;
        audit("SUBMITTED  " + expense.getExpenseId() + " by " + requester.getName());
        return expense;
    }

    public boolean approve(String expenseId, Employee approver) {
        requireApprover(approver);
        Expense expense = findById(expenseId);
        boolean approved = approver.approve(expense);
        audit((approved ? "APPROVED   " : "NOT APPROVED ") + expense.getExpenseId()
                + " by " + approver.getName());
        return approved;
    }

    public boolean reject(String expenseId, Employee approver, String reason) {
        requireApprover(approver);
        Expense expense = findById(expenseId);
        boolean rejected = approver.reject(expense, reason);
        audit((rejected ? "REJECTED   " : "NOT REJECTED ") + expense.getExpenseId()
                + " by " + approver.getName());
        return rejected;
    }

    public Expense reimburse(String expenseId, LocalDate paymentDate) {
        Expense expense = findById(expenseId);
        expense.reimburse(paymentDate);
        audit("REIMBURSED " + expense.getExpenseId() + " on " + paymentDate);
        return expense;
    }

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

    public List<Expense> listByStatus(Expense.Status status) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getStatus() == status) {
                result.add(e);
            }
        }
        return result;
    }

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

    public double calculateTotal(Employee employee) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getRequester().getEmpId().equals(employee.getEmpId())) {
                total += e.getAmount();
            }
        }
        return total;
    }

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

    public double calculateTotal(Expense.Status status) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getStatus() == status) {
                total += e.getAmount();
            }
        }
        return total;
    }

    public double calculateMonthlyTotal(YearMonth month) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getMonth().equals(month)) {
                total += e.getAmount();
            }
        }
        return total;
    }

    public double calculateMonthlyTotal(YearMonth month, Expense.Type type) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getMonth().equals(month) && e.getType() == type) {
                total += e.getAmount();
            }
        }
        return total;
    }

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

    private void requireApprover(Employee approver) {
        if (approver == null) {
            throw new IllegalArgumentException("Approver must not be null.");
        }
    }

    private void audit(String entry) {
        auditLog.add(entry);
    }
}
