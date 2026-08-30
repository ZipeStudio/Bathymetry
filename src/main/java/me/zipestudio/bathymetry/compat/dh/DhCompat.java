package me.zipestudio.bathymetry.compat.dh;

import me.zipestudio.bathymetry.client.BathymetryClient;

public final class DhCompat {

	private static final String DH_API_CLASS = "com.seibel.distanthorizons.api.DhApi";

	private static boolean registered;

	public static void register() {
		if (registered) {
			return;
		}

		try {
			Class.forName(DH_API_CLASS);
		} catch (Throwable ignored) {
			return;
		}

		try {
			DhTintBinder.bind();
			registered = true;
			BathymetryClient.LOGGER.info("Distant Horizons detected, water tint bound to its LOD renderer");
		} catch (Throwable throwable) {
			BathymetryClient.LOGGER.error("Failed to bind Distant Horizons water tint", throwable);
		}
	}

	public static void rebuildLods() {
		if (!registered) {
			return;
		}

		try {
			DhTintBinder.clearRenderCache();
		} catch (Throwable throwable) {
			BathymetryClient.LOGGER.error("Failed to clear Distant Horizons render cache", throwable);
		}
	}

}
