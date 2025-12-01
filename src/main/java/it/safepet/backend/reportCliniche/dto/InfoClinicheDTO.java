package it.safepet.backend.reportCliniche.dto;

import it.safepet.backend.reportCliniche.model.OrarioDiApertura;

import java.util.List;

public record InfoClinicheDTO(Long clinicaId, String nomeClinica, String indirizzo, String numeroTelefono,
                              Long vetId, String nomeVeterinario, String cognomeVeterinario, Double recensioni,
                              Double latitudine, Double longitudine, List<OrarioDiApertura> orariDiAperura) {
}
