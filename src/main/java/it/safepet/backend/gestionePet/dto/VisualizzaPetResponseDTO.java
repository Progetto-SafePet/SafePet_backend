package it.safepet.backend.gestionePet.dto;

import java.time.LocalDate;
import java.util.Base64;

public class VisualizzaPetResponseDTO {
    private Long id;
    private String nome;
    private String specie;
    private LocalDate dataNascita;
    private String sesso;
    private String fotoBase64;



    public VisualizzaPetResponseDTO(Long id, String nome, String specie, String sesso, byte[] foto, LocalDate dataNascita ) {
        this.id = id;
        this.nome = nome;
        this.specie = specie;
        this.sesso = sesso;
        if (foto != null && foto.length > 0) {
            this.fotoBase64 = Base64.getEncoder().encodeToString(foto);
        }
        this.dataNascita = dataNascita;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSesso() { return sesso; }
    public void setSesso(String sesso) { this.sesso = sesso; }

    public String getFotoBase64() { return fotoBase64; }
    public void setFotoBase64(String fotoBase64) { this.fotoBase64 = fotoBase64; }

    public String getSpecie() { return specie; }
    public void setSpecie(String specie) { this.specie = specie; }

    public LocalDate getDataNascita() { return dataNascita; }
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

}

