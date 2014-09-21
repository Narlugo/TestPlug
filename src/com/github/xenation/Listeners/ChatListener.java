package com.github.xenation.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.xenation.testplug.TestPlug;

public class ChatListener implements Listener {
	
	//Constructor
	public TestPlug plugin;
	public ChatListener(TestPlug plugin) {
		this.plugin = plugin;
	}
	
	//Sets actions to do when a player(any) speaks in chat
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerChat (AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		
		//Gets the player who spoke
		Player player = event.getPlayer();
		
		//Sets the Chat indications for the Developer
		if (player.getName().equalsIgnoreCase("Xenation")) {
			event.setFormat(ChatColor.GOLD + "[" + ChatColor.RED + "Developer" + ChatColor.GOLD + "] "
					+ ChatColor.RED + player.getName() + ": "
					+ ChatColor.AQUA + event.getMessage());
		}
		//Sets the Chat indications for operators(Ops)
		else if (player.isOp()) {
			event.setFormat(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Moderator" + ChatColor.GRAY + "] "
					+ ChatColor.RED + player.getName() + ": "
					+ ChatColor.RESET + event.getMessage());
		}
		//Sets the Chat indications for a normal player
		else {
			event.setFormat(ChatColor.GRAY + "[Player] "
					+ ChatColor.GREEN + player.getName() + ": "
					+ ChatColor.WHITE + event.getMessage());
		}
		
		//Sends the message the player entered to everyone
		String chat = plugin.chat.get(player.getName());
		for (Player p: Bukkit.getServer().getOnlinePlayers()) {
			if (plugin.chat.get(p.getName()).equals(chat)) {
				p.sendMessage(ChatColor.GREEN + "[" + chat + "] " + event.getFormat());
			}
		}
	}
}
