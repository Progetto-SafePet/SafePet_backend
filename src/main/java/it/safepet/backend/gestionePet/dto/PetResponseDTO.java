package it.safepet.backend.gestionePet.dto;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;

public class PetResponseDTO {
    private Long id;
    private String nome;
    private String specie;
    private LocalDate dataNascita;
    private Double peso;
    private String coloreMantello;
    private Boolean isSterilizzato;
    private String razza;
    private String microchip;
    private String sesso;
    private String fotoBase64;

    public PetResponseDTO(Long id, String nome, String specie, LocalDate dataNascita, Double peso, String coloreMantello, Boolean isSterilizzato, String razza, String microchip, String sesso, byte[] foto) {
        this.id = id;
        this.nome = nome;
        this.specie = specie;
        this.dataNascita = dataNascita;
        this.peso = peso;
        this.coloreMantello = coloreMantello;
        this.isSterilizzato = isSterilizzato;
        this.razza = razza;
        this.microchip = microchip;
        this.sesso = sesso;
        if (foto != null && foto.length > 0) {
            this.fotoBase64 = Base64.getEncoder().encodeToString(foto);
        }
    }

    public PetResponseDTO(String nome, String specie, String sesso, byte[] foto, Double peso) {
        this.nome = nome;
        this.specie = specie;
        this.sesso = sesso;
        if (foto != null && foto.length > 0) {
            this.fotoBase64 = Base64.getEncoder().encodeToString(foto);
        }
        this.peso = peso;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRazza() { return razza; }
    public void setRazza(String razza) { this.razza = razza; }

    public String getMicrochip() { return microchip; }
    public void setMicrochip(String microchip) { this.microchip = microchip; }

    public String getSesso() { return sesso; }
    public void setSesso(String sesso) { this.sesso = sesso; }

    public String getFotoBase64() { return fotoBase64; }
    public void setFotoBase64(String fotoBase64) { this.fotoBase64 = fotoBase64; }

    public String getSpecie() { return specie; }

    public void setSpecie(String specie) { this.specie = specie; }

    public LocalDate getDataNascita() { return dataNascita; }

    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

    public Double getPeso() { return peso; }

    public void setPeso(Double peso) { this.peso = peso; }

    public String getColoreMantello() { return coloreMantello; }

    public void setColoreMantello(String coloreMantello) { this.coloreMantello = coloreMantello; }

    public Boolean getSterilizzato() { return isSterilizzato; }

    public void setSterilizzato(Boolean sterilizzato) { isSterilizzato = sterilizzato; }
}
