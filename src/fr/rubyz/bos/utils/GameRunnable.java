package fr.rubyz.bos.utils;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.rubyz.bos.scoreboard.CustomScoreboardManager;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameManager;
import fr.rubyz.bos.GameState;
import fr.rubyz.bos.Team;

public class GameRunnable extends BukkitRunnable{
	
	@Override
	public void run() {
		
		//Count the number of diamonds in every chest
		for(Team t : BallsOfSteel.teams){
			Block chestBlock = t.getChest();
			
			if(chestBlock.getType().equals(Material.CHEST)){
				
				Chest chest = (Chest) chestBlock.getState();
				
				int diamondsAmout = 0;
				ItemStack[] items = chest.getInventory().getContents();
				
				for(ItemStack item : items){
					if(item != null){
						if(item.getType().equals(Material.DIAMOND)){
							diamondsAmout += item.getAmount();
						}
					}
				}
				
				t.setDiamonds(diamondsAmout);
				
			}else{
				System.out.println("§cError : The " + t.getName() + " team's chest is not correctly defined.");
			}
			
		}
		
		
		//Refreshing the scoreboard
		for(Entry<Player, CustomScoreboardManager> scoreboard : BallsOfSteel.getInstance().sb.entrySet()){
			CustomScoreboardManager board = scoreboard.getValue();
			board.refresh();
		}
		
		//Updating the Tab, Diamonds Indicator & remaining time
		if(GameState.isState(GameState.GAME)){
			Util.updateTab();
			for(Team t : BallsOfSteel.teams){
				t.getDiamondIndicator().update();
			}
			
			BallsOfSteel.remainingTime--;
			
			if(BallsOfSteel.remainingTime == 1200){
				Bukkit.broadcastMessage(Util.getGamePrefix() + "§620 minutes remaining !");
				for(Player pls : Bukkit.getOnlinePlayers()){pls.playSound(pls.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);}
			}else if(BallsOfSteel.remainingTime == 600){
				Bukkit.broadcastMessage(Util.getGamePrefix() + "§610 minutes remaining !");
				for(Player pls : Bukkit.getOnlinePlayers()){pls.playSound(pls.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);}
			}else if(BallsOfSteel.remainingTime == 300){
				Bukkit.broadcastMessage(Util.getGamePrefix() + "§65 minutes remaining !");
				for(Player pls : Bukkit.getOnlinePlayers()){pls.playSound(pls.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);}
			}else if(BallsOfSteel.remainingTime == -1){
				GameManager.stop();
			}
		}
		
	}
	
}
