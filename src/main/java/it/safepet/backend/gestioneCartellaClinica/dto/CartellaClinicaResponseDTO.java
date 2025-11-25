package it.safepet.backend.gestioneCartellaClinica.dto;

import java.util.List;

public class CartellaClinicaResponseDTO {
    private List<VisitaMedicaResponseDTO> visiteMediche;
    private List<PatologiaResponseDTO> patologie;
    private List<TerapiaResponseDTO> terapie;
    private List<VaccinazioneResponseDTO> vaccinazioni;

    public CartellaClinicaResponseDTO() {
    }

    public CartellaClinicaResponseDTO(List<VisitaMedicaResponseDTO> visiteMediche, List<PatologiaResponseDTO> patologie,
                                      List<TerapiaResponseDTO> terapie, List<VaccinazioneResponseDTO> vaccinazioni) {
        this.visiteMediche = visiteMediche;
        this.patologie = patologie;
        this.terapie = terapie;
        this.vaccinazioni = vaccinazioni;
    }

    public List<VisitaMedicaResponseDTO> getVisiteMediche() {
        return visiteMediche;
    }

    public void setVisiteMediche(List<VisitaMedicaResponseDTO> visiteMediche) {
        this.visiteMediche = visiteMediche;
    }

    public List<PatologiaResponseDTO> getPatologie() {
        return patologie;
    }

    public void setPatologie(List<PatologiaResponseDTO> patologie) {
        this.patologie = patologie;
    }

    public List<TerapiaResponseDTO> getTerapie() {
        return terapie;
    }

    public void setTerapie(List<TerapiaResponseDTO> terapie) {
        this.terapie = terapie;
    }

    public List<VaccinazioneResponseDTO> getVaccinazioni() {
        return vaccinazioni;
    }

    public void setVaccinazioni(List<VaccinazioneResponseDTO> vaccinazioni) {
        this.vaccinazioni = vaccinazioni;
    }
}
