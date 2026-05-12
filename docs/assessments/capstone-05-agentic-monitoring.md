# Capstone 5: Near-real-time risk monitoring with local LLM assistance

## Scenario / Fictional Company Context

Fraud and compliance pressure increased after PayNest’s launch. Rule-only scoring (Capstone 3–4) catches known patterns but misses nuanced fraud narratives. Leadership experiments with an **on-prem AI assist**: a **small local model** via **Ollama** (no third-party cloud keys in the teaching lab) that proposes **risk hints**, while humans retain accountability.

You join the **monitoring squad**. Your mandate: stream transactions through a **worker-style path**, combine **deterministic rules** with **AI hints**, persist **audit artefacts**, and **never brick** processing when the model hallucinates or the daemon is offline.

## Business Problem

Operations needs:

- **Continuous intake** of payment attempts for monitoring (queue-backed workflow).
- **Hybrid assessment**: baseline rules plus optional AI augmentation when available.
- **Correlated audit**: tie AI outputs and final decisions back to **transaction records** for investigations.
- **Graceful degradation**: network/timeouts/malformed JSON must fall back to rule-only classification **without crashing** the JVM thread or leaving records inconsistent.

## System Requirements

**Near-real-time intake**

- Extend `RiskMonitoringService` beyond enqueue/drain demos: implement a **worker loop** (dedicated thread or executor) that pulls work and drives processing with defined shutdown/error behaviour at a junior-appropriate level.

**Ollama integration**

- Configure `OllamaConfig` (base URL default `http://localhost:11434`, model name).
- Implement or harden `HttpOllamaClient` against real `/api/generate` responses; tune timeouts and prompts responsibly.

**Hybrid risk**

- Use `HybridRiskEvaluator` / `AiResponseParser` thoughtfully: define **merge policy** (for example conservative escalation—worst of rule vs parsed AI band) and document it.
- Provide **`evaluateAndAudit`** paths so when a `transactionRecordId` exists you persist **AI decision records** via `AiDecisionStore`.

**State linkage**

- Update `TransactionRecord` monitoring fields (`assessedRisk`, `monitoringFlag`, `aiAssessmentSummary`, etc.) consistent with final hybrid outcomes and persist through your Capstone 4 store.

**Robustness**

- Property-style tests or examples proving garbage LLM output maps to safe behaviour (`AiResponseParser`).

**Demonstration**

- Dry-run script: enqueue synthetic transactions, show logs + persisted audit rows (memory acceptable if labelled non-production), plus **offline mode** using `UnavailableOllamaClient`.

## Technical Constraints

- **Java 21**, **Maven**.
- **Ollama** runs locally; students must document model pull (`ollama pull …`) and daemon lifecycle.
- HTTP via **`java.net.http`** (already used in scaffolding)—no requirement for extra REST frameworks.
- Threading: avoid unbounded thread explosion; document queue **back-pressure** strategy (drop vs block vs bounded queue policy).

## Business Rules

- **Fallback precedence:** if AI unavailable or unparsable, **final risk** must equal stated rule-based merge outcome—never invent HIGH from noise.
- **Audit completeness:** each hybrid evaluation tied to a durable record must leave an **`AiDecisionRecord`** trail when persistence is enabled (note raw payload handling and PII policy per instructor).
- **Conservative merge:** document whether you take **max ordinal risk**, **weighted blend**, or instructor-approved alternative—implement consistently.
- **No silent success:** monitoring failures should surface in logs or persisted status fields, not vanish.

## Expected Deliverables

- Runnable monitoring path + **`mvn test`** including parser/hybrid offline tests.
- **Ops note** (1–2 pages): threading model, queue bounds, what happens when Ollama stops mid-shift.
- **Demo checklist:** cold start → enqueue → worker processes → audit rows/query output.
- Optional **sequence diagram**: queue → pipeline → hybrid evaluator → stores.

## Assessment Framing

Highest grades combine **systems thinking** with **defensive coding**: parsing limits, timeouts, and observable degraded mode. Reviewers look for **honest discussion** of limitations (single-node queue, lab-only LLM) versus silent overselling.

## Rubric

| Category | Weight | What reviewers look for |
|----------|--------|-------------------------|
| Architecture & hybrid design | 25% | Clear separation of queue, evaluation, persistence; documented merge policy; audit trail design. |
| Correctness & resilience | 30% | Offline degradation; malformed AI handling; transactional sanity with Capstone 4 fields. |
| Testing & verification | 20% | Parser tests; hybrid offline parity; monitoring smoke tests where feasible without flaky LLM asserts. |
| Operational realism | 15% | Thread/queue policy; Ollama ops notes; avoids blocking infinite growth without strategy. |
| Documentation & communication | 10% | Ops note + demo checklist; diagrams rewarded when accurate. |

**Note:** LLM outputs vary—tests must **not** depend on creative text from live models; stub payloads or recorded fixtures are encouraged.
