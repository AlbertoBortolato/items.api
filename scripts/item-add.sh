#!/bin/bash

SWITCH="\e["
NORMAL="${SWITCH}0m"
YELLOW="${SWITCH}33m"
RED="${SWITCH}31m"
GREEN="${SWITCH}32m"
BLUE="${SWITCH}34m"

dir=$(dirname "${BASH_SOURCE[0]}")


user=admin
pass=admin-1
creds=$user:$pass
curl -X POST -d @$dir/item.json -H "Content-Type: application/json" http://$creds@localhost:8080/api
