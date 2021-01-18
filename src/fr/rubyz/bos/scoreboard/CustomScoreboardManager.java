package fr.rubyz.bos.scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameState;
import fr.rubyz.bos.Team;

public class CustomScoreboardManager implements ScoreboardManager{
	
	private Scoreboard scoreboard;
	private Objective objective;
	private String name;
	private Player p;
	
	@SuppressWarnings("deprecation")
	public CustomScoreboardManager(Player p){
		this.p = p;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		if(BallsOfSteel.getInstance().sb.containsKey(p)) return;
		
		this.name = "sb." + new Random().nextInt(9999);
		this.objective = this.scoreboard.registerNewObjective(name, "dummy");
		objective.setDisplayName("§c§lBallsOfSteel");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		
		BallsOfSteel.getInstance().sb.put(p, this);
	}
	
	public void refresh(){
		for(String ligne : scoreboard.getEntries()){
			
			if(ligne.contains("§7Remaining Time : ")){
				scoreboard.resetScores(ligne);
				String lastligne = ligne.split(" : ")[0];
				String date = new SimpleDateFormat("mm:ss").format(new Date(BallsOfSteel.remainingTime*1000));
				String newligne = lastligne + " : " + date;
				objective.getScore(newligne).setScore(2);
			}
			
			if(ligne.contains(" diamonds") || ligne.contains(" diamond")){
				
				scoreboard.resetScores(ligne);
				Team t = BallsOfSteel.getPlayerTeam(p);
				if(t.getDiamonds() <= 1)
					objective.getScore("§b" + t.getDiamonds() + " diamond").setScore(4);
				else
					objective.getScore("§b" + t.getDiamonds() + " diamonds").setScore(4);
				
			}
			
		}
	}
	
	public void sendLine(){
		
		if(GameState.isState(GameState.LOBBY)){
			objective.getScore(" ").setScore(3);
			objective.getScore("§7Waiting for players").setScore(2);
			objective.getScore("  ").setScore(1);
		}
		
		if(GameState.isState(GameState.GAME)){	
			scoreboard.resetScores("§7Waiting for players");
			Team t = BallsOfSteel.getPlayerTeam(p);
			
			objective.getScore(" ").setScore(6);
			objective.getScore(t.getColor() + "Team " + t.getName()).setScore(5);
			
			if(t.getDiamonds() <= 1)
				objective.getScore("§b" + t.getDiamonds() + " diamond").setScore(4);
			else
				objective.getScore("§b" + t.getDiamonds() + " diamonds").setScore(4);
			
			objective.getScore("  ").setScore(3);
			String date = new SimpleDateFormat("mm:ss").format(new Date(BallsOfSteel.remainingTime*1000));
			objective.getScore("§7Remaining Time : "+date).setScore(2);
			objective.getScore("   ").setScore(1);
		}
		
		objective.getScore("§cplay§6.§crubyzgames§6.§cfr").setScore(0);;
		
	}
	
	public void setScoreboard(){
		p.setScoreboard(scoreboard);
	}

	@Override
	public Scoreboard getMainScoreboard() {
		return scoreboard;
	}

	@Override
	public Scoreboard getNewScoreboard() {
		return null;
	}
	
	
	
}
