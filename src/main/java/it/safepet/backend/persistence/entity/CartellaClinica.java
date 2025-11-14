package it.safepet.backend.persistence.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cartelle_cliniche")
public class CartellaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @OneToMany(mappedBy = "cartellaClinica", cascade = CascadeType.ALL)
    private List<Terapia> terapie = new ArrayList<>();

    @OneToMany(mappedBy = "cartellaClinica", cascade = CascadeType.ALL)
    private List<Vaccinazione> vaccinazioni = new ArrayList<>();

    @OneToMany(mappedBy = "cartellaClinica", cascade = CascadeType.ALL)
    private List<VisitaMedica> visiteMediche = new ArrayList<>();

    @OneToMany(mappedBy = "cartellaClinica", cascade = CascadeType.ALL)
    private List<Patologia> patologie = new ArrayList<>();

    public CartellaClinica() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public List<Terapia> getTerapie() {
        return terapie;
    }

    public void setTerapie(List<Terapia> terapie) {
        this.terapie = terapie;
    }

    public List<Vaccinazione> getVaccinazioni() {
        return vaccinazioni;
    }

    public void setVaccinazioni(List<Vaccinazione> vaccinazioni) {
        this.vaccinazioni = vaccinazioni;
    }

    public List<VisitaMedica> getVisiteMediche() {
        return visiteMediche;
    }

    public void setVisiteMediche(List<VisitaMedica> visiteMediche) {
        this.visiteMediche = visiteMediche;
    }

    public List<Patologia> getPatologie() {
        return patologie;
    }

    public void setPatologie(List<Patologia> patologie) {
        this.patologie = patologie;
    }
}
