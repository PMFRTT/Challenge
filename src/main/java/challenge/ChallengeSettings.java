package challenge;

import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.List;

public class ChallengeSettings {

    public static Inventory Settings;

    public static String inventorySelected;


    public static Inventory prepareInventory(Inventory inventory, String inventoryName) {
        inventory = Bukkit.createInventory(null, 54, Utils.colorize("&6" + inventoryName));

        return inventory;
    }

    public static void displaySettingsInv() {
        inventorySelected = "Settings.Main";
        //Adds Items to Change Settings
        addItem(Material.CHEST_MINECART, 1, "Backpack", 10, new ArrayList<String>() {{
            add("");
            add(Utils.colorize("&7Beschreibung:"));
            add(Utils.colorize("&fErmöglicht den Austausch von"));
            add(Utils.colorize("&fItem zwischen den Spielern!"));
        }}, Settings, ChallengeMain.backpackEnabled);
        addItem(Material.ENDER_PEARL, 1, "Zufälliger Teleport", 12, new ArrayList<String>() {{
            add("");
            add(Utils.colorize("&7Beschreibung:"));
            add(Utils.colorize("&fDie Spieler werden alle"));
            add(Utils.colorize("&f5 - 15 Minuten zu"));
            add(Utils.colorize("&fzufälligen Koordinaten teleportiert"));
        }}, Settings, ChallengeMain.randomTeleportEnabled);
        addItem(Material.SLIME_BALL, 1, "Force Block", 14, new ArrayList<String>() {{
            add("");
            add(Utils.colorize("&7Beschreibung:"));
            add(Utils.colorize("&fDie Spieler müssen alle"));
            add(Utils.colorize("&f5 - 15 Minuten auf einem"));
            add(Utils.colorize("&fzufälligen Block stehen!"));
        }}, Settings, ChallengeMain.forceBlockEnabled);
        addItem(Material.CLOCK, 1, "Zeige Rekord", 16, new ArrayList<String>() {{
            add("");
            add(Utils.colorize("&7Beschreibung:"));
            add(Utils.colorize("&fDer Rekord der Challenge"));
            add(Utils.colorize("&fwird in einer Leiste oben"));
            add(Utils.colorize("&fangezeigt!!"));
        }}, Settings, ChallengeMain.showRecord);
        addItem(Material.GOLDEN_APPLE, 1, "Hardcore", 40, new ArrayList<String>() {{
            add("");
            add(Utils.colorize("&7Beschreibung:"));
            add(Utils.colorize("&fDie Spieler haben"));
            add(Utils.colorize("&fkeine natürliche Regeneration!"));
            add(Utils.colorize("&fRegeneration ist aber durch andere"));
            add(Utils.colorize("&fMittel möglich!"));
        }}, Settings, ChallengeMain.isHardcore);
        addItem(Material.RED_MUSHROOM, 1, "Split-Herzen", 49, new ArrayList<String>() {{
            add("");
            add(Utils.colorize("&7Beschreibung:"));
            add(Utils.colorize("&fDie Herzen werden zwischen"));
            add(Utils.colorize("&fallen Spielern aufgeteilt!"));
        }}, Settings, ChallengeMain.isSplitHealth);
        addItem(Material.OAK_SIGN, 1, "Schadens-Anzeige", 31, new ArrayList<String>() {{
            add("");
            add(Utils.colorize("&7Beschreibung:"));
            add(Utils.colorize("&fJeder Schaden, den ein Spieler"));
            add(Utils.colorize("&ferhält wird im Chat angezeigt!"));
        }}, Settings, ChallengeMain.showDamage);
        addItem(Material.PAPER, 1, "Lebens-Anzeige", 53, new ArrayList<String>() {{
            add("");
            add(Utils.colorize("&7Beschreibung:"));
            add(Utils.colorize("&fDas Leben eines jeden Spielers"));
            add(Utils.colorize("&fwird in der Spieler-Liste angezeigt!"));
        }}, Settings, ChallengeMain.showHealthScoreboard);
        addItem(Material.FEATHER, 1, "No-Jump", 28, new ArrayList<String>() {{
            add("");
            add(Utils.colorize("&7Beschreibung:"));
            add(Utils.colorize("&fSobald ein Spieler springt,"));
            add(Utils.colorize("&fbekommt dieser Schaden!"));
        }}, Settings, ChallengeMain.noJump);
        addItem(Material.BARRIER, 1, "Single-Item", 34, new ArrayList<String>() {{
            add("");
            add(Utils.colorize("&7Beschreibung:"));
            add(Utils.colorize("&fJeder Item-Typ darf nur"));
            add(Utils.colorize("&f einmal in allen Inventaren vorhanden sein"));
        }}, Settings, ChallengeMain.singleItem);

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
        List<String> finalLore = new ArrayList<String>();
        ItemStack itemStack = new ItemStack(material, count);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Utils.colorize("&f" + name));


        if (isEnabled) {
            finalLore.add(Utils.colorize("&aAktiviert"));
        } else {
            finalLore.add(Utils.colorize("&cDeaktiviert"));
        }

        finalLore.addAll(lore);
        itemMeta.setLore(finalLore);

        if (isEnabled) {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(slot, itemStack);
    }

}
