package me.zipestudio.bathymetry.water;

import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import me.zipestudio.bathymetry.config.LeafyConfig;

public final class WaterDepthTint {

	public static final int RED_SHIFT = 16;
	public static final int GREEN_SHIFT = 8;
	public static final int BLUE_SHIFT = 0;

	private static final long ABSENT = Long.MIN_VALUE;

	private static final class Scope {

		private Object region;
		private final Long2LongOpenHashMap columns = new Long2LongOpenHashMap();
		private final Long2FloatOpenHashMap corners = new Long2FloatOpenHashMap();

		private Level level;
		private int blockX;
		private int blockY;
		private int blockZ;
		private boolean active;

		private float intensity;
		private float shallowDepth;
		private float deepDepth;
		private int maxScan;
		private boolean smoothEdges;
		private int smoothingRadius;
		private int shallowTint;
		private int deepTint;

		private Scope() {
			this.columns.defaultReturnValue(ABSENT);
			this.corners.defaultReturnValue(Float.NaN);
		}

		private void enter(Object newRegion) {
			if (this.region != newRegion) {
				this.region = newRegion;
				this.columns.clear();
				this.corners.clear();
			}
		}
	}

	private static final ThreadLocal<Scope> SCOPE = ThreadLocal.withInitial(Scope::new);

	public static void beginBlock(Object region, Level level, BlockPos pos, FluidState fluidState) {
		Scope scope = SCOPE.get();
		scope.enter(region);
		scope.level = level;
		scope.blockX = pos.getX();
		scope.blockY = pos.getY();
		scope.blockZ = pos.getZ();

		LeafyConfig config = LeafyConfig.getInstance();
		boolean neutral = config.getShallowTint() == 0xFFFFFF && config.getDeepTint() == 0xFFFFFF;
		scope.active = config.isModEnabled()
				&& config.getIntensity() > 0.0F
				&& !neutral
				&& level != null
				&& isWater(fluidState);
		if (!scope.active) {
			return;
		}

		scope.intensity = config.getIntensity();
		scope.shallowDepth = config.getShallowDepth();
		scope.deepDepth = Math.max(config.getDeepDepth(), scope.shallowDepth + 1.0F);
		scope.maxScan = (int) scope.deepDepth + 2;
		scope.smoothEdges = config.isSmoothEdges();
		scope.smoothingRadius = Math.max(0, config.getSmoothingRadius());

		scope.shallowTint = config.getShallowTint();
		scope.deepTint = config.getDeepTint();
	}

	public static void endBlock() {
		SCOPE.get().active = false;
	}

	public static boolean active() {
		return SCOPE.get().active;
	}

	public static float rampAtVertex(float localX, float localZ) {
		Scope scope = SCOPE.get();
		if (!scope.active) {
			return 0.0F;
		}

		int cornerX = scope.blockX + Math.round(localX - (scope.blockX & 15));
		int cornerZ = scope.blockZ + Math.round(localZ - (scope.blockZ & 15));
		return rampAtCorner(scope, cornerX, cornerZ);
	}

	public static float rampAtWorldVertex(float worldX, float worldZ) {
		Scope scope = SCOPE.get();
		if (!scope.active) {
			return 0.0F;
		}

		return rampAtCorner(scope, Math.round(worldX), Math.round(worldZ));
	}

	private static float rampAtCorner(Scope scope, int cornerX, int cornerZ) {
		float thickness = scope.smoothEdges
				? cornerThickness(scope, cornerX, cornerZ)
				: columnThickness(scope, scope.blockX, scope.blockZ);
		return smoothstep(scope, thickness);
	}

