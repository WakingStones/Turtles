## Waking Stones - Turtles

This is a community-driven effort to allow changes to Caller's Bane, a game released as freeware by Mojang.

https://mojang.github.io/CallersBaneSiteArchive/

## Using in Prod

Using this in production requires you to already install Caller's Bane. Follow the directions in the zip from
https://mojang.github.io/CallersBaneSiteArchive/.

Notable things:
- Java 8 is required https://adoptium.net/temurin/releases/?version=8


Download Turtle's latest artifacts from https://github.com/WakingStones/Turtles/actions (select latest run and artifacts
are at the bottom of the page)

Unzip into same place Caller's Bane server is located at.

Run the localserver.bat file

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
 