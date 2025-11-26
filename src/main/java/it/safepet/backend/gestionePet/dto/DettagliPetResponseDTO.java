package it.safepet.backend.gestionePet.dto;

import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;

public class DettagliPetResponseDTO {
    private PetResponseDTO anagraficaDTO;
    private CartellaClinicaResponseDTO cartellaClinicaDTO;

    public DettagliPetResponseDTO() {
    }

    public DettagliPetResponseDTO(PetResponseDTO anagraficaDTO, CartellaClinicaResponseDTO cartellaClinicaDTO) {
        this.anagraficaDTO = anagraficaDTO;
        this.cartellaClinicaDTO = cartellaClinicaDTO;
    }

    public PetResponseDTO getAnagraficaDTO() {
        return anagraficaDTO;
    }

    public void setAnagraficaDTO(PetResponseDTO anagraficaDTO) {
        this.anagraficaDTO = anagraficaDTO;
    }

    public CartellaClinicaResponseDTO getCartellaClinicaDTO() {
        return cartellaClinicaDTO;
    }

    public void setCartellaClinicaDTO(CartellaClinicaResponseDTO cartellaClinicaDTO) {
        this.cartellaClinicaDTO = cartellaClinicaDTO;
    }
}
