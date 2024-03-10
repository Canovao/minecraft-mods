package com.example.examplemod;

import com.example.examplemod.block.ModBlocks;
import com.example.examplemod.item.ModItems;
import com.example.examplemod.mobs.CursedZombieEntity;
import com.example.examplemod.villager.ModVillagers;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
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
import org.slf4j.Logger;

import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "zombieapocalipseforge";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Random random = new Random();
    private static final long ZOMBIE_SPAWNTIME = 10000L;
    private static final long ZOMBIE_SPAWN_DISTANCE = 10;

    public ExampleMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModVillagers.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static void spawnZombie(Level level, Player player, Long time) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        LOGGER.info("Spawning zombie");

                        try{
                            double playerX = player.getX();
                            double playerY = player.getY();
                            double playerZ = player.getZ();

                            double spawnX = playerX + (ZOMBIE_SPAWN_DISTANCE * (random.nextInt() % 2 == 0 ? -1 : 1));
                            double spawnZ = playerZ + (ZOMBIE_SPAWN_DISTANCE * (random.nextInt() % 2 == 0 ? -1 : 1));

                            CursedZombieEntity zombie = new CursedZombieEntity(level);

                            zombie.setPos(spawnX, playerY, spawnZ);

                            level.addFreshEntity(zombie);
                            LOGGER.info("\tZombie: " + zombie);

                            zombie.setTarget(player);

                            spawnZombie(level, player, time);
                        } catch(Exception e) {
                            LOGGER.error("Error spawning zombie: " + e.getMessage());
                        }
                    }
                },
                time // milliseconds
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.ZOMBIE_HEARTH);
            event.accept(ModItems.ZOMBIE_BRAIN);
            event.accept(ModItems.ZOMBIE_LUNG);
        }

        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES){
            event.accept(ModItems.COIN);
            event.accept(ModItems.COIN_PURSE);
        }

        if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS){
            event.accept(ModBlocks.APOCALIPSE_TRADING_STATION);
        }
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
            if(player.getInventory().contains(new ItemStack(Items.WOODEN_SWORD, 1))){
                return;
            }
            ItemStack item1 = new ItemStack(Items.WOODEN_SWORD, 1);
            player.getInventory().add(item1);
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity().level().isClientSide) {
                // Only execute on the server side
                return;
            }

            addStarterItems(event.getEntity());

            // 1 zumbi a cada ZOMBIE_SPAWNTIME segundos
            spawnZombie(event.getEntity().level(), event.getEntity(), ZOMBIE_SPAWNTIME);

            // 1 zumbi a cada ZOMBIE_SPAWNTIME * 2 segundos
            spawnZombie(event.getEntity().level(), event.getEntity(), ZOMBIE_SPAWNTIME * 2);

            // 1 zumbi a cada ZOMBIE_SPAWNTIME * 6 segundos
            spawnZombie(event.getEntity().level(), event.getEntity(), ZOMBIE_SPAWNTIME * 6);
        }
    }

    // TODO implementar o villager que troca coins por itens uteis
    // TODO implementar um efeito no player que faz com que nao spawne inimigos em volta dele
    // TODO implementar um efeito que faz com que spawne o dobro de zumbis amaldiçoados em volta dele
    // TODO implementar quests
    // TODO implementar novos tipos de zumbis
}
