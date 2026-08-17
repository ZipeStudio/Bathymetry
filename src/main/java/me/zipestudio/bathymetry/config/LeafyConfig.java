package me.zipestudio.bathymetry.config;

import lombok.*;
import net.lopymine.mossylib.loader.MossyLoader;
import net.lopymine.mossylib.utils.*;
import org.slf4j.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.zipestudio.bathymetry.Bathymetry;

import java.io.*;
import java.util.concurrent.CompletableFuture;

import static net.lopymine.mossylib.utils.CodecUtils.option;

@Getter
@Setter
@AllArgsConstructor
public class LeafyConfig {

	public static final int DEFAULT_SHALLOW_TINT = 0xFFFFFF;

	public static final int DEFAULT_DEEP_TINT = 0x6FC4F0;


	private static Codec<Integer> hexColor(int fallback) {
		return Codec.STRING.xmap(value -> parseHexColor(value, fallback), LeafyConfig::formatHexColor);
	}

	public static final Codec<LeafyConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			option("mod_enabled",   true,              Codec.BOOL, LeafyConfig::isModEnabled),
			option("intensity",     1.0F,                 Codec.FLOAT, LeafyConfig::getIntensity),
			option("shallow_tint",  DEFAULT_SHALLOW_TINT, hexColor(DEFAULT_SHALLOW_TINT), LeafyConfig::getShallowTint),
			option("deep_tint",     DEFAULT_DEEP_TINT,    hexColor(DEFAULT_DEEP_TINT),    LeafyConfig::getDeepTint),
			option("shallow_depth", 3,                 Codec.INT,  LeafyConfig::getShallowDepth),
			option("deep_depth",    20,                Codec.INT,  LeafyConfig::getDeepDepth),
			option("smooth_edges",  true,              Codec.BOOL, LeafyConfig::isSmoothEdges),
			option("smoothing_radius", 3,             Codec.INT,  LeafyConfig::getSmoothingRadius),
			option("underwater_fog", false,             Codec.BOOL, LeafyConfig::isUnderwaterFog),
			option("deep_fog_distance", 0.50F,         Codec.FLOAT, LeafyConfig::getDeepFogDistance)
	).apply(instance, LeafyConfig::new));

	private static final File CONFIG_FILE = MossyLoader.getConfigDir().resolve(Bathymetry.MOD_ID + ".json5").toFile();
	private static final Logger LOGGER = LoggerFactory.getLogger(Bathymetry.MOD_NAME + "/Config");
	private static LeafyConfig INSTANCE;

	private boolean modEnabled;

	private float intensity;

	private int shallowTint;

	private int deepTint;

	private int shallowDepth;

	private int deepDepth;

	private boolean smoothEdges;

	private int smoothingRadius;

	private boolean underwaterFog;

	private float deepFogDistance;

	@SuppressWarnings("unused")
	private LeafyConfig() {
		throw new IllegalArgumentException();
	}

	private static int parseHexColor(String value, int fallback) {
		try {
			return Integer.parseInt(value.trim().replace("#", ""), 16) & 0xFFFFFF;
		} catch (NumberFormatException e) {
			LOGGER.warn("Not a hex color: '{}', falling back to {}", value, formatHexColor(fallback));
			return fallback;
		}
	}

	private static String formatHexColor(int value) {
		return String.format("#%06X", value & 0xFFFFFF);
	}

	public static LeafyConfig getInstance() {
		return INSTANCE == null ? reload() : INSTANCE;
	}

	public static LeafyConfig reload() {
		return INSTANCE = LeafyConfig.read();
	}

	public static LeafyConfig getNewInstance() {
		return CodecUtils.parseNewInstanceHacky(CODEC);
	}

	private static LeafyConfig read() {
		return ConfigUtils.readConfig(CODEC, CONFIG_FILE, LOGGER);
	}

	public void saveAsync() {
		CompletableFuture.runAsync(this::save);
	}

	public void save() {
		ConfigUtils.saveConfig(this, CODEC, CONFIG_FILE, LOGGER);
	}
}
