package com.spacechase0.minecraft.componentequipment.client.gui.docs;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.client.gui.DocumentationGui;
import com.spacechase0.minecraft.componentequipment.item.PartItem;
import com.spacechase0.minecraft.componentequipment.tool.Material;
import com.spacechase0.minecraft.componentequipment.tool.MaterialData;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier.DisplayStack;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class MaterialCategory extends DocumentationCategory
{
	public MaterialCategory( DocumentationGui theGui )
	{
		super( theGui );
	}
	
	@Override
	public String getName()
	{
		return "materials";
	}

	@Override
	public List< String > getSections()
	{
		return Arrays.asList( Material.getTypes() );
	}
	
	@Override
	public String getSectionTitle( String section )
	{
		return TranslateUtils.translate( "componentequipment:material." + section );
	}

	@Override
	public int getPageCount( String section )
	{
		return 1;
	}

	@Override
	public void draw( String section, int page, List< DisplayStack > stacksToDraw )
	{
		MaterialData mat = Material.getData( section );

		drawString( 20, 17 + 0 * 20, "baseDur", mat.getBaseDurability() );
		drawString( 20, 17 + 1 * 10, "toolDurMult", mat.getToolMultiplier() );
		drawString( 20, 17 + 2 * 10, "armorDurMult", mat.getArmorDurabilityMultiplier() );

		drawString( 20, 17 + 4 * 10, "speed", mat.getMiningSpeed() );
		drawString( 20, 17 + 5 * 10, "level", mat.getMiningLevel() );
		drawString( 20, 17 + 6 * 10, "damage", mat.getAttackDamage() );

		drawString( 20, 17 + 8 * 10, "totalArmor", mat.getTotalArmor() );
		
		drawString( 20, 17 + 11 * 10, "special", TranslateUtils.translate( "componentequipment:special." + mat.getSpecialAbility() ) );

		stacksToDraw.add( new DisplayStack( gui.guiLeft + 48, gui.guiTop + DocumentationGui.BOOK_HEIGHT - 40, mat.getCraftingMaterials() ) );
		stacksToDraw.add( new DisplayStack( gui.guiLeft + DocumentationGui.BOOK_WIDTH - 48 - 16, gui.guiTop + DocumentationGui.BOOK_HEIGHT - 40, PartItem.getAllStacksOfMaterial( mat.getType() ) ) );
	}
	
	private void drawString( int x, int y, String str, Object... params )
	{
		ClientUtils.drawString( TranslateUtils.translate( "gui.componentequipment:docBook.materials." + str, params ), gui.guiLeft + x, gui.guiTop + y, 0 );
	}
}
