package it.safepet.backend.persistence.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String specie;

    @Column(nullable = false)
    private Date data_nascita;

    @Column(nullable = false)
    private String razza;

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    private String colore_mantello;

    @Column(name="microchip", unique = true, nullable = false)
    private String microchip;

    @Column(name="sesso", nullable = false, length = 1)
    private String sesso;

    @Column(nullable = false)
    private Boolean sterilizzato;

    @Lob
    @Column(name = "foto", columnDefinition = "LONGBLOB")
    private byte[] foto;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "owner_id")
//    private User owner;

    public Pet() {
    }

    public Long getId() {
        return id;
    }

}
