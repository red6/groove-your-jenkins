#!/bin/bash

DOCKER_SCRIPTS_HOME=src/scripts/docker

cp ./continuous-delivery-example-webapp.war $DOCKER_SCRIPTS_HOME/tomcat/
docker build -t grohmann/tomcat $DOCKER_SCRIPTS_HOME/tomcat/
rm $DOCKER_SCRIPTS_HOME/tomcat/continuous-delivery-example-webapp.war
docker run --rm --name tomcat-cd -p 8082:8080 grohmann/tomcat &


