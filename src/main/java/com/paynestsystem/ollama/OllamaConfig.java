package com.paynestsystem.ollama;

/**
 * Local Ollama HTTP endpoint configuration (Capstone 5 — default matches {@code ollama serve}).
 */
public class OllamaConfig {

    private String baseUrl = "http://localhost:11434";
    private String model = "llama3.2";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
