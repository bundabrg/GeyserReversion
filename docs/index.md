![Logo](img/title.png)

GeyserReversion is a Geyser Extension that provides protocol level support for MultiVersion in Geyser and will eventually
allow any supported version to connect to any supported Geyser.

If you've heard of [ViaVersion](https://github.com/ViaVersion/ViaVersion) then this is the equivalent but for the Bedrock
side of Geyser. It allows any of the supported client versions listed below to connect to any of the supported server
versions.

## Features

* Minecraft Educational Edition Support
* Multiversion Support - As more versions of an edition become available they can be added

## Client Versions Supported

### Minecraft Bedrock
* Minecraft Bedrock v1.16.2
* Minecraft Bedrock v1.16.3
* Minecraft Bedrock v1.16.4

### Minecraft Education
* Minecraft Education v1.14.31
* Minecraft Education v1.14.50

## Server Versions Supported
* Minecraft Bedrock v1.16.2
* Minecraft Bedrock v1.16.3
* Minecraft Bedrock v1.16.4

## Quick Start

1. Make sure you are running a build of Geyser that supports native extensions. You can find a prebuilt one [here](https://github.com/bundabrg/Geyser/releases).

2. Down the latest [GeyserReversion](https://github.com/bundabrg/GeyserReversion/releases) and place it inside your Geyser extensions folder. Note this is
a folder 'extensions' underneath your Geyser folder. If you are running a plugin version of Geyser (Spigot/Bungeecord/Velocity)  then
make sure to place it inside the `plugins/Geyser/extensions` folder. Standalone verison of Geyser will be a `extensions` folder
where your Geyser.jar file is located.

3. Start Geyser. It should show some indication that GeyserReversion is running. For example:

    ```
    [11:08:37 INFO] {GeyserReversion} EducationServer listening on /0.0.0.0:19133
    [11:08:37 DEBUG] {GeyserReversion} Registered Translator: Translator_v390ee_to_v408be
    ```

4. If using `education` you will need to generate a token for each tenancy that you support otherwise you'll get a 
"School not allowed" error. See [education docs](education.md) for more info
