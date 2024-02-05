package igentuman.nc.datagen.blockstates;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import igentuman.nc.datagen.models.ModelProviderUtil;
import igentuman.nc.datagen.models.NongeneratedModels;
import igentuman.nc.util.DataGenUtil;
import igentuman.nc.util.NCProperties;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static igentuman.nc.NuclearCraft.MODID;

public abstract class ExtendedBlockstateProvider extends BlockStateProvider
{
	protected static final List<Vec3i> COLUMN_THREE = ImmutableList.of(BlockPos.ZERO, BlockPos.ZERO.above(), BlockPos.ZERO.above(2));

	protected static final Map<ResourceLocation, String> generatedParticleTextures = new HashMap<>();
	protected final ExistingFileHelper existingFileHelper;
	protected final NongeneratedModels innerModels;

	public ExtendedBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
	{
		super(gen, MODID, exFileHelper);
		this.existingFileHelper = exFileHelper;
		this.innerModels = new NongeneratedModels(gen, existingFileHelper);
	}

	protected String name(Supplier<? extends Block> b)
	{
		return name(b.get());
	}

	protected String name(Block b)
	{
		return Registry.BLOCK.getKey(b).getPath();
	}

	public void simpleBlockAndItem(Supplier<? extends Block> b, ModelFile model)
	{
		simpleBlockAndItem(b, new ConfiguredModel(model));
	}

	protected void simpleBlockAndItem(Supplier<? extends Block> b, ConfiguredModel model)
	{
		simpleBlock(b.get(), model);
		itemModel(b, model.model);
	}

	protected void cubeSideVertical(Supplier<? extends Block> b, ResourceLocation side, ResourceLocation vertical)
	{
		simpleBlockAndItem(b, models().cubeBottomTop(name(b), side, vertical, vertical));
	}

	protected void cubeAll(Supplier<? extends Block> b, ResourceLocation texture)
	{
		cubeAll(b, texture, null);
	}

	protected void cubeAll(Supplier<? extends Block> b, ResourceLocation texture, @Nullable RenderType layer)
	{
		final BlockModelBuilder model = models().cubeAll(name(b), texture);
		setRenderType(layer, model);
		simpleBlockAndItem(b, model);
	}

	protected void setRenderType(@Nullable RenderType type, ModelBuilder<?>... builders)
	{
		if(type!=null)
		{
			final String typeName = ModelProviderUtil.getName(type);
			//for(final ModelBuilder<?> model : builders)
				//model.renderType(typeName);
		}
	}

	protected ResourceLocation forgeLoc(String path)
	{
		return new ResourceLocation("forge", path);
	}

	protected ResourceLocation addModelsPrefix(ResourceLocation in)
	{
		return new ResourceLocation(in.getNamespace(), "models/"+in.getPath());
	}

	protected void itemModel(Supplier<? extends Block> block, ModelFile model)
	{
		itemModels().getBuilder(name(block)).parent(model);
	}

	protected NongeneratedModels.NongeneratedModel innerObj(String loc, @Nullable RenderType layer)
	{
		Preconditions.checkArgument(loc.endsWith(".obj"));
		final var result = obj(loc.substring(0, loc.length()-4), modLoc(loc), innerModels);
		setRenderType(layer, result);
		return result;
	}

	protected NongeneratedModels.NongeneratedModel innerObj(String loc)
	{
		return innerObj(loc, null);
	}

	protected BlockModelBuilder obj(String loc)
	{
		return obj(loc, (RenderType)null);
	}

	protected BlockModelBuilder obj(String loc, @Nullable RenderType layer)
	{
		final var model = obj(loc, models());
		setRenderType(layer, model);
		return model;
	}

	protected <T extends ModelBuilder<T>>
	T obj(String loc, ModelProvider<T> modelProvider)
	{
		Preconditions.checkArgument(loc.endsWith(".obj"));
		return obj(loc.substring(0, loc.length()-4), modLoc(loc), modelProvider);
	}

	protected <T extends ModelBuilder<T>>
	T obj(String name, ResourceLocation model, ModelProvider<T> provider)
	{
		return obj(name, model, ImmutableMap.of(), provider);
	}

	protected <T extends ModelBuilder<T>>
	T obj(String name, ResourceLocation model, Map<String, ResourceLocation> textures, ModelProvider<T> provider)
	{
		return obj(provider.withExistingParent(name, mcLoc("block")), model, textures);
	}

	protected <T extends ModelBuilder<T>>
	T obj(T base, ResourceLocation model, Map<String, ResourceLocation> textures)
	{
		assertModelExists(model);
		T ret = base;
				//.customLoader(ModelLoad::begin)
				//.automaticCulling(false)
				//.modelLocation(addModelsPrefix(model))
				//.flipV(true)
			//	.end();
		String particleTex = DataGenUtil.getTextureFromObj(model, existingFileHelper);
		if(particleTex.charAt(0)=='#')
			particleTex = textures.get(particleTex.substring(1)).toString();
		ret.texture("particle", particleTex);
		generatedParticleTextures.put(ret.getLocation(), particleTex);
		for(Entry<String, ResourceLocation> e : textures.entrySet())
			ret.texture(e.getKey(), e.getValue());
		return ret;
	}

