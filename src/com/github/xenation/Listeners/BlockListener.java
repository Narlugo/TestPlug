package com.github.xenation.Listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

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
			for (int i = plugin.zoneMap.get(key).get("Positions").get("pos1").getLoc().getBlockX(); i != plugin.zoneMap.get(key).get("Positions").get("pos2").getLoc().getBlockX(); i++) {
				for (int o = plugin.zoneMap.get(key).get("Positions").get("pos1").getLoc().getBlockZ(); o != plugin.zoneMap.get(key).get("Positions").get("pos2").getLoc().getBlockZ(); o++) {
					if (i == block.getLocation().getBlockX() && o == block.getLocation().getBlockZ() && player.isOp() == false) {
						inZone = true;
						for (Player p: Bukkit.getServer().getOnlinePlayers()) {
							p.sendMessage(ChatColor.GOLD + "DEV -> "
									+ ChatColor.DARK_PURPLE + "Block InZone");
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
			//HashMap Spawn
			ArrayList<Block> delBlocks = new ArrayList<Block>();
			for (Block b: plugin.blocksSet.keySet()) {
				if (System.currentTimeMillis() >= plugin.blocksTimesMap.get(plugin.blocksSet.get(b)) + plugin.blockResTime) {
					plugin.blocksStatesMap.get(plugin.blocksSet.get(b)).update(true);
					delBlocks.add(b);
				}
			}
			for (int d = 0; d != delBlocks.toArray().length; d++) {
				Block b = delBlocks.get(d);
				plugin.blocksTimesMap.remove(plugin.blocksSet.get(b));
				plugin.blocksStatesMap.remove(plugin.blocksSet.get(b));
				plugin.blocksSet.remove(b);
			}
			
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
}
