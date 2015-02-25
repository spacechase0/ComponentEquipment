package com.spacechase0.minecraft.componentequipment.tool.material;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.spacechase0.minecraft.componentequipment.tool.MaterialData;

public class EnderMaterial extends MaterialData
{
	public EnderMaterial( int theBaseDur, float theMult, int theArmorDurMult, float theSpeed, int theLevel, int theDamage, int theTotalArmor, EnumChatFormatting theFormat )
	{
		super( "ender", new ItemStack( Items.ender_pearl ), theBaseDur, theMult, theArmorDurMult, theSpeed, theLevel, theDamage, theTotalArmor, theFormat.toString() );
	}
	
	@Override
	public String getSpecialAbility()
	{
		return "collector";
	}

	@Override
	public void breakBlock( EntityLivingBase breaker, ItemStack stack, int x, int y, int z )
	{
		if ( breaker.worldObj.isRemote )
		{
			return;
		}
		
		dropCollector.startCollecting( breaker );
	}
	
	@Override
	public void hitEntity( EntityLivingBase attacker, EntityLivingBase target, ItemStack stack )
	{
		if ( attacker.worldObj.isRemote )
		{
			return;
		}
		
		dropCollector.stopCollecting();
	}
	
	private static final EnderDropCollector dropCollector = new EnderDropCollector();
}
