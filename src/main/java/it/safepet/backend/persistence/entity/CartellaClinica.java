package it.safepet.backend.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cartelle_cliniche")
public class CartellaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public CartellaClinica() {
    }
}
