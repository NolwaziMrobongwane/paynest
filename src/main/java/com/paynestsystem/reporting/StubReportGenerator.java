package com.paynestsystem.reporting;

/**
 * Placeholder report — returns zeros until students query real data (Capstone 4).
 */
public class StubReportGenerator implements ReportGenerator {

    @Override
    public OperationsReport generateSummary() {
        // TODO: aggregate from TransactionRecordStore / SQL / files
        System.out.println("[OperationsReport] stub — no data wired");
        return new OperationsReport(0, 0, 0, 0.0);
    }
}
