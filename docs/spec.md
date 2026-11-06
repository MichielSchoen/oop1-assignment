# Smart Home – Specification

## 1. Project overview

This project implements a console-based Java application for managing a single smart home. The application manages rooms, the devices within those rooms, the power consumption of each device, and summary statistics for the home.

The system is intentionally in-memory only: all state is kept in memory while the program is running and is lost when the application exits. The home always starts empty with no rooms and no devices.

The solution must follow the UML contract described in the project instructions and must support the required public API as defined there. The package structure is part of the assignment, with the base package `nl.inholland.oop1`.

## 2. Objectives and scope

The application must:

- allow management of multiple rooms in one home
- allow devices to be created and assigned to rooms
- support distinct behavior for light devices and thermostat devices
- track each device’s id, name, type, and power usage
- enforce validation rules for names, power usage, thermostat ranges, and targeting temperatures
- prevent invalid room removal and invalid device access
- provide statistics for all current devices
- expose a console menu that handles invalid input without crashing
- provide enough documentation and tests to satisfy the project requirements

The scope does not include persistent storage, network access, or graphical UI. The application works entirely in RAM and uses the console as the user interface.

## 3. Core domain model

### 3.1 Home

- The system manages exactly one SmartHome instance.
- The home starts empty.
- The home contains rooms and devices.
- Device ids are unique across the entire home, not just within a room.

### 3.2 Room

- A room has a unique name.
- A room may contain zero or more devices.
- A room cannot be removed while it still contains devices.
- Looking up a room by name that does not exist must fail with a `RoomNotFoundException`.

### 3.3 Device

All devices share the following characteristics:

- a name
- a power usage in watts (`powerUsage`)
- an optional id that is `null` until the device is added to a room

Device validation rules:

- `null` or empty names are rejected
- negative power usage is rejected
- validation must occur in constructors and setters
- invalid values must throw `IllegalArgumentException`

Each device must report its concrete type:

- `LightDevice` returns `"Light"`
- `ThermostatDevice` returns `"Thermostat"`

The `describe()` method must format output as:

- `| id: <id> || name: <name> || type: <type> |`

For thermostats, the description also includes the temperature range and target temperature in the same format.

`toString()` must return exactly the same result as `describe()`.

### 3.4 Thermostat

A thermostat is a specialized device with additional state:

- a temperature range in the format `"<min>-<max>"`, for example `"16-24"` or `"16.5-23.5"`
- a target temperature
- validation for range boundaries and target values

Thermostat validation rules:

- the range string must not be empty
- the range must lie within 5 and 30 °C inclusive
- the minimum value must be lower than the maximum value
- invalid range input throws `IllegalArgumentException`
- a new thermostat starts with its minimum temperature as the target temperature
- `setTargetTemperature` rejects targets outside the allowed range with `IllegalArgumentException`

### 3.5 Statistics

The application provides a nested `HomeStats` class inside `SmartHome`.

`HomeStats` must calculate:

- average power usage across all current devices, or `0.0` when there are no devices
- count of devices by type

## 4. Functional requirements

### 4.1 Device lifecycle

- A newly created device has no id (`null`) until it is assigned to a room.
- When a device is added to a room, the system assigns a new unique id.
- The new id must be calculated as the highest existing id across all rooms plus one.
- Any existing preset id on the device must be ignored during assignment.
- The system must return the assigned id after adding the device.

### 4.2 Room operations

- A room name must be unique within the home.
- The application must allow adding and removing rooms.
- `removeRoom` must refuse to remove a room that still has devices and must throw `RoomNotEmptyException`.
- A caller must receive `RoomNotFoundException` when attempting to use a room name that does not exist.
- `Room.getDevices()` must expose the room’s device collection without allowing external callers to replace or mutate the underlying collection.

### 4.3 SmartHome operations

The `SmartHome` API must support the following behaviors:

- `addDeviceToRoom(roomName, device)` adds an existing device to a room and assigns a unique id
- `removeDevice(id)` removes the device with the given id and returns `true` if a device was removed; otherwise `false`
- `getById(id)` returns the matching device or throws `DeviceNotFoundException`
- `getAllDevices()` returns a new list, never `null`
- `find(filter)` returns a new list of matching devices; throws `NullPointerException` if the filter is `null`
- `findByType(type)` is a generic method; e.g. `findByType(ThermostatDevice.class)` returns a `List<ThermostatDevice>`
- `setTargetTemperature(id, target)` modifies a thermostat’s target; if the device is not a thermostat, the implementation must raise the predefined error chosen in the project decisions
- `sortedByPowerUsage()` returns all devices ordered by descending power usage; devices with equal usage must be ordered by name
- `totalPowerUsage()` returns the sum of all device power usages
- `stats()` returns a snapshot object representing current home statistics based only on current devices, not historic data

