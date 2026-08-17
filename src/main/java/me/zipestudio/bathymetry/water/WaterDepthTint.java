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
		private float shallowR;
		private float shallowG;
		private float shallowB;
		private float deepR;
		private float deepG;
		private float deepB;

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

	private WaterDepthTint() {
	}

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

		int shallow = config.getShallowTint();
		scope.shallowR = ((shallow >> 16) & 0xFF) / 255.0F;
		scope.shallowG = ((shallow >> 8) & 0xFF) / 255.0F;
		scope.shallowB = (shallow & 0xFF) / 255.0F;

		int deep = config.getDeepTint();
		scope.deepR = ((deep >> 16) & 0xFF) / 255.0F;
		scope.deepG = ((deep >> 8) & 0xFF) / 255.0F;
		scope.deepB = (deep & 0xFF) / 255.0F;
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
		int r = clampChannel(((color >> 16) & 0xFF) * factor(scope, ramp, scope.shallowR, scope.deepR));
		int g = clampChannel(((color >> 8) & 0xFF) * factor(scope, ramp, scope.shallowG, scope.deepG));
		int b = clampChannel((color & 0xFF) * factor(scope, ramp, scope.shallowB, scope.deepB));
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public static int darkenColorAbgr(int color, float ramp) {
		Scope scope = SCOPE.get();
		int a = color >>> 24;
		int b = clampChannel(((color >> 16) & 0xFF) * factor(scope, ramp, scope.shallowB, scope.deepB));
		int g = clampChannel(((color >> 8) & 0xFF) * factor(scope, ramp, scope.shallowG, scope.deepG));
		int r = clampChannel((color & 0xFF) * factor(scope, ramp, scope.shallowR, scope.deepR));
		return (a << 24) | (b << 16) | (g << 8) | r;
	}

	public static float redFactor(float ramp) {
		Scope scope = SCOPE.get();
		return factor(scope, ramp, scope.shallowR, scope.deepR);
	}

	public static float greenFactor(float ramp) {
		Scope scope = SCOPE.get();
		return factor(scope, ramp, scope.shallowG, scope.deepG);
	}

	public static float blueFactor(float ramp) {
		Scope scope = SCOPE.get();
		return factor(scope, ramp, scope.shallowB, scope.deepB);
	}

	private static float factor(Scope scope, float ramp, float shallow, float deep) {
		float target = shallow + (deep - shallow) * ramp;
		float value = 1.0F + (target - 1.0F) * scope.intensity;
		return value < 0.0F ? 0.0F : Math.min(value, 1.0F);
	}

	private static int clampChannel(float value) {
		int rounded = Math.round(value);
		return rounded < 0 ? 0 : Math.min(rounded, 255);
	}

	private static float smoothstep(Scope scope, float thickness) {
		float t = (thickness - scope.shallowDepth) / (scope.deepDepth - scope.shallowDepth);
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
