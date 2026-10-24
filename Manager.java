package com.expensemanagement;

public class Manager extends Employee {
    public enum Grade {
        JUNIOR("Junior (L1)"), SENIOR("Senior (L2)"), DIRECTOR("Director (L3)");

        private final String label;

        Grade(String label) { this.label = label; }

        public String getLabel() { return label; }
    }

    public static final double RECEIPT_THRESHOLD = 5000.0;
    private static int managerCount = 0;

    private double approvalLimit;
    private final Grade grade;
    private int approvedCount = 0;
    private int rejectedCount = 0;
    private double approvedTotal = 0;

    public Manager(String empId, String name, String dept, String email,
                   double approvalLimit, Grade grade) {
        super(checkManagerArguments(empId, approvalLimit, grade), name, dept, email);
        this.approvalLimit = approvalLimit;
        this.grade = grade;
        managerCount++;
    }

    public double getApprovalLimit() { return approvalLimit; }

    public void setApprovalLimit(double approvalLimit) {
        this.approvalLimit = validateLimit(approvalLimit);
    }

    public Grade getGrade()          { return grade; }
    public int getApprovedCount()    { return approvedCount; }
    public int getRejectedCount()    { return rejectedCount; }
    public double getApprovedTotal() { return approvedTotal; }
    public static int getManagerCount() { return managerCount; }

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

    @Override
    public String getRole() { return "Manager"; }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("            -> Grade: " + grade.getLabel()
                + " | Limit: " + Expense.formatMoney(approvalLimit));
        System.out.println("            -> Rule : " + getApprovalPolicy());
    }

    @Override
    public String getApprovalBlocker(Expense expense) {
        if (expense == null) {
            return "No expense was supplied.";
        }
        String id = expense.getExpenseId();

        if (expense.getStatus() != Expense.Status.PENDING) {
            return "Expense " + id + " is already " + expense.getStatus()
                    + "; only PENDING expenses can be approved.";
        }
        if (expense.getRequester().getEmpId().equals(getEmpId())) {
            return getName() + " cannot approve their own expense " + id
                    + ". Needs ANOTHER AUTHORIZED MANAGER.";
        }
        if (grade == Grade.JUNIOR && expense.getType() != Expense.Type.MEALS) {
            return "Grade rule: " + grade.getLabel() + " managers can approve Meals only. "
                    + id + " (" + expense.getType().getLabel()
                    + ") needs ESCALATION to a Senior or Director manager.";
        }
        if (expense.getAmount() > approvalLimit) {
            return "Amount " + Expense.formatMoney(expense.getAmount()) + " of " + id
                    + " exceeds " + getName() + "'s approval limit of "
                    + Expense.formatMoney(approvalLimit)
                    + ". Needs ESCALATION to a manager with a higher limit.";
        }
        if (grade == Grade.SENIOR && expense.getAmount() > RECEIPT_THRESHOLD
                && !expense.hasAttachment()) {
            return "Grade rule: " + grade.getLabel() + " managers need an attachment (receipt) "
                    + "for claims above " + Expense.formatMoney(RECEIPT_THRESHOLD) + ". "
                    + id + " has none. Ask the employee to attach it, or ESCALATE to a Director.";
        }
        return null;
    }

    @Override
    public boolean approve(Expense expense) {
        String blocker = getApprovalBlocker(expense);
        if (blocker != null) {
            setLastMessage("[DENIED]   " + blocker);
            return false;
        }
        expense.approve(getName());
        approvedCount++;
        approvedTotal += expense.getAmount();
        setLastMessage("[APPROVED] " + expense.getExpenseId() + " ("
                + Expense.formatMoney(expense.getAmount()) + ") approved by "
                + getName() + " [" + grade.getLabel() + "].");
        return true;
    }

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
        expense.reject(getName(), reason);
        rejectedCount++;
        setLastMessage("[REJECTED] " + expense.getExpenseId() + " rejected by "
                + getName() + ". Reason: " + reason.trim());
        return true;
    }

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
