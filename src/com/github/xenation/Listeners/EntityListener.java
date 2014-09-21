package com.github.xenation.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.github.xenation.testplug.TestPlug;

public class EntityListener implements Listener {
	
	public TestPlug plugin;
	
	public EntityListener(TestPlug plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityExplode(EntityExplodeEvent event) {
		Entity entity = event.getEntity();
		boolean inZone = false;
		
		for (Player p: Bukkit.getServer().getOnlinePlayers()) {
			p.sendMessage(ChatColor.GOLD + "DEV -> "
					+ ChatColor.DARK_PURPLE + "Entity = " + entity.toString());
		}
		
		for (String key: plugin.zoneMap.keySet()) {
			for (int i = plugin.zoneMap.get(key).get("Positions").get("pos1").getLoc().getBlockX(); i != plugin.zoneMap.get(key).get("Positions").get("pos2").getLoc().getBlockX(); i++) {
				for (int o = plugin.zoneMap.get(key).get("Positions").get("pos1").getLoc().getBlockZ(); o != plugin.zoneMap.get(key).get("Positions").get("pos2").getLoc().getBlockZ(); o++) {
					if (i == entity.getLocation().getBlockX() && o == entity.getLocation().getBlockZ()) {
						inZone = true;
						for (Player p: Bukkit.getServer().getOnlinePlayers()) {
							p.sendMessage(ChatColor.GOLD + "DEV -> "
									+ ChatColor.DARK_PURPLE + "Entity InZone");
						}
					}
				}
			}
		}
		
		for (Player p: Bukkit.getServer().getOnlinePlayers()) {
			p.sendMessage(ChatColor.GOLD + "DEV -> "
					+ ChatColor.DARK_PURPLE + "Entity Exploding");
		}
		
		if (inZone == true) {
			event.setCancelled(true);
			for (Player p: Bukkit.getServer().getOnlinePlayers()) {
				p.sendMessage(ChatColor.GOLD + "DEV -> "
						+ ChatColor.DARK_PURPLE + "Explosion Cancelled");
			}
		}
	}
	
}
