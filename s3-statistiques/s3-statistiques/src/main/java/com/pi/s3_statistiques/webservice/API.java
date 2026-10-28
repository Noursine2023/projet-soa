package com.pi.s3_statistiques.webservice;


import com.pi.s3_statistiques.entities.Prediction;
import com.pi.s3_statistiques.repo.PredictionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class API {
    @Autowired
    private PredictionRepo predictionRepo;
    @QueryMapping
    public List<Prediction> predictions() {
        return predictionRepo.findAll();
    }

    @QueryMapping
    public List<Prediction> predictionsByPatient(@Argument Long patientId){
        return predictionRepo.findByPatientId(patientId);
    }

    @QueryMapping
    public List<Prediction> predictionsByRiskLevel(@Argument String riskLevel) {
        return predictionRepo.findByRiskLevel(riskLevel);
    }

    @QueryMapping
    public Prediction predictionById(@Argument Long id) {
        return predictionRepo.findById(id).orElse(null);
    }


    @QueryMapping
    public Statistiques statistiques() {
        List<Prediction> all = predictionRepo.findAll();
        int total = all.size();
        int eleves = (int) all.stream()
                .filter(p -> "élevé".equals(p.getRiskLevel()))
                .count();
        double moyenne = all.stream()
                .mapToDouble(Prediction::getScore)
                .average()
                .orElse(0);
        return new Statistiques(total, eleves, Math.round(moyenne * 100.0) / 100.0);
    }

    public record Statistiques(int total, int risquesEleves, double scoreMoyen) {}


}
