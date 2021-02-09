package com.lonewolf.ee.alchemy;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Set;

public class ArrayManager
{
    private HashMap<ResourceLocation, AlchemyArray> arrays;

    public ArrayManager()
    {
        arrays = new HashMap<>();
    }

    public Set<ResourceLocation> getKeys()
    {
        return arrays.keySet();
    }

    public void register(ResourceLocation resourceLocation, AlchemyArray alchemyArray)
    {
        arrays.put(resourceLocation, alchemyArray);
    }

    public AlchemyArray getArray(ResourceLocation index)
    {
        if (arrays.containsKey(index))
            return arrays.get(index);

        return arrays.get(arrays.keySet().toArray()[0]);
    }
}
