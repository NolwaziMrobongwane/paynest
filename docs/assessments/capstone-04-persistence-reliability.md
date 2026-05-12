# Capstone 4: Durable payment attempts and operations-grade reliability

## Scenario / Fictional Company Context

PayNest’s routing and risk layer (Capstone 3) now decides **how** to attempt a payment. The operations team’s next crisis is **reliability**: app restarts, duplicate webhook retries from partners, and finance needing **end-of-day counts** that match the database—not developer laptops.

You own the **transaction lifecycle service**. Finance insists: “**Never double-post** a merchant-visible attempt for the same client reference, and **never** claim success in reporting if the row says FAILED.”

## Business Problem

In-memory structures alone **lose state** on restart and cannot explain disputes. The platform must:

- Record each payment attempt with a **stable identity** and **business status** (`TransactionStatus`).
- Accept **idempotency keys** from upstream systems so retries **replay the same outcome** instead of duplicating side effects.
- Survive partial failures: define **retry vs terminal FAILED** policy (even if simplified).
- Emit **operational reports**: throughput, failures, and aggregate volume suitable for a morning stand-up—not fiction.

## System Requirements

**Durable record model**

- Use `TransactionRecord` (or extend cooperatively) to bind:
  - unique record id,
  - **idempotency key**,
  - embedded `Transaction` snapshot inputs,
  - lifecycle status transitions aligned with your policy,
  - routing/risk summaries needed for support (`routingSummary`, assessed risk fields where applicable).

**Pipeline orchestration**

- Implement `ReliableTransactionPipeline#process` beyond the stub: coordinate **idempotency registry**, **routing** (`RoutingEngine`), **risk evaluation** (`RiskEvaluator`), optional provider execution (`PaymentProvider#process`) when your policy says so, and **persistence** via `TransactionRecordStore`.

**Persistence**

- Replace or augment `InMemory*` implementations with **durable** storage. The repo includes the **H2** JDBC driver and `persistence.jdbc.H2Schema` placeholders—typical path is a **file-backed H2** URL such as `jdbc:h2:file:./data/paynest` with DDL you author (unique idempotency constraints, indexes as justified).

**Idempotency semantics**

- Document and implement **at-least-once** ingress with **exactly-once business effect per key** within this service boundary (practical idempotency pattern).

**Reporting**

- Replace `StubReportGenerator` with real aggregates backed by your store (counts by status, failure counts, total Rand volume—define precisely in your submission).

**Verification**

- Tests cover duplicate keys, status transitions on failure injection, and reporting sanity checks on controlled fixtures.

## Technical Constraints

- **Java 21**, **Maven**.
- **No mandatory application framework**; JDBC or file IO acceptable if justified.
- **H2** already on classpath—prefer it unless instructor approves PostgreSQL/etc.
- Maintain clear package boundaries (`reliability`, `persistence`, `reporting`, `domain`).

## Business Rules

- **Idempotency:** second call with same key returns the **same logical outcome record** (duplicate flag in `PipelineResult` where applicable) without creating a second posted attempt.
- **Status grammar:** define allowed transitions (for example `PENDING → ROUTED → COMPLETED` / `FAILED`); illegal transitions must be impossible or rejected with tests.
- **Reporting truth:** report totals **only** from persisted rows created by your pipeline—not hard-coded demo numbers.
- **Failure honesty:** forced I/O or provider exceptions map to **FAILED** (or explicit retrying state if you implement one) and remain explainable in support logs.

## Expected Deliverables

- Durable implementation + meaningful tests + **`mvn test` passing**.
- **Schema artefact:** SQL file or embedded constant listing DDL you applied; migration notes if iterative.
- **Runbook snippet:** how to wipe local H2 during dev, where files land (`data/` gitignored).
- **Short incident narrative** (half page): “Duplicate POST storm hits `/pay`—what happens?” referencing your idempotency design.

## Assessment Framing

Reviewers reward **operational thinking**: transactional boundaries (even if simplified), idempotency key uniqueness, and reports that **survive** rerunning tests against a fresh DB file. They penalise “works on my machine” stores without teardown strategy.

## Rubric

| Category | Weight | What reviewers look for |
|----------|--------|-------------------------|
| Architecture & reliability design | 25% | Clear pipeline stages; persistence abstraction; reporting uses store—not mocks in production path. |
| Correctness & business rules | 30% | Idempotency proven; statuses coherent; failure behaviour documented and tested. |
| Testing & verification | 20% | Duplicate-key tests; failure injections; reporting calculations validated on fixtures. |
| Operational realism | 15% | DDL discipline; gitignore/data hygiene; runbook notes; honest aggregates. |
| Documentation & communication | 10% | Incident narrative; schema/runbook; instructor can reproduce reports locally. |
