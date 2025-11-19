package it.safepet.backend.gestioneRecensioni.model;

import it.safepet.backend.gestioneUtente.model.Proprietario;
import it.safepet.backend.gestioneUtente.model.Veterinario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recensioni")
public class Recensione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "punteggio", nullable = false)
    private Integer punteggio;

    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @ManyToOne
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;


    public Recensione() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(Integer punteggio) {
        this.punteggio = punteggio;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }
}

