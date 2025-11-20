package it.safepet.backend.autenticazione.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class RegistrazioneProprietarioRequestDTO {
    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 2, max = 50, message = "Il nome deve contenere tra 2 e 50 caratteri")
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(min = 2, max = 50, message = "Il cognome deve contenere tra 2 e 50 caratteri")
    private String cognome;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Size(min = 8, message = "La password deve contenere almeno 8 caratteri")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
            message = "La password deve contenere almeno una lettera minuscola, una maiuscola e un numero")
    private String password;

    @NotBlank(message = "La conferma password è obbligatoria")
    private String confermaPassword;

    @NotBlank(message = "Il numero di telefono è obbligatorio")
    @Pattern(regexp = "\\d{10}", message = "Il numero di telefono deve contenere esattamente 10 cifre numeriche")
    private String numeroTelefono;

    @NotNull(message = "La data di nascita è obbligatoria")
    @Past(message = "La data di nascita deve essere una data passata")
    private Date dataNascita;

    @NotBlank(message = "L'indirizzo è obbligatorio")
    @Size(min = 5, max = 100, message = "L'indirizzo deve contenere tra 5 e 100 caratteri")
    private String indirizzoDomicilio;

    @NotBlank(message = "Il genere è obbligatorio")
    @Pattern(regexp = "^[MFAmfa]$", message = "Il genere deve essere: M (maschio), F (femmina) o A (altro)")
    private String genere;

    public RegistrazioneProprietarioRequestDTO(String nome, String cognome, String email, 
                                               String password, String confermaPassword, 
                                               String numeroTelefono, Date dataNascita, 
                                               String indirizzoDomicilio, String genere) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.confermaPassword = confermaPassword;
        this.numeroTelefono = numeroTelefono;
        this.dataNascita = dataNascita;
        this.indirizzoDomicilio = indirizzoDomicilio;
        this.genere = genere;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfermaPassword() {
        return confermaPassword;
    }

    public void setConfermaPassword(String confermaPassword) {
        this.confermaPassword = confermaPassword;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getIndirizzoDomicilio() {
        return indirizzoDomicilio;
    }

    public void setIndirizzoDomicilio(String indirizzoDomicilio) {
        this.indirizzoDomicilio = indirizzoDomicilio;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }
}
