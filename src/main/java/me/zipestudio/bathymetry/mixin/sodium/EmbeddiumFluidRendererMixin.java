package me.zipestudio.bathymetry.mixin.sodium;

//? if neoforge && >=1.21 && <1.21.2 {
/*import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.FluidState;
import org.embeddedt.embeddium.impl.model.color.ColorProvider;
import org.embeddedt.embeddium.impl.model.light.LightPipeline;
import org.embeddedt.embeddium.impl.model.quad.ModelQuadView;
import org.embeddedt.embeddium.impl.render.chunk.compile.pipeline.FluidRenderer;
import org.embeddedt.embeddium.impl.world.WorldSlice;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.zipestudio.bathymetry.water.WaterDepthTint;

@Mixin(FluidRenderer.class)
public class EmbeddiumFluidRendererMixin {

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
*///?} else {
public class EmbeddiumFluidRendererMixin {
}
//?}
