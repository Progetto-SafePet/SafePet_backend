package it.safepet.backend.gestioneUtente.dto;

import java.util.Date;

public class VisualizzaDettagliVeterinariResponseDTO {
    private Long id;
    private String nome;
    private String cognome;
    private Date dataNascita;
    private String genere; // es: "M" o "F"
    private String email;
    private String numeroTelefono;
    private String specializzazioniAnimali;
    private Long idClinica;
    private String nomeClinica;
    private String indirizzoClinica;
    private double mediaRecensione;

    public VisualizzaDettagliVeterinariResponseDTO(Long id, String nome, String cognome, Date dataNascita,
                                                   String genere, String email, String numeroTelefono,
                                                   String specializzazioniAnimali, Long idClinica, String nomeClinica,
                                                   String indirizzoClinica, double mediaRecensione) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.genere = genere;
        this.email = email;
        this.numeroTelefono = numeroTelefono;
        this.specializzazioniAnimali = specializzazioniAnimali;
        this.idClinica = idClinica;
        this.nomeClinica = nomeClinica;
        this.indirizzoClinica = indirizzoClinica;
        this.mediaRecensione = mediaRecensione;
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

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Long getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(Long idClinica) {
        this.idClinica = idClinica;
    }

    public String getNomeClinica() {
        return nomeClinica;
    }

    public void setNomeClinica(String nomeClinica) {
        this.nomeClinica = nomeClinica;
    }

    public String getIndirizzoClinica() {
        return indirizzoClinica;
    }

    public void setIndirizzoClinica(String indirizzoClinica) {
        this.indirizzoClinica = indirizzoClinica;
    }

    public double getMediaRecensione() {
        return mediaRecensione;
    }

    public void setMediaRecensione(double mediaRecensione) {
        this.mediaRecensione = mediaRecensione;
    }
}
