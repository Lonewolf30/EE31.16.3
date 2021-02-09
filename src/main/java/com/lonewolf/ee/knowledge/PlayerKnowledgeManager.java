package com.lonewolf.ee.knowledge;

import com.google.common.collect.Maps;
import com.lonewolf.ee.EquivalentExchange;
import com.lonewolf.ee.exchange.ExchangeBlackList;
import com.lonewolf.ee.util.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerKnowledgeManager extends WorldSavedData
{
	private final Map<String, PlayerKnowledge> player_knowledge = Maps.newHashMap();
	private static final Logger logger = LogManager.getLogger();
	
	public static final Marker PLAYER_KNOWLEDGE_MARKER;
	public static final Marker PLAYER_LEARN_KNOWLEDGE;
	public static final Marker PLAYER_FORGET_KNOWLEDGE_MARKER;
	public static final Marker PLAYER_FORGET_ALL_KNOWLEDGE_MARKER;
	
	public PlayerKnowledgeManager()
	{
		super("ee_player_knowledge");
		this.markDirty();
	}
	
	public PlayerKnowledge getKnowledge(String playerName)
	{
		if (!player_knowledge.containsKey(playerName))
		{
			player_knowledge.put(playerName, new PlayerKnowledge());
			this.markDirty();
		}
		
		return player_knowledge.get(playerName);
	}
	
	public void setPlayer_knowledge(String playerName, PlayerKnowledge playerKnowledge)
	{
		this.player_knowledge.put(playerName, playerKnowledge);
		this.markDirty();
	}
	
	@Override
	public void read(CompoundNBT nbt)
	{
		for (String name : nbt.keySet())
		{
			CompoundNBT list = nbt.getCompound(name);
			List<ItemStack> itemStacks = new ArrayList<>();
			for (String item: list.keySet())
			{
				itemStacks.add(ItemStack.read(list.getCompound(item)));
			}
			player_knowledge.put(name, new PlayerKnowledge(itemStacks));
		}
		
		LogHelper.info(PLAYER_KNOWLEDGE_MARKER, "Player Knowledge Read");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		for (Map.Entry<String, PlayerKnowledge> entry : player_knowledge.entrySet())
		{
			String s = entry.getKey();
			PlayerKnowledge playerKnowledge = entry.getValue();
			if (s == null)
				continue;
			
			CompoundNBT nbt = new CompoundNBT();

			int count = 0;
			for (ItemStack itemStack : playerKnowledge.getKnownItemStacks())
			{
				nbt.put(String.valueOf(count), itemStack.serializeNBT());
				count++;
			}

			compound.put(s, nbt);
		}
		
		LogHelper.info(PLAYER_KNOWLEDGE_MARKER, "Player Knowledge Write");
		return compound;
	}
	
	public boolean canPlayerLearn(String playerName, ItemStack itemStack)
	{
		PlayerKnowledge playerKnowledge = player_knowledge.get(playerName);
		
		if (playerKnowledge == null)
		{
			playerKnowledge = new PlayerKnowledge();
			player_knowledge.put(playerName, playerKnowledge);
		}
		
		return !playerKnowledge.isKnown(itemStack) && !EquivalentExchange.getExchangeBlackList().isItemBlacklisted(itemStack);
	}
	
	public boolean doesPlayerKnow(String playerName, ItemStack itemStack)
	{
		PlayerKnowledge playerKnowledge = player_knowledge.get(playerName);
		
		if (playerKnowledge == null)
		{
			playerKnowledge = new PlayerKnowledge();
			player_knowledge.put(playerName, playerKnowledge);
		}
		
		return playerKnowledge.isKnown(itemStack);
	}
	
	public void teachPlayer(String ownerName, ItemStack itemStack)
	{
		player_knowledge.get(ownerName).learn(itemStack);
		logger.debug(PLAYER_LEARN_KNOWLEDGE, "Player {} learned {}", new Object[]{ownerName, itemStack});
		this.markDirty();
	}
	
	public void makePlayerForget(String playerName, ItemStack itemStack)
	{
		player_knowledge.get(playerName).forget(itemStack);
		logger.debug(PLAYER_FORGET_KNOWLEDGE_MARKER, "Player {} forgot {}", new Object[]{playerName, itemStack});
		this.markDirty();
	}
	
	static {
		PLAYER_KNOWLEDGE_MARKER = MarkerManager.getMarker("EE_PLAYER_KNOWLEDGE");
		PLAYER_LEARN_KNOWLEDGE = MarkerManager.getMarker("EE_PLAYER_TEACH_KNOWLEDGE", PLAYER_KNOWLEDGE_MARKER);
		PLAYER_FORGET_KNOWLEDGE_MARKER = MarkerManager.getMarker("EE_PLAYER_FORGET_KNOWLEDGE", PLAYER_KNOWLEDGE_MARKER);
		PLAYER_FORGET_ALL_KNOWLEDGE_MARKER = MarkerManager.getMarker("EE_PLAYER_FORGET_ALL_KNOWLEDGE", PLAYER_FORGET_KNOWLEDGE_MARKER);
	}
}
