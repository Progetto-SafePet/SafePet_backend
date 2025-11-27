package it.safepet.backend.gestioneCartellaClinica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class PatologiaRequestDTO {

    @NotBlank(message = "Il nome della patologia è obbligatorio")
    @Size(min = 3, max = 20, message = "Il nome deve contenere tra 3 e 25 caratteri")
    private String nome;

    @NotNull(message = "La data della diagnosi è obbligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataDiDiagnosi;

    @NotBlank(message = "I sintomi osservati sono obbligatori")
    @Size(max = 200, message = "I sintomi osservati possono contenere massimo 200 caratteri")
    private String sintomiOsservati;

    @NotBlank(message = "La diagnosi è obbligatoria")
    @Size(max = 200, message = "La diagnosi può contenere massimo 200 caratteri")
    private String diagnosi;

    @NotBlank(message = "La terapia associata è obbligatoria")
    @Size(max = 200, message = "La terapia associata può contenere massimo 200 caratteri")
    private String terapiaAssociata;

    private Long petId;

    public PatologiaRequestDTO() {}

    public PatologiaRequestDTO(String nome, Date dataDiDiagnosi, String sintomiOsservati,
                               String diagnosi, String terapiaAssociata, Long petId) {
        this.nome = nome;
        this.dataDiDiagnosi = dataDiDiagnosi;
        this.sintomiOsservati = sintomiOsservati;
        this.diagnosi = diagnosi;
        this.terapiaAssociata = terapiaAssociata;
        this.petId = petId;
    }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public Date getDataDiDiagnosi() { return dataDiDiagnosi; }

    public void setDataDiDiagnosi(Date dataDiDiagnosi) { this.dataDiDiagnosi = dataDiDiagnosi; }

    public String getSintomiOsservati() { return sintomiOsservati; }

    public void setSintomiOsservati(String sintomiOsservati) { this.sintomiOsservati = sintomiOsservati; }

    public String getDiagnosi() { return diagnosi; }

    public void setDiagnosi(String diagnosi) { this.diagnosi = diagnosi; }

    public String getTerapiaAssociata() { return terapiaAssociata; }

    public void setTerapiaAssociata(String terapiaAssociata) { this.terapiaAssociata = terapiaAssociata; }

    public Long getPetId() { return petId; }

    public void setPetId(Long petId) { this.petId = petId; }
}
