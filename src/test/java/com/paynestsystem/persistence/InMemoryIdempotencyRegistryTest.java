package com.paynestsystem.persistence;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryIdempotencyRegistryTest {

    @Test
    void lookup_returnsBoundRecordId() {
        InMemoryIdempotencyRegistry registry = new InMemoryIdempotencyRegistry();
        registry.bind("pay-001", "rec-abc");

        assertEquals(Optional.of("rec-abc"), registry.lookup("pay-001"));
    }

    @Test
    void lookup_returnsEmpty_whenUnknownKey() {
        assertTrue(new InMemoryIdempotencyRegistry().lookup("missing").isEmpty());
    }
}
