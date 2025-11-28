package it.safepet.backend.gestionePaziente.dto;

public class LinkingCodeRequestDTO {
    private final Long petId;

    public LinkingCodeRequestDTO(Long petId) {
        this.petId = petId;
    }

    public Long getPetId() {
        return petId;
    }
}
