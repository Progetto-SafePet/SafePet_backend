package it.safepet.backend.gestioneCondivisioneDati.html;

import it.safepet.backend.gestioneCondivisioneDati.dto.CondivisioneDatiPetResponseDTO;

public class LibrettoPetHtmlBuilder {

    public String buildHtml(CondivisioneDatiPetResponseDTO dto) {

        String vaccinazioni = dto.getCartellaClinica().getVaccinazioni().stream()
                .map(v -> "<tr><td>" + v.getNomeVaccino() + "</td><td>" + v.getDataDiSomministrazione() + "</td></tr>")
                .reduce("", String::concat);

        String patologie = dto.getCartellaClinica().getPatologie().stream()
                .map(p -> "<tr><td>" + p.getNome() + "</td><td>" + p.getDataDiDiagnosi() + "</td></tr>")
                .reduce("", String::concat);

        String terapie = dto.getCartellaClinica().getTerapie().stream()
                .map(t -> "<tr><td>" + t.getNome() + "</td><td>" + t.getDurata() + "</td></tr>")
                .reduce("", String::concat);

        String visite = dto.getCartellaClinica().getVisiteMediche().stream()
                .map(v -> "<tr><td>" + v.getNome() + "</td><td>" + v.getData() + "</td></tr>")
                .reduce("", String::concat);

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
    
                .divider {
                    border-bottom: 2px solid #c5a982;
                    margin: 40px 0;
                }
    
            </style>
        </head>
    
        <body>
    
            <h1>Scheda Anagrafica Pet: %s</h1>
    
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
   
    
            <h1>Storico Clinico Completo</h1>
    
            <h2>Vaccinazioni</h2>
            <table>
                <tr><th>Vaccino</th><th>Data</th></tr>
                %s
            </table>
    
            <h2>Patologie</h2>
            <table>
                <tr><th>Patologia</th><th>Data Diagnosi</th></tr>
                %s
            </table>
    
            <h2>Terapie</h2>
            <table>
                <tr><th>Terapia</th><th>Durata</th></tr>
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
