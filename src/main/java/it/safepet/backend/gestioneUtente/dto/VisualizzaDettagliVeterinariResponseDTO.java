package it.safepet.backend.gestioneUtente.dto;

import it.safepet.backend.gestioneRecensioni.dto.RecensioneResponseDTO;
import it.safepet.backend.reportCliniche.dto.OrariClinicaResponseDTO;

import java.util.Date;
import java.util.List;

public class VisualizzaDettagliVeterinariResponseDTO {
    private Long idVeterinario;
    private String nomeVeterinario;
    private String cognome;
    private Date dataNascita;
    private String genere; // es: "M" o "F"
    private String email;
    private String numeroTelefonoVeterinario;
    private String specializzazioniAnimali;

    private Long idClinica;
    private String nomeClinica; //inserisci tutti i dettagli della clinica
    private String indirizzoClinica; //aggiungere Lista di recensioni
    private String numeroTelefonoClinica;
    private Double latitudineClinica;
    private Double longitudineClinica;
    private List<OrariClinicaResponseDTO> orarioDiAperturaClinica;

    private List<RecensioneResponseDTO> listaRecensioniVeterinario;
    private Double mediaRecensione;

    public VisualizzaDettagliVeterinariResponseDTO(Long idVeterinario, String nomeVeterinario, String cognome,
                                                   Date dataNascita, String genere, String email,
                                                   String numeroTelefonoVeterinario, String specializzazioniAnimali,
                                                   Long idClinica, String nomeClinica, String indirizzoClinica,
                                                   String numeroTelefonoClinica, Double latitudineClinica,
                                                   Double longitudineClinica,
                                                   List<OrariClinicaResponseDTO> orarioDiAperturaClinica,
                                                   List<RecensioneResponseDTO> listaRecensioniVeterinario,
                                                   Double mediaRecensione) {
        this.idVeterinario = idVeterinario;
        this.nomeVeterinario = nomeVeterinario;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.genere = genere;
        this.email = email;
        this.numeroTelefonoVeterinario = numeroTelefonoVeterinario;
        this.specializzazioniAnimali = specializzazioniAnimali;
        this.idClinica = idClinica;
        this.nomeClinica = nomeClinica;
        this.indirizzoClinica = indirizzoClinica;
        this.numeroTelefonoClinica = numeroTelefonoClinica;
        this.latitudineClinica = latitudineClinica;
        this.longitudineClinica = longitudineClinica;
        this.orarioDiAperturaClinica = orarioDiAperturaClinica;
        this.listaRecensioniVeterinario = listaRecensioniVeterinario;
        this.mediaRecensione = mediaRecensione;
    }

    public Long getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(Long idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public String getNomeVeterinario() {
        return nomeVeterinario;
    }

    public void setNomeVeterinario(String nomeVeterinario) {
        this.nomeVeterinario = nomeVeterinario;
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

    public String getNumeroTelefonoVeterinario() {
        return numeroTelefonoVeterinario;
    }

    public void setNumeroTelefonoVeterinario(String numeroTelefonoVeterinario) {
        this.numeroTelefonoVeterinario = numeroTelefonoVeterinario;
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

    public String getNumeroTelefonoClinica() {
        return numeroTelefonoClinica;
    }

    public void setNumeroTelefonoClinica(String numeroTelefonoClinica) {
        this.numeroTelefonoClinica = numeroTelefonoClinica;
    }

    public Double getLatitudineClinica() {
        return latitudineClinica;
    }

    public void setLatitudineClinica(Double latitudineClinica) {
        this.latitudineClinica = latitudineClinica;
    }

    public Double getLongitudineClinica() {
        return longitudineClinica;
    }

    public void setLongitudineClinica(Double longitudineClinica) {
        this.longitudineClinica = longitudineClinica;
    }

    public List<OrariClinicaResponseDTO> getOrarioDiAperturaClinica() {
        return orarioDiAperturaClinica;
    }

    public void setOrarioDiAperturaClinica(List<OrariClinicaResponseDTO> orarioDiAperturaClinica) {
        this.orarioDiAperturaClinica = orarioDiAperturaClinica;
    }

    public List<RecensioneResponseDTO> getListaRecensioniVeterinario() {
        return listaRecensioniVeterinario;
    }

    public void setListaRecensioniVeterinario(List<RecensioneResponseDTO> listaRecensioniVeterinario) {
        this.listaRecensioniVeterinario = listaRecensioniVeterinario;
    }

    public Double getMediaRecensione() {
        return mediaRecensione;
    }

    public void setMediaRecensione(Double mediaRecensione) {
        this.mediaRecensione = mediaRecensione;
    }
}
