package it.safepet.backend.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "note_propeitari")
public class NoteProprietario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titolo", nullable = false)
    private String titolo;

    @Column(name = "descrizione", nullable = false)
    private String descrizione;
}
