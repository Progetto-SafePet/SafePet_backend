package it.safepet.backend.gestionePaziente.dto;

import java.util.Base64;
import java.util.Date;

public class PazienteResponseDTO {
    private String nome;
    private String specie;
    private Date dataNascita;
    private String proprietario;
    private String sesso;
    private String fotoBase64;

    public PazienteResponseDTO(String specie, String nome, Date dataNascita, String proprietario, String sesso, byte[] foto) {
        this.specie = specie;
        this.nome = nome;
        this.dataNascita = dataNascita;
        this.proprietario = proprietario;
        this.sesso = sesso;

        if (foto != null && foto.length > 0) {
            this.fotoBase64 = Base64.getEncoder().encodeToString(foto);
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public Date getDataNascita(){
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    public String getSesso() {
        return sesso;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }


}
