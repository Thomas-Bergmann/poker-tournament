Tournament
==========

Offline Poker Tournament and Cash Game Organizer

# Introduction

# Installation

* Standard Installation Tomcat 8 to
    * d:\http8090
    * you can change the http port conf/httpd.conf
* use services.bat to register as service (optional)
    * use SERVICE_NAME with http port in name
    * define JRE_HOME at beginning set JRE_HOME=d:\jdk1.7.0_45\jre
* copy postgresql jdbc driver from (http://jdbc.postgresql.org/download.html) to lib
    d:\http8090\lib\postgresql-9.3-1102.jdbc4.jar
* add datasource
    d:\http8090\conf\context.xml
* add tomcat users
    d:\http8090\conf\tomcat-users.xml
* add application war file from build directory
    d:\http8090\webapps\de.hatoka.account-0.1.war
    d:\http8090\webapps\de.hatoka.tournament-0.1.war

<pre>
$> set JPDA_OPTS=-agentlib:jdwp=transport=dt_socket,address=6667,server=y,suspend=n
$> catalina.bat jpda run
</pre>

wget http://localhost:8090/de.hatoka.account-0.1/account/accounts/list.html

# Developer Environment

<pre>
git clone https://github.com/OfflinePoker/Tournament.git Tournament
cd Tournament
gradle eclipse
gradle test
</pre>

Before you submit code, please
* apply the formatter for eclipse dev_env/code_formatter.xml
* add the content of dev_env/git_info_exclude.txt to your git exclude file
* add the pre-commit checker dev_env/git_hooks_pre-commit
