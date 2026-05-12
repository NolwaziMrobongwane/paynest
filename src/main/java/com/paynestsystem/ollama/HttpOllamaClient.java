package com.paynestsystem.ollama;

import com.paynestsystem.domain.Transaction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

/**
 * Skeleton HTTP client for Ollama's REST API (Capstone 5 — students refine prompts, timeouts, and parsing).
 */
public class HttpOllamaClient implements OllamaClient {

    private final OllamaConfig config;
    private final HttpClient httpClient;

    public HttpOllamaClient(OllamaConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }

    @Override
    public Optional<String> riskAssessment(Transaction transaction) {
        try {
            String prompt = buildPrompt(transaction);
            String jsonBody = """
                    {"model":"%s","prompt":%s,"stream":false}
                    """.formatted(config.getModel(), escapeJsonString(prompt));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getBaseUrl() + "/api/generate"))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody.strip()))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return Optional.empty();
            }
            return Optional.ofNullable(response.body());
        } catch (Exception e) {
            // TODO: structured logging; distinguish timeouts vs parse errors upstream
            return Optional.empty();
        }
    }

    private static String buildPrompt(Transaction transaction) {
        return "Rate fraud risk for bank=%s amount=%s at %s. Reply with one word: LOW, MEDIUM, or HIGH."
                .formatted(transaction.getBank(), transaction.getAmount(), transaction.getTimestamp());
    }

    private static String escapeJsonString(String s) {
        String escaped = s.replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + escaped + "\"";
    }
}
