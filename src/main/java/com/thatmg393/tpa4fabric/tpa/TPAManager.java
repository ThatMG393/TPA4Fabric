package com.thatmg393.tpa4fabric.tpa;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;

import org.slf4j.helpers.MessageFormatter;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TPAManager {
    private static final TPAManager INSTANCE = new TPAManager();

    public static TPAManager getInstance() {
        return INSTANCE;
    }

    private TPAManager() { }

    // TODO: Combine these two HashMaps to reduce memory usage (2 hashmap = x2 memory usage)
    private HashMap<String, Long> playersOnCooldown = new HashMap<>();
    private HashMap<String, HashMap<String, TPARequest>> tpaRequests = new HashMap<>();
    
    public int newTPA(
        ServerCommandSource context,
        ServerPlayerEntity to
    ) {
        var me = context.getPlayer();

        if (me.equals(to)) {
            me.sendMessage(fromLang("tpa4fabric.tpa2self"));
            return 1;
        }

        if (isPlayerOnCooldown(me.getUuidAsString())) {
            me.sendMessage(fromLang("tpa4fabric.cooldown"));
            return 1;
        }

        if (!tpaRequests.containsKey(to.getUuidAsString())) {
            tpaRequests.put(to.getUuidAsString(), new HashMap<>());
        }

        if (tpaRequests.get(to.getUuidAsString()).containsKey(me.getUuidAsString())) {
            me.sendMessage(fromLang("tpa4fabric.existingTpaReq", to.getName().getString()));
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

        me.sendMessage(fromLang("tpa4fabric.sentTpaReq", me.getName().getString()));
        to.sendMessage(fromLang("tpa4fabric.recvTpaReq", me.getName().getString()));

        return 0;
    }

    public int acceptTPA(
        ServerCommandSource context,
        ServerPlayerEntity from
    ) {
        ServerPlayerEntity me = context.getPlayer();
        if (!doesPlayerHaveTPARequest(me.getUuidAsString())) {
            me.sendMessage(fromLang("tpa4fabric.tpaListEmpty"));
            return 1;
        } else {
            TPARequest request = getTPARequest(me, from);
            if (request == null)
                return 1;

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
            me.sendMessage(fromLang("tpa4fabric.tpaListEmpty"));
            return 1;
        } else {
            TPARequest request = getTPARequest(me, from);
            if (request == null)
                return 1;

            request.deny(me);
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
                me.sendMessage(fromLang("tpa4fabric.noTpaReqFrom", from.getName().getString()));
                return null;
            }
        } else {
            if (requests.size() > 1) {
                me.sendMessage(fromLang("tpa4fabric.multiTpaReq"));
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
            if (delta <= 10)
                return true;
            else if (delta >= 10) {
                playersOnCooldown.remove(uuid);
                return false;
            }
        }
        return false;
    }

    private void markPlayerOnCooldown(String uuid) {
        playersOnCooldown.put(uuid, Instant.now().getEpochSecond());
    }

    private boolean doesPlayerHaveTPARequest(String uuid) {
        if (tpaRequests.containsKey(uuid))
            return !tpaRequests.get(uuid).isEmpty();
        return false;
    }

    public static Text buildText(String id, Object... format) {
        String m = Text.translatable(id).getString();
        return Text.of(MessageFormatter.format(m, format).getMessage());
    }
}
