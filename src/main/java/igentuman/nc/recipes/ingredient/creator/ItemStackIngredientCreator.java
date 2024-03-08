package igentuman.nc.recipes.ingredient.creator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import igentuman.nc.network.BasePacketHandler;
import igentuman.nc.recipes.ingredient.IMultiIngredient;
import igentuman.nc.recipes.ingredient.InputIngredient;
import igentuman.nc.recipes.ingredient.ItemStackIngredient;
import igentuman.nc.util.JsonConstants;
import igentuman.nc.util.StackUtils;
import igentuman.nc.util.TagUtil;
import igentuman.nc.util.annotation.NothingNullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import javax.annotation.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@NothingNullByDefault
public class ItemStackIngredientCreator implements IItemStackIngredientCreator {

    public static final ItemStackIngredientCreator INSTANCE = new ItemStackIngredientCreator();

    private ItemStackIngredientCreator() {
    }

    @Override
    public ItemStackIngredient from(Ingredient ingredient, int amount) {
        Objects.requireNonNull(ingredient, "ItemStackIngredients cannot be created from a null ingredient.");
        if (ingredient == Ingredient.EMPTY) {
            //Instance check for empty ingredient, because we could just be empty currently during datagen and want to allow it
            throw new IllegalArgumentException("ItemStackIngredients cannot be created using the empty ingredient.");
        } else if (amount <= 0) {
            throw new IllegalArgumentException("ItemStackIngredients must have an amount of at least one. Received size was: " + amount);
        }
        return new SingleItemStackIngredient(ingredient, amount);
    }

    @Override
    public ItemStackIngredient read(FriendlyByteBuf buffer) {
        Objects.requireNonNull(buffer, "ItemStackIngredients cannot be read from a null packet buffer.");
        return switch (buffer.readEnum(IngredientType.class)) {
            case SINGLE -> from(Ingredient.fromNetwork(buffer), buffer.readVarInt());
            case MULTI -> createMulti(BasePacketHandler.readArray(buffer, ItemStackIngredient[]::new, this::read));
        };
    }

