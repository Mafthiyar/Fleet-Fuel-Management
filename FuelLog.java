package com.fleet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fuel_logs")
public class FuelLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @NotNull(message = "Fuel date is required")
    private LocalDate fuelDate;

    @NotNull(message = "Litres filled is required")
    @Positive(message = "Litres filled must be positive")
    private Double litresFilled;

    @NotNull(message = "Cost per litre is required")
    @Positive(message = "Cost per litre must be positive")
    private Double costPerLitre;

    private Double totalCost; // auto-calculated

    @NotNull(message = "Odometer reading is required")
    @PositiveOrZero
    private Double odometerReading; // km at time of refuel

    private Double distanceSinceLastFill; // km since last refuel

    private Double fuelEfficiency; // km per litre

    private String fuelStation;

    private String notes;

    @Column(updatable = false)
    private LocalDateTime loggedAt;

    @PrePersist
    public void prePersist() {
        loggedAt = LocalDateTime.now();
        if (litresFilled != null && costPerLitre != null) {
            totalCost = litresFilled * costPerLitre;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }

    public LocalDate getFuelDate() { return fuelDate; }
    public void setFuelDate(LocalDate fuelDate) { this.fuelDate = fuelDate; }

    public Double getLitresFilled() { return litresFilled; }
    public void setLitresFilled(Double litresFilled) {
        this.litresFilled = litresFilled;
        if (litresFilled != null && costPerLitre != null) totalCost = litresFilled * costPerLitre;
    }

    public Double getCostPerLitre() { return costPerLitre; }
    public void setCostPerLitre(Double costPerLitre) {
        this.costPerLitre = costPerLitre;
        if (litresFilled != null && costPerLitre != null) totalCost = litresFilled * costPerLitre;
    }

    public Double getTotalCost() { return totalCost; }

    public Double getOdometerReading() { return odometerReading; }
    public void setOdometerReading(Double odometerReading) { this.odometerReading = odometerReading; }

    public Double getDistanceSinceLastFill() { return distanceSinceLastFill; }
    public void setDistanceSinceLastFill(Double distanceSinceLastFill) { this.distanceSinceLastFill = distanceSinceLastFill; }

    public Double getFuelEfficiency() { return fuelEfficiency; }
    public void setFuelEfficiency(Double fuelEfficiency) { this.fuelEfficiency = fuelEfficiency; }

    public String getFuelStation() { return fuelStation; }
    public void setFuelStation(String fuelStation) { this.fuelStation = fuelStation; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
}
