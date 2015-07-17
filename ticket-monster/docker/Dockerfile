FROM jboss/wildfly:8.2.0.Final
ADD postgresql-9.4-1201.jdbc4.jar /opt/jboss/wildfly/modules/system/layers/base/org/postgresql/main/
ADD module.xml /opt/jboss/wildfly/modules/system/layers/base/org/postgresql/main/
ADD ticket-monster.war /opt/jboss/wildfly/standalone/deployments/
ADD standalone.conf /opt/jboss/wildfly/bin/
ADD standalone.xml /opt/jboss/wildfly/standalone/configuration/
ADD forge-logo.png /tmp/ticket-monster/demo/src/main/webapp/admin/img/
RUN /opt/jboss/wildfly/bin/add-user.sh admin adminp@ssw0rd --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
