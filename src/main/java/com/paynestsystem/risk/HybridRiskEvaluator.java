package com.paynestsystem.risk;

import com.paynestsystem.domain.AiDecisionRecord;
import com.paynestsystem.domain.Transaction;
import com.paynestsystem.ollama.OllamaClient;
import com.paynestsystem.persistence.AiDecisionStore;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Combines {@link RiskEvaluator} with optional Ollama output (Capstone 5 — students tune merge policy).
 */
public class HybridRiskEvaluator implements RiskEvaluator {

    private final RiskEvaluator ruleBased;
    private final OllamaClient ollamaClient;
    private final AiResponseParser parser;
    private final AiDecisionStore aiDecisionStore;

    public HybridRiskEvaluator(RiskEvaluator ruleBased, OllamaClient ollamaClient, AiResponseParser parser) {
        this(ruleBased, ollamaClient, parser, null);
    }

    public HybridRiskEvaluator(
            RiskEvaluator ruleBased,
            OllamaClient ollamaClient,
            AiResponseParser parser,
            AiDecisionStore aiDecisionStore) {
        this.ruleBased = Objects.requireNonNull(ruleBased);
        this.ollamaClient = Objects.requireNonNull(ollamaClient);
        this.parser = Objects.requireNonNull(parser);
        this.aiDecisionStore = aiDecisionStore;
    }

    @Override
    public RiskLevel evaluate(Transaction transaction) {
        return evaluateAndAudit(transaction, null);
    }

    /**
     * When {@code transactionRecordId} is non-null and an {@link AiDecisionStore} was provided, persists an
     * {@link AiDecisionRecord}.
     */
    public RiskLevel evaluateAndAudit(Transaction transaction, String transactionRecordId) {
        RiskLevel base = ruleBased.evaluate(transaction);
        Optional<String> raw = ollamaClient.riskAssessment(transaction);
        RiskLevel resolved = parser.mergeRuleAndAi(base, raw.orElse(null));

        if (aiDecisionStore != null && transactionRecordId != null) {
            aiDecisionStore.save(new AiDecisionRecord(
                    transactionRecordId,
                    Instant.now(),
                    raw.orElse(""),
                    resolved,
                    raw.isEmpty()));
        }

        return resolved;
    }
}
