package it.safepet.backend.gestioneAnalisiDermatologica;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.safepet.backend.gestioneAnalisiDermatologica.dto.RisultatoDiagnosiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.MediaType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SkinDetectorAdapter {
    private final WebClient webClient;

    @Autowired
    public SkinDetectorAdapter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Avvia l'analisi dermatologica inviando l'immagine al servizio di predizione.
     *
     * @param image L'immagine da analizzare in formato byte array.
     * @param formato Il formato dell'immagine (accetta solo MediaType.IMAGE_PNG o MediaType.IMAGE_JPG)
     * @return Un oggetto RisultatoDiagnosiDTO contenente il risultato della diagnosi.
     */
    public RisultatoDiagnosiDTO avviaAnalisiDermatologica(byte[] image, MediaType formato) {
        return mapResponse(sendRequest(image, formato));
    }

    /**
     * Invia una richiesta POST al servizio di predizione esterno.
     *
     * @param image L'immagine da inviare in formato byte array.
     * @param formato Il formato dell'immagine (accetta solo MediaType.IMAGE_PNG o MediaType.IMAGE_JPG)
     * @return La risposta del servizio in formato String (JSON).
     */
    public String sendRequest(byte[] image, MediaType formato) {
        final String URL = "http://localhost:8000/predict";

        ByteArrayResource fileAsResource = new ByteArrayResource(image) {
            @Override
            public String getFilename() {
                return formato.equals(MediaType.IMAGE_PNG) ? "image.png" : "image.jpg";
            }
        };

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", fileAsResource)
                .contentType(formato);

        return webClient
                .post()
                .uri(URL)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(builder.build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Mappa la risposta JSON del servizio di predizione in un oggetto RisultatoDiagnosiDTO.
     *
     * @param response La risposta JSON come stringa.
     * @return Un oggetto RisultatoDiagnosiDTO popolato con i dati della risposta.
     */
    private RisultatoDiagnosiDTO mapResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(response);
            if (root.isTextual()) {
                root = mapper.readTree(root.asText());
            }

            String classe = root.path("class").asText(null);
            double confidence = root.path("confidence").asDouble(Double.NaN);

            if (classe == null || classe.isBlank() || Double.isNaN(confidence)) {
                return new RisultatoDiagnosiDTO("errore", 0.0);
            }

            return new RisultatoDiagnosiDTO(classe, confidence);

        } catch (Exception e) {
            return new RisultatoDiagnosiDTO("errore", 0.0);
        }
    }
}
