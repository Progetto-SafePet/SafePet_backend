package it.safepet.backend.reportCliniche.dto;

public class ElencoResponseDTO {
    private Long idVeterinario;
    private String nomeVeterinario;
    private String cognomeVeterinario;
    private Long idClinica;
    private String nomeClinica;
    private String indirizzoClinica;
    private String telefonoClinica;

    public ElencoResponseDTO(Long idVeterinario, String nomeVeterinario, String cognomeVeterinario, Long idClinica,
                             String nomeClinica, String indirizzoClinica, String telefonoClinica) {
        this.idVeterinario = idVeterinario;
        this.nomeVeterinario = nomeVeterinario;
        this.cognomeVeterinario = cognomeVeterinario;
        this.idClinica = idClinica;
        this.nomeClinica = nomeClinica;
        this.indirizzoClinica = indirizzoClinica;
        this.telefonoClinica = telefonoClinica;
    }

    public Long getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(Long idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public String getNomeVeterinario() {
        return nomeVeterinario;
    }

    public void setNomeVeterinario(String nomeVeterinario) {
        this.nomeVeterinario = nomeVeterinario;
    }

    public String getCognomeVeterinario() {
        return cognomeVeterinario;
    }

    public void setCognomeVeterinario(String cognomeVeterinario) {
        this.cognomeVeterinario = cognomeVeterinario;
    }

    public Long getIdClinica() {
        return idClinica;
    }

    public void setIdClinica(Long idClinica) {
        this.idClinica = idClinica;
    }

    public String getNomeClinica() {
        return nomeClinica;
    }

    public void setNomeClinica(String nomeClinica) {
        this.nomeClinica = nomeClinica;
    }

    public String getIndirizzoClinica() {
        return indirizzoClinica;
    }

    public void setIndirizzoClinica(String indirizzoClinica) {
        this.indirizzoClinica = indirizzoClinica;
    }

    public String getTelefonoClinica() {
        return telefonoClinica;
    }

    public void setTelefonoClinica(String telefonoClinica) {
        this.telefonoClinica = telefonoClinica;
    }
}
