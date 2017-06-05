package me.vertises.scoreboard;

import java.util.List;

import org.bukkit.entity.Player;

public interface SidebarProvider {

	String getTitle(Player player);
	
	List<SidebarEntry> getLines(Player player);
	
}
