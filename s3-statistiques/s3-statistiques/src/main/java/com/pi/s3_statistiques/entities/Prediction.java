package com.pi.s3_statistiques.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity

public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "patient_id")
    private Long patientId;

    private Double score ;
    @Column(name = "risk_level")
    private String riskLevel;

    @Column(name = "date_prediction")
    private LocalDateTime datePrediction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public LocalDateTime getDatePrediction() {
        return datePrediction;
    }

    public void setDatePrediction(LocalDateTime datePrediction) {
        this.datePrediction = datePrediction;
    }
}
