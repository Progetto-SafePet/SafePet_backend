package it.safepet.backend.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "linkingcodes")
public class LinkingCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice", nullable = false, unique = true)
    private String codice;

    @Column(name = "data_scadenza", nullable = false)
    private LocalDate dataScadenza;

    @Column(name = "usato", nullable = false)
    private Boolean isUsato;
}
