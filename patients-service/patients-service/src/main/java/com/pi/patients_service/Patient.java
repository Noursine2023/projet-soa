package com.pi.patients_service;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity

public class Patient {
    @Id
    private Integer id;
    private String nom , sex;
    private Integer age;
    @Column(name = "ChestPainType")
    private String chestPainType;
    @Column(name = "RestingBP")
    private Integer restingBP;

    @Column(name = "RestingECG")
    private String restingECG;

    @Column(name = "FastingBS")
    private Integer fastingBS;
    @Column(name = "MaxHR")
    private Integer maxHR;

    @Column(name = "ExerciseAngina")
    private String exerciseAngina;

    @Column(name = "Oldpeak")
    private Float oldpeak;

    @Column(name = "ST_Slope")
    private String stSlope;

    @Column(name = "HeartDisease")
    private Integer heartDisease;
    @Column(name = "medecin_id")
    private Integer medecinId;

    private Integer cholesterol ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getChestPainType() {
        return chestPainType;
    }

    public void setChestPainType(String chestPainType) {
        this.chestPainType = chestPainType;
    }

    public Integer getRestingBP() {
        return restingBP;
    }

    public void setRestingBP(Integer restingBP) {
        this.restingBP = restingBP;
    }

    public String getRestingECG() {
        return restingECG;
    }

    public void setRestingECG(String restingECG) {
        this.restingECG = restingECG;
    }

    public Integer getFastingBS() {
        return fastingBS;
    }

    public void setFastingBS(Integer fastingBS) {
        this.fastingBS = fastingBS;
    }

    public Integer getMaxHR() {
        return maxHR;
    }

    public void setMaxHR(Integer maxHR) {
        this.maxHR = maxHR;
    }

    public String getExerciseAngina() {
        return exerciseAngina;
    }

    public void setExerciseAngina(String exerciseAngina) {
        this.exerciseAngina = exerciseAngina;
    }

    public Float getOldpeak() {
        return oldpeak;
    }

    public void setOldpeak(Float oldpeak) {
        this.oldpeak = oldpeak;
    }

    public String getStSlope() {
        return stSlope;
    }

    public void setStSlope(String stSlope) {
        this.stSlope = stSlope;
    }

    public Integer getHeartDisease() {
        return heartDisease;
    }

    public void setHeartDisease(Integer heartDisease) {
        this.heartDisease = heartDisease;
    }

    public Integer getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(Integer medecinId) {
        this.medecinId = medecinId;
    }

    public Integer getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Integer cholesterol) {
        this.cholesterol = cholesterol;
    }
}
