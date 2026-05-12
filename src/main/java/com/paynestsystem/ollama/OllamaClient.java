package com.paynestsystem.ollama;

import com.paynestsystem.domain.Transaction;

import java.util.Optional;

/**
 * Calls a locally hosted LLM (Capstone 5 — implement HTTP to Ollama).
 */
@FunctionalInterface
public interface OllamaClient {

    /**
     * Optional raw JSON/text from the model; empty when offline, malformed transport, or disabled.
     */
    Optional<String> riskAssessment(Transaction transaction);
}
