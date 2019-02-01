FROM java:8



ADD /build/libs/loanapprover-0.0.1-SNAPSHOT.jar loanapprover.jar
RUN sh -c 'touch /loanapprover.jar'

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/loanapprover.jar"]
