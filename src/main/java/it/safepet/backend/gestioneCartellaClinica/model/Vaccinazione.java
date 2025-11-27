package it.safepet.backend.gestioneCartellaClinica.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "vaccinazioni")
@DiscriminatorValue("VACCINAZIONE")
public class Vaccinazione extends RecordMedico {
    @Column(name = "tipologia", nullable = false)
    private String tipologia;

    @Column(name = "data_di_somministrazione", nullable = false)
    private LocalDate dataDiSomministrazione;

    @Column(name = "dose_somministrata", nullable = false)
    private Float doseSomministrata;

    @Column(name = "via_di_somministrazione", nullable = false)
    private Somministrazione viaDiSomministrazione;

    @Column(name = "effetti_collaterali", nullable = false)
    private String effettiCollaterali;

    @Column(name = "richiamo_previsto", nullable = false)
    private LocalDate richiamoPrevisto;

    public enum Somministrazione {
        SOTTOCUTANEA,
        INTRAMUSCOLARE,
        ORALE,
        INTRANASALE,
        TRANSDERMICA;
    }

    public Vaccinazione() {

    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public LocalDate getDataDiSomministrazione() {
        return dataDiSomministrazione;
    }

    public void setDataDiSomministrazione(LocalDate dataDiSomministrazione) {
        this.dataDiSomministrazione = dataDiSomministrazione;
    }

    public Float getDoseSomministrata() {
        return doseSomministrata;
    }

    public void setDoseSomministrata(Float doseSomministrata) {
        this.doseSomministrata = doseSomministrata;
    }

    public Somministrazione getViaDiSomministrazione() {
        return viaDiSomministrazione;
    }

    public void setViaDiSomministrazione(Somministrazione viaDiSomministrazione) {
        this.viaDiSomministrazione = viaDiSomministrazione;
    }

    public String getEffettiCollaterali() {
        return effettiCollaterali;
    }

    public void setEffettiCollaterali(String effettiCollaterali) {
        this.effettiCollaterali = effettiCollaterali;
    }

    public LocalDate getRichiamoPrevisto() {
        return richiamoPrevisto;
    }

    public void setRichiamoPrevisto(LocalDate richiamoPrevisto) {
        this.richiamoPrevisto = richiamoPrevisto;
    }
}
