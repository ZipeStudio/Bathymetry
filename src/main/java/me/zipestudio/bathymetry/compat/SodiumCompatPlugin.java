package me.zipestudio.bathymetry.compat;

import net.lopymine.mossylib.compat.CompatPlugin;
import net.lopymine.mossylib.loader.MossyLoader;
import org.spongepowered.asm.service.MixinService;

public class SodiumCompatPlugin extends CompatPlugin {

	private static final String[] COMPAT_MOD_IDS = {"sodium", "embeddium", "rubidium"};

	@Override
	protected String getCompatModId() {
		return COMPAT_MOD_IDS[0];
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		for (String modId : COMPAT_MOD_IDS) {
			if (MossyLoader.isModLoaded(modId, true)) {
				return targetExists(targetClassName);
			}
		}
		return false;
	}

	private static boolean targetExists(String className) {
		try {
			MixinService.getService().getBytecodeProvider().getClassNode(className);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
