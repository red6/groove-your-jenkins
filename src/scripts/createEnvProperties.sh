#!/bin/bash
c="host.ip"; c="$c=$(docker inspect $1 | grep -m 1 \"IPAddress | cut -d '"' -f 4)"; echo $c > continuous-delivery-example-test/src/test/resources/env.properties; echo host.port=8080 >> continuous-delivery-example-test/src/test/resources/env.properties


