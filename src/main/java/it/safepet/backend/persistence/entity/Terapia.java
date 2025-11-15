package it.safepet.backend.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "terapie")
@DiscriminatorValue("TERAPIA")
public class Terapia extends RecordMedico {
    @Column(name = "forma_farmaceutica", nullable = false)
    private String formaFarmaceutica;

    @Column(name = "dosaggio", nullable = false)
    private String dosaggio;

    @Column(name = "posologia", nullable = false)
    private String posologia;

    @Column(name = "via_di_somministrazione", nullable = false)
    private String viaDiSomministrazione;

    @Column(name = "durata", nullable = false)
    private String durata;

    @Column(name = "frequenza", nullable = false)
    private String frequenza;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    public Terapia() {

    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public String getDosaggio() {
        return dosaggio;
    }

    public void setDosaggio(String dosaggio) {
        this.dosaggio = dosaggio;
    }

    public String getPosologia() {
        return posologia;
    }

    public void setPosologia(String posologia) {
        this.posologia = posologia;
    }

    public String getViaDiSomministrazione() {
        return viaDiSomministrazione;
    }

    public void setViaDiSomministrazione(String viaDiSomministrazione) {
        this.viaDiSomministrazione = viaDiSomministrazione;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getFrequenza() {
        return frequenza;
    }

    public void setFrequenza(String frequenza) {
        this.frequenza = frequenza;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
