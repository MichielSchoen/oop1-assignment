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
- **Proving test:** Add a `SmartHome` filtering test that passes an anonymous `Selector<Device>` and verifies the returned matching devices when the UML `Selector` API is implemented.

## UML API comparison

The UML diagram is the required public API. These core signatures currently match: `SmartHome()`; `addRoom(Room)`; `getRooms()`; `addDeviceToRoom(String, Device)`; `removeDevice(int)`; `getAllDevices()`; `findByType(Class<T>)`; `getById(int)`; `setTargetTemperature(int, double)`; `sortedByPowerUsage()`; `stats()`; `Room(String)`, `getName()`, `getDevices()`, and `addDevice(Device)`; `Device`'s name/id accessors; and the device type/description methods.

The following UML requirements are missing or differ and must be reconciled before the project is complete:

| UML requirement | Current difference |
|---|---|
| `Device` is abstract, implements `Describable`, and uses `double` power usage and `setPowerUsage(double)` | `Device` is concrete, does not implement `Describable`, and uses `int` power usage and `setPowerUsage(int)` |
| `LightDevice(String, double)` | Constructor currently accepts `int` power usage |
| `ThermostatDevice(String, double, String)`; `getMinimumTemperature()`, `getMaximumTemperature()`, `getTargetTemperature()`, `setTargetTemperature(double)`, `getType()`, and `describe()` | Constructor currently accepts `int` power usage; minimum/maximum getters are missing; extra `getRange()` and `setRange(String)` methods are present |
| `Room.removeDevice(int)` | Current method accepts a `Device` |
| `SmartHome.removeRoom(String)` returns `void` | Current method returns `boolean` |
| `SmartHome.find(Selector<Device>)` and the functional `Selector<T>.select(T)` interface | Current method accepts `Predicate<Device>`; `Selector<T>` is not present |
| `SmartHome.totalPowerUsage()` returns `double` | Current method returns `int` |
| `HomeStats(Collection<Device>)` is public | Constructor is private and accepts `List<Device>` |
| Service API: `SmartHomeService` methods for room/device listing, lookup, add/remove, target temperature, power filtering/sorting/total, and home stats; `SmartHomeServiceImpl(SmartHome)` | `SmartHomeService` and `SmartHomeServiceImpl` are not present |
| `ConsoleUserInterface(SmartHomeService)` and `start()`; `Application.main(String[])` | `ConsoleUserInterface` and `Application` are not present |

These are API mismatches, not additional design choices. Preserve the UML signatures when reconciling them; keep any extra public methods only when justified by a documented decision.