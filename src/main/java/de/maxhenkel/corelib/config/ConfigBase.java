package de.maxhenkel.corelib.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigBase {

    protected ForgeConfigSpec configSpec;

    public ConfigBase(ForgeConfigSpec.Builder builder) {

    }

    public void setConfigSpec(ForgeConfigSpec configSpec) {
        this.configSpec = configSpec;
    }

    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }
}
