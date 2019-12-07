plugins {
    kotlin("multiplatform") version "1.3.70-dev-2529"
}

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = "http://dl.bintray.com/kotlin/kotlin-eap"
    }
}

val os = org.gradle.internal.os.OperatingSystem.current()!!

kotlin {
    if (os.isMacOsX) {
        macosX64("mac") {
            binaries {
                executable {
                    entryPoint = "photobooth.main"

                    linkerOpts("-Lsrc/macMain/lib/ui", "-lui")
                }
            }

            compilations.getByName("main") {
                val libuiInterop by cinterops.creating {
                    defFile(project.file("src/nativeInterop/cinterop/libui.def"))
                    compilerOpts("-Isrc/macMain/include/ui")
                }
//                kotlinOptions.freeCompilerArgs = listOf(
//                    "-include-binary", "src/macMain/lib/ui/libui.a"
//                )
            }
        }
    }

    if (os.isWindows) {
        // TODO: build windows
//        mingwX64("win") {
//        }
    }

    linuxArm32Hfp("raspberry") {
        binaries {
            executable {
                entryPoint = "photobooth.main"

                // libpigpio.so are compiled on raspberry pi and copied here
                linkerOpts("-Lsrc/lib/pigpio", "-lpigpio")

                // libPCA9685.so are compiled on raspberry pi and copied here
                linkerOpts("-Lsrc/lib/pca9685", "-lpca9685")

                linkerOpts("-Lsrc/lib/ui", "-lui")
            }
        }

        compilations.getByName("main") {
            val libuiInterop by cinterops.creating {
                defFile(project.file("src/nativeInterop/cinterop/libui.def"))
                compilerOpts("-Isrc/include/ui")
            }

            val pca9685Interop by cinterops.creating {
                defFile(project.file("src/nativeInterop/cinterop/pca9685.def"))
                compilerOpts("-Isrc/include/pca9685")
            }

            val pigpioInterop by cinterops.creating {
                defFile(project.file("src/nativeInterop/cinterop/pigpio.def"))
                compilerOpts("-Isrc/include/pigpio")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val macMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation("com.github.msink:libui:0.1.6")
            }
        }
        val raspberryMain by getting {
            dependencies {
            }
        }
        val raspberryTest by getting {
            dependencies {
            }
        }
    }
}
