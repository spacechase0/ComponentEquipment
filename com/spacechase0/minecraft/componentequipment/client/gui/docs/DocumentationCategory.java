package com.spacechase0.minecraft.componentequipment.client.gui.docs;

import java.util.List;


import com.spacechase0.minecraft.componentequipment.client.gui.DocumentationGui;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier.DisplayStack;

public abstract class DocumentationCategory
{
	public DocumentationCategory( DocumentationGui theGui )
	{
		gui = theGui;
	}
	
	public abstract String getName();
	
	public abstract List< String > getSections();
	public abstract String getSectionTitle( String section );
	public abstract int getPageCount( String section );

	public abstract void draw( String section, int page, List< DisplayStack > stacksToDraw );
	
	protected final DocumentationGui gui;
}
