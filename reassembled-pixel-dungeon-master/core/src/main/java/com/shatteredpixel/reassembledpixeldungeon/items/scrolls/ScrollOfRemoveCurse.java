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

package com.shatteredpixel.reassembledpixeldungeon.items.scrolls;

import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.reassembledpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.reassembledpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.reassembledpixeldungeon.effects.Flare;
import com.shatteredpixel.reassembledpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.reassembledpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.reassembledpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.reassembledpixeldungeon.items.Item;
import com.shatteredpixel.reassembledpixeldungeon.items.armor.Armor;
import com.shatteredpixel.reassembledpixeldungeon.items.bags.Bag;
import com.shatteredpixel.reassembledpixeldungeon.items.rings.Ring;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.scenes.GameScene;
import com.shatteredpixel.reassembledpixeldungeon.utils.GLog;
import com.shatteredpixel.reassembledpixeldungeon.windows.WndBag;
import com.shatteredpixel.reassembledpixeldungeon.windows.WndOptions;
import com.watabou.utils.Random;

public class ScrollOfRemoveCurse extends InventoryScroll {

	{
		initials = 8;
		mode = WndBag.Mode.UNIDED_OR_CURSED;

	}
	@Override
	protected void onItemSelected(Item item) {
		new Flare( 6, 32 ).show( curUser.sprite, 2f ) ;

		boolean procced = uncurse( curUser, item);

		Weakness.detach( curUser, Weakness.class );

		if (procced) {
			GLog.p( Messages.get(this, "cleansed") );
		}
		else{
			GLog.i( Messages.get(this, "not_cleansed") );
		}
	}



	public static boolean uncurse( Hero hero, Item... items) {
		boolean procced = false;
		for (Item item : items) {
			if (item != null && item.cursed) {
				item.cursed = false;
				procced = true;
			}
			if (item instanceof Weapon){
				Weapon w = (Weapon) item;
				if (w.hasCurseEnchant()){
					w.enchant(null);
					w.cursed = false;
					procced = true;
				}
			}
			if (item instanceof Armor){
				final Armor a = (Armor) item;
				if (a.hasCurseGlyph()){
					a.inscribe(null);
					a.cursed = false;
					procced = true;
				}
				if (a.hasGoodGlyph()){
					boolean stor = a.hasGoodGlyph();
					GameScene.show(
							new WndOptions( Messages.get(ScrollOfRemoveCurse.class, "beneficial"),
									Messages.get(ScrollOfRemoveCurse.class, "sure_delete"),
									Messages.get(ScrollOfRemoveCurse.class, "yes"), Messages.get(ScrollOfRemoveCurse.class, "no") ) {
								@Override
								protected void onSelect(int index) {
									if (index == 0) {
										a.inscribe(null);
										GLog.i( Messages.get(ScrollOfRemoveCurse.class, "benif_removed") );
										Dungeon.hero.sprite.emitter().start(EnergyParticle.FACTORY, 0.05f, 10 );
									}
								};
							}
					);
					if(a.cursed == true){
						procced = true;
					}
					a.cursed = false;
				}
			}
			if (item instanceof Ring && item.level() <= 0 && item.level() > -3){
				item.upgrade(1 - item.level());
			}
			if (item instanceof Ring && item.level() <= -3){
				item.upgrade(2 - item.level());
			}
			if (item instanceof Bag){
				for (Item bagItem : ((Bag)item).items){
					if (bagItem != null && bagItem.cursed) {
						bagItem.cursed = false;
						procced = true;
					}
				}
			}
		}
		
		if (procced) {
			hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
		}

		return procced;
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
