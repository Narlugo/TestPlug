package com.github.xenation.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import com.github.xenation.testplug.TestPlug;

public class KillListener implements Listener{
	
	public TestPlug plugin;
	
	public KillListener(TestPlug plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath (PlayerDeathEvent event) {
		Player player = event.getEntity();
		Player killer = player.getKiller();
		
		killer.sendMessage("Hello Killer!");
		player.sendMessage("Hello Killed!");
		
		Objective objective = killer.getScoreboard().getObjective("dummy");
		Score score = objective.getScore("money");
		killer.sendMessage(ChatColor.GOLD + "DEV -> "
				+ ChatColor.DARK_AQUA + "Money = " + score.getScore() + "(score) or " + score.getEntry() + "(entry)");
		int money = score.getScore();
		score.setScore(money + 100);
		
		for (Player p: Bukkit.getServer().getOnlinePlayers()) {
			p.sendMessage(ChatColor.GOLD + "DEV -> "
					+ ChatColor.DARK_AQUA + "A player has been killed");
			p.sendMessage(ChatColor.DARK_RED + player.getDisplayName() + ChatColor.RED + " was killed by " + ChatColor.DARK_RED + killer.getDisplayName());
		}
	}
	
}
