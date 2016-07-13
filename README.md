Groove your Jenkins
======================================

Jenkins Jobs per Groovy Job DSL
---------------------------------------------------------
Die Groovy Skripte der JobDSL finden sich im Verzeichnis capitol-jenkins-seed.

Anleitung zum Aufsetzen des Continuous-Delivery-Beispiels
-----------------------------------------------------------

Voraussetzungen:

* Docker-Container
** Nexus: sonatype/nexus (Öffentliches Image)
** Jenkins: /continuous-delivery-example/src/scripts/docker/jenkins (Eigenes Image)


Anleitung:

1. Docker-Container mit Nexus starten
    docker run --name nexus sonatype/nexus
2. Docker-Image für Jenkins erstellen
    docker build -t grohmann/jenkins-cd-example .
3. Docker-Container für Jenkins starten:
    docker run -d -p 8080:8080 --name jenkins-cd --link nexus:nexus -v /var/run/docker.sock:/var/run/docker.sock grohmann/jenkins-cd-example

