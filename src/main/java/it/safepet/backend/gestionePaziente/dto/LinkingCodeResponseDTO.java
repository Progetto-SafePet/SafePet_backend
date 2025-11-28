package it.safepet.backend.gestionePaziente.dto;

import it.safepet.backend.gestionePaziente.model.LinkingCode;

import java.time.LocalDate;

public class LinkingCodeResponseDTO {
    private Long petId;
    private String nomePet;
    private Long linkingCodeId;
    private String codice;
    private LocalDate dataScadenza;
    private boolean isUsato;

    public static LinkingCodeResponseDTO from (LinkingCode linkingCode) {
        LinkingCodeResponseDTO dto = new LinkingCodeResponseDTO();
        dto.petId = linkingCode.getPet().getId();
        dto.nomePet = linkingCode.getPet().getNome();
        dto.linkingCodeId = linkingCode.getId();
        dto.codice = linkingCode.getCodice();
        dto.dataScadenza = linkingCode.getDataScadenza();
        dto.isUsato = linkingCode.getUsato();
        return dto;
    }

    public Long getPetId() {
        return petId;
    }

    public String getNomePet() {
        return nomePet;
    }

    public Long getLinkingCodeId() {
        return linkingCodeId;
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
}
