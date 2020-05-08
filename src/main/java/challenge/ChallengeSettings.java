package challenge;

import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.List;

public class ChallengeSettings {

    public static Inventory Settings;

    public static String inventorySelected;


    public static ItemStack healthSettings = new ItemStack(Material.RED_DYE, 1);
    public static ItemMeta healthSettingsMeta = healthSettings.getItemMeta();

    public static ItemStack ultraHardcore = new ItemStack(Material.GOLDEN_APPLE, 1);
    public static ItemMeta ultraHardcoreMeta = ultraHardcore.getItemMeta();

    public static ItemStack amountOfLife = new ItemStack(Material.RED_DYE, 10);
    public static ItemMeta amountOfLifeMeta = amountOfLife.getItemMeta();


    public static Inventory prepareInventory(Inventory inventory, String inventoryName) {
        inventory = Bukkit.createInventory(null, 54, Utils.colorize("&6" + inventoryName));

        return inventory;
    }

    public static void displaySettingsInv() {
        inventorySelected = "Settings.Main";
        //Adds Items to Change Settings

        addItem(Material.CHEST_MINECART, 1, "Backpack", 10, null, Settings, ChallengeMain.backpackEnabled);
        addItem(Material.ENDER_PEARL, 1, "Zufälliger Teleport", 12, null, Settings, ChallengeMain.randomTeleportEnabled);
        addItem(Material.SLIME_BALL, 1, "Force Block", 14, null, Settings, ChallengeMain.forceBlockEnabled);
        addItem(Material.CLOCK, 1, "Zeige Rekord", 16, null, Settings, ChallengeMain.showRecord);
        addItem(Material.GOLDEN_APPLE, 1, "Hardcore", 40, null, Settings, ChallengeMain.isHardcore);
        addItem(Material.RED_MUSHROOM, 1, "Split-Herzen", 49, null, Settings, ChallengeMain.isSplitHealth);
        addItem(Material.OAK_SIGN, 1, "Schadens-Anzeige", 31, null, Settings, ChallengeMain.showDamage);


        //Fills up empty slots
        for (int i = 0; i < 54; i++) {
            if (Settings.getItem(i) == null) {
                ItemStack empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                ItemMeta emptyMeta = empty.getItemMeta();
                emptyMeta.setDisplayName("--");
                empty.setItemMeta(emptyMeta);
                Settings.setItem(i, empty);
            }
        }
    }


    public static void addItem(Material material, int count, String name, int slot, List<String> lore, Inventory inventory, boolean isEnabled) {
        ItemStack itemStack = new ItemStack(material, count);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        if (isEnabled) {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(slot, itemStack);
    }

    public static void addItem(Material material, int count, String name, Boolean enchanted, int slot, List<String> lore, Inventory inventory) {
        ItemStack itemStack = new ItemStack(material, count);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        if (lore != null) {
            itemMeta.setLore(lore);
        }
        if (enchanted) {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(slot, itemStack);
    }

}
