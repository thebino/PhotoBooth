package photobooth

fun hello(): String = "Hello, Kotlin/Native"

expect fun initializeGPIO()

expect fun initializePWM()

expect fun initializeButtons(buttonHandler: ButtonHandler)

fun main() {
    initializeGPIO()

    initializePWM()

    val buttonHandler = object : ButtonHandler{
        override fun buttonPressed(buttonId: Int) {
            //
        }
    }

    initializeButtons(buttonHandler)
}

interface ButtonHandler {
    fun buttonPressed(buttonId: Int)
}
