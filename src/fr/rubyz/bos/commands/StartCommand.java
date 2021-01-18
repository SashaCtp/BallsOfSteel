package fr.rubyz.bos.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.rubyz.bos.GameManager;

public class StartCommand  implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.isOp()){
			
			GameManager.start();
		
		}
	
		return true;
		
	}

}
