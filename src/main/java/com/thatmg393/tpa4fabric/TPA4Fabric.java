package com.thatmg393.tpa4fabric;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.thatmg393.tpa4fabric.config.ModConfigManager;
import com.thatmg393.tpa4fabric.tpa.TPAManager;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;

public class TPA4Fabric implements DedicatedServerModInitializer {
	public static final String MOD_ID = "tpa4fabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeServer() {
		LOGGER.info("Using TPA4Fabric " + FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString());
		LOGGER.info("Loading config...");
		ModConfigManager.loadOrGetConfig();
		LOGGER.info("Loaded!");

		LOGGER.info("Registering TPA4Fabric commands...");
		registerCommands();
		LOGGER.info("Registered, have fun!");
	}

	private void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) -> {
			dispatcher.register(
				literal("tpa")
				.requires(ServerCommandSource::isExecutedByPlayer)
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

			/* Still thinking how would I index the ConfigData
			dispatcher.register(
				literal("tpaconfig")
				.requires(src -> src.isExecutedByPlayer())
				.then(
					argument("key", StringArgumentType.string())
					.then(
						argument("value", IntegerArgumentType.integer()) // TODO: Replace IntegerArgumentType to AnyArgumentType
						.executes(ctx -> 1)
					)
				)
			);
			*/
		});
	}
}
