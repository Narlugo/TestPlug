package com.github.xenation.testplug;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.xenation.Listeners.BlockListener;
import com.github.xenation.Listeners.ChatListener;
import com.github.xenation.Listeners.EntityListener;
import com.github.xenation.Listeners.JoinListener;
import com.github.xenation.Listeners.KillListener;
import com.github.xenation.Listeners.WeatherListener;

public class TestPlug extends JavaPlugin {
	
	public HashMap<String, String> chat = new HashMap<String, String>();
	public HashMap<String, String> countdown = new HashMap<String, String>();
	public HashMap<String, HashMap<String, HashMap<String, Param>>> zoneMap = new HashMap<String, HashMap<String, HashMap<String, Param>>>();
	
	public HashMap<Block, Double> blocksSet = new HashMap<Block, Double>();
	public HashMap<Double, BlockState> blocksStatesMap = new HashMap<Double, BlockState>();
	public HashMap<Double, Long> blocksTimesMap = new HashMap<Double, Long>();
	
	public boolean weatherLock = true;
	public boolean blockResLock = true;
	
	public Long blockResTime = 120000L;
	
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
		//checks if the Configuration.yml file exists
		if (!(config).exists()) {
			try {
				config.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//checks if the Zones.yml file exists
		if (!(zonesFile).exists()) {
			try {
				zonesFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//tries to load the Configuration.yml file
		try {
			cfg.load(config);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		//Sets the joinmessage value in the Configuration.yml file
		cfg.set("joinmessage", "TestPluging Working!\nWelcome to Xenation's Server!");
		cfg.set("weatherLock", weatherLock);
		//tries to save the Configuration.yml File
		try {
			cfg.save(config);
		} catch (IOException e) {
			e.printStackTrace();
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
		}
		//saves Zones.yml
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
		//pm.registerEvents(new SprintListener(this), this);    Disabled until further Development
		
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
	}
	
	//Called when plugin stops
	public void onDisable() {
		//Respawns the Blocks in blocksSet HashMap
		for (Block b: blocksSet.keySet()) {
			blocksStatesMap.get(blocksSet.get(b)).update(true);
		}
	}
}
