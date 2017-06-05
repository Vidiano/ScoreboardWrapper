package me.vertises.scoreboard;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Sidebar {

	public static final ChatColor[] COLORS;
	
	private final Player player;
	private final Scoreboard scoreboard;
	private final Objective objective;
	private final ScoreboardManager manager;
	
	public Sidebar(Player player, ScoreboardManager manager) {
		this.player = player;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = scoreboard.registerNewObjective("default", "dummy");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.objective.setDisplayName("");
		this.manager = manager;
		player.setScoreboard(scoreboard);
		update();
	}
	
	public void update() {
		if (player == null || scoreboard == null || objective == null || manager == null) {
			return;
		}
		List<SidebarEntry> entries = manager.getProvider().getLines(player);
		String label = manager.getProvider().getTitle(player);
		if (!objective.getDisplayName().equals(label)) {
			objective.setDisplayName(label);
		}
		if (entries.size() != scoreboard.getEntries().size()) {
			for (String entry : scoreboard.getEntries()) {
				scoreboard.resetScores(entry);
			}
		}
		for (int i = 0; i < 15; i ++) {
			while(entries.get(i) != null) {
				processEntry(entries.get(i), 15 - i);
			}
		}
	}
	
	private void processEntry(SidebarEntry entry, int index) {
		ChatColor base = COLORS[index];
		String code = base.toString() + base.toString() + base.toString() + base.toString();
		Team team = scoreboard.getTeam(code);
		if (team == null) {
			team = scoreboard.registerNewTeam(code);
		}
		if (!team.hasEntry(code)) {
			team.addEntry(code);
		}
		team.setPrefix(entry.left);
		team.setSuffix(entry.right);
		objective.getScore(code).setScore(index);
	}
	
	public void reset() {
		if (player != null && player.isOnline()) {
			player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		}
		scoreboard.getEntries().forEach(entry -> scoreboard.resetScores(entry));
		scoreboard.getTeams().forEach(team -> team.unregister());
		scoreboard.clearSlot(DisplaySlot.SIDEBAR);
		objective.unregister();
	}
	
	static {
		COLORS = ChatColor.values();
	}
	
}
