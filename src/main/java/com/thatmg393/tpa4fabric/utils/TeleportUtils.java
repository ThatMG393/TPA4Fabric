package com.thatmg393.tpa4fabric.utils;

import net.minecraft.server.network.ServerPlayerEntity;

public class TeleportUtils {
    public static void teleport(
        ServerPlayerEntity you,
        ServerPlayerEntity to
    ) {
        you.teleport(
            to.getServerWorld(),
            to.getX(),
            to.getY(),
            to.getZ(),
            you.getYaw(),
            you.getPitch()
        );
    }
}
