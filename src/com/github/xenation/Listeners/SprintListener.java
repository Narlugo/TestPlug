package com.github.xenation.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import com.github.xenation.testplug.TestPlug;

public class SprintListener implements Listener {
	
	//Constructor
	public TestPlug plugin;
	public SprintListener(TestPlug plugin) {
		this.plugin = plugin;
	}
	
	boolean isSprinting = false;
	
	@EventHandler
	public void onPlayerToggleSprint (PlayerToggleSprintEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();
		
		if (isSprinting == false) {
			isSprinting = true;
			player.sendMessage(ChatColor.GOLD + "DEV -> "
					+ ChatColor.DARK_AQUA + "Started Sprinting at: " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
		} else {
			isSprinting = false;
			player.sendMessage(ChatColor.GOLD + "DEV -> "
					+ ChatColor.DARK_AQUA + "Ended Sprinting at: " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
		}
		
		
		
	}
	
	/*public void GlassBreak (Location location) {
		while (isSprinting == true) {
			Location frontTop = location;
			Location frontMid = location;
			Location frontDown = location;
			
			if (location.getYaw() > -45 && location.getYaw() < 45) {
				//Front
				frontTop.setX(location.getX());
				frontTop.setY(location.getY() + 2);
				frontTop.setZ(location.getZ() + 1);
				
				//Mid
				frontMid.setX(location.getX());
				frontMid.setY(location.getY() + 1);
				frontMid.setZ(location.getZ() + 1);
				
				//Down
				frontDown.setX(location.getX());
				frontDown.setY(location.getY());
				frontDown.setZ(location.getZ() + 1);
			}
			
			Block frontTopB = frontTop.getBlock();
			Block frontMidB = frontMid.getBlock();
			Block frontDownB = frontDown.getBlock();
			
			if (frontTopB.toString() == "!!org.bukkit.Material 'GLASS'") {
				frontTopB.breakNaturally();
			}
			if (frontMidB.toString() == "!!org.bukkit.Material 'GLASS'") {
				frontMidB.breakNaturally();
			}
			if (frontDownB.toString() == "!!org.bukkit.Material 'GLASS'") {
				frontDownB.breakNaturally();
			}
		}
	}*/
}
