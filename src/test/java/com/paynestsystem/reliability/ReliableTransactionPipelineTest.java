package com.paynestsystem.reliability;

import com.paynestsystem.domain.Transaction;
import com.paynestsystem.domain.TransactionStatus;
import com.paynestsystem.persistence.InMemoryIdempotencyRegistry;
import com.paynestsystem.persistence.InMemoryTransactionRecordStore;
import com.paynestsystem.providers.ProviderA;
import com.paynestsystem.providers.ProviderB;
import com.paynestsystem.routing.DecisionLogger;
import com.paynestsystem.routing.DefaultRoutingEngine;
import com.paynestsystem.risk.BasicRiskEvaluator;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReliableTransactionPipelineTest {

    @Test
    void secondCallWithSameKey_isDuplicate() {
        Transaction tx = new Transaction(50.0, "DemoBank", Instant.now());
        ReliableTransactionPipeline pipeline = new ReliableTransactionPipeline(
                new InMemoryIdempotencyRegistry(),
                new InMemoryTransactionRecordStore(),
                new DefaultRoutingEngine(List.of(), List.of(new ProviderA(), new ProviderB())),
                new BasicRiskEvaluator(),
                new DecisionLogger());

        PipelineResult first = pipeline.process(tx, "idem-1");
        PipelineResult second = pipeline.process(tx, "idem-1");

        assertTrue(second.isDuplicateRequest());
        assertEquals(first.getRecord().getId(), second.getRecord().getId());
        assertEquals(TransactionStatus.ROUTED, second.getRecord().getStatus());
    }

    @Test
    void firstCall_processesAndStoresRecord() {
        Transaction tx = new Transaction(10.0, "DemoBank", Instant.now());
        ReliableTransactionPipeline pipeline = new ReliableTransactionPipeline(
                new InMemoryIdempotencyRegistry(),
                new InMemoryTransactionRecordStore(),
                new DefaultRoutingEngine(List.of(), List.of(new ProviderA())),
                new BasicRiskEvaluator(),
                new DecisionLogger());

        PipelineResult result = pipeline.process(tx, "idem-new");

        assertFalse(result.isDuplicateRequest());
        assertEquals(TransactionStatus.ROUTED, result.getRecord().getStatus());
    }
}
