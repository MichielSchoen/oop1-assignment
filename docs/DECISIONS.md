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