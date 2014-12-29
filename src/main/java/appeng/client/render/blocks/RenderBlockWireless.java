/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.client.render.blocks;

import java.util.EnumSet;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;
import appeng.api.util.AEColor;
import appeng.block.AEBaseBlock;
import appeng.client.render.BaseBlockRender;
import appeng.client.render.BlockRenderInfo;
import appeng.client.texture.CableBusTextures;
import appeng.client.texture.ExtraBlockTextures;
import appeng.client.texture.OffsetIcon;
import appeng.tile.networking.TileWireless;
import appeng.util.Platform;

public class RenderBlockWireless extends BaseBlockRender
{

	public RenderBlockWireless() {
		super( false, 20 );
	}

	@Override
	public void renderInventory(AEBaseBlock blk, ItemStack is, RenderBlocks renderer, ItemRenderType type, Object[] obj)
	{
		this.blk = blk;
		this.centerX = 0;
		this.centerY = 0;
		this.centerZ = 0;
		this.hasChan = false;
		this.hasPower = false;
		BlockRenderInfo ri = blk.getRendererInstance();
		Tessellator tess = Tessellator.instance;

		renderer.renderAllFaces = true;

		IIcon r = CableBusTextures.PartMonitorSidesStatus.getIcon();
		ri.setTemporaryRenderIcons( r, r, CableBusTextures.PartMonitorSides.getIcon(), CableBusTextures.PartMonitorSides.getIcon(), r, r );
		this.renderBlockBounds( renderer, 5, 5, 0, 11, 11, 1, ForgeDirection.EAST, ForgeDirection.UP, ForgeDirection.SOUTH );
		this.renderInvBlock( EnumSet.allOf( ForgeDirection.class ), blk, is, tess, 0xffffff, renderer );

		r = CableBusTextures.PartWirelessSides.getIcon();
		ri.setTemporaryRenderIcons( r, r, ExtraBlockTextures.BlockWirelessInside.getIcon(), ExtraBlockTextures.BlockWirelessInside.getIcon(), r, r );
		this.renderBlockBounds( renderer, 5, 5, 1, 11, 11, 2, ForgeDirection.EAST, ForgeDirection.UP, ForgeDirection.SOUTH );
		this.renderInvBlock( EnumSet.allOf( ForgeDirection.class ), blk, is, tess, 0xffffff, renderer );

		tess.startDrawingQuads();
		ri.setTemporaryRenderIcon( null );
		this.renderTorchAtAngle( renderer, ForgeDirection.EAST, ForgeDirection.UP, ForgeDirection.SOUTH );
		super.postRenderInWorld( renderer );
		tess.draw();

		ri.setTemporaryRenderIcons( r, r, ExtraBlockTextures.BlockWirelessInside.getIcon(), ExtraBlockTextures.BlockWirelessInside.getIcon(), r, r );

		ForgeDirection sides[] = new ForgeDirection[] { ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.UP, ForgeDirection.DOWN };

		int s = 1;

		for (ForgeDirection side : sides)
		{
			this.renderBlockBounds( renderer, 8 + (side.offsetX != 0 ? side.offsetX * 2 : -2), 8 + (side.offsetY != 0 ? side.offsetY * 2 : -2), 2
					+ (side.offsetZ != 0 ? side.offsetZ * 2 : -1) + s, 8 + (side.offsetX != 0 ? side.offsetX * 4 : 2),
					8 + (side.offsetY != 0 ? side.offsetY * 4 : 2), 2 + (side.offsetZ != 0 ? side.offsetZ * 5 : 1) + s, ForgeDirection.EAST, ForgeDirection.UP,
					ForgeDirection.SOUTH );
			this.renderInvBlock( EnumSet.allOf( ForgeDirection.class ), blk, is, tess, 0xffffff, renderer );
		}

		s = 3;
		for (ForgeDirection side : sides)
		{
			this.renderBlockBounds( renderer, 8 + (side.offsetX != 0 ? side.offsetX * 4 : -1), 8 + (side.offsetY != 0 ? side.offsetY * 4 : -1), 1
					+ (side.offsetZ != 0 ? side.offsetZ * 4 : -1) + s, 8 + (side.offsetX != 0 ? side.offsetX * 5 : 1),
					8 + (side.offsetY != 0 ? side.offsetY * 5 : 1), 2 + (side.offsetZ != 0 ? side.offsetZ * 5 : 1) + s, ForgeDirection.EAST, ForgeDirection.UP,
					ForgeDirection.SOUTH );

			this.renderInvBlock( EnumSet.allOf( ForgeDirection.class ), blk, is, tess, 0xffffff, renderer );
		}
	}

