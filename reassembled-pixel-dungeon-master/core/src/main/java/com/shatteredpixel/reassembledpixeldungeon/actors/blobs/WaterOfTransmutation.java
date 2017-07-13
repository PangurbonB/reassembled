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

package com.shatteredpixel.reassembledpixeldungeon.actors.blobs;

import com.shatteredpixel.reassembledpixeldungeon.Journal;
import com.shatteredpixel.reassembledpixeldungeon.Journal.Feature;
import com.shatteredpixel.reassembledpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.reassembledpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.reassembledpixeldungeon.effects.Speck;
import com.shatteredpixel.reassembledpixeldungeon.items.Generator;
import com.shatteredpixel.reassembledpixeldungeon.items.Generator.Category;
import com.shatteredpixel.reassembledpixeldungeon.items.Item;
import com.shatteredpixel.reassembledpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.Potion;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfMight;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.reassembledpixeldungeon.items.rings.Ring;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfMagicalBlessing;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.reassembledpixeldungeon.items.wands.Wand;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.plants.Plant;
import com.watabou.utils.Random;

public class WaterOfTransmutation extends WellWater {
	
	@Override
	protected Item affectItem( Item item ) {
		
		if (item instanceof MagesStaff) {
			item = changeStaff( (MagesStaff)item );
		} else if (item instanceof MeleeWeapon) {
			item = changeWeapon( (MeleeWeapon)item );
		} else if (item instanceof Scroll) {
			item = changeScroll( (Scroll)item );
		} else if (item instanceof Potion) {
			item = changePotion( (Potion)item );
		} else if (item instanceof Ring) {
			item = changeRing( (Ring)item );
		} else if (item instanceof Wand) {
			item = changeWand( (Wand)item );
		} else if (item instanceof Plant.Seed) {
			item = changeSeed( (Plant.Seed)item );
		} else if (item instanceof Artifact) {
			item = changeArtifact( (Artifact)item );
		} else {
			item = null;
		}

		if (item != null) {
			Journal.remove( Feature.WELL_OF_TRANSMUTATION );
		}

		return item;

	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.start( Speck.factory( Speck.CHANGE ), 0.2f, 0 );
	}

	private MagesStaff changeStaff( MagesStaff staff ){
		Class<?extends Wand> wandClass = staff.wandClass();

		if (wandClass == null){
			return null;
		} else {
			Wand n;
			do {
				n = (Wand)Generator.random(Category.WAND);
			} while (n.getClass() == wandClass);
			n.level(0);
			staff.imbueWand(n, null);
		}

		return staff;
	}
	
	private Weapon changeWeapon( MeleeWeapon w ) {
		
		Weapon n;
		Category c = Generator.wepTiers[w.tier-1];

		do {
			try {
				n = (Weapon)c.classes[Random.chances(c.probs)].newInstance();
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
				return null;
			}
		} while (!(n instanceof MeleeWeapon) || n.getClass() == w.getClass());

		int level = w.level();
		if (level > 0) {
			n.upgrade( level );
		} else if (level < 0) {
			n.degrade( -level );
		}

		n.enchantment = w.enchantment;
		n.levelKnown = w.levelKnown;
		n.cursedKnown = w.cursedKnown;
		n.cursed = w.cursed;
		n.imbue = w.imbue;

		return n;

	}
	
	private Ring changeRing( Ring r ) {
		Ring n;
		do {
			n = (Ring)Generator.random( Category.RING );
		} while (n.getClass() == r.getClass());
		
		n.level(0);
		
		int level = r.level();
		if (level > 0) {
			n.upgrade( level );
		} else if (level < 0) {
			n.degrade( -level );
		}
		
		n.levelKnown = r.levelKnown;
		n.cursedKnown = r.cursedKnown;
		n.cursed = r.cursed;
		
		return n;
	}

	private Artifact changeArtifact( Artifact a ) {
		Artifact n = Generator.randomArtifact();

		if (n != null){
			n.cursedKnown = a.cursedKnown;
			n.cursed = a.cursed;
			n.levelKnown = a.levelKnown;
			n.transferUpgrade(a.visiblyUpgraded());
		}

		return n;
	}
	
	private Wand changeWand( Wand w ) {
		
		Wand n;
		do {
			n = (Wand)Generator.random( Category.WAND );
		} while (n.getClass() == w.getClass());
		
		n.level( 0 );
		n.upgrade( w.level() );
		
		n.levelKnown = w.levelKnown;
		n.cursedKnown = w.cursedKnown;
		n.cursed = w.cursed;
		
		return n;
	}
	
	private Plant.Seed changeSeed( Plant.Seed s ) {
		
		Plant.Seed n;
		
		do {
			n = (Plant.Seed)Generator.random( Category.SEED );
		} while (n.getClass() == s.getClass());
		
		return n;
	}
	
	private Scroll changeScroll( Scroll s ) {
		if (s instanceof ScrollOfUpgrade) {
			
			return new ScrollOfMagicalInfusion();
			
		} else if (s instanceof ScrollOfMagicalInfusion) {
			
			return new ScrollOfMagicalBlessing();
			
		} else {
			
			Scroll n;
			do {
				n = (Scroll)Generator.random( Category.SCROLL );
			} while (n.getClass() == s.getClass());
			return n;
		}
	}
	
	private Potion changePotion( Potion p ) {
		if (p instanceof PotionOfStrength) {
			
			return new PotionOfMight();
			
		} else if (p instanceof PotionOfMight) {
			
			return new PotionOfStrength();
			
		} else {
			
			Potion n;
			do {
				n = (Potion)Generator.random( Category.POTION );
			} while (n.getClass() == p.getClass());
			return n;
		}
	}
	
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
