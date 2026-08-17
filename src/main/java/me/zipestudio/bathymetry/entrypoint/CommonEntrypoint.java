package me.zipestudio.bathymetry.entrypoint;

//? if fabric {

/*import me.zipestudio.bathymetry.Bathymetry;

import net.fabricmc.api.ModInitializer;

public class CommonEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		Bathymetry.onInitialize();
	}
}

*///?} elif neoforge {
/*import me.zipestudio.bathymetry.Bathymetry;

import net.neoforged.fml.common.Mod;

@Mod(Bathymetry.MOD_ID)
public class CommonEntrypoint {

	public CommonEntrypoint() {
		Bathymetry.onInitialize();
	}

}

*///?} elif forge {
import me.zipestudio.bathymetry.Bathymetry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(Bathymetry.MOD_ID)
public class CommonEntrypoint {

	public CommonEntrypoint() {
		Bathymetry.onInitialize();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientEntrypoint::onInitializeClient);
	}

}

//?}

