package fr.rubyz.bos;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_16_R3.NBTTagCompound;



public class DiamondIndicator {

	private Team team;
	private Location location;
	private ArmorStand armorStand;
	private boolean isSpawned;
	
	public DiamondIndicator(Team team, Location location){
		this.team = team;
		this.location = location;
		this.isSpawned = false;
	}

	/**
	 * Display the diamond indicator
	 */
	public void spawn(){
		this.armorStand = (ArmorStand) Bukkit.getWorlds().get(0).spawnEntity(this.location, EntityType.ARMOR_STAND);
		
		if(this.team.getDiamonds() <= 1)
			this.armorStand.setCustomName(team.getColor() + team.getDiamonds() + " diamond in the chest");
		else
			this.armorStand.setCustomName(team.getColor() + team.getDiamonds() + " diamonds in the chest");

		this.armorStand.setCustomNameVisible(true);

		this.armorStand.setGravity(false);
		this.armorStand.setInvulnerable(true);
		this.armorStand.setSilent(true);
		this.armorStand.setVisible(false);

		this.isSpawned = true;
	}

	/**
	 * Update the diamond indicator
	 */
	public void update(){
		if(this.isSpawned){
			if(this.team.getDiamonds() <= 1)
				this.armorStand.setCustomName(this.team.getColor() + this.team.getDiamonds() + " diamond in the chest");
			else
				this.armorStand.setCustomName(this.team.getColor() + this.team.getDiamonds() + " diamonds in the chest");
		}
	}

	/**
	 * Remove the diamond indicator from the team's base
	 */
	public void remove(){
		if(this.isSpawned)
			this.armorStand.remove();
	}
	
}
