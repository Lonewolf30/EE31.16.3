package com.lonewolf.ee.registry;

import com.lonewolf.ee.reference.Reference;
import com.lonewolf.ee.tile_entity.TileEntityAlchemyArray;
import com.lonewolf.ee.tile_entity.TileEntityEmptyAlchemy;
import com.lonewolf.ee.tile_entity.TileEntityResearchStation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Reference.mod_id)
public class TileEntityRegistry {
    public static TileEntityType<TileEntityResearchStation> researchStationTileEntityType;
    public static TileEntityType<TileEntityEmptyAlchemy> emptyAlchemyTileEntityType;
    public static TileEntityType<TileEntityAlchemyArray> alchemyArrayTileEntityType;

    static {
        researchStationTileEntityType = register("research_station", TileEntityType.Builder
                .create(TileEntityResearchStation::new,
                        BlockRegistry.researchStation));

        emptyAlchemyTileEntityType = register("empty_alchemy.json", TileEntityType.Builder
                .create(TileEntityEmptyAlchemy::new,
                        BlockRegistry.emptyAlchemy));

        alchemyArrayTileEntityType = register("alchemy_controller", TileEntityType.Builder
                .create(TileEntityAlchemyArray::new,
                        BlockRegistry.alchemyController));
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<TileEntityType<?>> evt) {
        IForgeRegistry<TileEntityType<?>> registry = evt.getRegistry();

        registry.register(researchStationTileEntityType);
        registry.register(emptyAlchemyTileEntityType);
        registry.register(alchemyArrayTileEntityType);
    }

    private static <T extends TileEntity> TileEntityType<T> register(String key, TileEntityType.Builder<T> builder) {
        TileEntityType<T> output = builder.build(null);
        output.setRegistryName(Reference.mod_id, key);
        return output;
    }
}
