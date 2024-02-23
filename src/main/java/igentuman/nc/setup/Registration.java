package igentuman.nc.setup;

import com.mojang.serialization.Codec;
import igentuman.nc.effect.RadiationResistance;
import igentuman.nc.multiblock.fusion.FusionReactor;
import igentuman.nc.multiblock.turbine.TurbineRegistration;
import igentuman.nc.recipes.NcRecipeType;
import igentuman.nc.multiblock.fission.FissionReactor;
import igentuman.nc.recipes.NcRecipeSerializers;
import igentuman.nc.setup.registration.*;
import igentuman.nc.world.ore.Generator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

import static igentuman.nc.NuclearCraft.MODID;

public class Registration {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    /*    private static final DeferredRegister<Codec<? extends Biome>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOMES, MODID);
  /*    private static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registry.STRUCT, MODID);*/
    private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, MODID);
    private static final DeferredRegister<Feature<?>> FEATURE_REGISTER = DeferredRegister.create(ForgeRegistries.FEATURES, MODID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);

    public static final RegistryObject<MobEffect> RADIATION_RESISTANCE = EFFECTS.register("radiation_resistance", () -> new RadiationResistance(MobEffectCategory.BENEFICIAL, 0xd4ffFF));

    public static void init() {

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        CONTAINERS.register(bus);
        ENTITIES.register(bus);
       // STRUCTURES.register(bus);
        //BIOME_MODIFIERS.register(bus);
        PLACED_FEATURES.register(bus);
        FEATURE_REGISTER.register(bus);
        CONTAINERS.register(bus);
        SERIALIZERS.register(bus);
        RECIPE_TYPES.register(bus);
        EFFECTS.register(bus);

        NcParticleTypes.PARTICLE_TYPES.register(bus);

        NCBlocks.init();
        NCStorageBlocks.init();
        NCItems.init();
        Fuel.init();
        NCFluids.init();
        NCEnergyBlocks.init();
        NCProcessors.init();
        FissionReactor.init();
        FusionReactor.init();
        TurbineRegistration.init();
        initOreGeneration();

        NcRecipeSerializers.RECIPE_SERIALIZERS.register(bus);
        NcRecipeType.RECIPE_TYPES.register(bus);
        NCSounds.SOUND_EVENTS.register(bus);
    }

    private static void initOreGeneration() {
        ORE_GENERATION = registerOreGenerators();
    }

    public static final BlockBehaviour.Properties BLOCK_PROPERTIES = BlockBehaviour.Properties.of(Material.METAL).strength(2f).requiresCorrectToolForDrops();

    //public static final TagKey<StructureSet> WASTELAND_DIMENSION_STRUCTURE_SET = TagKey.create(Registry.STRUCTURE_SET_REGISTRY, WastelandChunkGenerator.RL_WASTELAND_DIMENSION_SET);

   // public static final RegistryObject<StructureType<?>> LABORATORY = STRUCTURES.register("nc_laboratory", () -> typeConvert(LaboratoryStructure.CODEC));

    // Some common properties for our blocks and items
    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(CreativeTabs.NC_ITEMS);

    public static List<RegistryObject<PlacedFeature>> ORE_GENERATION;

    private static List<RegistryObject<PlacedFeature>> registerOreGenerators()
    {
        List<RegistryObject<PlacedFeature>> temp = new ArrayList<>();
        for(String ore: NCBlocks.ORE_BLOCKS.keySet()) {
            temp.add(PLACED_FEATURES.register("nc_ores_"+ore, () -> Generator.createOregen(ore)));
        }
        return temp;
    }

    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }

/*    private static <S extends Structure> StructureType<S> typeConvert(Codec<S> codec) {
        return () -> codec;
    }*/
}
