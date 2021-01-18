package fr.rubyz.bos.event;

import java.util.Arrays;
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

public class Events implements Listener {

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		
		Player p = e.getPlayer();
		
		if((GameState.isState(GameState.LOBBY) || GameState.isState(GameState.FINISH)) && p.getFoodLevel() < 20)
			p.setFoodLevel(20);
			
		if(p.getLocation().getX() > -197 || p.getLocation().getY() < 0 || p.getLocation().getY() > 216 || p.getLocation().getX() < -227 || p.getLocation().getZ() > 589 || p.getLocation().getZ() < 559){
			if(GameState.isState(GameState.LOBBY)){
				p.teleport(BallsOfSteel.getSpawn());
				p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
			}
		}
		
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e){
		Entity ent = e.getEntity();
		
		if(ent instanceof Player){
			
			Player p = (Player) e.getEntity();
			if(GameState.isState(GameState.LOBBY) || GameState.isState(GameState.FINISH)){
				e.setCancelled(true);
			}
			
			if(GameState.isState(GameState.GAME)){
				if(BallsOfSteel.getPlayerTeam(p).getBase().contains(p.getLocation())){
					e.setCancelled(true);
				}
			}
			
		}
	}
	
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e){
		
		if(GameState.isState(GameState.LOBBY)){
			
			e.setCancelled(true);
			
		}else if(e.getItemDrop().getItemStack().equals(Util.make(BallsOfSteel.gameConfig.getBuildBlockMaterial(), 1, "Bloc §oIllimité", null))){
			if(BallsOfSteel.gameConfig.isInfiniteBuildBlock())
				e.setCancelled(true);
		}
		
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e){
		
		if(e.getCurrentItem() != null){
			
			if(e.getCurrentItem().getType().equals(Material.RED_TERRACOTTA) ||
				e.getCurrentItem().getType().equals(Material.GREEN_TERRACOTTA) ||
				e.getCurrentItem().getType().equals(Material.YELLOW_TERRACOTTA) ||
				e.getCurrentItem().getType().equals(Material.BLUE_TERRACOTTA)){
				
				if(GameState.isState(GameState.LOBBY)){
					e.setCancelled(true);
				}
				
			}
			
			if(e.getCurrentItem().getType().equals(BallsOfSteel.gameConfig.getBuildBlockMaterial())){
				if(GameState.isState(GameState.GAME)){
					if(BallsOfSteel.gameConfig.isInfiniteBuildBlock()){
						if(e.getCurrentItem().getItemMeta().getDisplayName().equals("§oInfinite block")){
							e.setCancelled(true);
						}
					}
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
					if(BallsOfSteel.gameConfig.isMidProtected() || BallsOfSteel.gameConfig.isHost() == false)
						e.setCancelled(true);
				}
			}
		}
		
		if(b.getLocation().getY() >= 208)
			e.setCancelled(true);
		
		if(b.getType().equals(BallsOfSteel.gameConfig.getBuildBlockMaterial())){
			if(BallsOfSteel.gameConfig.isInfiniteBuildBlock())
				p.getInventory().setItem(0, Util.make(BallsOfSteel.gameConfig.getBuildBlockMaterial(), 1, null, Arrays.asList("§7Infinite block")));
			
		}
		
		for(Team t : BallsOfSteel.teams){
			if(t.getBase().contains(b.getLocation())){
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e){
		Block b = e.getBlock();
		
		if(!GameState.isState(GameState.GAME))
			e.setCancelled(true);
			
		if(b.getLocation().getX() <= -196 && b.getLocation().getX() >= -228){
			if(b.getLocation().getY() <= 70 && b.getLocation().getY() >= 56){
				if(b.getLocation().getZ() <= 590 && b.getLocation().getBlockZ() >= 558){
					if(BallsOfSteel.gameConfig.isMidProtected() || BallsOfSteel.gameConfig.isHost() == false)
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
			UUID uuid = e.getPlayer().getUniqueId();
			
			BallsOfSteel.diamondsMined.put(uuid, BallsOfSteel.diamondsMined.get(uuid)+1);
			int diamondsMined = BallsOfSteel.diamondsMined.get(uuid);
			
			if(diamondsMined%5 == 0){
				Bukkit.getPlayer(uuid).sendMessage("§b+0.5 coins §9(5 Diamonds Mined)");
			}
		}
		
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e){
		
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			
			Player p = (Player) e.getEntity();
			Player damager = (Player) e.getDamager();
			
			if(BallsOfSteel.getPlayerTeam(p) == BallsOfSteel.getPlayerTeam(damager))
				e.setCancelled(true);
			
			if(GameState.isState(GameState.GAME)){
				if(BallsOfSteel.getPlayerTeam(p).getBase().contains(p.getLocation())){
					e.setCancelled(true);
				}
			}
			
		}
		
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent e){
		
		Player p = e.getPlayer();
		
		if(BallsOfSteel.getPlayerTeam(p) == null){
			e.setRespawnLocation(BallsOfSteel.getSpawn());
		}else{
			e.setRespawnLocation(BallsOfSteel.getPlayerTeam(p).getSpawn());
		}
		
		GameManager.giveStuff(p);
		
	}
	
}