package me.zipestudio.bathymetry.yacl;

import lombok.experimental.ExtensionMethod;
import me.zipestudio.bathymetry.Bathymetry;
import net.lopymine.mossylib.yacl.api.*;
import net.lopymine.mossylib.yacl.extension.SimpleOptionExtension;
import net.minecraft.client.gui.screens.Screen;

import dev.isxander.yacl3.api.controller.ColorControllerBuilder;

import me.zipestudio.bathymetry.client.BathymetryClient;
import me.zipestudio.bathymetry.config.LeafyConfig;

import java.awt.Color;

@ExtensionMethod(SimpleOptionExtension.class)
public class YACLConfigurationScreen {

	private YACLConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}

	public static Screen createScreen(Screen parent) {
		LeafyConfig defConfig = LeafyConfig.getNewInstance();
		LeafyConfig config = LeafyConfig.getInstance();

		return SimpleYACLScreen.startBuilder(Bathymetry.MOD_ID, parent, () -> {
					config.saveAsync();
					BathymetryClient.rebuildChunks();
				})
				.categories(getGeneralCategory(defConfig, config))
				.build();
	}

	private static SimpleCategory getGeneralCategory(LeafyConfig defConfig, LeafyConfig config) {
		return SimpleCategory.startBuilder("general")
				.groups(
						getMainGroup(defConfig, config),
						getDepthGroup(defConfig, config),
						getFogGroup(defConfig, config)
				);
	}

	private static SimpleGroup getMainGroup(LeafyConfig defConfig, LeafyConfig config) {
		return SimpleGroup.startBuilder("main").options(
				SimpleOption.<Boolean>startBuilder("mod_enabled")
						.withBinding(defConfig.isModEnabled(), config::isModEnabled, config::setModEnabled, false)
						.withController(),

				SimpleOption.<Float>startBuilder("intensity")
						.withBinding(defConfig.getIntensity(), config::getIntensity, config::setIntensity, false)
						.withController(0.0F, 2.0F, 0.05F),

				SimpleOption.<Color>startBuilder("shallow_tint")
						.withBinding(
								new Color(defConfig.getShallowTint()),
								() -> new Color(config.getShallowTint()),
								color -> config.setShallowTint(color.getRGB() & 0xFFFFFF),
								false
						)
						.custom(builder -> builder.controller(option -> ColorControllerBuilder.create(option).allowAlpha(false))),

				SimpleOption.<Color>startBuilder("deep_tint")
						.withBinding(
								new Color(defConfig.getDeepTint()),
								() -> new Color(config.getDeepTint()),
								color -> config.setDeepTint(color.getRGB() & 0xFFFFFF),
								false
						)
						.custom(builder -> builder.controller(option -> ColorControllerBuilder.create(option).allowAlpha(false)))
		);
	}

	private static SimpleGroup getDepthGroup(LeafyConfig defConfig, LeafyConfig config) {
		return SimpleGroup.startBuilder("depth").options(
				SimpleOption.<Integer>startBuilder("shallow_depth")
						.withBinding(defConfig.getShallowDepth(), config::getShallowDepth, config::setShallowDepth, false)
						.withController(0, 32, 1),

				SimpleOption.<Integer>startBuilder("deep_depth")
						.withBinding(defConfig.getDeepDepth(), config::getDeepDepth, config::setDeepDepth, false)
						.withController(1, 48, 1),

				SimpleOption.<Boolean>startBuilder("smooth_edges")
						.withBinding(defConfig.isSmoothEdges(), config::isSmoothEdges, config::setSmoothEdges, false)
						.withController(),

				SimpleOption.<Integer>startBuilder("smoothing_radius")
						.withBinding(defConfig.getSmoothingRadius(), config::getSmoothingRadius, config::setSmoothingRadius, false)
						.withController(0, 6, 1)
		);
	}

	private static SimpleGroup getFogGroup(LeafyConfig defConfig, LeafyConfig config) {
		return SimpleGroup.startBuilder("fog").options(
				SimpleOption.<Boolean>startBuilder("underwater_fog")
						.withBinding(defConfig.isUnderwaterFog(), config::isUnderwaterFog, config::setUnderwaterFog, false)
						.withController(),

				SimpleOption.<Float>startBuilder("deep_fog_distance")
						.withBinding(defConfig.getDeepFogDistance(), config::getDeepFogDistance, config::setDeepFogDistance, false)
						.withController(0.1F, 1.0F, 0.05F)
		);
	}
}
