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
		String color = "";
		Team t = Team.getPlayerTeam(p);
		if(t != null)
			color = t.getColor();
		
		if(BallsOfSteel.gameState.equals(GameState.LOBBY)){

			String newMsg = "<" + color + p.getName() + "§r> " + message;
			Bukkit.broadcastMessage(newMsg);

		}else{

			// If spectator
			if(BallsOfSteel.spectator.contains(p) || t == null) {

				String newMsg =  "§7(Spec) " + p.getName() + " : §7§o" + message.substring(1);

				if(BallsOfSteel.gameState.equals(GameState.GAME)) {
					for (Player spec : BallsOfSteel.spectator)
						spec.sendMessage(newMsg);

					System.out.println(newMsg);
				}else
					Bukkit.broadcastMessage(newMsg);

			}else{
				if (message.startsWith("@")) {

					//Send the message to the general chat

					String newMsg = color + "(Global) " + p.getName() + " : §r" + message.substring(1);
					Bukkit.broadcastMessage(newMsg);

				} else {

					//Send the message to the team chat

					String newMsg = color + "(Team) " + p.getName() + " : §r" + message;

					for (Player pls : t.getPlayers())
						pls.sendMessage(newMsg);

					System.out.println(newMsg);

				}
			}
		}
		
	}
	
}