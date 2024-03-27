package com.thatmg393.tpa4fabric.manager;

import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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
            from.sendMessage(Text.of("You cannot teleport to yourself! Are you stupid?"));
            return 1;
        }

        if (isPlayerOnCooldown(from.getUuidAsString())) {
            from.sendMessage(Text.of("You are in cooldown! Chillax."));
            return 1;
        }

        if (!tpaRequests.containsKey(to.getUuidAsString())) {
            tpaRequests.put(to.getUuidAsString(), new HashMap<>());
        }

        if (tpaRequests.get(to.getUuidAsString()).containsKey(from.getUuidAsString())) {
            from.sendMessage(Text.of("You already have a teleport request to that player!"));
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
            me.sendMessage(Text.of("You have no TPA request!"));
            return 1;
        } else {
            var requests = tpaRequests.get(me.getUuidAsString());
            if (requests.size() == 0) {
                me.sendMessage(Text.of("You have no TPA request!"));
                return 1;
            }
            
            if (from != null && !requests.containsKey(from.getUuidAsString())) {
                me.sendMessage(Text.of("You have no TPA request from " + from.getName().getString()));
                return 1;
            }

            TPARequest request = null;
            if (from != null) {
                request = requests.remove(from.getUuidAsString());
            } else {
                if (requests.size() > 1) {
                    me.sendMessage(Text.of("You have multiple TPA reqeusts!"));
                    return 1;
                }
                request = requests.remove(requests.keySet().toArray()[0]);
            }

            from.setVelocity(0, 0, 0);
            from.setInvulnerable(true);
            from.sendMessage(Text.of("You have been teleported to " + me.getName().getString()));
            
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

    public int denyTPA(
        ServerCommandSource context,
        ServerPlayerEntity from
    ) {
        ServerPlayerEntity me = context.getPlayer();
        if (!tpaRequests.containsKey(me.getUuidAsString())
         || tpaRequests.get(me.getUuidAsString()).size() == 0
        ) {
            me.sendMessage(Text.of("You have no TPA request received."));
            return 1;
        } else {
            var requests = tpaRequests.get(me.getUuidAsString());
            
            TPARequest request = null;
            if (from != null && requests.containsKey(from.getUuidAsString())) {
                request = requests.remove(from.getUuidAsString());
            } else if (from == null) {
                if (requests.size() > 1) {
                    me.sendMessage(Text.of("You have multiple TPA requests!"));
                    return 1;
                }
                request = requests.remove(requests.keySet().toArray()[0]);
            } else {
                me.sendMessage(Text.of("You have no TPA request from " + from.getName().getString()));
                return 1;
            }

            request.deny(me.getName().getString());
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
            }, 30 * 1000);
        }

        public void consumed() {
            scheduler.cancel();
            if (container != null && container.containsValue(this)) container.remove(this);
        }

        public void accept(String name) {
            // no-op
            // implement later
        }

        public void deny(String name) {
            consumed();
            from.sendMessage(Text.of(name + " denied your TPA request."));
        }
    }
}
