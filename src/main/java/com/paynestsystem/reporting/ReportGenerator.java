package com.paynestsystem.reporting;

/**
 * Builds operational reports from persisted state (Capstone 4 — implement queries against your store).
 */
public interface ReportGenerator {

    OperationsReport generateSummary();
}