	int centerX = 0;
	int centerY = 0;
	int centerZ = 0;
	AEBaseBlock blk;
	boolean hasChan = false;
	boolean hasPower = false;

	@Override
	public boolean renderInWorld(AEBaseBlock blk, IBlockAccess world, int x, int y, int z, RenderBlocks renderer)
	{
		TileWireless tw = blk.getTileEntity( world, x, y, z );
		this.blk = blk;
		if ( tw != null )
		{
			this.hasChan = (tw.clientFlags & (TileWireless.POWERED_FLAG | TileWireless.CHANNEL_FLAG)) == (TileWireless.POWERED_FLAG | TileWireless.CHANNEL_FLAG);
			this.hasPower = (tw.clientFlags & TileWireless.POWERED_FLAG) == TileWireless.POWERED_FLAG;

			BlockRenderInfo ri = blk.getRendererInstance();

			ForgeDirection fdy = tw.getUp();
			ForgeDirection fdz = tw.getForward();
			ForgeDirection fdx = Platform.crossProduct( fdz, fdy ).getOpposite();

			renderer.renderAllFaces = true;

			IIcon r = CableBusTextures.PartMonitorSidesStatus.getIcon();
			ri.setTemporaryRenderIcons( r, r, CableBusTextures.PartMonitorSides.getIcon(), CableBusTextures.PartMonitorSides.getIcon(), r, r );
			this.renderBlockBounds( renderer, 5, 5, 0, 11, 11, 1, fdx, fdy, fdz );
			super.renderInWorld( blk, world, x, y, z, renderer );

			r = CableBusTextures.PartWirelessSides.getIcon();
			ri.setTemporaryRenderIcons( r, r, ExtraBlockTextures.BlockWirelessInside.getIcon(), ExtraBlockTextures.BlockWirelessInside.getIcon(), r, r );
			this.renderBlockBounds( renderer, 5, 5, 1, 11, 11, 2, fdx, fdy, fdz );
			super.renderInWorld( blk, world, x, y, z, renderer );

			this.centerX = x;
			this.centerY = y;
			this.centerZ = z;
			ri.setTemporaryRenderIcon( null );

			this.renderTorchAtAngle( renderer, fdx, fdy, fdz );
			super.postRenderInWorld( renderer );

			ri.setTemporaryRenderIcons( r, r, ExtraBlockTextures.BlockWirelessInside.getIcon(), ExtraBlockTextures.BlockWirelessInside.getIcon(), r, r );

			ForgeDirection sides[] = new ForgeDirection[] { ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.UP, ForgeDirection.DOWN };

			int s = 1;

			for (ForgeDirection side : sides)
			{
				this.renderBlockBounds( renderer, 8 + (side.offsetX != 0 ? side.offsetX * 2 : -2), 8 + (side.offsetY != 0 ? side.offsetY * 2 : -2), 2
						+ (side.offsetZ != 0 ? side.offsetZ * 2 : -1) + s, 8 + (side.offsetX != 0 ? side.offsetX * 4 : 2),
						8 + (side.offsetY != 0 ? side.offsetY * 4 : 2), 2 + (side.offsetZ != 0 ? side.offsetZ * 5 : 1) + s, fdx, fdy, fdz );
				super.renderInWorld( blk, world, x, y, z, renderer );
			}

			s = 3;
			for (ForgeDirection side : sides)
			{
				this.renderBlockBounds( renderer, 8 + (side.offsetX != 0 ? side.offsetX * 4 : -1), 8 + (side.offsetY != 0 ? side.offsetY * 4 : -1), 1
						+ (side.offsetZ != 0 ? side.offsetZ * 4 : -1) + s, 8 + (side.offsetX != 0 ? side.offsetX * 5 : 1),
						8 + (side.offsetY != 0 ? side.offsetY * 5 : 1), 2 + (side.offsetZ != 0 ? side.offsetZ * 5 : 1) + s, fdx, fdy, fdz );
				super.renderInWorld( blk, world, x, y, z, renderer );
			}

			r = CableBusTextures.PartMonitorSidesStatusLights.getIcon();
			// ri.setTemporaryRenderIcons( r, r, ExtraTextures.BlockChargerInside.getIcon(),
			// ExtraTextures.BlockChargerInside.getIcon(), r, r );
			this.renderBlockBounds( renderer, 5, 5, 0, 11, 11, 1, fdx, fdy, fdz );

			if ( this.hasChan )
			{
				int l = 14;
				Tessellator.instance.setBrightness( l << 20 | l << 4 );
				Tessellator.instance.setColorOpaque_I( AEColor.Transparent.blackVariant );
			}
			else if ( this.hasPower )
			{
				int l = 9;
				Tessellator.instance.setBrightness( l << 20 | l << 4 );
				Tessellator.instance.setColorOpaque_I( AEColor.Transparent.whiteVariant );
			}
			else
			{
				Tessellator.instance.setBrightness( 0 );
				Tessellator.instance.setColorOpaque_I( 0x000000 );
			}

			if ( ForgeDirection.UP != fdz.getOpposite() )
				super.renderFace( x, y, z, blk, r, renderer, ForgeDirection.UP );
			if ( ForgeDirection.DOWN != fdz.getOpposite() )
				super.renderFace( x, y, z, blk, r, renderer, ForgeDirection.DOWN );
			if ( ForgeDirection.EAST != fdz.getOpposite() )
				super.renderFace( x, y, z, blk, r, renderer, ForgeDirection.EAST );
			if ( ForgeDirection.WEST != fdz.getOpposite() )
				super.renderFace( x, y, z, blk, r, renderer, ForgeDirection.WEST );
			if ( ForgeDirection.SOUTH != fdz.getOpposite() )
				super.renderFace( x, y, z, blk, r, renderer, ForgeDirection.SOUTH );
			if ( ForgeDirection.NORTH != fdz.getOpposite() )
				super.renderFace( x, y, z, blk, r, renderer, ForgeDirection.NORTH );

			ri.setTemporaryRenderIcon( null );
			renderer.renderAllFaces = false;
		}

		return true;
	}

