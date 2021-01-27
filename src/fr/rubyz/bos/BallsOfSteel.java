package fr.rubyz.bos;

import java.io.File;
import java.util.*;

import com.sk89q.worldedit.bukkit.BukkitItemCategoryRegistry;
import fr.rubyz.bos.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import fr.rubyz.bos.event.EventManager;
import fr.rubyz.bos.scoreboard.CustomScoreboardManager;
import fr.rubyz.bos.utils.GameRunnable;
import fr.rubyz.bos.utils.Util;


public class BallsOfSteel extends JavaPlugin{
	
	public static BallsOfSteel instance;
	
	public static Team red;
	public static Team blue;
	public static Team yellow;
	public static Team green;
	
	private static Location spawn;
	
	public static GameConfiguration gameConfig;

	public static GameStats gameStats;
	public static GameState gameState;
	
	public static ArrayList<Player> spectator = new ArrayList<>();
	public static ArrayList<Team> teams = new ArrayList<>();
	public static ArrayList<PotionEffect> effectList = new ArrayList<PotionEffect>();
	
	public HashMap<Player, CustomScoreboardManager> sb = new HashMap<>();
	
	public static int gameMaxplayers;
	public static int clockTask;
	public static int remainingTime;
	
	@Override
	public void onEnable(){
		
		//Start message
		Bukkit.getConsoleSender().sendMessage("-----------------------------");
		Bukkit.getConsoleSender().sendMessage(Util.getGamePrefix() + "§7Starting BallsOfSteel Spigot plugin by Mega (§3§o@MegaSash§7)");
		Bukkit.getConsoleSender().sendMessage("§7Get sourcecode on github : §8§ohttps://github.com/SashaCtp/BallsOfSteel");
		Bukkit.getConsoleSender().sendMessage("-----------------------------");
		
		instance = this;

		World w = Bukkit.getWorlds().get(0);

		EventManager.registerEvents(this);

		gameStats = new GameStats();
		gameState = GameState.LOBBY;
		spawn = new Location(w, -211.5, 212.8, 574.5, 0, 0);
		
		//Config files
		this.getConfig().addDefault("host", false);
		this.getConfig().addDefault("teamsize", 5);
		this.getConfig().addDefault("gametime", 30);
		this.getConfig().addDefault("infiniteBuildBlock", false);
		this.getConfig().options().copyDefaults(true);
		saveConfig();
	
		//Game Config
		gameConfig = new GameConfiguration(0, false, 0, Material.DIRT);
		gameMaxplayers = gameConfig.getTeamsize()*4;
		remainingTime = gameConfig.getTime()*60;

		ArrayList<String> msg = new ArrayList<>();
		msg.add("  Host : " + gameConfig.isHost());
		msg.add("  Team size : " + gameConfig.getTeamsize() + " players");
		msg.add("  Match lenght : " + gameConfig.getTime() + " minutes");
		msg.add("  Infinite blocks : " + gameConfig.isInfiniteBuildBlock());
		Util.sendImportantMessage("Variables", msg, Bukkit.getConsoleSender());
		msg = null;

		//Commands
		Objects.requireNonNull(getCommand("game")).setExecutor(new GameCommand());
		Objects.requireNonNull(getCommand("stats")).setExecutor(new StatsCommand());
		Objects.requireNonNull(getCommand("debug")).setExecutor(new DebugCommand());

		//Team creation
		try{
			int teamMaxplayers = gameConfig.getTeamsize();
			
			Block redChest = w.getBlockAt(new Location(w, -212, 58, 577));
			red = new Team("Red", new ArrayList<>(), teamMaxplayers, new Location(w, -303, 70.3, 666, -90, 0), redChest, new Location(w, -303, 72-1.5, 672.5), "§c", Material.RED_TERRACOTTA, new Location(w, -299, 61, 661), new Location(w, -312, 80, 674));
			teams.add(red);
			
			Block blueChest = w.getBlockAt(new Location(w, -212, 58, 571));
			blue = new Team("Blue", new ArrayList<>(), teamMaxplayers, new Location(w, -120, 71.3, 483), blueChest, new Location(w, -120, 73-1.5, 476.5), "§3", Material.BLUE_TERRACOTTA, new Location(w, -125, 62, 487), new Location(w, -112, 81, 474));
			teams.add(blue);
			
			Block yellowChest = w.getBlockAt(new Location(w, -209, 58, 574));
			yellow = new Team("Yellow", new ArrayList<>(), teamMaxplayers, new Location(w, -120, 70.3, 666, 180, 0), yellowChest, new Location(w, -113.5, 72-1.5, 666), "§e", Material.YELLOW_TERRACOTTA, new Location(w, -125, 61, 661), new Location(w, -112, 80, 684));
			teams.add(yellow);
			
			Block greenChest = w.getBlockAt(new Location(w, -215, 58, 574));
			green = new Team("Green", new ArrayList<>(), teamMaxplayers, new Location(w, -303, 72.3, 483), greenChest, new Location(w, -309.5, 74-1.5, 483), "§a", Material.GREEN_TERRACOTTA, new Location(w, -299, 63, 487), new Location(w, -312, 82, 474));
			teams.add(green);

			ArrayList<String> message = new ArrayList<>();
			for(Team t : teams)
				message.add(" " + t.getColor() + t.getName() + " team");

			Util.sendImportantMessage("Teams", message, (CommandSender) Bukkit.getConsoleSender());

		}catch(Exception e){
			Bukkit.broadcastMessage("§c[ERROR] Error while creating the team /!\\ Check the console");
			e.printStackTrace();
		}

		Util.updateTab();
		
		Bukkit.getWorlds().get(0).setAutoSave(false);

		//Runnable
		new GameRunnable().runTaskTimer(this, 0L, 20L);

		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule keepInventory false");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule announceAdvancements false");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule spectatorsGenerateChunks false");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-off");

			}
		}, 10);
		
		Bukkit.getConsoleSender().sendMessage(Util.getGamePrefix() + "§aThe plugin started without any error !");

	}
	
	@Override
	public void onDisable(){
		for(Player pls : Bukkit.getOnlinePlayers()){

			//Clear des teams et prefix
			Team t = Team.getPlayerTeam(pls);
			if(t != null)
				t.removePlayer(pls);

			pls.kickPlayer(Util.getGamePrefix() + "\n\n §7§oServer is reloading");

		}
		
		for(Team t : teams){
			t.getDiamondIndicator().remove();

			Chest teamChest = (Chest) t.getChest().getState();
			teamChest.getInventory().clear();
			t.getDiamondIndicator().remove();
		}
		
		for(World worlds : Bukkit.getWorlds()){
			File folder = new File(worlds.getWorldFolder(), "playerdata");
			for(File files : Objects.requireNonNull(folder.listFiles())){
				files.delete();
			}
		}

	}
	
	public static void setSpectatorMode(Player p){
		spectator.add(p);
		
		p.setGameMode(GameMode.SPECTATOR);
		p.sendMessage(Util.getGamePrefix() + "You are now a spectator !");
		if(!Team.allTeamFull())
			p.sendMessage(Util.getGamePrefix() + "Open your inventory to join a team !");

		//Teleporting the spectator to a random player in the game
		int random = new Random().nextInt(Bukkit.getServer().getOnlinePlayers().size());
		Player p1 = (Player) Bukkit.getServer().getOnlinePlayers().toArray()[random];
		p.teleport(p1);
	}
	
	public void lobbyMenu(Player p){
		
		for(int i = 0; i < BallsOfSteel.getTeams().size(); i++){
			Team team = BallsOfSteel.getTeams().get(i);
			
			ArrayList<String> lore = new ArrayList<>();
			if(team.getPlayers() == null){
				lore.add("§7  > 0/" + team.getMaxplayers() + " players");
			}else if(team.getPlayers().size() == team.getMaxplayers()){
				lore.add("§7  > " + team.getPlayers().size() + "/" + team.getMaxplayers() + "  players  §c§lFULL");
			}else{
				lore.add("§7  > " + team.getPlayers().size() +"/"+team.getMaxplayers() + " players");
			}
			
			lore.add("  ");
			
			for(Player plr : team.getPlayers()){
				lore.add("§7- " + plr.getName());
			}
			
			p.getInventory().setItem(i, Util.make(team.getColorBlock(), 1, team.getColor() + "Join " + team.getName() +" team", lore));
			
		}
	}

	//Find the team of the hardened stained block
	public static Team materialToTeam(Material material){
		Team team = null;
		
		for(int i = 0; i < getTeams().size(); i++){
			
			if(material == getTeams().get(i).getColorBlock()){
				team = getTeams().get(i);
			}
			
		}
		
		return team;
	}
	
	public static BallsOfSteel getInstance(){
		return instance;
	}
	
	public static Location getSpawn() {
		return spawn;
	}
	
	public static ArrayList<Team> getTeams(){
		return teams;
	}
	
	public GameConfiguration getGameConfig(){
		return gameConfig;
	}

}