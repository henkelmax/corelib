package de.maxhenkel.corelib.death;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event is emitted when a player dies
 * <p>
 * NOTE: This event is server side only!
 */
public class PlayerDeathEvent extends Event {

    private Death death;
    private ServerPlayerEntity player;
    private DamageSource source;

    private boolean storeDeath;
    private boolean removeDrops;

    public PlayerDeathEvent(Death death, ServerPlayerEntity player, DamageSource source) {
        this.death = death;
        this.player = player;
        this.source = source;
    }

    public void storeDeath() {
        this.storeDeath = true;
    }

    public void removeDrops() {
        this.removeDrops = true;
    }

    public Death getDeath() {
        return death;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public DamageSource getSource() {
        return source;
    }

    boolean isStoreDeath() {
        return storeDeath;
    }

    boolean isRemoveDrops() {
        return removeDrops;
    }
}
