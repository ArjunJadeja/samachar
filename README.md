<h1 align="center">Samachar</h1>

<p align="center">  
🎨 Samachar is an Android news app built to demonstrate the MVVM architecture in Kotlin. It utilizes Jetpack Compose, Dagger Hilt, Retrofit, RoomDB, Coroutines, and Flow. ✨
</p>

## App UI
<p float="left">
  <img src="assets/SamacharUi.png" width="800"/> 
</p>

## Download
Check out [Releases](https://github.com/ArjunJadeja/Samachar/releases) to download the latest APK.

## Tech stack & Open-source libraries
- **Minimum SDK level 26**
- [**Kotlin**](https://kotlinlang.org/) - Modern, concise, and safe programming language.
- [**Dagger Hilt**](https://developer.android.com/training/dependency-injection/hilt-android) - Dagger Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.
- [**Kotlin Serialization**](https://github.com/Kotlin/kotlinx.serialization) - A compiler plugin that automatically produces visitor code for classes, and a runtime library that uses generated code to serialize objects without reflection.
- [**Coroutines**](https://kotlinlang.org/docs/coroutines-overview.html) - Asynchronous or non-blocking programming is an important part of the development landscape. Coroutines is our recommended solution for asynchronous programming on Android.
- [**Flow**](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - An asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [**Retrofit**](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [**RoomDB**](https://developer.android.com/training/data-storage/room) - The Room persistence library provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.
- [**Jetpack Compose**](https://developer.android.com/develop/ui/compose) - Jetpack Compose is Android’s recommended modern toolkit for building native UI. It simplifies and accelerates UI development on Android.
- [**Coil**](https://coil-kt.github.io/coil/) - An image loading library for Android backed by Kotlin Coroutines.
- [**Material 3**](https://github.com/material-components/material-components-android) - Material design components for building View layouts and Animations.

## App Architecture
MVVM architecture and the Repository pattern.
<p float="left">
  <img src="assets/SamacharArchitecture.png" width="600"/> 
</p>

## Credits
- API from [NewsAPI](https://newsapi.org)
- Icons from [Flaticon](https://www.flaticon.com)
- Illustrations from [Iconsout](https://iconscout.com)

## License
```xml
MIT License

Copyright (c) 2024 Arjun Jadeja

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
```