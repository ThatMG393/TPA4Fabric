package com.thatmg393.tpa4fabric.tpa;

import java.time.Instant;
import java.util.HashMap;
import java.util.Timer;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

public class Player {
    private ServerPlayerEntity me;
    private String myUuid;

    private HashMap<String, TPARequest> tpaRequests = new HashMap<>();
    private long cmdInvokeTime = 0;

    private boolean allowedTpaRequest;

    public Player(ServerPlayerEntity me) {
        this.me = me;
        this.myUuid = me.getUuidAsString();
    }

    public boolean newTPARequest(ServerPlayerEntity from) {
        if (!this.allowedTpaRequest) return false;
        if (tpaRequests.containsKey(from.getUuidAsString())) return false;

        tpaRequests.put(
            from.getUuidAsString(),
            new TPARequest(
                tpaRequests, from, new Timer()
            )
        );

        return true;
    }

    public TPARequest getTPARequest(ServerPlayerEntity from) {
        TPARequest r = null;

        if (isTPARequestsEmpty()) return r;

        if (from != null) {
            r = tpaRequests.get(from.getUuidAsString());
            if (r == null) sendChatMessage(fromLang("tpa4fabric.noTpaReqFrom", from.getName().getString()));
            return r;
        }

        if (tpaRequests.size() > 1) {
            sendChatMessage(fromLang("tpa4fabric.multiTpaReq"));
            return r;
        }
        
        r = tpaRequests.get(tpaRequests.keySet().toArray()[0]);
        return r;
    }

    public TPARequest cancelTPARequest(String playerUuid) {
        return tpaRequests.remove(playerUuid);
    }

    public boolean isTPARequestsEmpty() {
        return tpaRequests.isEmpty();
    }

    public void sendChatMessage(Text message) {
        me.sendMessage(message);
    }

    public void markInCooldown() {
        if (cmdInvokeTime != 0) System.out.println("An illegal thing occurred in Player.markInCooldown()!");
        cmdInvokeTime = Instant.now().getEpochSecond();
    }

    public boolean inCooldown() {
        if (cmdInvokeTime == 0) return false;
        
        long diff = Instant.now().getEpochSecond() - cmdInvokeTime;
        if (diff <= 5) return true;

        this.cmdInvokeTime = 0;
        return false;
    }

    public void setAllowedTPARequest(boolean newValue) {
        this.allowedTpaRequest = newValue;
    }

    public boolean getAllowedTPARequest() {
        return this.allowedTpaRequest;
    }

    public String getUUID() {
        return this.myUuid;
    }

    public ServerPlayerEntity getRealPlayer() {
        return this.me;
    }
}
