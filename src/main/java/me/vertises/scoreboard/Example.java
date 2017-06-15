package me.vertises.scoreboard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Example extends JavaPlugin {
	
	private ScoreboardWrapper wrapper;
	
	@Override
	public void onEnable() {
		//Note:
		// * If you load the library in onLoad(), it might not work properly due to earliest time
		// * listeners can be registered is AFTER the plugin has been properly enabled.
		this.wrapper = new ScoreboardWrapper(this, new ExampleProvider());
	}
	
	@Override
	public void onDisable() {
		//Note:
		// * This is totally unnecessary, especially if you have Incremental Garbage Collection
		// * enabled, but I put it there anyways.
		this.wrapper = null;
	}

	public static class ExampleProvider implements ScoreboardProvider {
		
		public static DecimalFormat TPS_FORMAT = new DecimalFormat("0.0");

		@Override
		public String getTitle(Player player) {
			return ChatColor.GOLD + "Example";
		}

		@Override
		public List<String> getLines(Player player) {
			List<String> lines = new ArrayList<>();
			lines.add(ChatColor.YELLOW + "Online: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().length);
			lines.add(ChatColor.YELLOW + "TPS: " + ChatColor.WHITE + TPS_FORMAT.format(Bukkit.spigot().getTPS()[0]));
			lines.add(ChatColor.YELLOW + "Lag: " + ChatColor.WHITE + TPS_FORMAT.format(100d - Bukkit.spigot().getTPS()[0] * 5d) + "%");
			//Note: -->
			// * If the line is longer than 32 chars,
			// * it will just clip off, instead of crashing the player!
			lines.add(ChatColor.GREEN + "Lines can be long as 32 chars!"); 
			if (!lines.isEmpty()) {
				lines.add("");
			}
			return lines;
		}

	}

}
