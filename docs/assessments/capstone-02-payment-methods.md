# Capstone 2: Unified checkout across payment rails

## Scenario / Fictional Company Context

PayNest’s pilot merchants loved the **order desk** (Capstone 1). Now they ask for **checkout**: pay by card, by bank transfer (EFT), or from an in-app wallet. Operations hates maintaining three separate “if card / else if EFT” codepaths—they want **one checkout funnel** that accepts any supported rail today and can add another rail **without rewriting order logic**.

You remain on the backend team. Product’s pitch is: “Same order total everywhere; only the payment rail changes.”

## Business Problem

Hard-coded branching per payment type **does not scale**: each new rail duplicates validation messaging, logging, and failure handling. PayNest needs a **payment abstraction** so `Order` (or the checkout orchestrator) depends on a **single contract**—process this Rand amount—while concrete rails encapsulate rail-specific behaviour (labels, simulated processing). The business still needs a **clear console narrative** when payment succeeds or fails so support staff can replay demos.

## System Requirements

**Polymorphic payment processing**

- Represent “how we pay” as a reusable abstraction (`PaymentMethod`-style contract in the codebase).
- Provide **at least three** concrete rails (card, EFT, wallet) with distinct user-visible messaging (`getPaymentType()` or equivalent).
- Process a payment for the **order grand total** using `processPayment(double amount)` semantics aligned with the starter implementation.

**Checkout orchestration**

- Order checkout must **calculate the total from line items** (same business meaning as Capstone 1) and pass that amount into the payment abstraction.
- Successful checkout prints confirmation tying **payment rail**, **amount**, and **order completion** (behaviour consistent with `PayNestApplication` expectations).

**Integration surface**

- Callers (such as `PaymentProcessor` or `Order#checkout`) must accept **any** `PaymentMethod` implementation without recompilation when a new rail is added—validated by design review and/or tests.

**Demonstration**

- The default application flow selects a concrete payment type and completes checkout end-to-end.

## Technical Constraints

- **Java 21**, **Maven**, plain Java—**no** payment gateway SDKs or HTTP calls in this capstone unless your instructor extends the brief.
- **Interfaces & polymorphism** are mandatory; avoid large `switch` on string payment type in core checkout logic (small switches inside concrete rails are acceptable if justified).
- Preserve Capstone 1 domain semantics unless you explicitly migrate with tests.

## Business Rules

- **Amount charged** at checkout must equal the order total produced by the order model for that checkout invocation (no silent discounts).
- Each rail exposes a **stable human-readable type label** suitable for logs and receipts.
- **`processPayment`** returns or signals success/failure consistently with the starter contract; document assumptions if you extend return types (prefer staying within the existing API unless approved).
- **Adding a new rail** should require **new class + wiring**, not editing `Order`’s core arithmetic.

## Expected Deliverables

- Source implementing polymorphic payments and integrated checkout.
- **`mvn test` passes**; include tests that exercise checkout with **at least two** different payment implementations (polymorphism actually used).
- Short note (submission README or PDF): **one paragraph** on why interfaces beat a single mega-method here; optional **class diagram** showing `PaymentMethod` and implementations.
- Demo instructions matching programme standards.

## Assessment Framing

Reviewers reward **dependency direction**: domain and checkout depend on abstractions, not concrete card classes. They will ask: “If we add `BuyNowPayLaterPayment` next sprint, what files change?” The best answers touch **one new class** plus composition root / demo wiring—not `Order` internals.

## Rubric

| Category | Weight | What reviewers look for |
|----------|--------|-------------------------|
| Architecture & polymorphism | 25% | Clean `PaymentMethod` contract; checkout depends on abstraction; minimal branching at call sites. |
| Correctness & business rules | 30% | Correct total flows into payment; rail labels coherent; success path matches domain expectations. |
| Testing & verification | 20% | Tests prove polymorphic behaviour (multiple rails); regression protection on totals + checkout. |
| Code quality & maintainability | 15% | Readable implementations per rail; no duplicated total calculation paths. |
| Documentation & communication | 10% | Brief design rationale; clear run instructions; diagram optional but rewarded if precise. |

**Distinction-level signal:** new payment type added in a **timed extension exercise** with minimal edits outside the new class and wiring.
