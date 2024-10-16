package com.thatmg393.tpa4fabric.tpa;

import java.util.Timer;
import java.util.TimerTask;

import com.thatmg393.tpa4fabric.config.ModConfigManager;
import com.thatmg393.tpa4fabric.utils.CountdownTimer;

import net.minecraft.util.math.Position;

public record TPARequest(TPAPlayerWrapper requester, TPAPlayerWrapper reciever, Timer expirationTimer) {
    public TPARequest {
        expirationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                reciever.removeTPARequest(requester.uuid);
            }
        }, 0);
    }

    private void consume() {
        expirationTimer.cancel();
    }

    public void accept() {
        consume();

        new CountdownTimer(new CountdownTimer.TimerCallback() {
            @Override
            public void onTick(CountdownTimer myself, long delta) {
                if ((delta % 1000) == 0) {
                    Position t1 = reciever.getPos1();
                    Position t2 = reciever.getPos2(); //bpos

                    System.out.println("pos: " + t1.getX() + " " + t1.getY() + " " + t1.getZ());
                    System.out.println("bpos: " + t2.getX() + " " + t2.getY() + " " + t2.getZ());
                }
            }

            @Override
            public void onStop(CountdownTimer myself, long remaining) { }

            @Override
            public void onFinished(CountdownTimer myself) {
                Position t1 = reciever.getPos1();
                Position t2 = reciever.getPos2(); //bpos

                System.out.println("pos: " + t1.getX() + " " + t1.getY() + " " + t1.getZ());
                System.out.println("bpos: " + t2.getX() + " " + t2.getY() + " " + t2.getZ());
            }
        }, (ModConfigManager.loadOrGetConfig().tpaTeleportTime + 1) * 1000).start();
    }

    public void deny() {
        consume();
    }
}
