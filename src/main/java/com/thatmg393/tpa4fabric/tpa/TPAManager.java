package com.thatmg393.tpa4fabric.tpa;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

import java.util.HashMap;

import com.thatmg393.tpa4fabric.TPA4Fabric;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class TPAManager {
    private static final TPAManager INSTANCE = new TPAManager();

    public static TPAManager getInstance() {
        return INSTANCE;
    }

    private TPAManager() {
        ServerPlayConnectionEvents.JOIN.register((netHandler, packetSender, server) -> {
            ServerPlayerEntity playerWhoJoined = netHandler.getPlayer();
            TPA4Fabric.LOGGER.info(playerWhoJoined.getNameForScoreboard() + " joined the server, registering in the TPA...");
            
            players.putIfAbsent(
                playerWhoJoined.getUuidAsString(),
                new TPAPlayer(playerWhoJoined)
            );
        });

        ServerPlayConnectionEvents.DISCONNECT.register((netHandler, server) -> {
            ServerPlayerEntity playerWhoLeft = netHandler.getPlayer();
            players.remove(playerWhoLeft.getUuidAsString());
        });
    }

    private HashMap<String, TPAPlayer> players = new HashMap<>();

    public int newTPA(
        ServerCommandSource context,
        ServerPlayerEntity to
    ) {
        TPAPlayer me = players.get(context.getPlayer().getUuidAsString());

        if (me.getServerPlayerEntity().equals(to)) {
            me.sendChatMessage(fromLang("tpa4fabric.tpa2self"));
            return 1;
        }

        if (me.isOnCooldown()) {
            me.sendChatMessage(fromLang("tpa4fabric.cooldown"));
            return 1;
        }

        TPAPlayer target = players.get(to.getUuidAsString());
        if (!target.allowsTPARequests()) {
            me.sendChatMessage(fromLang("tpa4fabric.playerTpaOff", target.getServerPlayerEntity().getNameForScoreboard()));
            return 1;
        }

        target.newTPARequest(me);
        target.markInCooldown();

        me.sendChatMessage(fromLang("tpa4fabric.sentTpaReq", to.getNameForScoreboard()));
        target.sendChatMessage(fromLang("tpa4fabric.recvTpaReq", me.getServerPlayerEntity().getNameForScoreboard()));
        
        return 0;
    }

    public int acceptTPA(
        ServerCommandSource context,
        ServerPlayerEntity from
    ) {
        TPAPlayer me = players.get(context.getPlayer().getUuidAsString());

        if (me.isTPARequestsEmpty()) {
            me.sendChatMessage(fromLang("tpa4fabric.tpaListEmpty"));
            return 1;
        } else {
            TPARequest request;
            if (from == null) request = me.getTPARequest(null);
            else request = me.getTPARequest(players.get(from.getUuidAsString()));
            
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
        TPAPlayer me = players.get(context.getPlayer().getUuidAsString());

        if (me.isTPARequestsEmpty()) {
            me.sendChatMessage(fromLang("tpa4fabric.tpaListEmpty"));
            return 1;
        } else {
            TPARequest request;
            if (from == null) request = me.getTPARequest(null);
            else request = me.getTPARequest(players.get(from.getUuidAsString()));
            
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
        TPAPlayer me = players.get(context.getPlayer().getUuidAsString());
        me.setAllowTPARequests(allow);
        me.sendChatMessage(fromLang("tpa4fabric.changeTpaAllowMsg", allow));
        
        return 0;
    }

    public int cancelTPA(
        ServerCommandSource context,
        ServerPlayerEntity to
    ) {
        TPAPlayer me = players.get(context.getPlayer().getUuidAsString());
        TPAPlayer to2 = players.get(to.getUuidAsString());
        TPARequest r = to2.cancelTPARequest(me.getPlayerUUID());
        if (r != null) {
            r.consumed(me);
            me.sendChatMessage(fromLang("tpa4fabric.tpaCancel", to.getNameForScoreboard()));
        } else me.sendChatMessage(fromLang("tpa4fabric.noSuchTpaReqExists", to.getNameForScoreboard()));
        
        return 0;
    }
}
