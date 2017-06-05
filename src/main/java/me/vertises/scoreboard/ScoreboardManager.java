package me.vertises.scoreboard;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.base.Preconditions;

import lombok.Getter;
import lombok.Setter;

public class ScoreboardManager {

	private final Map<UUID, Sidebar> sidebars;
	
	@Getter
	private final JavaPlugin plugin;
	
	@Getter
	private static ScoreboardManager instance;
	
	@Setter
	private SidebarProvider provider; 
	
	public ScoreboardManager(JavaPlugin plugin) {
		Preconditions.checkNotNull(plugin);
		ScoreboardManager.instance = this;
		this.plugin = plugin;
		this.sidebars = new ConcurrentHashMap<>();
		this.provider = new DefaultProvider();
		BukkitTask task = new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Sidebar sidebar : getSidebars()) {
					sidebar.update();
				}
			}
			
		}.runTaskTimerAsynchronously(plugin, 2l, 2l);
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@EventHandler
			public void onJoin(PlayerJoinEvent event) {
				Player player = event.getPlayer();
				UUID uniqueId = player.getUniqueId();
				Sidebar sidebar = null;
				if ((sidebar = sidebars.remove(uniqueId)) != null) {
					sidebar.reset();
				}
				sidebars.put(uniqueId, new Sidebar(player, instance));
			}
			
			@EventHandler
			public void onLeave(PlayerQuitEvent event) {
				Player player = event.getPlayer();
				UUID uniqueId = player.getUniqueId();
				Sidebar sidebar = null;
				if ((sidebar = sidebars.remove(uniqueId)) != null) {
					sidebar.reset();
				}
			}
			
			@EventHandler
			public void onDisable(PluginDisableEvent event) {
				if (event.getPlugin() == plugin) {
					HandlerList.unregisterAll(this);
					
				}
			}
			
		}, plugin);
	}
	
	public Collection<? extends Sidebar> getSidebars() {
		return Collections.unmodifiableCollection(sidebars.values());
	}
	
	public SidebarProvider getProvider() {
		return provider == null ? (provider = new DefaultProvider()) : provider;
	}
	
	public static class DefaultProvider implements SidebarProvider {

		@Override
		public String getTitle(Player player) {
			return ChatColor.WHITE + "Default Provider";
		}

		@Override
		public List<SidebarEntry> getLines(Player player) {
			return Arrays.asList(new SidebarEntry("", ""), new SidebarEntry(ChatColor.WHITE + "Default", ChatColor.WHITE + " Provider!"));
		}
		
	}
	
}
