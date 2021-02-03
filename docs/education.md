## Overview

Minecraft Education has a different method of allowing users on. By default a Minecraft Educational Client
can only connect to another Minecraft Education Client who is part of the same Microsoft Tenancy.

The server has to have a token for each tenancy that it will allow logins for. It can support multiple tenancies
at the same time.

## Generating a Token

To generate a token you will need to enter the following geyser command into the server console, not the in-game chat:

```
geyser education new
```

This will provide a URL that a user in the tenancy must open in their browser (any user in the tenancy is fine). This will allow them to log into
Microsoft as usual. They will end up with a white screen and a long address in their address bar. This address needs
to be copied. Then enter the following geyser command:

```
geyser education confirm {copied address}
```

This will generate the appropriate token. This token only allows the server access to the minecraft part of the account
and if the user ever changes their Microsoft password the token will no longer be valid. Now any user in that same
tenancy will be able to connect.

## Notes

TCP must also be portforwarded to be able to connect with Minecraft Education.

Minecraft Education uses the Tenancy username as the player username. This can cause collisions, so you may
wish to additionally use [GeyserLogin](https://github.com/bundabrg/GeyserLogin/releases) to provide a login screen
for them to select a username. 