	private void renderTorchAtAngle(RenderBlocks renderer, ForgeDirection x, ForgeDirection y, ForgeDirection z)
	{
		IIcon r = (this.hasChan ? CableBusTextures.BlockWirelessOn.getIcon() : this.blk.getIcon( 0, 0 ));
		IIcon sides = new OffsetIcon( r, 0.0f, -2.0f );

		switch (z)
		{
		case DOWN:
			renderer.uvRotateNorth = 3;
			renderer.uvRotateSouth = 3;
			renderer.uvRotateEast = 3;
			renderer.uvRotateWest = 3;
			break;
		case EAST:
			renderer.uvRotateTop = 1;
			renderer.uvRotateBottom = 2;
			renderer.uvRotateEast = 2;
			renderer.uvRotateWest = 1;
			break;
		case NORTH:
			renderer.uvRotateTop = 0;
			renderer.uvRotateBottom = 0;
			renderer.uvRotateNorth = 2;
			renderer.uvRotateSouth = 1;
			break;
		case SOUTH:
			renderer.uvRotateTop = 3;
			renderer.uvRotateBottom = 3;
			renderer.uvRotateNorth = 1;
			renderer.uvRotateSouth = 2;
			break;
		case WEST:
			renderer.uvRotateTop = 2;
			renderer.uvRotateBottom = 1;
			renderer.uvRotateEast = 1;
			renderer.uvRotateWest = 2;
			break;
		default:
			break;
		}

		Tessellator.instance.setColorOpaque_I( 0xffffff );
		this.renderBlockBounds( renderer, 0, 7, 1, 16, 9, 16, x, y, z );
		this.renderFace( this.centerX, this.centerY, this.centerZ, this.blk, sides, renderer, y );
		this.renderFace( this.centerX, this.centerY, this.centerZ, this.blk, sides, renderer, y.getOpposite() );

		this.renderBlockBounds( renderer, 7, 0, 1, 9, 16, 16, x, y, z );
		this.renderFace( this.centerX, this.centerY, this.centerZ, this.blk, sides, renderer, x );
		this.renderFace( this.centerX, this.centerY, this.centerZ, this.blk, sides, renderer, x.getOpposite() );

		this.renderBlockBounds( renderer, 7, 7, 1, 9, 9, 10.6, x, y, z );
		this.renderFace( this.centerX, this.centerY, this.centerZ, this.blk, r, renderer, z );
	}
}
