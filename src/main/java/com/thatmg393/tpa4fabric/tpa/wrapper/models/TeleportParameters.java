package com.thatmg393.tpa4fabric.tpa.wrapper.models;

import net.minecraft.server.world.ServerWorld;

public record TeleportParameters(
    ServerWorld dimension, Coordinates coordinates
) { }