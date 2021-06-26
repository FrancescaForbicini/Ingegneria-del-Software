# Ingegneria del software 
Politecnico di Milano 20/21
## Componenti del gruppo
- Francesca Forbicini
- Nicolo Fontana
- Alberto Fanton
## Funzionalità implementate
TODO
## Documentation

## Release
### Running the client
```bash
java -cp deliverables/final/jar/PSP64-1.0.jar
     it.polimi.ingsw.client.ClientMain 
     2>errors.txt  # redirects stderr on `error.txt`, this redirect is useful while using the CLI 
```
**UNIX-ONLY:** This command is conveniently stored in `utils/client.sh`

### Running the server
```bash
java -cp deliverables/final/jar/SP64-1.0.jar 
     it.polimi.ingsw.server.ServerMain
     2>errors.txt  # redirects stderr on `error.txt` 
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
mvn clean install && cp target/PSP64-1.0-jar-with-dependencies.jar  deliverables/final/jar/PSP64-1.0.jar
```
**UNIX-ONLY:** This command is conveniently stored in `utils/build-install.sh`