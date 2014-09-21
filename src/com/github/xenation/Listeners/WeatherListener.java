package com.github.xenation.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.github.xenation.testplug.TestPlug;

public class WeatherListener implements Listener {
	
	public TestPlug plugin;
	
	public WeatherListener (TestPlug plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onWeatherChange (WeatherChangeEvent event) {
		if (plugin.weatherLock == true) {
			event.setCancelled(true);
		}
	}
}
