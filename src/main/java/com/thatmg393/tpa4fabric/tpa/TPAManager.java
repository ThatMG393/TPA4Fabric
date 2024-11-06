package com.thatmg393.tpa4fabric.tpa;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

import java.util.HashMap;

import org.slf4j.event.Level;

import com.thatmg393.tpa4fabric.TPA4Fabric;
import com.thatmg393.tpa4fabric.tpa.wrapper.TPAPlayerWrapper;
import com.thatmg393.tpa4fabric.tpa.wrapper.result.CommandResult;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
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
                new TPAPlayerWrapper(playerWhoJoined)
            );
        });

        ServerPlayConnectionEvents.DISCONNECT.register((netHandler, server) -> {
            ServerPlayerEntity playerWhoLeft = netHandler.getPlayer();
            players.remove(playerWhoLeft.getUuidAsString());
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            // TODO: update references to already sent TPARequests
            TPAPlayerWrapper playerWrapper = players.get(newPlayer.getUuidAsString());
            if (playerWrapper != null) playerWrapper.updatePlayerReference(newPlayer);
            else TPA4Fabric.LOGGER.warn("Somehow the respawned player is not stored in the HashMap, weird.");
        });
    }

    private HashMap<String, TPAPlayerWrapper> players = new HashMap<>();

    public int tpa(
        ServerPlayerEntity executer,
        ServerPlayerEntity target
    ) {
        TPAPlayerWrapper you = players.get(executer.getUuidAsString());
        TPAPlayerWrapper them = players.get(target.getUuidAsString());

        CommandResult result = them.createNewTPARequest(you).orElse(CommandResult.IGNORE);

        switch (result) {
            case SUCCESS:
                you.sendMessage(fromLang("tpa4fabric.message.requester.tpa", them.name));
                them.sendMessage(fromLang("tpa4fabric.message.receiver.tpa", you.name));
                return 1;

            case TPA_SELF:
                you.sendMessage(fromLang("tpa4fabric.message.error.tpa_to_self"));
                return 0;

            case NOT_ALLOWED:
                you.sendMessage(fromLang("tpa4fabric.message.fail.tpa_not_allowed"));
                return 0;

            case ON_COOLDOWN:
                you.sendMessage(fromLang("tpa4fabric.message.fail.tpa_on_cooldown", result.<Long>getExtraData().get()));
                return 0;

            case HAS_EXISTING:
                you.sendMessage(fromLang("tpa4fabric.message.fail.has_existing_tpa"));
                return 0;

            default:
                TPA4Fabric.LOGGER.error("Unknown command result: " + result);
                return 0;
        }
    }

    public int tpaback(
        ServerPlayerEntity executer
    ) {
        TPAPlayerWrapper you = players.get(executer.getUuidAsString());

        switch (you.goBackToLastCoordinates().orElse(CommandResult.IGNORE)) {
            case SUCCESS:
                you.sendMessage(fromLang("tpa4fabric.message.tpa.back"));
                return 1;

            case NO_PREVIOUS_COORDS:
                you.sendMessage(fromLang("tpa4fabric.message.fail.no_previous_coordinates"));
                return 0;

            default:
                return 0;
        }
    }

    public int tpaaccept(
        ServerPlayerEntity executer,
        ServerPlayerEntity target
    ) {
        TPAPlayerWrapper you = players.get(executer.getUuidAsString());
        TPAPlayerWrapper them = target == null ? null : players.get(target.getUuidAsString());
        
        CommandResult result = you.acceptTPARequest(them).orElse(CommandResult.IGNORE);
        switch (result) {
            case SUCCESS:
                result.<String>getExtraData().ifPresentOrElse((d) -> {
                    TPAPlayerWrapper tmpPlayer = players.get(d);

                    you.sendMessage(fromLang("tpa4fabric.message.receiver.tpa.accept", tmpPlayer.name));
                    tmpPlayer.sendMessage(fromLang("tpa4fabric.message.requester.tpa.accept", you.name));
                }, () -> {
                    you.sendMessage(fromLang("tpa4tabric.message.receiver.tpa.accept", them.name));
                    them.sendMessage(fromLang("tpa4fabric.message.requester.tpa.accept", you.name));
                });
                
                return 1;

            case EMPTY_REQUESTS:
                you.sendMessage(fromLang("tpa4fabric.message.fail.empty_requests"));
                return 0;

            case NO_REQUEST:
                you.sendMessage(fromLang("tpa4fabric.message.fail.no_request_from_player"));
                return 0;

            case TPA_SELF: // too lazy to account
            default:
                return 0;
        }
    }

    public int tpadeny(
        ServerPlayerEntity executer,
        ServerPlayerEntity target
    ) {
        TPAPlayerWrapper you = players.get(executer.getUuidAsString());
        TPAPlayerWrapper them = target == null ? null : players.get(target.getUuidAsString());

        CommandResult result = you.denyTPARequest(them).orElse(CommandResult.IGNORE);

        switch (result) {
            case SUCCESS:
                result.<String>getExtraData().ifPresentOrElse((d) -> {
                    TPAPlayerWrapper tmpPlayer = players.get(d);

                    you.sendMessage(fromLang("tpa4fabric.message.receiver.tpa.deny", tmpPlayer.name));
                    tmpPlayer.sendMessage(fromLang("tpa4fabric.message.requester.tpa.deny", you.name));
                }, () -> {
                    you.sendMessage(fromLang("tpa4tabric.message.receiver.tpa.deny", them.name));
                    them.sendMessage(fromLang("tpa4fabric.message.requester.tpa.deny", you.name));
                });
                
                return 1;

            case EMPTY_REQUESTS:
                you.sendMessage(fromLang("tpa4fabric.message.fail.empty_requests"));
                return 0;

            case NO_REQUEST:
                you.sendMessage(fromLang("tpa4fabric.message.fail.no_request_from_player"));
                return 0;

            case TPA_SELF: // too lazy to account
            default:
                return 0;
        }
    }

    public int tpaallow(
        ServerPlayerEntity executer
    ) {
        TPAPlayerWrapper you = players.get(executer.getUuidAsString());
        you.sendMessage(fromLang("tpa4fabric.message.tpa.allow", you.allowsTPARequests()));

        return 1;
    }

    public int tpaallow(
        ServerPlayerEntity executer,
        boolean allow
    ) {
        TPAPlayerWrapper you = players.get(executer.getUuidAsString());
        you.setAllowTPARequest(allow);

        if (allow) you.sendMessage(fromLang("tpa4fabric.message.tpa.allow.change.on"));
        else you.sendMessage(fromLang("tpa4fabric.message.tpa.allow.change.off"));
        
        return 1;
    }
}
