package it.safepet.backend.gestioneUtente.dto;

import it.safepet.backend.gestionePet.dto.PetResponseDTO;

import java.time.LocalDate;
import java.util.List;

public class ProfiloProprietarioResponseDTO {
    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String numeroTelefono;
    private LocalDate dataNascita;
    private String genere;
    private String indirizzoDomicilio;
    private List<PetResponseDTO> pets;

    public ProfiloProprietarioResponseDTO() {
    }

    public ProfiloProprietarioResponseDTO(Long id, String nome, String cognome, String email,
                                          String numeroTelefono, LocalDate dataNascita, String genere,
                                          String indirizzoDomicilio, List<PetResponseDTO> pets) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.numeroTelefono = numeroTelefono;
        this.dataNascita = dataNascita;
        this.genere = genere;
        this.indirizzoDomicilio = indirizzoDomicilio;
        this.pets = pets;
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

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getIndirizzoDomicilio() {
        return indirizzoDomicilio;
    }

    public void setIndirizzoDomicilio(String indirizzoDomicilio) {
        this.indirizzoDomicilio = indirizzoDomicilio;
    }

    public List<PetResponseDTO> getPets() {
        return pets;
    }

    public void setPets(List<PetResponseDTO> pets) {
        this.pets = pets;
    }
}
