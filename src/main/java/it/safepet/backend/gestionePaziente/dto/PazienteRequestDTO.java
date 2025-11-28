package it.safepet.backend.gestionePaziente.dto;

public class PazienteRequestDTO {
    private final String linkingCode;

    public PazienteRequestDTO(String linkingCode) {
        this.linkingCode = linkingCode;
    }

    public String getLinkingCode() {
        return linkingCode;
    }
}
