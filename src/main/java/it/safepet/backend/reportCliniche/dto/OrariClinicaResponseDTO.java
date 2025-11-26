package it.safepet.backend.reportCliniche.dto;

import it.safepet.backend.reportCliniche.model.OrarioDiApertura.Giorno;
import java.time.LocalTime;

public class OrariClinicaResponseDTO {
    private Giorno giorno;
    private LocalTime orarioApertura;
    private LocalTime orarioChiusura;
    private Boolean aperto24h;

    public OrariClinicaResponseDTO(Giorno giorno, LocalTime orarioApertura,
                                   LocalTime orarioChiusura, Boolean aperto24h) {
        this.giorno = giorno;
        this.orarioApertura = orarioApertura;
        this.orarioChiusura = orarioChiusura;
        this.aperto24h = aperto24h;
    }

    public Giorno getGiorno() {
        return giorno;
    }

    public void setGiorno(Giorno giorno) {
        this.giorno = giorno;
    }

    public LocalTime getOrarioApertura() {
        return orarioApertura;
    }

    public void setOrarioApertura(LocalTime orarioApertura) {
        this.orarioApertura = orarioApertura;
    }

    public LocalTime getOrarioChiusura() {
        return orarioChiusura;
    }

    public void setOrarioChiusura(LocalTime orarioChiusura) {
        this.orarioChiusura = orarioChiusura;
    }

    public Boolean getAperto24h() {
        return aperto24h;
    }

    public void setAperto24h(Boolean aperto24h) {
        this.aperto24h = aperto24h;
    }
}
