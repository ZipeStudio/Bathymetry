package me.zipestudio.bathymetry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import me.zipestudio.bathymetry.water.UnderwaterFog;

//? if >=26.1 {
/*import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?} elif >=1.21.6 {
/*import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?} elif <1.21.2 {
import net.minecraft.client.renderer.FogRenderer;
import org.spongepowered.asm.mixin.injection.ModifyArg;
//?} else {
/*import net.minecraft.client.renderer.FogRenderer;
*///?}

//? if >=1.21.6 {
/*@Mixin(WaterFogEnvironment.class)
*///?} else {
@Mixin(FogRenderer.class)
//?}
public class FogDistanceMixin {

	//? if >=26.1 {
	/*@Inject(method = "setupFog", at = @At("RETURN"))
	private void bathymetry$deepenFog(
		FogData data,
		Camera camera,
		ClientLevel level,
		float renderDistance,
		DeltaTracker deltaTracker,
		CallbackInfo ci
	) {
		data.environmentalEnd = UnderwaterFog.scale(data.environmentalEnd);
		data.skyEnd = UnderwaterFog.scale(data.skyEnd);
		data.cloudEnd = UnderwaterFog.scale(data.cloudEnd);
	}
	*///?} elif >=1.21.6 {
	/*@Inject(method = "setupFog", at = @At("RETURN"))
	private void bathymetry$deepenFog(
		FogData data,
		Entity entity,
		BlockPos pos,
		ClientLevel level,
		float renderDistance,
		DeltaTracker deltaTracker,
		CallbackInfo ci
	) {
		data.environmentalEnd = UnderwaterFog.scale(data.environmentalEnd);
		data.skyEnd = UnderwaterFog.scale(data.skyEnd);
		data.cloudEnd = UnderwaterFog.scale(data.cloudEnd);
	}
	*///?} elif <1.21.2 {
	@ModifyArg(
		method = "setupFog",
		at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V")
	)
	private static float bathymetry$deepenFog(float fogEnd) {
		return UnderwaterFog.scale(fogEnd);
	}
	//?}
}
