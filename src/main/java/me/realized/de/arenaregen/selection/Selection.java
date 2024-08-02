package me.realized.de.arenaregen.selection;

import org.bukkit.Location;

public class Selection {

	private Location first;
	private Location second;

	public Location getFirst() {

		return first;

	}

	public void setFirst(Location first) {

		this.first = first;

	}

	public Location getSecond() {

		return second;

	}

	public void setSecond(Location second) {

		this.second = second;

	}

	public boolean isSelected() {

		return first != null && second != null;

	}

}