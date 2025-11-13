package it.safepet.backend.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lista_preferiti")
public class ListaPreferiti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

}
