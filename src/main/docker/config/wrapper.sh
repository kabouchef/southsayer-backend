#!/bin/sh


java -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8001 -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -Djavax.net.ssl.trustStore=/cacerts -Djava.security.egd=file:/dev/./urandom -jar /southsayer-backend.jar
