package me.bush.cornerstore.impl.module.modules.client

import me.bush.cornerstore.impl.module.Module

/**
 * @author bush
 * @since 2/22/2022
 */
object Config : Module("Manage client configuration profiles") {
    //todo finish configs lazy fuck

//    private val save = ActionSetting("Save Config") {
//        ConfigManager.saveAll()
//        message("{} saved.", current.value)
//    }
//    private val load = ActionSetting("Load Config") {
//        ConfigManager.loadAll()
//        message("{} loaded.", current.value)
//    }
//    private val refresh = ActionSetting("Refresh Files") {
//        current.syncWith(ConfigManager.configs)
//        ConfigManager.refresh()
//        message("Refreshed filesystem.")
//    }
//    private val current = ModeSetting("Current", *ConfigManager.configs.toTypedArray())
//        .onChange { ConfigManager.current = it }.setTransient().setDefault(ConfigManager.current)
//    private val create = StringSetting("Create Config", "").onChange(::handleAdd).setTransient()
//    private val delete = StringSetting("Delete Config", "").onChange(::handleDelete).setTransient()
//    private val autoSave = BooleanSetting("Auto Save", false).withInfo("Automatically saves configs every 60 seconds").onChange(::launchSaveThread)
//
//    override val toggleable = false
//
//    private fun launchSaveThread(start: Boolean) {
//        if (start) Background.launch {
//            while (autoSave.value) {
//                delay(60_000)
//                ConfigManager.saveAll()
//            }
//        }
//    }
//
//    private fun handleAdd(config: String) {
//        if (config == "") return
//        create.value = ""
//        if (config in ConfigManager.configs) {
//            message("{} already exists.", config)
//            return
//        }
//        if (invalid(config)) return
//        current.addValue(config)
//        current.value = config
//        ConfigManager.saveAll()
//        message("{} created.", config)
//    }
//
//    private fun handleDelete(config: String) {
//        if (config == "") return
//        delete.value = ""
//        val configs = ConfigManager.configs
//        if (invalid(config)) return
//        when {
//            config !in configs -> message("No config found with the name {}.", config)
//            configs.size == 1 -> message("Cannot delete the only config. Try adding another first.")
//            else -> {
//                current.removeValue(config)
//                ConfigManager.delete(config)
//                message("{} deleted.", config)
//                if (current.value == config) current.iterate()
//            }
//        }
//    }
//
//    private fun invalid(profile: String) = profile.isValidPath.also {
//        if (!it) message("{} is not a valid file name.", profile)
//    }.not() // I don't know if "!" inverts the lambda parameter, and I don't care enough to test it
}
