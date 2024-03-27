package com.thatmg393.tpa4fabric;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thatmg393.tpa4fabric.manager.TPAManager;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.EntityArgumentType;

public class TPA4Fabric implements ModInitializer {
	public static final String MOD_ID = "tpa4fabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Using TPA4Fabric v" + FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString());
		LOGGER.info("Registering TPA4Fabric commands...");
		registerCommands();
		LOGGER.info("Registered, have fun!");
	}

	private void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) -> {
			dispatcher.register(
				literal("tpa")
				.requires(src -> src.isExecutedByPlayer())
				.then(
					argument("to", EntityArgumentType.player())
				    .executes(ctx -> TPAManager.getInstance().newTPA(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "to")))
				)
			);

			dispatcher.register(
				literal("tpaaccept")
				.requires(src -> src.isExecutedByPlayer())
				.then(
					argument("from", EntityArgumentType.player())
				    .executes(ctx -> TPAManager.getInstance().acceptTPA(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "from")))
				)
				.executes(ctx -> TPAManager.getInstance().acceptTPA(ctx.getSource(), null))
			);

			dispatcher.register(
				literal("tpadeny")
				.requires(src -> src.isExecutedByPlayer())
				.then(
					argument("from", EntityArgumentType.player())
					.executes(ctx -> TPAManager.getInstance().denyTPA(ctx.getSource(), EntityArgumentType.getPlayer(ctx, "from")))
				)
				.executes(ctx -> TPAManager.getInstance().denyTPA(ctx.getSource(), null))
			);
		});
	}
}