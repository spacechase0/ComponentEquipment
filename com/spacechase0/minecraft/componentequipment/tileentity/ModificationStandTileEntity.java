package com.spacechase0.minecraft.componentequipment.tileentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes.Recipe;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes.Recipe.StackType;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier;
import com.spacechase0.minecraft.spacecore.util.ListUtils;
import com.spacechase0.minecraft.spacecore.util.Vector3i;

public class ModificationStandTileEntity extends TileEntity implements IInventory
{
	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public ItemStack getStackInSlot( int slot )
	{
		return stacks[ slot ];
	}

	@Override
	public ItemStack decrStackSize( int slot, int amt )
	{
		ItemStack stack = getStackInSlot( slot );
		if ( stack == null )
		{
			return null;
		}
		
		ItemStack ret = stack.copy();
		ret.stackSize = Math.min( amt, stack.stackSize );
		
		stack.stackSize -= ret.stackSize;
		
		if ( stack.stackSize <= 0 )
		{
			setInventorySlotContents( slot, null );
		}
		
		markDirty(); // onInventoryChanged
		worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
		
		return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing( int slot )
	{
		return null;
	}

	@Override
	public void setInventorySlotContents( int slot, ItemStack stack )
	{
		if ( isItemValidForSlot( slot, stack ) )
		{
			stacks[ slot ] = stack;
		}
	}
	
	@Override
	public boolean isItemValidForSlot( int i, ItemStack item )
	{
		if ( item == null )
		{
			return true;
		}

		return true;// ( item.getItem() instanceof ToolItem );
	}
	
	@Override
	public String getInventoryName()
	{
		return "tile.modificationStand.name";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		// Copied from chest code :P
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}
	
    @Override
    public void readFromNBT( NBTTagCompound tag )
    {
		super.readFromNBT( tag );
		
		for ( int i = 0; i < stacks.length; ++i )
		{
			stacks[ i ] = ItemStack.loadItemStackFromNBT( ( NBTTagCompound ) tag.getTag( "Item" + i ) );
		}
    }

    @Override
    public void writeToNBT( NBTTagCompound tag )
    {
		super.writeToNBT( tag );
		
		for ( int i = 0; i < stacks.length; ++i )
		{
			NBTTagCompound itemTag = new NBTTagCompound();
			if ( stacks[ i ] != null )
			{
				stacks[ i ].writeToNBT( itemTag );
			}
			tag.setTag( "Item" + i, itemTag );
		}
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -1, nbttagcompound);
    }
    
    @Override
    public void onDataPacket( NetworkManager net, S35PacketUpdateTileEntity pkt )
    {
    	if ( worldObj.isRemote )
    	{
    		readFromNBT( pkt.func_148857_g() );
    	}
    }
    
    /*
    public boolean hasValidRecipe()
    {
    	String result = checkRecipe();
    	return ( result != null && !result.equals( "needtool" ) && !result.equals( "nomods" ) );
    }*/
    
    public void activate()
    {
    	RecipeResult results = checkRecipe();
    	if ( results != null && results.error == null )
    	{
    		results.remove( getWorldObj() );
    		EquipmentItem item = ( EquipmentItem ) stacks[ 0 ].getItem();
    		item.equipment.setModifierLevel( stacks[ 0 ], results.recipe.mod, item.equipment.getModifierLevel( stacks[ 0 ], results.recipe.mod ) + 1 );

    		markDirty(); // onInventoryChanged
    		worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
    	}
    }
    
