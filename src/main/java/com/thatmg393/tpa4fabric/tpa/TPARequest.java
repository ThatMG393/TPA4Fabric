package com.thatmg393.tpa4fabric.tpa;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.thatmg393.tpa4fabric.config.ModConfigManager;
import com.thatmg393.tpa4fabric.utils.CountdownTimer;
import com.thatmg393.tpa4fabric.utils.TeleportUtils;

import net.minecraft.server.network.ServerPlayerEntity;

public record TPARequest(HashMap<String, TPARequest> container, ServerPlayerEntity from, Timer scheduler) {
    public TPARequest {
        scheduler.schedule(new TimerTask() {
            @Override
            public void run() {
                if (container != null)
                    container.remove(TPARequest.this);
                from.sendMessage(fromLang("tpa4fabric.tpaReqExp"));
            }
        }, ModConfigManager.loadOrGetConfig().tpaExpireTime * 1000);
    }

    public void accept(ServerPlayerEntity whoAccepted) {
        consumed();

        from.sendMessage(fromLang("tpa4fabric.acceptedTpaReqMsg", whoAccepted.getName().getString()));
        from.sendMessage(fromLang("tpa4fabric.teleporting"));
        
        final int x = (int) from.getX(), z = (int) from.getZ();

        new CountdownTimer(new CountdownTimer.TimerCallback() {
            @Override
            public void onTick(CountdownTimer myself, long delta) {
                if (x != ((int) from.getX()) || z != ((int) from.getZ())) {
                    myself.stop();
                    from.sendMessage(fromLang("tpa4fabric.tpaMoved"));
                }
            }

            @Override
            public void onStop(CountdownTimer myself, long remaining) { }

            @Override
            public void onFinished(CountdownTimer myself) {
                TeleportUtils.teleport(from, whoAccepted);
                from.sendMessage(fromLang("tpa4fabric.successTp"));
            }
        }, ModConfigManager.loadOrGetConfig().tpaTeleportTime * 1000).start();
    }

    public void deny(ServerPlayerEntity whoDenied) {
        consumed();

        whoDenied.sendMessage(fromLang("tpa4fabric.denyTpaReq", from.getName().getString()));
        from.sendMessage(fromLang("tpa4fabric.denyTpaReqMsg", whoDenied.getName().getString()));
    }

    public void consumed() {
        scheduler.cancel();
    }
}
