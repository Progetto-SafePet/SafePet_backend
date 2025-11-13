package it.safepet.backend.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "oraridiapertura")
public class OrarioDiApertura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Enumerated(EnumType.STRING)
    @Column(name = "giorno", nullable = false)
    private Giorno giorno;

    @Column(name = "orario_apertura", nullable = false)
    private LocalTime orarioApertura;

    @Column(name = "orario_chiusura", nullable = false)
    private LocalTime orarioChiusura;

    public enum Giorno {
        LUNEDÌ,
        MARTEDÌ,
        MERCOLEDÌ,
        GIOVEDÌ,
        VENERDÌ,
        SABATO,
        DOMENICA
    }

    public OrarioDiApertura() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
