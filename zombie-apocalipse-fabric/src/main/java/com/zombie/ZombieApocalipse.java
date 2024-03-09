package com.zombie;

import com.zombie.features.ZombieFeature;
import com.zombie.features.config.ZombieFeatureConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ZombieApocalipse implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	public static final Logger LOGGER = LoggerFactory.getLogger("zombie-apocalipse");

	public static final Identifier EXAMPLE_FEATURE_ID = new Identifier("tutorial", "example_feature");
	public static final ZombieFeature EXAMPLE_FEATURE = new ZombieFeature(ZombieFeatureConfig.CODEC);
	public static final ConfiguredFeature<ZombieFeatureConfig, ZombieFeature> EXAMPLE_FEATURE_CONFIGURED = new ConfiguredFeature<>(
			EXAMPLE_FEATURE,
			new ZombieFeatureConfig(10, new Identifier("minecraft", "netherite_block"))
	);
	// our PlacedFeature. this is what gets passed to the biome modification API to add to the biome.
	public static PlacedFeature EXAMPLE_FEATURE_PLACED = new PlacedFeature(
			RegistryEntry.of(
					EXAMPLE_FEATURE_CONFIGURED
					//  the SquarePlacementModifier makes the feature generate a cluster of pillars each time
			), List.of(SquarePlacementModifier.of())
	);

	@Override
	public void onInitialize() {
		// register the features
		Registry.register(Registries.FEATURE, EXAMPLE_FEATURE_ID, EXAMPLE_FEATURE);

		// add it to overworld biomes using FAPI
		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				// the feature is to be added while flowers and trees are being generated
				GenerationStep.Feature.VEGETAL_DECORATION,
				RegistryKey.of(RegistryKeys.PLACED_FEATURE, EXAMPLE_FEATURE_ID));
	}
}
