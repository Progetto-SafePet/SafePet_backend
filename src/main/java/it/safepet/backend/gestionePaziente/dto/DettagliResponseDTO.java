package it.safepet.backend.gestionePaziente.dto;

import it.safepet.backend.gestioneCartellaClinica.dto.CartellaClinicaResponseDTO;
import it.safepet.backend.gestionePet.dto.InserimentoNoteResponseDTO;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

public class DettagliResponseDTO {

    // =========================
    //     ANAGRAFICA PET
    // =========================
    private Long id;
    private String nome;
    private String sesso;
    private String specie;
    private String razza;
    private LocalDate dataNascita;
    private Double peso;
    private String coloreMantello;
    private String microchip;
    private Boolean sterilizzato;
    private String proprietarioCompleto;
    private String fotoBase64;

    // =========================
    //     CARTELLA CLINICA
    // =========================
    private CartellaClinicaResponseDTO cartellaClinica;

    // =========================
    //     NOTE PROPRIETARIO
    // =========================
    private List<InserimentoNoteResponseDTO> noteProprietario;


    // ======== COSTRUTTORE COMPLETO ========
    public DettagliResponseDTO(Long id,
                                       String nome,
                                       String sesso,
                                       String specie,
                                       String razza,
                                       LocalDate dataNascita,
                                       Double peso,
                                       String coloreMantello,
                                       String microchip,
                                       Boolean sterilizzato,
                                       String proprietarioCompleto,
                                       byte[] foto,
                                       CartellaClinicaResponseDTO cartellaClinica,
                                       List<InserimentoNoteResponseDTO> noteProprietario) {

        this.id = id;
        this.nome = nome;
        this.sesso = sesso;
        this.specie = specie;
        this.razza = razza;
        this.dataNascita = dataNascita;
        this.peso = peso;
        this.coloreMantello = coloreMantello;
        this.microchip = microchip;
        this.sterilizzato = sterilizzato;
        this.proprietarioCompleto = proprietarioCompleto;
        this.cartellaClinica = cartellaClinica;
        this.noteProprietario = noteProprietario;

        if (foto != null && foto.length > 0) {
            this.fotoBase64 = Base64.getEncoder().encodeToString(foto);
        }
    }


    // ======== GETTER & SETTER ========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSesso() { return sesso; }
    public void setSesso(String sesso) { this.sesso = sesso; }

    public String getSpecie() { return specie; }
    public void setSpecie(String specie) { this.specie = specie; }

    public String getRazza() { return razza; }
    public void setRazza(String razza) { this.razza = razza; }

    public LocalDate getDataNascita() { return dataNascita; }
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public String getColoreMantello() { return coloreMantello; }
    public void setColoreMantello(String coloreMantello) { this.coloreMantello = coloreMantello; }

    public String getMicrochip() { return microchip; }
    public void setMicrochip(String microchip) { this.microchip = microchip; }

    public Boolean getSterilizzato() { return sterilizzato; }
    public void setSterilizzato(Boolean sterilizzato) { this.sterilizzato = sterilizzato; }

    public String getProprietarioCompleto() { return proprietarioCompleto; }
    public void setProprietarioCompleto(String proprietarioCompleto) { this.proprietarioCompleto = proprietarioCompleto; }

    public String getFotoBase64() { return fotoBase64; }
    public void setFotoBase64(String fotoBase64) { this.fotoBase64 = fotoBase64; }

    public CartellaClinicaResponseDTO getCartellaClinica() { return cartellaClinica; }
    public void setCartellaClinica(CartellaClinicaResponseDTO cartellaClinica) { this.cartellaClinica = cartellaClinica; }

    public List<InserimentoNoteResponseDTO> getNoteProprietario() { return noteProprietario; }
    public void setNoteProprietario(List<InserimentoNoteResponseDTO> noteProprietario) { this.noteProprietario = noteProprietario; }
}
