package challenge;

import java.util.*;

import core.CoreMain;
import core.CoreResetServer;
import core.CoreSendStringPacket;
import core.Utils;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class ChallengeMain extends JavaPlugin implements Listener {

    public static Collection<? extends Player> players = Bukkit.getOnlinePlayers();
    private ChallengeCommandExecutor CommExecutor;

    public static List<Player> hasBeenTeleported = new ArrayList<Player>();

    private static Random random = new Random();

    public static int secondsRunning = 0;
    public static int recordInSeconds = 0;

    public static double splitHealth = 20;

    public static boolean started = false;
    public static boolean paused = false;
    public static boolean finished = false;
    public static boolean dead = false;

    //Settings

    public static boolean isHardcore = false;
    public static boolean isSplitHealth = false;
    public static boolean backpackEnabled = false;
    public static boolean randomTeleportEnabled = false;
    public static boolean forceBlockEnabled = false;
    public static boolean showRecord = false;
    public static boolean showDamage = false;

    public static HashMap<Player, Boolean> showParticles = new HashMap<Player, Boolean>();

    public static BossBar recordTimeBar = Bukkit.createBossBar(Utils.colorize("Euer &bRekord &fist &b" + Utils.formatTimerTime(recordInSeconds)), BarColor.WHITE, BarStyle.SOLID);

    public static float secondsUntilTeleport;

    private Boolean isTeleporterRunning = false;

    protected static World world;

    private ChallengeMain plugin = this;


    public static HashMap<String, Double> healthMap = new HashMap<String, Double>();


    public void onEnable() {

        CoreMain.setPlugin(this);
        world = Bukkit.getWorld("world");
        ChallengeFileReader challengeFileReader = new ChallengeFileReader(this);
        ChallengeFileReader.readDataFile();
        ChallengeListener challengeSettingsListener = new ChallengeListener(this);
        recordTimeBar.setVisible(false);

        CommExecutor = new ChallengeCommandExecutor(this);
        getCommand("timer").setExecutor(CommExecutor);
        getCommand("reset").setExecutor(CommExecutor);
        getCommand("settings").setExecutor(CommExecutor);
        getCommand("backpack").setExecutor(CommExecutor);
        getCommand("randomTP").setExecutor(CommExecutor);
        getCommand("record").setExecutor(CommExecutor);


        BukkitScheduler Timer = Bukkit.getScheduler(); //Scheduler for everything with a timer
        Timer.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {

                if (finished) {
                    finished = false;
                    started = false;
                    ChallengeFileReader.writeDataFile(secondsRunning);
                    for (Player p : players) {
                        p.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("Die Challenge wurde in &b" + Utils.formatTimerTime(secondsRunning) + " &fbeendet"));
                        p.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize(Utils.getRainbowString("Pog Clap")));
                    }
                    secondsRunning = 0;
                }

                for (Player player : players) {
                    String msg;
                    if (started && !paused) {
                        msg = Utils.colorize("Die Challenge läuft seit &b" + Utils.formatTimerTime(secondsRunning));
                    } else {
                        msg = Utils.colorize("&cDie Challenge ist pausiert!");
                    }
                    CoreSendStringPacket.sendPacketToHotbar(player, msg);

                }

                if (started && !paused && !finished && !dead) {
                    secondsRunning++;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (isHardcore) {
                        if (player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                            player.removePotionEffect(PotionEffectType.ABSORPTION);
                        }
                    }
                }


                if (started) {
                    if (!isTeleporterRunning) {
                        hasStarted();
                    }

                }


                for (Player player : Bukkit.getOnlinePlayers()) {
                    recordTimeBar.addPlayer(player);
                }
                if (!finished && started && !paused && recordInSeconds - secondsRunning >= 0 && showRecord) {
                    recordTimeBar.setVisible(true);
                    float progress = 1 - ((float) secondsRunning / (float) recordInSeconds);
                    recordTimeBar.setProgress(progress);
                    recordTimeBar.setTitle(Utils.colorize("Euer &bRekord &fist &b" + Utils.formatTimerTime(recordInSeconds) + "&f (noch &b" + Utils.formatTimerTime(recordInSeconds - secondsRunning) + "&f)"));

                    switch (recordInSeconds - secondsRunning) {
                        case 3600:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Challenge") + Utils.colorize("Um euren &bRekord&f zu schlagen habt ihr noch &b" + Utils.formatTimerTime(recordInSeconds - secondsRunning) + "&f!"));
                            break;
                        case 1800:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Challenge") + Utils.colorize("Um euren &bRekord&f zu schlagen habt ihr noch &b" + Utils.formatTimerTime(recordInSeconds - secondsRunning) + "&f!"));
                            recordTimeBar.setColor(BarColor.YELLOW);
                            break;
                        case 900:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Challenge") + Utils.colorize("Um euren &bRekord&f zu schlagen habt ihr noch &b" + Utils.formatTimerTime(recordInSeconds - secondsRunning) + "&f!"));
                            break;
                        case 600:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Challenge") + Utils.colorize("Um euren &bRekord&f zu schlagen habt ihr noch &b" + Utils.formatTimerTime(recordInSeconds - secondsRunning) + "&f!"));
                            break;
                        case 300:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Challenge") + Utils.colorize("Um euren &bRekord&f zu schlagen habt ihr noch &b" + Utils.formatTimerTime(recordInSeconds - secondsRunning) + "&f!"));
                            recordTimeBar.setColor(BarColor.RED);
                            break;
                        case 0:
                            Utils.sendMessageToEveryone(Utils.getPrefix("Challenge") + Utils.colorize("Leider habt ihr es nicht geschafft, euren &bRekord&f zu schlagen!"));
                            break;
                    }
                } else if (dead) {
                    recordTimeBar.setProgress(1);
                    recordTimeBar.setTitle(Utils.colorize("&cChallenge beendet!"));
                    recordTimeBar.setColor(BarColor.RED);

                }
            }
        }, 0L, 20L);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (paused || !started)
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (showParticles.containsKey(player)) {
                            if (showParticles.get(player)) {
                                player.spawnParticle(Particle.SPELL_WITCH, player.getLocation(), 100);
                            }
                        }
                    }
            }
        }, 0L, 1L);


    }


    public void hasStarted() {
        if (randomTeleportEnabled) {
            isTeleporterRunning = true;
            BossBar timeUntilTeleport = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SEGMENTED_20);
            for (Player player : Bukkit.getOnlinePlayers()) {
                timeUntilTeleport.addPlayer(player);
            }

            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (secondsUntilTeleport > 0) {
                        secondsUntilTeleport--;
                        if (secondsUntilTeleport <= 30) {
                            if (!timeUntilTeleport.isVisible()) {
                                timeUntilTeleport.setVisible(true);
                            }
                            timeUntilTeleport.setTitle(Utils.colorize("Nächster &bTeleport&f in &b" + Utils.formatTimerTime((int) secondsUntilTeleport)));
                            timeUntilTeleport.setProgress((secondsUntilTeleport / 45));
                        } else {
                            timeUntilTeleport.setVisible(false);
                        }
                    } else {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            ChallengeRandomTeleport.teleportPlayerRandomly(player);
                        }
                        secondsUntilTeleport = getNewTeleportTime();
                    }
                }
            }, 0L, 20);
        }
    }

    public static float getNewTeleportTime() {
        float temp;
        temp = random.nextFloat();
        if (temp + 0.3 >= 1) {
            temp = 1;
        } else {
            temp += .3;
        }

        System.out.println(temp * 900);

        return temp * 900;
    }


    public static void ChallengeFailed(Player player) {
        dead = true;
        started = false;
        for (Player ps : players) {
            ps.setGameMode(GameMode.SPECTATOR);
            ps.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("&b" + player.getDisplayName() + "&f ist gestorben!"));
            ps.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize("Die Challenge ist bei &b" + Utils.formatTimerTime(secondsRunning) + "&f gescheitert!"));
            ps.sendMessage(Utils.getPrefix("Challenge") + Utils.colorize(Utils.getRainbowString("FeelsBadMan")));
        }
    }


}
