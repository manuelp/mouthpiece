# mouthpiece

Simple anonymous guestbook, to spread ideas.

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

For deleting posts an authorization token is required, and by default it's empty and it's not possible to delete anything. To set an authorization token you just need to set the environment variable `MOUTHPIECE_TOKEN`. For example:

    MOUTHPIECE_TOKEN="some-password" lein ring server

Instead, if you start the application from the REPL (see the `mouthpiece.repl` namespace) then it's configured a default authorization token.

## Packaging

To package a standalone JAR just run:

    lein ring uberjar

You'll find the package in *target/mouthpiece-VERSION-standalone.jar*.

### Running packaged application as a *nix service

There is an init script in *service/mouthpiece* that you can edit and copy in */etc/init.d* to manage this application as a daemon.

## License

Copyright © 2014 Manuel Paccagnella, Released under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html)
