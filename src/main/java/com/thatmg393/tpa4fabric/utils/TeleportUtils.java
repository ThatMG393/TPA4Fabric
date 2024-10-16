package com.thatmg393.tpa4fabric.utils;

import net.minecraft.server.network.ServerPlayerEntity;

public class TeleportUtils {
    public static void teleport(
        ServerPlayerEntity you,
        ServerPlayerEntity to
    ) {
        you.teleport(
            to.getServerWorld(),
            to.getBlockPos().getX() + 0.5,
            to.getBlockPos().getY() + 0.1,
            to.getBlockPos().getZ() + 0.5,
            you.getYaw(),
            you.getPitch()
        );
    }
}
