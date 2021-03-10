package de.maxhenkel.corelib.inventory;

import de.maxhenkel.corelib.sound.SoundUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public abstract class ShulkerBoxInventory implements IInventory, INamedContainerProvider {

    protected NonNullList<ItemStack> items;
    protected ItemStack shulkerBox;
    protected int invSize;
    protected CompoundNBT blockEntityTag;

    protected ResourceLocation lootTable;
    protected long lootTableSeed;

    public ShulkerBoxInventory(PlayerEntity player, ItemStack shulkerBox, int invSize) {
        this.shulkerBox = shulkerBox;
        this.invSize = invSize;
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        startOpen(player);

        CompoundNBT c = shulkerBox.getTag();

        if (c == null) {
            return;
        }

        if (!c.contains("BlockEntityTag")) {
            return;
        }

        blockEntityTag = c.getCompound("BlockEntityTag");

        if (blockEntityTag.contains("Items")) {
            ItemStackHelper.loadAllItems(blockEntityTag, items);
        } else if (blockEntityTag.contains("LootTable")) {
            lootTable = new ResourceLocation(blockEntityTag.getString("LootTable"));
            lootTableSeed = blockEntityTag.getLong("LootTableSeed");
            fillWithLoot(player);
            blockEntityTag.remove("LootTable");
            blockEntityTag.remove("LootTableSeed");
        }
    }

    public ShulkerBoxInventory(PlayerEntity player, ItemStack shulkerBox) {
        this(player, shulkerBox, 27);
    }

    public void fillWithLoot(@Nullable PlayerEntity player) {
        if (lootTable != null && player != null) {
            LootTable loottable = player.level.getServer().getLootTables().get(lootTable);
            lootTable = null;

            LootContext.Builder builder = new LootContext.Builder((ServerWorld) player.level);

            if (lootTableSeed != 0L) {
                builder.withOptionalRandomSeed(lootTableSeed);
            }

            builder.withLuck(player.getLuck()).withParameter(LootParameters.THIS_ENTITY, player);

            loottable.fill(this, builder.create(LootParameterSets.CHEST));
            setChanged();
        }
    }

    @Override
    public int getContainerSize() {
        return invSize;
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack itemstack = ItemStackHelper.removeItem(items, index, count);
        setChanged();
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = ItemStackHelper.takeItem(items, index);
        setChanged();
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
        setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setChanged() {
        CompoundNBT tag = shulkerBox.getOrCreateTag();
        if (blockEntityTag == null) {
            tag.put("BlockEntityTag", blockEntityTag = new CompoundNBT());
        } else {
            tag.put("BlockEntityTag", blockEntityTag);
        }

        ItemStackHelper.saveAllItems(blockEntityTag, items, true);
    }

    @Override
    public void startOpen(PlayerEntity player) {
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), getOpenSound(), SoundCategory.BLOCKS, 0.5F, SoundUtils.getVariatedPitch(player.level));
    }

    @Override
    public void stopOpen(PlayerEntity player) {
        setChanged();
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), getCloseSound(), SoundCategory.BLOCKS, 0.5F, player.level.random.nextFloat() * 0.1F + 0.9F);
    }

    protected SoundEvent getOpenSound() {
        return SoundEvents.SHULKER_BOX_OPEN;
    }

    protected SoundEvent getCloseSound() {
        return SoundEvents.SHULKER_BOX_CLOSE;
    }

    @Override
    public void clearContent() {
        items.clear();
        setChanged();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            if (player.getItemInHand(hand).equals(shulkerBox)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return shulkerBox.getDisplayName();
    }

    @Nullable
    @Override
    public abstract Container createMenu(int id, PlayerInventory inventory, PlayerEntity player);

}