### 4.4 Service and application wiring

- `SmartHomeServiceImpl` receives a `SmartHome` instance through its constructor.
- `Application` creates and connects the required objects.
- The application must manage the interaction between the service layer and the console user interface.

### 4.5 Console user interface requirements

The console user interface must:

- present the menu as shown in the provided `console-user-interface.png`
- support user interaction with the home management features
- allow users to change a thermostat target temperature by device ID
- never crash on invalid input, including but not limited to:
  - non-numeric menu input
  - unknown room ids or names
  - invalid temperature ranges
  - out-of-range target temperatures
  - invalid ids or device selection
- display clear error messages when invalid input or rejected operations occur
- return the user to the main menu after an error instead of terminating the program
- provide a menu action for setting a thermostat target, prompting for its device ID and target temperature

The UI must validate and handle all user actions in a fail-safe way.

## 5. Exception model

### 5.1 Checked custom exceptions

All custom exceptions are checked exceptions.

Required exception messages:

- `DeviceNotFoundException`: `Device with id '<id>' not found.`
- `RoomNotFoundException`: `Room '<name>' not found.`
- `RoomNotEmptyException`: `Room '<name>' still contains <n> device(s).`

### 5.2 Validation exceptions

- Invalid constructor or setter input must throw `IllegalArgumentException`.
- Invalid data passed into the UI or service layer must not crash the program; a clear error message must be shown and the program must continue.

### 5.3 Open design decision

The specification requires a decision for behavior when calling `setTargetTemperature` on a device that is not a thermostat. This is a project-defined open point and must be documented in `DECISIONS.md` together with the reasoning and the test that proves the decision.

## 6. Non-functional requirements

### 6.1 Java and project structure

- The solution must use Java and follow the project UML and required class hierarchy.
- Packages are not shown in the UML; organising classes into packages is part of the assignment.
- The base package is `nl.inholland.oop1`.
- The implementation must preserve the required public API shown in the UML and may add private helper methods or additional public helper methods only when justified in `DECISIONS.md`.

### 6.2 Data and collection behavior

- All returned collections must be non-null.
- Empty results must be represented as empty collections, not `null`.
- `Room.getDevices()` must not allow callers to replace the internal collection.
- `getAllDevices()` and `find(...)` must always return a new collection instance.

### 6.3 Documentation requirements

- Every public class and every public method must have Javadoc.
- The Javadoc must describe purpose, parameters, return values, and thrown exceptions.
- `mvn javadoc:javadoc` must run without errors.

### 6.4 Testing and verification

- Unit tests must be written in JUnit for all classes except `ConsoleUserInterface` and `Application`.
- The test suite must cover the business rules and relevant edge cases.
- `SmartHomeServiceImpl` must be tested in isolation by mocking `SmartHome` with Mockito.
- The project must be verified with Maven using `mvn verify`.
- Coverage must be checked via `target/site/jacoco/index.html`.

### 6.5 Spec-driven workflow

The project is expected to use AI tooling while making the working process visible in the repository. The assignment explicitly requires evidence of spec-driven work, such as:

- `docs/spec.md` (the specification document created before implementation)
- `DECISIONS.md` documenting open decisions and rationale
- `docs/ai-log.md` noting prompts, mistakes, and corrections
- additional AI instruction or customization files, where appropriate

This is a grading requirement and is not optional.

## 7. User workflows and interactions

### 7.1 Typical device workflow

1. Create or select a room.
2. Create a device with a valid name and power usage.
3. Add the device to the room.
4. The device receives a unique id generated by the smart home.
5. Retrieve or inspect the device via its id.
6. If the device is a thermostat, adjust the target temperature within range.
7. Review current statistics and device listings.

### 7.2 Typical room workflow

1. Add a room with a unique name.
2. Add devices to the room.
3. The application blocks removal if the room still contains devices.
4. If the room is empty, it may be removed successfully.

### 7.3 Error-handling workflow

- Invalid input from the user must never crash the application.
- The user sees a clear message and returns to the menu.
- Domain rule violations must be translated into appropriate user-visible messages without terminating the program.

## 8. Input and output requirements

### 8.1 Input

The console application accepts user input through a menu-driven interface. Input may include:

