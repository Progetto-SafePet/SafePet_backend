package it.safepet.backend.gestioneCartellaClinica.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "patologie")
@DiscriminatorValue("PATOLOGIA")
public class Patologia extends RecordMedico {
    @Column(name = "data_di_diagnosi", nullable = false)
    private LocalDate dataDiDiagnosi;

    @Column(name = "sintomi_osservati", nullable = false)
    private String sintomiOsservati;

    @Column(name = "diagnosi", nullable = false)
    private String diagnosi;

    @Column(name = "trattamento", nullable = false)
    private String trattamento;

    public Patologia() { }

    public LocalDate getDataDiDiagnosi() {
        return dataDiDiagnosi;
    }

    public void setDataDiDiagnosi(LocalDate dataDiDiagnosi) {
        this.dataDiDiagnosi = dataDiDiagnosi;
    }

    public String getSintomiOsservati() {
        return sintomiOsservati;
    }

    public void setSintomiOsservati(String sintomiOsservati) {
        this.sintomiOsservati = sintomiOsservati;
    }

    public String getDiagnosi() {
        return diagnosi;
    }

    public void setDiagnosi(String diagnosi) {
        this.diagnosi = diagnosi;
    }

    public String getTrattamento() {
        return trattamento;
    }

    public void setTrattamento(String trattamento) {
        this.trattamento = trattamento;
    }
}
