package com.fleet.repository;

import com.fleet.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    List<Vehicle> findByStatus(String status);

    List<Vehicle> findByVehicleType(String vehicleType);

    boolean existsByRegistrationNumber(String registrationNumber);

    @Query("SELECT v FROM Vehicle v WHERE v.status = 'ACTIVE' ORDER BY v.createdAt DESC")
    List<Vehicle> findAllActiveVehicles();

    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.status = :status")
    Long countByStatus(String status);
}
