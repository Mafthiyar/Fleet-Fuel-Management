package com.fleet.service;

import com.fleet.exception.ResourceNotFoundException;
import com.fleet.model.Vehicle;
import com.fleet.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle registerVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsByRegistrationNumber(vehicle.getRegistrationNumber())) {
            throw new IllegalArgumentException(
                "Vehicle with registration number '" + vehicle.getRegistrationNumber() + "' already exists."
            );
        }
        return vehicleRepository.save(vehicle);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Vehicle getVehicleByRegNumber(String regNumber) {
        return vehicleRepository.findByRegistrationNumber(regNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with registration: " + regNumber));
    }

    @Transactional(readOnly = true)
    public List<Vehicle> getVehiclesByStatus(String status) {
        return vehicleRepository.findByStatus(status.toUpperCase());
    }

    public Vehicle updateVehicle(Long id, Vehicle updated) {
        Vehicle existing = getVehicleById(id);
        existing.setDriverName(updated.getDriverName());
        existing.setVehicleType(updated.getVehicleType());
        existing.setTankCapacityLitres(updated.getTankCapacityLitres());
        existing.setStatus(updated.getStatus());
        return vehicleRepository.save(existing);
    }

    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setStatus("INACTIVE");
        vehicleRepository.save(vehicle); // soft delete
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getFleetStatusSummary() {
        Map<String, Long> summary = new java.util.HashMap<>();
        summary.put("ACTIVE", vehicleRepository.countByStatus("ACTIVE"));
        summary.put("INACTIVE", vehicleRepository.countByStatus("INACTIVE"));
        summary.put("MAINTENANCE", vehicleRepository.countByStatus("MAINTENANCE"));
        return summary;
    }
}
