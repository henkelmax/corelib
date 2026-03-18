package de.maxhenkel.corelib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidUtils {

    public static FluidModel getFluidModel(Fluid fluid) {
        return getFluidModel(fluid.defaultFluidState());
    }

    public static FluidModel getFluidModel(FluidState fluid) {
        return Minecraft.getInstance()
                .getModelManager()
                .getFluidStateModelSet()
                .get(fluid);
    }

    public static int getTint(FluidState fluid) {
        FluidTintSource fluidTintSource = getFluidModel(fluid.getType()).fluidTintSource();
        if (fluidTintSource == null) {
            return 0;
        }
        return fluidTintSource.color(fluid);
    }

    public static int getTint(FluidState fluid, BlockState blockState, BlockAndTintGetter level, BlockPos pos) {
        FluidTintSource fluidTintSource = getFluidModel(fluid.getType()).fluidTintSource();
        if (fluidTintSource == null) {
            return 0;
        }
        return fluidTintSource.colorInWorld(fluid, blockState, level, pos);
    }

    public static int getTint(FluidStack fluid) {
        FluidTintSource fluidTintSource = getFluidModel(fluid.getFluid()).fluidTintSource();
        if (fluidTintSource == null) {
            return 0;
        }
        return fluidTintSource.colorAsStack(fluid);
    }

}
