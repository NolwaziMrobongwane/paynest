package com.paynestsystem.ollama;

import com.paynestsystem.domain.Transaction;

import java.util.Optional;

/**
 * Always-empty client for tests and offline runs (Capstone 5).
 */
public class UnavailableOllamaClient implements OllamaClient {

    @Override
    public Optional<String> riskAssessment(Transaction transaction) {
        return Optional.empty();
    }
}
