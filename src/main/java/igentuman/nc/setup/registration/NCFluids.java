package igentuman.nc.setup.registration;

import com.google.common.collect.ImmutableList;
import igentuman.nc.content.materials.Materials;
import igentuman.nc.fluid.AcidDefinition;
import igentuman.nc.fluid.GasDefinition;
import igentuman.nc.fluid.LiquidDefinition;
import igentuman.nc.fluid.NCFluid;
import igentuman.nc.block.NCFluidBlock;
import igentuman.nc.content.fuel.FuelManager;
import igentuman.nc.util.TagUtil;
import igentuman.nc.util.TextureUtil;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fmllegacy.RegistryObject;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import javax.annotation.Nullable;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static igentuman.nc.NuclearCraft.MODID;
import static igentuman.nc.NuclearCraft.rl;
import static igentuman.nc.content.materials.Materials.slurries;
import static igentuman.nc.util.ModUtil.isMekanismLoadeed;
import static net.minecraft.sounds.SoundEvents.BUCKET_EMPTY;
import static net.minecraft.sounds.SoundEvents.BUCKET_FILL;

public class NCFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);

    public static final HashMap<String, FluidEntry> ALL_FLUID_ENTRIES = new HashMap<>();
    public static final Set<NCBlocks.BlockEntry<? extends LiquidBlock>> ALL_FLUID_BLOCKS = new HashSet<>();
    public static HashMap<String, FluidEntry> NC_MATERIALS = new HashMap<>();
    public static HashMap<String, FluidEntry> NC_GASES = new HashMap<>();

    public static HashMap<String, Tag.Named<Fluid>> GASES_TAG = new HashMap<>();
    public static HashMap<String, Tag.Named<Fluid>> LIQUIDS_TAG = new HashMap<>();
    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        FLUIDS.register(bus);
        materialFluids();
        gases();
        fuel();
        isotopes();
        acids();
        liquidGases();
        liquids();
        slurryFluids();
    }

    public static BlockState getBlock(String name)
    {
        if(NC_MATERIALS.containsKey(name)) {
            return NC_MATERIALS.get(name).getBlock().defaultBlockState();
        }
        return Blocks.AIR.defaultBlockState();
    }

    private static void liquids() {
        HashMap<String, LiquidDefinition> items = new HashMap<>();
        if(isMekanismLoadeed()) {
            items.put("spent_nuclear_waste", new LiquidDefinition("spent_nuclear_waste", 0X901F1B14));
            items.put("nuclear_waste", new LiquidDefinition("nuclear_waste", 0X903D3323));
            items.put("fissile_fuel", new LiquidDefinition("fissile_fuel", 0X903D3323));
        }
        items.put("irradiated_boron", new LiquidDefinition("irradiated_boron", 0XFF6F896C, 800));
        items.put("irradiated_lithium", new LiquidDefinition("irradiated_lithium", 0XFFBCC661, 800));
        items.put("uranium_oxide", new LiquidDefinition("uranium_oxide", 0X90A9B544));
        items.put("cryotheum", new LiquidDefinition("cryotheum", 0X9089EDFF));
        items.put("radaway", new LiquidDefinition("radaway", 0x50B37AC4));
        items.put("ethanol", new LiquidDefinition("ethanol", 0x50B37AC4));
        items.put("methanol", new LiquidDefinition("methanol", 0x50B37AC4));
        items.put("hydrogen_chloride", new LiquidDefinition("hydrogen_chloride", 0x50B37AC4));
        items.put("lithium_fluoride", new LiquidDefinition("lithium_fluoride", 0x50B37AC4));
        items.put("beryllium_fluoride", new LiquidDefinition("beryllium_fluoride", 0x50B37AC4));
        items.put("radaway_slow", new LiquidDefinition("radaway_slow", 0x50A0EFFF));
        items.put("redstone_ethanol", new LiquidDefinition("redstone_ethanol", 0x507E8CC8));
        items.put("boron_nitride_solution", new LiquidDefinition("boron_nitride_solution", 0x506F8E5C));
        items.put("boron_arsenide_solution", new LiquidDefinition("boron_arsenide_solution", 0x506F8E5C));
        items.put("fluorite_water", new LiquidDefinition("fluorite_water", 0x508AB492));
        items.put("calcium_sulfate_solution", new LiquidDefinition("calcium_sulfate_solution", 0x50B8B0A6));
        items.put("sodium_fluoride_solution", new LiquidDefinition("sodium_fluoride_solution", 0x50C2B1A1));
        items.put("potassium_fluoride_solution", new LiquidDefinition("potassium_fluoride_solution", 0x50C1C99D));
        items.put("sodium_hydroxide_solution", new LiquidDefinition("sodium_hydroxide_solution", 0x50C2B7BB));
        items.put("potassium_hydroxide_solution", new LiquidDefinition("potassium_hydroxide_solution", 0x50B8C6B0));
        items.put("borax_solution", new LiquidDefinition("borax_solution", 0x50EEEEEE));
        items.put("irradiated_borax_solution", new LiquidDefinition("irradiated_borax_solution", 0x90FFD0A3));
        items.put("ice", new LiquidDefinition("ice", 0x90AFF1FF));
        items.put("slurry_ice", new LiquidDefinition("slurry_ice", 0x907EAEB7));
        items.put("heavy_water", new LiquidDefinition("heavy_water", 0x807EAEB7));
        items.put("chocolate_liquor", new LiquidDefinition("chocolate_liquor", 0xFF41241C));
        items.put("cocoa_butter", new LiquidDefinition("cocoa_butter", 0xFFF6EEBF));
        items.put("unsweetened_chocolate", new LiquidDefinition("unsweetened_chocolate", 0xFF2C0A08));
        items.put("dark_chocolate", new LiquidDefinition("dark_chocolate", 0xFF2C0B06));
        items.put("milk_chocolate", new LiquidDefinition("milk_chocolate", 0xFF884121));
        items.put("sugar", new LiquidDefinition("sugar", 0x50FFD59A));
        items.put("gelatin", new LiquidDefinition("gelatin", 0x50DDD09C));
        items.put("hydrated_gelatin", new LiquidDefinition("hydrated_gelatin", 0x50DDD09C));
        items.put("marshmallow", new LiquidDefinition("marshmallow", 0x90E1E1E3));
        items.put("pasteurized_milk", new LiquidDefinition("pasteurized_milk", 0xFFFFF2F2));
        items.put("technical_water", new LiquidDefinition("technical_water", 0x902F43F4));
        items.put("condensate_water", new LiquidDefinition("condensate_water", 0x902F43F4));
        items.put("emergency_coolant", new LiquidDefinition("emergency_coolant", 0x906DD0E7));
        items.put("emergency_coolant_heated", new LiquidDefinition("emergency_coolant_heated", 0x90CDBEE7));

        for(LiquidDefinition liquid: items.values()) {
            LIQUIDS_TAG.put(liquid.name, TagUtil.createFluidTagKey(liquid.name));
            NC_MATERIALS.put(liquid.name, FluidEntry.makeLiquid(liquid.name, liquid.color));

        }
    }

    private static void liquidGases() {
        HashMap<String, LiquidDefinition> items = new HashMap<>();
        items.put("liquid_hydrogen", new LiquidDefinition("liquid_hydrogen", 0x50B37AC4));
        items.put("liquid_helium", new LiquidDefinition("liquid_helium", 0x50A0EFFF));
        items.put("liquid_oxygen", new LiquidDefinition("liquid_oxygen", 0x507E8CC8));
        items.put("liquid_nitrogen", new LiquidDefinition("liquid_nitrogen", 0x5031C23A));

        for(LiquidDefinition liquid: items.values()) {
            LIQUIDS_TAG.put(liquid.name, TagUtil.createFluidTagKey( liquid.name));
            NC_MATERIALS.put(liquid.name, FluidEntry.makeLiquid(liquid.name, liquid.color));
        }
    }

    private static void slurryFluids() {
        HashMap<String, AcidDefinition> items = new HashMap<>();
        int id = 0;
        for(String material: slurries()) {
            int color;
            if(FMLEnvironment.dist.isClient()) {
                color = TextureUtil.getAverageColor("textures/block/ore/" + material + "_ore.png");
            } else {
                color = TextureUtil.getAverageColorServer("textures/block/ore/" + material + "_ore.png");
            }
            int[] rgba = TextureUtil.intToRgba(color);
            if(rgba[0] == 0 && rgba[1] == 0 && rgba[2] == 0) {
                Random rand = new Random(material.length()+id);
                rgba = new int[]{rand.nextInt(id+254), rand.nextInt(id+255), rand.nextInt(id+253), 255};
            }
            rgba[3] = 0xFE;
            items.put(material+"_slurry", new AcidDefinition(material+"_slurry", TextureUtil.rgbaToInt(rgba)));
            rgba[3] = 0xDD;
            items.put(material+"_clean_slurry", new AcidDefinition(material+"_clean_slurry", TextureUtil.rgbaToInt(rgba)));
        }
        for(AcidDefinition acid: items.values()) {
            LIQUIDS_TAG.put(acid.name, TagUtil.createFluidTagKey( acid.name));
            NC_MATERIALS.put(acid.name, FluidEntry.makeAcid(acid));
        }
    }

    private static void acids() {
        HashMap<String, AcidDefinition> items = new HashMap<>();
        items.put("hydrofluoric_acid", new AcidDefinition("hydrofluoric_acid", 0xCCFFEE99));
        items.put("hydrochloric_acid", new AcidDefinition("hydrochloric_acid", 0xBBEEEEFF));
        items.put("boric_acid", new AcidDefinition("boric_acid", 0xCCA0EFFF));
        items.put("sulfuric_acid", new AcidDefinition("sulfuric_acid", 0xCCF8FFD3));
        items.put("nitric_acid", new AcidDefinition("nitric_acid", 0xCC4F9EFF));
        items.put("aqua_regia_acid", new AcidDefinition("aqua_regia_acid", 0XCCFFBB99));

        for(AcidDefinition acid: items.values()) {
            LIQUIDS_TAG.put(acid.name, TagUtil.createFluidTagKey( acid.name));
            NC_MATERIALS.put(acid.name, FluidEntry.makeAcid(acid));

        }
    }

    private static void materialFluids() {
        for (String name: Materials.fluids().keySet()) {
            LIQUIDS_TAG.put(name, TagUtil.createFluidTagKey( name));
            NC_MATERIALS.put(name, FluidEntry.makeMoltenLiquid(name, Materials.fluids().get(name).color));
        }
    }

    private static void fuel() {
        for (String name: FuelManager.all().keySet()) {
            for(String subType: FuelManager.all().get(name).keySet()) {
                for(String type: new String[]{"", "_za", "_ox","_ni"}) {
                    String key = "fuel_"+name +"_"+ subType+type;
                    if (NC_MATERIALS.containsKey(key)) continue;
                    int colorDepleted = 0xFFCCCCCC;
                    int colorFuel = 0xFFCCCCCC;
                    if(FMLEnvironment.dist.isClient()) {
                        colorDepleted = TextureUtil.getAverageColor("textures/item/fuel/" + name + "/depleted/" + subType.replace("-", "_") + type + ".png");
                        colorFuel = TextureUtil.getAverageColor("textures/item/fuel/" + name + "/" + subType.replace("-", "_") + type + ".png");
                    }
                    NC_MATERIALS.put(key,
                            FluidEntry.makeMoltenLiquid(key.replace("-","_"),
                                    colorFuel));
                    LIQUIDS_TAG.put(key, TagUtil.createFluidTagKey( key.replace("-","_")));
                    NC_MATERIALS.put("depleted_"+key,
                            FluidEntry.makeMoltenLiquid("depleted_"+key.replace("-","_"), colorDepleted));
                    LIQUIDS_TAG.put("depleted_"+key, TagUtil.createFluidTagKey( "depleted_"+key.replace("-","_")));
                }
            }
        }
    }


    private static void gases() {
        HashMap<String, GasDefinition> items = new HashMap<>();

        items.put("steam", new GasDefinition("steam", 0xCC929292));
        items.put("high_pressure_steam", new GasDefinition("high_pressure_steam", 0xCCBDBDBD));
        items.put("exhaust_steam", new GasDefinition("exhaust_steam", 0xCC7E7E7E));
        items.put("low_pressure_steam", new GasDefinition("low_pressure_steam", 0xCCA8A8A8));
        items.put("low_quality_steam", new GasDefinition("low_quality_steam", 0xCC828282));
        items.put("argon", new GasDefinition("argon", 0xCCFF75DD));
        items.put("neon", new GasDefinition("neon", 0xCCFF9F7A));
        items.put("chlorine", new GasDefinition("chlorine", 0xCCFFFF8F));
        items.put("nitric_oxide", new GasDefinition("nitric_oxide", 0xCCC9EEFF));
        items.put("nitrogen_dioxide", new GasDefinition("nitrogen_dioxide", 0xCC782A10));
        items.put("hydrogen", new GasDefinition("hydrogen", 0xCCA0EFFF));
        items.put("helium", new GasDefinition("helium", 0xCCC57B81));
        items.put("helium_3", new GasDefinition("helium_3", 0xCCCBBB67));
        items.put("tritium", new GasDefinition("tritium", 0xCC5DBBD6));
        items.put("deuterium", new GasDefinition("deuterium", 0xCC9E6FEF));
        items.put("oxygen", new GasDefinition("oxygen", 0xCC7E8CC8));
        items.put("nitrogen", new GasDefinition("nitrogen", 0xCC7CC37B));
        items.put("fluorine", new GasDefinition("fluorine", 0xCCD3C75D));
        items.put("carbon", new GasDefinition("carbon", 0xCC5C635A));
        items.put("carbon_dioxide", new GasDefinition("carbon_dioxide", 0xCC5C635A));
        items.put("carbon_monoxide", new GasDefinition("carbon_monoxide", 0xCC4C5649));
        items.put("ethene", new GasDefinition("ethene", 0xCCFFE4A3));
        items.put("fluoromethane", new GasDefinition("fluoromethane", 0xCC424C05));
        items.put("ammonia", new GasDefinition("ammonia", 0xCC7AC3A0));
        items.put("oxygen_difluoride", new GasDefinition("oxygen_difluoride", 0xCCEA1B01));
        items.put("diborane", new GasDefinition("diborane", 0xCCCC6E8C));
        items.put("sulfur_dioxide", new GasDefinition("sulfur_dioxide", 0xCCC3BC7A));
        items.put("sulfur_trioxide", new GasDefinition("sulfur_trioxide", 0xCCD3AE5D));
        items.put("radon", new GasDefinition("radon", 0xFFFFFFFF));
        for(GasDefinition gas: items.values()) {
            LIQUIDS_TAG.put(gas.name, TagUtil.createFluidTagKey( gas.name));
            GASES_TAG.put(gas.name, TagUtil.createFluidTagKey( "gases/"+gas.name));
            NC_GASES.put(gas.name, FluidEntry.makeGas(gas));
        }
    }

    private static void isotopes()
    {
        for(String name: Materials.isotopes()) {
            for(String type: new String[]{"", "_za", "_ox","_ni"}) {
                if(NC_MATERIALS.containsKey(name+type)) continue;
                int color = 0xFFCCCCCC;
                if(FMLEnvironment.dist.isClient()) {
                    color = TextureUtil.getAverageColor("textures/item/material/isotope/" + name + type + ".png");
                }
                NC_MATERIALS.put(name+type,
                        FluidEntry.makeMoltenLiquid(name.replace("/", "_")+type,color));
                LIQUIDS_TAG.put(name+type, TagUtil.createFluidTagKey( name+type));

            }
        }
    }
    public static Consumer<FluidAttributes.Builder> meltBuilder(int temperature, int color)
    {
        int light = 1;
        int density = 2000;
        int visconsity = 3000;
        return builder -> builder.overlay(rl("block/material/fluid/molten_still"))
                .temperature(temperature).density(density).viscosity(visconsity).luminosity(light).color(color);
    }

    public static Consumer<FluidAttributes.Builder> liquidBuilder(int temperature, int color)
    {
        int density = 400;
        int visconsity = 1000;
        return builder -> builder.overlay(rl("block/material/fluid/liquid_still")).temperature(temperature).density(density).viscosity(visconsity).color(color);
    }

    public static Consumer<FluidAttributes.Builder> gasBuilder(int temperature, int color)
    {
        int density = -1000;
        int visconsity = 0;
        return builder -> builder.temperature(temperature).gaseous().density(density).viscosity(visconsity).color(color);
    }
    public record FluidEntry(
            RegistryObject<NCFluid> flowing,
            RegistryObject<NCFluid> still,
            NCBlocks.BlockEntry<NCFluidBlock> block,
            RegistryObject<BucketItem> bucket,
            List<Property<?>> properties,
            int color
    )
    {

        public static FluidEntry makeAcid(AcidDefinition acid) {
            return make(acid.name,0, rl("block/material/fluid/liquid_still"), rl("block/material/fluid/liquid_flow"), liquidBuilder(acid.temperature, acid.color), acid.color, false);
        }

        public static FluidEntry makeGas(GasDefinition gas) {
            return make(gas.name,0, rl("block/material/fluid/gas"), rl("block/material/fluid/gas"), gasBuilder(gas.temperature, gas.color), gas.color, true);
        }

        private static FluidEntry makeMoltenLiquid(String name, int color)
        {
            return make(name,0, rl("block/material/fluid/molten_still"), rl("block/material/fluid/molten_flow"), meltBuilder(1000, color), color, false);
        }
        private static FluidEntry makeLiquid(String name, int color)
        {
            return make(name,0, rl("block/material/fluid/liquid_still"), rl("block/material/fluid/liquid_flow"), liquidBuilder(400, color), color, false);
        }

        private static FluidEntry make(String name, ResourceLocation stillTex, ResourceLocation flowingTex, int color)
        {
            return make(name, 0, stillTex, flowingTex, color);
        }

        private static FluidEntry make(String name, int burnTime, ResourceLocation stillTex, ResourceLocation flowingTex, int color)
        {
            return make(name, burnTime, stillTex, flowingTex, null, color, false);
        }

        private static FluidEntry make(
                String name, int burnTime,
                ResourceLocation stillTex, ResourceLocation flowingTex,
                @Nullable Consumer<FluidAttributes.Builder> buildAttributes,
                int color,
                boolean isGas
        )
        {
            return make(
                    name, burnTime, stillTex, flowingTex, NCFluid::new, NCFluid.Flowing::new, buildAttributes,
                    ImmutableList.of(), color, isGas
            );
        }

        private static FluidEntry make(
                String name, ResourceLocation stillTex, ResourceLocation flowingTex,
                NCFluid.FluidConstructor makeStill, NCFluid.FluidConstructor makeFlowing,
                @Nullable Consumer<FluidAttributes.Builder> buildAttributes, ImmutableList<Property<?>> properties,
                int color
        )
        {
            return make(name, 0, stillTex, flowingTex, makeStill, makeFlowing, buildAttributes, properties, color, false);
        }

        private static FluidEntry make(
                String name, int burnTime,
                ResourceLocation stillTex, ResourceLocation flowingTex,
                NCFluid.FluidConstructor makeStill, NCFluid.FluidConstructor makeFlowing,
                @Nullable Consumer<FluidAttributes.Builder> buildAttributes, List<Property<?>> properties, int color, boolean isGas
        )
        {
            FluidAttributes.Builder builder = FluidAttributes.builder(stillTex, flowingTex);
            if(isGas || name.contains("acid")) {
                builder
                        .sound(BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH)
                        .sound(BUCKET_FILL, BUCKET_FILL);
            }
            builder.color(color);

            if(buildAttributes!=null)
                buildAttributes.accept(builder);

            Mutable<FluidEntry> thisMutable = new MutableObject<>();
            RegistryObject<NCFluid> still = FLUIDS.register(name, () -> NCFluid.makeFluid(
                    makeStill, thisMutable.getValue(), stillTex, flowingTex, buildAttributes
            ));

            RegistryObject<NCFluid> flowing = FLUIDS.register(name+"_flowing", () -> NCFluid.makeFluid(
                    makeFlowing, thisMutable.getValue(), stillTex, flowingTex, buildAttributes
            ));

            NCBlocks.BlockEntry<NCFluidBlock> block = new NCBlocks.BlockEntry<>(
                    name+"_fluid_block",
                    () -> BlockBehaviour.Properties.copy(Blocks.WATER).noCollission(),
                    p -> new NCFluidBlock(thisMutable.getValue(), p)
            );
            RegistryObject<BucketItem> bucket = NCItems.ITEMS.register(name+"_bucket", () -> makeBucket(still, burnTime));
            FluidEntry entry = new FluidEntry(flowing, still, block, bucket, properties, color);
            thisMutable.setValue(entry);
            ALL_FLUID_BLOCKS.add(block);
            ALL_FLUID_ENTRIES.put(name, entry);
            return entry;
        }



        public NCFluid getFlowing()
        {
            return flowing.get();
        }

        public NCFluid getStill()
        {
            return still.get();
        }

        public NCFluidBlock getBlock()
        {
            return block.get();
        }

        public BucketItem getBucket()
        {
            return bucket.get();
        }

        private static BucketItem makeBucket(RegistryObject<NCFluid> still, int burnTime)
        {
            BucketItem result = new BucketItem(
                    still, new Item.Properties()
                    .stacksTo(1)
                    .tab(CreativeTabs.NC_ITEMS)
                    .craftRemainder(Items.BUCKET))
            {
                @Override
                public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt)
                {
                    return new FluidBucketWrapper(stack);
                }

                @Override
                public int getBurnTime(ItemStack itemStack, RecipeType<?> type)
                {
                    return burnTime;
                }
            };
            return result;
        }

        public RegistryObject<NCFluid> getStillGetter()
        {
            return still;
        }
    }
}
