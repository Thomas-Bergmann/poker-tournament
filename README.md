Offline Poker Tournament and Cash Game Organizer

# Introduction

This project is intended be a service for all poker friends, which wants to play poker offline.
The software will help you to organize and run poker events.
Both cash games and tournaments will be supported.

# Feature Overview

## Cash Game

* Each registered user has an account to manage poker tournaments
* Create a new tournament and give the event a name and a buy-in (e.g. 5 EUR) as the result the created tournament is shown at the list
* The event list provides a link to manage players
    * Add new player - the player is added and assigned (competitor) to the event. The player can be easily assigned to other tournaments afterwards
    * Buy-In of an existing player - select the player and press "buy-in"
    * Re-Buy of a competitor - define the re-buy amount (the page contains the average over all competitors)
    * Seat-Open for a competitor - define the current amount of the player, the result for the player is calculated (the lost money is still registered as InPlay)

# Open Features and Tasks

* Display menu and registry of actions
* Define and implement a look and feel

## Open Tournament Features
* Manage tables and random placement of players
* Manage blind and ante steps
* Visualize current state of tournament

## Open Technical Tasks
* Usage of key store
* Encryption of passwords (one way encryption)
* Encryption of email addresses
* Signature of account reference cookie

# Developer Fun

This project is a java project (JDK8), it uses some important frameworks:
* Google Guice for dependency injection
* javax.ws as web application
* JPA for persistence (with hibernate implementation and postgres database)
* gradle build tool
* XSLT for template processing

<pre>
git clone https://github.com/OfflinePoker/Tournament.git Tournament
cd Tournament
gradle eclipse
gradle test
gradle war
</pre>

Before you submit code, please
* apply the formatter for eclipse dev_env/code_formatter.xml
* add the content of dev_env/git_info_exclude.txt to your git exclude file
* add the pre-commit checker dev_env/git_hooks_pre-commit

The project is registered at:
* Travis CI ![branch master](https://travis-ci.org/OfflinePoker/Tournament.svg?branch=master)
* Coverall.IO [![Coverage Status](https://coveralls.io/repos/OfflinePoker/Tournament/badge.png)](https://coveralls.io/r/OfflinePoker/Tournament)

# Installation

* Installation of PostgreSQL (http://www.postgresql.org/)
    * create two databases (accountdb and tournamentdb)
* Installation of "Tomcat 8" (http://tomcat.apache.org/) to
    * (example location d:\http8090)
    * you can change the http port conf/httpd.conf
* use services.bat to register as service (optional)
    * use SERVICE_NAME with http port in name
    * define JRE_HOME at beginning set JRE_HOME=d:\jdk1.7.0_45\jre
* copy postgresql jdbc driver from (http://jdbc.postgresql.org/download.html) to lib
    d:\http8090\lib\postgresql-9.3-1102.jdbc4.jar
* add datasources to context.xml (d:\http8090\conf\context.xml). Each project contains an example context.xml for detailed information
    * database accounts
    * mail server
* add tomcat users
    d:\http8090\conf\tomcat-users.xml
* copy application war file from build directory
    d:\http8090\webapps\de.hatoka.account-0.1.war
    d:\http8090\webapps\de.hatoka.tournament-0.1.war
* copy de.hatoka.jndi-0.1.jar to tomcat/lib directory

<pre>
$> set JPDA_OPTS=-agentlib:jdwp=transport=dt_socket,address=6667,server=y,suspend=n
$> catalina.bat jpda run
</pre>

wget http://localhost:8090/de.hatoka.account-0.1/account/accounts/list.html
