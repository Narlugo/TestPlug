package com.github.xenation.testplug;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	
	public File folder;
	public File zonesFile;
	public YamlConfiguration zonesCfg;
	
	//Constructor
	public TestPlug plugin;
	public Commands(TestPlug plugin) {
		this.plugin = plugin;
		
		this.folder = plugin.getDataFolder();
		this.zonesFile = new File(folder, "Zones.yml");
		this.zonesCfg = new YamlConfiguration();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		//Gets the player who executed the command
		Player player = (Player) sender;
		
		//Channel Change Command
		if (label.equalsIgnoreCase("chat")) {
			if (args.length == 1) {
				plugin.chat.put(player.getName(), args[0].toLowerCase());
			}
			else if (args.length == 0){
				plugin.chat.put(player.getName(), "default");
			} else if (args.length == 2){
				boolean found = false;
				for (Player p2: Bukkit.getOnlinePlayers()){
					if (p2.getName().equalsIgnoreCase(args[1])) {
						player = p2;
						found = true;
					}
				}
				if (found == true) {
					plugin.chat.put(player.getName(), args[0].toLowerCase());
				} else {
					player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Player not found");
				}
			}
		}
		
		//Countdown Command
		if (label.equalsIgnoreCase("countdown")) {
			if (args.length == 1) {
				for (Player p: Bukkit.getServer().getOnlinePlayers()) {
					p.sendMessage(ChatColor.GOLD + "DEV -> "
							+ ChatColor.DARK_AQUA + "countdown command called(reload)!");
					int time = Integer.parseInt(args[0]);
					for (int i = time; i!= -1; i--) {
						p.sendMessage("Reload in " + time + "!");
						try {
						    Thread.sleep(1000);//1000 milliseconds is one second.
						} catch(InterruptedException ex) {
						    Thread.currentThread().interrupt();
						}
						time--;
					}
				}
				Bukkit.getServer().reload();
			}
			else {
				player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Invalid Arguments");
			}
		}
		
		//Team Command
		if (label.equalsIgnoreCase("team")) {
			boolean found = false;
			Player player2 = player;
			if (args.length != 0) {
				for (Player p2: Bukkit.getOnlinePlayers()){
					if (p2.getName() == args[0]) {
						player2 = p2;
						found = true;
					}
				}
			}
			if (found == true || args.length == 0) {
				player.sendMessage(ChatColor.DARK_GREEN + "Team of " + player2.getName() +": " + ChatColor.GREEN + player2.getScoreboard().getPlayerTeam(player).getDisplayName());
			} else {
				player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Player not found");
			}
		}
		
		//heal Command
		if (label.equalsIgnoreCase("heal")) {
			player.setHealth(20);
			player.setFoodLevel(20);
			player.sendMessage(ChatColor.GREEN + "Healed!");

		}
		
		//Getpos Command
		if (label.equalsIgnoreCase("getpos")) {
			boolean found = false;
			if (args.length == 1) {
				for (Player p2: Bukkit.getOnlinePlayers()){
					if (p2.getName().equalsIgnoreCase(args[0])) {
						player = p2;
						found = true;
					}
				}
			}
			if (found == true || args.length == 0) {
				for (Player p: Bukkit.getServer().getOnlinePlayers()) {
					p.sendMessage(ChatColor.GOLD + "DEV -> "
							+ ChatColor.DARK_AQUA + "Player Pos Called\n"
									+ ChatColor.BLUE + "      " + player.getDisplayName() + ": "
									+ ChatColor.DARK_AQUA + "\n        X: " + player.getLocation().getBlockX() 
									+ "\n        Y: " + player.getLocation().getBlockY() 
									+ "\n        Z: " + player.getLocation().getBlockZ());
				}
			} else {
				player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Player not found");
			}
		}
		
		//Set Block Command (Bugged)
		if (label.equalsIgnoreCase("setb")) {
			
			if (args.length != 0) {
				for (Player p: Bukkit.getServer().getOnlinePlayers()) {
					p.sendMessage(ChatColor.GOLD + "DEV -> "
							+ ChatColor.DARK_AQUA + "Set Block Called\n"
									+ ChatColor.BLUE + "      " + player.getDisplayName() + ": "
									+ ChatColor.DARK_AQUA + "\n        X: " + player.getLocation().getBlockX() 
									+ "\n        Y: " + (player.getLocation().getBlockY() - 1) 
									+ "\n        Z: " + player.getLocation().getBlockZ() 
									+ "\n        Block: " + args[0]);
				}
				Location loc = player.getLocation();
				loc.setY(player.getLocation().getY() - 1);
				
				String type = args[0];
				Material mat = Material.valueOf(type);
				
				Block block;
				if (args.length == 2) {
					for (int i = 0; i < Integer.parseInt(args[1]); i++) {
						block = loc.getBlock();
						block.setType(mat);
						loc.setX(loc.getX() + 1);
					}
				} else if (args.length == 3) {
					loc.setY(player.getLocation().getY() - 1);
					loc.setZ(player.getLocation().getZ());
					for (int o = 0; o < Integer.parseInt(args[1]); o++) {
						loc.setX(player.getLocation().getX());
						for (int i = 0; i < Integer.parseInt(args[2]); i++) {
							block = loc.getBlock();
							block.setType(mat);
							loc.setX(loc.getX() + 1);
						}
						loc.setY(loc.getY() + 1);
					}
				} else if (args.length == 4) {
					loc.setZ(player.getLocation().getZ());
					for (int p = 0; p < Integer.parseInt(args[3]); p++) {
						loc.setY(player.getLocation().getY() - 1);
						for (int o = 0; o < Integer.parseInt(args[1]); o++) {
							loc.setX(player.getLocation().getX());
							for (int i = 0; i < Integer.parseInt(args[2]); i++) {
								block = loc.getBlock();
								block.setType(mat);
								loc.setX(loc.getX() + 1);
							}
							loc.setY(loc.getY() + 1);
						}
						loc.setZ(loc.getZ() + 1);
					}
				}
			}
			else {
				player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Invalid Arguments");
			}
		}
		
		//Zones Command
		if (label.equalsIgnoreCase("zones")) {
			if (player.isOp()) {
				if (args.length != 0) {
					//Add Command
					if (args[0].equalsIgnoreCase("add")) {
						//Adds the new Zone
						String zoneName = args[1];																	//Gets the zoneName from the Second Argument
						plugin.zoneMap.put(zoneName, new HashMap<String, HashMap<String, Param>>());				//Creates main Zone HashMap
						plugin.zoneMap.get(zoneName).put("Positions", new HashMap<String, Param>());				//Creates Positions HashMap in main Zone HashMap
						plugin.zoneMap.get(zoneName).put("Params", new HashMap<String, Param>());
						plugin.zoneMap.get(zoneName).get("Params").put("TNT", new Param(true));
						plugin.zoneMap.get(zoneName).get("Params").put("Break", new Param(true));
						plugin.zoneMap.get(zoneName).get("Params").put("Build", new Param(true));
						plugin.zoneMap.get(zoneName).get("Params").put("Access", new Param(true));
						plugin.zoneMap.get(zoneName).get("Params").put("Use", new Param(true));
						for (Player p: Bukkit.getServer().getOnlinePlayers()) {//DEV message
							if (p.isOp() == true) {
								p.sendMessage(ChatColor.GOLD + "DEV -> "
										+ ChatColor.DARK_PURPLE + "Added Zone: " + args[1]);
							}
						}
					}
					//Remove command
					else if (args[0].equalsIgnoreCase("remove")) {
						//Removes the zone in the zones HashMap
						if (plugin.zoneMap.containsKey(args[1]) == false) {
							player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Invalid zoneName");
						} else {
							plugin.zoneMap.remove(args[1]);	//Removes the zone in HashMap
							try {							//Loads Zones Config
								zonesCfg.load(zonesFile);
							} catch (IOException | InvalidConfigurationException e) {
								e.printStackTrace();
							}
							zonesCfg.set(args[1], null);	//Removes the zone Section
							try {							//Save Zones Config
								zonesCfg.save(zonesFile);
							} catch (IOException e) {
								e.printStackTrace();
							}
							for (Player p: Bukkit.getServer().getOnlinePlayers()) {//DEV message
								if (p.isOp() == true) {
									p.sendMessage(ChatColor.GOLD + "DEV -> "
											+ ChatColor.DARK_PURPLE + "Removed Zone: " + args[1]);
								}
							}
						}
					}
					//Set Command
					else if (args[0].equalsIgnoreCase("set")) {
						String zoneName = args[1];		//Gets the zoneName from the Second Argument
						String pos = args[2];			//Gets the pos to set ("pos1" or "pos2")
						if (plugin.zoneMap.containsKey(args[1]) == false) {
							player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Invalid zoneName");
						} else {
							for (Player p: Bukkit.getServer().getOnlinePlayers()) {//DEV message
								p.sendMessage(ChatColor.GOLD + "DEV -> "
										+ ChatColor.DARK_AQUA + "Set Safe Zone Called");
							}
							if (pos.equalsIgnoreCase("pos1")) {//Pos1
								//Sets the Pos1 Param in Positions HashMap in main zone HashMap to the Command Sender Position
								if (plugin.zoneMap.get(zoneName).get("Positions").containsKey("pos1") == false) {
									plugin.zoneMap.get(zoneName).get("Positions").put("pos1", new Param(player.getLocation()));
								}
								plugin.zoneMap.get(zoneName).get("Positions").get("pos1").setLoc(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getPitch(), player.getLocation().getYaw());
								
								for (Player p: Bukkit.getServer().getOnlinePlayers()) {//DEV message
									p.sendMessage(ChatColor.GOLD + "DEV -> "
											+ ChatColor.DARK_AQUA + "Set Safe Zone Pos1 Called\n"
													+ ChatColor.BLUE + "      " + zoneName + "Pos1: "
													+ ChatColor.DARK_AQUA + "\n        X: " + player.getLocation().getBlockX()
													+ "\n        Y: " + player.getLocation().getBlockY()
													+ "\n        Z: " + player.getLocation().getBlockZ());
								}
							}
							if (pos.equalsIgnoreCase("pos2")) {//Pos2
								//Sets the Pos2 Param in Positions HashMap in main zone HashMap to the Command Sender Position
								if (plugin.zoneMap.get(zoneName).get("Positions").containsKey("pos2") == false) {
									plugin.zoneMap.get(zoneName).get("Positions").put("pos2", new Param(player.getLocation()));
								}
								plugin.zoneMap.get(zoneName).get("Positions").get("pos2").setLoc(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getPitch(), player.getLocation().getYaw());
								
								for (Player p: Bukkit.getServer().getOnlinePlayers()) {//DEV message
									p.sendMessage(ChatColor.GOLD + "DEV -> "
											+ ChatColor.DARK_AQUA + "Set Safe Zone Pos2 Called\n"
													+ ChatColor.BLUE + "      " + zoneName + "Pos2: "
													+ ChatColor.DARK_AQUA + "\n        X: " + player.getLocation().getBlockX() 
													+ "\n        Y: " + player.getLocation().getBlockY()
													+ "\n        Z: " + player.getLocation().getBlockZ());
								}
							}
							//Changes the pos1 and pos2 Locations to fit the Detecting scripts
							if (plugin.zoneMap.get(zoneName).get("Positions").containsKey("pos1") && plugin.zoneMap.get(zoneName).get("Positions").containsKey("pos2")) {
								if (plugin.zoneMap.get(zoneName).get("Positions").get("pos1").getLoc().getBlockX() > plugin.zoneMap.get(zoneName).get("Positions").get("pos2").getLoc().getBlockX()) {
									int tmp = plugin.zoneMap.get(zoneName).get("Positions").get("pos1").getLoc().getBlockX();
									plugin.zoneMap.get(zoneName).get("Positions").get("pos1").getLoc().setX(plugin.zoneMap.get(zoneName).get("Positions").get("pos2").getLoc().getBlockX());
									plugin.zoneMap.get(zoneName).get("Positions").get("pos2").getLoc().setX(tmp);
								}
								if (plugin.zoneMap.get(zoneName).get("Positions").get("pos1").getLoc().getBlockZ() > plugin.zoneMap.get(zoneName).get("Positions").get("pos2").getLoc().getBlockZ()) {
									int tmp = plugin.zoneMap.get(zoneName).get("Positions").get("pos1").getLoc().getBlockZ();
									plugin.zoneMap.get(zoneName).get("Positions").get("pos1").getLoc().setZ(plugin.zoneMap.get(zoneName).get("Positions").get("pos2").getLoc().getBlockZ());
									plugin.zoneMap.get(zoneName).get("Positions").get("pos2").getLoc().setZ(tmp);
								}
							}
							
							try {//Loads Zones Config
								zonesCfg.load(zonesFile);
							} catch (IOException | InvalidConfigurationException e) {
								e.printStackTrace();
							}
							//Builds the Config Sections if they don't exist
							ConfigurationSection zoneSec;
							if (zonesCfg.contains(zoneName) == false) {
								zoneSec = zonesCfg.createSection(zoneName);					//Creates Zone Section
							} else {
								zoneSec = zonesCfg.getConfigurationSection(zoneName);		//Gets Zone Section
							}
							if (zonesCfg.contains(zoneName+".Positions") == false) {
								ConfigurationSection zoneSecPos = zoneSec.createSection("Positions");	//Creates Positions Section in Zone Section
								if (zonesCfg.contains(zoneName+".Positions."+pos) == false) {
									@SuppressWarnings("unused")
									ConfigurationSection zoneSecPos1 = zoneSecPos.createSection(pos);//Creates pos1 Section in Positions Section in Zone Section
								}
							}
							zonesCfg.set(zoneName+".Positions."+pos+".X", plugin.zoneMap.get(zoneName).get("Positions").get(pos).getLoc().getBlockX());//Sets the pos X value in Config
							zonesCfg.set(zoneName+".Positions."+pos+".Z", plugin.zoneMap.get(zoneName).get("Positions").get(pos).getLoc().getBlockZ());//Sets the pos Z value in Config
							try {//Saves Zones Config
								zonesCfg.save(zonesFile);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					//List Command
					else if (args[0].equalsIgnoreCase("list")) {
						String str = "";
						for (String zone: plugin.zoneMap.keySet()) {//Builds the String to display with all the Keys in zoneMap
							str = str + zone + ", ";
						}
						player.sendMessage(ChatColor.GOLD + "DEV -> "
								+ ChatColor.DARK_PURPLE + "Zones: " + str);
					}
					//Param Command
					else if (args[0].equalsIgnoreCase("param")) {
						boolean error = false;
						String zoneName = args[2];
						String param = "";
						if (plugin.zoneMap.containsKey(args[2]) == false) {
							player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Invalid zoneName");
							error = true;
						} else {
							//TNT Param
							if (args[1].equalsIgnoreCase("TNT")) {
								if (plugin.zoneMap.get(zoneName).get("Params").get("TNT").getBool() == true) {
									plugin.zoneMap.get(zoneName).get("Params").get("TNT").setBool(false);
								} else {
									plugin.zoneMap.get(zoneName).get("Params").get("TNT").setBool(true);
								}
								player.sendMessage(ChatColor.GREEN + "Zone Param: TNT = " + plugin.zoneMap.get(zoneName).get("Params").get("TNT").getBool());
								param = "TNT";
							}
							//Break Param
							else if (args[1].equalsIgnoreCase("Break")) {
								if (plugin.zoneMap.get(zoneName).get("Params").get("Break").getBool() == true) {
									plugin.zoneMap.get(zoneName).get("Params").get("Break").setBool(false);
								} else {
									plugin.zoneMap.get(zoneName).get("Params").get("Break").setBool(true);
								}
								player.sendMessage(ChatColor.GREEN + "Zone Param: Break = " + plugin.zoneMap.get(zoneName).get("Params").get("Break").getBool());
								param = "Break";
							}
							//Build Param
							else if (args[1].equalsIgnoreCase("Build")) {
								if (plugin.zoneMap.get(zoneName).get("Params").get("Build").getBool() == true) {
									plugin.zoneMap.get(zoneName).get("Params").get("Build").setBool(false);
								} else {
									plugin.zoneMap.get(zoneName).get("Params").get("Build").setBool(true);
								}
								player.sendMessage(ChatColor.GREEN + "Zone Param: Build = " + plugin.zoneMap.get(zoneName).get("Params").get("Build").getBool());
								param = "Build";
							}
							//Access Param
							else if (args[1].equalsIgnoreCase("Access")) {
								if (plugin.zoneMap.get(zoneName).get("Params").get("Access").getBool() == true) {
									plugin.zoneMap.get(zoneName).get("Params").get("Access").setBool(false);
								} else {
									plugin.zoneMap.get(zoneName).get("Params").get("Access").setBool(true);
								}
								player.sendMessage(ChatColor.GREEN + "Zone Param: Access = " + plugin.zoneMap.get(zoneName).get("Params").get("Access").getBool());
								param = "Acces";
							}
							//Use Param
							else if (args[1].equalsIgnoreCase("Use")) {
								if (plugin.zoneMap.get(zoneName).get("Params").get("Use").getBool() == true) {
									plugin.zoneMap.get(zoneName).get("Params").get("Use").setBool(false);
								} else {
									plugin.zoneMap.get(zoneName).get("Params").get("Use").setBool(true);
								}
								player.sendMessage(ChatColor.GREEN + "Zone Param: Use = " + plugin.zoneMap.get(zoneName).get("Params").get("Use").getBool());
								param = "Use";
							}
							//ERROR if wrong Param name
							else {
								player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Invalid arguments");
								error = true;
							}
						}
						if (error != true) {
							try {//Loads Zones Config
								zonesCfg.load(zonesFile);
							} catch (IOException | InvalidConfigurationException e) {
								e.printStackTrace();
							}
							//Builds the Config Sections if they don't exist
							if (zonesCfg.contains(zoneName+".Params") == false) {
								ConfigurationSection zoneSec = zonesCfg.getConfigurationSection(zoneName);
								@SuppressWarnings("unused")
								ConfigurationSection zoneSecParam = zoneSec.createSection("Params");
								zonesCfg.set(zoneName+".Params.TNT", true);
								zonesCfg.set(zoneName+".Params.Break", true);
								zonesCfg.set(zoneName+".Params.Build", true);
								zonesCfg.set(zoneName+".Params.Access", true);
								zonesCfg.set(zoneName+".Params.Use", true);
							}
							zonesCfg.set(zoneName+".Params."+param, plugin.zoneMap.get(zoneName).get("Params").get(param).getBool());
							try {//Saves Zones Config
								zonesCfg.save(zonesFile);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					//Get params Command
					else if (args[0].equalsIgnoreCase("getparams")) {
						String zoneName = args[1];
						if (plugin.zoneMap.containsKey(args[1]) == false) {
							player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Invalid zoneName");
						} else {
							player.sendMessage(ChatColor.GREEN + "Params:"
									+ "\n  Authorized:"
									+ "\n    TNT = " + plugin.zoneMap.get(zoneName).get("Params").get("TNT").getBool()
									+ "\n    Break = " + plugin.zoneMap.get(zoneName).get("Params").get("Break").getBool()
									+ "\n    Build = " + plugin.zoneMap.get(zoneName).get("Params").get("Build").getBool()
									+ "\n    Access = " + plugin.zoneMap.get(zoneName).get("Params").get("Access").getBool()
									+ "\n    Use = " + plugin.zoneMap.get(zoneName).get("Params").get("Use").getBool());
						}
					} else {
						player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Invalid arguments");
					}
				}
			}
			//ERROR if player isn't Op
			else {
				player.sendMessage(ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + "Access Denied");
			}
		}
		
		//XSpawn Command
		if (label.equalsIgnoreCase("xspawn")) {
			if (args.length != 0) {
				//Set Command
				if (args[0].equalsIgnoreCase("set")) {
					World world = Bukkit.getServer().getWorld("world");
					world.setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
					for (Player p: Bukkit.getServer().getOnlinePlayers()) {
						p.sendMessage(ChatColor.GOLD + "DEV -> "
								+ ChatColor.DARK_AQUA + "Spawn Set Called!\n"
										+ ChatColor.BLUE + "       New Spawn: "
										+ ChatColor.DARK_AQUA + "\nX:" + player.getLocation().getBlockX()
										+ "\nY:" + player.getLocation().getBlockY()
										+ "\nZ:" + player.getLocation().getBlockZ());
					}
				}
				//Get Command
				else if (args[0].equalsIgnoreCase("get")) {
					World world = Bukkit.getServer().getWorld("world");
					Location spawn = world.getSpawnLocation();
					for (Player p: Bukkit.getServer().getOnlinePlayers()) {
						p.sendMessage(ChatColor.GOLD + "DEV -> "
								+ ChatColor.DARK_AQUA + "Spawn Get Called!\n"
										+ ChatColor.BLUE + "       Spawn At: "
										+ ChatColor.DARK_AQUA + "\nX:" + spawn.getBlockX()
										+ "\nY:" + spawn.getBlockY()
										+ "\nZ:" + spawn.getBlockZ());
					}
				}
				//TP Command
				else if (args[0].equalsIgnoreCase("tp")) {
					World world = Bukkit.getServer().getWorld("world");
					Location spawn = world.getSpawnLocation();
					Location spawnDouble = spawn;
					spawnDouble.setX(spawnDouble.getX()+0.5);
					spawnDouble.setZ(spawnDouble.getZ()+0.5);
					player.teleport(spawnDouble);
					for (Player p: Bukkit.getServer().getOnlinePlayers()) {
						p.sendMessage(ChatColor.GOLD + "DEV -> "
								+ ChatColor.DARK_AQUA + "Spawn TP Called!\n"
										+ ChatColor.BLUE + "       Teleported " + player.getName() + " At: "
										+ ChatColor.DARK_AQUA + "\nX:" + spawn.getBlockX()
										+ "\nY:" + spawn.getBlockY()
										+ "\nZ:" + spawn.getBlockZ());
					}
				}
			} else {
				player.sendMessage(ChatColor.GOLD + "DEV -> "
						+ ChatColor.DARK_AQUA + "Spawn TP Called!\n"
								+ ChatColor.DARK_RED + "       ERROR: "
								+ ChatColor.RED + "invalid number of arguments");
			}
		}
		
		//Weather Lock Command
		if (label.equalsIgnoreCase("weatherlock")) {
			if (plugin.weatherLock == true) {
				plugin.weatherLock = false;
			} else {
				plugin.weatherLock = true;
			}
			for (Player p: Bukkit.getServer().getOnlinePlayers()) {
				if (p.isOp() == true) {
					p.sendMessage(ChatColor.GOLD + "DEV -> "
							+ ChatColor.DARK_PURPLE + "WeatherLock = " + plugin.weatherLock);
				}
			}
		}
		
		//BlockRes Command
		if (label.equalsIgnoreCase("blockres")) {
			//Lock Command
			if (args[0].equalsIgnoreCase("lock")) {
				if (plugin.blockResLock == true) {
					plugin.blockResLock = false;
				} else {
					plugin.blockResLock = true;
				}
				for (Player p: Bukkit.getServer().getOnlinePlayers()) {
					if (p.isOp() == true) {
						p.sendMessage(ChatColor.GOLD + "DEV -> "
								+ ChatColor.DARK_PURPLE + "BlockResLock = " + plugin.blockResLock);
					}
				}
			}
			//Time Command
			else if (args[0].equalsIgnoreCase("time")) {
				plugin.blockResTime = Long.valueOf(args[1]);
				for (Player p: Bukkit.getServer().getOnlinePlayers()) {
					if (p.isOp() == true) {
						p.sendMessage(ChatColor.GOLD + "DEV -> "
								+ ChatColor.DARK_PURPLE + "BlockResTime = " + plugin.blockResTime);
					}
				}
			}
		}
		
		//World Change Command
		if (label.equalsIgnoreCase("warp")) {
			boolean create = true;
			Server server = Bukkit.getServer();
			World world;
			
			for (World w: server.getWorlds()) {
				if (w.getName().equalsIgnoreCase(args[0])) {
					create = false;
				}
			}
			
			if (create == true) {
				for (Player p: Bukkit.getServer().getOnlinePlayers()) {
					if (p.isOp() == true) {
						p.sendMessage(ChatColor.GOLD + "DEV -> "
								+ ChatColor.DARK_PURPLE + "Creating World ...");
					}
				}
				WorldCreator worldCreator = WorldCreator.name(args[0]);
				world = worldCreator.createWorld();
				for (Player p: Bukkit.getServer().getOnlinePlayers()) {
					if (p.isOp() == true) {
						p.sendMessage(ChatColor.GOLD + "DEV -> "
								+ ChatColor.DARK_PURPLE + "Created World: " + world.getName());
					}
				}
			} else {
				world = server.getWorld(args[0]);
				for (Player p: Bukkit.getServer().getOnlinePlayers()) {
					if (p.isOp() == true) {
						p.sendMessage(ChatColor.GOLD + "DEV -> "
								+ ChatColor.DARK_PURPLE + "Got World: " + world.getName());
					}
				}
			}
			
			Location loc = player.getLocation();
			Location wSpawn = world.getSpawnLocation();
			loc.setWorld(world);
			loc.setX(wSpawn.getX());
			loc.setY(wSpawn.getY());
			loc.setZ(wSpawn.getZ());
			player.teleport(loc);
			
			for (Player p: Bukkit.getServer().getOnlinePlayers()) {
				if (p.isOp() == true) {
					p.sendMessage(ChatColor.GOLD + "DEV -> "
							+ ChatColor.DARK_PURPLE + "Teleported " + player.getName() + " to world: " + world.getName());
				}
			}
		}
		return true;
	}
}
