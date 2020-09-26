package me.magas8.cropcaptcha.Listeners;

import com.cryptomorin.xseries.XMaterial;
import me.magas8.cropcaptcha.CropCaptcha;
import me.magas8.cropcaptcha.Guis.CaptchaGUI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class CropListener implements Listener {
    private CropCaptcha plugin;
    public CropListener(CropCaptcha plugin){
        this.plugin=plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        CropCaptcha.cropCounter.put(e.getPlayer(),Long.valueOf("0"));
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        CropCaptcha.cropCounter.remove(e.getPlayer());
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e){
        if(CropCaptcha.getInvManager().getInventory(e.getPlayer()).isPresent() && CropCaptcha.getInvManager().getInventory(e.getPlayer()).get().getId().equalsIgnoreCase("captcha")) e.setCancelled(true);
        if(e.getBlock()!= null && e.getBlock().getType()!=null && CropCaptcha.detectionItems.contains(XMaterial.matchXMaterial(e.getBlock().getType()).parseMaterial()) && !e.getPlayer().hasPermission("cropcaptcha.bypass")){
            CropCaptcha.cropCounter.put(e.getPlayer(),CropCaptcha.cropCounter.get(e.getPlayer())+1);
        }
        if(CropCaptcha.cropCounter.get(e.getPlayer())>plugin.getConfig().getLong("captcha-on-break-number-min") && CropCaptcha.cropCounter.get(e.getPlayer())<plugin.getConfig().getLong("captcha-on-break-number-max")){
            Double random = new Random().nextDouble()* 100.0D;
            Double chance = 100.0D-plugin.getConfig().getDouble("captcha-chance");
            if(random > chance){
                new CaptchaGUI(plugin).open(e.getPlayer());
            }
        }else if(CropCaptcha.cropCounter.get(e.getPlayer())>plugin.getConfig().getLong("captcha-on-break-number-max")){
            new CaptchaGUI(plugin).open(e.getPlayer());
        }

    }

}
