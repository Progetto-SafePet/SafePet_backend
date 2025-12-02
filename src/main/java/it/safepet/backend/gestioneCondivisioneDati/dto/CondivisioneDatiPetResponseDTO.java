package it.safepet.backend.gestioneCondivisioneDati.dto;

import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;
import it.safepet.backend.gestioneUtente.dto.ProprietarioResponseDTO;

public class CondivisioneDatiPetResponseDTO {
    private ProprietarioResponseDTO proprietario;
    private PetResponseDTO pet;
    private CartellaClinicaResponseDTO cartellaClinica;

    public CondivisioneDatiPetResponseDTO() {
    }

    public CondivisioneDatiPetResponseDTO(ProprietarioResponseDTO proprietario, PetResponseDTO pet, CartellaClinicaResponseDTO cartellaClinica) {
        this.proprietario = proprietario;
        this.pet = pet;
        this.cartellaClinica = cartellaClinica;
    }

    public ProprietarioResponseDTO getProprietario() {
        return proprietario;
    }

    public void setProprietario(ProprietarioResponseDTO proprietario) {
        this.proprietario = proprietario;
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
