package de.maxhenkel.corelib.death;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class DeathEvents {

    private Map<ServerPlayer, Death> deathMap = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    void playerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player = (ServerPlayer) event.getEntity();
        deathMap.put(player, Death.fromPlayer(player));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    void playerDeath(LivingDropsEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }
        try {
            Death death = deathMap.remove(player);

            if (death == null) {
                death = Death.fromPlayer(player);
            }
            Collection<ItemEntity> drops = event.getDrops();
            death.processDrops(new ArrayList<>(drops));

            PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(death, player, event.getSource());
            NeoForge.EVENT_BUS.post(playerDeathEvent);

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
        NeoForge.EVENT_BUS.register(new DeathEvents());
    }

}
