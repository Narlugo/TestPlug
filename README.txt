
_____      ____  ________________ _____            __ ______________ ____       ______                        ____
\    \    /   / /               //     \      ____/  \\             \\   \     /      \                      /   /
 \    \  /   / /   ____________//   /\  \    /   /\   \\____    _____\\___\   /        \       ___          /   /
  \    \/   / /   /__________  /   /  \  \  /   /  \   \    \   \      ____  /    \     \     /   \        /   /
   \       / /              / /   /    \  \/   /____\   \    \   \     \   \ \     \     \   /     \      /   /
    \     / /   ___________/ /   /      \     /_______   \    \   \     \   \ \     \     \ /   /\  \    /   /
    /	  \ \   \_________  /   /        \   /        \   \    \   \     \   \ \     \    //   /  \  \  /   /
   /       \ \            \ \  /          \_/          \   \    \___\     \   \ \        //   /    \  \/   /
  /   /\    \ \____________\ \/                         \___\              \___\ \______//___/      \     /
 /   /  \____\  ____________________________________________________________________________________ \___/
/___/          /____________________________________________________________________________________\
                                                 \ XEN-PLUG /
                                                  \________/

Version: 0.14 (Alpha)

----====|INFORMATIONS|====----
This Plugin is meant to be used in a server with the Flan's Mod Installed (Currently works fine without)
FEATURES:
 - Zones System :
 	"/zones" Command, you can Define zones :
 		- "add" adds a zones (limits undefined) (ex: /zones add Spawn)
 		- "remove" removes the zone (ex: /zones remove Spawn)
 		- "set" set the limits of the zone:
 			- "pos1" sets the first position of the limits
 			- "pos2" sets the second position of the limits
 			(ex: /zones set Spawn pos1)
 		- "list" displays in the chat all the current zones (ex: /zones list)
 		- "param" changes the zone parameters(flags)
 			- "TNT" sets the TNT parameter of the zone (ex: /zones param TNT Spawn)
 			- "Break" sets the Break parameter of the zone (ex: /zones param break Spawn)
 			- "Build" sets the Build parameter of the zone (ex: /zones param build Spawn)
 			- "Acces" sets the Acces parameter of the zone (ex: /zones param acces Spawn) (Currently Useless)
 			- "Use" sets the TNT parameter of the zone (ex: /zones param use Spawn) (Currently Useless)
 		- "getparams" lists all the params of the zone (ex: /zones getparams Spawn)
 - Block Respawn :
 	"/blockres" Command, you can make the broken block respawn after some time
 		- "lock" activates/deactivates the block respawn (ex: /blockres lock)
 		- "time" Sets the time before the block respawns in milliseconds (ex: /blockres time 20000)
 	All the blocks will respawn before the server closes/restarts/reloads
 - Spawn Point Managing :
 	"/Xspawn" Command, you can change the spawn or find it
 		- "set" changes the world spawn to the position of the player (ex: /Xspawn set)
 		- "get" displays in chat where the spawn is located (ex: /Xspawn get)
 		- "tp" teleports the player where the spawn is located (ex: /Xspawn tp)
 - Wheather Locking :
	"/weatherlock" Command, Locks/Unlocks the weather to its current state
 - Heal :
 	"/heal" Command, refills the player's health and food bar
 - Team Display :
 	"/team" Command, Displays the player's team (ex: /team; or /team [player])
 - Chat Channels :
 	"/chat" Command, changes the player's chat channel (/chat for default channel, ex: /chat test)
 - Multi-Worlds :
 	"/warp" Command, you can create a new world or tp to an existing one (ex: /warp testworld)
 	If the entered world name doesn't corresponds to an existing one it will create it
 - Sprint Glass Breaking :
 	"/Xsprint" Command, you can switch on/off the glass breaking in front of you when sprinting


----=======|TO DO|========----
 - Glass Broken by gun bullets bug (Flan's Mod) :
 	Save the glass blocks broken by bullets from the Flan's Mod guns, Currently not implemented because bullets that break glass doesn't call any event
 - Block Set Command Fix ("/setb") :
 	Currently too messy and hard to use
