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

package com.shatteredpixel.reassembledpixeldungeon.levels.traps;

import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.reassembledpixeldungeon.items.Item;
import com.shatteredpixel.reassembledpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.reassembledpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.reassembledpixeldungeon.scenes.InterlevelScene;
import com.watabou.noosa.Game;

public class DistortionTrap extends Trap{

	{
		color = TEAL;
		shape = LARGE_DOT;
	}

	@Override
	public void activate() {
		InterlevelScene.returnDepth = Dungeon.depth;
		Belongings belongings = Dungeon.hero.belongings;
		belongings.ironKeys[Dungeon.depth] = 0;
		belongings.specialKeys[Dungeon.depth] = 0;
		for (Item i : belongings){
			if (i instanceof LloydsBeacon && ((LloydsBeacon) i).returnDepth == Dungeon.depth)
					((LloydsBeacon) i).returnDepth = -1;
		}

		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] ))
			if (mob instanceof DriedRose.GhostHero) mob.destroy();

		InterlevelScene.mode = InterlevelScene.Mode.RESET;
		Game.switchScene(InterlevelScene.class);
	}
}