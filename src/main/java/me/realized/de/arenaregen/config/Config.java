package me.realized.de.arenaregen.config;

import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import me.realized.de.arenaregen.ArenaRegen;

public class Config {

	private final Material selectingTool;
	private final boolean trackBlockChanges;
	private final int blocksPerTick;
	private final boolean allowArenaBlockBreak;
	private final String blockResetHandlerVersion;
	private final boolean removeDroppedItems;
	private final List<String> removeEntities;
	private final boolean preventBlockBurn;
	private final boolean preventBlockMelt;
	private final boolean preventBlockExplode;
	private final boolean preventFireSpread;
	private final boolean preventLeafDecay;

	public Config(final ArenaRegen extension) {

		final FileConfiguration config = extension.getConfig();

		this.selectingTool = Material.getMaterial(config.getString("selecting-tool", "IRON_AXE"));
		this.trackBlockChanges = config.getBoolean("experimental.track-block-changes", false);
		this.allowArenaBlockBreak = config.getBoolean("allow-arena-block-break", false);
		this.blocksPerTick = config.getInt("blocks-per-tick", 25);
		this.blockResetHandlerVersion = config.getString("block-reset-handler-version", "auto");
		this.removeDroppedItems = config.getBoolean("remove-dropped-items", true);
		this.removeEntities = config.isList("remove-entities") ? config.getStringList("remove-entities") : Collections.emptyList();
		this.preventBlockBurn = config.getBoolean("prevent-block-burn", true);
		this.preventBlockMelt = config.getBoolean("prevent-block-melt", true);
		this.preventBlockExplode = config.getBoolean("prevent-block-explode", true);
		this.preventFireSpread = config.getBoolean("prevent-fire-spread", true);
		this.preventLeafDecay = config.getBoolean("prevent-leaf-decay", true);

	}

	public Material getSelectingTool() {

		return selectingTool;

	}

	public boolean isTrackBlockChanges() {

		return trackBlockChanges;

	}

	public int getBlocksPerTick() {

		return blocksPerTick;

	}

	public boolean isAllowArenaBlockBreak() {

		return allowArenaBlockBreak;

	}

	public String getBlockResetHandlerVersion() {

		return blockResetHandlerVersion;

	}

	public boolean isRemoveDroppedItems() {

		return removeDroppedItems;

	}

	public List<String> getRemoveEntities() {

		return removeEntities;

	}

	public boolean isPreventBlockBurn() {

		return preventBlockBurn;

	}

	public boolean isPreventBlockMelt() {

		return preventBlockMelt;

	}

	public boolean isPreventBlockExplode() {

		return preventBlockExplode;

	}

	public boolean isPreventFireSpread() {

		return preventFireSpread;

	}

	public boolean isPreventLeafDecay() {

		return preventLeafDecay;

	}

}