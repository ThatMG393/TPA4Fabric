package com.thatmg393.tpa4fabric;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thatmg393.tpa4fabric.manager.TPAManager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;

public class TPA4Fabric implements ModInitializer {
	public static final String MOD_ID = "tpa4fabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		registerCommands();
	}

	private void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) -> {
			dispatcher.register(
				literal("tpa")
				.requires(source -> source.hasPermissionLevel(1))
				.then(argument("to", EntityArgumentType.player()))
				.executes(ctx -> TPAManager.getInstance().newTPA(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "to")))
			);

			dispatcher.register(
				literal("tpaccept")
				.requires(source -> source.hasPermissionLevel(1))
				.then(argument("from", EntityArgumentType.player()))
				    .executes(ctx -> TPAManager.getInstance().acceptTPA(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "from")))
				.executes(ctx -> 1)
			);
		});
	}
}