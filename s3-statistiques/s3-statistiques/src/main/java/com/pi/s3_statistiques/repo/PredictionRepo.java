package com.pi.s3_statistiques.repo;

import com.pi.s3_statistiques.entities.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PredictionRepo extends JpaRepository<Prediction, Long> {
    List<Prediction> findByRiskLevel(String riskLevel);
    List<Prediction> findByPatientId(Long patientId);
}
