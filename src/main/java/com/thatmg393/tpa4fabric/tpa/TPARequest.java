package com.thatmg393.tpa4fabric.tpa;

import static com.thatmg393.tpa4fabric.utils.MCTextUtils.fromLang;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.thatmg393.tpa4fabric.config.ModConfigManager;
import com.thatmg393.tpa4fabric.utils.CountdownTimer;
import com.thatmg393.tpa4fabric.utils.TeleportUtils;

import net.minecraft.text.Text;

public record TPARequest(HashMap<String, TPARequest> container, TPAPlayer from, Timer scheduler) {
    public TPARequest {
        scheduler.schedule(new TimerTask() {
            @Override
            public void run() {
                from.sendChatMessage(fromLang("tpa4fabric.tpaReqExp"));
                container.remove(from.getPlayerUUID()); // remove expired request
            }
        }, ModConfigManager.loadOrGetConfig().tpaExpireTime * 1000);
    }

    public void accept(TPAPlayer whoAccepted) {
        consumed(whoAccepted);

        from.sendChatMessage(fromLang("tpa4fabric.acceptedTpaReqMsg", whoAccepted.getServerPlayerEntity().getNameForScoreboard()));
        whoAccepted.sendChatMessage(fromLang("tpa4fabric.acceptedTpaReqMsg", whoAccepted.getServerPlayerEntity().getNameForScoreboard()));
        from.sendChatMessage(fromLang("tpa4fabric.teleporting"));
        whoAccepted.sendChatMessage(fromLang("tpa4fabric.teleporting"));
        
        final int x = (int) from.getServerPlayerEntity().getX(), z = (int) from.getServerPlayerEntity().getZ();

        new CountdownTimer(new CountdownTimer.TimerCallback() {
            @Override
            public void onTick(CountdownTimer myself, long delta) {
                if (x != ((int) from.getServerPlayerEntity().getX()) || z != ((int) from.getServerPlayerEntity().getZ())) {
                    myself.stop();
                    from.sendChatMessage(fromLang("tpa4fabric.tpaMoved"));
                }

                if ((delta % 1000) == 0) {
                    Text message = fromLang("tpa4fabric.tpaCountdown", (delta / 1000));
                    from.sendChatMessage(message);
                    whoAccepted.sendChatMessage(message);
                }
            }

            @Override
            public void onStop(CountdownTimer myself, long remaining) { }

            @Override
            public void onFinished(CountdownTimer myself) {
                TeleportUtils.teleport(from.getServerPlayerEntity(), whoAccepted.getServerPlayerEntity());
                from.sendChatMessage(fromLang("tpa4fabric.successTp"));
            }
        }, (ModConfigManager.loadOrGetConfig().tpaTeleportTime + 1) * 1000).start();
    }

    public void deny(TPAPlayer whoDenied) {
        consumed(whoDenied);

        whoDenied.sendChatMessage(fromLang("tpa4fabric.denyTpaReq", from.getServerPlayerEntity().getNameForScoreboard()));
        from.sendChatMessage(fromLang("tpa4fabric.denyTpaReqMsg", whoDenied.getServerPlayerEntity().getNameForScoreboard()));
    }

    public void consumed(TPAPlayer consumer) {
        scheduler.cancel();
        consumer.cancelTPARequest(from.getPlayerUUID());
    }
}
