package it.safepet.backend.gestionePet.dto;

import java.util.Base64;

public class PetResponseDTO {
    private Long id;
    private String nome;
    private String razza;
    private String microchip;
    private String sesso;
    private String fotoBase64;

    public PetResponseDTO(Long id, String razza, String nome, String microchip, String sesso, byte[] foto) {
        this.id = id;
        this.razza = razza;
        this.nome = nome;
        this.microchip = microchip;
        this.sesso = sesso;

        if (foto != null && foto.length > 0) {
            this.fotoBase64 = Base64.getEncoder().encodeToString(foto);
        }
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
}
