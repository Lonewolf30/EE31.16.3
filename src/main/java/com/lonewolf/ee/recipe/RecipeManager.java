package com.lonewolf.ee.recipe;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.lonewolf.ee.exchange.WrappedStack;
import com.lonewolf.ee.util.ComparatorUtils;
import com.lonewolf.ee.util.LogHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;

public class RecipeManager
{
	public static final Marker RECIPE_MARKER;
	private Multimap<WrappedStack, Set<WrappedStack>> recipeMap;
	private ImmutableMultimap<WrappedStack, Set<WrappedStack>> immutableRecipeMap;
	
	public RecipeManager() {
		this.recipeMap = TreeMultimap.create(WrappedStack.COMPARATOR, ComparatorUtils.WRAPPED_STACK_SET_COMPARATOR);
	}
	
	public void addRecipe(WrappedStack recipeOutput, ArrayList<?> recipeInputList) {
		if (recipeOutput != null) {
			Set<WrappedStack> wrappedRecipeInputList = new TreeSet<>();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(recipeOutput);
			for (Object recipeInputObject : recipeInputList)
			{
				WrappedStack wrappedInputObject = WrappedStack.wrap(recipeInputObject);
				if (wrappedInputObject == null)
				{
					return;
				}
				
				wrappedRecipeInputList.add(wrappedInputObject);
				stringBuilder.append(wrappedInputObject);
				stringBuilder.append(" ");
			}
			recipeMap.put(recipeOutput, wrappedRecipeInputList);
		}
	}
	
	public void registerVanillaRecipes(net.minecraft.item.crafting.RecipeManager recipeManager) {
		new RecipesVanilla().registerRecipes(recipeManager);
		new RecipesSmelting().registerRecipes(recipeManager);
//		(new RecipesFluidContainers()).registerRecipes();
//		(new RecipesPotions()).registerRecipes();
		
//		dumpRecipeRegistryToLog();
	}
	
	public Multimap<WrappedStack, Set<WrappedStack>> getRecipeMappings() {
		if (this.immutableRecipeMap == null) {
			this.immutableRecipeMap = ImmutableMultimap.copyOf(this.recipeMap);
		}
		
		return this.immutableRecipeMap;
	}
	
	public void dumpRecipeRegistryToLog() {
		for (WrappedStack wrappedStack : this.getRecipeMappings().keySet())
		{
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(String.format("Output: %s, Inputs: ", wrappedStack.toString()));
			
			for (Set<WrappedStack> wrappedStacks : this.getRecipeMappings().get(wrappedStack))
			{
				for (WrappedStack listStack : wrappedStacks)
				{
					stringBuilder.append(listStack.toString()).append(" ");
				}
			}
			
			LogManager.getLogger().info(RECIPE_MARKER, stringBuilder.toString());
		}
		
	}
	
	static {
		RECIPE_MARKER = MarkerManager.getMarker("EE_RECIPE");
	}
}
