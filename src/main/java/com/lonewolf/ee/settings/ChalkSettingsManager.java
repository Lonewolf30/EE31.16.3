package com.lonewolf.ee.settings;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChalkSettingsManager extends WorldSavedData
{
    private HashMap<UUID, ChalkSettings> settingsHashMap;

    public ChalkSettingsManager() {
        super("ee_chalk_settings");
        settingsHashMap = new HashMap<>();
    }

    public ChalkSettings getChalkSetting(UUID player)
    {
        if (!settingsHashMap.containsKey(player))
        {
            settingsHashMap.put(player, new ChalkSettings());
        }

        return settingsHashMap.get(player);
    }

    public void setChalkSettings(UUID player, ChalkSettings chalkSettings)
    {
        settingsHashMap.put(player, chalkSettings);
    }

    @Override
    public void read(CompoundNBT data)
    {
        CompoundNBT nbt = (CompoundNBT) data.get("data");

        for (String key : nbt.keySet())
        {
            UUID name = UUID.fromString(key);
            CompoundNBT chalkSettings = nbt.getCompound("key");
            ChalkSettings settings = new ChalkSettings();
            settings.readFromNBT(chalkSettings);
            settingsHashMap.put(name, settings);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        CompoundNBT data = new CompoundNBT();

        for (Map.Entry<UUID, ChalkSettings> entry : settingsHashMap.entrySet()) {
            UUID s = entry.getKey();
            ChalkSettings chalkSettings = entry.getValue();
            CompoundNBT chalkSettingsCompound = new CompoundNBT();
            chalkSettings.writeToNBT(chalkSettingsCompound);
            data.put(s.toString(), chalkSettingsCompound);
        }

        compound.put("data", data);
        return compound;
    }
}
