package de.maxhenkel.corelib.codec;

import com.mojang.serialization.MapCodec;
import de.maxhenkel.corelib.Logger;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Optional;

public class ValueInputOutputUtils {

    public static Optional<CompoundTag> getTag(ValueInput valueInput, String key) {
        return valueInput.read(key, CompoundTag.CODEC);
    }

    public static void setTag(ValueOutput valueOutput, String key, CompoundTag tag) {
        valueOutput.store(key, CompoundTag.CODEC, tag);
    }

    public static CompoundTag getTag(ValueInput valueInput) {
        return valueInput.read(MapCodec.assumeMapUnsafe(CompoundTag.CODEC)).orElseThrow();
    }

    public static TagValueOutput createValueOutput(BlockEntity blockEntity, HolderLookup.Provider provider) {
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), Logger.INSTANCE)) {
            return TagValueOutput.createWithContext(scopedCollector, provider);
        }
    }

    public static TagValueOutput createValueOutput(Entity entity, HolderLookup.Provider provider) {
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(entity.problemPath(), Logger.INSTANCE)) {
            return TagValueOutput.createWithContext(scopedCollector, provider);
        }
    }

    public static TagValueInput createValueInput(BlockEntity blockEntity, HolderLookup.Provider provider, CompoundTag tag) {
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), Logger.INSTANCE)) {
            return (TagValueInput) TagValueInput.create(scopedCollector, provider, tag);
        }
    }

    public static TagValueInput createValueInput(Entity entity, HolderLookup.Provider provider, CompoundTag tag) {
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(entity.problemPath(), Logger.INSTANCE)) {
            return (TagValueInput) TagValueInput.create(scopedCollector, provider, tag);
        }
    }

    public static TagValueInput createValueInput(String problemPath, HolderLookup.Provider provider, CompoundTag tag) {
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(createPath(problemPath), Logger.INSTANCE)) {
            return (TagValueInput) TagValueInput.create(scopedCollector, provider, tag);
        }
    }

    public static ProblemReporter.PathElement createPath(String name) {
        return new ProblemReporter.RootFieldPathElement(name);
    }

    public static CompoundTag toTag(TagValueOutput valueOutput) {
        return valueOutput.buildResult();
    }

}
