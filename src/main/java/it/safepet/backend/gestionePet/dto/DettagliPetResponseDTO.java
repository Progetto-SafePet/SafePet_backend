package it.safepet.backend.gestionePet.dto;

import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;
import java.util.List;

public class DettagliPetResponseDTO {
    private PetResponseDTO anagraficaDTO;
    private CartellaClinicaResponseDTO cartellaClinicaDTO;
    private List<InserimentoNoteResponseDTO> noteProprietarioDTO;

    public DettagliPetResponseDTO() {
    }

    public DettagliPetResponseDTO(PetResponseDTO anagraficaDTO, CartellaClinicaResponseDTO cartellaClinicaDTO,
                                  List<InserimentoNoteResponseDTO> noteProprietarioDTO) {
        this.anagraficaDTO = anagraficaDTO;
        this.cartellaClinicaDTO = cartellaClinicaDTO;
        this.noteProprietarioDTO = noteProprietarioDTO;
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

    public List<InserimentoNoteResponseDTO> getNoteProprietarioDTO() {
        return noteProprietarioDTO;
    }

    public void setNoteProprietarioDTO(List<InserimentoNoteResponseDTO> noteProprietarioDTO) {
        this.noteProprietarioDTO = noteProprietarioDTO;
    }
}
