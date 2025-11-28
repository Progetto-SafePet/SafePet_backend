package it.safepet.backend.gestionePet.repository;

import it.safepet.backend.gestionePet.model.NoteProprietario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.List;

public interface NoteProprietarioRepository extends JpaRepository<NoteProprietario, Long> {
    List<NoteProprietario> findByPetId(Long petId);
}
