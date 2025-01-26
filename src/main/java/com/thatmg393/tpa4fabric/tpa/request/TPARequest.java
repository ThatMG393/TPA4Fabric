package com.thatmg393.tpa4fabric.tpa.request;

import com.thatmg393.tpa4fabric.TPA4Fabric;
import com.thatmg393.tpa4fabric.config.ModConfigManager;
import com.thatmg393.tpa4fabric.tpa.request.base.BaseRequest;
import com.thatmg393.tpa4fabric.tpa.request.callback.enums.TPAFailReason;
import com.thatmg393.tpa4fabric.tpa.wrapper.TPAPlayerWrapper;
import com.thatmg393.tpa4fabric.tpa.wrapper.models.Coordinates;
import com.thatmg393.tpa4fabric.tpa.wrapper.models.TeleportParameters;
import com.thatmg393.tpa4fabric.utils.CountdownTimer;

public class TPARequest extends BaseRequest {
    public TPARequest(TPAPlayerWrapper requester, TPAPlayerWrapper receiver) {
        super(requester, receiver);
    }
    
    @Override
    public void accept() {
        super.accept();

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

                default_onTick(delta);

                if (!receiver.isAlive()) {
                    requester.onTPAFail(TPAFailReason.RECEIVER_DEAD_OR_DISCONNECTED);
                    myself.stop();
                }

                if (!requester.isAlive()) {
                    receiver.onTPAFail(TPAFailReason.REQUESTER_DEAD_OR_DISCONNECTED);
                    myself.stop();
                }
            }

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
}
