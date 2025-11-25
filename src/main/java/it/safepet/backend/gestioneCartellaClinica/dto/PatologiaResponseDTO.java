package it.safepet.backend.gestioneCartellaClinica.dto;

import java.util.Date;

public class PatologiaResponseDTO {
    private Long patologiaId;
    private String nome; // nuovo campo
    private Date dataDiDiagnosi;
    private String sintomiOsservati;
    private String diagnosi;
    private String terapiaAssociata;
    private Long petId;
    private Long veterinarioId;

    public PatologiaResponseDTO(Long patologiaId, String nome, Date dataDiDiagnosi, String sintomiOsservati,
                                String diagnosi, String terapiaAssociata, Long petId, Long veterinarioId) {
        this.patologiaId = patologiaId;
        this.nome = nome;
        this.dataDiDiagnosi = dataDiDiagnosi;
        this.sintomiOsservati = sintomiOsservati;
        this.diagnosi = diagnosi;
        this.terapiaAssociata = terapiaAssociata;
        this.petId = petId;
        this.veterinarioId = veterinarioId;
    }

    public Long getPatologiaId() { return patologiaId; }
    public void setPatologiaId(Long patologiaId) { this.patologiaId = patologiaId; }

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

    public Long getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Long veterinarioId) { this.veterinarioId = veterinarioId; }
}
