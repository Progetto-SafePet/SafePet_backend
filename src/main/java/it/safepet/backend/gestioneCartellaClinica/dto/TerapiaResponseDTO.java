package it.safepet.backend.gestioneCartellaClinica.dto;

import it.safepet.backend.gestioneCartellaClinica.model.Terapia;

public class TerapiaResponseDTO {

    private Long terapiaId;
    private String nome;
    private Long petId;
    private Long veterinarioId;

    private String formaFarmaceutica;
    private String dosaggio;
    private String posologia;
    private String viaDiSomministrazione;
    private String durata;
    private String frequenza;
    private String motivo;

    private String nomeVeterinarioCompleto;

    public TerapiaResponseDTO() {
    }

    public TerapiaResponseDTO(Long terapiaId, String nome, Long petId, Long veterinarioId,
                              String formaFarmaceutica, String dosaggio, String posologia, String viaDiSomministrazione,
                              String durata, String frequenza, String motivo, String nomeVeterinarioCompleto) {
        this.terapiaId = terapiaId;
        this.nome = nome;
        this.petId = petId;
        this.veterinarioId = veterinarioId;
        this.formaFarmaceutica = formaFarmaceutica;
        this.dosaggio = dosaggio;
        this.posologia = posologia;
        this.viaDiSomministrazione = viaDiSomministrazione;
        this.durata = durata;
        this.frequenza = frequenza;
        this.motivo = motivo;
        this.nomeVeterinarioCompleto = nomeVeterinarioCompleto;
    }

    public Long getTerapiaId() {
        return terapiaId;
    }

    public void setTerapiaId(Long terapiaId) {
        this.terapiaId = terapiaId;
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

    public Long getVeterinarioId() {
        return veterinarioId;
    }

    public void setVeterinarioId(Long veterinarioId) {
        this.veterinarioId = veterinarioId;
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

    public String getNomeVeterinarioCompleto() {
        return nomeVeterinarioCompleto;
    }

    public void setNomeVeterinarioCompleto(String nomeVeterinarioCompleto) {
        this.nomeVeterinarioCompleto = nomeVeterinarioCompleto;
    }

    public static TerapiaResponseDTO from(Terapia terapia) {
        return new TerapiaResponseDTO(
                terapia.getId(),
                terapia.getNome(),
                terapia.getPet().getId(),
                terapia.getVeterinario().getId(),
                terapia.getFormaFarmaceutica(),
                terapia.getDosaggio(),
                terapia.getPosologia(),
                terapia.getViaDiSomministrazione(),
                terapia.getDurata(),
                terapia.getFrequenza(),
                terapia.getMotivo(),
                terapia.getVeterinario().getNome() + " " + terapia.getVeterinario().getCognome()
        );
    }
}
