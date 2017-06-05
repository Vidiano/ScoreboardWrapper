package me.vertises.scoreboard;

public class SidebarEntry {

	final String left;
	final String right;
	
	public SidebarEntry(String left, String right) {
		this.left = left == null ? "" : left.substring(Math.min(left.length(), 16));
		this.right = right == null ? "" : right.substring(Math.min(right.length(), 16));
	}
	
}
