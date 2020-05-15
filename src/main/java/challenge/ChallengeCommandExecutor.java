package challenge;

import core.CoreResetServer;
import core.CoreSendStringPacket;
import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Collection;

public class ChallengeCommandExecutor implements CommandExecutor {

    private ChallengeMain Challenge;
    public static Collection<? extends Player> players = Bukkit.getOnlinePlayers();
    public static BukkitScheduler scheduler = Bukkit.getScheduler();

    public ChallengeCommandExecutor(ChallengeMain Challenge) {
        this.Challenge = Challenge;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (command.getName().equalsIgnoreCase("settings")) {
            ChallengeSettings.Settings = ChallengeSettings.prepareInventory(ChallengeSettings.Settings, "Challenge-Settings");
            ChallengeSettings.displaySettingsInv();
            player.openInventory(ChallengeSettings.Settings);
            return true;
        }


        if (command.getName().equalsIgnoreCase("backpack")) {
            if (ChallengeMain.backpackEnabled) {
                player.openInventory(ChallengeBackpack.updateBackpack());
                return true;
            } else {
                for (Player p : players) {
                    player.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("&cDas Backpack ist deaktiviert!"));
                }
                return false;
            }
        }

        if (command.getName().equalsIgnoreCase("reset")) {
            CoreResetServer.resetServer("Challenge", true);
        }

        if (command.getName().equalsIgnoreCase("Timer")) {
            if (args[0].equalsIgnoreCase("start")) {
                ChallengeMain.started = true;
                for (Player p : players) {
                    p.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("Die Challenge wurde gestartet! " + Utils.getRainbowString("Viel Glück!")));
                    p.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("Der aktuelle &bRekord&f liegt bei &b") + Utils.formatTimerTime(ChallengeMain.recordInSeconds));
                    p.setGameMode(GameMode.SURVIVAL);
                    CoreSendStringPacket.sendPacketToTitle(p, "Challenge", Utils.colorize("wird &agestartet!"));
                    Utils.heal(p);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("pause")) {
                if (ChallengeMain.paused == false) {
                    ChallengeMain.paused = true;
                    for (Player p : players) {
                        p.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("Die Challenge wurde von &b" + sender.getName() + "&f pausiert!"));
                        CoreSendStringPacket.sendPacketToTitle(p, "Challenge", Utils.colorize("wurde &cpausiert!"));
                    }
                    return true;
                } else {
                    player.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("&cDie Challenge ist bereits pausiert!"));
                }
            }
            if (args[0].equalsIgnoreCase("resume")) {
                if (ChallengeMain.paused == true) {
                    ChallengeMain.paused = false;
                    for (Player p : players) {
                        p.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("Die Challenge wurde von &b" + sender.getName() + "&f fortgesetzt!"));
                        CoreSendStringPacket.sendPacketToTitle(p, "Challenge", Utils.colorize("wurde &afortgesetzt!"));
                    }
                    return true;
                } else {
                    player.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("&cDie Challenge läuft bereits!"));
                }
            }
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length == 2) {
                    Challenge.secondsRunning = Integer.parseInt(args[1]);
                    return true;
                }
            }

        }else if(command.getLabel().equalsIgnoreCase("randomtp")){
            if(args.length == 0){
            ChallengeRandomTeleport.teleportPlayerRandomly(player);}
            else if(args[0].equalsIgnoreCase("range")){
                ChallengeRandomTeleport.setRange(Integer.parseInt(args[1]));
            }
        }
        else if(command.getLabel().equalsIgnoreCase("record")){
            if(args.length == 0){
                ChallengeFileReader.readDataFile();
                player.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("Der aktuelle &bRekord &fliegt bei &b" + Utils.formatTimerTime(ChallengeMain.recordInSeconds)));
            }else if(args.length == 2){
                if(args[0].equalsIgnoreCase("set")){
                    ChallengeMain.recordInSeconds = Integer.parseInt(args[1]);
                    player.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("Der neue &bRekord &fwurde auf &b" + Utils.formatTimerTime(Integer.parseInt(args[1])) + "&fgesetzt"));
                    ChallengeFileReader.writeDataFile(Integer.parseInt(args[1]));
                }
            }
        }


        return false;

    }

}
