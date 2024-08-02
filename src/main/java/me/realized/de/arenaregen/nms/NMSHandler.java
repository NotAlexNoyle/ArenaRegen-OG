package me.realized.de.arenaregen.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import me.realized.de.arenaregen.util.ReflectionUtil;

public class NMSHandler implements NMS {

	@Override
	public void sendChunkUpdate(final Player player, final Chunk chunk) {

		try {

			Object craftPlayer = ReflectionUtil.getCBClass("entity.CraftPlayer").cast(player);
			Object nmsPlayer = ReflectionUtil.getMethod(craftPlayer.getClass(), "getHandle").invoke(craftPlayer);

			Object craftChunk = ReflectionUtil.getCBClass("CraftChunk").cast(chunk);
			Object nmsChunk = ReflectionUtil.getMethod(craftChunk.getClass(), "getHandle").invoke(craftChunk);

			Class<?> serverPlayerClass = ReflectionUtil.getClassUnsafe("net.minecraft.server.level.ServerPlayer");
			Class<?> levelChunkClass = ReflectionUtil.getClassUnsafe("net.minecraft.world.level.chunk.LevelChunk");

			Class<?> packetClass = ReflectionUtil.getClassUnsafe("net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket");
			Class<?> lightEngineClass = ReflectionUtil.getClassUnsafe("net.minecraft.world.level.lighting.LightEngine");

			Method getLightEngineMethod = ReflectionUtil.getMethod(levelChunkClass, "getLightEngine");
			Object lightEngine = getLightEngineMethod.invoke(nmsChunk);

			Constructor<?> packetConstructor = ReflectionUtil.getConstructor(packetClass, levelChunkClass, lightEngineClass, int[].class, boolean[].class);
			Object packet = packetConstructor.newInstance(nmsChunk, lightEngine, null, null);

			Field connectionField = ReflectionUtil.getField(serverPlayerClass, "connection");
			Object connection = connectionField.get(nmsPlayer);

			Method sendPacketMethod = ReflectionUtil.getMethod(connection.getClass(), "send", ReflectionUtil.getClassUnsafe("net.minecraft.network.protocol.Packet"));
			sendPacketMethod.invoke(connection, packet);

		}
		catch (Exception error) {

			error.printStackTrace();

		}

	}

	@Override
	public void setBlockFast(final World world, final int x, final int y, final int z, final int data, final Material material) {

		try {

			Object craftWorld = ReflectionUtil.getCBClass("CraftWorld").cast(world);

			Class<?> blockPositionClass = ReflectionUtil.getClassUnsafe("net.minecraft.core.BlockPosition");
			Constructor<?> blockPositionConstructor = ReflectionUtil.getConstructor(blockPositionClass, int.class, int.class, int.class);
			Object blockPosition = blockPositionConstructor.newInstance(x, y, z);

			Class<?> chunkClass = ReflectionUtil.getClassUnsafe("net.minecraft.world.level.chunk.Chunk");
			Object craftChunk = ReflectionUtil.getMethod(craftWorld.getClass(), "getChunkAt", int.class, int.class).invoke(craftWorld, x >> 4, z >> 4);
			Object nmsChunk = ReflectionUtil.getMethod(craftChunk.getClass(), "getHandle").invoke(craftChunk);

			Object block = CraftMagicNumbers.getBlock(material);
			Method getBlockDataMethod = ReflectionUtil.getMethod(block.getClass(), "m");
			Object blockData = getBlockDataMethod.invoke(block);

			Method setBlockDataMethod = ReflectionUtil.getMethod(chunkClass, "a", blockPositionClass, blockData.getClass(), boolean.class);
			setBlockDataMethod.invoke(nmsChunk, blockPosition, blockData, true);

			updateLighting(world, x, y, z);
		}
		catch (Exception error) {

			error.printStackTrace();

		}

	}

	@Override
	public void updateLighting(final World world, final int x, final int y, final int z) {

		try {

			Object craftWorld = ReflectionUtil.getCBClass("CraftWorld").cast(world);
			Object nmsWorld = ReflectionUtil.getMethod(craftWorld.getClass(), "getHandle").invoke(craftWorld);

			Class<?> blockPositionClass = ReflectionUtil.getClassUnsafe("net.minecraft.core.BlockPosition");
			Constructor<?> blockPositionConstructor = ReflectionUtil.getConstructor(blockPositionClass, int.class, int.class, int.class);
			Object blockPosition = blockPositionConstructor.newInstance(x, y, z);

			Class<?> lightEngineClass = ReflectionUtil.getClassUnsafe("net.minecraft.world.level.lighting.LightEngine");
			Method getLightEngineMethod = ReflectionUtil.getMethod(nmsWorld.getClass(), "getLightEngine");
			Object lightEngine = getLightEngineMethod.invoke(nmsWorld);

			Method checkBlockMethod = ReflectionUtil.getMethod(lightEngineClass, "a", blockPositionClass);
			checkBlockMethod.invoke(lightEngine, blockPosition);

		}
		catch (Exception error) {

			error.printStackTrace();

		}

	}

}