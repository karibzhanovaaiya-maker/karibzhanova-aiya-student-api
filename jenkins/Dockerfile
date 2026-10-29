# Jenkins controller with Docker CLI, used to build the Student API image
# Author: Karibzhanova Aiya
# The Docker engine itself is NOT inside this image: Jenkins talks to the
# host's Docker through the mounted socket /var/run/docker.sock.

FROM jenkins/jenkins:lts-jdk21

USER root
COPY --from=docker:27-cli /usr/local/bin/docker /usr/local/bin/docker
USER jenkins

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/

# Skip the first-run wizard; the admin user is created by init.groovy.d
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
