package fr.rubyz.bos.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.rubyz.bos.BallsOfSteel;
import fr.rubyz.bos.GameState;
import fr.rubyz.bos.Team;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle.EnumTitleAction;

public class Util {
	
	
	//Chat Util
	
	private static String gamePrefix = "§7[§9§lBallsOfSteel§7]§r ";
	public static String getGamePrefix(){
		return gamePrefix;
	}
	
	public static ItemStack make(Material material, int amount, String name, List<String> lore){
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        assert(itemMeta != null);
        if(name != null){
        	itemMeta.setDisplayName(name);
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        item.setAmount(amount);
        return item;
    }
	
	public static ItemStack makePotionHidePotionEffect(Material material, byte data, int amount, String name, List<String> lore){
        @SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(material, amount, data);
        ItemMeta itemMeta = item.getItemMeta();
        assert(itemMeta != null);
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(itemMeta);
        item.setAmount(amount);
        return item;
    }
    
    //Item Util
    public static ItemStack addGlow(ItemStack item) {
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }
		
	private static void sendTime(Player player, int ticks) {
		PacketPlayOutTitle p = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, 20, ticks, 20);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
	}
	
	//Tab Util
	
	public static void updateTab(){
		
		String state = null;
		StringBuilder ranking = new StringBuilder(" \n");
		
		for(Player pls : Bukkit.getOnlinePlayers()){
		
			if(GameState.isState(GameState.LOBBY)){
				state = "Waiting for players - " + Bukkit.getOnlinePlayers().size() + "/" + BallsOfSteel.gameMaxplayers + " joueurs";
			}else if(GameState.isState(GameState.GAME)){
				if(BallsOfSteel.getPlayerTeam(pls) != null){
					if(BallsOfSteel.getPlayerTeam(pls).getDiamonds() <= 1){
						state = "In game - " + Bukkit.getOnlinePlayers().size() + "/" + BallsOfSteel.gameMaxplayers + " joueurs - " + BallsOfSteel.getPlayerTeam(pls).getColor() + BallsOfSteel.getPlayerTeam(pls).getDiamonds() + " diamond";
					}else{
						state = "In game - " + Bukkit.getOnlinePlayers().size() + "/" + BallsOfSteel.gameMaxplayers + " joueurs - " + BallsOfSteel.getPlayerTeam(pls).getColor() + BallsOfSteel.getPlayerTeam(pls).getDiamonds() + " diamonds";
					}
				}else{
					state = "In game";
				}
				
				//Classement en fonction du nombre de diamants
				for(Team t : BallsOfSteel.teams){
                    ranking.append(t.getColor()).append("§l█ §b").append(t.getDiamonds()).append(" §l♦ §r ");
				}
				
				
			}
			
			pls.setPlayerListHeader("§c§lRubyzGames §7- §6Balls Of Steel\n §7" + state + " \n");
			pls.setPlayerListFooter(ranking +" \n§6 play.rubyzgames.fr  -  www.rubyzgames.fr  -  ts.rubyzgames.fr ");
		
		}
		
	}
	
}
