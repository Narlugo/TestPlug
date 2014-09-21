package com.github.xenation.Listeners;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.xenation.testplug.TestPlug;

public class JoinListener implements Listener{
	
	public File folder;
	public File file;
	public YamlConfiguration config;
	
	public TestPlug plugin;
	
	public JoinListener(TestPlug plugin) {
		this.plugin = plugin;
		
		this.folder = plugin.getDataFolder();
		this.file = new File(folder, "Configuration.yml");
		this.config = new YamlConfiguration();
		
		if (!(folder).exists()) {
			folder.mkdir();
		}
		
		if (!(file).exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		String message = config.getString("joinmessage");
		player.sendMessage(ChatColor.RED + message);
		
		plugin.chat.put(player.getName(), "default");
	}
}
