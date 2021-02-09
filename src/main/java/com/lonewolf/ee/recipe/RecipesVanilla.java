package com.lonewolf.ee.recipe;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.exchange.OreStack;
import com.lonewolf.ee.exchange.WrappedStack;
import com.lonewolf.ee.util.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;
import java.util.stream.Collectors;

public class RecipesVanilla
{
	private final Marker vanillaRecipeMarker = MarkerManager.getMarker("Vanilla Crafting Recipe");
	
	public void registerRecipes(RecipeManager recipeManager)
	{
		List<ICraftingRecipe> recipes = recipeManager.getRecipesForType(IRecipeType.CRAFTING);
		
		for (ICraftingRecipe recipe : recipes)
		{
			if (recipe.isDynamic()) continue;
			
			WrappedStack itemStack = WrappedStack.wrap(new ItemStack(ForgeRegistries.ITEMS.getValue(recipe.getRecipeOutput().getItem().getRegistryName())),
			                                           recipe.getRecipeOutput().getCount());
			
			ArrayList<Object> ingredient = new ArrayList<>();
			HashMap<Ingredient, Integer> hm = new HashMap<>();
			for (Ingredient recipeIngredient : recipe.getIngredients())
			{
				Integer amount = hm.get(recipeIngredient);
				hm.put(recipeIngredient, (amount == null) ? 1 : amount + 1);
			}
			
			for (Map.Entry<Ingredient, Integer> entry : hm.entrySet())
			{
				Ingredient ingredient1 = entry.getKey();
				Integer integer = entry.getValue();
				ItemStack[] possibleItems = ingredient1.getMatchingStacks();
				
				if (possibleItems.length > 1)
				{
					ArrayList<String> tags = new ArrayList<>();
					for (ItemStack stack : possibleItems)
					{
						Collection<ResourceLocation> resourceLocations = ItemTags.getCollection().getOwningTags(
								stack.getItem());
						resourceLocations.forEach(resourceLocation -> tags.add(resourceLocation.getPath()));
					}
					
					if (tags.size() == 0)
					{
						continue;
					}
					
					String mostCommon = getCommonTag(tags);
					OreStack oreStack = new OreStack(mostCommon, integer);
					ingredient.add(WrappedStack.wrap(oreStack, integer));
				}
				else if (possibleItems.length == 1)
				{
					ItemStack itemStack1 = possibleItems[0];
					itemStack1.setCount(integer);
					ingredient.add(WrappedStack.wrap(itemStack1, integer));
				}
				
			}
			
			EquivalentExchange.getRecipeManager().addRecipe(itemStack, ingredient);
		}
	}
	
	
	private String getCommonTag(ArrayList<String> strings)
	{
		return strings.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting())).entrySet().stream().max(
				Map.Entry.comparingByValue()).get().getKey();
	}
}
