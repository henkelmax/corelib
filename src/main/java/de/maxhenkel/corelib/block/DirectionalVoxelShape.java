package de.maxhenkel.corelib.block;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import java.util.EnumMap;
import java.util.Map;

public class DirectionalVoxelShape {

    private Map<Direction, VoxelShape> shapes;

    private DirectionalVoxelShape(Map<Direction, VoxelShape> shapes) {
        this.shapes = shapes;
    }

    /**
     * Gets the voxel shape for the provided direction.
     * Defaults to an empty shape.
     *
     * @param direction the direction
     * @return the voxel shape of the direction
     */
    public VoxelShape get(Direction direction) {
        return shapes.getOrDefault(direction, VoxelShapes.empty());
    }

    public static class Builder {
        private Map<Direction, VoxelShape> shapeMap;

        public Builder() {
            shapeMap = new EnumMap<>(Direction.class);
        }

        public Builder direction(Direction direction, VoxelShape... shapes) {
            shapeMap.put(direction, VoxelUtils.combine(shapes));
            return this;
        }

        public DirectionalVoxelShape build() {
            return new DirectionalVoxelShape(shapeMap);
        }
    }
}
