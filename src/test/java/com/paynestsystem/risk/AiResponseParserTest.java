package com.paynestsystem.risk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AiResponseParserTest {

    @Test
    void mergeRuleAndAi_returnsRuleLevel_whenPayloadUnparseable() {
        AiResponseParser parser = new AiResponseParser();
        RiskLevel base = RiskLevel.MEDIUM;

        RiskLevel merged = parser.mergeRuleAndAi(base, "{not-json");

        assertEquals(RiskLevel.MEDIUM, merged);
    }

    @Test
    void mergeRuleAndAi_prefersHigherRisk_whenTokenPresent() {
        AiResponseParser parser = new AiResponseParser();
        String ollamaBody = "{\"response\":\"This looks HIGH risk\"}";

        RiskLevel merged = parser.mergeRuleAndAi(RiskLevel.LOW, ollamaBody);

        assertEquals(RiskLevel.HIGH, merged);
    }
}
