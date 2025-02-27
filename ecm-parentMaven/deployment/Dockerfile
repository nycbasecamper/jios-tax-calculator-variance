ARG BASE_IMAGE=passed_from_the_jenkinsfile
FROM ecp-non-prod.nexus-ecp.web.irs.gov/ubi8/${BASE_IMAGE} AS builder

USER root

WORKDIR /app
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM ecp-non-prod.nexus-ecp.web.irs.gov/ubi8/${BASE_IMAGE}

COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/application/ ./

USER spring
EXPOSE 8443

LABEL "irs.project.name"="ECM" \
      "irs.project.group.email"="it.ecm.systems-team@irs.gov" \
      "irs.poc.name"="Michael Gayhart" \
      "irs.poc.email"="michael.l.gayhart@irs.gov" \
      "irs.poc2.name"="Bill Schwartz" \
      "irs.poc2.email"="william.m.schwartz@irs.gov" \
      "irs.ao"="Bob James" \
      "irs.ao.email"="robert.c.james@irs.gov" \
      "irs.isso"="Carey Kovach" \
      "irs.isso.email"="Carey.M.Kovach@irs.gov>"

ENTRYPOINT ["java", "-Dcom.redhat.fips=false", "org.springframework.boot.loader.launch.JarLauncher"]