/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2015
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.GeoStrata.Blocks;

import java.awt.Color;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.GeoStrata.GeoStrata;
import Reika.GeoStrata.TileEntityGeoBlocks;
import Reika.GeoStrata.Items.ItemBlockAnyGeoVariant;
import Reika.GeoStrata.Registry.RockTypes;

public class BlockGeoSlab extends BlockSlab {

	public BlockGeoSlab(Material mat) {
		super(false, mat);
		this.setCreativeTab(GeoStrata.tabGeoSlabs);
		this.setHardness(2);
		this.setResistance(10);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public String func_150002_b(int p_150002_1_) {
		return "";
	}

	@Override
	public IIcon getIcon(int s, int meta) {
		return Blocks.stone.getIcon(0, 0);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity te = world.getTileEntity(x, y, z);
		return te instanceof TileEntityGeoBlocks ? ((TileEntityGeoBlocks)te).getIcon() : Blocks.stone.getIcon(0, 0);
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		return new TileEntityGeoBlocks();
	}

	@Override
	public boolean hasTileEntity(int meta) {
		return true;
	}

	@Override
	public final int colorMultiplier(IBlockAccess iba, int x, int y, int z) {
		TileEntityGeoBlocks te = (TileEntityGeoBlocks)iba.getTileEntity(x, y, z);
		RockTypes rock = te.getRockType();
		//ReikaJavaLibrary.pConsole(rock);
		if (rock == RockTypes.OPAL) {
			int sc = 48;
			float hue1 = (float)(ReikaMathLibrary.py3d(x, y*4, z+x)%sc)/sc;
			//float hue2 = (float)(Math.cos(x/24D)+Math.sin(z/24D))+(y%360)*0.05F;
			return Color.HSBtoRGB(hue1, 0.4F, 1F);
		}
		else {
			return super.colorMultiplier(iba, x, y, z);
		}
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean harvest)
	{
		if (harvest)
			this.harvestBlock(world, player, x, y, z, 0);
		return world.setBlockToAir(x, y, z);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer ep, int x, int y, int z, int meta)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile instanceof TileEntityGeoBlocks) {
			TileEntityGeoBlocks te = (TileEntityGeoBlocks)tile;
			ItemStack is = new ItemStack(this, 1, ItemBlockAnyGeoVariant.getStack(te.getRockType(), te.getRockShape()));
			ReikaItemHelper.dropItem(world, x+0.5, y+0.5, z+0.5, is);
		}
	}

}
