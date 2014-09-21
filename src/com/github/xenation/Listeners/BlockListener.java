package com.github.xenation.Listeners;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.xenation.testplug.TestPlug;

public class BlockListener implements Listener{
	
	public File folder;
	public File breakfile;
	public YamlConfiguration breakconfig;
	
	public TestPlug plugin;
	
	public BlockListener(TestPlug plugin) {
		this.plugin = plugin;
		
		this.folder = plugin.getDataFolder();
		this.breakfile = new File(folder, "BreakLog.yml");
		this.breakconfig = new YamlConfiguration();
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
			for (String blockPos: breakconfig.getKeys(false)) {
				if (System.currentTimeMillis() >= breakconfig.getLong(blockPos+".Time") + plugin.blockResTime) {
					String pos[] = blockPos.split("/");
					World w = Bukkit.getWorld("world");
					Location loc = Bukkit.getWorld("world").getSpawnLocation();
					loc.zero();
					loc.setWorld(w);
					loc.setX(Integer.valueOf(pos[0]));
					loc.setY(Integer.valueOf(pos[1]));
					loc.setZ(Integer.valueOf(pos[2]));
					Block b = loc.getBlock();
					b.setType(Material.valueOf(breakconfig.getString(blockPos+".Type")));
					player.sendMessage("Block respawn at: "+b.getLocation().getBlockX()+", "+b.getLocation().getBlockY()+", "+b.getLocation().getBlockZ());
					try {
						breakconfig.load(breakfile);
					} catch (IOException | InvalidConfigurationException e) {
						e.printStackTrace();
					}
					breakconfig.set(blockPos, null);
					try {
						breakconfig.save(breakfile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			try {
				breakconfig.load(breakfile);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			ConfigurationSection blockSec = breakconfig.createSection(block.getX() + "/" + block.getY() + "/" + block.getZ());
			blockSec.set("Type", block.getType().toString());
			blockSec.set("Time", System.currentTimeMillis());
			try {
				breakconfig.save(breakfile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (Player p: Bukkit.getServer().getOnlinePlayers()) {
				if (p.getName().equalsIgnoreCase("Xenation")) {
					p.sendMessage(ChatColor.GOLD + "DEV -> "
							+ ChatColor.DARK_PURPLE + "BreakLog Updated! Breaker: " + player.getName());
				}
			}
		}
	}
	
}
