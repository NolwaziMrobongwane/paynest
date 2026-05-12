# Capstone 3: Smart rails selection and transaction risk

## Scenario / Fictional Company Context

PayNest now connects to **two upstream payment processors** (“Provider A” and “Provider B”) with different pricing, geography, and uptime. Operations cannot manually choose a processor for every transaction—volume is too high—yet **wrong routing** causes declines (wrong bank support) or unnecessary fees (small payments on expensive rails).

Risk & compliance also asks for a **first-pass risk label** on each bank-scoped payment attempt before funds move. You work on the **integrations squad**. Leadership wants a **policy-driven router** and **explainable decisions** suitable for audit review.

## Business Problem

The organisation must answer three questions for every payment attempt:

1. **Which processor rail is appropriate** for this amount, bank, and operational posture?
2. **What risk posture** applies so downstream teams can prioritise reviews?
3. **Why** was that decision taken—**traceable reasons** (rules matched, fallback used)?

The repository ships **interfaces and skeleton classes only**. Empty `matches()` methods and “always LOW” risk are **not acceptable** as final behaviour—you must implement coherent policy.

## System Requirements

**Transaction domain**

- Model each routable attempt as a `Transaction`: monetary amount, bank identifier string, and timestamp (`Instant`). This is the unit routed and risk-scored.

**Routing**

- Implement `RoutingEngine` behaviour (via `DefaultRoutingEngine` or approved equivalent) so that **rules run in a defined priority order** and produce a `RouteDecision` containing:
  - chosen `PaymentProvider` when possible,
  - human-readable **reason** text,
  - list of **applied rule identifiers/names** for audit,
  - **fallback flag** when you deliberately use a fallback rule/provider.

**Rules**

- Flesh out `AmountRoutingRule`, `BankSupportRule`, and `FallbackRule` (and extend `AbstractRoutingRule`) so `matches` reflects real predicates driven by configuration (`RoutingConfig` thresholds, supported bank sets, etc.).

**Providers**

- Replace placeholder processor behaviour with **honest simulations**: availability toggles, typed failures, or success paths documented in code—enough to demo routing outcomes.

**Risk**

- Extend `BasicRiskEvaluator` beyond “always LOW”: amount tiers, simple velocity/frequency heuristics (may introduce a small history provider), or other justified signals mapped to `RiskLevel`.

**Audit**

- Use or replace `DecisionLogger` so routing outcomes are **reviewable** (structured console lines acceptable for this programme).

**Verification**

- **`mvn test` green** with tests that lock in representative routing and risk cases you claim in comments.

**Demonstration**

- Supply a **JUnit test** or small `main` that wires lists of rules/providers and prints or asserts outcomes for at least three scenarios (low amount, unsupported bank, fallback).

## Technical Constraints

- **Java 21**, **Maven**, **plain Java**—no Spring/repositories unless instructor overrides.
- **Compose** existing types; do not fork unrelated modules.
- Configuration via `RoutingConfig` (and extensions) rather than hard-coded magic numbers scattered across classes—reviewers will grep for unexplained literals.

## Business Rules

- **Rule ordering:** document whether lower `priority()` numbers win first or last—implement consistently.
- **Conflict policy:** when multiple rules match, define **deterministic** provider selection and capture it in `RouteDecision` reasons.
- **Fallback:** when no primary provider qualifies, `fallbackUsed` must be **true** and the reason must state why fallback engaged.
- **Risk mapping:** every returned `RiskLevel` must be **derivable** from stated inputs (amount thresholds, history window, etc.).
- **No silent default provider:** picking “first in list” without policy is a **design smell**—justify or replace.

## Expected Deliverables

- Implemented routing, rules, provider simulations, and risk logic with meaningful tests.
- **Design note** (1–2 pages max or structured README section): rule priority diagram or bullet timeline; example decision traces for two transactions.
- Evidence of **auditability**: sample log output or structured strings in `RouteDecision`.

## Assessment Framing

Top marks require **policy clarity under change**: if PayNest signs Provider C next month, your architecture shows **where** new rules plug in and **what** remains stable. Reviewers penalise opaque boolean logic and undeclared tie-breaks.

## Rubric

| Category | Weight | What reviewers look for |
|----------|--------|-------------------------|
| Architecture & routing design | 25% | Clear separation between rules, engine, providers; documented ordering; extensible config. |
| Correctness & business rules | 30% | Decisions match declared predicates; fallback semantics honest; risk mapping justified. |
| Testing & verification | 20% | Tests encode priority/tie-break/fallback cases; regressions would fail loudly. |
| Auditability & operational realism | 15% | Reasons and applied-rules lists trustworthy; logging readable under demo load. |
| Documentation & communication | 10% | Design note explains tradeoffs; instructor can replay scenarios from doc alone. |

**Anti-pattern:** all rules return `false` except a catch-all—reviewers treat that as incomplete policy unless explicitly assigned as a staged milestone.
