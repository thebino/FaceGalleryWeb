# FaceGallery Web
[![CC-BY license](https://img.shields.io/badge/License-CC--BY-blue.svg)](https://creativecommons.org/licenses/by-nd/4.0)
![CI](https://github.com/thebino/FaceGalleryWeb/workflows/CI/badge.svg)

Face Gallery is an open source application to manage images within included faces.

 * [Ktor](https://github.com/ktorio/ktor) - Kotlin async web framework
 * [Netty](https://github.com/netty/netty) - Async web server
 * [Exposed](https://github.com/JetBrains/Exposed) - Kotlin SQL framework

## Table of contents

* [Getting started](#getting-started)
  * [Prerequisites](#prerequisites)
* [Library usage](#library-usage)
* [Contribution](#contribution)
* [License](#license)

## Getting started
#### Prerequisites
TensorFlow needs to be installed
```shell script
pip3 install tensorflow
```

Download the appropriate [JNI Archive from the website](https://www.tensorflow.org/install/lang_java#tensorflow_with_the_jdk) and unpack it to your `lib` directory.

For macOS for example, use the following command:
```shell script
tar -C /usr/local/lib/ -xvf libtensorflow_jni-cpu-darwin-x86_64-1.15.0.tar.gz
```


OpenCV needs to be installed
```shell script
brew install opencv
```

## Installation

To build and run the app, use the included gradle wrapper:

```sh
./gradlew run
```

## Notes
* [Interesting links](notes/Notes.md)
* [Tensorflow Operations](notes/detect_tensorflow_model_operations.md)

## Contributing

1. Fork it (https://github.com/thebino/FaceGallery/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

## License
Stürmer Benjamin – [@BenjaminStrmer](https://twitter.com/BenjaminStrmer) – webmaster@stuermer-benjamin.de

<a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/">Creative Commons Attribution-ShareAlike 4.0 International License</a>.