    @Override
    public ItemStackIngredient deserialize(@Nullable JsonElement json) {
        if (json == null || json.isJsonNull()) {
            throw new JsonSyntaxException("Ingredient cannot be null.");
        }
        if (json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();
            int size = jsonArray.size();
            if (size == 0) {
                throw new JsonSyntaxException("Ingredient array cannot be empty, at least one ingredient must be defined.");
            } else if (size > 1) {
                ItemStackIngredient[] ingredients = new ItemStackIngredient[size];
                for (int i = 0; i < size; i++) {
                    ingredients[i] = deserialize(jsonArray.get(i));
                }
                return createMulti(ingredients);
            }
            json = jsonArray.get(0);
        }
        if (!json.isJsonObject()) {
            throw new JsonSyntaxException("Expected item to be object or array of objects.");
        }
        JsonObject jsonObject = json.getAsJsonObject();
        int amount = 1;
        if (jsonObject.has("count")) {
            JsonElement count = jsonObject.get("count");
            if (!GsonHelper.isNumberValue(count)) {
                throw new JsonSyntaxException("Expected amount to be a number that is one or larger.");
            }
            amount = count.getAsJsonPrimitive().getAsInt();
            if (amount < 1) {
                throw new JsonSyntaxException("Expected amount to larger than or equal to one.");
            }
        }

        Ingredient ingredient = Ingredient.fromJson(jsonObject);
        return from(ingredient, amount);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Converts a stream of ingredients into a single ingredient by converting the stream to an array and calling {@link #createMulti(ItemStackIngredient[])}.
     */
    @Override
    public ItemStackIngredient createMulti(ItemStackIngredient... ingredients) {
        Objects.requireNonNull(ingredients, "Cannot create a multi ingredient out of a null array.");
        if (ingredients.length == 0) {
            throw new IllegalArgumentException("Cannot create a multi ingredient out of no ingredients.");
        } else if (ingredients.length == 1) {
            return ingredients[0];
        }
        List<ItemStackIngredient> cleanedIngredients = new ArrayList<>();
        for (ItemStackIngredient ingredient : ingredients) {
            if (ingredient instanceof MultiItemStackIngredient multi) {
                Collections.addAll(cleanedIngredients, multi.ingredients);
            } else {
                cleanedIngredients.add(ingredient);
            }
        }
        return new MultiItemStackIngredient(cleanedIngredients.toArray(new ItemStackIngredient[0]));
    }

    @Override
    public ItemStackIngredient from(Stream<ItemStackIngredient> ingredients) {
        return createMulti(ingredients.toArray(ItemStackIngredient[]::new));
    }

    @NothingNullByDefault
    public static class SingleItemStackIngredient extends ItemStackIngredient {

        private final Ingredient ingredient;
        private final int amount;

        private SingleItemStackIngredient(Ingredient ingredient, int amount) {
            this.ingredient = Objects.requireNonNull(ingredient);
            this.amount = amount;
        }

        @Override
        public boolean test(ItemStack stack) {
            return testType(stack) && stack.getCount() >= amount;
        }

        @Override
        public boolean testType(ItemStack stack) {
            return ingredient.test(stack);
        }

        @Override
        public ItemStack getMatchingInstance(ItemStack stack) {
            return test(stack) ? StackUtils.size(stack, amount) : ItemStack.EMPTY;
        }

        @Override
        public long getNeededAmount(ItemStack stack) {
            return testType(stack) ? amount : 0;
        }

        @Override
        public boolean hasNoMatchingInstances() {
            ItemStack[] items = ingredient.getItems();
            if (items.length == 0) {
                return true;
            } else if (items.length == 1) {
                ItemStack item = items[0];
                if (item.getItem() != Items.BARRIER) return false;
                String contents = (String) item.getHoverName().getContents();
                return contents.startsWith("Empty Tag: ");
            }
            return false;
        }

        public List<String> getItemsByTagKey(String key)
        {
            List<String> tmp = new ArrayList<>();
            Tag.Named<Item> tag = TagUtil.createItemTag(new ResourceLocation(key));
            Ingredient ing = Ingredient.fromValues(Stream.of(new Ingredient.TagValue(tag)));
            for (ItemStack item: ing.getItems()) {
                tmp.add(item.getItem().toString());
            }
            return tmp;
        }

        @Override
        public List<ItemStack> getRepresentations() {
            List<ItemStack> representations = new ArrayList<>();
            for (ItemStack stack : ingredient.getItems()) {
                if (stack.getCount() == amount) {
                    representations.add(stack);
                } else {
                    ItemStack copy = stack.copy();
                    copy.setCount(amount);
                    representations.add(copy);
                }
            }
            return representations;
        }

        /**
         * For use in recipe input caching. Do not use this to modify the backing stack.
         */
        public Ingredient getInputRaw() {
            return ingredient;
        }

        /**
         * For use in CrT comparing.
         */
        public int getAmountRaw() {
            return amount;
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            buffer.writeEnum(IngredientType.SINGLE);
            ingredient.toNetwork(buffer);
            buffer.writeVarInt(amount);
        }

        @Override
        public JsonElement serialize() {
            JsonObject json = new JsonObject();
            if (amount > 1) {
                json.addProperty("count", amount);
            }
            json.add(JsonConstants.INGREDIENT, ingredient.toJson());
            return json;
        }

        @Override
        public String getName() {
            return getRepresentations().get(0).getItem().toString();
        }

        @Override
        public int getAmount() {
            return amount;
        }
    }

    @NothingNullByDefault
    public static class MultiItemStackIngredient extends ItemStackIngredient implements IMultiIngredient<ItemStack, ItemStackIngredient> {

        private final ItemStackIngredient[] ingredients;

        private MultiItemStackIngredient(ItemStackIngredient... ingredients) {
            this.ingredients = ingredients;
        }

        @Override
        public boolean test(ItemStack stack) {
            return Arrays.stream(ingredients).anyMatch(ingredient -> ingredient.test(stack));
        }

        @Override
        public String getName() {
            return getRepresentations().get(0).getItem().toString();
        }

        @Override
        public int getAmount() {
            return getRepresentations().get(0).getCount();
        }

        @Override
        public boolean testType(ItemStack stack) {
            return Arrays.stream(ingredients).anyMatch(ingredient -> ingredient.testType(stack));
        }

        @Override
        public ItemStack getMatchingInstance(ItemStack stack) {
            for (ItemStackIngredient ingredient : ingredients) {
                ItemStack matchingInstance = ingredient.getMatchingInstance(stack);
                if (!matchingInstance.isEmpty()) {
                    return matchingInstance;
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public long getNeededAmount(ItemStack stack) {
            for (ItemStackIngredient ingredient : ingredients) {
                long amount = ingredient.getNeededAmount(stack);
                if (amount > 0) {
                    return amount;
                }
            }
            return 0;
        }

        @Override
        public boolean hasNoMatchingInstances() {
            return Arrays.stream(ingredients).allMatch(InputIngredient::hasNoMatchingInstances);
        }

        @Override
        public List<ItemStack> getRepresentations() {
            List<ItemStack> representations = new ArrayList<>();
            for (ItemStackIngredient ingredient : ingredients) {
                representations.addAll(ingredient.getRepresentations());
            }
            return representations;
        }

        @Override
        public boolean forEachIngredient(Predicate<ItemStackIngredient> checker) {
            boolean result = false;
            for (ItemStackIngredient ingredient : ingredients) {
                result |= checker.test(ingredient);
            }
            return result;
        }

        @Override
        public final List<ItemStackIngredient> getIngredients() {
            return List.of(ingredients);
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            buffer.writeEnum(IngredientType.MULTI);
            BasePacketHandler.writeArray(buffer, ingredients, InputIngredient::write);
        }

        @Override
        public JsonElement serialize() {
            JsonArray json = new JsonArray();
            for (ItemStackIngredient ingredient : ingredients) {
                json.add(ingredient.serialize());
            }
            return json;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o == null || getClass() != o.getClass()) {
                return false;
            }
            return Arrays.equals(ingredients, ((MultiItemStackIngredient) o).ingredients);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(ingredients);
        }
    }

    private enum IngredientType {
        SINGLE,
        MULTI
    }
}