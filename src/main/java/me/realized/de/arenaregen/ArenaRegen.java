package me.realized.de.arenaregen;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import me.realized.de.arenaregen.command.ArenaregenCommand;
import me.realized.de.arenaregen.config.Config;
import me.realized.de.arenaregen.config.Lang;
import me.realized.de.arenaregen.nms.NMSHandler;
import me.realized.de.arenaregen.selection.SelectionManager;
import me.realized.de.arenaregen.zone.ZoneManager;
import me.realized.duels.api.extension.DuelsExtension;

public class ArenaRegen extends DuelsExtension {

	private Config configuration;
	private Lang lang;
	private SelectionManager selectionManager;
	private ZoneManager zoneManager;
	public static StateFlag DUELS_ARENA = new StateFlag("duels-arena", false);

	public Config getConfiguration() {

		return configuration;

	}

	public Lang getLang() {

		return lang;

	}

	public void setLang(Lang lang) {

		this.lang = lang;

	}

	public SelectionManager getSelectionManager() {

		return selectionManager;

	}

	public ZoneManager getZoneManager() {

		return zoneManager;

	}

	@Override
	public void onEnable() {

		this.configuration = new Config(this);

		if (configuration.isTrackBlockChanges() && configuration.isAllowArenaBlockBreak()) {

			warn("The config options 'track-block-changes' and 'allow-arena-block-break' are incompatible with each other.");

		}

		this.lang = new Lang(this);

		registerCustomFlag();

		info("NMSHandler: Using " + getHandler().getClass().getName());

		this.selectionManager = new SelectionManager(this, api);
		this.zoneManager = new ZoneManager(this, api);

		api.registerSubCommand("duels", new ArenaregenCommand(this, api));

	}

	private void registerCustomFlag() {

		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
		try {

			registry.register(DUELS_ARENA);

		}
		catch (FlagConflictException error) {

			Flag<?> existing = registry.get("duels-arena");
			if (existing instanceof StateFlag) {

				// Use the existing flag.
				DUELS_ARENA = (StateFlag) existing;

			}
			else {

				warn("Conflict with an existing flag!");

			}

		}

	}

	public NMSHandler getHandler() {

		return new NMSHandler();

	}

	@Override
	public void onDisable() {

		zoneManager.handleDisable();

	}

	public void info(final String s) {

		api.info("[" + getName() + " Extension] " + s);

	}

	public void warn(final String s) {

		api.warn("[" + getName() + " Extension] " + s);

	}

	public void error(final String s) {

		api.error("[" + getName() + " Extension] " + s);

	}

	public void error(final String s, final Throwable thrown) {

		api.error("[" + getName() + " Extension] " + s, thrown);

	}

}
