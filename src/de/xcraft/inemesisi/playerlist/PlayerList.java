package de.xcraft.inemesisi.playerlist;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerList extends JavaPlugin {

	private EventListener eventlistener = new EventListener(this);

	private Permission permission = null;

	private ConfigurationSection permsection;
	private Set<String> toReplace = new HashSet<String>();

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(eventlistener, this);

		setupPermission();
		load();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		reloadConfig();
		load();
		update();
		sendMsg((Player) sender, ChatColor.DARK_AQUA + this.getDescription().getFullName() + " config reloaded!");
		return true;
	}

	public void load() {
		final File check = new File(getDataFolder(), "config.yml");
		if (!check.exists()) {
			saveDefaultConfig();
		}
		FileConfiguration config = getConfig();
		permsection = config.getConfigurationSection("permissions");
		parseListFormat(config.getString("config.listformat"));
	}

	public void update() {
		for (Player player : getServer().getOnlinePlayers())
			player.setPlayerListName(getPlayerListName(player));
	}

	private boolean setupPermission() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(
				net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) permission = permissionProvider.getProvider();

		return permission != null;
	}

	public Permission getPermission() {
		return permission;
	}

	private void parseListFormat(String listFormat) {
		int i = 0;
		int d1 = 0;
		int d2 = 0;

		while (i < listFormat.length()) {
			if ((d1 = listFormat.indexOf("$", i)) == -1) {
				break;
			}
			d1++;

			if ((d2 = listFormat.indexOf("$", d1)) == -1) {
				break;
			}

			String tag = listFormat.substring(d1, d2);
			toReplace.add(tag);
			i = ++d2;
		}
	}

	public String getPlayerListName(Player player) {
		Map<String, String> values = new HashMap<String, String>();
		values.put("name", player.getName());

		for (String permnode : permsection.getKeys(false)) {
			if (player.hasPermission(getName() + "." + permnode)) {
				for (String val : permsection.getConfigurationSection(permnode).getKeys(false)) {
					values.put(val, permsection.getString(permnode + "." + val));
				}
				String listformat = getConfig().getString("config.listformat");
				for (String replaceMe : toReplace) {
					if (values.containsKey(replaceMe)) {
						listformat = listformat.replace("$" + replaceMe + "$", values.get(replaceMe));
					} else listformat = listformat.replace("$" + replaceMe + "$", "");
				}
				if (listformat.length() > 16) listformat = listformat.substring(0, 13) + "...";
				return listformat.replaceAll("&([a-f0-9])", "\u00A7$1");
			}
		}
		return player.getName();
	}

	public void sendMsg(Player player, String string) {
		player.sendMessage("[" + ChatColor.DARK_AQUA + getDescription().getName() + ChatColor.WHITE + "] " + ChatColor.GRAY + string);
	}
}
