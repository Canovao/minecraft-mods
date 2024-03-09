package com.example.examplemod;

import com.example.examplemod.item.ModItems;
import com.example.examplemod.mobs.CursedZombieEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "zombieapocalipseforge";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Random random = new Random();
    private static final long ZOMBIE_SPAWNTIME = 30000L;

    public ExampleMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static void spawnZombie(Level level) {
        LOGGER.info("Spawning zombie");

        try{
            double playerX = level.players().get(random.nextInt() % level.players().size()).getX();
            double playerY = level.players().get(random.nextInt() % level.players().size()).getY();
            double playerZ = level.players().get(random.nextInt() % level.players().size()).getZ();

            double spawnX = playerX + random.nextInt() % 20 + (20 * (random.nextInt() % 2 == 0 ? -1 : 1));
            double spawnZ = playerZ + random.nextInt() % 20 + (20 * (random.nextInt() % 2 == 0 ? -1 : 1));

            CursedZombieEntity zombie = new CursedZombieEntity(level);
            LOGGER.info("\tZombie: " + zombie);

            if (spawnX == playerX && spawnZ == playerZ) {
                spawnX += (random.nextInt() % 2 == 0 ? -1 : 1) * 20;
                spawnZ += (random.nextInt() % 2 == 0 ? -1 : 1) * 20;
            }

            zombie.setPos(spawnX, playerY, spawnZ);

            if (zombie.checkSpawnObstruction(level)) {
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                spawnZombie(level);
                            }
                        },
                        ZOMBIE_SPAWNTIME * 2L // milliseconds
                );
            } else {
                level.addFreshEntity(zombie);
            }
        } catch(Exception e) {
            LOGGER.error("Error spawning zombie: " + e.getMessage());
        }

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        spawnZombie(level);
                    }
                },
                ZOMBIE_SPAWNTIME // milliseconds
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE) // Use FORGE bus for server-side events
    public static class ForgeModEvents {

        private static void addStarterItems(Player player){
            ItemStack item1 = new ItemStack(Items.IRON_SWORD, 1);
            player.getInventory().add(item1);
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity().level().isClientSide) {
                // Only execute on the server side
                return;
            }

            addStarterItems(event.getEntity());

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            spawnZombie(event.getEntity().level());
                        }
                    },
                    30000 // milliseconds
            );
        }
    }

}
