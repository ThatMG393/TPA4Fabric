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

public record TPARequest(TPAPlayerWrapper requester, TPAPlayerWrapper reciever, Timer expirationTimer) {
    public TPARequest {
        expirationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                reciever.removeTPARequest(requester.uuid);

                requester.sendMessage(fromLang("tpa4fabric.message.requester.tpa.expire", reciever.name));
                reciever.sendMessage(fromLang("tpa4fabric.message.reciever.tpa.expire", requester.name));
            }
        }, 0);
    }

    private void consume() {
        expirationTimer.cancel();
    }

    public void accept() {
        consume();

        reciever.sendMessage(fromLang("tpa4fabric.message.receiver.tpa.accept", requester.name));
        requester.sendMessage(fromLang("tpa4fabric.message.requester.tpa.accept", reciever.name));

        Coordinates lastRequesterCoordinates = requester.getCurrentCoordinates();

        new CountdownTimer(new CountdownTimer.TimerCallback() {
            @Override
            public void onTick(CountdownTimer myself, long delta) {
                if ((delta % 1000) == 0) {
                    if (!lastRequesterCoordinates.equals(requester.getCurrentCoordinates())) {
                        requester.onTPAFail(TPAFailReason.YOU_MOVED);
                        reciever.onTPAFail(TPAFailReason.REQUESTER_MOVED);
                        myself.stop();

                        return;
                    }

                    float remain = (delta / 1000);
                    requester.sendMessage(fromLang("tpa4fabric.message.teleport.countdown", remain));
                    reciever.sendMessage(fromLang("tpa4fabric.message.teleport.countdown", remain));
                }

                if (!reciever.isAlive()) {
                    requester.onTPAFail(TPAFailReason.TARGET_DEAD_OR_DISCONNECTED);
                    myself.stop();
                }
            }

            @Override
            public void onStop(CountdownTimer myself, long remaining) { }

            @Override
            public void onFinished(CountdownTimer myself) {
                TeleportParameters teleportParams = new TeleportParameters(reciever.getCurrentDimension(), reciever.getCurrentCoordinates());

                requester.teleport(teleportParams);
                TPA4Fabric.LOGGER.info("Teleporting " + requester.name + " to " + teleportParams);

                requester.onTPASuccess(teleportParams); // might just pass 'reciever' fr?
                reciever.onTPASuccess(null);
            }
        }, (ModConfigManager.loadOrGetConfig().tpaTeleportTime + 1) * 1000).start();
    }

    public void deny() {
        consume();
        reciever.removeTPARequest(requester.uuid);

        requester.sendMessage(fromLang("tpa4fabric.message.requester.tpa.deny", reciever.name));
        reciever.sendMessage(fromLang("tpa4fabric.message.reciever.tpa.deny", requester.name));
    }
}
