# PhotoBooth for DSLR/DSLM (Kotlin/native)

[![CC-BY license](https://img.shields.io/badge/License-CC--BY-blue.svg)](https://creativecommons.org/licenses/by-nd/4.0)

A Photobooth application for (Windows, Linux & Mac) wich controls DSLR/DSLM cameras to creat stunning images automatically.
In history a photobooth is a automated photography machine wich was very popular on fairies and train stations where visitors could buy 8 printed photos for 25 cent.

When you only got printed images then, you can now send the digital copies directly to your mail inbox.

## Installation

You can run the release binary on

- Linux
- Mac
- Windows

without any requirements. A native UI should appear and guide you through the process.
If the application setup is already done, you can access it via the settings item in the top-right corner.


# Development

For development I recommend using the [IntelliJ IDE](https://www.jetbrains.com/idea/)

### GUI

To show native UI on Linux, Mac and Windows, the [kotlin-libui](https://github.com/msink/kotlin-libui) is used which is based on the original c-library [libui](https://github.com/andlabs/libui).

#### Connection

This application uses [libgphoto2](http://gphoto.org/proj/libgphoto2/) to communicate with a connected DSLR/DSLM camera.


## Release History

* 0.0.1
    * First draft
    
## Roadmap

- add settings screen
- control external flash light
- Mail integration
- WhatsApp integration

## Meta

Benjamin Stürmer – [@benjaminstrmer](https://twitter.com/benjaminstrmer) – webmaster@stuermer-benjamin.de

Distributed under the Attribution 4.0 International (CC BY 4.0) license. See ``LICENSE`` for more information.

[https://github.com/thebino/PhotoBooth](https://github.com/thebino/PhotoBooth)

## Contributing

1. Fork it (https://github.com/thebino/PhotoBooth/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request
