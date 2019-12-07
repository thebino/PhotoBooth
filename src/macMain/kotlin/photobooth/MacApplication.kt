package photobooth

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import libui.ktx.MsgBox
import libui.ktx.MsgBoxError
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
        this.borderless = false
        this.fullscreen = false

        vbox {
            val scroll = textarea {
                readonly = true
                stretchy = true
            }
            button("Settings") {
                action {
                    MsgBox("open settings", "Not implemented yet!")
                }
            }

            button("START") {
                action {
                    MsgBoxError("start timer", "Not implemented yet!")
                    this.enabled = false

                    onTimer(1000) {
                        memScoped {
                            val now = alloc<time_tVar>().apply { value = time(null) }
                            val str = ctime(now.ptr)!!.toKString()
                            if (!scroll.disposed) {
                                scroll.append(str)
                            }
                        }
                        true
                    }
                }
            }
        }
    }
}

actual fun initializeButtons(buttonHandler: ButtonHandler) {
}

actual fun initializePWM() {
}
