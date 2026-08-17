package me.zipestudio.bathymetry.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.zipestudio.bathymetry.water.WaterDepthTint;

//? if >=26.1 {
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidRenderer;
//?} else {
/*import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.world.level.BlockAndTintGetter;
*///?}

//? if >=26.1 {
@Mixin(FluidRenderer.class)
//?} else {
/*@Mixin(LiquidBlockRenderer.class)
*///?}
public class FluidTintMixin {

	@Inject(method = "tesselate", at = @At("HEAD"))
	private void bathymetry$beginBlock(
		BlockAndTintGetter region,
		BlockPos pos,
		//? if >=26.1 {
		FluidRenderer.Output output,
		//?} else {
		/*VertexConsumer buffer,
		*///?}
		BlockState blockState,
		FluidState fluidState,
		CallbackInfo ci
	) {
		Level level = Minecraft.getInstance().level;
		WaterDepthTint.beginBlock(region, level, pos, fluidState);
	}

	@Inject(method = "tesselate", at = @At("RETURN"))
	private void bathymetry$endBlock(
		BlockAndTintGetter region,
		BlockPos pos,
		//? if >=26.1 {
		FluidRenderer.Output output,
		//?} else {
		/*VertexConsumer buffer,
		*///?}
		BlockState blockState,
		FluidState fluidState,
		CallbackInfo ci
	) {
		WaterDepthTint.endBlock();
	}

	//? if >=26.1 {
	@WrapOperation(
		method = "addFace",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/FluidRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFIFFI)V")
	)
	private void bathymetry$deepenVertex(
		FluidRenderer self, VertexConsumer builder,
		float x, float y, float z, int color, float u, float v, int light,
		Operation<Void> original
	) {
		if (WaterDepthTint.active()) {
			float ramp = WaterDepthTint.rampAtVertex(x, z);
			color = WaterDepthTint.darkenColor(color, ramp);
		}
		original.call(self, builder, x, y, z, color, u, v, light);
	}
	//?} elif forge {
	/*@WrapOperation(
		method = "tesselate",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFFI)V", remap = false)
	)
	private void bathymetry$deepenVertex(
		LiquidBlockRenderer self, VertexConsumer builder,
		double x, double y, double z, float r, float g, float b, float alpha, float u, float v, int light,
		Operation<Void> original
	) {
		if (WaterDepthTint.active()) {
			float ramp = WaterDepthTint.rampAtVertex((float) x, (float) z);
			r *= WaterDepthTint.redFactor(ramp);
			g *= WaterDepthTint.greenFactor(ramp);
			b *= WaterDepthTint.blueFactor(ramp);
		}
		original.call(self, builder, x, y, z, r, g, b, alpha, u, v, light);
	}
	*///?} elif neoforge {
	/*@WrapOperation(
		method = "tesselate",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFFFFFFFI)V")
	)
	private void bathymetry$deepenVertex(
		LiquidBlockRenderer self, VertexConsumer builder,
		float x, float y, float z, float r, float g, float b, float alpha, float u, float v, int light,
		Operation<Void> original
	) {
		if (WaterDepthTint.active()) {
			float ramp = WaterDepthTint.rampAtVertex(x, z);
			r *= WaterDepthTint.redFactor(ramp);
			g *= WaterDepthTint.greenFactor(ramp);
			b *= WaterDepthTint.blueFactor(ramp);
		}
		original.call(self, builder, x, y, z, r, g, b, alpha, u, v, light);
	}
	*///?} elif >=1.21 {
	/*@WrapOperation(
		method = "tesselate",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFFFFFFI)V")
	)
	private void bathymetry$deepenVertex(
		LiquidBlockRenderer self, VertexConsumer builder,
		float x, float y, float z, float r, float g, float b, float u, float v, int light,
		Operation<Void> original
	) {
		if (WaterDepthTint.active()) {
			float ramp = WaterDepthTint.rampAtVertex(x, z);
			r *= WaterDepthTint.redFactor(ramp);
			g *= WaterDepthTint.greenFactor(ramp);
			b *= WaterDepthTint.blueFactor(ramp);
		}
		original.call(self, builder, x, y, z, r, g, b, u, v, light);
	}
	*///?} else {
	/*@WrapOperation(
		method = "tesselate",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFI)V")
	)
	private void bathymetry$deepenVertex(
		LiquidBlockRenderer self, VertexConsumer builder,
		double x, double y, double z, float r, float g, float b, float u, float v, int light,
		Operation<Void> original
	) {
		if (WaterDepthTint.active()) {
			float ramp = WaterDepthTint.rampAtVertex((float) x, (float) z);
			r *= WaterDepthTint.redFactor(ramp);
			g *= WaterDepthTint.greenFactor(ramp);
			b *= WaterDepthTint.blueFactor(ramp);
		}
		original.call(self, builder, x, y, z, r, g, b, u, v, light);
	}
	*///?}
}
