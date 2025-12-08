package it.safepet.backend.gestioneCartellaClinica.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class VaccinazioneRequestDTO {

    @NotBlank(message = "Il nome del vaccino è obbligatorio")
    @Size(min = 3, max = 20, message = "Il nome del vaccino deve contenere tra 3 e 20 caratteri")
    private String nomeVaccino;

    @NotBlank(message = "La tipologia è obbligatoria")
    @Size(min = 3, max = 20, message = "La tipologia deve contenere tra 3 e 20 caratteri")
    private String tipologia;

    @NotNull(message = "La data di somministrazione è obbligatoria")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataDiSomministrazione;

    @NotNull(message = "La dose somministrata è obbligatoria")
    @DecimalMin(value = "0.1", message = "La dose minima è 0.1 ml")
    @DecimalMax(value = "10.0", message = "La dose massima è 10 ml")
    private Float doseSomministrata;

    @NotBlank(message = "La via di somministrazione è obbligatoria")
    @Pattern(
            regexp = "SOTTOCUTANEA|INTRAMUSCOLARE|ORALE|INTRANASALE|TRANSDERMICA",
            message = "La via di somministrazione non è valida"
    )
    private String viaDiSomministrazione;

    @Size(max = 200, message = "Gli effetti collaterali possono contenere massimo 200 caratteri")
    private String effettiCollaterali;

    @NotNull(message = "Il richiamo previsto è obbligatorio")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate richiamoPrevisto;

    public VaccinazioneRequestDTO() {
    }

    public VaccinazioneRequestDTO(
            String nomeVaccino,
            String tipologia,
            LocalDate dataDiSomministrazione,
            Float doseSomministrata,
            String viaDiSomministrazione,
            String effettiCollaterali,
            LocalDate richiamoPrevisto) {

        this.nomeVaccino = nomeVaccino;
        this.tipologia = tipologia;
        this.dataDiSomministrazione = dataDiSomministrazione;
        this.doseSomministrata = doseSomministrata;
        this.viaDiSomministrazione = viaDiSomministrazione;
        this.effettiCollaterali = effettiCollaterali;
        this.richiamoPrevisto = richiamoPrevisto;
    }


    public @NotNull(message = "La data di somministrazione è obbligatoria") LocalDate getDataDiSomministrazione() {
        return dataDiSomministrazione;
    }

    public @NotNull(message = "La dose somministrata è obbligatoria")
        @DecimalMin(value = "0.1", inclusive = true, message = "La dose minima è 0.1 ml")
        @DecimalMax(value = "10.0", inclusive = true, message = "La dose massima è 10 ml")
        Float getDoseSomministrata() {
        return doseSomministrata;
    }

    public @NotBlank(message = "La via di somministrazione è obbligatoria") @Pattern(
            regexp = "SOTTOCUTANEA|INTRAMUSCOLARE|ORALE|INTRANASALE|TRANSDERMICA",
            message = "La via di somministrazione non è valida"
    ) String getViaDiSomministrazione() {
        return viaDiSomministrazione;
    }

    public @Size(min = 1, max = 200, message = "Gli effetti collaterali possono contenere massimo 200 caratteri e minimo 1") String getEffettiCollaterali() {
        return effettiCollaterali;
    }

    public @NotNull(message = "Il richiamo previsto è obbligatorio") LocalDate getRichiamoPrevisto() {
        return richiamoPrevisto;
    }

    public @NotBlank(message = "La tipologia è obbligatoria")
        @Size(min = 3, max = 20, message = "La tipologia deve contenere tra 3 e 20 caratteri")
        String getTipologia() {
        return tipologia;
    }

    public @NotBlank(message = "Il nome del vaccino è obbligatorio")
        @Size(min = 3, max = 20, message = "Il nome del vaccino deve contenere tra 3 e 20 caratteri")
        String getNomeVaccino() {
        return nomeVaccino;
    }


    public void setNomeVaccino(@NotBlank(message = "Il nome del vaccino è obbligatorio")
                               @Size(min = 3, max = 20, message = "Il nome del vaccino deve contenere tra 3 e 20 caratteri")
                               String nomeVaccino) {
        this.nomeVaccino = nomeVaccino;
    }

    public void setTipologia(@NotBlank(message = "La tipologia è obbligatoria")
                             @Size(min = 3, max = 20, message = "La tipologia deve contenere tra 3 e 20 caratteri")
                             String tipologia) {
        this.tipologia = tipologia;
    }

    public void setDataDiSomministrazione(@NotNull(message = "La data di somministrazione è obbligatoria") LocalDate dataDiSomministrazione) {
        this.dataDiSomministrazione = dataDiSomministrazione;
    }

    public void setDoseSomministrata(@NotNull(message = "La dose somministrata è obbligatoria")
                                     @DecimalMin(value = "0.1", inclusive = true, message = "La dose minima è 0.1 ml")
                                     @DecimalMax(value = "10.0", inclusive = true, message = "La dose massima è 10 ml")
                                     Float doseSomministrata) {
        this.doseSomministrata = doseSomministrata;
    }

    public void setViaDiSomministrazione(@NotBlank(message = "La via di somministrazione è obbligatoria") @Pattern(
            regexp = "SOTTOCUTANEA|INTRAMUSCOLARE|ORALE|INTRANASALE|TRANSDERMICA",
            message = "La via di somministrazione non è valida"
    ) String viaDiSomministrazione) {
        this.viaDiSomministrazione = viaDiSomministrazione;
    }

    public void setEffettiCollaterali(
            @Size(max = 200, message = "Gli effetti collaterali possono contenere massimo 200 caratteri")
            String effettiCollaterali) {
        this.effettiCollaterali = effettiCollaterali;
    }

    public void setRichiamoPrevisto(@NotNull(message = "Il richiamo previsto è obbligatorio") LocalDate richiamoPrevisto) {
        this.richiamoPrevisto = richiamoPrevisto;
    }
}
