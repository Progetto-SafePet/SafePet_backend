package it.safepet.backend.gestioneCondivisioneDati.dto;

import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;

public class CondivisioneDatiPetResponseDTO {
    private String nomeProprietario;
    private String cognomeProprietario;
    private String emailProprietario;
    private String telefonoProprietario;
    private String indirizzoProprietario;

    private PetResponseDTO pet;
    private CartellaClinicaResponseDTO cartellaClinica;

    public CondivisioneDatiPetResponseDTO() {
    }

    public CondivisioneDatiPetResponseDTO(String cognomeProprietario, String nomeProprietario, String emailProprietario, String telefonoProprietario, String indirizzoProprietario, PetResponseDTO pet, CartellaClinicaResponseDTO cartellaClinica) {
        this.cognomeProprietario = cognomeProprietario;
        this.nomeProprietario = nomeProprietario;
        this.emailProprietario = emailProprietario;
        this.telefonoProprietario = telefonoProprietario;
        this.indirizzoProprietario = indirizzoProprietario;
        this.pet = pet;
        this.cartellaClinica = cartellaClinica;
    }

    public String getNomeProprietario() {
        return nomeProprietario;
    }

    public void setNomeProprietario(String nomeProprietario) {
        this.nomeProprietario = nomeProprietario;
    }

    public String getCognomeProprietario() {
        return cognomeProprietario;
    }

    public void setCognomeProprietario(String cognomeProprietario) {
        this.cognomeProprietario = cognomeProprietario;
    }

    public String getEmailProprietario() {
        return emailProprietario;
    }

    public void setEmailProprietario(String emailProprietario) {
        this.emailProprietario = emailProprietario;
    }

    public String getTelefonoProprietario() {
        return telefonoProprietario;
    }

    public void setTelefonoProprietario(String telefonoProprietario) {
        this.telefonoProprietario = telefonoProprietario;
    }

    public String getIndirizzoProprietario() {
        return indirizzoProprietario;
    }

    public void setIndirizzoProprietario(String indirizzoProprietario) {
        this.indirizzoProprietario = indirizzoProprietario;
    }

    public PetResponseDTO getPet() {
        return pet;
    }

    public void setPet(PetResponseDTO pet) {
        this.pet = pet;
    }

    public CartellaClinicaResponseDTO getCartellaClinica() {
        return cartellaClinica;
    }

    public void setCartellaClinica(CartellaClinicaResponseDTO cartellaClinica) {
        this.cartellaClinica = cartellaClinica;
    }
}