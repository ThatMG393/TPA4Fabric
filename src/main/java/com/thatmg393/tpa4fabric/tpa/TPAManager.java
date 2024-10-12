package com.thatmg393.tpa4fabric.tpa;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

import java.util.HashMap;

import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.connection.GeyserConnection;

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
            ServerPlayerEntity whoJoined = netHandler.getPlayer();
            TPA4Fabric.LOGGER.info(whoJoined.getNameForScoreboard() + " joined the server, registering in the TPA...");
            if (!players.containsKey(whoJoined.getUuidAsString())) {
                players.put(
                    whoJoined.getUuidAsString(),
                    new TPAPlayer(whoJoined)
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

        TPAPlayer target = getTPAPlayer(to);

        if (target == null) {
            TPA4Fabric.LOGGER.warn(to.getUuidAsString() + " is not a player.");
            return 1;
        }

        if (!target.allowsTPARequests()) {
            me.sendChatMessage(fromLang("tpa4fabric.playerTpaOff", target.getServerPlayerEntity().getName().getString()));
            return 1;
        }

        target.newTPARequest(me);
        target.markInCooldown();

        me.sendChatMessage(fromLang("tpa4fabric.sentTpaReq", to.getName().getString()));
        target.sendChatMessage(fromLang("tpa4fabric.recvTpaReq", me.getServerPlayerEntity().getName().getString()));
        
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
            TPARequest request = me.getTPARequest(getTPAPlayer(from));
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
            TPARequest request = me.getTPARequest(getTPAPlayer(from));
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
        TPAPlayer to2 = getTPAPlayer(to);
        TPARequest r = to2.cancelTPARequest(context.getPlayer().getUuidAsString());
        if (r != null) r.consumed();

        context.getPlayer().sendMessage(fromLang("tpa4fabric.tpaCancel", to.getName().getString()));

        return 0;
    }

    // Geyser compat?
    public TPAPlayer getTPAPlayer(ServerPlayerEntity player) {
        if (!players.containsKey(player.getUuidAsString())) {
            if (FabricLoader.getInstance().isModLoaded("geyser-fabric")) {
                GeyserConnection bedrockPlayer = GeyserApi.api().connectionByUuid(player.getUuid());
                if (bedrockPlayer != null)
                    return players.put(player.getUuidAsString(), new TPAPlayer(player));
            }
            return null;
        }
        return players.get(player.getUuidAsString());
    }
}