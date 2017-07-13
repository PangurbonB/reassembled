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

package com.shatteredpixel.reassembledpixeldungeon.levels.features;

import com.shatteredpixel.reassembledpixeldungeon.Challenges;
import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.reassembledpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.reassembledpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.reassembledpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.reassembledpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.reassembledpixeldungeon.items.Dewdrop;
import com.shatteredpixel.reassembledpixeldungeon.items.Generator;
import com.shatteredpixel.reassembledpixeldungeon.items.Item;
import com.shatteredpixel.reassembledpixeldungeon.items.armor.glyphs.Camouflage;
import com.shatteredpixel.reassembledpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.reassembledpixeldungeon.levels.Level;
import com.shatteredpixel.reassembledpixeldungeon.levels.Terrain;
import com.shatteredpixel.reassembledpixeldungeon.plants.BlandfruitBush;
import com.shatteredpixel.reassembledpixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public class HighGrass {

	public static void trample( Level level, int pos, Char ch ) {
		
		Level.set( pos, Terrain.GRASS );
		GameScene.updateMap( pos );

		if (!Dungeon.isChallenged( Challenges.NO_HERBALISM )) {
			int naturalismLevel = 0;

			if (ch != null) {
				SandalsOfNature.Naturalism naturalism = ch.buff( SandalsOfNature.Naturalism.class );
				if (naturalism != null) {
					if (!naturalism.isCursed()) {
						naturalismLevel = naturalism.itemLevel() + 1;
						naturalism.charge();
					} else {
						naturalismLevel = -1;
					}
				}
			}

			if (naturalismLevel >= 0) {
				// Seed, scales from 1/16 to 1/4
				if (Random.Int(16 - ((int) (naturalismLevel * 3))) == 0) {
					Item seed = Generator.random(Generator.Category.SEED);

					if (seed instanceof BlandfruitBush.Seed) {
						if (Random.Int(15) - Dungeon.limitedDrops.blandfruitSeed.count >= 0) {
							level.drop(seed, pos).sprite.drop();
							Dungeon.limitedDrops.blandfruitSeed.count++;
						}
					} else
						level.drop(seed, pos).sprite.drop();
				}

				// Dew, scales from 1/6 to 1/3
				if (Random.Int(24 - naturalismLevel*3) <= 3) {
					level.drop(new Dewdrop(), pos).sprite.drop();
				}
			}
		}

		int leaves = 4;
		

		if (ch instanceof Hero) {
			Hero hero = (Hero)ch;

			// Barkskin
			if (hero.subClass == HeroSubClass.WARDEN) {
				Buff.affect(ch, Barkskin.class).level(ch.HT / 3);
				leaves += 4;
			}

			//Camouflage
			if (hero.belongings.armor != null && hero.belongings.armor.hasGlyph(Camouflage.class)){
				Buff.affect(hero, Camouflage.Camo.class).set(3 + hero.belongings.armor.level());
				leaves += 4;
			}
		}
		
		CellEmitter.get( pos ).burst( LeafParticle.LEVEL_SPECIFIC, leaves );
		if (Dungeon.visible[pos])
			Dungeon.observe();
	}
}
