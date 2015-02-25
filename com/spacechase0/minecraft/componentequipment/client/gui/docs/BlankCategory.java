package com.spacechase0.minecraft.componentequipment.client.gui.docs;

import java.util.ArrayList;
import java.util.List;


import com.spacechase0.minecraft.componentequipment.client.gui.DocumentationGui;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier.DisplayStack;

public class BlankCategory extends DocumentationCategory
{
	public BlankCategory( DocumentationGui theGui, String theTitle )
	{
		super( theGui );
		title = theTitle;
	}
	
	@Override
	public String getName()
	{
		return title;
	}

	@Override
	public List< String > getSections()
	{
		return new ArrayList< String >();
	}
	
	@Override
	public String getSectionTitle( String section )
	{
		return section;
	}

	@Override
	public int getPageCount( String section )
	{
		return 1;
	}

	@Override
	public void draw( String section, int page, List<DisplayStack> stacksToDraw )
	{
	}
	
	private final String title;
}
