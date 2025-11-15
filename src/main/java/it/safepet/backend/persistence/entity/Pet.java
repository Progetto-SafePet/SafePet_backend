package it.safepet.backend.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "sesso", nullable = false, length = 1)
    private String sesso;

    @Column(name = "specie", nullable = false)
    private String specie;

    @Column(name = "razza", nullable = false)
    private String razza;

    @Column(name = "data_nascita", nullable = false)
    private Date dataNascita;

    @Column(name = "peso", nullable = false)
    private Double peso;

    @Column(name = "colore_mantello", nullable = false)
    private String coloreMantello;

    @Column(name = "microchip", nullable = false, unique = true)
    private String microchip;

    @Column(name = "sterilizzato", nullable = false)
    private Boolean isSterilizzato;

    @Lob
    @Column(name = "foto", columnDefinition = "LONGBLOB")
    private byte[] foto;

    @ManyToOne //(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

    @ManyToMany(mappedBy = "petsAssociati")
    private List<Veterinario> veterinariAssociati = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoteProprietario> note = new ArrayList<>();

    @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL)
    private LinkingCode linkingCode;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordMedico> recordMedici = new ArrayList<>();


    public Pet() {
    }

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

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getRazza() {
        return razza;
    }

    public void setRazza(String razza) {
        this.razza = razza;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getColoreMantello() {
        return coloreMantello;
    }

    public void setColoreMantello(String coloreMantello) {
        this.coloreMantello = coloreMantello;
    }

    public String getMicrochip() {
        return microchip;
    }

    public void setMicrochip(String nMicrochip) {
        this.microchip = nMicrochip;
    }

    public Boolean getSterilizzato() {
        return isSterilizzato;
    }

    public void setSterilizzato(Boolean sterilizzato) {
        isSterilizzato = sterilizzato;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public List<Veterinario> getVeterinariAssociati() {
        return veterinariAssociati;
    }

    public void setVeterinariAssociati(List<Veterinario> veterinariAssociati) {
        this.veterinariAssociati = veterinariAssociati;
    }

    public List<NoteProprietario> getNote() {
        return note;
    }

    public void setNote(List<NoteProprietario> note) {
        this.note = note;
    }

    public LinkingCode getLinkingCode() {
        return linkingCode;
    }

    public void setLinkingCode(LinkingCode linkingCode) {
        this.linkingCode = linkingCode;
    }

    public List<RecordMedico> getRecordMedici() {
        return recordMedici;
    }

    public void setRecordMedici(List<RecordMedico> recordMedici) {
        this.recordMedici = recordMedici;
    }
}
