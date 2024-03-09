package com.zombie.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;

public class DemonZombieEntity extends ZombieEntity {
    public DemonZombieEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public DemonZombieEntity(World world) {
        super(world);
    }

    @Override
    public boolean burnsInDaylight(){
        return false;
    }
}
