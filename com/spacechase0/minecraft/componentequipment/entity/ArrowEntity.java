package com.spacechase0.minecraft.componentequipment.entity;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tool.Arrow;

public class ArrowEntity extends ArrowEntityBase
{
	public ArrowEntity( World world )
	{
		super( world );
	}

	public ArrowEntity( World world, double x, double y, double z )
	{
		super( world, x, y, z );
	}

	public ArrowEntity( World world, EntityLivingBase living1, EntityLivingBase living2, float par4, float par5 )
	{
		super( world, living1, living2, par4, par5 );
	}

	public ArrowEntity( World world, EntityLivingBase living, float par3 )
	{
		super( world, living, par3 );
	}
	
	@Override
    protected void entityInit()
    {
		super.entityInit();
		
		// Not sure how this happens...
		if ( headMat == null )
		{
			headMat = "flint";
		}
		
        dataWatcher.addObject( ARROW_HEAD, headMat );
        dataWatcher.addObject( ARROW_FEATHER, ( byte )( feathered ? 1 : 0 ) );
    }
	
	@Override
    public void onUpdate()
	{
		String str = dataWatcher.getWatchableObjectString( ARROW_HEAD );
		if ( str != null )
		{
			headMat = str;
		}
		
		feathered = ( dataWatcher.getWatchableObjectByte( ARROW_FEATHER ) == 1 ) ? true : false;
		
		super.onUpdate();
	}
	
	@Override
    public void onCollideWithPlayer( EntityPlayer player )
    {
		// Copied from parent, needed to change the item
        if ( !worldObj.isRemote && inGround && arrowShake <= 0 )
        {
            boolean flag = canBePickedUp == 1 || canBePickedUp == 2 && player.capabilities.isCreativeMode;

            if ( canBePickedUp == 1 && !player.inventory.addItemStackToInventory( getCorrespondingItem() ) )
            {
                flag = false;
            }

            if (flag)
            {
                playSound("random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.onItemPickup( this, 1 );
                setDead();
            }
        }
    }
	
	@Override
    public void writeEntityToNBT( NBTTagCompound tag )
    {
		super.writeEntityToNBT( tag );
		tag.setString( "ArrowHead", headMat );
		tag.setBoolean( "ArrowFeather", feathered );
    }
	
	@Override
    public void readEntityFromNBT( NBTTagCompound tag )
    {
		super.readEntityFromNBT( tag );
		headMat = tag.getString( "ArrowHead" );
		feathered = tag.getBoolean( "ArrowFeather" );
    }
	
	public String getHeadMaterial()
	{
		return headMat;
	}
	
	public void setHeadMaterial( String mat )
	{
		headMat = mat;
		setDamage( Arrow.getData( headMat ).getDamage() );
		
		if ( headMat.equals( "gold" ) )
		{
			motionX *= 2;
			motionY *= 2;
			motionZ *= 2;
		}
		
		dataWatcher.updateObject( ARROW_HEAD, headMat );
	}
	
	public boolean isFeathered()
	{
		return feathered;
	}
	
	public void setFeathered( boolean theFeathered )
	{
		feathered = theFeathered;
		if ( !feathered )
		{
			motionX *= 0.75;
			motionY *= 0.75;
			motionZ *= 0.75;
		}
		
		dataWatcher.updateObject( ARROW_FEATHER, ( byte )( feathered ? 1 : 0 ) );
	}
	
	public ItemStack getCorrespondingItem()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString( "Head", headMat );
		//tag.setString( "Stick", "wood" );
		tag.setBoolean( "Feather", feathered );
		
		ItemStack stack = new ItemStack( ComponentEquipment.items.arrow, 1 );
		stack.setTagCompound( tag );
		
		return stack;
	}
	
	@Override
	protected void onHit( MovingObjectPosition mop )
	{
		boolean wasDead = isDead;
		super.onHit( mop );
		
		if ( wasDead )
		{
			return;
		}
		
		if ( headMat.equals( "enderPearl" ) )
		{
			doEnderPearl();
		}
		else if ( headMat.equals( "torch" ) )
		{
			doTorch( mop );
		}
		else if ( headMat.equals( "tnt" ) )
		{
			doTnt( mop );
		}
		else if ( headMat.equals( "bonemeal" ) )
		{
			doBonemeal( mop );
		}
	}
	
	private void doEnderPearl()
	{
        for (int i = 0; i < 32; ++i)
        {
            this.worldObj.spawnParticle("portal", this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
        }

        if (!this.worldObj.isRemote )
        {
            if (this.getThrower() != null && this.getThrower() instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)this.getThrower();
                
                if (entityplayermp.playerNetServerHandler.func_147362_b().isChannelOpen() && entityplayermp.worldObj == this.worldObj)
                {
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, this.posX, this.posY, this.posZ, 5);
                    if (!MinecraftForge.EVENT_BUS.post(event))
                    {
                    	EntityLivingBase living = ( EntityLivingBase ) getThrower();
                    	if ( living.isRiding() )
                    	{
                    		living.mountEntity( null );
                    	}
                    	living.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
                    	living.fallDistance = 0.0F;
                    	living.attackEntityFrom(DamageSource.fall, event.attackDamage);
                    }

                }
            }

            this.setDead();
        }
    }
	
	private void doTorch( MovingObjectPosition mop )
	{
		if ( !worldObj.isRemote && mop.entityHit == null && mop.sideHit != -1 )
		{
			ForgeDirection dir = ForgeDirection.getOrientation( mop.sideHit );
			int x = mop.blockX + dir.offsetX;
			int y = mop.blockY + dir.offsetY;
			int z = mop.blockZ + dir.offsetZ;
			Block block = worldObj.getBlock( x, y, z );
			
			if ( torch.canPlaceBlockAt( worldObj, x, y, z ) && ( block == null || block.isReplaceable( worldObj, x, y, z ) ) )
			{
				worldObj.setBlock( x, y, z, torch, 6 - mop.sideHit, 0x3 );
				setDead();
			}
		}
	}
	
	private void doTnt( MovingObjectPosition mop )
	{
		if ( !worldObj.isRemote )
		{
			//System.out.println("tnt@"+posX+" "+posY+" "+posZ);
			worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 1.5f, true);
			setDead();
		}
	}
	
	private void doBonemeal( MovingObjectPosition mop )
	{
		if ( !worldObj.isRemote && mop.entityHit == null && mop.sideHit != -1 )
		{
			int x = mop.blockX;
			int y = mop.blockY;
			int z = mop.blockZ
					;
			Block block = worldObj.getBlock( x, y, z );
			
			if ( block == farmland )
			{
				--y;
			}
			
			if ( block != null )
			{
				ItemStack bonemeal = new ItemStack( dye, 1, 15 );
				ItemDye.applyBonemeal( bonemeal, worldObj, x, y, z, null );
				
				if ( bonemeal.stackSize == 0 )
				{
					setDead();
				}
			}
		}
	}
	
	private String headMat = "flint";
	private boolean feathered = true;
	
	private static final int ARROW_HEAD = 20;
	private static final int ARROW_FEATHER = 22;
}
