package me.magas8.cropcaptcha;

import fr.minuskube.inv.InventoryManager;
import me.magas8.cropcaptcha.Commands.command;
import me.magas8.cropcaptcha.Listeners.CropListener;
import me.magas8.cropcaptcha.Utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class CropCaptcha extends JavaPlugin {
    public static ArrayList<Material> detectionItems = new ArrayList<>();
    public static HashMap<Player,Long> cropCounter = new HashMap<>();

    private static InventoryManager invManager;

    @Override
    public void onEnable() {
        invManager = new InventoryManager(this);
        invManager.init();
        this.saveDefaultConfig();
        this.saveConfig();
        utils.loadCropMaterials();
        new CropListener(this);
        new command(this);
        Bukkit.getServer().getOnlinePlayers().forEach(player->{
            cropCounter.put(player,Long.valueOf("0"));
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static InventoryManager getInvManager() {
        return invManager;
    }

}
