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

		// Refreshing the scoreboard
		for(Entry<Player, CustomScoreboardManager> scoreboard : BallsOfSteel.getInstance().sb.entrySet()){
			CustomScoreboardManager board = scoreboard.getValue();
			board.refresh();
		}

		// Countdown in the lobby
		if(BallsOfSteel.gameState.equals(GameState.LOBBY)){

			boolean sufficientPlayer = (Bukkit.getOnlinePlayers().size() >= 4*2);

			if(sufficientPlayer && !BallsOfSteel.lobbyCountDown.started())
				BallsOfSteel.lobbyCountDown.start(false);
			else if(BallsOfSteel.lobbyCountDown.started()){

				if(!sufficientPlayer && !BallsOfSteel.lobbyCountDown.forceStarted())
					BallsOfSteel.lobbyCountDown.stop(Util.getGamePrefix() + " Not enough player to launch !");
				else if(BallsOfSteel.lobbyCountDown.finished())
					GameManager.start();
				else {

					BallsOfSteel.lobbyCountDown.displayCountdown();
					BallsOfSteel.lobbyCountDown.decrement();

				}

			}

		}
		
		// Updating the Tab, Diamonds Indicator & remaining time
		if(BallsOfSteel.gameState.equals(GameState.GAME)){

			Util.updateTab();

			for(Team t : BallsOfSteel.teams){
				t.refreshDiamondCount();
				t.getDiamondIndicator().update();
			}

			announceRemainingTime();

			if(BallsOfSteel.remainingTime == 0){
				GameManager.stop();
				return;
			}

			BallsOfSteel.remainingTime--;
		}
		
	}

	public static void announceRemainingTime(){

		String message = null;
		int min = BallsOfSteel.remainingTime/60;
		int round = BallsOfSteel.remainingTime%60;

		if((min == 20 || min == 10 || min == 5) && round == 0)
			message = "§c" + min + " minutes§f remaining !";

		if(BallsOfSteel.remainingTime == 60)
			message = "§fThe game ends in §c1 minute§f !";

		if(BallsOfSteel.remainingTime == 10 || BallsOfSteel.remainingTime == 30 || (BallsOfSteel.remainingTime <= 5 && BallsOfSteel.remainingTime > 1))
			message = "§fThe game ends in §c" + BallsOfSteel.remainingTime + " seconds§f !";

		if(BallsOfSteel.remainingTime == 1)
			message = "§fThe game ends in §c" + BallsOfSteel.remainingTime + " second§f !";

		if(message != null){
			Bukkit.broadcastMessage(Util.getGamePrefix() + message);

			for(Player pls : Bukkit.getOnlinePlayers())
				pls.playSound(pls.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.3F, 1F);
		}


	}
	
}
