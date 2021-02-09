package com.lonewolf.ee.util;

import com.lonewolf.ee.reference.Reference;
import net.minecraft.util.ResourceLocation;

public class ResourceLocationHelper
{
	public ResourceLocationHelper() {
	}
	
	public static ResourceLocation getResourceLocation(String modId, String path) {
		return new ResourceLocation(modId, path);
	}
	
	public static ResourceLocation getResourceLocation(String path) {
		return getResourceLocation(Reference.mod_id, path);
	}
}
