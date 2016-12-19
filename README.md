Offline Poker Tournament and Cash Game Organizer

[![Build Status](https://travis-ci.org/Thomas-Bergmann/Tournament.svg?branch=master)](https://travis-ci.org/Thomas-Bergmann/Tournament)

# Introduction

This project is intended to be a service for all poker friends, who want to play poker offline.
The software helps to organize and run private poker events.
Both cash games and tournaments will be supported as well as performance tracking for players, poker events and established groups of players.

# Business requirements

## Individual accounts

* users of the platform (players, event organizers) have to have an account. The account must be created and can be deleted incl. all data (group assignments, statistics)
* basic account information are nickname, email address and password
* users can be part of multiple groups of players (based on request to group admin)
* each user can create poker events and groups of players
* individual statistics are collected per account. Initial statistics could be:
    * win/loss
    * average
    * period of time
    * #events

## Groups of players

A group of players is an established and regular playing group of offline poker players (in german: eine Pokerrunde)

* each user can create groups of players
* the user who creates a group of players is automatically the administrator of this group
* basic group information are offline location and group name
* the administrator can add and remove group members
* the administrator can delete/archive the group of players
* each group of players has its own statistics
    * see individual statistics
    * per poker event type

## Poker Events

Poker events are offline cash games or offline tournaments. The software supports the organization and execution of the event.

* basic poker event information are event name, date, location, buy-in and event type (cash/tournament)
* poker events are either open for all platform users or restricted to specific groups of players
* the creator of the event is autmatically the event administrator
* the administrator assigns users to the event
* each event has its own statistics
* cash game support:
     * buy-in tracking
     * re-buy tracking
* tournament support:
     * seat allocation
     * blind level definition
     * timing for blind levels
     * remove players
     * buy-in tracking
     * re-buy tracking

## Community

* match making: announce poker events to the community
* looking for group of players member: a group of players wants more members and announces its request to the community
* looking for a group of players: a user is looking for a group of players he can join and announces his request to the community
* a forum is provided to start individual discussion threads

# Technical requirements

## Mobile support

Because everything is usually managed during the offline game itself mobile support is essential.

## 24/7

The server should be available 7 days a week and 24 hours a day.


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
* JPA for persistence (with eclipse link implementation and postgres database)
* gradle build tool
* XSLT for template processing

<pre>
git clone https://github.com/Thomas-Bergmann/Tournament.git Tournament
cd Tournament
gradle eclipse
gradle test
gradle war
</pre>
Before you submit code, please
* apply the formatter for eclipse dev_env/code_formatter.xml
* add the content of dev_env/git_info_exclude.txt to your git exclude file
* add the pre-commit checker dev_env/git_hooks_pre-commit

# Installation
This installation is an example, you can choose your favorite database system, as long as there is a JPA support available.
* Installation of PostgreSQL (http://www.postgresql.org/)
    * create two databases (accountdb and tournamentdb)
* Installation of "Tomcat 8" (http://tomcat.apache.org/) to
    * (example location d:\http8090)
    * you can change the http port conf/httpd.conf
* use services.bat to register as service (optional)
    * use SERVICE_NAME with http port in name
    * define JRE_HOME at beginning set JRE_HOME=d:\jdk1.8.0_45\jre
* set environment of applications
    * see example setenv.bat (can be located 'bin' of tomcat)
* copy application war file from build directory
    d:\http8090\webapps\de.hatoka.account-x.x.x.war
    d:\http8090\webapps\de.hatoka.tournament-x.x.x.war

<pre>
$> set JPDA_OPTS=-agentlib:jdwp=transport=dt_socket,address=6667,server=y,suspend=n
$> catalina.bat jpda run
</pre>

wget http://localhost:8090/de.hatoka.account-0.1/account/accounts/list.html
