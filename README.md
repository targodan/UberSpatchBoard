# UberSpatchBoard [![Build Status](https://travis-ci.org/targodan/UberSpatchBoard.svg?branch=develop)](https://travis-ci.org/targodan/UberSpatchBoard) [![Coverage Status](https://coveralls.io/repos/github/targodan/UberSpatchBoard/badge.svg?branch=develop)](https://coveralls.io/github/targodan/UberSpatchBoard?branch=develop)

The ultimate spatch board for dispatching fuel rats cases.


# How to Install

**NOTE:** So far only HexChat is supported. The default place where it is looking for the logfile is `%appdata%\hexchat\logs\fuelrats\#fuelrats.log` or on linux `~/.config/hexchat/fuelrats/fuelrats.log`.
If your HexChat logs to some other location go to `File` -> `Settings` and change the location.

## Archlinux

Just install the package from the AUR using your favourite AUR helper. `pacaur -S uberspatchboard-git`

## Windows

Just download the latest "\*-windows.zip"-file from [releases](https://github.com/targodan/UberSpatchBoard/releases) unzip it to wherever you want and double click the exe-file to start it. Done.

If you want to move the jar file to a different location without loosing your settings do also move the file `usb.yml` with the jar file as that contains the configuration.

## Other OS

Just download the latest ".jar"-file from [releases](https://github.com/targodan/UberSpatchBoard/releases) put it wherever you want and double click to start it. If double clicking doesn't work you may need to execute it via `java -jar UberSpatchBoard.jar`.

If you want to move the jar file to a different location without loosing your settings do also move the file `usb.yml` with the jar file as that contains the configuration.

# Disclaimer

Greetings Dispatch o7. It is important that you know this is a dispatch **aid**, it won't do your job for you and you will still need to stay at your console watching every message coming in.
Do **not** rely on it!
There *will* be things that this tool won't detect and there may be things that this tool will say it detected but that didn't actually happen.

# Features

- Tracking of jump calls and reports like sys, fr and so on.
- Tracking of basic commands like `!go 2 rat1 rat2` and so on. (`!grab` and `!inject` not yet supported but coming soon)

# Planned Features

- Support of other IRC clients
- Prepared answers and questions for sending to the client like `Hello CLIENT, do you see a blue O2 timer at the top right of your screen?`

Any feature you'd like to see missing? Go to [issues](https://github.com/targodan/UberSpatchBoard/issues) and either open up a new issue or give a :+1: on an existing issue that you really want to see implemented.

# Contribute

You know your way around Java and want to help out? Great! :smiley:
Please read the [Code of Conduct](CODE_OF_CONDUCT.md) and then just make a fork and open a pull request on the `develop` branch (not the master branch).

Please take a look at the existing code though and adopt that code style.
I'm sorry but that style is not debatable and pull requests not written in that style will not be accepted.
With mixed styles the code would grow to be absolutely hideous and ultimately unmaintainable.

Also please try and keep your commit messages reasonably descriptive.
I don't like seeing commit messages like "Did stuff".
There is always `git rebase -i`. :wink:

Your favourite IRC client is not supported? No problem, it's quite easy to add support for any logging IRC client. Just take a look at this tutorial: [How to add support for any IRC client?](https://github.com/targodan/UberSpatchBoard/wiki/How-to-add-support-for-any-IRC-client%3F)

# License

This is released under the MIT license.

MIT License

Copyright (c) 2017 Luca Corbatto

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
