package it.safepet.backend.gestionePet.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class NewPetDTO {
    @NotBlank
    private String nome;
    private String sesso;
    private String specie;
    private String razza;
    private Date dataNascita;
    private Double peso;
    private String coloreMantello;
    private String microchip;
    private Boolean isSterilizzato;
    private MultipartFile foto;

    public NewPetDTO(String nome, String sesso, String specie, String razza, Date dataNascita, Double peso, String coloreMantello, String microchip, Boolean isSterilizzato, MultipartFile foto) {
        this.nome = nome;
        this.sesso = sesso;
        this.specie = specie;
        this.razza = razza;
        this.dataNascita = dataNascita;
        this.peso = peso;
        this.coloreMantello = coloreMantello;
        this.microchip = microchip;
        this.isSterilizzato = isSterilizzato;
        this.foto = foto;
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

    public String getRazza() {
        return razza;
    }

    public void setRazza(String razza) {
        this.razza = razza;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
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

    public void setMicrochip(String microchip) {
        this.microchip = microchip;
    }

    public Boolean getSterilizzato() {
        return isSterilizzato;
    }

    public void setSterilizzato(Boolean sterilizzato) {
        isSterilizzato = sterilizzato;
    }

    public MultipartFile getFoto() {
        return foto;
    }

    public void setFoto(MultipartFile foto) {
        this.foto = foto;
    }
}
