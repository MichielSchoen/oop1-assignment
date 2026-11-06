# Smart Home decisions

## Calling `setTargetTemperature` for a non-thermostat

- **Question:** What should happen when the device id exists but belongs to a non-thermostat?
- **Decision:** Throw `IllegalArgumentException`; a missing id continues to throw `DeviceNotFoundException`.
- **Reason:** The id lookup succeeds, but the requested operation is incompatible with that device's type.
- **Proving test:** `SmartHomeOperationsTest.setTargetTemperatureUpdatesThermostatAndRejectsOtherDevices`.

## Device type counts with no devices

- **Question:** Should `HomeStats.getCountByType()` include device types with a zero count?
- **Decision:** Include only types present in the snapshot; an empty home returns an empty map.
- **Reason:** The map reports counts observed in the snapshot and does not invent categories not represented by devices.
- **Proving test:** `SmartHomeOperationsTest.emptyHomeStatsHaveZeroAverageAndNoTypeCounts`.

## First device ID

- **Question:** What ID should be assigned to the first device in an empty home?
- **Decision:** Start IDs at `1`; each new ID is the highest currently assigned ID across all rooms plus one.
- **Reason:** IDs are positive and naturally numbered from one; the rule also follows the specified highest-existing-ID calculation when the home is populated.
- **Proving test:** `SmartHomeIdGenerationTest.firstDeviceGetsIdOneAndPresetIdIsOverwritten`.

## Anonymous `Selector` implementation

- **Question:** When should the required anonymous `Selector` implementation be used instead of a lambda?
- **Decision:** Use an anonymous class for at least one `Selector` implementation to make the interface contract and its `select` method explicit; use lambdas or method references for short, direct filtering and sorting operations.
- **Reason:** The assignment requires demonstrating both forms. The anonymous class is educationally useful where naming the implemented operation and its method body is clearer; lambdas remain more concise for simple single-expression behavior.
- **Proving test:** `SmartHomeLookupTest.findSupportsAnonymousSelectorImplementation` passes an anonymous `Selector<Device>` and checks the selected result.

## Rejecting non-finite numeric values

- **Question:** The assignment explicitly rejects negative power usage and out-of-range thermostat targets, but does not specify how NaN or infinity should be handled.
- **Decision:** Reject non-finite device power usage, thermostat range bounds, and thermostat target temperatures with `IllegalArgumentException`.
- **Reason:** NaN and infinity are not meaningful watts or temperatures and could produce misleading sorting, totals, averages, and validation results.
- **Proving tests:** `DeviceNameValidationTest.constructorAndSetterRejectNegativePowerUsage`, `DeviceNameValidationTest.thermostatConstructorRejectsInvalidRanges`, and `DeviceNameValidationTest.thermostatRejectsTargetsOutsideRange`.

## Setting a thermostat target from the console

- **Question:** The supplied menu image does not show a target-temperature action, although the specification requires thermostat target updates and the service exposes that operation. How can users perform the thermostat workflow through the UI?
- **Decision:** Add a menu option to set a thermostat target by device ID, while retaining all existing menu choices and the `0` exit action.
- **Reason:** This makes the specified thermostat operation available to console users without changing existing actions.
- **Proving tests:** `ConsoleUserInterfaceTest.menuCanSetThermostatTargetTemperature` and `ConsoleUserInterfaceTest.invalidThermostatUpdatesReportErrorsAndReturnToMenu`.