    public void disassemble()
    {
    	ItemStack stack = stacks[ 0 ];
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		
		List< ItemStack > results = new ArrayList< ItemStack >();
		
		if ( !item.type.equals( "paxel" ) )
		{
			for ( String part : item.equipment.getBaseData( item.type ).getParts() )
			{
				String partItem = part;
				if ( part.endsWith( "Left" ) )
				{
					partItem = part.substring( 0, part.indexOf( "Left" ) );
				}
				else if ( part.endsWith( "Left" ) )
				{
					partItem = part.substring( 0, part.indexOf( "Right" ) );
				}
				else if ( part.equals( "head" ) )
				{
					partItem = item.type + part.substring( 0, 1 ).toUpperCase() + part.substring( 1 );
				}
				
				NBTTagCompound partTag = new NBTTagCompound();
				partTag.setString( "Part", partItem );
				partTag.setString( "Material", item.equipment.getMaterialOf( stack, part ).getType() );
				
				ItemStack partStack = new ItemStack( ComponentEquipment.items.part );
				partStack.setTagCompound( partTag );
				results.add( partStack );
				
				if ( item.equipment.getCasing( stack, part ) != null )
				{
					ItemStack casingStack = new ItemStack( ComponentEquipment.items.partCasing );
					casingStack.setItemDamage( partStack.getItemDamage() );
					casingStack.stackSize = partStack.stackSize;
					casingStack.setTagCompound( ( NBTTagCompound ) partStack.getTagCompound().copy() );
					casingStack.getTagCompound().setString( "Material", item.equipment.getCasing( stack, part ) );
					results.add( casingStack );
				}
			}
			
			for ( String modName : item.equipment.getModifiers( stack ) )
			{
				Modifier mod = Modifier.getModifier( modName );
				mod.addDisassemblyResults( stack, results );
			}
		}
		else
		{
			Item[] items = new Item[] { ComponentEquipment.items.pickaxe, ComponentEquipment.items.shovel, ComponentEquipment.items.axe };
			for ( Item toolItem : items )
			{
				ItemStack newStack = new ItemStack( toolItem, 1, 0 );
				newStack.setTagCompound( ( NBTTagCompound ) stack.getTagCompound().copy() );
				results.add( newStack );
			}
			
			results.add( new ItemStack( nether_star ) );
		}

        worldObj.playSoundEffect( xCoord, yCoord, zCoord, "random.explode", 3.5f, 0.5f + ( worldObj.rand.nextFloat() * 0.2f ) );
        
        for ( ItemStack dropStack : results )
        {
        	final double FORCE = 0.15;

        	double velX = ( Math.random() * FORCE * 2 ) - FORCE;
        	double velY = 0.1 + ( Math.random() * FORCE );
        	double velZ = ( Math.random() * FORCE * 2 ) - FORCE;
        	
        	EntityItem entity = new EntityItem( worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, dropStack );
        	entity.setVelocity( velX, velY, velZ );
        	worldObj.spawnEntityInWorld( entity );
        }
        
        stacks[ 0 ] = null;
        
        worldObj.setBlock( xCoord, yCoord - 1, zCoord, air );
        worldObj.setBlock( xCoord, yCoord, zCoord, air );
    }
    
    public static class RecipeResult
    {
    	public RecipeResult( Recipe theRecipe ) { recipe = theRecipe; }
    	public RecipeResult( String theError ) { error = theError; }
    	
    	public Recipe recipe;
    	public String error;
    	
    	public List< Vector3i > removeBlocks = new ArrayList< Vector3i >();
    	public List< EntityItemFrame > removeItems = new ArrayList< EntityItemFrame >();
    	public int removeXpAmount;
    	public EntityPlayer removeXpFrom;
    	
    	public void remove( World world )
    	{
    		for ( Vector3i pos : removeBlocks )
    		{
    			world.setBlock( pos.x, pos.y, pos.z, air );
    		}
    		
    		for ( EntityItemFrame frame : removeItems )
    		{
    			frame.setDisplayedItem( null );
    		}
    		
    		removeXpFrom.addExperienceLevel( -removeXpAmount );
    	}
    }
    
