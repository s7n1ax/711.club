package me.bush.cornerstore.util.system

import me.bush.cornerstore.util.lang.logExceptions
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

private val clipboard = Toolkit.getDefaultToolkit().systemClipboard
var Clipboard
    get() = logExceptions("Exception thrown while attempting to get system clipboard") {
        clipboard.getData(DataFlavor.stringFlavor) as String
    }.getOrNull()
    set(value) {
        clipboard.setContents(StringSelection(value), null)
    }
