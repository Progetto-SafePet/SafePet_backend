package it.safepet.backend.gestionePet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class NewPetDTO {
    @NotBlank(message = "Il nome del pet è obbligatorio")
    @Size(min = 3, max = 20, message = "Il nome deve contenere dai 3 caratteri ai 20 caratteri")
    private String nome;

    @NotBlank(message = "Il sesso del pet è obbligatorio")
    @Pattern(
            regexp = "^( M | F )$",
            message = "Il sesso deve essere 'MASCHIO' o 'FEMMINA'"
    )
    private final String sesso;

    @NotBlank(message = "La specie del pet è obbligatoria")
    private final String specie;

    @Size(min = 3, max = 30, message = "La razza deve contenere dai 3 caratteri ai 30 caratteri")
    private final String razza;

    @NotNull(message = "La data di nascita del pet è obbligatoria")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate dataNascita;

    @DecimalMin("0.1")
    @DecimalMax("100.0")
    private final Double peso;

    @Size(min = 3, max = 15, message = "Il colore del mantello deve contenere dai 3 caratteri ai 15 caratteri")
    private final String coloreMantello;

    @Size(min = 15, max = 15, message = "Il microchip deve contenere 15 cifre numeriche")
    @Pattern(regexp = "\\d{15}", message = "Il microchip deve contenere solo cifre numeriche")
    private final String microchip;

    private final Boolean isSterilizzato;

    private final MultipartFile foto;

    public NewPetDTO(String nome, String sesso, String specie, String razza, LocalDate dataNascita, Double peso, String
                coloreMantello, String microchip, Boolean isSterilizzato, MultipartFile foto) {
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

    public String getSpecie() {
        return specie;
    }

    public String getRazza() {
        return razza;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public Double getPeso() {
        return peso;
    }

    public String getColoreMantello() {
        return coloreMantello;
    }

    public String getMicrochip() {
        return microchip;
    }

    public Boolean getSterilizzato() {
        return isSterilizzato;
    }

    public MultipartFile getFoto() {
        return foto;
    }

}
