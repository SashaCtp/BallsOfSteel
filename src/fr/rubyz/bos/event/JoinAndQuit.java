package fr.rubyz.bos.event;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameManager;
import fr.rubyz.bos.GameState;
import fr.rubyz.bos.Team;
import fr.rubyz.bos.scoreboard.CustomScoreboardManager;
import fr.rubyz.bos.utils.Util;


public class JoinAndQuit implements Listener{
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e){
		Player p = e.getPlayer();
		
		AttributeInstance ai = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
		if(ai != null){
			ai.setBaseValue(16);
		}
		
		CustomScoreboardManager board = new CustomScoreboardManager(p);
		board.sendLine();
		board.setScoreboard();
		
		for (PotionEffect effect : p.getActivePotionEffects ()){
			p.removePotionEffect (effect.getType());
		}
		
		if(BallsOfSteel.gameState.equals(GameState.LOBBY)){
			
			e.setJoinMessage(Util.getGamePrefix() + p.getName() + " §7joined the game ! " + "§c(" + Bukkit.getOnlinePlayers().size() + "/" + BallsOfSteel.gameMaxplayers + ")");
			
			Util.updateTab();
			
			p.teleport(BallsOfSteel.getSpawn());
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);


			p.sendTitle("§6Balls Of Steel", "§7" + Bukkit.getOnlinePlayers().size() + "/" + BallsOfSteel.gameMaxplayers + " players", 1, 60, 1);

			p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
			
			p.setGameMode(GameMode.ADVENTURE);
			
			BallsOfSteel.getInstance().lobbyMenu(p);

		}else if(BallsOfSteel.gameState.equals(GameState.GAME)){
			Team playerTeam = Team.getPlayerTeam(p);
			
			if(playerTeam != null){
				e.setJoinMessage(Util.getGamePrefix() + p.getName() + " §7joined the game ! " + "§c(" + Bukkit.getOnlinePlayers().size() + "/" + BallsOfSteel.gameMaxplayers + ")");

				Util.updateTab();
				GameManager.giveStuff(p);

				p.teleport(playerTeam.getSpawn());
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
			}else{
				//Set the player in spectator mod
				BallsOfSteel.setSpectatorMode(p);
			}
		}else if(BallsOfSteel.gameState.equals(GameState.FINISH)){
			e.setJoinMessage("");
			p.kickPlayer("§c§lEnd of the game ! \n \n §fplay.rubyzgames.fr");
		}
		
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e){
		Player p = e.getPlayer();
		
		BallsOfSteel.getInstance().sb.remove(p);
		
		for (PotionEffect effect : p.getActivePotionEffects ()){
			p.removePotionEffect (effect.getType ());
		}
		
		Util.updateTab();
		if(BallsOfSteel.gameState.equals(GameState.LOBBY) || BallsOfSteel.gameState.equals(GameState.GAME)){
			e.setQuitMessage(Util.getGamePrefix() + p.getName() + " §7left the game ! " + "§c(" + (Bukkit.getOnlinePlayers().size()-1) + "/" + BallsOfSteel.gameMaxplayers + ")");
		}else if(BallsOfSteel.gameState.equals(GameState.FINISH)){
			e.setQuitMessage("");
		}
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		
		//Clearing the team
		if(Team.getPlayerTeam(p) != null){
			if(BallsOfSteel.gameState.equals(GameState.LOBBY)){
				Team.getPlayerTeam(p).removePlayer(p);
				for(Player pls : Bukkit.getOnlinePlayers()){
					BallsOfSteel.getInstance().lobbyMenu(pls);
				}
			}
		}
		
	}
	
}
