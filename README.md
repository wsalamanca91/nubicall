# nubicall Service

# Requirements
- Apache tomcat 9
- Java 8
- PostgreSQL 9.6

# Install 
To install the component is necesary to put the war file into the Tomcat deploy path. Then start tomcat and go to direction localhost:8081/nubicall.service/users to use the service.

# Configure
To configure the component is necesary to edit the  application.properties file. The file is in {tomcat_deploy_path}\nubicall.service\WEB-INF\classes. There is necesary to change the next values

- db.url: The Database URL
- db.username: Username to access database
- db.password: password to user database

# Logs
You can see the log file named nubicallRestLog.log in the next directory:

-  ${catalina.home}/logs/
