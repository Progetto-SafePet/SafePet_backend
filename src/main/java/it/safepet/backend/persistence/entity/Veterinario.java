package it.safepet.backend.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "veterinari")
public class Veterinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nome", nullable = false, length = 50)
    private String nome;

    @Column(name="cognome", nullable = false, length = 50)
    private String cognome;

    @Column(name="data_nascita", nullable = false)
    private String dataNascita;

    @Column(name="genere", nullable = false, length = 1)
    private String genere; // es: "M" o "F"

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="numero_telefono", nullable = false, length = 10)
    private String numeroTelefono;

    //caricamento documento professionale

    @Column(name="specializzazioni_animali", nullable = false)
    private String specializzazioniAnimali;

    //onetomany per recensioni

    public Veterinario() {
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

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
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
}
