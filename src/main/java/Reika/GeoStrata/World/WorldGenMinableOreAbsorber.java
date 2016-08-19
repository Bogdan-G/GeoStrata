/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2015
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.GeoStrata.World;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import Reika.DragonAPI.Libraries.World.ReikaBlockHelper;
import Reika.GeoStrata.TileEntityGeoOre;
import Reika.GeoStrata.Registry.GeoBlocks;
import Reika.GeoStrata.Registry.RockShapes;
import Reika.GeoStrata.Registry.RockTypes;

public class WorldGenMinableOreAbsorber extends WorldGenerator {

	private final int size;
	private final RockTypes rock;
	private final Block overwrite;
	private final Block id;

	public WorldGenMinableOreAbsorber(RockTypes r, int size) {
		this.size = size;
		overwrite = Blocks.stone;
		rock = r;
		id = rock.getID(RockShapes.SMOOTH);
	}

	@Override
	public boolean generate(World world, Random r, int x, int y, int z) {
		int count = 0;
		float f = r.nextFloat() * (float)Math.PI;
		//>_> One-standard code, I see not in the first mode, and up to the end, and this is not full optimize
		//reduce the number of arithmetic and use only the float, it may be obvious, but it will increase the speed of ~30%
		float temp1 = size / 8.0F;
		float temp1_s = MathHelper.sin(f) * temp1;
		float temp1_c = MathHelper.cos(f) * temp1;
		float d0 = x + 8 + temp1_s;
		//float d1 = x + 8 - temp1_s;
		float d2 = z + 8 + temp1_c;
		//float d3 = z + 8 - temp1_c;
		int dtemp1 = r.nextInt(3);
		int dtemp2 = r.nextInt(3);
		float d4 = y + dtemp1 - 2;
		int d5 = dtemp2 - dtemp1;

		for (int l = 0; l <= size; ++l)
		{
			float ltemp = l / (float)size;
			float d6 = d0 + (-2*temp1_s) * ltemp;
			float d7 = d4 + (d5) * ltemp;
			float d8 = d2 + (-2*temp1_c) * ltemp;
			float d9 = r.nextFloat() * size / 16.0F;
			float d10 = (MathHelper.sin(l * (float)Math.PI / size) + 1.0F) * d9 + 1.0F;
			//float d11 = (MathHelper.sin(l * (float)Math.PI / size) + 1.0F) * d9 + 1.0F;
			float temp2 = d10 / 2.0F;
			int i1 = MathHelper.floor_float(d6 - temp2);
			int j1 = MathHelper.floor_float(d7 - temp2);
			int k1 = MathHelper.floor_float(d8 - temp2);
			int l1 = MathHelper.floor_float(d6 + temp2);
			int i2 = MathHelper.floor_float(d7 + temp2);
			int j2 = MathHelper.floor_float(d8 + temp2);

			for (int dx = i1; dx <= l1; dx++) {
				float d12 = (dx + 0.5F - d6) / (temp2);
				float d12d = d12 * d12;
				if (d12d < 1.0F) {
					for (int dy = j1; dy <= i2; dy++) {
						float d13 = (dy + 0.5F - d7) / (temp2);
						float d13d = d13 * d13;
						if (d12d + d13d < 1.0F) {
							for (int dz = k1; dz <= j2; dz++) {
								float d14 = (dz + 0.5F - d8) / (temp2);
								if (d12d + d13d + d14 * d14 < 1.0F) {
									Block b = world.getBlock(dx, dy, dz);
									int meta = world.getBlockMetadata(dx, dy, dz);
									if (b.isReplaceableOreGen(world, dx, dy, dz, overwrite)) {
										world.setBlock(dx, dy, dz, id, 0, 2);
										count++;
									}
									else if (RockGenerator.instance.generateOres() && ReikaBlockHelper.isOre(b, meta)) {
										TileEntityGeoOre te = new TileEntityGeoOre();
										te.initialize(rock, b, meta);
										world.setBlock(dx, dy, dz, GeoBlocks.ORETILE.getBlockInstance());
										world.setTileEntity(dx, dy, dz, te);
									}
								}
							}
						}
					}
				}
			}
		}

		return count > 0;
	}

}
