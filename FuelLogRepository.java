package com.fleet.repository;

import com.fleet.model.FuelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FuelLogRepository extends JpaRepository<FuelLog, Long> {

    List<FuelLog> findByVehicleIdOrderByFuelDateDesc(Long vehicleId);

    List<FuelLog> findByFuelDateBetweenOrderByFuelDateDesc(LocalDate from, LocalDate to);

    @Query("SELECT SUM(f.litresFilled) FROM FuelLog f WHERE f.vehicle.id = :vehicleId")
    Double getTotalLitresByVehicle(@Param("vehicleId") Long vehicleId);

    @Query("SELECT SUM(f.totalCost) FROM FuelLog f WHERE f.vehicle.id = :vehicleId")
    Double getTotalCostByVehicle(@Param("vehicleId") Long vehicleId);

    @Query("SELECT AVG(f.fuelEfficiency) FROM FuelLog f WHERE f.vehicle.id = :vehicleId AND f.fuelEfficiency IS NOT NULL")
    Double getAvgEfficiencyByVehicle(@Param("vehicleId") Long vehicleId);

    @Query("SELECT SUM(f.totalCost) FROM FuelLog f WHERE f.fuelDate BETWEEN :from AND :to")
    Double getTotalCostBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("SELECT f FROM FuelLog f WHERE f.vehicle.id = :vehicleId ORDER BY f.odometerReading DESC")
    List<FuelLog> findLatestByVehicleId(@Param("vehicleId") Long vehicleId);

    @Query(value = """
        SELECT v.registration_number, v.driver_name,
               SUM(f.litres_filled) AS total_litres,
               SUM(f.total_cost) AS total_cost,
               AVG(f.fuel_efficiency) AS avg_efficiency
        FROM fuel_logs f
        JOIN vehicles v ON f.vehicle_id = v.id
        WHERE f.fuel_date BETWEEN :from AND :to
        GROUP BY v.id, v.registration_number, v.driver_name
        ORDER BY total_cost DESC
        """, nativeQuery = true)
    List<Object[]> getFleetSummaryReport(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
