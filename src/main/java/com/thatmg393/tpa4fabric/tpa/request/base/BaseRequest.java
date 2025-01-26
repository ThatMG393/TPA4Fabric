package com.thatmg393.tpa4fabric.tpa.request.base;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

import java.util.Timer;
import java.util.TimerTask;

import com.thatmg393.tpa4fabric.TPA4Fabric;
import com.thatmg393.tpa4fabric.config.ModConfigManager;
import com.thatmg393.tpa4fabric.tpa.wrapper.TPAPlayerWrapper;

public abstract class BaseRequest {
    private final Timer expirationTimer = new Timer();

    public final TPAPlayerWrapper requester;
    public final TPAPlayerWrapper receiver;

    public BaseRequest(TPAPlayerWrapper requester, TPAPlayerWrapper receiver) {
        this.requester = requester;
        this.receiver = receiver;

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
        TPA4Fabric.LOGGER.info("Consumed TPA request from " + requester.name);
    }

    public void accept() {
        consume();
    }

    public void deny() {
        consume();
    }

    public void default_onTick(long delta) {
        if ((delta % 1000) == 0) {
            float remain = (delta / 1000);
            requester.sendMessage(fromLang("tpa4fabric.message.teleport.countdown", remain));
            receiver.sendMessage(fromLang("tpa4fabric.message.teleport.countdown", remain));
        }
    }
}
