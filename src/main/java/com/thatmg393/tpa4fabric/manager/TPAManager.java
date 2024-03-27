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
        var me = context.getPlayer();
        
        if (me.equals(to)) {
            me.sendMessage(Text.of("You cannot teleport to yourself! Are you stupid?"));
            return 1;
        }

        if (isPlayerOnCooldown(me.getUuidAsString())) {
            me.sendMessage(Text.of("You are in cooldown! Chillax."));
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
            me.getUuidAsString(),
            new TPARequest(
                requests,
                me,
                new Timer()
            )
        );

		markPlayerOnCooldown(me.getUuidAsString());

        return 0;
    }

    public int acceptTPA(
        ServerCommandSource context,
        ServerPlayerEntity from
    ) {
        ServerPlayerEntity me = context.getPlayer();
		if (!doesPlayerHaveTPARequest(me.getUuidAsString()))
            me.sendMessage(Text.of("You have no TPA request received."));
            return 1;
        } else {
			TPARequest request = getTPARequest(me, from);
			if (request == null) return 1:

			request.accept(me);
        }

        return 0;
    }

    public int denyTPA(
        ServerCommandSource context,
        ServerPlayerEntity from
    ) {
        ServerPlayerEntity me = context.getPlayer();
        if (!doesPlayerHaveTPARequest(me.getUuidAsString())) {
            me.sendMessage(Text.of("You have no TPA request received."));
            return 1;
        } else {
            TPARequest request = getTPARequest(me, from);
			if (request == null) return 1;

            request.deny(me.getName().getString());
        }

        return 0;
    }

	public TPARequest getTPARequest(ServerPlayerEntity me, ServerPlayerEntity from) {
		var requests = tpaRequests.get(me.getUuidAsString());
        TPARequest r = null;

		if (from != null) {
			if (requests.containsKey(from.getUuidAsString())) {
				r = requests.remove(from.getUuidAsString());
			} else {
				me.sendMessage(Text.of("You have no TPA request from " + from.getName().getString()));
				return null;
			}
		} else {
			if (requests.size() > 1) {
				me.sendMessage(Text.of("You have multiple TPA requests!"));
				return null;
			}
			r = requests.remove(requests.keySet().toArray()[0]);
		}

		r.consumed();
		return r;
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

	private boolean doesPlayerHaveTPARequest(String uuid) {
		if (tpaRequests.containsKey(uuid))
			return tpaRequests.get(uuid).size() > 1;
		return false;
	}

    public record TPARequest(HashMap<?, ?> container, ServerPlayerEntity from, Timer scheduler) {
        public TPARequest {
            scheduler.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (container != null) container.remove(this);
                    from.sendMessage(Text.of("Your TPA request expired."));
                }
            }, 120 * 1000 /* 2 minutes */ );
        }

        public void accept(ServerPlayerEntity whoAccepted) {
			consumed();

			from.sendMessage(whoAccepted.getName().getString() + " accepted your TPA request.");
			from.sendMessage("Teleporting...");

			from.setVelocity(0, 0, 0);
			from.setInvulnerable(true);
			from.teleport(
				whoAccepted.getServerWorld(),
				whoAccepted.getX(),
	            whoAccepted.getY(),
				whoAccepted.getZ(),
				from.getYaw(),
				from.getPitch()
			);
			from.setInvulnerable(false);

			from.sendMessage("Teleported!");
        }

        public void deny(String name) {
            consumed();
            from.sendMessage(Text.of(name + " denied your TPA request."));
        }

		 public void consumed() {
            scheduler.cancel();
        }
    }
}
