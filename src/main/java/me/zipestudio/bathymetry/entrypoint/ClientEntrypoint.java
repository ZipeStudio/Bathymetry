package me.zipestudio.bathymetry.entrypoint;

//? if fabric {

/*import net.fabricmc.api.ClientModInitializer;

import me.zipestudio.bathymetry.client.BathymetryClient;

public class ClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BathymetryClient.onInitializeClient();
	}

}

*///?} elif neoforge {
/*import me.zipestudio.bathymetry.Bathymetry;

import me.zipestudio.bathymetry.client.BathymetryClient;
import me.zipestudio.bathymetry.modmenu.ModMenuIntegration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = Bathymetry.MOD_ID, dist = Dist.CLIENT)
public class ClientEntrypoint {

	public ClientEntrypoint(ModContainer container) {
		BathymetryClient.onInitializeClient();
		ModMenuIntegration integration = new ModMenuIntegration();
		integration.register(container);
	}

}

*///?} elif forge {

import me.zipestudio.bathymetry.client.BathymetryClient;
import me.zipestudio.bathymetry.modmenu.ModMenuIntegration;
import net.minecraftforge.fml.ModLoadingContext;

public class ClientEntrypoint {

	public static void onInitializeClient() {
		BathymetryClient.onInitializeClient();
		ModMenuIntegration integration = new ModMenuIntegration();
		integration.register(ModLoadingContext.get().getActiveContainer());
	}

}

//?}
