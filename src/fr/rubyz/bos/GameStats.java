package fr.rubyz.bos;

import fr.rubyz.bos.utils.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameStats {

    private HashMap<UUID, Integer> kills;
    private HashMap<UUID, Integer> deaths;
    private HashMap<UUID, Integer> diamondsMined;

    public GameStats() {

        this.kills = new HashMap<>();
        this.deaths = new HashMap<>();
        this.diamondsMined = new HashMap<>();

    }

    /**
     * Initialise the counters to zero for a player
     */
    public void initCounters(Player p){

        this.kills.put(p.getUniqueId(), 0);
        this.deaths.put(p.getUniqueId(), 0);
        this.diamondsMined.put(p.getUniqueId(), 0);

    }

    /**
     * Displays the player statistics to the player
     * @param statsPlayer Player of which we want to see the statistics
     * @param displayPlayer Player to display the statistics
     */
    public void displayPlayerStats(Player statsPlayer, Player displayPlayer){

        ArrayList<String> message = new ArrayList<>();
        message.add("  §7- Kills : §a" + this.getPlayerKills(statsPlayer));
        message.add("  §7- Deaths : §a" + this.getPlayerDeaths(statsPlayer));
        message.add("  §7- Diamonds mined : §a" + this.getPlayerDiamondsMined(statsPlayer));

        Util.sendImportantMessage(statsPlayer.getDisplayName() + "'s statistics", message, displayPlayer);

    }

    /**
     * Increment the kill count of the player by 1
     * @param p
     */
    public void addKill(Player p){

        this.kills.put(p.getUniqueId(), this.getPlayerKills(p) + 1);

    }

    /**
     * Increment the death count of a player by 1
     * @param p
     */
    public void addDeath(Player p){

        this.deaths.put(p.getUniqueId(), this.getPlayerDeaths(p) + 1);

    }

    /**
     * Increment the death count of a player by 1
     * @param p
     */
    public void addDiamondMined(Player p){

        this.diamondsMined.put(p.getUniqueId(), this.getPlayerDiamondsMined(p) + 1);

    }

    /**
     * Get the player's kill count
     * @param p Player
     * @return Number of kills
     */
    public int getPlayerKills(Player p){

        return this.kills.get(p.getUniqueId());

    }

    /**
     * Get the player's death count
     * @param p Player
     * @return Number of deaths
     */
    public int getPlayerDeaths(Player p){

        return this.deaths.get(p.getUniqueId());

    }

    /**
     * Get the player's diamond count
     * @param p Player
     * @return Number of deaths
     */
    public int getPlayerDiamondsMined(Player p){

        return this.diamondsMined.get(p.getUniqueId());

    }

    public HashMap<UUID, Integer> getKills() {
        return this.kills;
    }

    public HashMap<UUID, Integer> getDeaths() {
        return this.deaths;
    }

    public HashMap<UUID, Integer> getDiamondsMined() {
        return this.diamondsMined;
    }
}
