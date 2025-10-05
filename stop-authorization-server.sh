#!/usr/bin/env bash

kill $(ps aux | grep ':authorization-server' | awk '{print $2}')
kill $(ps aux | grep ':resource-server' | awk '{print $2}')