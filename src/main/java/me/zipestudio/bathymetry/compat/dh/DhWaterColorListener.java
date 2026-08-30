package me.zipestudio.bathymetry.compat.dh;

import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiBlockColorOverrideEvent;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiEventParam;
import com.seibel.distanthorizons.api.objects.data.DhApiTerrainDataPoint;
import com.seibel.distanthorizons.api.objects.data.IDhApiFullDataSource;
import com.seibel.distanthorizons.api.interfaces.world.IDhApiLevelWrapper;

import me.zipestudio.bathymetry.config.LeafyConfig;
import me.zipestudio.bathymetry.water.WaterDepthTint;

import java.util.List;

public class DhWaterColorListener extends DhApiBlockColorOverrideEvent {

	@Override
	public void onBlockColorOverridden(DhApiEventParam<EventParam> event) {
		EventParam param = event.value;
		if (param == null) {
			return;
		}

		LeafyConfig config = LeafyConfig.getInstance();
		int shallowTint = config.getShallowTint();
		int deepTint = config.getDeepTint();
		float intensity = config.getIntensity();
		if (!config.isModEnabled() || intensity <= 0.0F || (shallowTint == 0xFFFFFF && deepTint == 0xFFFFFF)) {
			return;
		}

		int thickness = columnThickness(param);
		if (thickness <= 0) {
			return;
		}

		float shallowDepth = config.getShallowDepth();
		float deepDepth = Math.max(config.getDeepDepth(), shallowDepth + 1.0F);
		float ramp = WaterDepthTint.rampFromThickness(thickness, shallowDepth, deepDepth);

		int red = scaleChannel(param.getRed(), ramp, intensity, shallowTint, deepTint, WaterDepthTint.RED_SHIFT);
		int green = scaleChannel(param.getGreen(), ramp, intensity, shallowTint, deepTint, WaterDepthTint.GREEN_SHIFT);
		int blue = scaleChannel(param.getBlue(), ramp, intensity, shallowTint, deepTint, WaterDepthTint.BLUE_SHIFT);
		param.setColor(red, green, blue);
	}

	private static int scaleChannel(int value, float ramp, float intensity, int shallowTint, int deepTint, int shift) {
		float factor = WaterDepthTint.channelFactor(
			ramp,
			intensity,
			WaterDepthTint.tintChannel(shallowTint, shift),
			WaterDepthTint.tintChannel(deepTint, shift)
		);

		int scaled = Math.round(value * factor);
		return scaled < 0 ? 0 : Math.min(scaled, 255);
	}

	private static int columnThickness(EventParam param) {
		IDhApiFullDataSource dataSource = param.getDataSource();
		IDhApiLevelWrapper levelWrapper = param.getLevelWrapper();
		if (dataSource == null || levelWrapper == null) {
			return 0;
		}

		int width = dataSource.getWidthInDataColumns();
		if (width <= 0) {
			return 0;
		}

		List<DhApiTerrainDataPoint> column;
		try {
			column = dataSource.getApiDataPointColumn(
				Math.floorMod(param.getBlockPosX(), width),
				Math.floorMod(param.getBlockPosZ(), width)
			);
		} catch (IndexOutOfBoundsException ignored) {
			return 0;
		}

		if (column == null) {
			return 0;
		}

		int relativeY = param.getBlockPosY() - levelWrapper.getMinHeight();
		for (DhApiTerrainDataPoint point : column) {
			if (point == null || relativeY < point.bottomYBlockPos || relativeY >= point.topYBlockPos) {
				continue;
			}
			return point.topYBlockPos - point.bottomYBlockPos;
		}

		return 0;
	}

}
