# CISQ1: Lingo Trainer

Erik Meijer V2D

[![Java CI](https://github.com/ErikMeijerHU/cisq1-lingo/actions/workflows/build.yml/badge.svg)](https://github.com/ErikMeijerHU/cisq1-lingo/actions/workflows/build.yml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ErikMeijerHU_cisq1-lingo&metric=coverage)](https://sonarcloud.io/dashboard?id=ErikMeijerHU_cisq1-lingo)


## Domeinmodel
![trainer-domein](https://user-images.githubusercontent.com/70518847/113525700-b947a980-95b6-11eb-88ef-00585786f6ab.png)

# Security
## A1:2017 - Injection
### Beschrijving
Injection houdt in dat er in een input van het programma, code geinjecteerd kan worden (meestal SQL) om bepaalde 
gegevens op te halen waar die gebruiken normaal niet bij hoort te kunnen of juist om dingen aan te passen in het 
programma/database.

### Risico's 
In dit project is de enige directe user tekst input het doen van een gok wat het woord is, daar zou dan dus misschien
injection kunnen gebeuren als dat fout gehanteerd wordt.

Het toevoegen van authenticatie en autorisatie zou ervoor zorgen dat niet zomaar iedereen bij andere mensen hun gegevens
kunnen, dus dat zou een deel van de injection oplossen, maar injection zou misschien ook juist weer gebruikt kunnen worden
om die authenticatiegegevens te stelen.

### Tegenmaatregelen
Aangezien de enige inputs bestaande woorden mogen zijn, kan vrij makkelijk alle speciale tekens en spaties eruit gefilterd
worden zodat injection al meteen niet meer werkt.

## A9:2017 - Using Components with Known Vulnerabilities
### Beschrijving
OWASP A9 houdt in dat er een component gebruikt wordt, dat bekend staat dat er fouten in zitten of onveilig kan zijn.
Dit kan mogelijk voor backdoors of andere fouten zorgen.
### Risico's
In dit project is de enige dependency die een warning gekregen heeft van de security test Tomcat,
wat alleen maar gebruikt wordt voor local test hosting, dus dit is in echte deployment geen risico.

Het toevoegen van authenticatie en autorisatie zou in dit geval niet veel verschil maken.
### Tegenmaatregelen
Om te zorgen dat dit geen problemen veroorzaakt moet je altijd zorgen dat de laatste versie van dependencies/plugins
gebruikt wordt of je zorgt dat er helemaal geen plugins met bekende vulnerabilities gebruikt worden.

## A10:2017 - Insufficient Logging & Monitoring
### Beschrijving
Deze is vrij vanzelfsprekend wat het betekend, dat er te weinig gelogd en gemonitord wordt, bijvoorbeeld test logs,
error logs, logins etc.
### Risico's
Door te weinig te loggen en te monitoren kan het veel langer duren om een mogelijke aanval/backdoor te ontdekken en
op te lossen. In dit is het enige dat gelogd wordt de tests, voor de rest wordt er helemaal niks opgeslagen als errors.

Het implementeren van een login-systeem zou voor nog iets zorgen dat gelogd en gemonitord moet worden.
### Tegenmaatregelen
Om problemen met logging te voorkomen moet gezorgd worden dat alle login, errors en tests op een duidelijke en
makkelijke manier wordt opgeslagen mogelijk met alerts zodat ze snel gelezen kunnen worden en er dus snel een reactie kan komen.

