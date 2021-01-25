package fr.rubyz.bos;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import fr.rubyz.bos.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
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
	
	public static ArrayList<Player> spectator = new ArrayList<>();
	public static ArrayList<Team> teams = new ArrayList<>();
	public static ArrayList<PotionEffect> effectList = new ArrayList<PotionEffect>();
	
	public HashMap<Player, CustomScoreboardManager> sb = new HashMap<>();
	
	public static HashMap<UUID, Integer> kills = new HashMap<>();
	public static HashMap<UUID, Integer> death = new HashMap<>();
	public static HashMap<UUID, Integer> diamondsMined = new HashMap<>();
	
	public static int gameMaxplayers;
	public static int clockTask;
	public static int remainingTime;
	
	@Override
	public void onEnable(){
		
		//Start message
		Bukkit.broadcastMessage("-----------------------------");
		Bukkit.broadcastMessage(Util.getGamePrefix() + "§7Starting BallsOfSteel Spigot plugin by Mega (§3§o@MegaSash§7)");
		Bukkit.broadcastMessage("§7Get sourcecode on github : §8§ohttps://github.com/MegaSash/BallsOfSteel");
		Bukkit.broadcastMessage("-----------------------------");
		
		instance = this;
		
		World w = Bukkit.getWorlds().get(0);
		
		EventManager.registerEvents(this);
		GameState.setState(GameState.LOBBY);
		
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
		
		Bukkit.broadcastMessage(Util.getGamePrefix() + "Variables : ");
		Bukkit.broadcastMessage("                  " + "Host : " + gameConfig.isHost());
		Bukkit.broadcastMessage("                  " + "Team size : " + gameConfig.getTeamsize() + " players");
		Bukkit.broadcastMessage("                  " + "Match lenght : " + gameConfig.getTime() + " minuts");
		Bukkit.broadcastMessage("                  " + "Infinite blocks : " + gameConfig.isInfiniteBuildBlock());
		Bukkit.broadcastMessage("-----------------------------");
		
		//Commands
		getCommand("gametime").setExecutor(new GametimeCommand());
		getCommand("param").setExecutor(new ParamCommand());
		getCommand("start").setExecutor(new StartCommand());
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("debug").setExecutor(new DebugCommand());

		//Team creation
		try{
			int teamMaxplayers = gameConfig.getTeamsize();
			
			Block redChest = w.getBlockAt(new Location(w, -212, 58, 577));
			red = new Team("Red", new ArrayList<UUID>(), teamMaxplayers, new Location(w, -303, 70.3, 666, -90, 0), redChest, new Location(w, -303, 72-0.7, 672.5), "§c", Material.RED_TERRACOTTA, new Location(w, -299, 61, 661), new Location(w, -312, 80, 674));
			teams.add(red);
			
			Block blueChest = w.getBlockAt(new Location(w, -212, 58, 571));
			blue = new Team("Blue", new ArrayList<UUID>(), teamMaxplayers, new Location(w, -120, 71.3, 483), blueChest, new Location(w, -120, 73-0.7, 476.5), "§3", Material.BLUE_TERRACOTTA, new Location(w, -125, 62, 487), new Location(w, -112, 81, 474));
			teams.add(blue);
			
			Block yellowChest = w.getBlockAt(new Location(w, -209, 58, 574));
			yellow = new Team("Yellow", new ArrayList<UUID>(), teamMaxplayers, new Location(w, -120, 70.3, 666, 180, 0), yellowChest, new Location(w, -113.5, 72-0.7, 666), "§e", Material.YELLOW_TERRACOTTA, new Location(w, -125, 61, 661), new Location(w, -112, 80, 684));
			teams.add(yellow);
			
			Block greenChest = w.getBlockAt(new Location(w, -215, 58, 574));
			green = new Team("Green", new ArrayList<UUID>(), teamMaxplayers, new Location(w, -303, 72.3, 483), greenChest, new Location(w, -309.5, 74-0.7, 483), "§a", Material.GREEN_TERRACOTTA, new Location(w, -299, 63, 487), new Location(w, -312, 82, 474));
			teams.add(green);
			
			Bukkit.broadcastMessage(Util.getGamePrefix() + "Teams :");
			for(Team t : teams)
				Bukkit.broadcastMessage("                  " + t.getColor() + t.getName() + " team");
			Bukkit.broadcastMessage("-----------------------------");
		}catch(Exception e){
			Bukkit.broadcastMessage("§c[ERROR] Error while creating the team /!\\ Check the console");
			e.printStackTrace();
		}

		//Stats
		gameStats = new GameStats();

		Util.updateTab();
		
		Bukkit.getWorlds().get(0).setAutoSave(false);
		
		//Runnable
		new GameRunnable().runTaskTimer(this, 0L, 20L);
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule keepInventory false");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-off");
		
		Bukkit.broadcastMessage(Util.getGamePrefix() + "§aThe plugin started without any error !");
		
	}
	
	@Override
	public void onDisable(){
		for(Player pls : Bukkit.getOnlinePlayers()){
			pls.kickPlayer(Util.getGamePrefix() + "\n §a§lReload");
		}
		
		//Kill des ArmorStand
		for(Entity ent : Bukkit.getWorlds().get(0).getEntities()){
			if(ent instanceof ArmorStand || ent instanceof Item || ent instanceof Arrow){
				ent.remove();
			}
		}
		
		for(Player pls : Bukkit.getOnlinePlayers()){
			//Clear des teams et prefix
			if(BallsOfSteel.getPlayerTeam(pls) != null){
				BallsOfSteel.getPlayerTeam(pls).removePlayer(pls);
			}
		}
		
		for(Team t : teams){
			Chest teamChest = (Chest) t.getChest().getState();
			teamChest.getInventory().clear();
			t.getDiamondIndicator().remove();
		}
		
		for(World worlds : Bukkit.getWorlds()){
			File folder = new File(worlds.getWorldFolder(), "playerdata");
			for(File files : folder.listFiles()){
				files.delete();
			}
		}
	}
	
	public static void setSpectatorMode(Player p){
		spectator.add(p);
		
		p.setGameMode(GameMode.SPECTATOR);
		p.sendMessage(Util.getGamePrefix() + "You are now a spectator !");
		if(!Team.allTeamFull())p.sendMessage(Util.getGamePrefix() + "Open your inventory to join a team !");
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
	
	public static Team getPlayerTeam(Player p){
		Team t = null;
		
		for(Team teamTest : teams){
			if(teamTest.getPlayers().contains(p)){
				t = teamTest;
			}
		}
		
		return t;
	}
	
	public GameConfiguration getGameConfig(){
		return gameConfig;
	}

}