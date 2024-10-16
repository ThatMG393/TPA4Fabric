package com.thatmg393.tpa4fabric.tpa;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.thatmg393.tpa4fabric.config.ModConfigManager;
import com.thatmg393.tpa4fabric.utils.CountdownTimer;
import com.thatmg393.tpa4fabric.utils.TeleportUtils;

import net.minecraft.text.Text;
import net.minecraft.util.math.Position;

public record TPARequestOld(HashMap<String, TPARequestOld> container, TPAPlayerOld requestOwner, Timer scheduler) {
    public TPARequestOld {
        scheduler.schedule(new TimerTask() {
            @Override
            public void run() {
                requestOwner.sendChatMessage(fromLang("tpa4fabric.tpaReqExp"));
                container.remove(requestOwner.getPlayerUUID()); // remove expired request
            }
        }, ModConfigManager.loadOrGetConfig().tpaExpireTime * 1000);
    }

    public void accept(TPAPlayerOld whoAccepted) {
        consumed(whoAccepted);

        requestOwner.sendChatMessage(fromLang("tpa4fabric.acceptedTpaReqMsg", whoAccepted.getServerPlayerEntity().getNameForScoreboard()));
        whoAccepted.sendChatMessage(fromLang("tpa4fabric.acceptedTpaReq", requestOwner.getServerPlayerEntity().getNameForScoreboard()));
        
        requestOwner.sendChatMessage(fromLang("tpa4fabric.teleporting"));
        whoAccepted.sendChatMessage(fromLang("tpa4fabric.teleporting"));
        
        final int x = (int) requestOwner.getServerPlayerEntity().getX(), z = (int) requestOwner.getServerPlayerEntity().getZ();

        new CountdownTimer(new CountdownTimer.TimerCallback() {
            @Override
            public void onTick(CountdownTimer myself, long delta) {
                if (x != ((int) requestOwner.getServerPlayerEntity().getX()) || z != ((int) requestOwner.getServerPlayerEntity().getZ())) {
                    myself.stop();
                    requestOwner.sendChatMessage(fromLang("tpa4fabric.requester.moved"));
                    whoAccepted.sendChatMessage(fromLang("tpa4fabric.acceptor.r_moved", requestOwner.getServerPlayerEntity().getNameForScoreboard()));
                }

                if ((delta % 1000) == 0) {
                    Text message = fromLang("tpa4fabric.tpaCountdown", (delta / 1000));
                    requestOwner.sendChatMessage(message);
                    whoAccepted.sendChatMessage(message);
                }
            }

            @Override
            public void onStop(CountdownTimer myself, long remaining) { }

            @Override
            public void onFinished(CountdownTimer myself) {
                TeleportUtils.teleport(requestOwner.getServerPlayerEntity(), whoAccepted.getServerPlayerEntity());

                requestOwner.sendChatMessage(fromLang("tpa4fabric.successTp"));
                whoAccepted.sendChatMessage(fromLang("tpa4fabric.successTp"));
            }
        }, (ModConfigManager.loadOrGetConfig().tpaTeleportTime + 1) * 1000).start();
    }

    public void deny(TPAPlayerOld whoDenied) {
        consumed(whoDenied);

        whoDenied.sendChatMessage(fromLang("tpa4fabric.denyTpaReq", requestOwner.getServerPlayerEntity().getNameForScoreboard()));
        requestOwner.sendChatMessage(fromLang("tpa4fabric.denyTpaReqMsg", whoDenied.getServerPlayerEntity().getNameForScoreboard()));
    }

    public void consumed(TPAPlayerOld consumer) {
        scheduler.cancel();
        consumer.cancelTPARequest(requestOwner.getPlayerUUID());
    }
}
