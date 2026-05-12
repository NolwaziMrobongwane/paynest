package com.paynestsystem.reporting;

/**
 * Aggregate operational metrics for dashboards or logs (Capstone 4 — extend fields as needed).
 */
public final class OperationsReport {

    private final long totalTransactions;
    private final long completedCount;
    private final long failedCount;
    private final double totalVolume;

    public OperationsReport(long totalTransactions, long completedCount, long failedCount, double totalVolume) {
        this.totalTransactions = totalTransactions;
        this.completedCount = completedCount;
        this.failedCount = failedCount;
        this.totalVolume = totalVolume;
    }

    public long getTotalTransactions() {
        return totalTransactions;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public long getFailedCount() {
        return failedCount;
    }

    public double getTotalVolume() {
        return totalVolume;
    }
}
