package it.safepet.backend.gestioneCartellaClinica.dto;

import it.safepet.backend.gestioneCartellaClinica.model.Vaccinazione;

import java.util.Date;

public class VaccinazioneResponseDTO {

    private Long vaccinazioneId;
    private String nomeVaccino;
    private Long petId;
    private Long veterinarioId;

    private String tipologia;
    private Date dataDiSomministrazione;
    private Float doseSomministrata;
    private String viaDiSomministrazione;
    private String effettiCollaterali;
    private Date richiamoPrevisto;

    private String nomeVeterinarioCompleto;

    public VaccinazioneResponseDTO() {
    }

    public VaccinazioneResponseDTO(
            Long vaccinazioneId,
            String nomeVaccino,
            Long petId,
            Long veterinarioId,
            String tipologia,
            Date dataDiSomministrazione,
            Float doseSomministrata,
            String viaDiSomministrazione,
            String effettiCollaterali,
            Date richiamoPrevisto,
            String nomeVeterinarioCompleto) {

        this.vaccinazioneId = vaccinazioneId;
        this.nomeVaccino = nomeVaccino;
        this.petId = petId;
        this.veterinarioId = veterinarioId;
        this.tipologia = tipologia;
        this.dataDiSomministrazione = dataDiSomministrazione;
        this.doseSomministrata = doseSomministrata;
        this.viaDiSomministrazione = viaDiSomministrazione;
        this.effettiCollaterali = effettiCollaterali;
        this.richiamoPrevisto = richiamoPrevisto;
        this.nomeVeterinarioCompleto = nomeVeterinarioCompleto;
    }

    public Long getVaccinazioneId() {
        return vaccinazioneId;
    }

    public void setVaccinazioneId(Long vaccinazioneId) {
        this.vaccinazioneId = vaccinazioneId;
    }

    public String getNomeVaccino() {
        return nomeVaccino;
    }

    public void setNomeVaccino(String nomeVaccino) {
        this.nomeVaccino = nomeVaccino;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Long getVeterinarioId() {
        return veterinarioId;
    }

    public void setVeterinarioId(Long veterinarioId) {
        this.veterinarioId = veterinarioId;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public Date getDataDiSomministrazione() {
        return dataDiSomministrazione;
    }

    public void setDataDiSomministrazione(Date dataDiSomministrazione) {
        this.dataDiSomministrazione = dataDiSomministrazione;
    }

    public Float getDoseSomministrata() {
        return doseSomministrata;
    }

    public void setDoseSomministrata(Float doseSomministrata) {
        this.doseSomministrata = doseSomministrata;
    }

    public String getViaDiSomministrazione() {
        return viaDiSomministrazione;
    }

    public void setViaDiSomministrazione(String viaDiSomministrazione) {
        this.viaDiSomministrazione = viaDiSomministrazione;
    }

    public String getEffettiCollaterali() {
        return effettiCollaterali;
    }

    public void setEffettiCollaterali(String effettiCollaterali) {
        this.effettiCollaterali = effettiCollaterali;
    }

    public Date getRichiamoPrevisto() {
        return richiamoPrevisto;
    }

    public void setRichiamoPrevisto(Date richiamoPrevisto) {
        this.richiamoPrevisto = richiamoPrevisto;
    }

    public String getNomeVeterinarioCompleto() {
        return nomeVeterinarioCompleto;
    }

    public void setNomeVeterinarioCompleto(String nomeVeterinarioCompleto) {
        this.nomeVeterinarioCompleto = nomeVeterinarioCompleto;
    }

    public static VaccinazioneResponseDTO from(Vaccinazione vaccinazione) {
        return new VaccinazioneResponseDTO(
                vaccinazione.getId(),
                vaccinazione.getNome(),
                vaccinazione.getPet().getId(),
                vaccinazione.getVeterinario().getId(),
                vaccinazione.getTipologia(),
                vaccinazione.getDataDiSomministrazione(),
                vaccinazione.getDoseSomministrata(),
                vaccinazione.getViaDiSomministrazione().name(),
                vaccinazione.getEffettiCollaterali(),
                vaccinazione.getRichiamoPrevisto(),
                vaccinazione.getVeterinario().getNome() + " " + vaccinazione.getVeterinario().getCognome()
        );
    }
}
