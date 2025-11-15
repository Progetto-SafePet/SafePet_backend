package it.safepet.backend.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "visite_mediche")
@DiscriminatorValue("VISITA_MEDICA")
public class VisitaMedica extends RecordMedico {
    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "data", nullable = false)
    private Date data;

    @Lob
    @Column(name = "referto")
    private byte[] referto;

    public VisitaMedica() {

    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public byte[] getReferto() {
        return referto;
    }

    public void setReferto(byte[] referto) {
        this.referto = referto;
    }
}