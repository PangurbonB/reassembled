/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.reassembledpixeldungeon.levels;

import com.shatteredpixel.reassembledpixeldungeon.Assets;
import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.BeeTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.FloodTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.SlowTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.GoldSpearTrap;
import com.shatteredpixel.reassembledpixeldungeon.tiles.DungeonTileSheet;
import com.shatteredpixel.reassembledpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.reassembledpixeldungeon.levels.Room.Type;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.LightningTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.SpearTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.VenomTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.reassembledpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class CityLevel extends RegularLevel {

	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CITY;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CITY;
	}
	
	protected boolean[] water() {
		return Patch.generate( this, feeling == Feeling.WATER ? 0.65f : 0.45f, 4 );
	}
	
	protected boolean[] grass() {
		return Patch.generate( this, feeling == Feeling.GRASS ? 0.60f : 0.40f, 3 );
	}

	@Override
	protected Class<?>[] trapClasses() {
		return new Class[]{ BlazingTrap.class, FrostTrap.class, SpearTrap.class, VenomTrap.class, GoldSpearTrap.class, SlowTrap.class,
				ExplosiveTrap.class, GrippingTrap.class, LightningTrap.class, RockfallTrap.class, OozeTrap.class, WeakeningTrap.class,
				CursingTrap.class, FlockTrap.class, GuardianTrap.class, PitfallTrap.class, SummoningTrap.class, TeleportationTrap.class, BeeTrap.class,
				DisarmingTrap.class, WarpingTrap.class, FloodTrap.class};
	}

	@Override
	protected float[] trapChances() {
		return new float[]{ 8, 8, 8, 8, 8,
				4, 4, 4, 4, 4, 4,
				2, 2, 2, 2, 2, 2, 2,
				1, 1, (float).5 };
	}
	
	@Override
	protected boolean assignRoomType() {
		if (!super.assignRoomType()) return false;
		
		for (Room r : rooms) {
			if (r.type == Type.TUNNEL) {
				r.type = Type.PASSAGE;
			}
		}

		return true;
	}
	
	@Override
	protected void decorate() {
		
		for (int i=0; i < length() - width(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
				map[i] = Terrain.EMPTY_DECO;
			} else if (map[i] == Terrain.WALL
					&& DungeonTileSheet.floorTile(map[i + width()])
					&& Random.Int( 21 - Dungeon.depth ) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		placeSign();
	}
	
	@Override
	protected void createItems() {
		super.createItems();
		
		Imp.Quest.spawn( this );
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(CityLevel.class, "water_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CityLevel.class, "high_grass_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(CityLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(CityLevel.class, "exit_desc");
			case Terrain.WALL_DECO:
			case Terrain.EMPTY_DECO:
				return Messages.get(CityLevel.class, "deco_desc");
			case Terrain.EMPTY_SP:
				return Messages.get(CityLevel.class, "sp_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(CityLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CityLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		addCityVisuals( this, visuals );
		return visuals;
	}

	public static void addCityVisuals( Level level, Group group ) {
		for (int i=0; i < level.length(); i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				group.add( new Smoke( i ) );
			}
		}
	}
	
	private static class Smoke extends Emitter {
		
		private int pos;
		
		private static final Emitter.Factory factory = new Factory() {
			
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				SmokeParticle p = (SmokeParticle)emitter.recycle( SmokeParticle.class );
				p.reset( x, y );
			}
		};
		
		public Smoke( int pos ) {
			super();
			
			this.pos = pos;
			
			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 6, p.y - 4, 12, 12 );
			
			pour( factory, 0.2f );
		}
		
		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {
				super.update();
			}
		}
	}
	
	public static final class SmokeParticle extends PixelParticle {
		
		public SmokeParticle() {
			super();
			
			color( 0x000000 );
			speed.set( Random.Float( -2, 4 ), -Random.Float( 3, 6 ) );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan = 2f;
		}
		
		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.8f ? 1 - p : p * 0.25f;
			size( 6 - p * 3 );
		}
	}
}