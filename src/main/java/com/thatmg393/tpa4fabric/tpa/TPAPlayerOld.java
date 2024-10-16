package com.thatmg393.tpa4fabric.tpa;

import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;

import com.thatmg393.tpa4fabric.config.ModConfigManager;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Position;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

public class TPAPlayerOld {
    private final ServerPlayerEntity realPlayer;
    private final String myUuid;
    private boolean allowTPARequests;

    private HashMap<String, TPARequestOld> tpaRequests = new HashMap<>();
    private long cmdInvokeTime = 0;

    public TPAPlayerOld(ServerPlayerEntity me) {
        this.realPlayer = me;
        this.myUuid = me.getUuidAsString();
        this.allowTPARequests = ModConfigManager.loadOrGetConfig().defaultAllowTPARequests;
    }

    public void newTPARequest(TPAPlayerOld from) {
        tpaRequests.put(
            from.getPlayerUUID(),
            new TPARequestOld(
                tpaRequests, from, new Timer()
            )
        );
    }

    public TPARequestOld getTPARequest(TPAPlayerOld from) {
        if (isTPARequestsEmpty()) return null;

        if (from != null) {
            TPARequestOld r = tpaRequests.get(from.getPlayerUUID());
            if (r == null) sendChatMessage(fromLang("tpa4fabric.noTpaReqFrom", from.getServerPlayerEntity().getNameForScoreboard()));
            return r;
        } else {
            if (tpaRequests.size() > 1) {
                sendChatMessage(fromLang("tpa4fabric.multiTpaReq"));
                return null;
            }
        }
        
        return tpaRequests.values().stream().findFirst().orElse(null);
    }

    public TPARequestOld cancelTPARequest(String playerUuid) {
        return tpaRequests.remove(playerUuid);
    }

    public boolean hasExistingTPARequest(String fromPlayerUuid) {
        return tpaRequests.containsKey(fromPlayerUuid);
    }

    public void sendChatMessage(Text message) {
        realPlayer.sendMessage(message);
    }

    public void markInCooldown() {
        if (cmdInvokeTime != 0) System.out.println("marking bro on cooldown whilst on cooldown? doesnt even make any sense.");
        cmdInvokeTime = Instant.now().getEpochSecond();
    }

    public void setAllowTPARequests(boolean allowTPARequests) {
        this.allowTPARequests = allowTPARequests;
    }

    public String getPlayerUUID() {
        return this.myUuid;
    }

    public ServerPlayerEntity getServerPlayerEntity() {
        return this.realPlayer;
    }

    public boolean isOnCooldown() {
        if (cmdInvokeTime == 0) return false;
        
        long diff = Instant.now().getEpochSecond() - cmdInvokeTime;
        if (diff <= ModConfigManager.loadOrGetConfig().tpaCooldown) return true;

        this.cmdInvokeTime = 0;
        return false;
    }

    public boolean allowsTPARequests() {
        return allowTPARequests;
    }

    public boolean isTPARequestsEmpty() {
        return tpaRequests.isEmpty();
    }

    public Position getPos1() {
        return realPlayer.getPos();
    }

    public Position getPos2() {
        return new Position() {
            @Override
            public double getX() {
                return realPlayer.getBlockPos().getX();
            }

            @Override
            public double getY() {
                return realPlayer.getBlockPos().getY();
            }

            @Override
            public double getZ() {
                return realPlayer.getBlockPos().getZ();
            }
        };
    }
}
