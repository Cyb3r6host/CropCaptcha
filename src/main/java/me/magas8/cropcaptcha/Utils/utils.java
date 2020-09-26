package me.magas8.cropcaptcha.Utils;

import com.cryptomorin.xseries.XMaterial;
import me.magas8.cropcaptcha.CropCaptcha;
import me.magas8.cropcaptcha.Managers.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class utils {
    private static CropCaptcha plugin = CropCaptcha.getPlugin(CropCaptcha.class);
    public static List<String> colors =  Arrays.asList("&2","&4","&6","&8","&1","&3","&5","&7","&9","&a","&c","&e","&b","&d","&f");
    public static List<String> WoolColors = Arrays.asList("White","Black","Blue","Brown","Cyan","Gray","Green","Lime","Magenta","Orange","Pink","Purple","Red","Yellow");
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static ItemStack generateRandomWoolItem(String color,String wool){
        return new ItemBuilder(XMaterial.valueOf(wool.toUpperCase()).parseItem()).setColoredName(color+wool.split("_")[0]+"&7 "+wool.split("_")[1]).toItemStack();
    }

    public static String getRandomColor(){
        return colors.get(getRandomNumberInRange(0,colors.size()-1));
    }
    public static String getRandomWool(){
        return WoolColors.get(getRandomNumberInRange(0,WoolColors.size()-1))+"_wool";
    }

    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    public static void loadCropMaterials(){
        for(String material: plugin.getConfig().getStringList("Material-Detection")){
            if(XMaterial.matchXMaterial(material).isPresent()) {
                CropCaptcha.detectionItems.add(XMaterial.matchXMaterial(material.toUpperCase()).get().parseMaterial());
            }
        }
    }
}
