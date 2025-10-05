#!/usr/bin/env bash

./start-authorization-server.sh
./mvnw -pl :javafx-desktop-application spring-boot:run
./stop-authorization-server.sh