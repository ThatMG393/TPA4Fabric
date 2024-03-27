package com.thatmg393.tpa4fabric.manager;

import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import net.fabricmc.fabric.mixin.biome.modification.MinecraftServerMixin;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TPAManager {
    private static final TPAManager INSTANCE = new TPAManager();

    public static TPAManager getInstance() {
        return INSTANCE;
    }

    private TPAManager() { }

    private HashMap<String, Long> playersOnCooldown = new HashMap<>();
    private HashMap<String, HashMap<String, TPARequest>> tpaRequests = new HashMap<>();

    public int newTPA(
        ServerCommandSource context,
        ServerPlayerEntity to
    ) {
        var from = context.getPlayer();
        
        if (from.equals(to)) {
            from.sendMessage(Text.literal("You cannot teleport to yourself! Are you stupid?"));
            return 1;
        }

        if (isPlayerOnCooldown(from.getUuidAsString())) {
            from.sendMessage(Text.literal("You are in cooldown! Chillax."));
            return 1;
        }

        if (!tpaRequests.containsKey(to.getUuidAsString())) {
            tpaRequests.put(to.getUuidAsString(), new HashMap<>());
        }

        if (tpaRequests.get(to.getUuidAsString()).containsKey(from.getUuidAsString())) {
            from.sendMessage(Text.literal("You already have a teleport request to that player!"));
            return 1;
        }

        var requests = tpaRequests.get(to.getUuidAsString());
        requests.put(
            from.getUuidAsString(),
            new TPARequest(
                requests,
                from,
                new Timer()
            )
        );

        return 0;
    }

    public int acceptTPA(
        ServerCommandSource context,
        ServerPlayerEntity from
    ) {
        ServerPlayerEntity me = context.getPlayer();
        if (!tpaRequests.containsKey(me.getUuidAsString())) {
            return 1;
        } else {
            if (tpaRequests.get(me.getUuidAsString()).size() == 0) {
                return 1;
            }

            var requests = tpaRequests.get(me.getUuidAsString());
            if (requests.size() == 0) {
                me.sendMessage(Text.of("You have no teleport request!"));
                return 1;
            } if (!requests.containsKey(from.getUuidAsString())) {
                me.sendMessage(Text.of("You have no teleport request from " + from.getName().getString()));
                return 1;
            }

            var request = requests.get(from.getUuidAsString());
            requests.remove(from.getUuidAsString());

            from.setVelocity(0, 0, 0);
            from.setInvulnerable(true);
            from.sendMessage(Text.of("teleported to " + me.getName().getString()));
            
            from.teleport(
                me.getServerWorld(),
                me.getX(),
                me.getY(),
                me.getZ(),
                from.getYaw(),
                from.getPitch()
            );

            from.setInvulnerable(false);
            request.consumed();

            markPlayerOnCooldown(me.getUuidAsString());
        }

        return 0;
    }

    public boolean isPlayerOnCooldown(String uuid) {
        if (playersOnCooldown.containsKey(uuid)) {
            long delta = Instant.now().getEpochSecond() - playersOnCooldown.get(uuid);
            if (delta <= 5) return true;
            else if (delta >= 5) {
                playersOnCooldown.remove(uuid);
                return false;
            }
        }
        return false;
    }

    private void markPlayerOnCooldown(String uuid) {
        if (!playersOnCooldown.containsKey(uuid)) {
            playersOnCooldown.put(uuid, Instant.now().getEpochSecond());
        }
    }

    public record TPARequest(HashMap<?, ?> container, ServerPlayerEntity from, Timer scheduler) {
        public TPARequest {
            scheduler.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (container != null) container.remove(this);
                    from.sendMessage(Text.of("Your TPA request expired."));
                }
            }, 5);
        }

        public void consumed() {
            scheduler.cancel();
            if (container != null) container.remove(this);
        }
    }
}
