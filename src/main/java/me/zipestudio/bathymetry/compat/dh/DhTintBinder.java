package me.zipestudio.bathymetry.compat.dh;

import com.seibel.distanthorizons.api.DhApi;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiBlockColorOverrideEvent;
import com.seibel.distanthorizons.api.methods.events.abstractEvents.DhApiBlockStateWrapperCreatedEvent;

public final class DhTintBinder {

	public static void bind() {
		DhApi.events.bind(DhApiBlockStateWrapperCreatedEvent.class, new DhWaterStateListener());
		DhApi.events.bind(DhApiBlockColorOverrideEvent.class, new DhWaterColorListener());
	}

	public static void clearRenderCache() {
		if (DhApi.Delayed.renderProxy != null) {
			DhApi.Delayed.renderProxy.clearRenderDataCache();
		}
	}

}
