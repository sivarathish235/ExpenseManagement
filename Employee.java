package com.expensemanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Employee {
    private static int employeeCount = 0;

    private final String empId;
    private String name;
    private String dept;
    private String email;
    private final List<Expense> submittedExpenses = new ArrayList<>();
    private String lastMessage = "";

    public Employee(String empId, String name, String dept, String email) {
        this.empId = requireText(empId, "Employee ID");
        this.name = requireText(name, "Name");
        this.dept = requireText(dept, "Department");
        this.email = requireEmail(email);
        employeeCount++;
    }

    public String getEmpId() { return empId; }
    public String getName()  { return name; }
    public String getDept()  { return dept; }
    public String getEmail() { return email; }

    public void setName(String name)   { this.name = requireText(name, "Name"); }
    public void setDept(String dept)   { this.dept = requireText(dept, "Department"); }
    public void setEmail(String email) { this.email = requireEmail(email); }

    public String getLastMessage() { return lastMessage; }

    protected void setLastMessage(String message) { this.lastMessage = message; }

    public static int getEmployeeCount() { return employeeCount; }

    public String getRole() { return "Employee"; }

    public void displayDetails() {
        System.out.printf("  %-9s %-5s %-14s %-12s %s%n",
                getRole(), empId, name, dept, email);
    }

    public String getApprovalBlocker(Expense expense) {
        return name + " (" + getRole() + ") has no authority to approve expenses.";
    }

    public boolean canApprove(Expense expense) {
        return getApprovalBlocker(expense) == null;
    }

    public boolean approve(Expense expense) {
        lastMessage = "[DENIED]   " + getApprovalBlocker(expense);
        return false;
    }

    public boolean reject(Expense expense, String reason) {
        lastMessage = "[DENIED]   " + name + " (" + getRole()
                + ") has no authority to reject expenses.";
        return false;
    }

    void addExpense(Expense expense) { submittedExpenses.add(expense); }

    public List<Expense> getSubmittedExpenses() {
        return Collections.unmodifiableList(submittedExpenses);
    }

    public double calculateTotal() {
        double total = 0;
        for (Expense e : submittedExpenses) {
            total += e.getAmount();
        }
        return total;
    }

    public double calculateTotal(Expense.Status status) {
        double total = 0;
        for (Expense e : submittedExpenses) {
            if (e.getStatus() == status) {
                total += e.getAmount();
            }
        }
        return total;
    }

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
