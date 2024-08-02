package me.realized.de.arenaregen.zone;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.bukkit.Chunk;
import org.bukkit.World;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class RegionDetection {

	public static Set<ProtectedRegion> getRegionsInChunk(Chunk chunk) {

		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		Set<Future<Set<ProtectedRegion>>> futures = new HashSet<>();

		// Divide the chunk into sections for parallel processing.
		for (int x = 0; x < 16; x++) {

			for (int z = 0; z < 16; z++) {

				futures.add(executor.submit(new RegionTask(chunk, x, z)));
			}

		}

		Set<ProtectedRegion> regions = new HashSet<>();
		for (Future<Set<ProtectedRegion>> future : futures) {

			try {

				regions.addAll(future.get());

			}
			catch (InterruptedException | ExecutionException error) {

				error.printStackTrace();

			}

		}

		executor.shutdown();

		return regions;

	}

	static class RegionTask implements Callable<Set<ProtectedRegion>> {

		private final Chunk chunk;
		private final int x;
		private final int z;

		public RegionTask(Chunk chunk, int x, int z) {

			this.chunk = chunk;
			this.x = x;
			this.z = z;

		}

		@Override
		public Set<ProtectedRegion> call() {

			Set<ProtectedRegion> regions = new HashSet<>();
			World world = chunk.getWorld();

			RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionQuery query = container.createQuery();

			for (int y = 0; y < world.getMaxHeight(); y++) {

				org.bukkit.Location bukkitLocation = new org.bukkit.Location(world, chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z);
				Location worldEditLocation = BukkitAdapter.adapt(bukkitLocation);
				ApplicableRegionSet set = query.getApplicableRegions(worldEditLocation);

				regions.addAll(set.getRegions());

			}

			return regions;

		}

	}

}