package it.safepet.backend.persistence.entity;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "visite_mediche")
public class VisitaMedica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "data", nullable = false)
    private Date data;

    @Lob
    @Column(name = "referto")
    private byte[] referto;

    @ManyToOne
    @JoinColumn(name = "cartella_clinica_id", nullable = false)
    private CartellaClinica cartellaClinica;


    public VisitaMedica() {}

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

    public CartellaClinica getCartellaClinica() {
        return cartellaClinica;
    }

    public void setCartellaClinica(CartellaClinica cartellaClinica) {
        this.cartellaClinica = cartellaClinica;
    }
}