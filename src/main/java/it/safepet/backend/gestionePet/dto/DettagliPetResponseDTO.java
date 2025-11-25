package it.safepet.backend.gestionePet.dto;

import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;

public class DettagliPetResponseDTO {
    private PetResponseDTO anagraficaDTO;
    private CartellaClinicaResponseDTO cartellaClinicaDTO;
//    private InserimentoNoteResponseDTO noteProprietarioDTO;

    public DettagliPetResponseDTO() {
    }

    //momentaneamente senza note per test
    public DettagliPetResponseDTO(PetResponseDTO anagraficaDTO, CartellaClinicaResponseDTO cartellaClinicaDTO) {
        this.anagraficaDTO = anagraficaDTO;
        this.cartellaClinicaDTO = cartellaClinicaDTO;
    }

    //    public DettagliPetResponseDTO(PetResponseDTO anagraficaDTO, CartellaClinicaResponseDTO cartellaClinicaDTO,
//                                  InserimentoNoteResponseDTO noteProprietarioDTO) {
//        this.anagraficaDTO = anagraficaDTO;
//        this.cartellaClinicaDTO = cartellaClinicaDTO;
//        this.noteProprietarioDTO = noteProprietarioDTO;
//    }

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

//    public InserimentoNoteResponseDTO getNoteProprietarioDTO() {
//        return noteProprietarioDTO;
//    }
//
//    public void setNoteProprietarioDTO(InserimentoNoteResponseDTO noteProprietarioDTO) {
//        this.noteProprietarioDTO = noteProprietarioDTO;
//    }
}
