package fr.rubyz.bos.event;

import fr.rubyz.bos.GameState;
import fr.rubyz.bos.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.rubyz.bos.BallsOfSteel;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class Kills implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {

		BallsOfSteel.gameStats.addDeath(e.getEntity());

		if (e.getEntity().getKiller() != null) {

			BallsOfSteel.gameStats.addKill(e.getEntity().getKiller());

			e.setDeathMessage("> " + Team.getPlayerTeam(e.getEntity().getKiller()).getColor() + e.getEntity().getKiller().getName() +  " §fkilled " + Team.getPlayerTeam(e.getEntity()).getColor() + e.getEntity().getName());

		}else if(e.getEntity().getLocation().getY() <= 0){

			e.setDeathMessage("> " + Team.getPlayerTeam(e.getEntity()).getColor() + Objects.requireNonNull(e.getEntity().getPlayer()).getName() + "§f fell into the void !");

		}else{

			e.setDeathMessage("> " + Team.getPlayerTeam(e.getEntity()).getColor() + Objects.requireNonNull(e.getEntity().getPlayer()).getName() + "§f died !");

		}

	}

}
