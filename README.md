## Waking Stones - Turtles

This is a community-driven effort to allow changes to Caller's Bane, a game released as freeware by Mojang.

https://mojang.github.io/CallersBaneSiteArchive/

## Installation

Using this requires you to already install Caller's Bane. Download the zip and extract it to a folder on your computer.
https://mojang.github.io/CallersBaneSiteArchive/

Notable things:
- Java 8 is required (https://adoptium.net/temurin/releases/?version=8)
- MariaDB 10.11 (recommended version, newer versions may not work) (https://mariadb.org/download/?t=mariadb&p=mariadb&r=10.11.2&os=windows&cpu=x86_64&pkg=msi)
- HeidiSQL (included with MariaDB)

Install Java:
- Use default values

Install MySQL:
- Leave as all features. 
- Set a root password.
- Check the "Use UTF8 as default server's character set"
- Install as a service

Start HeidiSQL:
- Select "New"
- Set Hostname as "localhost"
- Set "Password" to be the same password from above
- Select "Save"
- Connect to the new connection you just made
- Select "File" -> "Run SQL File"
- Navigate to the Callersbane folder you made before, select the "callersbane_database.sql" file. Select "Encoding" and change to UTF-8.
- Select "Tools" -> "User Manager"
- Select "Add".
- On the right, set "User name" as scrolls. Set "Password" as "scrolls"
- Select "Add object". Enter "scrolls" in the bottom textbox. 
- Check the box next to "Database: scrolls"
- Select "Save"

Configure Scrolls:
- Open the CallersBane-Server/cfg/hibernate.cfg.xml file
- Set the username to "scrolls"
- Set the password to "scrolls"
- Save this file

Run Scrolls as-is:
- Run the localserver.bat file
- Press CTRL+C after a few minutes to ensure the server is working correctly.

Setting up Turtles:
- Download Turtle's latest artifacts from https://github.com/WakingStones/Turtles/actions (select latest run and artifacts
are at the bottom of the page)
- Unzip into same place Caller's Bane Server.jar is located at (this is located in the CallerBane-Server folder).

Run Turtles:
- Run the startServer.bat file

Your server is now available at localhost, for local testing.

## Build

Building the project requires the following:
- Java 8

This has only been tested with IntelliJ.

Clone and run the "prepareWorkspace" Gradle task. Once complete, you may need to restart your IDE to fully load the
dependencies.

Because this is an old game, dependency updates are difficult to apply. Note that while there are CVEs being reported
with libs, it is a risk that has to be taken.

This relies on Mixin to work properly. A read-through of the wiki should be done to do code changes:
https://github.com/SpongePowered/Mixin/wiki

## Applying Changes

Changes are done in several ways, but care has to be taken to ensure they are done in the right order and in all places

- rules_desc.json can be used to tweak existing rules that do not have logic changes. This is the easiest way to
  fix a number on a rule (if supported)
- card_types.json can be edit existing scrolls. Use other scrolls for references, but this is how to tweak 
health/cooldowns and such
- New rules require both a database entry and a class that handles logic. Refer to the rules.CustomRules interface for
the base items needed, and "Appurtnance_2" for an example implementation. The rule must also be added to the database
for it to apply, but the registry will handle this for you.
 