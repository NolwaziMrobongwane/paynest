package com.paynestsystem.risk;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Defensive parsing for LLM output (Capstone 5 — replace heuristics with structured JSON schema).
 */
public class AiResponseParser {

    private static final Pattern OLLAMA_RESPONSE = Pattern.compile("\"response\"\\s*:\\s*\"([^\"]*)\"");

    /**
     * Merges rule-based risk with AI output; on any failure returns {@code ruleBased}.
     */
    public RiskLevel mergeRuleAndAi(RiskLevel ruleBased, String rawHttpBodyNullable) {
        if (rawHttpBodyNullable == null || rawHttpBodyNullable.isBlank()) {
            return ruleBased;
        }
        try {
            String text = unwrapModelText(rawHttpBodyNullable);
            Optional<RiskLevel> parsed = parseRiskToken(text);
            return parsed.map(ai -> conservativeMax(ruleBased, ai)).orElse(ruleBased);
        } catch (RuntimeException e) {
            return ruleBased;
        }
    }

    /**
     * Exposed for tests — extracts human-readable model text from Ollama {@code /api/generate} JSON when possible.
     */
    public String unwrapModelText(String rawHttpBody) {
        Matcher m = OLLAMA_RESPONSE.matcher(rawHttpBody);
        if (m.find()) {
            return m.group(1).replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return rawHttpBody;
    }

    /**
     * Looks for LOW / MEDIUM / HIGH tokens (Capstone 5 — replace with JSON binding).
     */
    public Optional<RiskLevel> parseRiskToken(String text) {
        if (text == null) {
            return Optional.empty();
        }
        String upper = text.toUpperCase(Locale.ROOT);
        if (upper.contains("HIGH")) {
            return Optional.of(RiskLevel.HIGH);
        }
        if (upper.contains("MEDIUM")) {
            return Optional.of(RiskLevel.MEDIUM);
        }
        if (upper.contains("LOW")) {
            return Optional.of(RiskLevel.LOW);
        }
        return Optional.empty();
    }

    private static RiskLevel conservativeMax(RiskLevel a, RiskLevel b) {
        return a.ordinal() >= b.ordinal() ? a : b;
    }
}
