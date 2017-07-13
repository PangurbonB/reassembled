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

package com.shatteredpixel.reassembledpixeldungeon.actors.buffs;

import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.reassembledpixeldungeon.items.Item;
import com.shatteredpixel.reassembledpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.reassembledpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.Potion;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfMight;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.reassembledpixeldungeon.items.rings.RingOfElements.Resistance;
import com.shatteredpixel.reassembledpixeldungeon.levels.Level;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.reassembledpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.reassembledpixeldungeon.utils.GLog;

public class Frost extends FlavourBuff {

	private static final float DURATION	= 5f;

	{
		type = buffType.NEGATIVE;
	}
	
	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			
			target.paralysed++;
			Buff.detach( target, Burning.class );
			Buff.detach( target, Chill.class );

			if (target instanceof Hero) {

				Hero hero = (Hero)target;
				Item item = hero.belongings.randomUnequipped();
				if (item instanceof Potion
						&& !(item instanceof PotionOfStrength || item instanceof PotionOfMight)) {

					item = item.detach( hero.belongings.backpack );
					GLog.w( Messages.get(this, "freezes", item.toString()) );
					((Potion) item).shatter(hero.pos);

				} else if (item instanceof MysteryMeat) {

					item = item.detach( hero.belongings.backpack );
					FrozenCarpaccio carpaccio = new FrozenCarpaccio();
					if (!carpaccio.collect( hero.belongings.backpack )) {
						Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
					}
					GLog.w( Messages.get(this, "freezes", item.toString()) );

				}
			} else if (target instanceof Thief) {

				Item item = ((Thief) target).item;

				if (item instanceof Potion && !(item instanceof PotionOfStrength || item instanceof PotionOfMight)) {
					((Potion) ((Thief) target).item).shatter(target.pos);
					((Thief) target).item = null;
				}

			}

			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		if (target.paralysed > 0)
			target.paralysed--;
		if (Level.water[target.pos])
			Buff.prolong(target, Chill.class, 4f);
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FROST;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.FROZEN);
		else target.sprite.remove(CharSprite.State.FROZEN);
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}

	public static float duration( Char ch ) {
		Resistance r = ch.buff( Resistance.class );
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}
}