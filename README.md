![Logo](https://bundabrg.github.io/GeyserReversion/img/title.png)

[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](https://lbesson.mit-license.org/)
[![GitHub release](https://img.shields.io/github/release/Bundabrg/GeyserReversion)](https://GitHub.com/Bundabrg/GeyserReversion/releases/)
[![GitHub commits](https://img.shields.io/github/commits-since/Bundabrg/GeyserReversion/latest)](https://GitHub.com/Bundabrg/GeyserReversion/commit/)
[![Github all releases](https://img.shields.io/github/downloads/Bundabrg/GeyserReversion/total.svg)](https://GitHub.com/Bundabrg/GeyserReversion/releases/)
<!-- ![HitCount](http://hits.dwyl.com/bundabrg/GeyserReversion.svg) -->

![Workflow](https://github.com/bundabrg/GeyserReversion/workflows/build/badge.svg)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-no-red.svg)](https://GitHub.com/Bundabrg/GeyserReversion/graphs/commit-activity)
[![GitHub contributors](https://img.shields.io/github/contributors/Bundabrg/GeyserReversion)](https://GitHub.com/Bundabrg/GeyserReversion/graphs/contributors/)
[![GitHub issues](https://img.shields.io/github/issues/Bundabrg/GeyserReversion)](https://GitHub.com/Bundabrg/GeyserReversion/issues/)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/Bundabrg/GeyserReversion.svg)](http://isitmaintained.com/project/Bundabrg/GeyserReversion "Average time to resolve an issue")
[![GitHub pull-requests](https://img.shields.io/github/issues-pr/Bundabrg/GeyserReversion)](https://GitHub.com/Bundabrg/GeyserReversion/pull/)


---

[**Documentation**](https://bundabrg.github.io/GeyserReversion/)

[**Source Code**](https://github.com/bundabrg/GeyserReversion/)

---

PLEASE NOTE: As of 20/07/2022 this project is no longer being maintained as the extension interface was
not accepted upstream and it was too hard to maintain updates. I will be working on a proxy that will sit in front
of a bedrock server (be it Geyser or vanilla) and provide the same translation
of versions and a plugin interface. I'll update this notification once I have something up.

---

GeyserReversion is a Geyser Extension that provides protocol level support for MultiVersion in Geyser and will
eventually allow any supported version to connect to any supported Geyser.

If you've heard of [ViaVersion](https://github.com/ViaVersion/ViaVersion) then this is the equivalent but for the
Bedrock side of Geyser. It allows any of the supported client versions listed below to connect to any of the supported
server versions.

## Features

* Minecraft Educational Edition Support
* Multiversion Support - As more versions of an edition become available they can be added

## Client Versions Supported

### Minecraft Bedrock

* Minecraft Bedrock v1.16.0
* Minecraft Bedrock v1.16.2
* Minecraft Bedrock v1.16.3
* Minecraft Bedrock v1.16.100

### Minecraft Education
* Minecraft Education v1.14.31
* Minecraft Education v1.14.50
* Minecraft Education v1.14.70

## Server Versions Supported
* Minecraft Bedrock v1.16.2
* Minecraft Bedrock v1.16.3
* Minecraft Bedrock v1.16.100

## Quick Start

1. Make sure you are running a build of Geyser that supports native extensions. You can find a prebuilt one [here](https://github.com/bundabrg/Geyser/releases).

2. Down the latest [GeyserReversion](https://github.com/bundabrg/GeyserReversion/releases) and place it inside your Geyser extensions folder. Note this is
a folder 'extensions' underneath your Geyser folder. If you are running a plugin version of Geyser (Spigot/Bungeecord/Velocity) then
make sure to place it inside the `plugins/Geyser/extensions` folder. Standalone version of Geyser will be a `extensions` folder
where your Geyser.jar file is located.

3. Start Geyser. It should show some indication that GeyserReversion is running. For example:

```
[11:08:37 INFO] {GeyserReversion} EducationServer listening on /0.0.0.0:19133
[11:08:37 DEBUG] {GeyserReversion} Registered Translator: Translator_v390ee_to_v408be
```

4. If using `education` you will need to generate a token for each tenancy that you support otherwise you'll get a
   "School not allowed" error. See [education docs](https://bundabrg.github.io/GeyserReversion/education/) for more info

## Donations

As this is an open source project it is free to use and free to modify. If you do wish to donate then you may use the
following link with all funds going into my "Evening Fund".

The Evening Fund is a fund I (bundabrg) have set up for all my open source projects. Anything sent there will be used to
help others. I appreciate your contribution, and I thank you for giving me the ability to bring some happiness to others
as well.

* Bitcoin: `bc1q0twm860q6qtr4aaje4xqaft2zumsxdh37k04u5`
* Dogecoin: `DJspj5eK7ZjbDdPjVf1qZUGi3e8Gao79xT`
* Litecoin: `ltc1q23yqxgwn60rjlwdhpnwtzjyk9rw3rmmzy4qm3x`