	public static int darkenColor(int color, float ramp) {
		Scope scope = SCOPE.get();
		int a = color >>> 24;
		int r = clampChannel(((color >> 16) & 0xFF) * channelFactor(scope, ramp, RED_SHIFT));
		int g = clampChannel(((color >> 8) & 0xFF) * channelFactor(scope, ramp, GREEN_SHIFT));
		int b = clampChannel((color & 0xFF) * channelFactor(scope, ramp, BLUE_SHIFT));
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static int darkenColorAbgr(int color, float ramp) {
		return swapRedBlue(darkenColor(swapRedBlue(color), ramp));
	}

	private static int swapRedBlue(int color) {
		return (color & 0xFF00FF00) | ((color >> 16) & 0xFF) | ((color & 0xFF) << 16);
	}

	public static float channelFactor(float ramp, int shift) {
		return channelFactor(SCOPE.get(), ramp, shift);
	}

	private static float channelFactor(Scope scope, float ramp, int shift) {
		return factor(scope, ramp, tintChannel(scope.shallowTint, shift), tintChannel(scope.deepTint, shift));
	}

	public static float tintChannel(int tint, int shift) {
		return ((tint >>> shift) & 0xFF) * (1.0F / 255.0F);
	}

	private static float factor(Scope scope, float ramp, float shallow, float deep) {
		return channelFactor(ramp, scope.intensity, shallow, deep);
	}

	public static float channelFactor(float ramp, float intensity, float shallow, float deep) {
		float target = shallow + (deep - shallow) * ramp;
		float value = 1.0F + (target - 1.0F) * intensity;
		return value < 0.0F ? 0.0F : Math.min(value, 1.0F);
	}

	private static int clampChannel(float value) {
		int rounded = Math.round(value);
		return rounded < 0 ? 0 : Math.min(rounded, 255);
	}

	private static float smoothstep(Scope scope, float thickness) {
		return rampFromThickness(thickness, scope.shallowDepth, scope.deepDepth);
	}

	public static float rampFromThickness(float thickness, float shallowDepth, float deepDepth) {
		float t = (thickness - shallowDepth) / (deepDepth - shallowDepth);
		if (!(t > 0.0F)) {
			return 0.0F;
		}
		if (t >= 1.0F) {
			return 1.0F;
		}
		return t * t * (3.0F - 2.0F * t);
	}

	private static float cornerThickness(Scope scope, int cornerX, int cornerZ) {
		long key = cornerKey(cornerX, cornerZ, scope.blockY);
		float cached = scope.corners.get(key);
		if (!Float.isNaN(cached)) {
			return cached;
		}

		int radius = scope.smoothingRadius;
		float peak = radius + 1.0F;
		float sum = 0.0F;
		float weight = 0.0F;
		for (int dx = -1 - radius; dx <= radius; dx++) {
			float weightX = peak - Math.abs(dx + 0.5F);
			if (weightX <= 0.0F) {
				continue;
			}
			for (int dz = -1 - radius; dz <= radius; dz++) {
				float weightZ = peak - Math.abs(dz + 0.5F);
				if (weightZ <= 0.0F) {
					continue;
				}
				int thickness = columnThickness(scope, cornerX + dx, cornerZ + dz);
				if (thickness > 0) {
					float w = weightX * weightZ;
					sum += thickness * w;
					weight += w;
				}
			}
		}

		float average = weight <= 0.0F ? 0.0F : sum / weight;
		scope.corners.put(key, average);
		return average;
	}

	private static int columnThickness(Scope scope, int x, int z) {
		int y = scope.blockY;

		long key = columnKey(x, y, z);
		long cached = scope.columns.get(key);
		if (cached != ABSENT) {
			int surfaceY = (int) (cached >> 32);
			int floorY = (int) cached;
			if (y >= floorY && y <= surfaceY) {
				return surfaceY - floorY + 1;
			}
		}

		LevelChunk chunk = scope.level.getChunk(x >> 4, z >> 4);
		if (chunk == null) {
			return 0;
		}

		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		if (!isWater(chunk.getBlockState(cursor.set(x, y, z)).getFluidState())) {
			return 0;
		}

		int surfaceY = y;
		for (int scanY = y + 1; scanY <= y + scope.maxScan; scanY++) {
			if (!isWater(chunk.getBlockState(cursor.set(x, scanY, z)).getFluidState())) {
				break;
			}
			surfaceY = scanY;
		}

		int floorY = y;
		for (int scanY = y - 1; scanY >= y - scope.maxScan; scanY--) {
			if (!isWater(chunk.getBlockState(cursor.set(x, scanY, z)).getFluidState())) {
				break;
			}
			floorY = scanY;
		}

		scope.columns.put(key, ((long) surfaceY << 32) | (floorY & 0xFFFFFFFFL));
		return surfaceY - floorY + 1;
	}

	private static boolean isWater(FluidState fluidState) {
		return fluidState.getType().isSame(Fluids.WATER);
	}

	private static long columnKey(int x, int y, int z) {
		return ((long) (x & 0x3FFFFFF) << 38)
			| ((long) (z & 0x3FFFFFF) << 12)
			| (((y >> 4) + 64) & 0xFFFL);
	}

	private static long cornerKey(int x, int z, int y) {
		return ((long) (x & 0x3FFFFFF) << 38)
			| ((long) (z & 0x3FFFFFF) << 12)
			| ((y + 64) & 0xFFFL);
	}

}
