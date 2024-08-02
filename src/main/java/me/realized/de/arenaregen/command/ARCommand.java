package me.realized.de.arenaregen.command;

import org.bukkit.command.CommandSender;

import me.realized.de.arenaregen.ArenaRegen;
import me.realized.de.arenaregen.config.Lang;
import me.realized.de.arenaregen.selection.SelectionManager;
import me.realized.de.arenaregen.zone.ZoneManager;
import me.realized.duels.api.Duels;
import me.realized.duels.api.arena.ArenaManager;

public abstract class ARCommand {

	protected final ArenaManager arenaManager;
	protected final Lang lang;
	protected final SelectionManager selectionManager;
	protected final ZoneManager zoneManager;

	private final String name;
	private final String usage;
	private final String description;
	private final int length;
	private final boolean playerOnly;

	protected ARCommand(final ArenaRegen extension, final Duels api, final String name, final String usage, final String description, final int length, final boolean playerOnly) {

		this.arenaManager = api.getArenaManager();
		this.lang = extension.getLang();
		this.selectionManager = extension.getSelectionManager();
		this.zoneManager = extension.getZoneManager();
		this.name = name;
		this.usage = usage;
		this.description = description;
		this.length = length;
		this.playerOnly = playerOnly;

	}

	public String getName() {

		return name;

	}

	public String getUsage() {

		return usage;

	}

	public String getDescription() {

		return description;

	}

	public int getLength() {

		return length;

	}

	public boolean isPlayerOnly() {

		return playerOnly;

	}

	public abstract void execute(final CommandSender sender, final String label, final String[] args);

}