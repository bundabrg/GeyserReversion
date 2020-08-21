![Logo](https://bundabrg.github.io/GeyserReversion/img/title.png)

[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](https://lbesson.mit-license.org/)
[![GitHub release](https://img.shields.io/github/release/Bundabrg/GeyserReversion)](https://GitHub.com/Bundabrg/GeyserReversion/releases/)
[![GitHub commits](https://img.shields.io/github/commits-since/Bundabrg/GeyserReversion/latest)](https://GitHub.com/Bundabrg/GeyserReversion/commit/)
[![Github all releases](https://img.shields.io/github/downloads/Bundabrg/GeyserReversion/total.svg)](https://GitHub.com/Bundabrg/GeyserReversion/releases/)
![HitCount](http://hits.dwyl.com/bundabrg/GeyserReversion.svg)

![Workflow](https://github.com/bundabrg/GeyserReversion/workflows/build/badge.svg)
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/Bundabrg/GeyserReversion/graphs/commit-activity)
[![GitHub contributors](https://img.shields.io/github/contributors/Bundabrg/GeyserReversion)](https://GitHub.com/Bundabrg/GeyserReversion/graphs/contributors/)
[![GitHub issues](https://img.shields.io/github/issues/Bundabrg/GeyserReversion)](https://GitHub.com/Bundabrg/GeyserReversion/issues/)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/Bundabrg/GeyserReversion.svg)](http://isitmaintained.com/project/Bundabrg/GeyserReversion "Average time to resolve an issue")
[![GitHub pull-requests](https://img.shields.io/github/issues-pr/Bundabrg/GeyserReversion)](https://GitHub.com/Bundabrg/GeyserReversion/pull/)
 

---

[**Documentation**](https://bundabrg.github.io/GeyserReversion/)

[**Source Code**](https://github.com/bundabrg/GeyserReversion/)

---

GeyserReversion is a Geyser Plugin that provides protocol level support for MultiVersion in Geyser and will eventually
allow any supported version to connect to any supported Geyser.

## Features

* Minecraft Educational Edition Support
* Multiversion Support - As more versions of an edition become available they can be added

## Client Versions Supported
* Minecraft Education v1.14.31
* Minecraft Bedrock v1.16.2

## Server Versions Support
* Geyser Bedrock v1.16.2

## Quick Start

1. Make sure you are running a build of Geyser that supports native plugins. You can find a prebuilt one [here](https://github.com/bundabrg/Geyser/releases).

2. Down the latest [GeyserReversion](https://github.com/bundabrg/GeyserReversion/releases) and place it inside your Geyser plugins folder. Note this is
a folder 'plugins' underneath your Geyser folder. If you are running a plugin version of Geyser (Spigot/Bungeecord/Velocity)  then
make sure to place it inside the `plugins/Geyser/plugins` folder. Standalone verison of Geyser will be a `plugins` folder
where your Geyser.jar file is located.

3. Start Geyser. It should show some indication that GeyserReversion is running. For example:

```
[11:08:37 INFO] {GeyserReversion} EducationServer listening on /0.0.0.0:19133
[11:08:37 DEBUG] {GeyserReversion} Registered Translator: Translator_v390ee_to_v408be
```

 4. If using `education` you will need to generate a token for each tenancy that you support otherwise you'll get a 
 "School not allowed" error. See [education docs](https://bundabrg.github.io/GeyserReversion/education/) for more info
