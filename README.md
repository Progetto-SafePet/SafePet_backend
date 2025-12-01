# 🐾 SafePet_backend

### 📚 Indice
* [Overview del progetto](#overview-del-progetto)
* [Tecnologie usate](#tecnologie-usate)
* [Microservizi](#microservizi)
* [Installazione ed esecuzione](#installazione-ed-esecuzione)
    * [Prerequisiti](#prerequisiti)
    * [Installazione Maven](#installazione-maven)
    * [Backend](#backend)
    * [Frontend](#frontend)
    * [Account per utilizzo](#account-per-utilizzo)
* [Team di sviluppo](#team-di-sviluppo)

---

### 💡 Overview del progetto
La **disponibilità immediata di informazioni cliniche aggiornate** è **cruciale** per interventi tempestivi ed efficaci sugli animali da compagnia, specialmente in **emergenze** (incidenti, allergie, intossicazioni, calamità naturali).
La rapidità di accesso a dati come patologie pregresse, terapie e allergie può essere **vitale**.
Il progetto **SafePet** è una piattaforma digitale che mira a:

* **Digitalizzare e Centralizzare i dati:** Superare la gestione cartacea e frammentata.
* **Garantire l'Immediata Disponibilità:** Assicurare che le informazioni cliniche essenziali siano disponibili in **ogni situazione di emergenza**.
* **Favorire la Cooperazione:** Semplificare la **condivisione sicura** delle informazioni tra proprietari e veterinari.
* **Potenziare la Risposta alle Emergenze:** Migliorare la capacità di risposta alle crisi veterinarie e sanitarie integrate.
* **Promuovere una Gestione Moderna:** Sostenere una sanità veterinaria più resiliente e le politiche nazionali di prevenzione e benessere animale.
---

### ⚙️ Tecnologie usate
* **React**
* **Vite.js** (v. 5.2.0)
* **Canva**
* **OpenStreetMap**
* **Spring Framework**
* **Spring Boot** (v. 3.5.7)
* **Java 21**
* **Hibernate**
* **Spring Data JPA**
* **MySQL Connector J**
* **JJWT** (Java JWT)
* **Spring Security Crypto**
* **Checkstyle** // ???
* **Maven**
* **JUnit**
* **Spring Test**
* **Mockito**
* **Postman**
* **Docker**
* **MySQL**

---

### 🧱 Microservizi

| Microservizio | Linguaggio | Descrizione | Link repository |
|:---|:---|:---|:---:|
| **Backend** | Java | Fornisce la logica di business, gestisce le API REST, l'autenticazione tramite JWT e <br/>la persistenza dei dati clinici nel database. | [🔍](https://github.com/Progetto-SafePet/SafePet_backend.git) |
| **Frontend** | TypeScript | Interfaccia utente dell'applicazione, sviluppata in React, che permette ai proprietari e ai veterinari di visualizzare e interagire con i dati sanitari degli pet. | [🔍](https://github.com/Progetto-SafePet/SafePet_frontend.git) |

---

### 🚀 Installazione ed esecuzione

---

### ✅ Prerequisiti
* Node.js (22.16.0)
* Java JDK 21.0.8
* Apache Maven 3.9.11
* Docker 4.51.0
* Intellij IDEA 2025.2.5 (per il backend)
* WebStorm IDEA 2025.2.5 (per il frontend)

---

### ⚙️ Installazione Maven
Per installare maven su sistema Windows è necessario aver già installato **Java Development Kit (JDK)** ed aver configurato la variabile d'ambiente `JAVA_HOME`.

Dopodichè è necessario seguire i seguenti passaggi:
1. Scaricare `apache-maven-3.9.11-bin.zip` dalla seguente pagina: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
2. Effettuare l'unzip del file scaricato al passo uno all'interno di una qualsiasi directory (preferibilmente in `Program Files`)
3. Aggiungere la directory della cartella `bin` (contenuta nella cartella estratta al passo 2) alla variabile d'ambiente `PATH`
4. Verificare se l'installazione è andata a buon fine utilizzando il comando `mvn -v` all`interno di una nuova shell

---

### ☕ Backend

Per eseguire il backend è necessario seguire i seguenti passaggi:
1.  **Avviare Intellij** e clonare la repository del progetto:
    * `https://github.com/Progetto-SafePet/SafePet_backend.git`
2.  **Aprire Docker**.
3.  Tramite il terminale di Intellij digitare:
    * `docker compose up -d`
4.  Eseguire `clean` e `install` di Maven.
5.  Runnare `BackendApplication.java`.

---

### 🎨 Frontend

Per eseguire il frontend è necessario seguire i seguenti passaggi:

1.  **Avviare WebStorm** e clonare la repository del progetto:
    * `https://github.com/Progetto-SafePet/SafePet_frontend.git`
2.  Tramite il terminale di WebStorm digitare:
    * `cd safapet`
3.  Digitare il comando per installare le dipendenze:
    * `npm install`
4.  Digitare il comando per avviare lo sviluppo:
    * `npm run dev`
5.  Selezionare l'indirizzo per visualizzare l'applicazione:
    * `http://localhost:5173/`

---

### 🔐 Account per utilizzo
| Email | Password | Ruolo |
|:---|:---|:---|
| maria.rossi@example.com | `$2a$10$Nqbed79OJKcw2Fx4WFYOWemUCRT58WFIghR7366cqVt0rSmVrFykO` | Proprietario |
| luca.bianchi@example.com | `$2a$10$J.NzkHNb/mFVLJDbKYYdLeAnHBKDHYcNb6t8FoM6uW9JeZIuaW3aq` | Proprietario |
| acmemoli@gmail.com | `$2a$10$ee/6pnNfOQpLAW.GhbQYbuwIfjq/nvEaWSY9F1pO8B6236gnCAqqy` | Veterinario |
| gamatruda2@gmail.com | `$2a$10$OVuLgX7mnmzIi2oZMfnceOJAGCuJjICfPLdLNHzeyNGKf9SN9D1aK` | Veterinario |

---

### 👥 Team di sviluppo
| Nome | Cognome | Ruolo |                                                   Profilo GitHub                                                   |
| :--- | :--- | :--- |:------------------------------------------------------------------------------------------------------------------:|
| Francesco Alessandro | Pinto | Project Manager |    [![Francesco Pinto](https://github.com/FrancescoPinto02.png?size=100)](https://github.com/FrancescoPinto02)     |
| Francesco Maria | Torino | Project Manager | [![Francesco Torino](https://github.com/FrancescoTorino1999.png?size=100)](https://github.com/FrancescoTorino1999) |
| Aldo | Adinolfi | Team Member |             [![Aldo Adinolfi](https://github.com/AldoAdi04.png?size=60)](https://github.com/AldoAdi04)             |
| Gianmarco | Amatruda | Team Member |           [![Gianmarco Amatruda](https://github.com/Giammi19.png?size=60)](https://github.com/Giammi19)            |
| Simone | Cimmino | Team Member |            [![Simone Cimmino](https://github.com/SimoCimmi.png?size=60)](https://github.com/SimoCimmi)             |
| Matteo Ferdinando | Emolo | Team Member |               [![Matteo Emolo](https://github.com/0xMatte.png?size=100)](https://github.com/0xMatte)               |
| Anna Chiara | Memoli | Team Member |           [![Anna Chiara Memoli](https://github.com/memoli04.png?size=60)](https://github.com/memoli04)            |
| Chiara | Memoli | Team Member |            [![Chiara Memoli](https://github.com/chiara0605.png?size=60)](https://github.com/chiara0605)            |
| Vincenzo Giuseppe | Nappi | Team Member |           [![Vincenzo Nappi](https://github.com/VincenzoGN.png?size=60)](https://github.com/VincenzoGN)            |
| Giuseppe | Rossano | Team Member |          [![Giuseppe Rossano](https://github.com/PeppeRed04.png?size=100)](https://github.com/PeppeRed04)          |
| Rosario | Saggese | Team Member |          [![Rosario Saggese](https://github.com/rossssss111.png?size=60)](https://github.com/rossssss111)          |
| Luca | Salvatore | Team Member |           [![Luca Salvatore](https://github.com/lucasalvaa.png?size=100)](https://github.com/lucasalvaa)           |
| Morgan | Vitiello | Team Member |       [![Morgan Vitiello](https://github.com/MorganVitiello.png?size=60)](https://github.com/MorganVitiello)       |