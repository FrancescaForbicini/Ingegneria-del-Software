# Ingegneria del software 
Politecnico di Milano 20/21
## Componenti del gruppo
- Francesca Forbicini
- Nicolo Fontana
- Alberto Fanton
## Funzionalità implementate
- [x] Partite multiple (thread pool, Registry)
- [ ] **[NON COMPLETO]** Partita locale `feature/solo-game` (Connector)
- [ ] **[NON COMPLETO]** Partita modificabile `feature/custom-settings`
## Release
### Running the client
```bash
java -cp deliverables/final/jar/PSP64-1.0.jar \
     it.polimi.ingsw.client.ClientMain \
     2>errors.txt  # redirects stderr on `error.txt`, this redirect is useful while using the CLI 
```
**UNIX-ONLY:** This command is conveniently stored in `utils/client.sh`

### Running the server
```bash
java -cp deliverables/final/jar/SP64-1.0.jar \
     it.polimi.ingsw.server.ServerMain
```
**UNIX-ONLY:** This command is conveniently stored in `utils/server.sh`


## Development
In the following steps we assume maven is installed system-wide and can be correctly used in a through a shell/command prompt
### Testing
```bash
mvn test
```
### Building
```bash
mvn clean install
```
### Building and installing
```bash
mvn clean install \
    && cp target/PSP64-1.0.jar  deliverables/final/jar/PSP64-1.0.jar
```
**UNIX-ONLY:** This command is conveniently stored in `utils/clean-install.sh`

## Note di design
Pattern utilizzati
### MVC
Come da requisito, il pattern, attraverso la sua estensione MVVC (Model-View-VirtualView-Controller) definisce l'architettura fondamentale dell'applicazione

### Registry
Utilizzato per la gestione delle partite multiple. 
Implementato attraverso un *singleton* che mantiene una mappa di connessioni. Nella recezione di ogni connessione, il server affida la stessa ad thread di una thread pool (pool1) che si occupera di governare la fase di creazione partite e/o login.
I thread di questa pool, interrogheranno il *Registry* per verificare se la partita esiste ed eventualmente crearla.
La partita verrà eseguita su un thread a parte appartenente ad un'altra pool (pool2), a cui si ha accesso tramite questo componente (Registry).

### Singleton
Vasto uso di singleton per componanti che hanno uno stato univoco all'interno di un contesto, in particolare esistono:
- global singleton: solo il GameRegistry appartiene a questa classe, un'unica instanza è disponibile e condivisa da vari thread
- thread local singleton: nella thread pool che esegue le partite (pool2), diversi componenti ricadono in questa classe (`GameController`, `FaithTrack`,...)

### Builder
Usato per produrre aggiornamenti per la vista

### Strategy
Le azioni del gioco (`TurnAction`) sono un caso d'uso del pattern