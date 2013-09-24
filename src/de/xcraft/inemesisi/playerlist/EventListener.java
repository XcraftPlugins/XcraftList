package de.xcraft.inemesisi.playerlist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {

	private PlayerList plugin = null;

	public EventListener(PlayerList instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().setPlayerListName(plugin.getPlayerListName(event.getPlayer()));
	}

}
