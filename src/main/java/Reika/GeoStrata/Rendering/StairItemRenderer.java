/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2015
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.GeoStrata.Rendering;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.GeoStrata.Items.ItemBlockAnyGeoVariant;
import Reika.GeoStrata.Registry.RockShapes;
import Reika.GeoStrata.Registry.RockTypes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StairItemRenderer implements IItemRenderer {

	private final DummyBlock dummy;

	public StairItemRenderer() {
		dummy = new DummyBlock();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		ItemBlockAnyGeoVariant i = (ItemBlockAnyGeoVariant)item.getItem();
		RockTypes r = i.getRock(item);
		RockShapes s = i.getShape(item);
		IIcon ico = r.getIcon(s);
		dummy.icon = ico;
		RenderBlocks rb = (RenderBlocks)data[0];
		Block base = i.field_150939_a;
		dummy.renderType = base instanceof BlockStairs ? 10 : 0;
		dummy.setBounds(base instanceof BlockSlab);
		dummy.rock = r;
		rb.renderBlockAsItem(dummy, 1, 1);
	}

	private static class DummyBlock extends Block {

		private int renderType;
		private IIcon icon;
		private RockTypes rock;

		protected DummyBlock() {
			super(Material.rock);
		}

		@Override
		public int getRenderType() {
			return renderType;
		}

		private void setBounds(boolean half) {
			maxY = half ? 0.5F : 1F;
		}

		@Override
		public IIcon getIcon(int s, int meta) {
			return icon;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public final int getRenderColor(int dmg) {
			EntityPlayer ep = Minecraft.getMinecraft().thePlayer;
			int x = MathHelper.floor_double(ep.posX);
			int y = MathHelper.floor_double(ep.posY);
			int z = MathHelper.floor_double(ep.posZ);
			if (rock == RockTypes.OPAL) {
				int sc = 48;
				float hue1 = (float)(ReikaMathLibrary.py3d(x, y*4, z+x)%sc)/sc;
				//float hue2 = (float)(Math.cos(x/24D)+Math.sin(z/24D))+(y%360)*0.05F;
				return Color.HSBtoRGB(hue1, 0.4F, 1F);
			}
			else {
				return super.getRenderColor(dmg);
			}
		}

		@Override
		public int tickRate(World p_149738_1_)
		{
			return 20;
		}

		@Override
		public boolean isReplaceableOreGen(World world, int x, int y, int z, Block target)
		{
			return true;
		}

	}

}
