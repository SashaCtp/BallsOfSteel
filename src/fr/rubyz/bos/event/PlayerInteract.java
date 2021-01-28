package fr.rubyz.bos.event;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameState;
import fr.rubyz.bos.Team;
import fr.rubyz.bos.utils.Util;

public class PlayerInteract implements Listener{

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		
		Player p = e.getPlayer();
		
		if(BallsOfSteel.gameState.equals(GameState.LOBBY)){
			if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				
				Material mat = p.getInventory().getItemInMainHand().getType();
				
				if(mat.equals(Material.RED_TERRACOTTA) ||
					mat.equals(Material.BLUE_TERRACOTTA) ||
					mat.equals(Material.YELLOW_TERRACOTTA) ||
					mat.equals(Material.GREEN_TERRACOTTA)){
					
					Team team = BallsOfSteel.materialToTeam(mat);
					
					if(team.getPlayers().size() == team.getMaxplayers()){
						p.sendMessage(Util.getGamePrefix() + "§cThis team is full !");
					}else{
						Team oldTeam = Team.getPlayerTeam(p);
						if(oldTeam != team){
							if(oldTeam != null){
								oldTeam.removePlayer(p);
							}
							
							team.addPlayer(p);

							p.setPlayerListName(team.getColor() + p.getName());
							p.setDisplayName(team.getColor() + p.getName());
							p.sendMessage(Util.getGamePrefix() + "§oYou've just joined " + team.getColor() + team.getName() + " team");
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
							
							for(Player pls : Bukkit.getOnlinePlayers())
								BallsOfSteel.lobbyMenu(pls);

						}
					}
			
				}
			}
		}
		
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			
			Block block = e.getClickedBlock();
			
			if(block != null && (
				block.getType().equals(Material.CHEST) ||
				block.getType().equals(Material.TRAPPED_CHEST) || 
				block.getType().equals(Material.ENCHANTING_TABLE) ||
				block.getType().equals(Material.ANVIL))){
			
				Team chestTeam = Team.getTeamChest(block);
				
				if(chestTeam != null){
					if(!chestTeam.getPlayers().contains(p)){
						p.sendMessage("§cYou can not open this chest !");
						p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
						e.setCancelled(true);
					}
				}
				
				if(BallsOfSteel.gameState.equals(GameState.GAME)){
					for(Team t : BallsOfSteel.teams){
						if(t.getBase().contains(block.getLocation())){
							if(!t.getPlayers().contains(p)){
								e.setCancelled(true);
								p.sendMessage("§cYou can not open opposing team's chest !");
							}	
						}
					}
				}
				
			}
			
		}
		
	}
	
}
