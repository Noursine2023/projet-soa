package com.pi.patients_service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient , Integer> {
    List<Patient> findBySex(String sex);

    List<Patient> findByMedecinId(Integer medecinId);

    List<Patient> findByHeartDisease(Integer heartDisease);

    List<Patient> findByAgeGreaterThanEqual(Integer age);

    List<Patient> findByAgeGreaterThanAndHeartDisease(Integer age, Integer heartDisease);
}