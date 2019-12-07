@file:Suppress("EXPERIMENTAL_API_USAGE")

package photobooth

import kotlinx.cinterop.*
import pca9685.PCA9685_initPWM
import pca9685.PCA9685_openI2C
import pca9685.PCA9685_setPWMVals
import pca9685._PCA9685_CHANS
import pigpio.PI_INPUT
import pigpio.PI_TIME_RELATIVE
import pigpio.gpioInitialise
import pigpio.gpioSetAlertFunc
import pigpio.gpioSetMode
import pigpio.gpioSleep

// GPIO
const val GPIO_LED = 6
const val GPIO_BUTTON = 5

// I2C PWM/Servo driver
const val I2C_SERVO_ADDRESS = 0x40

const val PWM_FREQUENCY = 60
const val SERVO_OFF_ANGLE = 100
const val SERVO_ON_ANGLE = 350

actual fun initializeGPIO() {
    println("Initialize GPIO...")

    if (gpioInitialise() < 0) {
        println("Initialize GPIO... FAILED!")
        return
    }
}

actual fun initializeButtons(buttonHandler: ButtonHandler) {
    val onButtonPressed = staticCFunction<Int, Int, UInt, Unit> { gpio, level, tick ->
        println("pressed button on $gpio ($level) and $tick")
        when (level) {
            0 -> {
//                buttonHandler.buttonPressed(0)

                println("Button on GPIO ($gpio) Pressed down, level 0")
                // TODO perform action here will lead to signal terminated?
            }
            1 -> println("Button on GPIO ($gpio) Released, level 1")
            2 -> println("Button on GPIO ($gpio) timeout, no level change")
        }
    }

    initializeRaspberryButtons(onButtonPressed)
}

actual fun initializePWM() {
    println("Initialize PWM...")
    val adapterNumber = 1
    val fileDescriptor = PCA9685_openI2C(
        adapterNumber.toUByte(),
        I2C_SERVO_ADDRESS.toUByte()
    )

    // TODO check the init result, 0 for success, other value for failure
    val result = PCA9685_initPWM(
        fileDescriptor,
        I2C_SERVO_ADDRESS.toUByte(),
        PWM_FREQUENCY.toUInt()
    )
    if (result != 0) {
        println("Initialize PWM... FAILED!")
        return
    }

    setPWMValues(fileDescriptor)
}

private fun initializeRaspberryButtons(onButtonPressed: pigpio.gpioAlertFunc_t?) {
    val buttonPort = GPIO_BUTTON.toUInt()
    initPortWithMode(buttonPort, PI_INPUT)

    gpioSetAlertFunc(buttonPort, onButtonPressed)
}

private fun initPortWithMode(port: UInt, mode: Int) {
    if (gpioSetMode(port, mode.toUInt()) < 0) {
        println("Could not set mode for GPIO$port")
        return
    }
}

fun setPWMValues(fileDescriptor: Int) {
    memScoped {
        val onValues = allocArray<UIntVar>(_PCA9685_CHANS)
        val offValues = allocArray<UIntVar>(_PCA9685_CHANS)

        for (i in 0 until _PCA9685_CHANS - 1) {
            onValues[i] = SERVO_ON_ANGLE.toUInt()
        }
        for (i in 0 until _PCA9685_CHANS - 1) {
            offValues[i] = SERVO_OFF_ANGLE.toUInt()
        }

        PCA9685_setPWMVals(fileDescriptor, I2C_SERVO_ADDRESS.toUByte(), onValues, offValues)

        // calibrate servomotors
        var calibrationCount = 3
        while (calibrationCount > 0) {
            PCA9685_setPWMVals(fileDescriptor, I2C_SERVO_ADDRESS.toUByte(), offValues, onValues)
            gpioSleep(PI_TIME_RELATIVE, 1, 0)
            PCA9685_setPWMVals(fileDescriptor, I2C_SERVO_ADDRESS.toUByte(), onValues, offValues)
            calibrationCount--
        }
    }
}
