package fr.rubyz.bos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;

import fr.rubyz.bos.scoreboard.CustomScoreboardManager;
import fr.rubyz.bos.utils.Util;

public class GameManager {

	// Function to start the game
	public static void start(){

		BallsOfSteel.gameState = GameState.GAME;
		
		ArrayList<Player> noTeam = new ArrayList<>();
		for(Player pls : Bukkit.getOnlinePlayers()){
			if(BallsOfSteel.getPlayerTeam(pls) == null){
				if(!noTeam.contains(pls)){
					noTeam.add(pls);
				}
			}
		}
		
		for(Team t : BallsOfSteel.teams){
			for(Iterator<Player> it = noTeam.iterator(); it.hasNext();){
				
				Player pls = it.next();
				
				if(!t.isFull()){
					t.addPlayer(pls);
					it.remove();
					noTeam.remove(pls);
					pls.sendMessage(Util.getGamePrefix() + "You've just join " + t.getColor() + t.getName() + " team §f !");
				}
			}

			t.getDiamondIndicator().spawn();
			t.teleportPlayers();
		}
		
		for(Player p : Bukkit.getOnlinePlayers()){

			if(!BallsOfSteel.spectator.contains(p))
				BallsOfSteel.gameStats.initCounters(p);

		}

		for(Player pls : Bukkit.getOnlinePlayers()){
			pls.getInventory().clear();
			giveStuff(pls);
			pls.setHealth(20);
			pls.setFoodLevel(20);
			pls.setGameMode(GameMode.SURVIVAL);
			pls.setLevel(0);
			pls.setExp(0);
			Util.updateTab();
		}
		
		for(Entry<Player, CustomScoreboardManager> scoreboard : BallsOfSteel.getInstance().sb.entrySet()){
			CustomScoreboardManager board = scoreboard.getValue();
			board.sendLine();
			board.refresh();
		}

		// Display informations
		
		Bukkit.broadcastMessage("§a--------------------------------");
		Bukkit.broadcastMessage("  §bTips : §7Global chat : §l@§7Your message");
		Bukkit.broadcastMessage("§a--------------------------------");
		Bukkit.broadcastMessage("       §c-=Match parameters=-");
		
		String space = "       ";
		
		if(BallsOfSteel.gameConfig.isHost())Bukkit.broadcastMessage(space + "§7- Version Host");
		if(BallsOfSteel.gameConfig.isMidProtected())Bukkit.broadcastMessage(space + "§7- Protection du Mid active");
		if(BallsOfSteel.gameConfig.isInfiniteBuildBlock())Bukkit.broadcastMessage(space + "§7- Blocs infinis");
		Bukkit.broadcastMessage(space + "§7- Default block : " + BallsOfSteel.gameConfig.getBuildBlockMaterial().name());
		Bukkit.broadcastMessage("                            ");
		Bukkit.broadcastMessage(space + "§6§lGood luck everyone and have fun !");
		
	}
	
	//Function top stop (end) the game
	public static void stop(){

		BallsOfSteel.gameState = GameState.FINISH;

		Bukkit.broadcastMessage(Util.getGamePrefix() + "End of the match !");
		for(Player pls : Bukkit.getOnlinePlayers()){
			pls.sendTitle("§cEnd of the match !", "", 1, 70, 1);
		}

		//Make the ranks
		ArrayList<Team> ranking = Team.sortTeamList(BallsOfSteel.teams);

		Team first = ranking.get(ranking.size()-1);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(BallsOfSteel.getInstance(), new Runnable(){

			@Override
			public void run() {
			
			Random r = new Random();
			
			for(Team team : BallsOfSteel.teams){
				Location chestLoc = team.getChest().getLocation();
				
				Location loc = new Location(Bukkit.getWorld("world"), chestLoc.getX() + 0.5, chestLoc.getY() + 1, chestLoc.getZ() + 0.5);
				
				Firework firework1 = (Firework)loc.getWorld().spawn(loc, Firework.class);
				FireworkEffect effect1 = FireworkEffect.builder()
					.withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
					.withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
					.withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
					.flicker(true)
					.withFade(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
					.withFade(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
					.withFade(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)))
					.with(FireworkEffect.Type.BURST)
					.build();
					FireworkMeta meta1 = firework1.getFireworkMeta();
					meta1.addEffect(effect1);
					meta1.setPower(1);
					firework1.setFireworkMeta(meta1);
				
				}
			}
			
		}, 0, 20L);

		for(Player pls : Bukkit.getOnlinePlayers()){
			
			BallsOfSteel.gameStats.displayPlayerStats(pls, pls);
			
		}
		
		//Kick the players at the end of the game then restart
		Bukkit.getScheduler().scheduleSyncDelayedTask(BallsOfSteel.getInstance(), new Runnable(){

			@Override
			public void run() {
				
				for(Player pls : Bukkit.getOnlinePlayers()){
					pls.kickPlayer("§f§lEnd of the match ! \n \n§7" + first.getColor() + first.getName() + " team§7 won ! \n \n §oServer is restarting ...");
				}
				
				Bukkit.getScheduler().runTaskLater(BallsOfSteel.getInstance(), new Runnable(){

					@Override
					public void run() {
						Bukkit.spigot().restart();
					}}, 30L);
				}
			
		}, 200L);
		
	}
	
