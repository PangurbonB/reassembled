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

package com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee;

import com.shatteredpixel.reassembledpixeldungeon.Assets;
import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Actor;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.reassembledpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.reassembledpixeldungeon.items.Heap;
import com.shatteredpixel.reassembledpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.enchantments.Ethereal;
import com.shatteredpixel.reassembledpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.reassembledpixeldungeon.items.Item;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.reassembledpixeldungeon.levels.Level;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class MeleeWeapon extends Weapon{

	Hero hero = Dungeon.hero;
	int bonus;
	public int tier;

	@Override
	public int throwPos(Hero user, int dst){
		if (hasEnchant(Projecting.class)
				&& !Level.solid[dst] && Dungeon.level.distance(user.pos, dst) <= 4){
			return dst;
		} else {
			return super.throwPos(user, dst);
		}
	}


	//Code for the new class path for Huntress. Makes thrown weapons do damage.
	@Override
	protected void onThrow( int cell ) {
		Char enemy = Actor.findChar( cell );
		if (enemy == null || enemy == curUser || hero.subClass != HeroSubClass.SLINGER) {
			super.onThrow( cell );
		}
		else{
			float defRoll = Random.Float( enemy.defenseSkill( enemy ) );
			float acuRoll =  Random.Float((int)(hero.attackSkill * this.accuracyFactor( hero )));

			if (hero.buff(Bless.class) != null) acuRoll *= 1.20f;
			if (enemy.buff(Bless.class) != null) defRoll *= 1.20f;
			if (Dungeon.level.distance( hero.pos, enemy.pos ) == 1) {
				acuRoll *= 0.5f;
			}

			boolean didHit = acuRoll >= defRoll;

			if(didHit){
				bonus = RingOfForce.getBonus(hero, RingOfForce.Force.class);
				int dmg = this.damageRoll( hero ) + bonus;
				if (dmg < 0) dmg = 0;
				if(!this.hasEnchant(Ethereal.class)) {
					int dr = enemy.drRoll();
					int effectiveDamage = Math.max( dmg - dr, 0 );

					effectiveDamage = hero.attackProc( enemy, effectiveDamage );
					effectiveDamage = enemy.defenseProc( hero, effectiveDamage );

					enemy.damage(effectiveDamage, hero);
				}
				else{
					enemy.damage(dmg, hero);
				}
			}
			else{
				String defense = enemy.defenseVerb();
				enemy.sprite.showStatus( CharSprite.NEUTRAL, defense );

				Sample.INSTANCE.play(Assets.SND_MISS);
			}
			//hero.spend(this.speedFactor(hero));
			Heap heap = Dungeon.level.drop( this, cell );
			if (!heap.isEmpty()) {
				heap.sprite.drop( cell );
			}
		}
	}

	@Override
	public int min(int lvl) {
		return  tier +  //base
				lvl;    //level scaling
	}

	@Override
	public int max(int lvl) {
		return  5*(tier+1) +    //base
				lvl*(tier+1);   //level scaling
	}

	@Override
	public Item upgrade() {
		return upgrade( false );
	}
	
	public Item safeUpgrade() {
		return upgrade( enchantment != null );
	}

	public int STRReq(int lvl){
		lvl = Math.max(0, lvl);
		//strength req decreases at +1,+3,+6,+10,etc.
		return (8 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}
	
	@Override
	public String info() {

		String info = desc();

		if (levelKnown) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, imbue.damageFactor(min()), imbue.damageFactor(max()), STRReq());
			if (STRReq() > Dungeon.hero.STR()) {
				info += " " + Messages.get(Weapon.class, "too_heavy");
			} else if (Dungeon.hero.STR() > STRReq()){
				info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
			}
		} else {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
			if (STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
			}
		}

		String stats_desc = Messages.get(this, "stats_desc");
		if (!stats_desc.equals("")) info+= "\n\n" + stats_desc;

		switch (imbue) {
			case LIGHT:
				info += "\n\n" + Messages.get(Weapon.class, "lighter");
				break;
			case HEAVY:
				info += "\n\n" + Messages.get(Weapon.class, "heavier");
				break;
			case NONE:
		}

		if (enchantment != null && (cursedKnown || !enchantment.curse())){
			info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
			info += " " + Messages.get(enchantment, "desc");
		}

		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		}
		
		return info;
	}
	
	@Override
	public int price() {
		int price = 20 * tier;
		if (hasGoodEnchant()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseEnchant())) {
			price /= 2;
		}
		if (levelKnown && level() > 0) {
			price *= (level() + 1);
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

}
