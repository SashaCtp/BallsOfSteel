package fr.rubyz.bos.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameManager;
import fr.rubyz.bos.GameState;
import fr.rubyz.bos.Team;
import fr.rubyz.bos.utils.Util;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;

public class Events implements Listener {
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e){
		Entity ent = e.getEntity();
		
		if(ent instanceof Player){
			
			Player p = (Player) e.getEntity();
			if(BallsOfSteel.gameState.equals(GameState.LOBBY) || BallsOfSteel.gameState.equals(GameState.FINISH)){
				e.setCancelled(true);
			}
			
			if(BallsOfSteel.gameState.equals(GameState.GAME)){
				if(Team.getPlayerTeam(p).getBase().contains(p.getLocation())){
					e.setCancelled(true);
				}
			}
			
		}
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e){
		
		if(BallsOfSteel.gameState.equals(GameState.LOBBY)
			|| (BallsOfSteel.gameConfig.isInfiniteBuildBlock() && Util.isTheInfiniteBlock(e.getItemDrop().getItemStack())))
			e.setCancelled(true);

	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e){
		
		if(e.getCurrentItem() != null){
			
			if(e.getCurrentItem().getType().equals(Material.RED_TERRACOTTA) ||
				e.getCurrentItem().getType().equals(Material.GREEN_TERRACOTTA) ||
				e.getCurrentItem().getType().equals(Material.YELLOW_TERRACOTTA) ||
				e.getCurrentItem().getType().equals(Material.BLUE_TERRACOTTA)){
				
				if(BallsOfSteel.gameState.equals(GameState.LOBBY)){
					e.setCancelled(true);
				}
				
			}

		}
		
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent e){
		
		Player p = e.getPlayer();
		
		Block b = e.getBlock();
		
		if(b.getLocation().getX() <= -196 && b.getLocation().getX() >= -228){
			if(b.getLocation().getY() <= 70 && b.getLocation().getY() >= 56){
				if(b.getLocation().getZ() <= 590 && b.getLocation().getBlockZ() >= 558){
					if(BallsOfSteel.gameConfig.isMidProtected() || !BallsOfSteel.gameConfig.isHost())
						e.setCancelled(true);
				}
			}
		}
		
		if(b.getLocation().getY() >= 208)
			e.setCancelled(true);

		if(BallsOfSteel.gameConfig.isInfiniteBuildBlock() && Util.isTheInfiniteBlock(e.getItemInHand()))
				p.getInventory().setItem(e.getHand(), Util.getInfiniteItem());
		
		for(Team t : BallsOfSteel.teams){
			if(t.getBase().contains(b.getLocation())){
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e){
		Block b = e.getBlock();
		
		if(!BallsOfSteel.gameState.equals(GameState.GAME))
			e.setCancelled(true);
			
		if(b.getLocation().getX() <= -196 && b.getLocation().getX() >= -228){
			if(b.getLocation().getY() <= 70 && b.getLocation().getY() >= 56){
				if(b.getLocation().getZ() <= 590 && b.getLocation().getBlockZ() >= 558){
					if(BallsOfSteel.gameConfig.isMidProtected() || !BallsOfSteel.gameConfig.isHost())
						e.setCancelled(true);
				}
			}
		}
		
		for(Team t : BallsOfSteel.teams){
			if(t.getBase().contains(b.getLocation()))
				e.setCancelled(true);
			
			if(b.getLocation() == t.getChest().getLocation())
				e.setCancelled(true);
		}
		
		if(b.getType().equals(Material.DIAMOND_ORE)){

			BallsOfSteel.gameStats.addDiamondMined(e.getPlayer());
			
			if(BallsOfSteel.gameStats.getPlayerDiamondsMined(e.getPlayer())%5 == 0){
				e.getPlayer().sendMessage("§b+0.5 coins §9(5 Diamonds Mined)");
			}
		}
		
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
		
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			
			Player p = (Player) e.getEntity();
			Player damager = (Player) e.getDamager();
			
			if(Team.getPlayerTeam(p) == Team.getPlayerTeam(damager))
				e.setCancelled(true);
			
			if(BallsOfSteel.gameState.equals(GameState.GAME)){
				if(Team.getPlayerTeam(p).getBase().contains(p.getLocation())){
					e.setCancelled(true);
				}
			}
			
		}
		
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent e){
		
		Player p = e.getPlayer();
		
		if(Team.getPlayerTeam(p) == null){
			e.setRespawnLocation(BallsOfSteel.getSpawn());
		}else{
			e.setRespawnLocation(Team.getPlayerTeam(p).getSpawn());
		}
		
		GameManager.giveStuff(p);
		
	}
	
}