	//Give the stuff to the player
	public static void giveStuff(final Player p){
		
		Material mat = BallsOfSteel.gameConfig.getBuildBlockMaterial();
		
		p.getInventory().setItem(1, Util.make(Material.IRON_SWORD, 1, null, Arrays.asList("§7  Default Sword")));
		p.getInventory().setItem(4, Util.make(Material.IRON_PICKAXE, 1, null, null));
		p.getInventory().setItem(5, Util.make(Material.IRON_SHOVEL, 1, null, null));
		p.getInventory().setItem(6, Util.make(Material.IRON_AXE, 1, null, null));
		
		if(BallsOfSteel.gameConfig.isInfiniteBuildBlock()){
			p.getInventory().setItem(0, Util.make(mat, 1, "§oInfinite block", null));
		}else{
			p.getInventory().setItem(0, Util.make(mat, 64, "Construction block", null));
		}
		
		p.getInventory().setItem(3, new ItemStack(Material.COOKED_BEEF, 16));
		
		p.getInventory().setItem(2, Util.make(Material.BOW, 1, "Bow", null));
		p.getInventory().setItem(17, new ItemStack(Material.ARROW, 16));
		
		giveTeamArmor(p);
		
		Bukkit.getScheduler().runTaskLater(BallsOfSteel.getInstance(), new Runnable(){
			
			@Override
			public void run(){
				
				for(PotionEffect t : BallsOfSteel.effectList){
					p.addPotionEffect(t);
				}
				
			}
			
		}, 10);
	}
	
	//Give the correct team colored armor
	private static void giveTeamArmor(Player p){
		Color color = null;
		
		String playerTeamColor = BallsOfSteel.getPlayerTeam(p).getColor();

		switch(playerTeamColor) {
			case "§1":
				color = Color.fromRGB(0,0,170);
			case "§9":
				color = Color.fromRGB(83,85,255);
			case "§3":
				color = Color.fromRGB(1,169,170);
			case "§b":
				color = Color.fromRGB(84,255,255);
			case "§c":
				color = Color.fromRGB(255, 80, 82);
			case "§2":
				color = Color.fromRGB(0, 170, 3);
			case "§e":
				color = Color.fromRGB(255, 254, 87);
			case "§a":
				color = Color.fromRGB(85, 255, 88);
		}
		
	    ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
	    LeatherArmorMeta lhelmetmeta = (LeatherArmorMeta)lhelmet.getItemMeta();
	    assert lhelmetmeta != null;
	    lhelmetmeta.setDisplayName("Default armor");
	    lhelmetmeta.setColor(color);
	    lhelmet.setItemMeta(lhelmetmeta);
	    
	    p.getInventory().setHelmet(lhelmet);
	    
	    ItemStack lchestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
	    LeatherArmorMeta lchestplatemeta = (LeatherArmorMeta)lhelmet.getItemMeta();
	    lchestplatemeta.setDisplayName("Default armor");
	    lchestplatemeta.setColor(color);
	    lchestplate.setItemMeta(lchestplatemeta);
	    
	    p.getInventory().setChestplate(lchestplate);
	    
	    ItemStack lboots = new ItemStack(Material.LEATHER_BOOTS, 1);
	    LeatherArmorMeta lbootsmeta = (LeatherArmorMeta)lhelmet.getItemMeta();
	    lbootsmeta.setDisplayName("Default armor");
	    lbootsmeta.setColor(color);
	    lboots.setItemMeta(lbootsmeta);
	    
	    p.getInventory().setBoots(lboots);
		
	}
	
}
