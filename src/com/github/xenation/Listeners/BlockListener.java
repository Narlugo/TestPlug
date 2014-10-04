package com.github.xenation.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.github.xenation.testplug.TestPlug;

public class BlockListener implements Listener{
	
	public TestPlug plugin;
	
	public BlockListener(TestPlug plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		
		boolean inZone = false;
		
		for (String key: plugin.zoneMap.keySet()) {
			if (plugin.zoneMap.get(key).get("Params").get("Break").getBool() == false) {
				for (int i = plugin.zoneMap.get(key).get("Positions").get("pos1").getLoc().getBlockX(); i != plugin.zoneMap.get(key).get("Positions").get("pos2").getLoc().getBlockX(); i++) {
					for (int o = plugin.zoneMap.get(key).get("Positions").get("pos1").getLoc().getBlockZ(); o != plugin.zoneMap.get(key).get("Positions").get("pos2").getLoc().getBlockZ(); o++) {
						if (i == block.getLocation().getBlockX() && o == block.getLocation().getBlockZ() /*&& player.isOp() == false*/) {
							inZone = true;
							for (Player p: Bukkit.getServer().getOnlinePlayers()) {
								p.sendMessage(ChatColor.GOLD + "DEV -> "
										+ ChatColor.DARK_PURPLE + "Block InZone");
							}
						}
					}
				}
			}
		}
		
		if (inZone == true) {
			event.setCancelled(true);
		}
		
		//Block Re-spawn
		if (plugin.blockResLock == true) {
			//Saves the breaked block
			//HashMap Save
			double unique = Math.random();
			while (plugin.blocksSet.containsValue(unique) == true) {
				unique = Math.random();
			}
			plugin.blocksSet.put(block, unique);
			plugin.blocksStatesMap.put(unique, block.getState());
			plugin.blocksTimesMap.put(unique, System.currentTimeMillis());
			
			//BreakLog update notifier
			for (Player p: Bukkit.getServer().getOnlinePlayers()) {
				if (p.getName().equalsIgnoreCase("Xenation")) {
					p.sendMessage(ChatColor.GOLD + "DEV -> "
							+ ChatColor.DARK_PURPLE + "BreakLog Updated! Breaker: " + player.getName());
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace (BlockPlaceEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		
		boolean inZone = false;
		for (String key: plugin.zoneMap.keySet()) {
			if (plugin.zoneMap.get(key).get("Params").get("Build").getBool() == false) {
				for (int i = plugin.zoneMap.get(key).get("Positions").get("pos1").getLoc().getBlockX(); i != plugin.zoneMap.get(key).get("Positions").get("pos2").getLoc().getBlockX(); i++) {
					for (int o = plugin.zoneMap.get(key).get("Positions").get("pos1").getLoc().getBlockZ(); o != plugin.zoneMap.get(key).get("Positions").get("pos2").getLoc().getBlockZ(); o++) {
						if (i == block.getLocation().getBlockX() && o == block.getLocation().getBlockZ() /*&& player.isOp() == false*/) {
							inZone = true;
							for (Player p: Bukkit.getServer().getOnlinePlayers()) {
								p.sendMessage(ChatColor.GOLD + "DEV -> "
										+ ChatColor.DARK_PURPLE + "Block InZone (place)");
							}
						}
					}
				}
			}
		}
		
		if (inZone == true) {
			event.setCancelled(true);
		}
		
		player.sendMessage(ChatColor.GOLD + "DEV -> "
				+ ChatColor.DARK_PURPLE + "Block Placed!");
	}
}
