package com.spacechase0.minecraft.componentequipment.addon.thaumcraft.asm;

import java.util.Iterator;
import java.util.ListIterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import static org.objectweb.asm.Opcodes.*;

import com.spacechase0.minecraft.componentequipment.CELog;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedMethod;

import net.minecraft.launchwrapper.IClassTransformer;

public class BorePickaxeCheckTransformer implements IClassTransformer
{
	@Override
	public byte[] transform( String name, String transformedName, byte[] bytes )
	{
		if ( !transformedName.equals( "thaumcraft.common.tiles.TileArcaneBore" ) )
		{
			return bytes;
		}
		CELog.fine( "[Addon_Thaumcraft] Found arcane bore tile entity class." );

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader( bytes );
        classReader.accept( classNode, 0 );
        
        Iterator< MethodNode > it = classNode.methods.iterator();
        while ( it.hasNext() )
        {
        	MethodNode method = it.next();
        	
        	ObfuscatedMethod invChangedFunc = ObfuscatedMethod.fromMcp( "net/minecraft/tileentity/TileEntity", "onInventoryChanged", "()V" );
        	
        	// TODO: Find proper way to do this with above
        	if ( method.name.equals( "func_70296_d" ) )
        	{
        		CELog.fine( "[Addon_Thaumcraft] Found onInventoryChanged()." );
        		changePickaxeReferences( method );
        	}
        }

        ClassWriter writer = new ClassWriter( /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS );
        classNode.accept( writer );
        return writer.toByteArray();
	}
	
	private void changePickaxeReferences( MethodNode method )
	{
		TypeInsnNode instanceCheckNode = null;
    	ListIterator< AbstractInsnNode > it = method.instructions.iterator();
    	while ( it.hasNext() )
    	{
    		AbstractInsnNode insn = it.next();
    		if ( insn.getOpcode() == INSTANCEOF )
    		{
    			instanceCheckNode = ( TypeInsnNode ) insn;
    		}
    	}
    	
    	if ( instanceCheckNode != null && instanceCheckNode.desc.contains( "ItemPickaxe" ) )
    	{
    		CELog.fine( "[Addon_Thaumcraft] Found ( ... instanceof ItemPickaxe ) check, adding a check for our pickaxes as well." );

    		int index = method.instructions.indexOf( instanceCheckNode );
    		AbstractInsnNode ifCheck = method.instructions.get( index + 1 );
    		if ( ifCheck.getOpcode() != IFEQ )
    		{
    			CELog.warning( "[Addon_Thaumcraft] Element after instanceof was not IFEQ like expected. Skipping..." );
    			return;
    		}
    		
    		LabelNode afterChecks = new LabelNode();
    		
    		// I was going to put my check afterwards, but it is easier just to do all of this at once.
    		InsnList list = new InsnList();
    		list.add( new InsnNode( DUP ) ); // Copy the top of the stack, so Azanor's check still works.
    		list.add( new MethodInsnNode( INVOKESTATIC, "com/spacechase0/minecraft/componentequipment/item/HarvesterToolItem", "isPickaxe", "(Lnet/minecraft/item/Item;)Z" ) );
    		list.add( new JumpInsnNode( IFNE, afterChecks ) ); // If the return is true, go to where the label is.
    		method.instructions.insertBefore( instanceCheckNode, list );
    		
    		// Need to pop the stack from above, but they also need to skip my popping if they were both used.
    		LabelNode afterPop = new LabelNode();
    		
    		list = new InsnList();
    		list.add( new JumpInsnNode( GOTO, afterPop ) );
    		list.add( afterChecks ); // This is to skip the ItemPickaxe check
    		list.add( new InsnNode( POP ) );
    		list.add( afterPop );
    		method.instructions.insert( ifCheck, list );
    	}
	}
}
