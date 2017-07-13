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

package com.shatteredpixel.reassembledpixeldungeon.items.armor.curses;

import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Actor;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Bestiary;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.reassembledpixeldungeon.items.armor.Armor;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.reassembledpixeldungeon.levels.Level;
import com.shatteredpixel.reassembledpixeldungeon.scenes.GameScene;
import com.shatteredpixel.reassembledpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Multiplicity extends Armor.Glyph {

	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		if (Random.Int(20) == 0){
			ArrayList<Integer> spawnPoints = new ArrayList<>();

			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = defender.pos + PathFinder.NEIGHBOURS8[i];
				if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
					spawnPoints.add( p );
				}
			}

			if (spawnPoints.size() > 0) {

				Mob m = null;
				if (Random.Int(2) == 0 && defender instanceof Hero){
					m = new MirrorImage();
					((MirrorImage)m).duplicate( (Hero)defender );

				} else {
					//FIXME should probably have a mob property for this
					if (attacker.properties().contains(Char.Property.BOSS) || attacker.properties().contains(Char.Property.MINIBOSS)
							|| attacker instanceof Mimic || attacker instanceof Statue){
						m = Bestiary.mutable(Dungeon.depth % 5 == 0 ? Dungeon.depth - 1 : Dungeon.depth);
					} else {
						try {
							m = (Mob)attacker.getClass().newInstance();
							Bundle store = new Bundle();
							attacker.storeInBundle(store);
							m.restoreFromBundle(store);
							m.HP = m.HT;

							//If a thief has stolen an item, that item is not duplicated.
							if (m instanceof Thief){
								((Thief) m).item = null;
							}

						} catch (Exception e) {
							ShatteredPixelDungeon.reportException(e);
							m = null;
						}
					}

				}

				if (m != null) {
					GameScene.add(m);
					ScrollOfTeleportation.appear(m, Random.element(spawnPoints));
				}

			}
		}

		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}

	@Override
	public boolean curse() {
		return true;
	}
}