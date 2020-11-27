FROM openjdk:8-jre-alpine

COPY target/southsayer-backend-*.jar southsayer-backend.jar
COPY src/main/docker/config/cacerts /cacerts
COPY src/main/docker/config/wrapper.sh /wrapper.sh
COPY src/main/docker/config/cities.json /cities.json
COPY src/main/resources/static/files /src/main/resources/static/files
COPY downloads /downloads

ENV ENVIRONMENT=prod
ENV DATABASE_ENV_URL=jdbc:oracle:thin:@PFRLMDBOAAD01:1521:poaadb
ENV DATABASE_ENV_USERNAME=oaa
ENV DATABASE_ENV_PASSWORD=oaa
ENV DATABASE_ENV_SCHEMA=oaa
ENV WS_STORAGE_URL=http://ypoaas.corp.leroymerlin.com/spc-storage-server/services/WsSpcStorageServiceV3?wsdl

RUN chown 2000:2000 /southsayer-backend.jar /cacerts /wrapper.sh /cities.json
RUN chmod 777 -R /src/main/resources/static/files
RUN chmod 777 -R /downloads
RUN chmod +x /wrapper.sh

EXPOSE 8080

USER 2000

ENTRYPOINT ["/wrapper.sh"]
