package com.github.xenation.testplug;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Param {
	
	String param;
	
	public Param(Location loc) {
		param = loc.getX() + "/" + loc.getY() + "/" + loc.getZ() + "/" + loc.getPitch() + "/" + loc.getYaw();
	}
	
	public Param(boolean bool) {
		param = Boolean.toString(bool);
	}
	
	public boolean getBool() {
		boolean bool = Boolean.valueOf(param);
		return bool;
	}
	
	public Location getLoc() {
		Player listP[] = Bukkit.getOnlinePlayers();
		Player p = listP[0].getPlayer();
		Location loc = p.getLocation();
		
		String params[] = param.split("/");
		//p.sendMessage("params[] = "+params[0]+", "+params[1]+", "+params[2]+", "+params[3]+", "+params[4]+"<-");
		
		loc.setX(Double.parseDouble(params[0]));
		loc.setY(Double.parseDouble(params[1]));
		loc.setZ(Double.parseDouble(params[2]));
		loc.setPitch(Float.parseFloat(params[3]));
		loc.setYaw(Float.parseFloat(params[4]));
		
		return loc;
	}
	
	public void set(double X, double Y, double Z, float Pitch, float Yaw) {
		param = X + "/" + Y + "/" + Z + "/" + Pitch + "/" + Yaw;
	}
}
