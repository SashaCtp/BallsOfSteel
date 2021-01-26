package fr.rubyz.bos.event;

import fr.rubyz.bos.GameState;
import fr.rubyz.bos.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.rubyz.bos.BallsOfSteel;
import org.bukkit.event.player.PlayerMoveEvent;

public class Kills implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {

		BallsOfSteel.gameStats.addDeath(e.getEntity());

		if (e.getEntity().getKiller() != null) {

			BallsOfSteel.gameStats.addKill(e.getEntity().getKiller());

		}

	}

	@EventHandler
	public void onMove(PlayerMoveEvent e){

		Player p = e.getPlayer();

		if(p.getLocation().getY() <= 0){

			if(BallsOfSteel.gameState.equals(GameState.GAME)){

				p.setHealth(0);

			}else if(BallsOfSteel.gameState.equals(GameState.FINISH)){

				Team t = Team.getTeamOfPlayer(p);

				if(t == null)
					p.teleport(BallsOfSteel.getSpawn());
				else
					p.teleport(t.getSpawn());

			}

		}



	}

}
