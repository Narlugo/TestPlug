package com.github.xenation.testplug;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xenation.Listeners.BlockListener;
import com.github.xenation.Listeners.ChatListener;
import com.github.xenation.Listeners.EntityListener;
import com.github.xenation.Listeners.JoinListener;
import com.github.xenation.Listeners.KillListener;
import com.github.xenation.Listeners.WeatherListener;

public class TestPlug extends JavaPlugin {
	
	//HashMaps
	public HashMap<String, String> chat = new HashMap<String, String>();
	public HashMap<String, HashMap<String, HashMap<String, Param>>> zoneMap = new HashMap<String, HashMap<String, HashMap<String, Param>>>();
	public HashMap<Block, Double> blocksSet = new HashMap<Block, Double>();
	public HashMap<Double, BlockState> blocksStatesMap = new HashMap<Double, BlockState>();
	public HashMap<Double, Long> blocksTimesMap = new HashMap<Double, Long>();
	
	//booleans
	public boolean weatherLock = true;
	public boolean blockResLock = true;
	public boolean allowSprintBreak = true;
	
	//Numeric Values
	public long blockResTime = 120000L;
	
	//Strings
	public String joinMessage = "";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	//Called when plugin starts
	public void onEnable() {
		
		//Plugin Folder
		File folder = this.getDataFolder();
		
		/*
		 * FILES
		 */
		//Configuration.yml File
		File config = new File(folder, "Configuration.yml");
		YamlConfiguration cfg = new YamlConfiguration();
		//Zones.yml File
		File zonesFile = new File(folder, "Zones.yml");
		YamlConfiguration zonesCfg = new YamlConfiguration();
		
		/*
		 * CHECKS
		 */
		//checks if the plugin folder exists
		if (!(folder).exists()) {
			folder.mkdir();
		}
		//checks if the Configuration.yml file doesn't exists
		if (!(config).exists()) {
			//Creates the Configuration.yml File
			try {
				config.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			//Loads Configuration.yml
			try {
				cfg.load(config);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			//Sets the default values
			cfg.set("joinmessage", "TestPluging Working!\nWelcome to Xenation's Server!");
			cfg.set("weatherLock", weatherLock);
			cfg.set("blockResLock", blockResLock);
			cfg.set("blockResTime", blockResTime);
			cfg.set("allowSprintBreak", allowSprintBreak);
			//Saves Configuration.yml
			try {
				cfg.save(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			//Loads Configuration.yml
			try {
				cfg.load(config);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			//Gets the values
			joinMessage = cfg.getString("joinmessage");
			weatherLock = cfg.getBoolean("weatherLock");
			blockResLock = cfg.getBoolean("blockResLock");
			blockResTime = cfg.getLong("blockresTime");
			allowSprintBreak = cfg.getBoolean("allowSprintBreak");
			//Saves Configuration.yml
			try {
				cfg.save(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//checks if the Zones.yml file doesn't exists
		if (!(zonesFile).exists()) {
			try {
				zonesFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Load Zones.yml
		try {
			zonesCfg.load(zonesFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		//Loads zonesMap and zones
		Location locpos1 = Bukkit.getServer().getWorld("world").getSpawnLocation();
		Location locpos2 = Bukkit.getServer().getWorld("world").getSpawnLocation();
		for (String str: zonesCfg.getKeys(false)) {
			for (String str2: zonesCfg.getConfigurationSection(str + ".Positions").getKeys(false)) {
				if (str2.equalsIgnoreCase("pos1")) {
					locpos1.setX(zonesCfg.getInt(str + ".Positions.pos1.X"));
					locpos1.setZ(zonesCfg.getInt(str + ".Positions.pos1.Z"));
				} else if (str2.equalsIgnoreCase("pos2")) {
					locpos2.setX(zonesCfg.getInt(str + ".Positions.pos2.X"));
					locpos2.setZ(zonesCfg.getInt(str + ".Positions.pos2.Z"));
				}
			}
			zoneMap.put(str, new HashMap<String, HashMap<String, Param>>());
			zoneMap.get(str).put("Positions", new HashMap<String, Param>());
			zoneMap.get(str).get("Positions").put("pos1", new Param(locpos1.clone()));
			zoneMap.get(str).get("Positions").put("pos2", new Param(locpos2.clone()));
			if (zonesCfg.contains(str+".Params")) {
				zoneMap.get(str).put("Params", new HashMap<String, Param>());
				zoneMap.get(str).get("Params").put("TNT", new Param(zonesCfg.getBoolean(str + ".Params.TNT")));
				zoneMap.get(str).get("Params").put("Break", new Param(zonesCfg.getBoolean(str + ".Params.Break")));
				zoneMap.get(str).get("Params").put("Build", new Param(zonesCfg.getBoolean(str + ".Params.Build")));
				zoneMap.get(str).get("Params").put("Access", new Param(zonesCfg.getBoolean(str + ".Params.Acces")));
				zoneMap.get(str).get("Params").put("Use", new Param(zonesCfg.getBoolean(str + ".Params.Use")));
			}
		}
		//Saves Zones.yml
		try {
			zonesCfg.save(zonesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Gets the plugin manager
		PluginManager pm = this.getServer().getPluginManager();
		
		//Listeners
		pm.registerEvents(new JoinListener(this), this);
		pm.registerEvents(new ChatListener(this), this);
		pm.registerEvents(new KillListener(this), this);
		pm.registerEvents(new BlockListener(this), this);
		pm.registerEvents(new EntityListener(this), this);
		pm.registerEvents(new WeatherListener(this), this);
		
		//Commands
		getCommand("chat").setExecutor(new Commands(this));
		getCommand("countdown").setExecutor(new Commands(this));
		getCommand("team").setExecutor(new Commands(this));
		getCommand("heal").setExecutor(new Commands(this));
		getCommand("getpos").setExecutor(new Commands(this));
		getCommand("setb").setExecutor(new Commands(this));
		getCommand("zones").setExecutor(new Commands(this));
		getCommand("xspawn").setExecutor(new Commands(this));
		getCommand("weatherlock").setExecutor(new Commands(this));
		getCommand("blockres").setExecutor(new Commands(this));
		getCommand("warp").setExecutor(new Commands(this));
		
		//SCHEDULES
		//Sprint Glass Break
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				if (allowSprintBreak == true) {
					for (Player p: Bukkit.getServer().getOnlinePlayers()) {
						if (p.isSprinting() == true) {
							Location loc = p.getLocation();
							Location locf = loc;
							//Player Looking Direction Detection
							if (loc.getYaw() > 337.5d || loc.getYaw() >= 0 && loc.getYaw() < 22.5d) {
								locf.setX(loc.getX());
								locf.setY(loc.getY() + 1);
								locf.setZ(loc.getZ() + 1);
							} else if (loc.getYaw() > 22.5d && loc.getYaw() < 67.5d) {
								locf.setX(loc.getX() - 1);
								locf.setY(loc.getY() + 1);
								locf.setZ(loc.getZ() + 1);
							} else if (loc.getYaw() > 67.5d && loc.getYaw() < 112.5d) {
								locf.setX(loc.getX() - 1);
								locf.setY(loc.getY() + 1);
								locf.setZ(loc.getZ());
							} else if (loc.getYaw() > 112.5d && loc.getYaw() < 157.5d) {
								locf.setX(loc.getX() - 1);
								locf.setY(loc.getY() + 1);
								locf.setZ(loc.getZ() - 1);
							} else if (loc.getYaw() > 157.5d && loc.getYaw() < 202.5d) {
								locf.setX(loc.getX());
								locf.setY(loc.getY() + 1);
								locf.setZ(loc.getZ() - 1);
							} else if (loc.getYaw() > 202.5d && loc.getYaw() < 247.5d) {
								locf.setX(loc.getX() + 1);
								locf.setY(loc.getY() + 1);
								locf.setZ(loc.getZ() - 1);
							} else if (loc.getYaw() > 247.5d && loc.getYaw() < 292.5d) {
								locf.setX(loc.getX() + 1);
								locf.setY(loc.getY() + 1);
								locf.setZ(loc.getZ());
							} else if (loc.getYaw() > 292.5d && loc.getYaw() < 337.5d) {
								locf.setX(loc.getX() + 1);
								locf.setY(loc.getY() + 1);
								locf.setZ(loc.getZ() + 1);
							}
							
							//Glass Block Breaking
							if (Bukkit.getWorld("world").getBlockAt(locf).getType().equals(Material.GLASS)) {
								Bukkit.getWorld("world").getBlockAt(locf).breakNaturally();
							}
							if (Bukkit.getWorld("world").getBlockAt(locf.getBlockX(), locf.getBlockY()+1, locf.getBlockZ()).getType().equals(Material.GLASS)) {
								Bukkit.getWorld("world").getBlockAt(locf.getBlockX(), locf.getBlockY()+1, locf.getBlockZ()).breakNaturally();
							}
							if (Bukkit.getWorld("world").getBlockAt(locf.getBlockX(), locf.getBlockY()-1, locf.getBlockZ()).getType().equals(Material.GLASS)) {
								Bukkit.getWorld("world").getBlockAt(locf.getBlockX(), locf.getBlockY()-1, locf.getBlockZ()).breakNaturally();
							}
							if (Bukkit.getWorld("world").getBlockAt(locf.getBlockX(), locf.getBlockY()-2, locf.getBlockZ()).getType().equals(Material.GLASS)) {
								Bukkit.getWorld("world").getBlockAt(locf.getBlockX(), locf.getBlockY()-2, locf.getBlockZ()).breakNaturally();
							}
						}
					}
				}
			}
		}, 0, 1);
		//Block Re-spawn
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				if (blockResLock == true) {
					//HashMap Spawn
					ArrayList<Block> delBlocks = new ArrayList<Block>();
					for (Block b: blocksSet.keySet()) {
						if (System.currentTimeMillis() >= blocksTimesMap.get(blocksSet.get(b)) + blockResTime) {
							blocksStatesMap.get(blocksSet.get(b)).update(true);
							delBlocks.add(b);
						}
					}
					for (int d = 0; d != delBlocks.toArray().length; d++) {
						Block b = delBlocks.get(d);
						blocksTimesMap.remove(blocksSet.get(b));
						blocksStatesMap.remove(blocksSet.get(b));
						blocksSet.remove(b);
					}
				}
			}
		}, 0, 20);
	}
	
	//Called when plugin stops
	public void onDisable() {
		//Respawns the Blocks in blocksSet HashMap
		for (Block b: blocksSet.keySet()) {
			blocksStatesMap.get(blocksSet.get(b)).update(true);
		}
		
		//Plugin Folder
		File folder = this.getDataFolder();
		//Configuration.yml File
		File config = new File(folder, "Configuration.yml");
		YamlConfiguration cfg = new YamlConfiguration();
		try {
			cfg.load(config);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		//Sets the joinmessage value in the Configuration.yml file
		cfg.set("joinmessage", joinMessage);
		cfg.set("weatherLock", weatherLock);
		cfg.set("blockResLock", blockResLock);
		cfg.set("blockResTime", blockResTime);
		cfg.set("allowSprintBreak", allowSprintBreak);
		//tries to save the Configuration.yml File
		try {
			cfg.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
