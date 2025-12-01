package it.safepet.backend.reportCliniche.dto;

import java.util.List;

public record InfoClinicheDTO(Long clinicaId, String nomeClinica, String indirizzo, String numeroTelefono,
                              Long vetId, String nomeVeterinario, String cognomeVeterinario, int numRecensioni, Double mediaRecensioni,
                              Double latitudine, Double longitudine, List<OrariClinicaResponseDTO> orariDiApertura) {
}
