package de.maxhenkel.corelib.death;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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

            death.processDrops(event.getDrops());

            PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(death, player, event.getSource());
            MinecraftForge.EVENT_BUS.post(playerDeathEvent);

            if (playerDeathEvent.isStoreDeath()) {
                DeathManager.addDeath(player, death);
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
