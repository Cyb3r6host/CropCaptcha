package me.magas8.cropcaptcha.Commands;

import me.magas8.cropcaptcha.CropCaptcha;
import me.magas8.cropcaptcha.Guis.CaptchaGUI;
import me.magas8.cropcaptcha.Utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class command implements CommandExecutor {
    private CropCaptcha plugin;
    public command(CropCaptcha plugin) {
        this.plugin = plugin;
        plugin.getCommand("cropcaptcha").setExecutor(this);
        plugin.getCommand("cc").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("cropcaptcha") || command.getName().equalsIgnoreCase("cc")) {
            if (args.length < 1) {
                sender.sendMessage(utils.color(plugin.getConfig().getString("command-usage")));
                return true;
            }
            if(args[0].equalsIgnoreCase("reload")){
                if(sender.hasPermission("cropcaptcha.reload")) {
                    plugin.reloadConfig();
                    sender.sendMessage(utils.color(plugin.getConfig().getString("command-reload")));
                }else{
                    sender.sendMessage(utils.color(plugin.getConfig().getString("command-noperm")));
                }
            }else{
                if(sender.hasPermission("cropcaptcha.use")) {
                    Player player = Bukkit.getServer().getPlayer(args[0]);
                    if (player != null) {
                        new CaptchaGUI(plugin).open(player);
                        sender.sendMessage(utils.color(plugin.getConfig().getString("command-open-captcha").replace("%player%", player.getName())));
                    }else{
                        sender.sendMessage(utils.color(plugin.getConfig().getString("command-no-player").replace("%player%", args[0])));

                    }
                }else{
                    sender.sendMessage(utils.color(plugin.getConfig().getString("command-noperm")));
                }
            }
        }
        return true;
    }
}
