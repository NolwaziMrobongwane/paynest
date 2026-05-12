# PayNest capstone assessments

This folder contains the **authoritative project briefs** for the five PayNest capstones. Use them for assignment wording, rubrics, and learner expectations. Technical setup (compile, test, run) stays in the repository root [README.md](../../README.md).

## Progression

| Order | Brief | You are building |
|-------|--------|------------------|
| 1 | [Capstone 1: Merchant order desk](capstone-01-commerce-engine.md) | A small commercial **order and pricing engine** for SME merchants: catalogue, customers, line items, printable order summary. |
| 2 | [Capstone 2: Unified checkout](capstone-02-payment-methods.md) | A **polymorphic payment layer** so checkout does not care which rail (card, EFT, wallet) is used. |
| 3 | [Capstone 3: Smart rails and risk](capstone-03-routing-risk.md) | **Provider routing** and **risk scoring** for bank-scoped payment attempts (`Transaction`), with auditable decisions. |
| 4 | [Capstone 4: Durable money path](capstone-04-persistence-reliability.md) | **Idempotent pipelines**, **durable state**, and **operational reports** on top of the routing and risk abstractions. |
| 5 | [Capstone 5: Live risk operations](capstone-05-agentic-monitoring.md) | A **queue-backed monitoring path** with **local LLM** assistance (Ollama), **defensive parsing**, and **AI decision audit** trails. |

**Prerequisites chain:** each capstone assumes the previous is conceptually done. The Git repository may ship partial scaffolding (especially 3–5); your job is to **complete behaviour** to the brief, with tests and documentation of tradeoffs.

## How to use with the repo

- Build and test: `mvn compile` and `mvn test` (Java 21, Maven).
- Default `PayNestApplication` demonstrates early flows; later capstones often use **tests** or a **custom `main`** as specified in each brief.
- **Capstone 5** needs a local [Ollama](https://ollama.com/) install for full integration; offline degradation must still work.

If instructions in this folder conflict with coursework slides, **follow your instructor** unless they defer to these briefs.
