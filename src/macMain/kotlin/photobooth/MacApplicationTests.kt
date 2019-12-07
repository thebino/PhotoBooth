package photobooth

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import libui.ktx.TextArea
import libui.ktx.appWindow
import libui.ktx.button
import libui.ktx.onTimer
import libui.ktx.textarea
import libui.ktx.vbox
import platform.posix.ctime
import platform.posix.time
import platform.posix.time_tVar

actual fun initializeGPIO() {
    appWindow(
        title = "Photobooth",
        width = 300,
        height = 200,
        margined = true
    ) {
        this.fullscreen = false

        vbox {

            var scroll = TextArea()
            button("Go!") {
                action {
                    onTimer(1000) {
                        memScoped {
                            val now = alloc<time_tVar>().apply { value = time(null) }
                            val str = ctime(now.ptr)!!.toKString()
                            if (!scroll.disposed) scroll.append(str)
                        }
                        true
                    }

                    scroll.visible = false
                }
            }

            scroll = textarea {
                readonly = true
                stretchy = true
            }

        }
    }
}

actual fun initializeButtons(buttonHandler: ButtonHandler) {
}

actual fun initializePWM() {
}
