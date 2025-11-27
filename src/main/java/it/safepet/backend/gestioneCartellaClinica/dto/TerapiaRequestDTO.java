package it.safepet.backend.gestioneCartellaClinica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TerapiaRequestDTO {
    @NotBlank(message = "Il nome della terapia è obbligatorio")
    @Size(min = 3, max = 100, message = "Il nome deve contenere tra 3 e 100 caratteri")
    private String nome;

    private Long petId;

    @NotBlank(message= "La forma farmaceutica è obbligatoria")
    @Size(min= 2, max= 50, message = "La forma farmaceutica deve contenere tra 2 e 50 caratteri")
    private String formaFarmaceutica;

    @NotBlank(message= "Il dosaggio è obbligatorio")
    @Size(min= 1, max= 20, message = "Il dosaggio deve contenere tra 1 e 20 caratteri")
    private String dosaggio;

    @NotBlank(message= "La posologia è obbligatoria")
    @Size(min= 5, max= 100, message = "La posologia deve contenere tra 5 e 100 caratteri")
    private String posologia;

    @NotBlank(message= "La via di somministrazione è obbligatoria")
    @Size(min= 2, max= 50, message = "La via di somministrazione deve contenere tra 2 e 50 caratteri")
    private String viaDiSomministrazione;

    @NotBlank(message= "La durata è obbligatoria")
    @Size(min= 1, max= 10, message = "La durata deve contenere tra 1 e 10 caratteri")
    private String durata;

    @NotBlank(message= "La frequenza è obbligatoria")
    @Size(min= 3, max= 50, message = "La frequenza deve contenere tra 3 e 50 caratteri")
    private String frequenza;

    @NotBlank(message= "Il motivo è obbligatorio")
    @Size(min= 5, max= 160, message = "Il motivo deve contenere tra 5 e 160 caratteri")
    private String motivo;

    public TerapiaRequestDTO() {
    }

    public TerapiaRequestDTO(String nome, Long petId, String formaFarmaceutica, String dosaggio, String posologia, String viaDiSomministrazione, String durata, String frequenza, String motivo) {
        this.nome = nome;
        this.petId = petId;
        this.formaFarmaceutica = formaFarmaceutica;
        this.dosaggio = dosaggio;
        this.posologia = posologia;
        this.viaDiSomministrazione = viaDiSomministrazione;
        this.durata = durata;
        this.frequenza = frequenza;
        this.motivo = motivo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public String getDosaggio() {
        return dosaggio;
    }

    public void setDosaggio(String dosaggio) {
        this.dosaggio = dosaggio;
    }

    public String getPosologia() {
        return posologia;
    }

    public void setPosologia(String posologia) {
        this.posologia = posologia;
    }

    public String getViaDiSomministrazione() {
        return viaDiSomministrazione;
    }

    public void setViaDiSomministrazione(String viaDiSomministrazione) {
        this.viaDiSomministrazione = viaDiSomministrazione;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getFrequenza() {
        return frequenza;
    }

    public void setFrequenza(String frequenza) {
        this.frequenza = frequenza;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
