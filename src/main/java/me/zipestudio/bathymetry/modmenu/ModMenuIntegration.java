package me.zipestudio.bathymetry.modmenu;

import me.zipestudio.bathymetry.yacl.YACLConfigurationScreen;
import me.zipestudio.bathymetry.Bathymetry;
import net.lopymine.mossylib.modmenu.AbstractModMenuIntegration;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuIntegration extends AbstractModMenuIntegration {

	@Override
	protected String getModId() {
		return Bathymetry.MOD_ID;
	}

	@Override
	protected Screen createConfigScreen(Screen screen) {
		return YACLConfigurationScreen.createScreen(screen);
	}
}
