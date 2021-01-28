package fr.rubyz.bos.event;

import fr.rubyz.bos.Team;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.rubyz.bos.BallsOfSteel;

import java.util.Objects;

public class Kills implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {

		BallsOfSteel.gameStats.addDeath(e.getEntity());

		String playerColor = "";
		Team t = Team.getPlayerTeam(e.getEntity().getPlayer());

		if(t != null)
			playerColor = t.getColor();

		if (e.getEntity().getKiller() != null) {

			BallsOfSteel.gameStats.addKill(e.getEntity().getKiller());

			String killerColor = "";
			Team tk = Team.getPlayerTeam(e.getEntity().getPlayer());

			if(tk != null)
				killerColor = tk.getColor();


			e.setDeathMessage("> " + killerColor + e.getEntity().getKiller().getName() +  " §fkilled " + playerColor + e.getEntity().getName());

		}else if(e.getEntity().getLocation().getY() <= 0){

			e.setDeathMessage("> " + playerColor + Objects.requireNonNull(e.getEntity().getPlayer()).getName() + "§f fell into the void !");

		}else{

			e.setDeathMessage("> " + playerColor + Objects.requireNonNull(e.getEntity().getPlayer()).getName() + "§f died !");

		}

	}

}
