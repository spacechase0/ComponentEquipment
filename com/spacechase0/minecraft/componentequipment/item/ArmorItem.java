package com.spacechase0.minecraft.componentequipment.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import thaumcraft.api.IGoggles;
import thaumcraft.api.nodes.IRevealer;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.client.ClientProxy;
import com.spacechase0.minecraft.componentequipment.client.model.ArmorModel;
import com.spacechase0.minecraft.componentequipment.tool.Armor;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.IArmorApiarist;
import forestry.api.core.IArmorNaturalist;

@Optional.InterfaceList( {
                         	@Optional.Interface( iface = "thaumcraft.api.nodes.IRevealer", modid = "Thaumcraft" ),
                         	@Optional.Interface( iface = "thaumcraft.api.IGoggles",        modid = "Thaumcraft" ),
                         	@Optional.Interface( iface = "forestry.api.apiculture.IArmorApiarist", modid = "Forestry" ),
                         	@Optional.Interface( iface = "forestry.api.core.IArmorNaturalist",     modid = "Forestry" )
                         } )
public class ArmorItem extends EquipmentItem implements ISpecialArmor, IRevealer, IGoggles, IArmorApiarist, IArmorNaturalist
{
	public ArmorItem( String theType )
	{
		super( theType, Armor.instance );
		armor = Armor.instance;
		
		setCreativeTab( ComponentEquipment.toolsTab );
	}
	
	@Override
	protected String getIconDirectory()
	{
		return "armor";
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer player)
    {
		String[] mods = armor.getModifiers( stack ).toArray( new String[] {} );
		for ( String mod : mods )
		{
			if ( Modifier.getModifier( mod ).handleArmorRightClick( player, stack ) )
			{
				return stack;
			}
		}
		
        int i = 4 - armor.getData( type ).getType() - 1;
        ItemStack itemstack1 = player.getCurrentArmor(i);

        if (itemstack1 == null)
        {
            player.setCurrentItemOrArmor(i + 1, stack.copy()); //Forge: Vanilla bug fix associated with fixed setCurrentItemOrArmor indexs for players.
            stack.stackSize = 0;
        }

        return stack;
    }
	
	@Override
    public boolean isValidArmor( ItemStack stack, int armorType, Entity entity )
    {
		return ( armor.getData( type ).getType() == armorType );
    }
    
	/*
    @Override
    public String getArmorTexture( ItemStack stack, Entity entity, int slot, int layer )
    {
    	if ( slot != 1 )
    	{
    		return null;
    	}
		
		return "/armor/power.png";//armor.getTextureForLayer( stack, layer );
    }
    */
    
    @Override
    @SideOnly( Side.CLIENT )
    public ModelBiped getArmorModel( EntityLivingBase entity, ItemStack stack, int slot )
    {
    	ClientProxy proxy = ( ClientProxy ) ComponentEquipment.proxy;
    	
    	ArmorModel model = proxy.getArmorModel( entity, slot );
    	if ( model == null )
    	{
    		proxy.addArmorModel( entity, slot, new ArmorModel( entity, type ) );
    		return getArmorModel( entity, stack, slot );
    	}
    	
        return model;
    }

	@Override
	public ArmorProperties getProperties( EntityLivingBase player, ItemStack stack, DamageSource source, double damage, int slot )
	{
		if ( getDamage( stack ) > getMaxDamage( stack ) || source.isUnblockable() )
		{
			return new ArmorProperties( 0, 0, 1 );
		}
		
		return new ArmorProperties( 0, armor.getProtectionOf( stack ), 10 /* ? */ );
	}

	@Override
	public int getArmorDisplay( EntityPlayer player, ItemStack stack, int slot )
	{
		if ( getDamage( stack ) > getMaxDamage( stack ) )
		{
			return 0;
		}
		
		return armor.getDisplayProtectionOf( stack );
	}

	@Override
	public void damageArmor( EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot )
	{
		damageItemStack( entity, stack, damage );
	}
	
	@Override
    public void onArmorTick( World world, EntityPlayer player, ItemStack stack )
    {
		onUpdate( stack, world, player, 0, false );
    }
	
	public final Armor armor;

	// Thaumcraft
	@Optional.Method( modid = "Thaumcraft" )
	@Override
	public boolean showNodes( ItemStack stack, EntityLivingBase player )
	{
		if ( armor.getModifierLevel( stack, "seeAuraNodes" ) > 0 )
		{
			return true;
		}
		
		return false;
	}
	
	@Optional.Method( modid = "Thaumcraft" )
	@Override
	public boolean showIngamePopups( ItemStack stack, EntityLivingBase player )
	{
		if ( armor.getModifierLevel( stack, "seeAuraNodes" ) > 0 )
		{
			return true;
		}
		
		return false;
	}

	// Forestry
	@Optional.Method( modid = "Forestry" )
	@Override
	public boolean canSeePollination( EntityPlayer player, ItemStack stack, boolean doSee )
	{
		if ( armor.getModifierLevel( stack, "natureVisor" ) > 0 )
		{
			return true;
		}
		
		return false;
	}

	@Optional.Method( modid = "Forestry" )
	@Override
	public boolean protectPlayer( EntityPlayer player, ItemStack stack, String cause, boolean doProtect )
	{
		if ( armor.getModifierLevel( stack, "beeSuit" ) > 0 )
		{
			return true;
		}
		
		return false;
	}
}