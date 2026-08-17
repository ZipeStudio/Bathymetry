package me.zipestudio.bathymetry.client;

import net.lopymine.mossylib.logger.MossyLogger;

import me.zipestudio.bathymetry.Bathymetry;
import me.zipestudio.bathymetry.config.LeafyConfig;
import net.minecraft.client.Minecraft;

public class BathymetryClient {

	public static MossyLogger LOGGER = Bathymetry.LOGGER.extend("Client");

	public static void onInitializeClient() {
		LeafyConfig.getInstance();
		LOGGER.info("{} Client Initialized", Bathymetry.MOD_NAME);
	}

	public static void rebuildChunks() {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null || minecraft.levelRenderer == null) {
			return;
		}

		//? if >=26.2 {
		minecraft.levelRenderer.invalidateCompiledGeometry(
			minecraft.level,
			minecraft.options,
			minecraft.gameRenderer.mainCamera(),
			minecraft.getBlockColors()
		);
		//?} else {
		/*minecraft.levelRenderer.allChanged();
		*///?}

	}

}
