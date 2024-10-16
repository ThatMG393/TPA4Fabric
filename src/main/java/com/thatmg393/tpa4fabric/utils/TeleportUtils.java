package com.thatmg393.tpa4fabric.utils;

import net.minecraft.server.network.ServerPlayerEntity;

public class TeleportUtils {
    public static void teleport(
        ServerPlayerEntity you,
        ServerPlayerEntity to
    ) {
        you.teleport(
            to.getServerWorld(),
            to.getBlockPos().getX(),
            to.getBlockPos().getY() + 1,
            to.getBlockPos().getZ(),
            you.getYaw(),
            you.getPitch()
        );
    }
}
