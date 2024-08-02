package me.realized.de.arenaregen.zone.task;

import java.util.Queue;

import me.realized.de.arenaregen.ArenaRegen;
import me.realized.de.arenaregen.nms.BlockInfo;
import me.realized.de.arenaregen.nms.NMSHandler;
import me.realized.de.arenaregen.util.Callback;
import me.realized.de.arenaregen.util.Pair;
import me.realized.de.arenaregen.util.Position;
import me.realized.de.arenaregen.zone.Zone;

public class ResetBlocksTask extends Task {

	private final Queue<Pair<Position, BlockInfo>> changed;

	public ResetBlocksTask(final ArenaRegen extension, final Zone zone, final Callback onDone, final Queue<Pair<Position, BlockInfo>> changed) {

		super(extension, zone, onDone);

		this.changed = changed;

	}

	@Override
	public void run() {

		int count = 0;
		Pair<Position, BlockInfo> current;
		while ((current = changed.poll()) != null) {

			final Position pos = current.getKey();
			final BlockInfo info = current.getValue();

			handler.setBlockFast(zone.getWorld(), pos.getX(), pos.getY(), pos.getZ(), info.getDataAsInt(), info.getType());

			count++;

			if (count >= config.getBlocksPerTick()) {

				return;

			}

		}

		cancel();

		// Skip re-lighting if using fallback handler.
		if (handler instanceof NMSHandler) {

			zone.startSyncTaskTimer(null);
			zone.getArena().setDisabled(false);

			if (onDone != null) {

				onDone.call();

			}

			return;

		}

		zone.startSyncTaskTimer(new RelightBlocksTask(extension, zone, onDone));

	}

}