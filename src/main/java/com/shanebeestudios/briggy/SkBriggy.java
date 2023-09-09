package com.shanebeestudios.briggy;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.util.Version;
import com.shanebeestudios.briggy.api.util.Utils;
import com.shanebeestudios.skbee.SkBee;
import com.shanebeestudios.skbee.api.nbt.NBTApi;
import com.shanebeestudios.skbee.api.nbt.NBTContainer;
import com.shanebeestudios.skbee.api.nbt.utils.MinecraftVersion;
import com.shanebeestudios.skbee.config.Config;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SkBriggy extends JavaPlugin {

    @Override
    public void onLoad() {
        CommandAPIBukkitConfig commandAPIBukkitConfig = new CommandAPIBukkitConfig(this);
        if (Bukkit.getPluginManager().getPlugin("SkBee") != null && MinecraftVersion.getVersion() != MinecraftVersion.UNKNOWN) {
            commandAPIBukkitConfig.initializeNBTAPI(NBTContainer.class, NBTContainer::new);
        }
        CommandAPI.onLoad(commandAPIBukkitConfig.verboseOutput(false));

    }

    public static boolean HAS_SKBEE_COMPONENT;
    public static boolean HAS_SKBEE_NBT;

    @SuppressWarnings("deprecation")
    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        Utils.log("Starting up SkBriggy!!!");

        if (Bukkit.getPluginManager().getPlugin("SkBee") instanceof SkBee skbee) {
            Config skBeeConfig = skbee.getPluginConfig();
            if (skBeeConfig.ELEMENTS_TEXT_COMPONENT) {
                HAS_SKBEE_COMPONENT = true;
                Utils.log("&5SkBee Text Components &asuccessfully hooked");
            }
            if (skBeeConfig.ELEMENTS_NBT && NBTApi.isEnabled()) {
                HAS_SKBEE_NBT = true;
                Utils.log("&5SkBee NBT &asuccessfully hooked");
            }
        }

        if (Skript.getVersion().isSmallerThan(new Version(2,7))) {
            Utils.log("&cOutdated Skript Version: &e" + Skript.getVersion());
            Utils.log("&eSkript 2.7+ is required for SkBriggy to run.");
        }

        if (Skript.isAcceptRegistrations()) {
            SkriptAddon skriptAddon = Skript.registerAddon(this);
            try {
                skriptAddon.loadClasses("com.shanebeestudios.briggy.skript");
                skriptAddon.setLanguageFileDirectory("lang");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Utils.log("&cSkript isn't accepting registrations?!?!?");
        }

        // Beta check + notice
        String version = getDescription().getVersion();
        if (version.contains("-")) {
            Utils.log("&eThis is a BETA build, things may not work as expected, please report any bugs on GitHub");
            Utils.log("&ehttps://github.com/ShaneBeee/SkBriggy/issues");
        }

        CommandAPI.onEnable();
        long finish = System.currentTimeMillis() - start;
        Utils.log("Finished loading in &b" + finish + "ms");
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }

}
