package com.paynestsystem.monitoring;

import com.paynestsystem.domain.Transaction;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * Near-real-time queue for transactions (Capstone 5 — students add executor / threads and back-pressure).
 */
public class RiskMonitoringService {

    private final BlockingQueue<Transaction> queue = new LinkedBlockingQueue<>();

    public void enqueue(Transaction transaction) {
        queue.offer(transaction);
    }

    /**
     * Processes up to {@code max} queued items on the calling thread (demo-friendly).
     */
    public int drain(Consumer<Transaction> consumer, int max) {
        int processed = 0;
        while (processed < max) {
            Transaction next = queue.poll();
            if (next == null) {
                break;
            }
            consumer.accept(next);
            processed++;
        }
        return processed;
    }
}
