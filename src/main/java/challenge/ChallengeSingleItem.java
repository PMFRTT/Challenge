package challenge;

import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ChallengeSingleItem {

    ChallengeMain main;

    public ChallengeSingleItem(ChallengeMain main) {
        this.main = main;
    }

    HashMap<Player, Inventory> playerInventoryHashMap = new HashMap<Player, Inventory>();


    public void singleItemChecker() {

        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (ChallengeMain.started && !ChallengeMain.paused) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        playerInventoryHashMap.put(player, player.getInventory());
                    }

                    for (Map.Entry<Player, Inventory> entry : playerInventoryHashMap.entrySet()) {
                        for (ItemStack i : entry.getValue().getContents()) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player != entry.getKey()) {
                                    for (ItemStack itemStack : Arrays.asList(playerInventoryHashMap.get(player).getContents())) {
                                        if (i != null && itemStack != null) {
                                            if (i.getType() == itemStack.getType()) {
                                                ChallengeMain.ChallengeFailed(player);
                                                cancel();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }.runTaskTimer(main, 0, 5);


    }


}
