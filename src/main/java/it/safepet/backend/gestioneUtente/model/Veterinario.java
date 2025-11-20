package it.safepet.backend.gestioneUtente.model;

import it.safepet.backend.reportVeterinariECliniche.model.Clinica;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestioneRecensioni.model.Recensione;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "veterinari")
public class Veterinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "cognome", nullable = false, length = 50)
    private String cognome;

    @Column(name = "data_nascita", nullable = false)
    private Date dataNascita;

    @Column(name = "genere", nullable = false, length = 1)
    private String genere; // es: "M" o "F"

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "numero_telefono", nullable = false, length = 10)
    private String numeroTelefono;

    @Column(name = "specializzazioni_animali", nullable = false)
    private String specializzazioniAnimali;

    @OneToOne
    @JoinColumn(name = "clinica_id", unique = true)
    private Clinica clinica;

    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL)
    private List<Recensione> recensioni = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "veterinario_pet",
            joinColumns = @JoinColumn(name = "veterinario_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id")
    )
    private List<Pet> petsAssociati = new ArrayList<>();


    public Veterinario() {

    }

    public Veterinario(String nome, String cognome, Date dataNascita, String genere, String email, String password,
                       String numeroTelefono, String specializzazioniAnimali) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.genere = genere;
        this.email = email;
        this.password = password;
        this.numeroTelefono = numeroTelefono;
        this.specializzazioniAnimali = specializzazioniAnimali;
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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getSpecializzazioniAnimali() {
        return specializzazioniAnimali;
    }

    public void setSpecializzazioniAnimali(String specializzazioniAnimali) {
        this.specializzazioniAnimali = specializzazioniAnimali;
    }

    public Clinica getClinica() {
        return clinica;
    }

    public void setClinica(Clinica clinica) {
        this.clinica = clinica;
    }

    public List<Recensione> getRecensioni() {
        return recensioni;
    }

    public void setRecensioni(List<Recensione> recensioni) {
        this.recensioni = recensioni;
    }

    public List<Pet> getPetsAssociati() {
        return petsAssociati;
    }

    public void setPetsAssociati(List<Pet> petsAssociati) {
        this.petsAssociati = petsAssociati;
    }
}
