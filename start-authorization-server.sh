#!/usr/bin/env bash

./mvnw clean install
./mvnw -pl :authorization-server spring-boot:run &
./mvnw -pl :resource-server spring-boot:run &