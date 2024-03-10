package com.example.examplemod.villager;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.block.ModBlocks;
import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, ExampleMod.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, ExampleMod.MODID);

    public static final RegistryObject<PoiType> APOCALIPSE_POI = POI_TYPES.register("apocalipse_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.APOCALIPSE_TRADING_STATION.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> APOCALIPSE_VILLAGER =
            VILLAGER_PROFESSIONS.register("apocalipse_villager",
                    () -> new VillagerProfession("apocalipse_villager",
                            holder -> holder.get() == APOCALIPSE_POI.get(), holder -> holder.get() == APOCALIPSE_POI.get(),
                            ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER));

    public static void register(IEventBus eventBus){
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
