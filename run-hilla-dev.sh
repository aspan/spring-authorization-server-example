#!/usr/bin/env bash

./start-authorization-server.sh
./mvnw -pl :hilla-web-application spring-boot:run
./stop-authorization-server.sh