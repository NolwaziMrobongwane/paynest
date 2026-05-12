package com.paynestsystem.risk;

import com.paynestsystem.domain.Transaction;
import com.paynestsystem.ollama.UnavailableOllamaClient;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HybridRiskEvaluatorTest {

    @Test
    void evaluate_matchesRuleEvaluator_whenOllamaUnavailable() {
        RiskEvaluator rules = new BasicRiskEvaluator();
        HybridRiskEvaluator hybrid = new HybridRiskEvaluator(
                rules,
                new UnavailableOllamaClient(),
                new AiResponseParser());

        Transaction tx = new Transaction(99.0, "Bank", Instant.now());

        assertEquals(rules.evaluate(tx), hybrid.evaluate(tx));
    }
}
