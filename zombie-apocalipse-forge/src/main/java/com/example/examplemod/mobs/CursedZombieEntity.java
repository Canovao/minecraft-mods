package com.example.examplemod.mobs;

import com.example.examplemod.item.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CursedZombieEntity extends Zombie{
    private static final int COMMON = 49;
    private static final int UNCOMMON = COMMON + 24;
    private static final int RARE = UNCOMMON + 11;
    private static final int EPIC = RARE + 7;
    private static final int LEGENDARY = EPIC + 4;
//    private static final int NOTHING = 5;


    private static final List<ItemStack> commonDropList = List.of(
            new ItemStack(Items.POTATO, 1),
            new ItemStack(Items.CARROT, 1),
            new ItemStack(Items.WHEAT_SEEDS, 1),
            new ItemStack(Items.POTATO, 1),
            new ItemStack(Items.STICK, 1),
            new ItemStack(Items.COBBLESTONE, 1),
            new ItemStack(Items.APPLE, 1)
    );

    private static final List<ItemStack> uncommonDropList = List.of(
            new ItemStack(Items.COPPER_ORE, 1),
            new ItemStack(Items.GOLD_ORE, 1),
            new ItemStack(Items.LEATHER_BOOTS, 1),
            new ItemStack(Items.LEATHER_CHESTPLATE, 1),
            new ItemStack(Items.LEATHER_HELMET, 1),
            new ItemStack(Items.LEATHER_LEGGINGS, 1),
            new ItemStack(Items.STONE_SWORD, 1)
    );

    private static final List<ItemStack> rareDropList = List.of(
            new ItemStack(Items.IRON_SHOVEL, 1),
            new ItemStack(Items.IRON_PICKAXE, 1),
            new ItemStack(Items.ARROW, 1),
            new ItemStack(Items.BOW, 1),
            new ItemStack(Items.CROSSBOW, 1),
            new ItemStack(Items.MAGMA_CREAM, 1)
    );

    private static final List<ItemStack> epicDropList = List.of(
            new ItemStack(Items.IRON_SWORD, 1),
            new ItemStack(Items.SHIELD, 1),
            new ItemStack(Items.IRON_INGOT, 1),
            new ItemStack(Items.BOOK, 1)
    );

    private static final List<ItemStack> legendaryDropList = List.of(
            new ItemStack(Items.DIAMOND, 1),
            new ItemStack(Items.OBSIDIAN, 1),
            new ItemStack(Items.GOLDEN_APPLE, 1)
    );

    private static final List<ItemStack> guaranteedDropList = List.of(
            new ItemStack(ModItems.ZOMBIE_HEARTH.get(), 1),
            new ItemStack(ModItems.ZOMBIE_BRAIN.get(), 1)
    );

    public CursedZombieEntity(EntityType<? extends Zombie> entity, Level level) {
        super(entity, level);
        setupCursedZombie();
    }

    public CursedZombieEntity(Level level) {
        super(level);
        setupCursedZombie();
    }

    private void setupCursedZombie(){
        this.setHealth(this.getMaxHealth() / 2);
        this.setSpeed(this.getSpeed() / 8);
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource dmgSource, int intVal, boolean boolVal) {
        super.dropCustomDeathLoot(dmgSource, intVal, boolVal);

        getGuaranteedLoot();

        if(getRandomLoot()){
           this.dropExperience();
        }
    }

    private void getGuaranteedLoot(){
        int randomChoice = random.nextInt(2);

        if(randomChoice == 0){
            spawnAtLocation(new ItemStack(ModItems.COIN.get(), 1));
        } else {
            spawnAtLocation(getRandomInList(guaranteedDropList));
        }
    }

    private boolean getRandomLoot() {
        int randomChoice = random.nextInt(100) + 1;

        if (randomChoice <= COMMON) {
            spawnAtLocation(getRandomInList(commonDropList));
            return true;
        } else if (randomChoice <= UNCOMMON) {
            spawnAtLocation(getRandomInList(uncommonDropList));
            return true;
        } else if (randomChoice <= RARE) {
            spawnAtLocation(getRandomInList(rareDropList));
            return true;
        } else if (randomChoice <= EPIC) {
            spawnAtLocation(getRandomInList(epicDropList));
            return true;
        } else if (randomChoice <= LEGENDARY) {
            spawnAtLocation(getRandomInList(legendaryDropList));
            return true;
        }
        return false;
    }

    private ItemStack getRandomInList(List<ItemStack> list) {
        return list.get(random.nextInt(list.size()));
    }
}
