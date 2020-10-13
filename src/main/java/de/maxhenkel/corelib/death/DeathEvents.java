package de.maxhenkel.corelib.death;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class DeathEvents {

    private Map<ServerPlayerEntity, Death> deathMap = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    void playerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        deathMap.put(player, Death.fromPlayer(player));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    void playerDeath(LivingDropsEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ServerPlayerEntity)) {
            return;
        }
        try {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
            Death death = deathMap.remove(player);

            if (death == null) {
                death = Death.fromPlayer(player);
            }
            Collection<ItemEntity> drops = event.getDrops();
            death.processDrops(new ArrayList<>(drops));

            PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(death, player, event.getSource());
            MinecraftForge.EVENT_BUS.post(playerDeathEvent);

            if (playerDeathEvent.isStoreDeath()) {
                DeathManager.addDeath(player, death);
            }
            if (playerDeathEvent.isRemoveDrops()) {
                drops.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers the events in order to emit the {@link PlayerDeathEvent}
     * NOTE: This removes drops from getting spawned
     */
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new DeathEvents());
    }

}
