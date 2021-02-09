package com.lonewolf.ee;

import com.lonewolf.ee.alchemy.ArrayManager;
import com.lonewolf.ee.alchemy.TestArray;
import com.lonewolf.ee.alchemy.TransmutationArray;
import com.lonewolf.ee.client.handler.KeyBindManager;
import com.lonewolf.ee.client.renderer.tileentity.ControllerAlchemyArrayTESR;
import com.lonewolf.ee.configuration.ConfigurationManager;
import com.lonewolf.ee.exchange.EnergyValueManager;
import com.lonewolf.ee.exchange.ExchangeBlackList;
import com.lonewolf.ee.knowledge.PlayerKnowledgeManager;
import com.lonewolf.ee.network.ExchangeNetwork;
import com.lonewolf.ee.recipe.RecipeManager;
import com.lonewolf.ee.reference.Reference;
import com.lonewolf.ee.registry.TileEntityRegistry;
import com.lonewolf.ee.settings.ChalkSettingsManager;
import javafx.scene.input.KeyCode;
import net.minecraft.client.ClientGameSession;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.ClientHooks;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.mod_id)
public class EquivalentExchange {
    private static final Logger logger = LogManager.getLogger();

    private static PlayerKnowledgeManager playerKnowledgeManager;
    private static ArrayManager arrayManager;
    private static ExchangeBlackList exchangeBlackList;
    private static EnergyValueManager energyValueManager;
    private static RecipeManager recipeManager;
    private static ChalkSettingsManager chalkSettingsManager;
    private static KeyBindManager keyBindManager;

    public EquivalentExchange() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }

    public static PlayerKnowledgeManager getPlayerKnowledgeManager() {
        return playerKnowledgeManager;
    }

    public static ExchangeBlackList getExchangeBlackList() {
        return exchangeBlackList;
    }

    public static EnergyValueManager getEnergyValueManager() {
        return energyValueManager;
    }

    public static RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public static ArrayManager getArrayManager() {return arrayManager;}

    public static ChalkSettingsManager getChalkSettingsManager() {
        return chalkSettingsManager;
    }

    public static KeyBindManager getKeyBindManager() {
        return keyBindManager;
    }

    private void setup(final FMLCommonSetupEvent event) {
        ExchangeNetwork.init();
        ExchangeNetwork.registerPackets();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigurationManager.serverSpec);

        exchangeBlackList = new ExchangeBlackList();
        energyValueManager = new EnergyValueManager();
        recipeManager = new RecipeManager();
        playerKnowledgeManager = new PlayerKnowledgeManager();
        chalkSettingsManager = new ChalkSettingsManager();
        arrayManager = new ArrayManager();
        keyBindManager = new KeyBindManager();


        arrayManager.register(new ResourceLocation(Reference.mod_id, "array_transmutation"), new TransmutationArray());
        arrayManager.register(new ResourceLocation(Reference.mod_id, "test_array"), new TestArray());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigurationManager.clientSpec);


        ClientRegistry.bindTileEntityRenderer(TileEntityRegistry.alchemyArrayTileEntityType, ControllerAlchemyArrayTESR::new);
        keyBindManager.init();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private void processIMC(final InterModProcessEvent event) {

    }
}
