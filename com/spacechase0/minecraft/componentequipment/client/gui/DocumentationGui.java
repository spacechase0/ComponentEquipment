package com.spacechase0.minecraft.componentequipment.client.gui;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.spacechase0.minecraft.componentequipment.client.gui.docs.DocumentationCategory;
import com.spacechase0.minecraft.componentequipment.client.gui.docs.GettingStartedCategory;
import com.spacechase0.minecraft.componentequipment.client.gui.docs.MaterialCategory;
import com.spacechase0.minecraft.componentequipment.client.gui.docs.ModifierCategory;
import com.spacechase0.minecraft.componentequipment.client.gui.docs.RecipeCategory;
import com.spacechase0.minecraft.componentequipment.client.gui.docs.ToolCategory;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier.DisplayStack;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class DocumentationGui extends GuiScreen
{
	public DocumentationGui()
	{
		categories.add( new GettingStartedCategory( this ) );
		categories.add( new RecipeCategory( this ) );
		categories.add( new MaterialCategory( this ) );
		categories.add( new ToolCategory( this ) );
		categories.add( new ModifierCategory( this ) );
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
        guiLeft = ( width - BOOK_WIDTH ) / 2;
        guiTop = ( height - BOOK_HEIGHT ) / 2;

        buttonList.add( new GuiButton( GETTING_STARTED_ID, guiLeft - 105, guiTop + 5, 100, 20, TranslateUtils.translate( "gui.componentequipment:docBook.gettingStarted" ) ) );
        buttonList.add( new GuiButton( RECIPES_ID, guiLeft - 105, guiTop + 30, 100, 20, TranslateUtils.translate( "gui.componentequipment:docBook.recipes" ) ) );
        buttonList.add( new GuiButton( MATERIALS_ID, guiLeft - 105, guiTop + 55, 100, 20, TranslateUtils.translate( "gui.componentequipment:docBook.materials" ) ) );
        buttonList.add( new GuiButton( TOOLS_ID, guiLeft - 105, guiTop + 80, 100, 20, TranslateUtils.translate( "gui.componentequipment:docBook.tools" ) ) );
        buttonList.add( new GuiButton( MODIFIERS_ID, guiLeft - 105, guiTop + 105, 100, 20, TranslateUtils.translate( "gui.componentequipment:docBook.modifiers" ) ) );

        buttonList.add( new GuiButton( SCROLL_UP_ID, guiLeft + BOOK_WIDTH + 5, guiTop + 5, 100, 20, TranslateUtils.translate( "gui.componentequipment:docBook.scrollUp" ) ) );
        buttonList.add( new GuiButton( SCROLL_DOWN_ID, guiLeft + BOOK_WIDTH + 5, guiTop + 155, 100, 20, TranslateUtils.translate( "gui.componentequipment:docBook.scrollDown" ) ) );
        
        if ( currCategory == -1 )
        {
        	select( 0 );
        }
	}
	
	@Override
    protected void mouseClicked( int mx, int my, int mb )
	{
		super.mouseClicked( mx, my, mb );
		
		justClicked = true;
	}
	
	@Override
    protected void actionPerformed( GuiButton button )
	{
		if ( button.id <= MODIFIERS_ID )
		{
			select( button.id );
		}
		else if ( button.id == SCROLL_UP_ID )
		{
			--currScroll;
			select( currCategory );
		}
		else if ( button.id == SCROLL_DOWN_ID )
		{
			++currScroll;
			select( currCategory );
		}
		else
		{
			currSection = buttonSectionMapping.get( button.id );
			currPage = 0;
		}
	}
	
	@Override
    public void drawScreen( int mx, int my, float par3 )
    {
        drawDefaultBackground();
    	super.drawScreen( mx, my, par3 );
    	glColor4f( 1.f, 1.f, 1.f, 1.f );
    	
    	TextureManager tm = mc.getTextureManager();
    	
    	tm.bindTexture( BOOK_TEX );
    	drawTexturedModalRect( guiLeft, guiTop, 19, 0, BOOK_WIDTH, BOOK_HEIGHT );
    	
    	if ( currSection.equals( "" ) )
    	{
    		return;
    	}

    	// Page buttons
    	int pageCount = getCurrentCategory().getPageCount( currSection );
    	
		int backX = guiLeft;
		int backY = guiTop + BOOK_HEIGHT + 5;
		int forwardX = guiLeft + BOOK_WIDTH - BUTTON_WIDTH;
		int forwardY = backY;
		
    	drawTexturedModalRect( backX, backY, ( currPage > 0 ) ? 25 : 2, 206, BUTTON_WIDTH, BUTTON_HEIGHT );
    	drawTexturedModalRect( forwardX, forwardY, ( currPage < pageCount - 1 ) ? 25 : 2, 193, BUTTON_WIDTH, BUTTON_HEIGHT );
    	{
    		String str = "(" + ( currPage + 1 ) + "/" + pageCount + ")";
    		int w = ClientUtils.getStringWidth( str );
    		
    		ClientUtils.drawString( str, guiLeft + BOOK_WIDTH / 2 - w / 2, backY + 3, 0xFFFFFFFF );
    	}
    	
    	if ( justClicked )
    	{
        	if ( currPage > 0 && mx >= backX && my >= backY && mx < backX + BUTTON_WIDTH && my < backY + BUTTON_HEIGHT )
        	{
        		--currPage;
        	}
        	else if ( currPage < pageCount - 1 && mx >= forwardX && my >= forwardY && mx < forwardX + BUTTON_WIDTH && my < forwardY + BUTTON_HEIGHT )
        	{
        		++currPage;
        	}
    	}
    	
    	List< DisplayStack > items = new ArrayList< DisplayStack >();
    	
    	// Display page
    	getCurrentCategory().draw( currSection, currPage, items );
    	
    	// Draw items
    	ItemStack tooltip = null;
    	for ( DisplayStack display : items )
    	{
    		int x = display.x;
    		int y = display.y;
    		ItemStack stack = display.getCurrent();
    		
			glColor4f( 1.f, 1.f, 1.f, 1.f );
			glPushMatrix();
			{
				glTranslatef( 0, 0, 250 + display.z );
	            glDisable( GL_LIGHTING );
				
	            renderItem.renderItemAndEffectIntoGUI( mc.fontRenderer, mc.getTextureManager(), stack, x, y );
	            renderItem.renderItemOverlayIntoGUI( mc.fontRenderer, mc.getTextureManager(), stack, x, y, null );
			}
			glPopMatrix();
            
            if ( mx >= x && my >= y && mx < x + 16 && my < y + 16 )
            {
            	tooltip = stack;
            }
    	}
    	
    	if ( tooltip != null )
    	{
            List list = tooltip.getTooltip( mc.thePlayer, mc.gameSettings.advancedItemTooltips );

            for (int k = 0; k < list.size(); ++k)
            {
                if (k == 0)
                {
                    list.set(k, "\u00a7" + tooltip.getRarity().rarityColor.getFormattingCode() + (String)list.get(k));
                }
                else
                {
                    list.set(k, EnumChatFormatting.GRAY + (String)list.get(k));
                }
            }

            FontRenderer font = tooltip.getItem().getFontRenderer(tooltip);
            drawHoveringText(list, mx, my, (font == null ? mc.fontRenderer : font));
    	}
    	
    	justClicked = false;
    }
	
	private void select( int type )
	{
		if ( type > MODIFIERS_ID ) return;
		
		if ( currCategory != type )
		{
			currScroll = 0;
			currSection = "";
			currPage = 0;
		}
		
		buttonSectionMapping.clear();
		if ( currCategory != -1 )
		{
			buttonList.clear();
			initGui();
		}
		currCategory = type;
		
		for ( int i = 0; i < buttonList.size(); ++i )
		{
			GuiButton button = ( GuiButton ) buttonList.get( type );
			button.enabled = ( i != type );
		}
		
		List< String > sections = getCurrentCategory().getSections();
		for ( int i = currScroll; i < sections.size() && i < currScroll + 5; ++i )
		{
			int pos = i - currScroll + 1;
			if ( i < 0 || i >= sections.size() )
			{
				continue;
			}
			String key = sections.get( i );
			
	        buttonList.add( new GuiButton( SCROLL_DOWN_ID + 1 + i, guiLeft + BOOK_WIDTH + 5, guiTop + 5 + 25 * pos, 100, 20, getCurrentCategory().getSectionTitle( key ) ) );
	        buttonSectionMapping.put( SCROLL_DOWN_ID + 1 + i, key );
		}
		
		( ( GuiButton ) buttonList.get( SCROLL_UP_ID ) ).enabled = ( currScroll > 0 );
		( ( GuiButton ) buttonList.get( SCROLL_DOWN_ID ) ).enabled = ( currScroll + 5 < sections.size() );
	}
	
	public DocumentationCategory getCurrentCategory()
	{
		return categories.get( currCategory );
	}
	
	// From GuiContainer
	/*
    private void drawHoveringText(List par1List, int par2, int par3, FontRenderer font)
    {
        if (!par1List.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator iterator = par1List.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                int l = font.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int i1 = par2 + 12;
            int j1 = par3 - 12;
            int k1 = 8;

            if (par1List.size() > 1)
            {
                k1 += 2 + (par1List.size() - 1) * 10;
            }

            if (i1 + k > this.width)
            {
                i1 -= 28 + k;
            }

            if (j1 + k1 + 6 > this.height)
            {
                j1 = this.height - k1 - 6;
            }

            this.zLevel = 300.0F;
            renderItem.zLevel = 300.0F;
            int l1 = -267386864;
            this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
            this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

            for (int k2 = 0; k2 < par1List.size(); ++k2)
            {
                String s1 = (String)par1List.get(k2);
                font.drawStringWithShadow(s1, i1, j1, -1);

                if (k2 == 0)
                {
                    j1 += 2;
                }

                j1 += 10;
            }

            this.zLevel = 0.0F;
            renderItem.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
    */

	public int guiLeft = 0;
	public int guiTop = 0;
	
	private int currCategory = -1;
	private int currScroll = 0;
	private String currSection = "";
	private int currPage = 0;
	
	private boolean justClicked = false;
	
	private List< DocumentationCategory > categories = new ArrayList< DocumentationCategory >();
	private Map< Integer, String > buttonSectionMapping = new HashMap< Integer, String >();
    
	private RenderItem renderItem = new RenderItem();

    public static final ResourceLocation BOOK_TEX = new ResourceLocation( "minecraft:textures/gui/book.png" );
    public static final int BOOK_WIDTH = 148;
    public static final int BOOK_HEIGHT = 182;
    private static final int BUTTON_WIDTH = 20;
    private static final int BUTTON_HEIGHT = 12;

    private static final int GETTING_STARTED_ID = 0;
    private static final int RECIPES_ID = 1;
    private static final int MATERIALS_ID = 2;
    private static final int TOOLS_ID = 3;
    private static final int MODIFIERS_ID = 4;
    
    private static final int SCROLL_UP_ID = 5;
    private static final int SCROLL_DOWN_ID = 6;
}
