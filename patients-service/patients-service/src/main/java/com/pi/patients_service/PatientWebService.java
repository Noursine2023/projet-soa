package com.pi.patients_service;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Path("/service")
public class PatientWebService {
    @Autowired
    private PatientRepository patientRepository;

    @GET
    @Path("/patients")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    @GET
    @Path("/patients/{id}")
    @Produces (MediaType.APPLICATION_JSON)
    public Patient getPatientById(@PathParam("id") Integer id){
        return patientRepository.findById(id).orElseThrow(() -> new NotFoundException("Patient introuvelle :" + id));

    }

    @GET
    @Path("/patients-sex")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Patient> getPatientBySex(@QueryParam("sex") String sex){
        return patientRepository.findBySex(sex);
    }

    @GET
    @Path("/patients-medecin")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Patient> getPatientsByMedecin(@QueryParam("medecinId") Integer medecinId) {
        return patientRepository.findByMedecinId(medecinId);
    }

    @GET
    @Path("/patients-heart")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Patient> getPatientsByHeartDisease(@QueryParam("heartDisease") Integer heartDisease) {
        return patientRepository.findByHeartDisease(heartDisease);
    }

    @GET
    @Path("/patients-age")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Patient> getPatientsByAgeMin(@QueryParam("ageMin") Integer ageMin) {
        return patientRepository.findByAgeGreaterThanEqual(ageMin);
    }

    @GET
    @Path("/patients-seniors-heart")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Patient> getSeniorsWithHeartDisease() {
        return patientRepository.findByAgeGreaterThanAndHeartDisease(60, 1);
    }

}
