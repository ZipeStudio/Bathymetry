package me.zipestudio.bathymetry.water;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.level.material.Fluids;

import me.zipestudio.bathymetry.config.LeafyConfig;

public final class UnderwaterFog {

	private static final float MIN_MULTIPLIER = 0.1F;

	private UnderwaterFog() {
		throw new IllegalStateException("Utility class");
	}

	public static float scale(float distance) {

		float multiplier = multiplier();
		return multiplier >= 1.0F ? distance : distance * multiplier;
	}

	public static float multiplier() {

		LeafyConfig config = LeafyConfig.getInstance();
		if (!config.isModEnabled() || !config.isUnderwaterFog() || config.getIntensity() <= 0.0F) {
			return 1.0F;
		}

		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null || minecraft.gameRenderer == null) {
			return 1.0F;
		}

		//? if >=26.2 {
		/*Camera camera = minecraft.gameRenderer.mainCamera();
		*///?} else {
		Camera camera = minecraft.gameRenderer.getMainCamera();
		//?}
		if (camera == null || camera.getFluidInCamera() != FogType.WATER) {
			return 1.0F;
		}

		float depth = depthBelowSurface(minecraft, camera, config);
		if (depth <= 0.0F) {
			return 1.0F;
		}

		float shallow = config.getShallowDepth();
		float deep = Math.max(config.getDeepDepth(), shallow + 1.0F);
		float ramp = smoothstep((depth - shallow) / (deep - shallow)) * config.getIntensity();

		float multiplier = 1.0F + (config.getDeepFogDistance() - 1.0F) * ramp;
		return Math.max(MIN_MULTIPLIER, Math.min(multiplier, 1.0F));
	}

	private static float depthBelowSurface(Minecraft minecraft, Camera camera, LeafyConfig config) {

		//? if >=1.21.11 {
		/*BlockPos cameraPos = camera.blockPosition();
		double eyeY = camera.position().y;
		*///?} else {
		BlockPos cameraPos = camera.getBlockPosition();
		double eyeY = camera.getPosition().y;
		//?}
		int x = cameraPos.getX();
		int y = cameraPos.getY();
		int z = cameraPos.getZ();
		int maxScan = config.getDeepDepth() + 2;

		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		if (!isWater(minecraft, cursor.set(x, y, z))) {
			return 0.0F;
		}

		int surfaceY = y;
		for (int scanY = y + 1; scanY <= y + maxScan; scanY++) {
			if (!isWater(minecraft, cursor.set(x, scanY, z))) {
				break;
			}
			surfaceY = scanY;
		}

		return (float) (surfaceY + 1.0 - eyeY);
	}

	private static boolean isWater(Minecraft minecraft, BlockPos pos) {

		ClientLevel level = minecraft.level;
		if (level == null) return false;

		return level.getFluidState(pos).getType().isSame(Fluids.WATER);
	}

	private static float smoothstep(float t) {

		if (!(t > 0.0F)) {
			return 0.0F;
		}

		if (t >= 1.0F) {
			return 1.0F;
		}

		return t * t * (3.0F - 2.0F * t);
	}
}
