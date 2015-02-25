package com.spacechase0.minecraft.componentequipment.item;

import com.spacechase0.minecraft.spacecore.BaseMod;
import com.spacechase0.minecraft.spacecore.util.ModObject;

public class Items extends com.spacechase0.minecraft.spacecore.item.Items
{
	@ModObject
	public IngotItem ingot;
	public Object[] ingotParams = new Object[] { IngotItem.TYPES };
	
	@ModObject
	public NuggetItem nugget;
	public Object[] nuggetParams = new Object[] { IngotItem.TYPES };
	
	@ModObject
	public PartItem part;
	
	@ModObject
	public HarvesterToolItem axe;
	public Object[] axeParams = new Object[] { "axe" };
	
	@ModObject
	public HoeItem hoe;
	
	@ModObject
	public HarvesterToolItem pickaxe;
	public Object[] pickaxeParams = new Object[] { "pickaxe" };
	
	@ModObject
	public SwordItem sword;
	
	@ModObject
	public HarvesterToolItem shovel;
	public Object[] shovelParams = new Object[] { "shovel" };
	
	@ModObject
	public BowItem bow;
	
	@ModObject
	public ArrowItem arrow;
	
	@ModObject
	public QuiverItem quiver;
	
	@ModObject
	public ArmorItem helmet;
	public Object[] helmetParams = new Object[] { "helmet" };
	
	@ModObject
	public ArmorItem chestplate;
	public Object[] chestplateParams = new Object[] { "chestplate" };
	
	@ModObject
	public ArmorItem leggings;
	public Object[] leggingsParams = new Object[] { "leggings" };
	
	@ModObject
	public ArmorItem boots;
	public Object[] bootsParams = new Object[] { "boots" };
	
	@ModObject
	public DocumentationItem docBook;
	
	@ModObject
	public ModifierItem modifierEssence;
	
	@ModObject
	public PartCasingItem partCasing;
	
	@ModObject
	public HarvesterToolItem paxel;
	public Object[] paxelParams = new Object[] { "paxel" };
}
