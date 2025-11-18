package it.safepet.backend.ReportVeterinariECliniche.model;

import it.safepet.backend.gestioneUtente.model.Veterinario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliniche")
public class Clinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "indirizzo", nullable = false)
    private String indirizzo;

    @Column(name = "numero_telefono", nullable = false, length = 10)
    private String numeroTelefono;

    @Column(name = "latitudine", nullable = false)
    private Double latitudine;

    @Column(name = "longitudine", nullable = false)
    private Double longitudine;

    @OneToOne(mappedBy = "clinica", cascade = CascadeType.ALL)
    private Veterinario veterinario;

    @OneToMany(mappedBy = "clinica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrarioDiApertura> orariApertura = new ArrayList<>();

    public Clinica() {
    }

    public Clinica(String nome, String indirizzo, String numeroTelefono, Double latitudine, Double longitudine) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.numeroTelefono = numeroTelefono;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public List<OrarioDiApertura> getOrariApertura() {
        return orariApertura;
    }

    public void setOrariApertura(List<OrarioDiApertura> orariApertura) {
        this.orariApertura = orariApertura;
    }
}
