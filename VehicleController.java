package com.fleet.controller;

import com.fleet.model.Vehicle;
import com.fleet.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // POST /api/vehicles — Register a new vehicle
    @PostMapping
    public ResponseEntity<Vehicle> registerVehicle(@Valid @RequestBody Vehicle vehicle) {
        Vehicle saved = vehicleService.registerVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // GET /api/vehicles — Get all vehicles
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    // GET /api/vehicles/{id} — Get vehicle by ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    // GET /api/vehicles/reg/{regNumber} — Get vehicle by registration number
    @GetMapping("/reg/{regNumber}")
    public ResponseEntity<Vehicle> getVehicleByRegNumber(@PathVariable String regNumber) {
        return ResponseEntity.ok(vehicleService.getVehicleByRegNumber(regNumber));
    }

    // GET /api/vehicles/status/{status} — Filter by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Vehicle>> getVehiclesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(vehicleService.getVehiclesByStatus(status));
    }

    // PUT /api/vehicles/{id} — Update vehicle details
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id,
                                                  @Valid @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicle));
    }

    // DELETE /api/vehicles/{id} — Soft delete (mark INACTIVE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(Map.of("message", "Vehicle marked as INACTIVE successfully."));
    }

    // GET /api/vehicles/summary — Fleet status summary
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getFleetStatusSummary() {
        return ResponseEntity.ok(vehicleService.getFleetStatusSummary());
    }
}
