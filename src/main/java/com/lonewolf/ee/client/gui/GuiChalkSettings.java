package com.lonewolf.ee.client.gui;

import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.reference.Reference;
import com.lonewolf.ee.settings.ChalkSettings;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;

import java.util.UUID;

public class GuiChalkSettings extends Screen
{
    private ChalkSettings settings;
    private UUID playerID;
    private Button shrinkSize, expandSize, transmute, rando;

    public GuiChalkSettings()
    {
        super(new StringTextComponent("ChalkSettings"));

        assert Minecraft.getInstance().player != null;
        playerID = Minecraft.getInstance().player.getUniqueID();
        settings = EquivalentExchange.getChalkSettingsManager().getChalkSetting(playerID);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height)
    {
        super.init(minecraft, width, height);
        this.addButton(this.shrinkSize = new Button(64, 64, 16, 20,
                new TranslationTextComponent("ee.gui.settings.shrink"), b -> decreaseArraySize()));

        this.addButton(this.expandSize = new Button(64+32, 64, 16, 20,
                new TranslationTextComponent("ee.gui.settings.expand"), b -> increaseArraySize()));

        this.addButton(this.transmute = new Button(64+64, 64, 16, 20,
                new TranslationTextComponent("ee.gui.settings.expand"), b -> setTransmute()));

        this.addButton(this.rando = new Button(64+96, 64, 16, 20,
                new TranslationTextComponent("ee.gui.settings.shrink"), b -> setRando()));
    }

    public void increaseArraySize()
    {
        settings.incrementSize();
    }

    public void setTransmute()
    {
        settings.setIndex(new ResourceLocation(Reference.mod_id, "array_transmutation"));
    }

    public void setRando()
    {
        settings.setIndex(new ResourceLocation(Reference.mod_id, "test_array"));
    }

    public void decreaseArraySize()
    {
        settings.decrementSize();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public void renderBackground(MatrixStack matrixStack)
    {

    }

    @Override
    public void closeScreen()
    {
        EquivalentExchange.getChalkSettingsManager().setChalkSettings(playerID, settings);
        LogManager.getLogger().info(settings.getSize());
        super.closeScreen();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        ITextComponent text = new StringTextComponent(String.valueOf(settings.getSize()));
        getFontRenderer().func_238422_b_(matrixStack, text.func_241878_f(), 64+16, 64 - getFontRenderer().FONT_HEIGHT, 0xFFFFFF);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public FontRenderer getFontRenderer()
    {
        return minecraft.fontRenderer;
    }
}
