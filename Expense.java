package com.expensemanagement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;

public class Expense {
    public enum Type {
        TRAVEL("Travel"), MEALS("Meals");

        private final String label;

        Type(String label) { this.label = label; }

        public String getLabel() { return label; }

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

    public enum Status { PENDING, APPROVED, REJECTED, REIMBURSED }

    private static int totalCreated = 0;

    private final String expenseId;
    private final Type type;
    private double amount;
    private Status status;
    private final Employee requester;
    private final LocalDate expenseDate;
    private final String description;
    private final String attachmentName;
    private final String costCenter;
    private String decidedBy;
    private String remarks;
    private LocalDate reimbursedOn;

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

    public YearMonth getMonth() { return YearMonth.from(expenseDate); }

    void approve(String approverName) {
        requireStatus(Status.PENDING, "approve");
        status = Status.APPROVED;
        decidedBy = approverName;
        remarks = "Approved by " + approverName;
    }

    void reject(String approverName, String reason) {
        requireStatus(Status.PENDING, "reject");
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("A rejection reason is required.");
        }
        status = Status.REJECTED;
        decidedBy = approverName;
        remarks = "Rejected by " + approverName + ": " + reason.trim();
    }

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

    public void updateAmount(double newAmount) {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Amount of expense " + expenseId
                    + " cannot be edited because it is " + status + ".");
        }
        this.amount = validateAmount(newAmount);
    }

    public static String summaryHeader() {
        return String.format("%-8s %-7s %12s %-11s %-14s %s",
                "ID", "Type", "Amount(Rs.)", "Status", "Requester", "Date");
    }

    public String summaryLine() {
        return String.format("%-8s %-7s %12s %-11s %-14s %s",
                expenseId, type.getLabel(), formatAmount(amount), status,
                requester.getName(), expenseDate);
    }

    public String describeExtras() {
        return "Attachment: " + (attachmentName == null ? "none" : attachmentName)
                + " | Cost Center: " + (costCenter == null ? "none" : costCenter);
    }

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

    public static String formatAmount(double value) {
        return String.format(Locale.US, "%,.2f", value);
    }

    public static String formatMoney(double value) {
        return "Rs. " + formatAmount(value);
    }

    @Override
    public String toString() {
        return expenseId + " [" + type.getLabel() + ", " + formatMoney(amount) + ", " + status + "]";
    }

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
