package fr.rubyz.bos;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_16_R3.NBTTagCompound;



public class DiamondIndicator {

	private Team team;
	private Location location;
	private Entity armorStand;
	private boolean isSpawned;
	
	public DiamondIndicator(Team team, Location location){
		this.team = team;
		this.location = location;
		this.isSpawned = false;
	}
	
	public void spawn(){
		this.armorStand = Bukkit.getWorlds().get(0).spawnEntity(this.location, EntityType.ARMOR_STAND);
		
		if(this.team.getDiamonds() <= 1)
			this.armorStand.setCustomName(team.getColor() + team.getDiamonds() + " diamond in the chest");
		else
			this.armorStand.setCustomName(team.getColor() + team.getDiamonds() + " diamonds in the chest");

		this.armorStand.setCustomNameVisible(true);

		armorStand.setGravity(false);
		armorStand.setInvulnerable(true);
		armorStand.setSilent(true);

		this.isSpawned = true;
	}
	
	public void update(){
		if(this.isSpawned){
			if(this.team.getDiamonds() <= 1)
				this.armorStand.setCustomName(this.team.getColor() + this.team.getDiamonds() + " diamond in the chest");
			else
				this.armorStand.setCustomName(this.team.getColor() + this.team.getDiamonds() + " diamonds in the chest");
		}
	}
	
	public void remove(){
		if(this.isSpawned)
			this.armorStand.remove();
	}
	
}
