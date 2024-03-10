package com.example.examplemod.item;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExampleMod.MODID);

    public static final FoodProperties ZOMBIE_HEARTH_PROPERTIES = (new FoodProperties.Builder()).nutrition(0).saturationMod(1.0F).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 1), 1.0F).alwaysEat().build();
    public static final RegistryObject<Item> ZOMBIE_HEARTH = ITEMS.register("zombie_hearth", () -> new Item((new Item.Properties()).rarity(Rarity.RARE).food(ZOMBIE_HEARTH_PROPERTIES)));

    public static final FoodProperties ZOMBIE_BRAIN_PROPERTIES = (new FoodProperties.Builder()).nutrition(0).saturationMod(1.0F).effect(() -> new MobEffectInstance(MobEffects.LUCK, 400 ,1), 1.0F).alwaysEat().build();
    public static final RegistryObject<Item> ZOMBIE_BRAIN = ITEMS.register("zombie_brain", () -> new Item((new Item.Properties()).rarity(Rarity.RARE).food(ZOMBIE_BRAIN_PROPERTIES)));


    public static final FoodProperties ZOMBIE_LUNG_PROPERTIES = (new FoodProperties.Builder()).nutrition(0).saturationMod(1.0F).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400 ,1), 1.0F).alwaysEat().build();
    public static final RegistryObject<Item> ZOMBIE_LUNG = ITEMS.register("zombie_lung", () -> new Item((new Item.Properties()).rarity(Rarity.RARE).food(ZOMBIE_LUNG_PROPERTIES)));

    public static final RegistryObject<Item> COIN = ITEMS.register("coin", () -> new Item((new Item.Properties()).rarity(Rarity.UNCOMMON)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
