package it.safepet.backend.persistence.entity;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "vaccinazioni")
public class Vaccinazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "tipologia", nullable = false)
    private String tipologia;

    @Column(name = "data_di_somministrazione", nullable = false)
    private Date dataDiSomministrazione;

    @Column(name = "dose_somministrata", nullable = false)
    private Float doseSomministrata;

    @Column(name = "via_di_somministrazione", nullable = false)
    private Somministrazione viaDiSomministrazione;

    @Column(name = "effetti_collaterali", nullable = false)
    private String effettiCollaterali;

    @Column(name = "richiamo_previsto", nullable = false)
    private Date richiamoPrevisto;

    @ManyToOne
    @JoinColumn(name = "cartella_clinica_id", nullable = false)
    private CartellaClinica cartellaClinica;

    public enum Somministrazione {
        SOTTOCUTANEA,
        INTRAMUSCOLARE,
        ORALE,
        INTRANASALE,
        TRANSDERMICA;
    }

    public Vaccinazione() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Date getRichiamoPrevisto() {
        return richiamoPrevisto;
    }

    public void setRichiamoPrevisto(Date richiamoPrevisto) {
        this.richiamoPrevisto = richiamoPrevisto;
    }

    public CartellaClinica getCartellaClinica() {
        return cartellaClinica;
    }

    public void setCartellaClinica(CartellaClinica cartellaClinica) {
        this.cartellaClinica = cartellaClinica;
    }
}