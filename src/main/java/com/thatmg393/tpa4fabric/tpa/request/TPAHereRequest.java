package com.thatmg393.tpa4fabric.tpa.request;

import com.thatmg393.tpa4fabric.config.ModConfigManager;
import com.thatmg393.tpa4fabric.tpa.request.base.BaseRequest;
import com.thatmg393.tpa4fabric.tpa.request.callback.enums.TPAFailReason;
import com.thatmg393.tpa4fabric.tpa.wrapper.TPAPlayerWrapper;
import com.thatmg393.tpa4fabric.tpa.wrapper.models.Coordinates;
import com.thatmg393.tpa4fabric.utils.CountdownTimer;

public class TPAHereRequest extends BaseRequest {
    public TPAHereRequest(TPAPlayerWrapper requester, TPAPlayerWrapper receiver) {
        super(requester, receiver);
    }

    @Override
    public void accept() {
        super.accept();

        Coordinates lastReceiverCoordinates = receiver.getCurrentCoordinates();
        new CountdownTimer(new CountdownTimer.TimerCallback() {
            @Override
            public void onTick(CountdownTimer myself, long delta) {
                if ((delta % 500) == 0) {
                    if ((int) lastReceiverCoordinates.x() != (int) receiver.getCurrentCoordinates().x()
                        && (int) lastReceiverCoordinates.z() != (int) receiver.getCurrentCoordinates().z()) {
                        receiver.onTPAFail(TPAFailReason.YOU_MOVED);
                        requester.onTPAFail(TPAFailReason.REQUESTER_MOVED);
                        myself.stop();
        
                        return;
                    }
                }

                default_onTick(delta);

                if (!requester.isAlive()) {
                    receiver.onTPAFail(TPAFailReason.REQUESTER_DEAD_OR_DISCONNECTED);
                    myself.stop();
                }

                if (!receiver.isAlive()) {
                    requester.onTPAFail(TPAFailReason.RECEIVER_DEAD_OR_DISCONNECTED);
                }
            }

            @Override
            public void onFinished(CountdownTimer myself) {
                
            }
        }, (ModConfigManager.loadOrGetConfig().tpaTeleportTime + 1) * 1000).start();
    }

    @Override
    public void deny() {
        
    }
}