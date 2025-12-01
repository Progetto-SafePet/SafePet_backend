package it.safepet.backend.gestioneUtente.dto;

public class ProprietarioResponseDTO {

    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private String indirizzo;

    public ProprietarioResponseDTO() {
    }

    public ProprietarioResponseDTO(String nome, String cognome, String email, String telefono, String indirizzo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.indirizzo = indirizzo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }
}
