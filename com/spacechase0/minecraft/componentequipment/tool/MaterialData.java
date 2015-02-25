package com.spacechase0.minecraft.componentequipment.tool;

import com.spacechase0.minecraft.componentequipment.CELog;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MaterialData
{
	public MaterialData( String theType, ItemStack theMat, int theBaseDur, float theToolDurMult, int theArmorDurMult, float theSpeed, int theLevel, int theDamage, int theTotalArmor, String theFormat )
	{
		this( theType, new ItemStack[] { theMat }, theBaseDur, theToolDurMult, theArmorDurMult, theSpeed, theLevel, theDamage, theTotalArmor, theFormat );
	}
	
	public MaterialData( String theType, ItemStack[] theMat, int theBaseDur, float theToolDurMult, int theArmorDurMult, float theSpeed, int theLevel, int theDamage, int theTotalArmor, String theFormat )
	{
		type = theType;
		mat = theMat;
		baseDur = theBaseDur;
		toolDurMult = theToolDurMult;
		armorDurMult = theArmorDurMult;
		speed = theSpeed;
		level = theLevel;
		damage = theDamage;
		format = theFormat;
		totalArmor = theTotalArmor;
	}
	
	public String getType()
	{
		return type;
	}
	
	public boolean canRepairWith( ItemStack stack )
	{
		for ( ItemStack valid : getCraftingMaterials() )
		{
			if ( valid == null )
			{
				CELog.severe( "NULL MATERIAL FOR MATERIAL: " + getType() + " " + this );
				continue;
			}
			
			if ( RecipeSimplifier.matches( valid,  stack ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	public ItemStack[] getCraftingMaterials()
	{
		return mat;
	}
	
	public int getBaseDurability()
	{
		return baseDur;
	}
	
	public float getToolMultiplier()
	{
		return toolDurMult;
	}
	
	public float getArmorDurabilityMultiplier()
	{
		return armorDurMult;
	}
	
	public float getMiningSpeed()
	{
		return speed;
	}
	
	public float getMiningSpeedBonus( ItemStack stack )
	{
		return 0;
	}
	
	public final float getMiningSpeedWithBonus( ItemStack stack )
	{
		return getMiningSpeed() + getMiningSpeedBonus( stack );
	}
	
	public int getMiningLevel()
	{
		return level;
	}
	
	public int getMiningLevelBonus( ItemStack stack )
	{
		return 0;
	}
	
	public final int getMiningLevelWithBonus( ItemStack stack )
	{
		return getMiningLevel() + getMiningLevelBonus( stack );
	}
	
	public int getAttackDamage()
	{
		return damage;
	}
	
	public int getAttackDamageBonus( ItemStack stack )
	{
		return 0;
	}
	
	public final int getAttackDamageWithBonus( ItemStack stack )
	{
		return getAttackDamage() + getAttackDamageWithBonus( stack );
	}
	
	public int getExtraModifiers()
	{
		return 0;
	}
	
	public float getTotalArmor()
	{
		return totalArmor;
	}
	
	public String getFormat()
	{
		return format;
	}
	
	public String getSpecialAbility()
	{
		return "none";
	}
	
	// Copied from Modifier
	public void itemTick( Entity entity, ItemStack stack ) {}
	public void entityTick( EntityItem entity ) {}
	public void breakBlock( EntityLivingBase breaker, ItemStack stack, int x, int y, int z ) {}
	public void hitEntity( EntityLivingBase living2, EntityLivingBase living1, ItemStack stack ) {}
	//public void rightClick( EntityPlayer clicker, ItemStack stack, int x, int y, int z ) {}
	
	private final String type;
	private final ItemStack[] mat;
	private final int baseDur;
	private final float toolDurMult;
	private final float armorDurMult;
	private final float speed;
	private final int level;
	private final int damage;
	private final float totalArmor;
	private final String format;
}
