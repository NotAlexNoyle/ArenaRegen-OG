package me.realized.de.arenaregen.nms;

import java.lang.reflect.Method;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

public class BlockInfo {

	private final Material type;
	private final BlockData data;

	public BlockInfo(final Material type, final BlockData data) {

		this.type = type;
		this.data = data;

	}

	public BlockInfo() {

		this(Material.AIR, Material.AIR.createBlockData());

	}

	public BlockInfo(final BlockState state) {

		this(state.getType(), state.getBlockData());

	}

	public Material getType() {

		return type;

	}

	public BlockData getData() {

		return data;

	}

	public boolean matches(final Block block) {

		return block.getType() == type && block.getBlockData().matches(data);

	}

	public int getDataAsInt() {

		try {

			// Get the CraftBlockData class.
			Class<?> craftBlockDataClass = Class.forName("org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData");

			// Get the handle to the state.
			Method getStateMethod = craftBlockDataClass.getMethod("getState");
			Object nmsState = getStateMethod.invoke(data);

			// Get the Block class and the getId method.
			Class<?> blockClass = Class.forName("net.minecraft.world.level.block.Block");
			Method getIdMethod = blockClass.getMethod("getId", Class.forName("net.minecraft.world.level.block.state.BlockState"));

			// Invoke the getId method.
			return (int) getIdMethod.invoke(null, nmsState);

		}
		catch (Exception error) {

			error.printStackTrace();

			// Return a default value.
			return -1;

		}

	}

	@Override
	public String toString() {

		return type + ";" + data.getAsString();

	}


}