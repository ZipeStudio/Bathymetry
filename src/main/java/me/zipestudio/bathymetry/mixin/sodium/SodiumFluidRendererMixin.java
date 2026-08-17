package me.zipestudio.bathymetry.mixin.sodium;

//? if forge {
/*public class SodiumFluidRendererMixin {
}
*///?} elif >=1.21 {
import net.caffeinemc.mods.sodium.client.model.color.ColorProvider;
import net.caffeinemc.mods.sodium.client.model.light.LightPipeline;
import net.caffeinemc.mods.sodium.client.model.quad.ModelQuadViewMutable;
import net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.zipestudio.bathymetry.water.WaterDepthTint;

@Mixin(DefaultFluidRenderer.class)
public class SodiumFluidRendererMixin {

	@Shadow
	@SuppressWarnings("unused")
	private int[] quadColors;

	@Inject(method = "updateQuad", at = @At("RETURN"))
	private void bathymetry$deepenQuad(
		ModelQuadViewMutable quad,
		LevelSlice level,
		BlockPos pos,
		LightPipeline lighter,
		Direction dir,
		ModelQuadFacing facing,
		float brightness,
		ColorProvider<FluidState> colorProvider,
		FluidState fluidState,
		CallbackInfo ci
	) {
		WaterDepthTint.beginBlock(level, Minecraft.getInstance().level, pos, fluidState);
		if (!WaterDepthTint.active()) {
			return;
		}

		for (int i = 0; i < 4; i++) {
			float ramp = WaterDepthTint.rampAtWorldVertex(pos.getX() + quad.getX(i), pos.getZ() + quad.getZ(i));
			this.quadColors[i] = WaterDepthTint.darkenColorAbgr(this.quadColors[i], ramp);
		}

		WaterDepthTint.endBlock();
	}
}
//?} else {
/*import me.jellysquid.mods.sodium.client.model.color.ColorProvider;
import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.FluidRenderer;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.zipestudio.bathymetry.water.WaterDepthTint;

@Mixin(FluidRenderer.class)
public class SodiumFluidRendererMixin {

	@Shadow
	@SuppressWarnings("unused")
	private int[] quadColors;

	@Inject(method = "updateQuad", at = @At("RETURN"))
	private void bathymetry$deepenQuad(
		ModelQuadView quad,
		WorldSlice world,
		BlockPos pos,
		LightPipeline lighter,
		Direction dir,
		float brightness,
		ColorProvider<FluidState> colorProvider,
		FluidState fluidState,
		CallbackInfo ci
	) {
		WaterDepthTint.beginBlock(world, Minecraft.getInstance().level, pos, fluidState);
		if (!WaterDepthTint.active()) {
			return;
		}

		for (int i = 0; i < 4; i++) {
			float ramp = WaterDepthTint.rampAtWorldVertex(pos.getX() + quad.getX(i), pos.getZ() + quad.getZ(i));
			this.quadColors[i] = WaterDepthTint.darkenColorAbgr(this.quadColors[i], ramp);
		}

		WaterDepthTint.endBlock();
	}
}
*///?}
