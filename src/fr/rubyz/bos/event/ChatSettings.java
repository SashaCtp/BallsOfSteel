package fr.rubyz.bos.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameState;
import fr.rubyz.bos.Team;

public class ChatSettings implements Listener{

	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		
		e.setCancelled(true);
		
		String message = e.getMessage();
		
		if(BallsOfSteel.gameState.equals(GameState.LOBBY)){
			String color = "";
			if(BallsOfSteel.getPlayerTeam(p) != null){
				color = BallsOfSteel.getPlayerTeam(p).getColor();
			}
			
			String newMsg = "<" + color + p.getName() + "§r> " + message;
			Bukkit.broadcastMessage(newMsg);
		}else{
			if(message.startsWith("@")){
				//Send the message to the general chat
				
				String color = "";
				if(BallsOfSteel.getPlayerTeam(p) != null){
					color = BallsOfSteel.getPlayerTeam(p).getColor();
				}
				
				String newMsg = color + "(Global) " + p.getName() + " : §r" + message.substring(1);
				Bukkit.broadcastMessage(newMsg);
			}else{
				//Send the message to the team chat
				Team playerTeam = BallsOfSteel.getPlayerTeam(p);
				
				String color = playerTeam.getColor();
				
				String newMsg = color + "(Team) " + p.getName() + " : §r" + message;
				for(Player pls : playerTeam.getPlayers()){pls.sendMessage(newMsg);}
				System.out.println(newMsg);
			}
		}
		
	}
	
}