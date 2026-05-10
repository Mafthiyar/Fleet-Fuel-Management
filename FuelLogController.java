package com.fleet.controller;

import com.fleet.model.FuelLog;
import com.fleet.service.FuelLogService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FuelLogController {

    private final FuelLogService fuelLogService;

    public FuelLogController(FuelLogService fuelLogService) {
        this.fuelLogService = fuelLogService;
    }

    // POST /api/vehicles/{vehicleId}/fuel-logs — Add fuel log for a vehicle
    @PostMapping("/vehicles/{vehicleId}/fuel-logs")
    public ResponseEntity<FuelLog> addFuelLog(@PathVariable Long vehicleId,
                                               @Valid @RequestBody FuelLog fuelLog) {
        FuelLog saved = fuelLogService.addFuelLog(vehicleId, fuelLog);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET /api/vehicles/{vehicleId}/fuel-logs — Get all logs for a vehicle
    @GetMapping("/vehicles/{vehicleId}/fuel-logs")
    public ResponseEntity<List<FuelLog>> getFuelLogsByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(fuelLogService.getLogsByVehicle(vehicleId));
    }

    // GET /api/fuel-logs/{id} — Get a specific log
    @GetMapping("/fuel-logs/{id}")
    public ResponseEntity<FuelLog> getFuelLogById(@PathVariable Long id) {
        return ResponseEntity.ok(fuelLogService.getLogById(id));
    }

    // GET /api/vehicles/{vehicleId}/performance — Performance summary for a vehicle
    @GetMapping("/vehicles/{vehicleId}/performance")
    public ResponseEntity<Map<String, Object>> getVehiclePerformance(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(fuelLogService.getVehiclePerformanceSummary(vehicleId));
    }

    // GET /api/reports/fleet?from=2024-01-01&to=2024-12-31 — Fleet-wide report
    @GetMapping("/reports/fleet")
    public ResponseEntity<Map<String, Object>> getFleetReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(fuelLogService.getFleetReport(from, to));
    }

    // DELETE /api/fuel-logs/{id} — Delete a fuel log
    @DeleteMapping("/fuel-logs/{id}")
    public ResponseEntity<Map<String, String>> deleteFuelLog(@PathVariable Long id) {
        fuelLogService.deleteLog(id);
        return ResponseEntity.ok(Map.of("message", "Fuel log deleted successfully."));
    }
}
