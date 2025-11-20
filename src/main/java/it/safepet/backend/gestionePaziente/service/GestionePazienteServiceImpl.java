package it.safepet.backend.gestionePaziente.service;

import it.safepet.backend.gestionePaziente.dto.PazienteResponseDTO;
import it.safepet.backend.gestionePaziente.repository.LinkingCodeRepository;
import it.safepet.backend.gestionePet.dto.PetResponseDTO;
import it.safepet.backend.gestionePet.model.Pet;
import it.safepet.backend.gestionePet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Calendar;
import java.util.List;

@Service
@Validated
public class GestionePazienteServiceImpl implements GestionePazienteService {
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private LinkingCodeRepository linkingCodeRepository;

    @Override
    public int getEta(Pet pet) {
        if (pet.getDataNascita() == null) {
            return 0;
        }

        // Convertiamo la Date in Calendar
        Calendar nascita = Calendar.getInstance();
        nascita.setTime(pet.getDataNascita());

        Calendar oggi = Calendar.getInstance();

        int eta = oggi.get(Calendar.YEAR) - nascita.get(Calendar.YEAR);

        // Controlla se il compleanno non è ancora passato
        if (oggi.get(Calendar.MONTH) < nascita.get(Calendar.MONTH) ||
                (oggi.get(Calendar.MONTH) == nascita.get(Calendar.MONTH) &&
                        oggi.get(Calendar.DAY_OF_MONTH) < nascita.get(Calendar.DAY_OF_MONTH))) {
            eta--;
        }

        return eta;
    }



    @Override
    public List<PetResponseDTO> getAllPets() {
        /**
         * Recupera tutti i PET presenti nel database tramite PetRepository.
         * Ogni entità Pet viene convertita in un DTO PetResponseDTO,
         * che contiene solo i dati necessari alla visualizzazione.
         * @return lista di PetResponseDTO con le informazioni dei PET
         */

        return Repository.findAll().stream().map(pet ->
                new PazienteResponseDTO(
                        pet.getNome(),
                        pet.getSpecie(),
                        getEta(pet),
                        pet.getProprietario().getNome(),

                        pet.getSesso(),
                        pet.getFoto()
                )
        ).toList();
    }
}
