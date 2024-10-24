package com.thatmg393.tpa4fabric.tpa.request;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

import java.util.Timer;
import java.util.TimerTask;

import com.thatmg393.tpa4fabric.TPA4Fabric;
import com.thatmg393.tpa4fabric.config.ModConfigManager;
import com.thatmg393.tpa4fabric.tpa.request.callback.enums.TPAFailReason;
import com.thatmg393.tpa4fabric.tpa.wrapper.TPAPlayerWrapper;
import com.thatmg393.tpa4fabric.tpa.wrapper.models.Coordinates;
import com.thatmg393.tpa4fabric.tpa.wrapper.models.TeleportParameters;
import com.thatmg393.tpa4fabric.utils.CountdownTimer;

public record TPARequest(TPAPlayerWrapper requester, TPAPlayerWrapper receiver, Timer expirationTimer) {
    public TPARequest {
        expirationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                receiver.removeTPARequest(requester.uuid);

                requester.sendMessage(fromLang("tpa4fabric.message.requester.tpa.expire", receiver.name));
                receiver.sendMessage(fromLang("tpa4fabric.message.receiver.tpa.expire", requester.name));
            }
        }, ModConfigManager.loadOrGetConfig().tpaExpireTime * 1000);
    }

    private void consume() {
        expirationTimer.cancel();
    }

    public void accept() {
        consume();
        
        Coordinates lastRequesterCoordinates = requester.getCurrentCoordinates();
        new CountdownTimer(new CountdownTimer.TimerCallback() {
            @Override
            public void onTick(CountdownTimer myself, long delta) {
                if ((delta % 500) == 0) {
                    if ((int) lastRequesterCoordinates.x() != (int) requester.getCurrentCoordinates().x()
                     && (int) lastRequesterCoordinates.z() != (int) requester.getCurrentCoordinates().z()) {
                        requester.onTPAFail(TPAFailReason.YOU_MOVED);
                        receiver.onTPAFail(TPAFailReason.REQUESTER_MOVED);
                        myself.stop();

                        return;
                    }
                }

                if ((delta % 1000) == 0) {
                    float remain = (delta / 1000);
                    requester.sendMessage(fromLang("tpa4fabric.message.teleport.countdown", remain));
                    receiver.sendMessage(fromLang("tpa4fabric.message.teleport.countdown", remain));
                }

                if (!receiver.isAlive()) {
                    requester.onTPAFail(TPAFailReason.TARGET_DEAD_OR_DISCONNECTED);
                    myself.stop();
                }
            }

            @Override
            public void onStop(CountdownTimer myself, long remaining) { }

            @Override
            public void onFinished(CountdownTimer myself) {
                TeleportParameters teleportParams = new TeleportParameters(receiver.getCurrentDimension(), receiver.getCurrentCoordinates());

                if (requester.beforeTeleport(teleportParams)) {
                    requester.teleport(teleportParams);
                    TPA4Fabric.LOGGER.info(requester.name + " teleported to " + teleportParams);

                    requester.onTPASuccess(teleportParams); // might just pass 'receiver' fr?
                    receiver.onTPASuccess(null);
                }
            }
        }, (ModConfigManager.loadOrGetConfig().tpaTeleportTime + 1) * 1000).start();
    }

    public void deny() {
        consume();
        receiver.removeTPARequest(requester.uuid);

        requester.sendMessage(fromLang("tpa4fabric.message.requester.tpa.deny", receiver.name));
        receiver.sendMessage(fromLang("tpa4fabric.message.receiver.tpa.deny", requester.name));
    }
}
