---
trigger: always_on
---

# Memory Bank System Rules (Windsurf Agent)

## Directory Structure
memory-bank/
├── project-brief.md # Project overview and business context
├── product-context.md # User needs and product requirements
├── system-patterns.md # Architecture and design patterns
├── tech-context.md # Technical decisions and stack details
├── decision-log.md # Key architectural or strategic decisions
├── open-questions.md # Assumptions or pending clarifications
└── progress.md # Daily updates and progress tracking
---
---

## Update Rules

> 🤖 **Agent Instruction**: After every major code generation or user story implementation, check if any of the memory-bank files should be updated and do so with a summary entry and timestamp.

---

### `project-brief.md`

- **When to Update**: When there are changes to project scope, goals, timelines, or stakeholders.
- **Content to Include**:
  - Business objectives and success criteria
  - KPIs and project metrics
  - Stakeholders and their responsibilities
  - Project timeline and major milestones
  - High-level functional and non-functional requirements

---

### `product-context.md`

- **When to Update**: When there are changes to user needs, personas, feature specs, or market context.
- **Content to Include**:
  - User personas, goals, and pain points
  - User stories and feature requirements
  - Competitive or market analysis
  - Key workflows, journey maps, and acceptance criteria

---

### `system-patterns.md`

- **When to Update**: Whenever architectural changes, component designs, or new patterns are applied.
- **Content to Include**:
  - System architecture diagrams or descriptions
  - Design patterns used and why they were chosen
  - Component responsibilities and communication flows
  - Key API contracts or external system integrations
  - Data model snapshots or schema design decisions

---

### `tech-context.md`

- **When to Update**: Whenever technical stack, tools, CI/CD setup, or security policies change.
- **Content to Include**:
  - Tech stack and framework versions
  - Development tooling and IDE setup
  - Build, release, and deployment pipelines
  - Configuration strategy and secrets management
  - Performance, observability, and security standards

---

### `decision-log.md`

- **When to Update**: Every time a tradeoff, workaround, or major technical or product decision is made.
- **Content to Include**:
  - Decision summary
  - Alternatives considered
  - Rationale and trade-offs
  - Date and contributors
  - Link to related discussions/issues/PRs

---

### `open-questions.md`

- **When to Update**: When assumptions, pending validations, or design clarifications exist.
- **Content to Include**:
  - Unanswered questions or dependencies
  - Risk impact if left unresolved
  - Assigned owner or follow-up owner
  - Target resolution timeline (if any)

---

### `progress.md`

- **When to Update**: Every day or after each major task, milestone, or blocker resolution.
- **Content to Include**:
  - Daily or weekly status logs (timestamped)
  - Completed features or components
  - Blockers encountered and resolutions
  - Learnings and retrospectives
  - Key decisions (summarized and linked to `decision-log.md`)
  - Action items with owners

---

## Automation Instructions (For Agent)

- 🔁 **Auto-scan Triggers**: After generating code, resolving a bug, refactoring, or completing a major feature or integration.
- 📌 **If new system patterns are introduced**, append to `system-patterns.md` and link them in `tech-context.md` or `decision-log.md`.
- ✍️ **Daily Summary**: Log a short note in `progress.md` even if the update was minor.
- 🔗 **Cross-link relevant changes**: e.g., If a design pattern was used for a user requirement, reference it in both `product-context.md` and `system-patterns.md`.

---

## Version Control Rules

1. All `memory-bank` files must be version controlled.
2. Commit messages should specify updated file(s) and purpose.
3. Use semantic versioning for major documentation milestones (e.g., `v1.0.0`).
4. Create Git tags for key project stages (e.g., `alpha`, `beta`, `MVP-release`).
5. Follow branching conventions for docs (e.g., `docs/update-patterns`).

---

## Maintenance Guidelines

1. Review all files at the **start of each sprint or iteration**.
2. Keep cross-references between files consistent and updated.
3. Archive older sections when making significant rewrites.
4. Ensure documentation is always **in sync with code and PRs**.
5. Validate all links and diagrams periodically.

---

## Backup Strategy

1. Take automated daily backups of the `memory-bank/` directory.
2. Retain backups for a rolling window of **30 days**.
3. Store backups in a secure off-site or cloud location.
4. Perform quarterly validation of the backup restoration process.

---

## Review Process

1. Include `memory-bank/` updates in **Definition of Done**.
2. Ensure memory-bank entries are reviewed in PR reviews.
3. Perform a **monthly health check** of documentation.
4. Update documentation as part of issue/PR closure workflow.

---

## Best Practices

1. Keep content **clear, concise, and well-structured**.
2. Use **consistent markdown formatting and section headers**.
3. Add diagrams or visuals for architecture and flows.
4. Document **not just what was done, but why it was done**.
5. Maintain a changelog or footer timestamp for each file.
6. Use templates where applicable (decision logs, personas, etc.).
7. Include examples and references for future team members.

---

## Compliance

1. Ensure documentation follows team and org-level standards.
2. Mask or exclude sensitive data in all files.
3. Maintain a log of **who updated what and when** (via Git history).
4. Define access controls for confidential files.
5. Capture approvals for critical or externally-facing documentation.