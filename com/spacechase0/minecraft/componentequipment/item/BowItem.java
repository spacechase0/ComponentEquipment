package com.spacechase0.minecraft.componentequipment.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.entity.ArrowEntity;
import com.spacechase0.minecraft.componentequipment.tool.Arrow;

public class BowItem extends ToolItem
{
	public BowItem()
	{
		super( "bow" );
	}
	
	@Override
    public void registerIcons( IIconRegister register )
    {
		super.registerIcons( register );
		
		stringIcons = new IIcon[ 4 ];
		for ( int is = 0; is < stringIcons.length; ++is )
		{
			stringIcons[ is ] = register.registerIcon( "componentequipment:bowString/" + is );
		}
    }
	
	@Override
    public IIcon getIcon( ItemStack stack, int pass, EntityPlayer player, ItemStack using, int use )
    {
        if ( pass == 0 )
        {
        	int maxUseTime = tool.getBowChargeTime( stack );
        	int min = ( maxUseTime / 3 ) * 2;
        	
        	int useTime = getMaxItemUseDuration( stack ) - use;
        	
        	if ( use == 0 )
        	{
        		return stringIcons[ 0 ];
        	}
        	else if ( useTime < min )
        	{
        		return stringIcons[ 1 ];
        	}
        	else if ( useTime < maxUseTime )
        	{
        		return stringIcons[ 2 ];
        	}
        	else
        	{
        		return stringIcons[ 3 ];
        	}
        }
        
        return super.getIcon( stack, pass - 1, player, using, use );
    }
	
	@Override
    public ItemStack onEaten( ItemStack stack, World world, EntityPlayer player )
    {
		// What?
        return stack;
    }
	
	@Override
    public int getMaxItemUseDuration( ItemStack stack )
    {
        return 72000;
    }
	
	@Override
    public EnumAction getItemUseAction( ItemStack stack )
    {
        return EnumAction.bow;
    }
	
	@Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {
        ArrowNockEvent event = new ArrowNockEvent( player, stack );
        MinecraftForge.EVENT_BUS.post( event );
        if ( event.isCanceled() )
        {
            return event.result;
        }
        
        // TODO: Custom arrows
        if ( getActiveArrow( player ) != null )
        {
            player.setItemInUse( stack, this.getMaxItemUseDuration( stack ) );
        }

        return stack;
    }
	
	@Override
    public void onPlayerStoppedUsing (ItemStack stack, World world, EntityPlayer player, int useDur )
    {
		// Ammo check and stuff
        Ammo ammo = getActiveArrow( player );
        if ( ammo == null || ammo.inv == null || ammo.stack == null || ammo.slot == -1 )
        {
        	return;
        }
        
        boolean ignoreArrows = EnchantmentHelper.getEnchantmentLevel( Enchantment.infinity.effectId, stack ) > 0 && Arrow.getData( ArrowItem.getHeadMaterial( ammo.stack ) ).affectedByInfinity();
        ignoreArrows = player.capabilities.isCreativeMode || ignoreArrows;
        
        // Charge
        int charge = getMaxItemUseDuration( stack ) - useDur;
        ArrowLooseEvent event = new ArrowLooseEvent( player, stack, charge );
        MinecraftForge.EVENT_BUS.post( event );
        if ( event.isCanceled() || charge < 5 )
        {
            return;
        }
        charge = event.charge;
        
        // Shoot arrow
        float f = (float) charge / 20.0F;
        f = ( f * f + f * 2.0F ) / 3.0F;
        if ( f > 1.0F )
        {
            f = 1.0F;
        }

        ArrowEntity arrow = new ArrowEntity( world, player, f * 2.0F );
        arrow.setHeadMaterial( ArrowItem.getHeadMaterial( ammo.stack ) );
        arrow.setFeathered( ArrowItem.getFeathered( ammo.stack ) );

        if (f == 1.0F)
        {
            arrow.setIsCritical( true );
        }

        // TODO: Detect TConstruct and double damage?
        int powerLevel = EnchantmentHelper.getEnchantmentLevel( Enchantment.power.effectId, stack );
        if ( powerLevel > 0 )
        {
            arrow.setDamage( arrow.getDamage() + (double) powerLevel * 0.5D + 0.5D );
        }

        int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
        if ( punchLevel > 0 )
        {
            arrow.setKnockbackStrength( punchLevel );
        }

        if ( EnchantmentHelper.getEnchantmentLevel( Enchantment.flame.effectId, stack ) > 0 )
        {
            arrow.setFire( 100 );
        }

        if ( !player.capabilities.isCreativeMode )
        {
        	damageItemStack( player, stack, 1 );
        }
        
        world.playSoundAtEntity( player, "random.bow", 1.0F, 1.0F / ( itemRand.nextFloat() * 0.4F + 1.2F ) + f * 0.5F );

        if ( ignoreArrows )
        {
            arrow.canBePickedUp = 2;
        }
        else
        {
        	ammo.inv.decrStackSize( ammo.slot, 1 );
        }
        
        if ( !world.isRemote )
        {
            world.spawnEntityInWorld( arrow );
        }
    }
	
	private static class Ammo
	{
		IInventory inv;
		ItemStack stack;
		int slot;
	}
	
	private Ammo getActiveArrow( EntityPlayer player )
	{
		ItemStack quiver = QuiverItem.findFirstQuiver( player.inventory );
		if ( quiver != null )
		{
			IInventory inv = QuiverItem.getInventoryOf( quiver );
			int selectedSlot = QuiverItem.getSelected( player );
			ItemStack selected = inv.getStackInSlot( selectedSlot );
			if ( selected != null )
			{
				Ammo ammo = new Ammo();
				ammo.inv = inv;
				ammo.stack = selected;
				ammo.slot = selectedSlot;
				
				return ammo;
			}
		}
		
        ItemStack arrowStack = null;
        int arrowSlot = -1;
        for ( int i = 0; i < player.inventory.getSizeInventory(); ++i )
        {
        	ItemStack slot = player.inventory.getStackInSlot( i );
        	if ( slot == null )
        	{
        		continue;
        	}
        	
        	if ( slot.getItem() == ComponentEquipment.items.arrow )
        	{
        		arrowSlot = i;
        		arrowStack = slot;
        		break;
        	}
        }

		Ammo ammo = new Ammo();
		ammo.inv = player.inventory;
		ammo.stack = arrowStack;
		ammo.slot = arrowSlot;
		
		if ( arrowStack != null )
		{
			return ammo;
		}
		
		return null;
	}
	
	private IIcon stringIcons[];
}
