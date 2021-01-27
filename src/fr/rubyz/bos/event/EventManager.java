package fr.rubyz.bos.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import fr.rubyz.bos.inventory.ParamInventoryEvent;

public class EventManager implements Listener{

	public static void registerEvents(Plugin pl){
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new JoinAndQuit(), pl);
		pm.registerEvents(new Events(), pl);
		pm.registerEvents(new Kills(), pl);
		pm.registerEvents(new ParamInventoryEvent(), pl);
		pm.registerEvents(new ChatSettings(), pl);
		pm.registerEvents(new PlayerInteract(), pl);
		pm.registerEvents(new PlayerMove(), pl);
		pm.registerEvents(new Weather(), pl);
	}
	
}
