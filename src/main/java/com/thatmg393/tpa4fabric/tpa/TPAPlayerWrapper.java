package com.thatmg393.tpa4fabric.tpa;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Timer;

import com.thatmg393.tpa4fabric.TPA4Fabric;
import com.thatmg393.tpa4fabric.config.ModConfigManager;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Position;

public class TPAPlayerWrapper {
    public TPAPlayerWrapper(ServerPlayerEntity player) {
        this.name = player.getNameForScoreboard();
        this.uuid = player.getUuidAsString();

        this.player = player;
    }

    public final String name;
    public final String uuid;

    private final ServerPlayerEntity player;

    private Instant lastCommandInvokeTime = null;
    private Coordinates lastTPACoordinates = null;

    private boolean allowsTPARequests = ModConfigManager.loadOrGetConfig().defaultAllowTPARequests;

    private LinkedHashMap<String, TPARequest> incomingTPARequests = new LinkedHashMap<>(ModConfigManager.loadOrGetConfig().tpaRequestLimit);

    public Optional<CommandResult> createNewTPARequest(TPAPlayerWrapper requester) {
        if (requester.equals(this)) return Optional.of(CommandResult.TPA_SELF);
        if (requester.isOnCommandCooldown()) return Optional.of(CommandResult.ON_COOLDOWN);
        if (hasExistingTPARequest(requester.uuid)) return Optional.of(CommandResult.HAS_EXISTING);
        
        requester.markInCooldown();
        incomingTPARequests.put(
            requester.uuid, new TPARequest(requester, this, new Timer())
        );

        return Optional.of(CommandResult.SUCCESS);
    }

    public Optional<CommandResult> consumeTPARequest(TPAPlayerWrapper from) {
        if (isIncomingTPARequestEmpty()) return Optional.of(CommandResult.EMPTY_REQUESTS);

        if (from == null) {
            TPARequest tpaRequest = incomingTPARequests.pollFirstEntry().getValue();
            tpaRequest.accept();
        } else {
            if (from.equals(this)) Optional.of(CommandResult.TPA_SELF);
            if (!hasExistingTPARequest(from.uuid)) return Optional.of(CommandResult.NO_REQUEST);

            TPARequest tpaRequest = incomingTPARequests.remove(from.uuid);
            tpaRequest.accept();
        }

        return Optional.of(CommandResult.SUCCESS);
    }

    public void removeTPARequest(String requesterUuid) {
        incomingTPARequests.remove(requesterUuid);
    }

    public boolean hasExistingTPARequest(String requesterUuid) {
        return incomingTPARequests.containsKey(requesterUuid);
    }

    public boolean isIncomingTPARequestEmpty() {
        return incomingTPARequests.isEmpty();
    }

    public void markInCooldown() {
        if (lastCommandInvokeTime != null)
            TPA4Fabric.LOGGER.warn("Cannot mark " + name + " in cooldown while they are still on cooldown.");
        
        lastCommandInvokeTime = Instant.now();
    }

    public boolean isOnCommandCooldown() {
        if (lastCommandInvokeTime == null) return false;

        Instant now = Instant.now();
        if (now.compareTo(lastCommandInvokeTime) <= ModConfigManager.loadOrGetConfig().tpaCooldown) return true;

        lastCommandInvokeTime = null;
        return false;
    }

    public Position getPos1() {
        return player.getPos();
    }

    public Position getPos2() {
        return new Position() {
            @Override
            public double getX() {
                return player.getBlockPos().getX();
            }

            @Override
            public double getY() {
                return player.getBlockPos().getY();
            }

            @Override
            public double getZ() {
                return player.getBlockPos().getZ();
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null)
            && (obj instanceof TPAPlayerWrapper)
            && name.equals(((TPAPlayerWrapper) obj).name)
            && uuid.equals(((TPAPlayerWrapper) obj).uuid);
    }

    public static enum CommandResult {
        SUCCESS,

        TPA_SELF,
        ON_COOLDOWN,

        HAS_EXISTING,
        NO_REQUEST,
        EMPTY_REQUESTS
    }

    private static final record Coordinates(
        float x, float y, float z
    ) { }
}
