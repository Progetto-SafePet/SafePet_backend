package it.safepet.backend.gestioneCartellaClinica.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "patologie")
@DiscriminatorValue("PATOLOGIA")
public class Patologia extends RecordMedico {
    @Column(name = "data_di_diagnosi", nullable = false)
    private Date dataDiDiagnosi;

    @Column(name = "sintomi_osservati", nullable = false)
    private String sintomiOsservati;

    @Column(name = "diagnosi", nullable = false)
    private String diagnosi;

    @Column(name = "terapia_associata", nullable = false)
    private String terapiaAssociata;

    public Patologia() { }

    public Date getDataDiDiagnosi() {
        return dataDiDiagnosi;
    }

    public void setDataDiDiagnosi(Date dataDiDiagnosi) {
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

    public String getTerapiaAssociata() {
        return terapiaAssociata;
    }

    public void setTerapiaAssociata(String terapiaAssociata) {
        this.terapiaAssociata = terapiaAssociata;
    }
}