    public RecipeResult checkRecipe()
    {
    	if ( stacks[ 0 ] == null || !( stacks[ 0 ].getItem() instanceof EquipmentItem ) )
    	{
    		return new RecipeResult( "needtool" );
    	}
    	
    	// TODO: Rewrite for new modifier recipe system.
    	for ( Recipe recipe : ModifierRecipes.recipes )
    	{
    		RecipeResult results = checkRecipe( recipe );
    		if ( results != null && results.error == null )
    		{
    			EquipmentItem item = ( EquipmentItem ) stacks[ 0 ].getItem();
    	    	if ( item.equipment.getModifiersRemaining( stacks[ 0 ] ) < Modifier.getModifier( recipe.mod ).getModifierCost() )
    	    	{
    	    		if ( item.equipment.getModifierLevel( stacks[ 0 ], recipe.mod ) <= 0 )
    	    		{
    	    			return new RecipeResult( "nomods" );
    	    		}
    	    	}
    	    	{
    	    		int levelCost = item.equipment.getXpCost( stacks[ 0 ] );
    	    		
    	    		EntityPlayer hasXp = null;
    	    		List players = worldObj.getEntitiesWithinAABB( EntityPlayer.class, AxisAlignedBB.getBoundingBox( xCoord - 2, yCoord - 2, zCoord - 2, xCoord + 3, yCoord + 3, zCoord + 3 ) );
    	    		for ( Object obj : players )
    	    		{
    	    			EntityPlayer player = ( EntityPlayer ) obj;
    	    			if ( player.experienceLevel >= levelCost )
    	    			{
    	    				hasXp = player;
    	    			}
    	    		}
    	    		
    	    		if ( hasXp == null )
    	    		{
    	    			return new RecipeResult( "needxp" );
    	    		}
    	    		results.removeXpAmount = levelCost;
    	    		results.removeXpFrom = hasXp;
    	    	}
    	    	
    			return results;
    		}
    	}
    	
    	return new RecipeResult( "badrecipe" );
    }
    
    private RecipeResult checkRecipe( Recipe recipe )
    {
		EquipmentItem item = ( EquipmentItem ) stacks[ 0 ].getItem();
    	int level = item.equipment.getModifierLevel( stacks[ 0 ], recipe.mod );
    	if ( !Modifier.getModifier( recipe.mod ).canAdd( stacks[ 0 ] ) ||
    	     level + 1 != recipe.level )
    	{
    		return null;
    	}
    	
    	List< StackType > ingredients = ListUtils.clone( recipe.ingredients );
    	for ( int i = 0; i < ingredients.size(); ++i )
    	{
    		ingredients.set( i, ingredients.get( i ).copy() );
    	}
    	
    	RecipeResult results = new RecipeResult( recipe );
    	for ( int ix = ( int ) xCoord - 2; ix <= ( int ) xCoord + 2; ++ix )
    	{
        	for ( int iz = ( int ) zCoord - 2; iz <= ( int ) zCoord + 2; ++iz )
        	{
            	for ( int iy = ( int ) yCoord - 1; iy <= ( int ) yCoord; ++iy )
            	{
            		if ( ix == ( int ) xCoord && iz == ( int ) zCoord )
            		{
            			continue;
            		}
            		
            		Block block = worldObj.getBlock( ix, iy, iz );
            		for ( StackType ingredient : ingredients )
            		{
            			if ( ingredient.amt > 0 &&
            				 ( ingredient.matches( new ItemStack( block, worldObj.getBlockMetadata( ix, iy, iz ) ) ) ||
            			       ingredient.matches( new ItemStack( block.getItem( worldObj, ix, iy, iz ) ) ) ) )
            			{
            				ingredient.amt -= 1;
            				results.removeBlocks.add( new Vector3i( ix, iy, iz ) );
            				break;
            			}
            		}
            	}
        	}
    	}
    	
    	List frames = worldObj.getEntitiesWithinAABB( EntityItemFrame.class, AxisAlignedBB.getBoundingBox( xCoord - 2, yCoord - 2, zCoord - 2, xCoord + 3, yCoord + 3, zCoord + 3 ) );
    	for ( Object obj : frames )
	    {
    		EntityItemFrame frame = ( EntityItemFrame ) obj;
    		if ( frame.getDisplayedItem() == null ) continue;
	    	for ( StackType ingredient : ingredients )
	    	{
	    		if ( ingredient.amt > 0 && ingredient.matches( frame.getDisplayedItem() ) )
	    		{
	    			ingredient.amt -= 1;
	    			results.removeItems.add( frame );
	    			break;
	    		}
	    	}
    	}
    	
    	for ( StackType ingredient : ingredients )
    	{
    		if ( ingredient.amt > 0 )
    		{
    			results.error = "badrecipe";
    			results.removeBlocks.clear();
    			results.removeItems.clear();
    			break;
    		}
    	}
    	
    	return results;
    }
    
    private ItemStack[] stacks = new ItemStack[ 1 ];
}
