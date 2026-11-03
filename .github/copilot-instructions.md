# Copilot instructions for the Smart Home assignment

## Project overview

This repository contains a Java/Maven assignment for building a console-based smart home application. The application manages a single home with rooms, devices, power usage, and summary statistics. All data is kept in memory only; when the program exits, all state is lost.

The assignment is intentionally spec-driven: the expected behavior is defined in the repository documentation, especially the root `INSTRUCTIONS.md` file and the working specification in `docs/spec.md`. The project is a classroom assignment, not a production app, so the priority is correctness against the specification, clear Java design, and testability.

## Repository structure

Key files and directories in this repository:

- `INSTRUCTIONS.md` — the formal assignment brief and source of truth for requirements.
- `docs/spec.md` — the implementation specification and acceptance criteria.
- `docs/DECISIONS.md` — place for open-point decisions and rationale.
- `docs/ai-log.md` — evidence of specification-driven AI / design work.
- `pom.xml` — Maven build and dependency configuration.
- `src/main/java/` — application source code.
- `src/test/java/` — unit tests.
- `uml/uml-diagram.png` — UML reference for the required class hierarchy and public API.
- `console-user-interface.png` — menu / console UI reference.

The project is currently scaffolded; implementation files are not yet present. Follow the spec before creating new classes or methods.

## Technology stack

- Java 25 (`maven.compiler.release` in `pom.xml`)
- Maven build tool
- JUnit Jupiter for tests
- Mockito for mocking `SmartHome` in service-layer tests
- Jacoco for coverage reporting
- Javadoc generation via Maven plugin

## Architecture and design expectations

- Use the base package `nl.inholland.oop1`.
- Keep the implementation aligned with the UML contract in `uml/uml-diagram.png`.
- The required public API must be preserved; do not rename or remove required members.
- Use clear domain modeling for:
  - `SmartHome`
  - `Room`
  - `Device`
  - `LightDevice`
  - `ThermostatDevice`
  - `HomeStats`
  - `SmartHomeServiceImpl`
  - `Application`
  - `ConsoleUserInterface`
- Keep the design object-oriented and type-safe rather than procedural.
- Prefer explicit domain behavior over hidden side effects.

## Specification-driven development

Treat `INSTRUCTIONS.md` and `docs/spec.md` as the authoritative requirements. Before implementing a feature:

1. Read the relevant requirement in the specification.
2. Confirm the required API and behavior from the UML / assignment notes.
3. Keep implementation constrained to the defined scope.
4. If the spec leaves a design gap, document the decision in `docs/DECISIONS.md` with reasoning and a proving test.

Do not implement functionality that is not described or justified by the assignment unless the user explicitly requests it.

## Coding conventions

- Follow Java naming conventions: PascalCase for classes, camelCase for methods/variables.
- Prefer small, focused classes and methods.
- Keep code readable and aligned to the assignment’s domain language.
- Preserve the required public API exactly; add helper methods only when necessary and justified.
- Use existing project patterns rather than introducing new abstraction layers unnecessarily.
- Prefer domain-specific validation over silent coercion.
- Use lambdas or method references where they improve clarity, for example in filtering and sorting.
- At least one `Selector` must be implemented via an anonymous class, as required by the assignment; document the reason in `DECISIONS.md`.

## Development guidelines

- Start from the domain model and validation rules before building the UI.
- Confirm behavior with the smallest targeted tests before widening scope.
- Keep changes focused and avoid unrelated refactoring.
- Do not silently change existing behavior or “fix” issues not related to the requested task.
- When adding public methods, ensure they are justified by the spec or by a documented decision.
- Handle invalid input in the UI without crashing; return to the menu instead of terminating the app.
- Keep all collections non-null and return empty collections rather than `null`.

## Testing expectations

- Write unit tests for all classes except `ConsoleUserInterface` and `Application`.
- Test business rules and edge cases, not just the happy path.
- Mock `SmartHome` with Mockito when testing `SmartHomeServiceImpl`.
- Cover validation failures, exception behavior, statistics logic, sorting, filtering, room checks, and device id assignment.
- Run the smallest relevant test command first; use broader Maven verification before finishing.

Use these commands when validating the project:

- `mvn test` — focused test execution
- `mvn verify` — full verification including coverage report
- `mvn javadoc:javadoc` — Javadoc generation check

Also verify that the coverage report is generated at `target/site/jacoco/index.html`.

## Error handling and edge cases

The project must be resilient to bad input and invalid operations.

Follow these patterns:

- Reject invalid names and negative power usage with `IllegalArgumentException`.
- Throw the exact custom checked exceptions and messages specified in the assignment:
  - `DeviceNotFoundException`: `Device with id '<id>' not found.`
  - `RoomNotFoundException`: `Room '<name>' not found.`
  - `RoomNotEmptyException`: `Room '<name>' still contains <n> device(s).`
- Use fail-safe UI handling: invalid numeric input, invalid id selection, invalid temperatures, and unsupported actions should show a clear message and continue.
- Do not let runtime errors escape the console menu in normal user workflows.
- Validate thermostat ranges and target temperatures strictly inside the allowed range.

## Dependency policy

- Prefer the dependencies already configured in `pom.xml`.
- Do not add new frameworks or libraries unless the specification or assignment explicitly requires them.
- Keep dependency changes minimal and justified.
- If a dependency is added, document why it is needed and ensure it is compatible with the Java 25 setup.

## Documentation requirements

- Add Javadoc to every public class and every public method.
- Document purpose, parameters, return values, and thrown exceptions.
- Keep documentation aligned with actual behavior; do not describe features not implemented.
- Maintain `docs/spec.md`, `docs/DECISIONS.md`, and `docs/ai-log.md` as evidence of the spec-driven workflow.

## Restrictions and guardrails

- Do not implement functionality outside the assignment scope without explicit instruction.
- Do not rename required UML members or remove required methods.
- Do not replace the in-memory, console-based architecture with persistent or GUI code.
- Do not silently choose behavior that contradicts the specification; record such decisions in `docs/DECISIONS.md`.
- Do not assume a technology or pattern that is not supported by the repository or the assignment.
- Do not leave requirements ambiguous; if a requirement is unclear, document the question and the chosen resolution.

## Working style for Copilot

When generating or modifying code:

- read the relevant assignment files first
- keep the change narrow and relevant
- follow the existing Java/Maven conventions in the repo
- prefer the simplest correct implementation
- validate with the smallest necessary Maven test command
- preserve the requirement-driven flow from the assignment and `docs/spec.md`

The target is a small, correct, testable Java implementation that matches the smart-home specification, not a generalized framework or extra features.
