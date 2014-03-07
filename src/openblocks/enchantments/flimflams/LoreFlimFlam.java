package openblocks.enchantments.flimflams;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import openblocks.api.IAttackFlimFlam;
import openblocks.rubbish.LoreGenerator;
import openmods.utils.ItemUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class LoreFlimFlam implements IAttackFlimFlam {

	private static final Random random = new Random();

	@Override
	public void execute(EntityPlayer source, EntityPlayer target) {
		int slot = random.nextInt(5);
		ItemStack item;

		if (slot == 4) item = target.getHeldItem();
		else item = target.inventory.armorInventory[slot];

		if (item != null) {
			NBTTagCompound tag = ItemUtils.getItemTag(item);

			NBTTagCompound display = tag.getCompoundTag("display");
			if (!tag.hasKey("display")) tag.setTag("display", display);

			String lore = LoreGenerator.generateLore(target.username, identityType(item));

			NBTTagList loreList = new NBTTagList();
			for (String line : splitText(lore, 30))
				loreList.appendTag(new NBTTagString("lies", line));

			display.setTag("Lore", loreList);
		}
	}

	private static List<String> splitText(String lore, int maxSize) {
		List<String> result = Lists.newArrayList();

		Joiner joiner = Joiner.on(" ");
		Iterable<String> words = Splitter.on(" ").omitEmptyStrings().split(lore);
		List<String> buffer = Lists.newArrayList();
		int length = 0;
		for (String word : words) {
			int newLength = length + word.length();
			if (newLength > maxSize) {
				result.add(joiner.join(buffer));
				length = 0;
				buffer.clear();
			}
			buffer.add(word);
			length += word.length() + 1;
		}
		if (!buffer.isEmpty()) result.add(joiner.join(buffer));

		return result;
	}

	private static String identityType(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ItemArmor) {
			switch (((ItemArmor)item).armorType) {
				case 0:
					return "helmet";
				case 1:
					return "chestplate";
				case 2:
					return "leggings";
				case 3:
					return "boots";
			}
		} else if (item instanceof ItemPickaxe) return "pickaxe";
		else if (item instanceof ItemShears) return "shears";
		else if (item instanceof ItemAxe) return "axe";
		else if (item instanceof ItemSpade) return "shovel";
		else if (item instanceof ItemBlock) return "block";
		else if (item instanceof ItemBucket) return "bucket";

		return "junk";
	}

	@Override
	public String name() {
		return "epic lore add";
	}

	@Override
	public float weight() {
		return 0;
	}

}