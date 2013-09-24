package de.xcraft.INemesisI.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventListener implements Listener {

	private XcraftList plugin = null;

	public EventListener(XcraftList instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().setPlayerListName(plugin.getPlayerListName(event.getPlayer()));
	}

}