	protected void addParticleTextureFrom(BlockModelBuilder result, ModelFile model)
	{
		String particles = generatedParticleTextures.get(model.getLocation());
		if(particles!=null)
		{
			result.texture("particle", particles);
			generatedParticleTextures.put(result.getLocation(), particles);
		}
	}

	protected ConfiguredModel emptyWithParticles(String name, String particleTexture)
	{
		ModelFile model = models().withExistingParent(name, modLoc("block/ie_empty"))
				.texture("particle", particleTexture);
		generatedParticleTextures.put(modLoc(name), particleTexture);
		return new ConfiguredModel(model);
	}

	public void assertModelExists(ResourceLocation name)
	{
		String suffix = name.getPath().contains(".")?"": ".json";
		Preconditions.checkState(
				existingFileHelper.exists(name, PackType.CLIENT_RESOURCES, suffix, "models"),
				"Model \""+name+"\" does not exist");
	}

	protected int getAngle(Direction dir, int offset)
	{
		return (int)((dir.toYRot()+offset)%360);
	}

	protected void createHorizontalRotatedBlock(Supplier<? extends Block> block, ModelFile model)
	{
		createHorizontalRotatedBlock(block, $ -> model, List.of());
	}

	protected void createHorizontalRotatedBlock(Supplier<? extends Block> block, ModelFile model, int offsetRotY) {
		createRotatedBlock(block, $ -> model, NCProperties.FACING_HORIZONTAL, List.of(), 0, offsetRotY);
	}

	protected void createHorizontalRotatedBlock(Supplier<? extends Block> block, Function<PartialBlockstate, ModelFile> model, List<Property<?>> additionalProps)
	{
		createRotatedBlock(block, model, NCProperties.FACING_HORIZONTAL, additionalProps, 0, 180);
	}

	protected void createAllRotatedBlock(Supplier<? extends Block> block, ModelFile model)
	{
		createAllRotatedBlock(block, $ -> model, List.of());
	}

	protected void createAllRotatedBlock(Supplier<? extends Block> block, Function<PartialBlockstate, ModelFile> model, List<Property<?>> additionalProps)
	{
		createRotatedBlock(block, model, NCProperties.FACING_ALL, additionalProps, 90, 0);
	}

	protected void createRotatedBlock(Supplier<? extends Block> block, ModelFile model, Property<Direction> facing,
									  List<Property<?>> additionalProps, int offsetRotX, int offsetRotY)
	{
		createRotatedBlock(block, $ -> model, facing, additionalProps, offsetRotX, offsetRotY);
	}

	protected void createRotatedBlock(Supplier<? extends Block> block, Function<PartialBlockstate, ModelFile> model, Property<Direction> facing,
									  List<Property<?>> additionalProps, int offsetRotX, int offsetRotY)
	{
		VariantBlockStateBuilder stateBuilder = getVariantBuilder(block.get());
		forEachState(stateBuilder.partialState(), additionalProps, state -> {
			ModelFile modelLoc = model.apply(state);
			for(Direction d : facing.getPossibleValues())
			{
				int x;
				int y;
				switch(d)
				{
					case UP -> {
						x = 90;
						y = 0;
					}
					case DOWN -> {
						x = -90;
						y = 0;
					}
					default -> {
						y = getAngle(d, offsetRotY);
						x = 0;
					}
				}
				state.with(facing, d).setModels(new ConfiguredModel(modelLoc, x+offsetRotX, y, false));
			}
		});
	}

	protected static String getName(RenderStateShard state)
	{
		//TODO clean up/speed up
		try
		{
			// Datagen should only ever run in a deobf environment, so no need to use unreadable SRG names here
			// This is a workaround for the fact that client-side Mixins are not applied in datagen
			Field f = RenderStateShard.class.getDeclaredField("name");
			f.setAccessible(true);
			return (String)f.get(state);
		} catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static <T extends Comparable<T>> void forEach(PartialBlockstate base, Property<T> prop,
														 List<Property<?>> remaining, Consumer<PartialBlockstate> out)
	{
		for(T value : prop.getPossibleValues())
			forEachState(base, remaining, map -> {
				map = map.with(prop, value);
				out.accept(map);
			});
	}

	public static void forEachState(PartialBlockstate base, List<Property<?>> props, Consumer<PartialBlockstate> out)
	{
		if(props.size() > 0)
		{
			List<Property<?>> remaining = props.subList(1, props.size());
			Property<?> main = props.get(0);
			forEach(base, main, remaining, out);
		}
		else
			out.accept(base);
	}
}
