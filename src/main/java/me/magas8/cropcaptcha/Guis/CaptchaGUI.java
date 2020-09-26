package me.magas8.cropcaptcha.Guis;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.magas8.cropcaptcha.CropCaptcha;
import me.magas8.cropcaptcha.Managers.ItemBuilder;
import me.magas8.cropcaptcha.Utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;

public class CaptchaGUI implements InventoryProvider {
    private CropCaptcha plugin;
    private SmartInventory captchaInv;
    private String WoolName;
    private Integer attempts;
    private Material realMaterial;
    public CaptchaGUI(CropCaptcha plugin){
        this.plugin=plugin;
        this.captchaInv = SmartInventory.builder()
                .id("captcha")
                .provider(this)
                .size(3, 9)
                .manager(CropCaptcha.getInvManager())
                .title(utils.color(plugin.getConfig().getString("captcha-gui-title")))
                .closeable(false)
                .build();
        this.WoolName = utils.getRandomWool();
        this.attempts=0;
         this.realMaterial= XMaterial.valueOf(WoolName.toUpperCase()).parseMaterial();

    }
    public CaptchaGUI open(Player player){
        captchaInv.open(player);
        return this;
    }
    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemBuilder(XMaterial.valueOf(plugin.getConfig().getString("captcha-gui-fill-item").toUpperCase()).parseItem()).setName(" ").toItemStack()));
        contents.set(0,4,ClickableItem.empty(new ItemBuilder(XMaterial.valueOf(plugin.getConfig().getString("captcha-question-item").toUpperCase()).parseItem()).setColoredName(plugin.getConfig().getString("captcha-question-item-name").replace("%wool%",utils.getRandomColor()+WoolName.split("_")[0]+"&7 "+WoolName.split("_")[1])).toItemStack()));
        ArrayList<ItemStack> items = new ArrayList<>();
        ItemStack realItem = utils.generateRandomWoolItem(utils.getRandomColor(),WoolName);
        for (int i = 0; i < 7; i++) {
            ItemStack item = utils.generateRandomWoolItem(utils.getRandomColor(),utils.getRandomWool());
            while(item.getDurability() == realItem.getDurability()){
               item = utils.generateRandomWoolItem(utils.getRandomColor(),utils.getRandomWool());
           }
            items.add(item);
        }
        int index = utils.getRandomNumberInRange(0,items.size()-1);
        items.set(index,realItem);
        Iterator<ItemStack> itemsIterator = items.iterator();
        while(itemsIterator.hasNext() && contents.firstEmpty().isPresent()){
            contents.set(contents.firstEmpty().get(),ClickableItem.of(itemsIterator.next(), e->{
                if(XMaterial.matchXMaterial(WoolName).get().parseItem().getDurability() == e.getCurrentItem().getDurability()){
                    captchaInv.close((Player) e.getWhoClicked());
                    e.getWhoClicked().sendMessage(utils.color(plugin.getConfig().getString("captcha-pass-check")));
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(),100,2);
                    CropCaptcha.cropCounter.put((Player) e.getWhoClicked(),Long.valueOf("0"));
                }else{
                    this.attempts+=1;
                    e.getWhoClicked().sendMessage(utils.color(plugin.getConfig().getString("captcha-attempt-failure-message")));
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), XSound.BLOCK_NOTE_BLOCK_BASS.parseSound(),100,0);
                    if(attempts>=plugin.getConfig().getInt("captcha-attempts")){
                        CropCaptcha.cropCounter.put((Player) e.getWhoClicked(),Long.valueOf("0"));
                        captchaInv.close((Player) e.getWhoClicked());
                        for(String string : plugin.getConfig().getStringList("captcha-commands-on-failure")){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),string.replace("%player%",e.getWhoClicked().getName()));
                        }
                    }
                }

            }));

        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

}
