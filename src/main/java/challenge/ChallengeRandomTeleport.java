package challenge;

import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Random;

public class ChallengeRandomTeleport {

    private static int range = 1000;

    public static void teleportPlayerRandomly(Player player) {

        player.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("Du wurdest zu &bzufälligen &fKoordinaten teleportiert!"));


        ChallengeMain.hasBeenTeleported.add(player);
        player.teleport(floorTeleportValues(player));

    }

    public static void setRange(int newRange) {
        range = newRange;
    }

    private static Location floorTeleportValues(Player player) {
        Random random = new Random();

        float tempX = random.nextFloat();

        if (tempX + .3 >= 1) {
            tempX = 1;
        } else {
            tempX += .3;
        }

        float tempZ = random.nextFloat();

        if (tempZ + .3 >= 1) {
            tempZ = 1;
        } else {
            tempZ += .3;
        }

        tempZ = (tempZ * 2) - 1;
        tempX = (tempX * 2) - 1;


        float x = tempX * range;
        float z = tempZ * range;

        Location currentLocation = player.getLocation();

        World world = Bukkit.getWorld("world");

        Location teleportToLocation = currentLocation.add(x, 0, z);
        Block highestBlockAtLocation = world.getHighestBlockAt(teleportToLocation);
        teleportToLocation.setY(highestBlockAtLocation.getY() + 25);

        return teleportToLocation;
    }


}
