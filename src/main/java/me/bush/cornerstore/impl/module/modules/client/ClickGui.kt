package me.bush.cornerstore.impl.module.modules.client

import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.api.gui.Theme
import me.bush.cornerstore.api.load.LoadHandler.awaitPrimary
import me.bush.cornerstore.api.setting.*
import me.bush.cornerstore.api.setting.settings.*
import me.bush.cornerstore.impl.gui.clickgui.ClickGuiScreen
import me.bush.cornerstore.impl.module.Module
import me.bush.cornerstore.util.lang.Background
import org.lwjgl.input.Keyboard

/**
 * @author bush
 * @since 2/18/2022
 */
object ClickGui : Module(
    """
     
     Bitch, you know it's Lil Boat
     Bitch, it's Lil Boat and Lil Perry (Perry)
     I keep them hoes like a ferry
     My hair be red like a cherry
     All of them niggas, they know
     All of them niggas, they know me
     All of them niggas, they know us (whoa, whoa, whoa, whoa)
     Them niggas, they wanna be us, whoa, damn
     
     Bitch, you know it's Lil Boat
     Bitch, it's Lil Boat and Lil Perry
     I keep them hoes like a ferry
     My hair be red like a cherry
     All of them niggas, they know
     All of them niggas, they know me
     All of them niggas, they know us
     Them niggas, they wanna be us, whoa, damn
     
     Shoutout to my nigga Colby (whoa)
     Gold on my neck like a trophy (whoa)
     Lil Boat powered up like a Mophie (whoa)
     Lil Boat, that nigga stay low-key (oh, whoa)
     All of these bitches, they know me (damn)
     All of these bitches, they on me (oh, damn)
     That bitch, she smile like emoji (emoji)
     That bitch, she smile like emoji, whoa, damn
     
     I be posted up with Perry
     I be counting up the guap
     Keep a condom in my sock
     'Cause these bitches on my cock
     I need a mouth full of rocks (yeah)
     And I need me a Rollie
     Need a mansion for my brodies (whoa)
     Need my diamonds to be dancin' (whoa, damn)
     
     Bitch, you know it's Lil Boat
     Bitch, it's Lil Boat and Lil Perry (Perry)
     I keep them hoes like a ferry
     My hair be red like a cherry
     All of them niggas, they know
     All of them niggas, they know me
     All of them niggas, they know us (whoa, whoa, whoa, whoa)
     Them niggas, they wanna be us, whoa, damn
     
     Bitch, you know it's Lil Boat
     Bitch, it's Lil Boat and Lil Perry
     I keep them hoes like a ferry
     My hair be red like a cherry
     All of them niggas, they know
     All of them niggas, they know me
     All of them niggas, they know us
     Them niggas, they wanna be us
     
     All of these niggas, they know (they know)
     I need my bitch like a Reese's (Reese's)
     Got your bitch wet like the Ganges
     Walkin' on water like Jesus (like Jesus)
     Bitches, they changin' like seasons (oh)
     I need like five for my leases (whoa)
     I got ice on my wrist (whoa)
     Whipping the work like a Prius
     None of these niggas can see us (nah)
     
     I'm up in the Himalayas
     Bitches they trying to lay us
     I hit that bitch, then I lay up (woo)
     Put it in like a finger roll
     Hit that bitch and then I troll
     Can't have one bitch, I need like four
     They scream my name at the shows
     
     Bitch, you know it's Lil Boat
     Bitch, it's Lil Boat and Lil Perry (Perry)
     I keep them hoes like a ferry
     My hair be red like a cherry
     All of them niggas, they know
     All of them niggas, they know me
     All of them niggas, they know us (whoa, whoa, whoa, whoa)
     Them niggas, they wanna be us, whoa, damn
     
     Bitch, you know it's Lil Boat
     Bitch, it's Lil Boat and Lil Perry
     I keep them hoes like a ferry
     My hair be red like a cherry
     All of them niggas, they know
     All of them niggas, they know me
     All of them niggas, they know us
     Them niggas, they wanna be us
     
     """.trimIndent()
) {
    // Colors
    val color = ColorSetting("Main Color", 87, 29, 129, 240)
    val outline = ColorSetting("Outline", 0, 0, 0, 240)
    val bgColor = ColorSetting("Bg. Color", 0, 0, 0, 146)
    val colors = ParentSetting("Colors", color, outline, bgColor)
    // Appearance
    val theme = ModeSetting("Theme", *Theme.themes.toTypedArray()).onChange { Theme.setTheme(it) }.withFormat { Theme.current.name }
    val width = IntSetting("Column Width", 104, 50, 200)
    val alignMode = ModeSetting("Align Mode", "Default", "Center")
    private val resetComponents = ActionSetting("Realign All") { ClickGuiScreen.alignComponents() }
    val backgroud = ModeSetting("Backgroud", "None", "Blur", "Dirt", "Default", "Gradient")
    val gradientStart = ColorSetting("Gradient Start", 100, 10, 210, 120).withVis { backgroud.isValue("Gradient") }
    val gradientEnd = ColorSetting("Gradient End", 220, 20, 80, 120).withVis { backgroud.isValue("Gradient") }
    val vertical = BooleanSetting("Vertical", false).withVis { backgroud.isValue("Gradient") }
    val mintMode = BooleanSetting("Mint Mode", false).withInfo("Funny bug in the main menu forge config screen")
    val hotbar = BooleanSetting("Hide Hotbar", false)
    val appearance = ParentSetting("Appearance", theme, width, alignMode, resetComponents, backgroud, gradientStart, gradientEnd, vertical, mintMode, hotbar)
    // Feel
    val pickerMode = ModeSetting("Picker Mode", "Hue/Sat", "Hue/Bright", "Sat/Bright").withInfo("Which HSB values should be on the color picker rectangle. The third value will be on a slider, along with alpha.")
    val closeOnEscape = BooleanSetting("ESC Close", true).withInfo("Close gui when escape key is pressed")
    val sounds = BooleanSetting("Sounds", true)
    val globalScroll = BooleanSetting("Global Scroll", false).withInfo("Scroll every component, rather than the one your mouse is over.")
    val resetScroll = BooleanSetting("Reset Scroll", true).withInfo("Reset scroll every time you close the gui")
    val invertScroll = BooleanSetting("Invert Scroll", false)
    val scrollSpeed = FloatSetting("Scroll Speed", 1f, 0.1f, 2f)
    val scroll = ParentSetting("Scrolling", globalScroll, resetScroll, invertScroll, scrollSpeed)
    val feel = ParentSetting("Feel", pickerMode, closeOnEscape, sounds, scroll)

    override val transient = true

    init {
        // Will be overridden if there is a config
        bind = Keyboard.KEY_RSHIFT
    }

    override fun onEnable() {
        // Instantiated in main so gui doesn't have to reload every time u open it (big brain)
        mc.displayGuiScreen(ClickGuiScreen)
    }

    override fun fromConfig(config: JsonObject) {
        Background.launch {
            ClickGuiScreen.awaitPrimary().fromJson(config)
        }
    }

    override fun toConfig(config: JsonObject) {
        ClickGuiScreen.toJson(config)
    }
}
