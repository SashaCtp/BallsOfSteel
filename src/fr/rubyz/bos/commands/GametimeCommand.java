package fr.rubyz.bos.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.rubyz.bos.BallsOfSteel;

public class GametimeCommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player){

			Player p = (Player) sender;

			if(p.isOp()){

				if(args.length == 2){
					
					int time = 60;
					String action = args[0];
						
					try{
						time = Integer.parseInt(args[1]);
							
						if(action.equalsIgnoreCase("set")){
							BallsOfSteel.remainingTime = time;
						}
							
						if(action.equalsIgnoreCase("add")){
							BallsOfSteel.remainingTime = BallsOfSteel.remainingTime + time;
						}
							
						if(action.equalsIgnoreCase("remove")){
							BallsOfSteel.remainingTime = BallsOfSteel.remainingTime - time;
						}
							
					}catch(Exception e){
						p.sendMessage("§cEnter a correct value please");
					}
						
				}else{
					p.sendMessage("§cUsage : /gametime <action> <time>");
				}
					
			}else{
				p.sendMessage("§cYou do not have the permission !");
			}
		
		}
		
		return true;
		
	}

}
