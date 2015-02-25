package com.spacechase0.minecraft.componentequipment.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModificationStandModel extends ModelBase
{
	public ModificationStandModel()
	{
		base = new ModelRenderer( this, 0, 0 );
		base.addBox( 4, 0, 4, 8, 1, 8 );
		
		peg = new ModelRenderer( this, 0, 0 );
		peg.addBox( 8, 1, 8, 1, 7, 1 );
	}
	
	public void render()
	{
		base.render( 1.f / 16 );
	}
	
	private ModelRenderer base;
	private ModelRenderer peg;
}
