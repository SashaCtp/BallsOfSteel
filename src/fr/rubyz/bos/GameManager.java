package fr.rubyz.bos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;

import fr.rubyz.bos.scoreboard.CustomScoreboardManager;
import fr.rubyz.bos.utils.Util;
import org.bukkit.util.Vector;

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
			pls.playSound(pls.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.4F, 1F);
			Util.updateTab();
		}
		
		for(Entry<Player, CustomScoreboardManager> scoreboard : BallsOfSteel.getInstance().sb.entrySet()){
			CustomScoreboardManager board = scoreboard.getValue();
			board.sendLine();
			board.refresh();
		}

		// Display informations
		Bukkit.getScheduler().scheduleSyncDelayedTask(BallsOfSteel.getInstance(), new Runnable(){

			@Override
			public void run() {

				for(Player pls : Bukkit.getOnlinePlayers())
					pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.4F, 1F);

				Bukkit.broadcastMessage("\n\n§7------------- [§9Tips§7] -------------");
				Bukkit.broadcastMessage("   §fGlobal chat : §l@§f§oYour message");
				Bukkit.broadcastMessage("§7--------------------------------\n\n");

			}

		}, 100);

		// Display informations
		Bukkit.getScheduler().scheduleSyncDelayedTask(BallsOfSteel.getInstance(), new Runnable(){

			@Override
			public void run() {

				for(Player pls : Bukkit.getOnlinePlayers())
					displayGameParameters(pls);

			}

		}, 40);

		Bukkit.broadcastMessage("\n" + Util.getGamePrefix() + " Good luck everyone and have fun !\n");
		
	}
	
	//Function top stop (end) the game
	public static void stop() {

		BallsOfSteel.gameState = GameState.FINISH;

		Bukkit.broadcastMessage(Util.getGamePrefix() + "End of the match !");

		// Check if the match is a draw
		boolean draw = isGameDraw();

		Team winningTeam = null;
		final String kickMessage;
		if (!draw){
			ArrayList<Team> ranking = Team.sortTeamList(BallsOfSteel.teams);
			winningTeam = ranking.get(ranking.size() - 1);
			kickMessage = "§f§lEnd of the match ! \n \n§7" + winningTeam.getColor() + winningTeam.getName() + " team§7 won ! §6Congratulations !\n \n §7§oThe server is now restarting ...";
			Bukkit.broadcastMessage(winningTeam.getColor() + winningTeam.getName() + " team§7 won the game !");
		}else {
			kickMessage = "§f§lEnd of the match ! \n \n§7It's a draw !\n \n §oThe server is now restarting ...";
			Bukkit.broadcastMessage("§7It's a draw !");
		}

		// On affiche les effets aux joueurs
		for (Player pls : Bukkit.getOnlinePlayers()) {

			if(winningTeam != null && winningTeam.getPlayers().contains(pls))
				pls.sendTitle("§a§lVictory !", winningTeam.getColor() + winningTeam.getName() + " team§7 won the game !", 1, 20*7, 1);
			else if(winningTeam != null)
				pls.sendTitle("§c§lDefeat !", winningTeam.getColor() + winningTeam.getName() + " team§7 won the game !", 1, 20*7, 1);
			else
				pls.sendTitle("§7§lDraw !", "§fNobody won the game !", 1, 20*7, 1);

			pls.playSound(pls.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 0.7F, 1F);

			// Effet d'envol
			pls.setAllowFlight(true);
			pls.setFlying(true);

			Vector v = new Vector();
			v.setX(pls.getVelocity().getX());
			v.setY(3);
			v.setZ(pls.getVelocity().getZ());

			pls.setVelocity(v);

			// On affiche les statistiques du joueur
			BallsOfSteel.gameStats.displayPlayerStats(pls, pls);
		}

		launchEndFireworks(winningTeam);

		// Kick the players at the end of the game then restart
		Bukkit.getScheduler().scheduleSyncDelayedTask(BallsOfSteel.getInstance(), new Runnable(){

			@Override
			public void run() {
				
				for(Player pls : Bukkit.getOnlinePlayers())
					pls.kickPlayer(kickMessage);
				
				Bukkit.getScheduler().runTaskLater(BallsOfSteel.getInstance(), new Runnable(){

					@Override
					public void run() {
						Bukkit.spigot().restart();
					}}, 30L);
				}
			
		}, 20L*20);
		
	}

	public static void displayGameParameters(Player p){
		String space = "   ";

		p.sendMessage("§7--------- [§9Game parameters§7] ---------");

		if(BallsOfSteel.gameConfig.isHost())
			p.sendMessage(space + "§f- Version : §cHost");
		else
			p.sendMessage(space + "§f- Version : §cClassique");

		if(BallsOfSteel.gameConfig.isMidProtected())p.sendMessage(space + "§f- Protection du Mid active");
		if(BallsOfSteel.gameConfig.isInfiniteBuildBlock())p.sendMessage(space + "§f- Blocs : §cInfinis");
		p.sendMessage(space + "§f- Default block : §c" + BallsOfSteel.gameConfig.getBuildBlockMaterial().name() + "\n");
		p.sendMessage("§7-----------------------------------\n");

		p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.4F, 1F);

	}

	/**
	 * Launch the fireworks at the end of the game
	 * @param winningTeam 	The team which won the game
	 */
	public static void launchEndFireworks(Team winningTeam){

		final Team wt = winningTeam;

		Bukkit.getScheduler().scheduleSyncRepeatingTask(BallsOfSteel.getInstance(), new Runnable(){

			@Override
			public void run() {

				for(Team team : BallsOfSteel.teams){
					Location chestLoc = team.getChest().getLocation();

					Location loc = new Location(Bukkit.getWorld("world"), chestLoc.getX() + 0.5, chestLoc.getY() + 1, chestLoc.getZ() + 0.5);

					Util.spawnFirework(loc);

					if(wt != null){

						for(Player pls : wt.getPlayers()){

							if(pls != null && pls.isOnline())
								Util.spawnFirework(pls.getLocation());

						}

					}

				}
			}

		}, 0, 20L);

	}

	/**
	 * Checks if there is a draw
	 * Only when the game is finished
	 */
	public static boolean isGameDraw(){

		if(!BallsOfSteel.gameState.equals(GameState.FINISH))
			return false;

		for(int i = 0; i < BallsOfSteel.teams.size()-1; i++){
			if (BallsOfSteel.teams.get(i).getDiamonds() != BallsOfSteel.teams.get(i+1).getDiamonds())
				return false;
		}

		return true;

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
