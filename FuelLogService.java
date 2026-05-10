package com.fleet.service;

import com.fleet.exception.ResourceNotFoundException;
import com.fleet.model.FuelLog;
import com.fleet.model.Vehicle;
import com.fleet.repository.FuelLogRepository;
import com.fleet.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FuelLogService {

    private final FuelLogRepository fuelLogRepository;
    private final VehicleRepository vehicleRepository;

    public FuelLogService(FuelLogRepository fuelLogRepository, VehicleRepository vehicleRepository) {
        this.fuelLogRepository = fuelLogRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public FuelLog addFuelLog(Long vehicleId, FuelLog fuelLog) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

        if (!"ACTIVE".equals(vehicle.getStatus())) {
            throw new IllegalArgumentException("Cannot log fuel for an inactive or maintenance vehicle.");
        }

        fuelLog.setVehicle(vehicle);

        // Auto-calculate fuel efficiency if previous log exists
        List<FuelLog> prevLogs = fuelLogRepository.findLatestByVehicleId(vehicleId);
        if (!prevLogs.isEmpty()) {
            FuelLog lastLog = prevLogs.get(0);
            double distance = fuelLog.getOdometerReading() - lastLog.getOdometerReading();
            if (distance > 0) {
                fuelLog.setDistanceSinceLastFill(distance);
                fuelLog.setFuelEfficiency(distance / fuelLog.getLitresFilled());
            }
        }

        return fuelLogRepository.save(fuelLog);
    }

    @Transactional(readOnly = true)
    public List<FuelLog> getLogsByVehicle(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + vehicleId);
        }
        return fuelLogRepository.findByVehicleIdOrderByFuelDateDesc(vehicleId);
    }

    @Transactional(readOnly = true)
    public FuelLog getLogById(Long id) {
        return fuelLogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fuel log not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getVehiclePerformanceSummary(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + vehicleId);
        }
        Map<String, Object> summary = new HashMap<>();
        summary.put("vehicleId", vehicleId);
        summary.put("totalLitresFilled", fuelLogRepository.getTotalLitresByVehicle(vehicleId));
        summary.put("totalCostSpent", fuelLogRepository.getTotalCostByVehicle(vehicleId));
        summary.put("averageFuelEfficiency", fuelLogRepository.getAvgEfficiencyByVehicle(vehicleId));
        return summary;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getFleetReport(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' date must be before 'to' date.");
        }
        Map<String, Object> report = new HashMap<>();
        report.put("from", from);
        report.put("to", to);
        report.put("totalFleetCost", fuelLogRepository.getTotalCostBetweenDates(from, to));
        report.put("vehicleBreakdown", fuelLogRepository.getFleetSummaryReport(from, to));
        return report;
    }

    public void deleteLog(Long id) {
        FuelLog log = getLogById(id);
        fuelLogRepository.delete(log);
    }
}