- numeric selections
- strings for names and room names
- numeric values for power usage and target temperature
- temperature-range strings in the format `"min-max"`

All invalid user input must be handled gracefully.

### 8.2 Output

The application must produce clear output for:

- menu display
- device lists
- room lists
- room and device creation results
- error messages
- statistics summaries

Device and thermostat output must respect the exact formatting requirements of `describe()` and `toString()`.

## 9. Data structures and models

The implementation must model, at minimum, the following concepts:

- `SmartHome`
- `Room`
- `Device`
- `LightDevice`
- `ThermostatDevice`
- `HomeStats`
- `SmartHomeServiceImpl`
- `Application`
- `ConsoleUserInterface`

The model must support polymorphism and generics as required by the assignment, including:

- `getType()` as polymorphic behavior
- `describe()` as polymorphic output
- `findByType(...)` as a generic collection query
- lambda expression or method reference usage for filtering and sorting where appropriate

The assignment also requires at least one use of an anonymous class for a `Selector`, with a justification recorded in `DECISIONS.md` describing when that pattern is preferable to a lambda.

## 10. Technical constraints and implementation rules

- The application keeps all data in memory only.
- All data is lost on exit.
- The implementation must not rename or remove anything in the required public API shown in the UML.
- Additional public methods are allowed only when justified in `DECISIONS.md`.
- Helper methods may be private.
- The assignment requires the use of lambdas or method references where they improve clarity, such as filtering and sorting.
- The project must compile and pass verification through Maven.

## 11. Error handling and edge cases

The implementation must handle the following edge cases correctly:

- empty home with no rooms and no devices
- empty room with no devices
- attempt to remove a non-empty room
- attempt to access a missing room by name
- attempt to access a missing device by id
- invalid name input (`null`, empty string)
- invalid power usage (`null` is not relevant for primitive values; negative usage is invalid)
- invalid thermostat temperature range
- target temperature outside the thermostat range
- unknown id in UI selection logic
- text entered where a numeric choice is required
- zero results from `find(...)`, `findByType(...)`, or `getAllDevices()`

All of the above must produce controlled program behavior, not crashes.

## 12. Acceptance criteria and testable requirements

The implementation is accepted only if all of the following are true:

1. The project compiles successfully with Maven.
2. `mvn verify` succeeds on a clean checkout.
3. `mvn javadoc:javadoc` runs without errors.
4. Unit tests exist for all business logic classes except `ConsoleUserInterface` and `Application`.
5. `SmartHomeServiceImpl` is tested with Mockito mocks of `SmartHome`.
6. The home starts empty and remains in-memory only.
7. Device ids are assigned correctly and are globally unique across all rooms.
8. Room removal is blocked when the room still contains devices.
9. Custom checked exceptions use the exact specified messages.
10. Device and thermostat validation logic rejects invalid inputs with `IllegalArgumentException`.
11. Thermostat target temperature respects the defined range.
12. `find(...)` and `findByType(...)` return non-null collections and handle null filters as required.
13. `sortedByPowerUsage()` orders devices by descending power usage and then by name.
14. `totalPowerUsage()` and `stats()` reflect the current state only.
15. `ConsoleUserInterface` never crashes on invalid input and returns safely to the menu.
16. The project includes evidence of spec-driven AI usage through the required repository files and decisions documentation.

## 13. Open questions and explicit decisions to be recorded

The assignment deliberately leaves some product decisions unresolved. These must be captured in `DECISIONS.md` and justified with reasoning and a proving test.

Open design points include:

- which exception should be thrown when `setTargetTemperature` is called on a non-thermostat device
- the id assigned to the first device in an empty home
- whether `getCountByType()` includes an entry for a type with zero devices
- any additional edge cases or ambiguities discovered during detailed analysis of the specification

The implementation team must identify all such gaps and resolve them in writing before or alongside the code.

## 14. Deliverables

The deliverables required by the assignment are:

- working Java project implementing the smart home specification
- JUnit tests covering business rules and edge cases
- Javadoc for every public class and public method
- `DECISIONS.md` documenting open points and resolved design choices
- `docs/ai-log.md` documenting AI-assisted workflow and corrections
- `docs/spec.md` as the specification baseline used before coding
- final repository ready for verification with Maven

## 15. Summary of required behavior

The project is a single-home, console-driven, in-memory smart-home management application that follows a strict UML public API, enforces validation and exception rules, keeps the UI fault-tolerant, and provides domain statistics and documentation. The implementation must be backed by disciplined spec-driven development evidence and must satisfy the full Maven verification pipeline.
