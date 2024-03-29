package com.thatmg393.tpa4fabric.tpa;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

import java.util.HashMap;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class TPAManager {
    private static final TPAManager INSTANCE = new TPAManager();

    public static TPAManager getInstance() {
        return INSTANCE;
    }

    private TPAManager() {
        ServerPlayConnectionEvents.JOIN.register((netHandler, packetSender, server) -> {
            ServerPlayerEntity whoJoined = netHandler.getPlayer();
            if (!players.containsKey(whoJoined.getUuidAsString())) {
                players.put(
                    whoJoined.getUuidAsString(),
                    new Player(whoJoined)
                );
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register((netHandler, server) -> {
            ServerPlayerEntity whoLeft = netHandler.getPlayer();
            if (players.containsKey(whoLeft.getUuidAsString())) {
                players.remove(whoLeft.getUuidAsString());
            }
        });
    }

    private HashMap<String, Player> players = new HashMap<>();

    public int newTPA(
        ServerCommandSource context,
        ServerPlayerEntity to
    ) {
        var me = context.getPlayer();

        if (me.equals(to)) {
            me.sendMessage(fromLang("tpa4fabric.tpa2self"));
            return 1;
        }

        if (!players.containsKey(to.getUuidAsString())) {
            return 1;
        }

        if (players.get(me.getUuidAsString()).inCooldown()) {
            me.sendMessage(fromLang("tpa4fabric.cooldown"));
            return 1;
        }

        Player target = players.get(to.getUuidAsString());

        if (!target.getAllowedTPARequest()) {
            me.sendMessage(fromLang("tpa4fabric.playerTpaOff", target.getRealPlayer().getName().getString()));
            return 1;
        }

        target.newTPARequest(me);
        target.markInCooldown();

        me.sendMessage(fromLang("tpa4fabric.sentTpaReq", to.getName().getString()));
        target.sendChatMessage(fromLang("tpa4fabric.recvTpaReq", me.getName().getString()));
        
        return 0;
    }

    public int acceptTPA(
        ServerCommandSource context,
        ServerPlayerEntity from
    ) {
        ServerPlayerEntity me = context.getPlayer();
        Player target = players.get(me.getUuidAsString());

        if (target.isTPARequestsEmpty()) {
            me.sendMessage(fromLang("tpa4fabric.tpaListEmpty"));
            return 1;
        } else {
            TPARequest request = target.getTPARequest(from);
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
        Player target = players.get(me.getUuidAsString());

        if (target.isTPARequestsEmpty()) {
            me.sendMessage(fromLang("tpa4fabric.tpaListEmpty"));
            return 1;
        } else {
            TPARequest request = target.getTPARequest(from);
            if (request == null)
                return 1;

            request.deny(me);
        }

        return 0;
    }

    public int allowTPA(
        ServerCommandSource context,
        boolean allow
    ) {
        Player me = players.get(context.getPlayer().getUuidAsString());
        me.setAllowedTPARequest(allow);
        me.sendChatMessage(fromLang("tpa4fabric.changeTpaAllowMsg", allow));
        
        return 1;
    }
}
