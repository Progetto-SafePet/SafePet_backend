package it.safepet.backend.gestioneCondivisioneDati.html;

import it.safepet.backend.gestioneCondivisioneDati.dto.CondivisioneDatiPetResponseDTO;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class LibrettoPetHtmlBuilder {

    private String loadLogoAsDataUrl() {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("src/main/resources/images/logo1.png"));
            String base64 = Base64.getEncoder().encodeToString(bytes);
            return "data:image/png;base64," + base64;
        } catch (Exception e) {
            return ""; // niente logo se fallisce
        }
    }


    public String buildHtml(CondivisioneDatiPetResponseDTO dto) {

        String vaccinazioni = dto.getCartellaClinica().getVaccinazioni().stream()
                .map(v -> "<tr><td>" + v.getNomeVaccino() + "</td><td>" + v.getDataDiSomministrazione() + "</td><td>"
                        + v.getRichiamoPrevisto() + "</td><td>" + v.getTipologia() + "</td></tr>")
                .reduce("", String::concat);

        String patologie = dto.getCartellaClinica().getPatologie().stream()
                .map(p -> "<tr><td>" + p.getNome() + "</td><td>" + p.getDataDiDiagnosi() + "</td><td>" + p.getSintomiOsservati()
                        + "</td></tr>")
                .reduce("", String::concat);

        String terapie = dto.getCartellaClinica().getTerapie().stream()
                .map(t -> "<tr><td>" + t.getNome() + "</td><td>" + t.getDurata() + "</td><td>" + t.getFormaFarmaceutica()
                        + "</td><td>" + t.getViaDiSomministrazione() + "</td><td>" + t.getFrequenza() + "</td></tr>" )
                .reduce("", String::concat);

        String visite = dto.getCartellaClinica().getVisiteMediche().stream()
                .map(v -> "<tr><td>" + v.getNome() + "</td><td>" + v.getData() + "</td></tr>")
                .reduce("", String::concat);

        String logoSrc = loadLogoAsDataUrl();

        return """
        <html>
        <head>
            <meta charset="UTF-8"/>
            <style>
    
                body {
                    font-family: Arial, sans-serif;
                    padding: 30px;
                    color: #3a2c1a;
                }
    
                h1 {
                    text-align: center;
                    color: #7d4f21;
                    font-size: 34px;
                    margin-top: 0;
                    margin-bottom: 40px;
                    border-bottom: 3px solid #c5a982;
                    padding-bottom: 10px;
                }
    
                h2 {
                    color: #7d4f21;
                    font-size: 24px;
                    margin-top: 40px;
                    margin-bottom: 15px;
                }
    
                table {
                    width: 100%%;
                    border-collapse: collapse;
                    margin-bottom: 25px;
                }
    
                th {
                    background-color: #f7e7ce;
                    color: #3a2c1a;
                    font-weight: bold;
                    padding: 10px;
                    border: 1px solid #e0d3b8;
                    width: 25%%;
                }
    
                td {
                    background-color: #ffffff;
                    padding: 10px;
                    border: 1px solid #e0d3b8;
                }
    
                /* BOX PROPRIETARIO */
                .box {
                    background-color: #f7e7ce;
                    border: 2px solid #d6b894;
                    padding: 20px;
                    border-radius: 8px;
                    margin-top: 20px;
                    margin-bottom: 30px;
                }
    
                .label {
                    font-weight: bold;
                }
    
                /* logo */
                .header-logo{
                    text-align: center;
                    margin-bottom: 5px;
                }
  
                .header-logo img {
                    width: 70px;
                    height: 70px;
                    margin-bottom: 20px;
                }
        
                .page-break-before {
                     page-break-before: always;
                }
            </style>
        </head>
    
        <body>
            <!-- LOGO -->
            <div class="header-logo">
                <img src="%s" alt="SafePet Logo" />
            </div>
        
            <h1> Scheda Anagrafica Pet: %s </h1>
        
            <!-- DATI ANAGRAFICI PET -->
            <h2>Dati Anagrafici</h2>
    
            <table>
                <tr><th>Specie</th><td>%s</td></tr>
                <tr><th>Razza</th><td>%s</td></tr>
                <tr><th>Sesso</th><td>%s</td></tr>
                <tr><th>Data Nascita</th><td>%s</td></tr>
                <tr><th>Peso</th><td>%s kg</td></tr>
                <tr><th>Colore Mantello</th><td>%s</td></tr>
                <tr><th>Microchip</th><td>%s</td></tr>
                <tr><th>Sterilizzato</th><td>%s</td></tr>
            </table>
    
            <!-- DATI PROPRIETARIO -->
            <h2>Contatti Proprietario</h2>
            <div class="box">
                <p><span class="label">Nome Completo:</span> %s %s<br/>
                   <span class="label">Email:</span> %s<br/>
                   <span class="label">Telefono:</span> %s<br/>
                   <span class="label">Indirizzo:</span> %s
                </p>
            </div>
   
            <h1 class="page-break-before">Storico Clinico Completo</h1>
    
            <h2>Vaccinazioni</h2>
            <table>
                <tr><th>Vaccino</th><th>Data</th><th>Richiamo</th><th>Tipologia</th></tr>
                %s
            </table>
    
            <h2>Patologie</h2>
            <table>
                <tr><th>Patologia</th><th>Data Diagnosi</th><th> Sintomi Osservati</th></tr>
                %s
            </table>
    
            <h2>Terapie</h2>
            <table>
                <tr><th>Terapia</th><th>Durata</th><th>Forma Farmaceutica</th><th>Via di somministrazione</th><th>Frequenza</th></tr>
                %s
            </table>
    
            <h2>Visite Mediche</h2>
            <table>
                <tr><th>Visita</th><th>Data</th></tr>
                %s
            </table>
    
        </body>
        </html>
        """.formatted(
                logoSrc,
                dto.getPet().getNome(),
                dto.getPet().getSpecie(),
                dto.getPet().getRazza(),
                dto.getPet().getSesso(),
                dto.getPet().getDataNascita(),
                dto.getPet().getPeso(),
                dto.getPet().getColoreMantello(),
                dto.getPet().getMicrochip(),
                dto.getPet().getSterilizzato() ? "Sì" : "No",
                dto.getProprietario().getNome(),
                dto.getProprietario().getCognome(),
                dto.getProprietario().getEmail(),
                dto.getProprietario().getTelefono(),
                dto.getProprietario().getIndirizzo(),
                vaccinazioni,
                patologie,
                terapie,
                visite
        );
    }
}
