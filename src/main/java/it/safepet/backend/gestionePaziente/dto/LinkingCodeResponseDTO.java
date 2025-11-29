package it.safepet.backend.gestionePaziente.dto;

import it.safepet.backend.gestionePaziente.model.LinkingCode;

import java.time.LocalDate;

public class LinkingCodeResponseDTO {
    private String nomePet;
    private String codice;
    private LocalDate dataScadenza;
    private boolean isScaduto;
    private boolean isUsato;

    public static LinkingCodeResponseDTO from (LinkingCode linkingCode) {
        LinkingCodeResponseDTO dto = new LinkingCodeResponseDTO();
        dto.nomePet = linkingCode.getPet().getNome();
        dto.codice = linkingCode.getCodice();
        dto.dataScadenza = linkingCode.getDataScadenza();
        dto.isUsato = linkingCode.getUsato();
        dto.isScaduto = linkingCode.isScaduto();
        return dto;
    }

    public String getNomePet() {
        return nomePet;
    }

    public String getCodice() {
        return codice;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public boolean isUsato() {
        return isUsato;
    }

    public boolean isScaduto() {
        return isScaduto;
    }
}
