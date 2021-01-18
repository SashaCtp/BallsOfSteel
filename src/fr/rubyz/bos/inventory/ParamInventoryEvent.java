package fr.rubyz.bos.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.utils.Util;

public class ParamInventoryEvent implements Listener{
	
	public static PotionEffect regen1;
	public static PotionEffect regen2;
	public static PotionEffect regen3;
	
	public static PotionEffect strength1;
	public static PotionEffect strength2;
	public static PotionEffect strength3;
	
	public static PotionEffect speed1;
	public static PotionEffect speed2;
	public static PotionEffect speed3;

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e){
		
		final Player p = (Player) e.getWhoClicked();
		ItemStack item;
		if(e.getCurrentItem() != null){
			item = e.getCurrentItem();
		
			if(e.getView().getTitle().equals("Parameters")){
				e.setCancelled(true);
				if(item.getType().equals(BallsOfSteel.gameConfig.getBuildBlockMaterial())){
					ParamInventory.openParamInventory(1, p);
					p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 2F, 2F);
				}
				if(item.getType().equals(Material.BARRIER)){
					p.closeInventory();
				}
				if(item.getType().equals(Material.BEACON)){
					
					if(BallsOfSteel.gameConfig.isMidProtected()){
						BallsOfSteel.gameConfig.setMidProtected(false);
						Bukkit.broadcastMessage(Util.getGamePrefix() + p.getName() + " disabled mid protection");
					}else{
						BallsOfSteel.gameConfig.setMidProtected(true);
						Bukkit.broadcastMessage(Util.getGamePrefix() + p.getName() + " enabled mid protection");
					}
					p.playSound(p.getLocation(), Sound.ENTITY_ENDERMITE_DEATH, 1F, 1F);
					ParamInventory.openParamInventory(0, p);
					
				}
				if(item.getType().equals(Material.CRAFTING_TABLE)){
					
					if(BallsOfSteel.gameConfig.isInfiniteBuildBlock()){
						BallsOfSteel.gameConfig.setInfiniteBuildBlock(false);
						Bukkit.broadcastMessage(Util.getGamePrefix() + p.getName() + " disabled infinite block");
					}else{
						BallsOfSteel.gameConfig.setInfiniteBuildBlock(true);
						Bukkit.broadcastMessage(Util.getGamePrefix() + p.getName() + " enabled infinite block");
					}
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 3F, 3F);
					ParamInventory.openParamInventory(0, p);
					
				}
				if(item.getType().equals(Material.PAPER)){
					ParamInventory.openParamInventory(2, p);
					p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 2F, 2F);
				}
				if(item.getType().equals(Material.BLAZE_POWDER)){
					ParamInventory.openParamInventory(3, p);
					p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 2F, 2F);
				}
			}else if(e.getView().getTitle().equals("Parameters > Blocks")){
				e.setCancelled(true);
				
				if(item.getType().equals(Material.ARROW)){
					ParamInventory.openParamInventory(0, p);
				}
				
				if(item.getType().equals(Material.DIRT)){
					if(!BallsOfSteel.gameConfig.getBuildBlockMaterial().equals(Material.DIRT)){
						BallsOfSteel.gameConfig.setBuildBlockMaterial(Material.DIRT);
						Bukkit.broadcastMessage(Util.getGamePrefix() + p.getName() + " chose as block by default : §bDirt");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
						ParamInventory.openParamInventory(0, p);
					}
				}else if(item.getType().equals(Material.SANDSTONE)){
					if(!BallsOfSteel.gameConfig.getBuildBlockMaterial().equals(Material.SANDSTONE)){
						BallsOfSteel.gameConfig.setBuildBlockMaterial(Material.SANDSTONE);
						Bukkit.broadcastMessage(Util.getGamePrefix() + p.getName() + " chose as block by default : §bSandstone");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
						ParamInventory.openParamInventory(0, p);
					}
				}else if(item.getType().equals(Material.RED_SANDSTONE)){
					if(!BallsOfSteel.gameConfig.getBuildBlockMaterial().equals(Material.RED_SANDSTONE)){
						BallsOfSteel.gameConfig.setBuildBlockMaterial(Material.RED_SANDSTONE);
						Bukkit.broadcastMessage(Util.getGamePrefix() + p.getName() + " chose as block by default : §bRed Sandstone");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
						ParamInventory.openParamInventory(0, p);
						
					}
				}else if(item.getType().equals(Material.QUARTZ_BLOCK)){
					if(!BallsOfSteel.gameConfig.getBuildBlockMaterial().equals(Material.QUARTZ_BLOCK)){
						BallsOfSteel.gameConfig.setBuildBlockMaterial(Material.QUARTZ_BLOCK);
						Bukkit.broadcastMessage(Util.getGamePrefix() + p.getName() + " chose as block by default : §bQuartz Block");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
						ParamInventory.openParamInventory(0, p);
					}
				}else if(item.getType().equals(Material.WHITE_STAINED_GLASS)){
					if(!BallsOfSteel.gameConfig.getBuildBlockMaterial().equals(Material.WHITE_STAINED_GLASS)){
						BallsOfSteel.gameConfig.setBuildBlockMaterial(Material.WHITE_STAINED_GLASS);
						Bukkit.broadcastMessage(Util.getGamePrefix() + p.getName() + " chose as block by default : §bGlass Block");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
						ParamInventory.openParamInventory(0, p);
					}
				}
				
				
			}else if(e.getView().getTitle().equals("Parameters > Scenarios")){
				e.setCancelled(true);
				if(item.getType().equals(Material.ARROW))
					ParamInventory.openParamInventory(0, p);
			}else if(e.getView().getTitle().equals("Parameters > Effects")){				
				e.setCancelled(true);
				if(item.getType().equals(Material.ARROW))
					ParamInventory.openParamInventory(0, p);
				
				regen1 = new PotionEffect(PotionEffectType.REGENERATION, 100000, 0, true);
				regen2 = new PotionEffect(PotionEffectType.REGENERATION, 100000, 1, true);
				regen3 = new PotionEffect(PotionEffectType.REGENERATION, 100000, 2, true);
				
				strength1 = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000, 0, true);
				strength2 = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000, 1, true);
				strength3 = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100000, 2, true);
				
				speed1 = new PotionEffect(PotionEffectType.SPEED, 100000, 0, true);
				speed2 = new PotionEffect(PotionEffectType.SPEED, 100000, 1, true);
				speed3 = new PotionEffect(PotionEffectType.SPEED, 100000, 2, true);
				
				//Regen
				
				
				String itemName = item.getItemMeta().getDisplayName();
				
				if(itemName.equals("§5Rgnration I") || itemName.equals("§5Regeneration I - §aActive")){
					if(BallsOfSteel.effectList.contains(regen1)){
						BallsOfSteel.effectList.remove(regen1);
					}else{
						if(BallsOfSteel.effectList.contains(regen2))
							BallsOfSteel.effectList.remove(regen2);
						else if(BallsOfSteel.effectList.contains(regen3))
							BallsOfSteel.effectList.remove(regen3);
						
						BallsOfSteel.effectList.add(regen1);
					}
					p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1F, 1F);
					ParamInventory.openParamInventory(3, p);
				}else if(itemName.equals("§5Rgnration II") || itemName.equals("§5Regeneration II - §aActive")){
					if(BallsOfSteel.effectList.contains(regen2)){
						BallsOfSteel.effectList.remove(regen2);
					}else{
						if(BallsOfSteel.effectList.contains(regen1))
							BallsOfSteel.effectList.remove(regen1);
						else if(BallsOfSteel.effectList.contains(regen3))
							BallsOfSteel.effectList.remove(regen3);
						
						BallsOfSteel.effectList.add(regen2);
					}
					p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1F, 1F);
					ParamInventory.openParamInventory(3, p);
				}else if(itemName.equals("§5Rgnration III") || itemName.equals("§5Regeneration III - §aActive")){
					if(BallsOfSteel.effectList.contains(regen3)){
						BallsOfSteel.effectList.remove(regen3);
					}else{
						if(BallsOfSteel.effectList.contains(regen1))
							BallsOfSteel.effectList.remove(regen1);
						else if(BallsOfSteel.effectList.contains(regen2))
							BallsOfSteel.effectList.remove(regen2);
						
						BallsOfSteel.effectList.add(regen3);
					}
					p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1F, 1F);
					ParamInventory.openParamInventory(3, p);
				}
				
				//Force
				
				if(itemName.equals("§cForce I") || itemName.equals("§cStrength I - §aActive")){
					if(BallsOfSteel.effectList.contains(strength1)){
						BallsOfSteel.effectList.remove(strength1);
					}else{
						if(BallsOfSteel.effectList.contains(strength2))
							BallsOfSteel.effectList.remove(strength2);
						else if(BallsOfSteel.effectList.contains(strength3))
							BallsOfSteel.effectList.remove(strength3);
						
						BallsOfSteel.effectList.add(strength1);
					}
					p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1F, 1F);
					ParamInventory.openParamInventory(3, p);
				}else if(itemName.equals("§cForce II") || itemName.equals("§cStrength II - §aActive")){
					if(BallsOfSteel.effectList.contains(strength2)){
						BallsOfSteel.effectList.remove(strength2);
					}else{
						if(BallsOfSteel.effectList.contains(strength1))
							BallsOfSteel.effectList.remove(strength1);
						else if(BallsOfSteel.effectList.contains(strength3))
							BallsOfSteel.effectList.remove(strength3);
						
						BallsOfSteel.effectList.add(strength2);
					}
					p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1F, 1F);
					ParamInventory.openParamInventory(3, p);
				}else if(itemName.equals("§cForce III") || itemName.equals("§cStrength I - §aActive")){
					if(BallsOfSteel.effectList.contains(strength3)){
						BallsOfSteel.effectList.remove(strength3);
					}else{
						if(BallsOfSteel.effectList.contains(strength1))
							BallsOfSteel.effectList.remove(strength1);
						else if(BallsOfSteel.effectList.contains(strength2))
							BallsOfSteel.effectList.remove(strength2);
						
						BallsOfSteel.effectList.add(strength3);
					}
					p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1F, 1F);
					ParamInventory.openParamInventory(3, p);
				}
				
				//Vitesse
				
				if(itemName.equals("§3Vitesse I") || itemName.equals("§3Speed I - §aActive")){
					if(BallsOfSteel.effectList.contains(speed1)){
						BallsOfSteel.effectList.remove(speed1);
					}else{
						if(BallsOfSteel.effectList.contains(speed2))
							BallsOfSteel.effectList.remove(speed2);
						else if(BallsOfSteel.effectList.contains(speed3))
							BallsOfSteel.effectList.remove(speed3);
						
						BallsOfSteel.effectList.add(speed1);
					}
					p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1F, 1F);
					ParamInventory.openParamInventory(3, p);
				}else if(itemName.equals("§3Vitesse II") || itemName.equals("§3Speed II - §aActive")){
					if(BallsOfSteel.effectList.contains(speed2)){
						BallsOfSteel.effectList.remove(speed2);
					}else{
						if(BallsOfSteel.effectList.contains(speed1))
							BallsOfSteel.effectList.remove(speed1);
						else if(BallsOfSteel.effectList.contains(speed3))
							BallsOfSteel.effectList.remove(speed3);
						
						BallsOfSteel.effectList.add(speed2);
					}
					p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1F, 1F);
					ParamInventory.openParamInventory(3, p);
				}else if(itemName.equals("§3Vitesse III") || itemName.equals("§3Speed III - §aActive")){
					if(BallsOfSteel.effectList.contains(speed3)){
						BallsOfSteel.effectList.remove(speed3);
					}else{
						if(BallsOfSteel.effectList.contains(speed1))
							BallsOfSteel.effectList.remove(speed1);
						else if(BallsOfSteel.effectList.contains(speed2))
							BallsOfSteel.effectList.remove(speed2);
						
						BallsOfSteel.effectList.add(speed3);
					}
					p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1F, 1F);
					ParamInventory.openParamInventory(3, p);
				}
				
			}
			
		}
		
	}
	
}
