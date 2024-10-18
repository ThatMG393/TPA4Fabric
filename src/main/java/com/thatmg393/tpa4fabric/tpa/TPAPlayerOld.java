package com.thatmg393.tpa4fabric.tpa;

import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;

import com.thatmg393.tpa4fabric.config.ModConfigManager;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Position;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

public class TPAPlayerOld implements AfterDeath {
    private ServerPlayerEntity realPlayer;
    private final String myUuid;
    private boolean allowTPARequests;

    private HashMap<String, TPARequestOld> tpaRequests = new HashMap<>();
    private long cmdInvokeTime = 0;

    public TPAPlayerOld(ServerPlayerEntity me) {
        this.realPlayer = me;
        this.myUuid = me.getUuidAsString();
        this.allowTPARequests = ModConfigManager.loadOrGetConfig().defaultAllowTPARequests;

        ServerLivingEntityEvents.AFTER_DEATH.register(this);
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

    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity instanceof PlayerEntity) {
            if (((PlayerEntity)entity).getUuidAsString().equals(realPlayer.getUuidAsString())) {
                System.out.println("Player -> " + realPlayer.getNameForScoreboard() + " died. Finna re-set the realPlayer variable.");
                realPlayer = (ServerPlayerEntity) entity;
            }
        }
    }
}
