import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name="patologie")
public class Patologia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="nome", nullable = false)
    private String nome;

    @Column(name="data_di_diagnosi", nullable = false)
    private Date dataDiDiagnosi;

    @Column(name="sintomi_osservati", nullable = false)
    private String sintomiOsservati;

    @Column(name="diagnosi", nullable = false)
    private String diagnosi;

    @Column(name="terapia_associata", nullable = false)
    private String terapiaAssociata;

    public Patologia() {}

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

    public Date getDataDiDiagnosi() {
        return dataDiDiagnosi;
    }

    public void setDataDiDiagnosi(Date dataDiDiagnosi) {
        this.dataDiDiagnosi = dataDiDiagnosi;
    }

    public String getSintomiOsservati() {
        return sintomiOsservati;
    }

    public void setSintomiOsservati(String sintomiOsservati) {
        this.sintomiOsservati = sintomiOsservati;
    }

    public String getDiagnosi() {
        return diagnosi;
    }

    public void setDiagnosi(String diagnosi) {
        this.diagnosi = diagnosi;
    }

    public String getTerapiaAssociata() {
        return terapiaAssociata;
    }

    public void setTerapiaAssociata(String terapiaAssociata) {
        this.terapiaAssociata = terapiaAssociata;
    }
}