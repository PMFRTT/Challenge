package challenge;

import core.CoreSendStringPacket;
import core.Utils;
import net.minecraft.server.v1_15_R1.ItemBoneMeal;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static challenge.ChallengeMain.*;

public class ChallengeListener implements Listener {

    private static double damageTaken = 0;

    ChallengeMain main;

    public ChallengeListener(ChallengeMain main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == ChallengeSettings.Settings) {

            if (ChallengeSettings.inventorySelected == "Settings.Main") {
                if (e.getSlot() == 10) {
                    e.setCancelled(true);
                    if (backpackEnabled) {
                        backpackEnabled = false;
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Backpack", false);
                    } else {
                        backpackEnabled = true;
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Backpack", true);
                    }
                }

                if (e.getSlot() == 12) {
                    if (!randomTeleportEnabled) {
                        e.setCancelled(true);
                        randomTeleportEnabled = true;
                        ChallengeSettings.displaySettingsInv();
                        secondsUntilTeleport = ChallengeMain.getNewTeleportTime();
                        settingsChangeMessage("Random-Teleport", true);
                    } else {
                        e.setCancelled(true);
                        randomTeleportEnabled = false;
                        ChallengeSettings.displaySettingsInv();
                        secondsUntilTeleport = -1;
                        settingsChangeMessage("Random-Teleport", false);
                    }
                }

                if (e.getSlot() == 14) {
                    if (!forceBlockEnabled) {
                        e.setCancelled(true);
                        forceBlockEnabled = true;
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Force-Block", true);
                    } else {
                        e.setCancelled(true);
                        forceBlockEnabled = false;
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Force-Block", false);
                    }
                }

                if (e.getSlot() == 16) {
                    if (!showRecord) {
                        e.setCancelled(true);
                        showRecord = true;
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Zeige Rekord", true);
                        recordTimeBar.setVisible(true);
                        ChallengeFileReader.readDataFile();
                        recordTimeBar.setTitle(Utils.colorize("Euer &bRekord &fist &b" + Utils.formatTimerTime(recordInSeconds)));
                    } else {
                        e.setCancelled(true);
                        showRecord = false;
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Zeige Rekord", false);
                        recordTimeBar.setVisible(false);
                        ChallengeFileReader.readDataFile();
                        recordTimeBar.setTitle(Utils.colorize("Euer &bRekord &fist &b" + Utils.formatTimerTime(recordInSeconds)));
                    }
                }

                if (e.getSlot() == 40) {
                    if (!isHardcore) {
                        isHardcore = true;
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Hardcore", true);
                        Utils.changeGamerule(GameRule.NATURAL_REGENERATION, false);
                    } else {
                        isHardcore = false;
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Hardcore", false);
                        Utils.changeGamerule(GameRule.NATURAL_REGENERATION, true);
                    }
                }

                if (e.getSlot() == 49) {
                    if (!isSplitHealth) {
                        if (isHardcore) {
                            isSplitHealth = true;
                            ChallengeSettings.displaySettingsInv();
                            settingsChangeMessage("Split-Herzen", true);
                        } else {
                            e.getWhoClicked().sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("&cSplit-Herzen funktioniert nur, wenn Hardcore an ist!"));
                        }
                    } else {
                        isSplitHealth = false;
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Split-Herzen", false);
                    }
                }

                if (e.getSlot() == 31) {
                    if (!showDamage) {
                        showDamage = true;
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Schadensanzeige", true);
                    } else {
                        showDamage = false;
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Schadensanzeige", false);
                    }
                }

                if (e.getSlot() == 53) {
                    if (!showHealthScoreboard) {
                        showHealthScoreboard = true;
                        Utils.createHealthDisplay();
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Lebensanzeige", true);
                    } else {
                        showHealthScoreboard = false;
                        Utils.createHealthDisplay();
                        ChallengeSettings.displaySettingsInv();
                        settingsChangeMessage("Lebensanzeige", false);
                    }
                }


            } else if (ChallengeSettings.inventorySelected == "Settings.Health") {

            }
            e.setCancelled(true);
        } /**else if (e.getClickedInventory() == ChallengeSettings.Health) {
         if (e.getSlot() == 10) {
         e.setCancelled(true);
         if (e.getClick().isLeftClick()) {
         if (ChallengeSettings.amountOfLife.getAmount() == 15) {
         changeHealth(40);
         } else if (ChallengeSettings.amountOfLife.getAmount() == 10) {
         changeHealth(30);
         } else if (ChallengeSettings.amountOfLife.getAmount() == 5) {
         changeHealth(20);
         } else if (ChallengeSettings.amountOfLife.getAmount() == 1) {
         changeHealth(10);
         }
         } else if (e.getClick().isRightClick()) {
         if (ChallengeSettings.amountOfLife.getAmount() == 20) {
         changeHealth(30);
         } else if (ChallengeSettings.amountOfLife.getAmount() == 15) {
         changeHealth(20);
         } else if (ChallengeSettings.amountOfLife.getAmount() == 10) {
         changeHealth(10);
         } else if (ChallengeSettings.amountOfLife.getAmount() == 5) {
         changeHealth(1);
         }
         }
         } else if (e.getSlot() == 13) {
         e.setCancelled(true);
         if (!ultraHardcore) {
         world.setGameRule(GameRule.NATURAL_REGENERATION, false);
         ChallengeSettings.ultraHardcoreMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
         ultraHardcore = true;

         } else {
         world.setGameRule(GameRule.NATURAL_REGENERATION, true);
         ChallengeSettings.ultraHardcoreMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
         ultraHardcore = false;
         }

         }
         }*/

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        showParticles.put(e.getPlayer(), true);


        for (Player player : Bukkit.getOnlinePlayers()) {
            recordTimeBar.addPlayer(player);
        }

        if (!started) {
            ItemStack settings = new ItemStack(Material.REDSTONE);
            ItemMeta settingsMeta = settings.getItemMeta();

            settingsMeta.setDisplayName(Utils.colorize("&cEinstellungen"));
            settings.setItemMeta(settingsMeta);

            ItemStack start = new ItemStack(Material.LIME_DYE);
            ItemMeta startMeta = start.getItemMeta();

            startMeta.setDisplayName(Utils.colorize("&aStart"));
            start.setItemMeta(startMeta);

            ItemStack particles = new ItemStack(Material.GHAST_TEAR);
            ItemMeta particlesMeta = particles.getItemMeta();

            particlesMeta.setDisplayName(Utils.colorize("&dPartikel"));
            particles.setItemMeta(particlesMeta);

            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().setItem(8, start);
            e.getPlayer().getInventory().setItem(4, settings);
            e.getPlayer().getInventory().setItem(0, particles);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!started || paused) {
            Player player = e.getPlayer();
            if (player.getItemInHand().getType().equals(Material.REDSTONE)) {
                e.setCancelled(true);
                ChallengeSettings.Settings = ChallengeSettings.prepareInventory(ChallengeSettings.Settings, "Challenge-Settings");
                ChallengeSettings.displaySettingsInv();
                player.openInventory(ChallengeSettings.Settings);
            }
            if (player.getItemInHand().getType().equals(Material.LIME_DYE)) {
                ChallengeMain.started = true;
                for (Player p : players) {
                    p.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("Die Challenge wurde gestartet! " + Utils.getRainbowString("Viel Glück!")));
                    p.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("Der aktuelle &bRekord&f liegt bei &b") + Utils.formatTimerTime(ChallengeMain.recordInSeconds));
                    p.setGameMode(GameMode.SURVIVAL);
                    Utils.heal(p);
                    p.getInventory().clear();
                }
            }
            if (player.getItemInHand().getType().equals(Material.GHAST_TEAR)) {
                e.setCancelled(true);
                if (showParticles.get(player)) {
                    showParticles.put(player, false);
                    player.sendMessage(Utils.getPrefix("Partikel") + Utils.colorize("Es werden nun keine &dPartikel &fmehr angezeigt!"));
                } else {
                    showParticles.put(player, true);
                    player.sendMessage(Utils.getPrefix("Partikel") + Utils.colorize("Es werden nun &dPartikel&f angezeigt!"));
                }
            }
        }
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (paused || !started) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("&cDu kannst die Welt nicht verändert, während die Challenge pausiert ist!"));
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (paused || !started) {
            if (e.getEntity().getType().equals(EntityType.CREEPER)) {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public static void playerDamage(EntityDamageEvent e) {

        if (paused || !started) {
            e.setCancelled(true);
        } else {

            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (!hasBeenTeleported.contains(p)) {
                    if (p.getHealth() - e.getDamage() < .1) {
                        e.setCancelled(true);
                        ChallengeFailed(p);
                    }
                }
            }

            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                if (e.getEntity() instanceof Player) {
                    if (hasBeenTeleported.contains(e.getEntity())) {
                        e.setCancelled(true);
                        hasBeenTeleported.remove(e.getEntity());
                    }
                }
            }
        }

    }

    @EventHandler
    public void playerKillsEnderdragon(EntityDeathEvent e) {
        Entity killed = e.getEntity();
        if (killed.getType().equals(EntityType.ENDER_DRAGON)) {
            if (started && !finished && !paused) {
                finished = true;
            }
        }
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent e) {
        if (paused) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void showDamage(EntityDamageEvent e) {
        String damageTaken = this.damageTaken + "❤";
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (showDamage) {

                if (!paused && started) {

                    switch (e.getCause()) {
                        case DROWNING:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Schaden") + Utils.colorize("&b" +
                                    player.getDisplayName() + "&f hat durch &1ERTRINKEN&f Schaden erhalten (&c" + damageTaken + "&f)"));
                            break;
                        case FALL:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Schaden") + Utils.colorize("&b" +
                                    player.getDisplayName() + "&f hat durch einen &eFALL&f Schaden erhalten (&c" + damageTaken + "&f)"));
                            break;
                        case FIRE:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Schaden") + Utils.colorize("&b" +
                                    player.getDisplayName() + "&f hat durch &4FEUER&f Schaden erhalten (&c" + damageTaken + "&f)"));
                            break;
                        case LAVA:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Schaden") + Utils.colorize("&b" +
                                    player.getDisplayName() + "&f hat durch &4LAVA&f Schaden erhalten (&c" + damageTaken + "&f)"));
                            break;
                        case MAGIC:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Schaden") + Utils.colorize("&b" +
                                    player.getDisplayName() + "&f hat durch &dMAGIE&f Schaden erhalten (&c" + damageTaken + "&f)"));
                            break;
                        case POISON:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Schaden") + Utils.colorize("&b" +
                                    player.getDisplayName() + "&f hat durch &aVERGIFTUNG&f Schaden erhalten (&c" + damageTaken + "&f)"));
                            break;
                        case PROJECTILE:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Schaden") + Utils.colorize("&b" +
                                    player.getDisplayName() + "&f hat durch ein &7PROJEKTIL&f Schaden erhalten (&c" + damageTaken + "&f)"));
                            break;
                        case FIRE_TICK:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Schaden") + Utils.colorize("&b" +
                                    player.getDisplayName() + "&f hat durch &cVERBRENNUNG&f Schaden erhalten (&c" + damageTaken + "&f)"));
                            break;
                        case ENTITY_EXPLOSION:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Schaden") + Utils.colorize("&b" +
                                    player.getDisplayName() + "&f hat durch eine &cEXPLOSION&f Schaden erhalten (&c" + damageTaken + "&f)"));
                            break;
                        case ENTITY_ATTACK:
                            Entity closest = Utils.getClosestEntity(player, 2);

                            switch (closest.getType()) {

                                case PRIMED_TNT:
                                    break;
                                case CREEPER:
                                    break;
                                case ARROW:
                                    break;


                                case HUSK:
                                    sendColoredEntityString("&e", player, "WÜSTEN-ZOMBIE", damageTaken, "einen");
                                    break;
                                case WOLF:
                                    sendColoredEntityString("&7", player, "WOLF", damageTaken, "einen");
                                    break;
                                case AREA_EFFECT_CLOUD:
                                    sendColoredEntityString("&d", player, "AOE-CLOUD", damageTaken, "die");
                                    break;
                                case ZOMBIE_VILLAGER:
                                    sendColoredEntityString("&a", player, "ZOMBIE-VIILLAGER", damageTaken, "einen");
                                    break;
                                case BLAZE:
                                    sendColoredEntityString("&6", player, "BLAZE", damageTaken, "eine");
                                    break;
                                case GHAST:
                                    sendColoredEntityString("&7", player, "GHAST", damageTaken, "einen");
                                    break;
                                case SLIME:
                                    sendColoredEntityString("&a", player, "SLIME", damageTaken, "einen");
                                    break;
                                case FOX:
                                    sendColoredEntityString("&c", player, "FUCHS", damageTaken, "einen");
                                    break;
                                case WITHER:
                                    sendColoredEntityString("&0", player, "WITHER", damageTaken, "den");
                                    break;
                                case WITHER_SKELETON:
                                    sendColoredEntityString("&0", player, "WITHER-SKELETT", damageTaken, "ein");
                                    break;
                                case DRAGON_FIREBALL:
                                    sendColoredEntityString("&d", player, "DRACHEN-FEUERBALL", damageTaken, "einen");
                                    break;
                                case SMALL_FIREBALL:
                                    sendColoredEntityString("&6", player, "KLEINEN FEUERBALL", damageTaken, "einen");
                                    break;
                                case ELDER_GUARDIAN:
                                    sendColoredEntityString("&3", player, "GROßEN GUARDIAN", damageTaken, "einen");
                                    break;
                                case SHULKER_BULLET:
                                    sendColoredEntityString("&d", player, "SHULKER-KUGEL", damageTaken, "eine");
                                    break;
                                case ENDER_DRAGON:
                                    sendColoredEntityString("&d", player, "ENDERDRACHE", damageTaken, "den");
                                    break;
                                case CAVE_SPIDER:
                                    sendColoredEntityString("&a", player, "HÖHLENSPINNE", damageTaken, "eine");
                                    break;
                                case SILVERFISH:
                                    sendColoredEntityString("&7", player, "SILBERFISCH", damageTaken, "einen");
                                    break;
                                case PLAYER:
                                    sendColoredEntityString("&c", player, "SPIELER", damageTaken, "einen");
                                    break;
                                case ZOMBIE:
                                    sendColoredEntityString("&a", player, "ZOMBIE", damageTaken, "einen");
                                    break;
                                case ENDERMAN:
                                    sendColoredEntityString("&d", player, "ENDERMAN", damageTaken, "einen");
                                    break;
                                case SPIDER:
                                    sendColoredEntityString("&0", player, "SPINNE", damageTaken, "eine");
                                    break;
                            }
                            break;
                        case CONTACT:
                            sendColoredEntityString("&7", player, "KONTAKT", damageTaken, "einen");
                            break;
                        default:
                            sendColoredEntityString(null, player, "UNDEFINIERT", damageTaken, "einen");

                    }

                }
            }

        }

    }

    @EventHandler
    private void splitDamage(EntityDamageEvent e) {
        damageTaken = e.getDamage();
        if (e.getEntity() instanceof Player) {
            if (!paused && started) {
                if (isSplitHealth) {
                    splitHealth -= damageTaken;
                    if (splitHealth > 0) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.setHealth(splitHealth);
                        }
                        e.setDamage(0);
                    } else {
                        ChallengeFailed((Player) e.getEntity());
                    }

                }
            }
        }
    }

    @EventHandler
    private void splitHeal(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!paused && started) {
                if (isSplitHealth) {
                    double healthRegained = e.getAmount();
                    splitHealth += healthRegained;
                    if (splitHealth < 20) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.setHealth(splitHealth);
                        }
                        e.setAmount(0);
                    }

                }
            }
        }
    }

    private static void sendColoredEntityString(String colorCode, Player player, String entity, String damageTaken, String add) {
        if (colorCode == null) {
            Utils.sendMessageToEveryone(Utils.getPrefix("Schaden") + Utils.colorize("&b" + player.getDisplayName() + "&f hat durch " + add + " " + Utils.getRainbowString(entity) + "&f Schaden erhalten (&c" + damageTaken + "&f)"));
        } else {
            Utils.sendMessageToEveryone(Utils.getPrefix("Schaden") + Utils.colorize("&b" + player.getDisplayName() + "&f hat durch " + add + " " + colorCode + entity + "&f Schaden erhalten (&c" + damageTaken + "&f)"));
        }
    }

    private static void settingsChangeMessage(String setting, boolean active) {

        if (active) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                CoreSendStringPacket.sendPacketToTitle(player, setting, Utils.colorize("wurde &aAktiviert"));
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                CoreSendStringPacket.sendPacketToTitle(player, setting, Utils.colorize("wurde &cDeaktiviert"));
            }
        }
    }


}
