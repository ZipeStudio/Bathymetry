package me.zipestudio.bathymetry.compat.dh;

import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiBlockStateWrapperCreatedEvent;
import com.seibel.distanthorizons.api.methods.events.sharedParameterObjects.DhApiEventParam;

public class DhWaterStateListener extends DhApiBlockStateWrapperCreatedEvent {

	private static final String WATER_SERIAL_PREFIX = "minecraft:water";

	@Override
	public void blockStateWrapperCreated(DhApiEventParam<EventParam> event) {
		EventParam param = event.value;
		if (param == null || param.getBlockStateWrapper() == null) {
			return;
		}

		String serial = param.getBlockStateWrapper().getSerialString();
		if (serial != null && serial.startsWith(WATER_SERIAL_PREFIX)) {
			param.setAllowApiColorOverride(true);
		}
	}

}
