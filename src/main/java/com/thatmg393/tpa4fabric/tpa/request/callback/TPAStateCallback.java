package com.thatmg393.tpa4fabric.tpa.request.callback;

import com.thatmg393.tpa4fabric.tpa.request.callback.enums.TPAFailReason;
import com.thatmg393.tpa4fabric.tpa.wrapper.models.TeleportParameters;

public interface TPAStateCallback {
    boolean beforeTeleport(TeleportParameters location);
    void onTPASuccess(TeleportParameters location);
    void onTPAFail(TPAFailReason reason);
}
