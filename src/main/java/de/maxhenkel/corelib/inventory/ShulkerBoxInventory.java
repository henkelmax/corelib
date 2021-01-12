package de.maxhenkel.corelib.inventory;

import de.maxhenkel.corelib.sound.SoundUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;

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
        this.items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        openInventory(player);

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
            LootTable loottable = player.world.getServer().getLootTableManager().getLootTableFromLocation(lootTable);
            lootTable = null;

            LootContext.Builder builder = new LootContext.Builder((ServerWorld) player.world);

            if (lootTableSeed != 0L) {
                builder.withSeed(lootTableSeed);
            }

            builder.withLuck(player.getLuck()).withParameter(LootParameters.THIS_ENTITY, player);

            loottable.fillInventory(this, builder.build(LootParameterSets.CHEST));
            markDirty();
        }
    }

    @Override
    public int getSizeInventory() {
        return invSize;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(items, index, count);
        markDirty();
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = ItemStackHelper.getAndRemove(items, index);
        markDirty();
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        items.set(index, stack);
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        CompoundNBT tag = shulkerBox.getOrCreateTag();
        if (blockEntityTag == null) {
            tag.put("BlockEntityTag", blockEntityTag = new CompoundNBT());
        } else {
            tag.put("BlockEntityTag", blockEntityTag);
        }

        ItemStackHelper.saveAllItems(blockEntityTag, items, true);
    }

    @Override
    public void openInventory(PlayerEntity player) {
        player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), getOpenSound(), SoundCategory.BLOCKS, 0.5F, SoundUtils.getVariatedPitch(player.world));
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        markDirty();
        player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), getCloseSound(), SoundCategory.BLOCKS, 0.5F, player.world.rand.nextFloat() * 0.1F + 0.9F);
    }

    protected SoundEvent getOpenSound() {
        return SoundEvents.BLOCK_SHULKER_BOX_OPEN;
    }

    protected SoundEvent getCloseSound() {
        return SoundEvents.BLOCK_SHULKER_BOX_CLOSE;
    }

    @Override
    public void clear() {
        items.clear();
        markDirty();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            if (player.getHeldItem(hand).equals(shulkerBox)) {
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