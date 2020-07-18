package de.maxhenkel.corelib.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigBase {

    protected ForgeConfigSpec configSpec;

    public ConfigBase(ForgeConfigSpec.Builder builder) {

    }

    public void onReload(ModConfig.ModConfigEvent event) {

    }

    public void setConfigSpec(ForgeConfigSpec configSpec) {
        this.configSpec = configSpec;
    }

    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }
}
