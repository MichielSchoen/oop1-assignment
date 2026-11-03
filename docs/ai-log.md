PROMPT 1

Read the `instructions.md` file carefully and use it as the source of truth for the project requirements.

Your task is to create a comprehensive `spec.md` file containing all specifications necessary to implement the project.

Follow these steps:

1. **Analyze the instructions:** Read the entire `instructions.md` file and identify all functional requirements, technical requirements, constraints, expected behaviors, and deliverables.
2. **Extract requirements:** Break down the project into individual, clearly defined specifications. Do not overlook small details or implicit requirements that are necessary for the project to work as described.
3. **Structure the specifications:** Organize `spec.md` into logical sections. Include relevant sections such as:

- Project overview and objectives
- Scope and features
- Functional requirements
- Non-functional requirements
- User interactions and workflows
- Input/output requirements
- Data structures and models
- Technical requirements and constraints
- Error handling and edge cases
- Acceptance criteria and testable requirements
4. **Make requirements testable:** Each specification must be specific, unambiguous, and independently verifiable wherever possible. Describe what the system must do rather than prescribing implementation details unless the instructions explicitly require them.
5. **Preserve the original requirements:** Do not invent features, change the intended functionality, or make assumptions without identifying them. If something is unclear or missing, document it under an `Open Questions` or `Assumptions` section rather than silently deciding.
6. **Check completeness:** Before finishing, compare the generated `specs.md` against `instructions.md` to ensure every requirement has been captured and that no important details have been omitted.
**Important constraints:**

- Do not start implementing the project.
- Do not modify `instructions.md` or other project files.
- Only update `spec.md`.
- Do not leave requirements vague or overly general.

The goal is to produce a detailed, structured, and implementation-ready `specs.md` that can serve as the foundation for the subsequent spec-driven development process.

PROMPT 2
if it were up to you, what is the place youd suggest to start with this project, making sure to take it small steps at a time

PROMPT 3

Your task is to analyze this repository and create a comprehensive `.github/copilot-instructions.md` file that guides GitHub Copilot when working on this project.

### 1. Analyze the repository
Before writing anything, inspect the project thoroughly, including:

- Project structure and directory organization.
- Existing source code and architecture.
- Programming languages, frameworks, and libraries.
- Configuration files and dependencies.
- Existing coding conventions and naming patterns.
- Build, run, and test commands.
- Existing documentation, especially `instructions.md` and `specs.md`, if available.
Use the actual repository contents as your source of truth. Do not assume technologies, conventions, or architectural decisions that you cannot verify.

### 2. Define agent instructions
Create instructions that help Copilot consistently produce code that fits this project.

Include relevant sections covering:

- **Project overview:** What the project does and its main objectives.
- **Technology stack:** Languages, frameworks, libraries, and tools.
- **Project architecture:** Important directories, modules, components, and their responsibilities.
- **Coding conventions:** Naming, formatting, code organization, and programming patterns.
- **Development guidelines:** How new features should be implemented and how existing code should be modified.
- **Specification-driven development:** Treat `spec.md` as the source of truth for functional requirements. Check specifications before implementing features and avoid implementing functionality outside the defined scope without clarification.
- **Testing requirements:** Expected testing practices and how to run tests.
- **Error handling:** Project-specific expectations for handling errors and edge cases.
- **Dependencies:** Guidelines for introducing, updating, or replacing dependencies.
- **Documentation:** Expectations for maintaining relevant documentation.
- **Restrictions:** Patterns, approaches, or changes that should be avoided.

### 3. Make the instructions actionable
Write instructions that Copilot can directly follow when generating, modifying, or reviewing code.

For example:

- Prefer existing project patterns over introducing new ones.
- Reuse existing components and utilities where appropriate.
- Avoid unnecessary abstractions and dependencies.
- Keep changes focused on the requested task.
- Do not silently change existing behavior.
- Follow the acceptance criteria defined in `spec.md`.
- Run relevant tests after making changes.
- Ask for clarification when requirements are ambiguous or contradictory.
Avoid generic advice that could apply to any repository. Make the instructions specific to this project.

### 4. Validate the result
Before finishing:

1. Verify that every instruction is supported by the repository or project documentation.
2. Check that the instructions do not contradict `instructions.md` or `spec.md`.
3. Remove redundant or conflicting guidelines.
4. Ensure the instructions are concise enough to remain useful to an AI coding agent.
5. Ensure the Markdown structure is clear and easy to navigate.

### 5. Deliverable
update `.github/copilot-instructions.md`.

Do not modify application source code, project specifications, or unrelated files.

Do not start implementing project features.

Your final result should be a project-specific instruction document that enables GitHub Copilot to understand the repository and work consistently within its established architecture, conventions, and specifications.

PROMPT 4

Add the core classes without behavior yet:
  - `Device`
  - `LightDevice`
  - `ThermostatDevice`
  - `Room`
  - `SmartHome` 
Please Only add the most basic skeleton of the core classes

PROMPT 5

please add the checked exceptions, now

PROMPT 6

Implement first: `Device` + `ThermostatDevice`
- Start with validation rules:
  - name not null/empty

PROMPT 7

now add these rules to the same files: power usage not negative
  - thermostat range valid
  - target temperature inside allowed range

PROMPT 8

please implement the following items into the copilot-instructions.md file: ## Decision-Making and Efficient Tool Usage
Your goal is to solve problems, not endlessly investigate them.

### 1. Investigate with purpose

- Understand the user's request before using tools.
- Only search the codebase when additional information is genuinely needed.
- Prefer reading relevant files over searching for individual keywords repeatedly.
- Do not search for every phrase, term, or possible interpretation of a request.
- Do not repeatedly inspect files that have already been examined.
- Avoid exploring unrelated parts of the repository.

### 2. Limit exploration

- Start with the most likely relevant files.
- Use a maximum of 3 targeted search operations before evaluating the information gathered.
- If sufficient information is available, stop searching immediately.
- Only perform additional searches when a specific unanswered question prevents progress.
- Never repeat a search that has already produced useful results.

### 3. Make decisions

- Analyze the available information and choose the most reasonable solution.
- Do not endlessly generate alternative approaches.
- When multiple solutions are possible, briefly compare them and recommend one.
- Make reasonable assumptions when necessary, and explicitly state important assumptions.
- Ask the user for clarification only when missing information materially affects the solution or creates a significant risk.

### 4. Prioritize action over discussion

- Do not describe every investigative step.
- Do not provide endless lists of possibilities.
- Do not keep searching for confirmation of an already established conclusion.
- Once you have enough information, formulate a clear conclusion and proceed with the requested task.
- If asked to implement something, implement it rather than merely explaining how it could be implemented.

### 5. Know when to stop
Stop investigating when:

- The relevant requirements are understood.
- The necessary project files have been identified.
- A reasonable implementation approach has been established.
- No critical unanswered questions remain.
Do not continue searching simply because more information might exist.

### 6. Expected response format
For tasks requiring investigation or decision-making, structure your response as follows:

1. **Understanding:** Briefly explain what you understand the task to be.
2. **Findings:** Include only relevant discoveries.
3. **Decision:** State the chosen solution and briefly explain why.
4. **Action:** Perform the requested work when applicable.
5. **Verification:** Report the checks performed and their results.
Be decisive, efficient, and focused on delivering a result.

PROMPT 9

now add the following to the room.java file: Add unique names
- Prevent removal when non-empty
- Expose the device list without allowing replacement of the internal collection
