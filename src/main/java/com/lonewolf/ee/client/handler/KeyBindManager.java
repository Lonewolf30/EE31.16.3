package com.lonewolf.ee.client.handler;

import javafx.scene.input.KeyCode;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindManager
{
    public KeyBinding chalk_settings;

    public void init()
    {
        chalk_settings = new KeyBinding("Chalk Settings", KeyCode.G.impl_getCode(), "Equivalent Exchange");

        ClientRegistry.registerKeyBinding(chalk_settings);
    }

    public KeyBinding getChalk_settings()
    {
        return chalk_settings;
    }
}
