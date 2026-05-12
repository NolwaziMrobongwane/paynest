package com.paynestsystem.reliability;

import com.paynestsystem.domain.TransactionRecord;

/**
 * Outcome of {@link ReliableTransactionPipeline#process}.
 */
public final class PipelineResult {

    private final TransactionRecord record;
    private final boolean duplicateRequest;

    public PipelineResult(TransactionRecord record, boolean duplicateRequest) {
        this.record = record;
        this.duplicateRequest = duplicateRequest;
    }

    public TransactionRecord getRecord() {
        return record;
    }

    public boolean isDuplicateRequest() {
        return duplicateRequest;
    }
}
