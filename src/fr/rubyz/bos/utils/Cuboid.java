package fr.rubyz.bos.utils;

import org.bukkit.Location;

public class Cuboid {
	
	private double x1;
	private double y1;
	private double z1;
	private double x2;
	private double y2;
	private double z2;
	
	/*
	 * Variables "1" are always the biggest
	 * and the "2" are the smaler
	 */
	
	public Cuboid(Location loc1, Location loc2){
		
		double x1 = loc1.getX();
		double x2 = loc2.getX();
		
		double y1 = loc1.getY();
		double y2 = loc2.getY();
		
		double z1 = loc1.getZ();
		double z2 = loc2.getZ();
		
		if(x1 < x2){
			this.x1 = x2;
			this.x2 = x1;
		}else{
			this.x1 = x1;
			this.x2 = x2;
		}
		
		if(y1 < y2){
			this.y1 = y2;
			this.y2 = y1;
		}else{
			this.y1 = y1;
			this.y2 = y2;
		}
		
		if(z1 < z2){
			this.z1 = z2;
			this.z2 = z1;
		}else{
			this.z1 = z1;
			this.z2 = z2;
		}
	}
	
	public boolean contains(Location loc){
		
		boolean contains = false;
		
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		
		if(x <= this.x1 && x >= this.x2){
			if(y <= this.y1 && y >= this.y2){
				if(z <= this.z1 && z >= this.z2){
					contains = true;
				}
			}
		}
		
		return contains;
		
	}
	
}
