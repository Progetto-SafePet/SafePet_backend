package it.safepet.backend.gestioneAnalisiDermatologica.service;

import it.safepet.backend.gestioneAnalisiDermatologica.dto.RisultatoDiagnosiDTO;

public interface AnalisiDermatologicaService {
    RisultatoDiagnosiDTO avviaAnalisiDermatologica(byte[] image);
}

