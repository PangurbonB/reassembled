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

package com.shatteredpixel.reassembledpixeldungeon.actors.hero;

import com.shatteredpixel.reassembledpixeldungeon.Assets;
import com.shatteredpixel.reassembledpixeldungeon.Badges;
import com.shatteredpixel.reassembledpixeldungeon.Challenges;
import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.reassembledpixeldungeon.items.Item;
import com.shatteredpixel.reassembledpixeldungeon.items.Stylus;
import com.shatteredpixel.reassembledpixeldungeon.items.TomeOfMastery;
import com.shatteredpixel.reassembledpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.reassembledpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.reassembledpixeldungeon.items.bags.Bag;
import com.shatteredpixel.reassembledpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.reassembledpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.reassembledpixeldungeon.items.bags.SeedPouch;
import com.shatteredpixel.reassembledpixeldungeon.items.bags.WandHolster;
import com.shatteredpixel.reassembledpixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.reassembledpixeldungeon.items.food.Food;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.Potion;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfMight;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfOoze;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfMagicalBlessing;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.reassembledpixeldungeon.items.wands.Wand;
import com.shatteredpixel.reassembledpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.reassembledpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.reassembledpixeldungeon.items.wands.WandOfTranslocation;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.DevAxe;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.Greataxe;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.Knuckles;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.missiles.Boomerang;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.missiles.Dart;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.plants.Oozenettle;
import com.shatteredpixel.reassembledpixeldungeon.plants.Plant;
import com.watabou.utils.Bundle;

public enum HeroClass {

	WARRIOR( "warrior" ),
	MAGE( "mage" ),
	ROGUE( "rogue" ),
	HUNTRESS( "huntress" );

	private String title;

	HeroClass( String title ) {
		this.title = title;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;
		}

		hero.updateAwareness();
	}

	private static void initCommon( Hero hero ) {
		if (!Dungeon.isChallenged(Challenges.NO_ARMOR))
			(hero.belongings.armor = new ClothArmor()).identify();

		if (!Dungeon.isChallenged(Challenges.NO_FOOD))
			new Food().identify().collect();
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		Dart darts = new Dart( 8 );
		darts.identify().collect();

		Scroll scroll = new ScrollOfMagicalBlessing();
		scroll.identify().collect();
		scroll = new ScrollOfMagicalInfusion();
		scroll.identify().collect();

		if ( Badges.isUnlocked(Badges.Badge.TUTORIAL_WARRIOR) ){
			if (!Dungeon.isChallenged(Challenges.NO_ARMOR))
				hero.belongings.armor.affixSeal(new BrokenSeal());
			Dungeon.quickslot.setSlot(0, darts);
		} else {
			if (!Dungeon.isChallenged(Challenges.NO_ARMOR)) {
				BrokenSeal seal = new BrokenSeal();
				seal.collect();
				Dungeon.quickslot.setSlot(0, seal);
			}
			Dungeon.quickslot.setSlot(1, darts);
		}

		new PotionOfHealing().setKnown();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		if ( Badges.isUnlocked(Badges.Badge.TUTORIAL_MAGE) ){
			staff = new MagesStaff(new WandOfMagicMissile());
		} else {
			staff = new MagesStaff();
			new WandOfMagicMissile().identify().collect();
		}

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfUpgrade().setKnown();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.misc1 = cloak).identify();
		hero.belongings.misc1.activate( hero );

		Dart darts = new Dart( 8 );
		darts.identify().collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, darts);

		new ScrollOfMagicMapping().setKnown();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Knuckles()).identify();
		Boomerang boomerang = new Boomerang();
		boomerang.identify().collect();

		Dungeon.quickslot.setSlot(0, boomerang);

		new PotionOfMindVision().setKnown();
	}
	
	public String title() {
		return Messages.get(HeroClass.class, title);
	}
	
	public String spritesheet() {
		
		switch (this) {
		case WARRIOR:
			return Assets.WARRIOR;
		case MAGE:
			return Assets.MAGE;
		case ROGUE:
			return Assets.ROGUE;
		case HUNTRESS:
			return Assets.HUNTRESS;
		}
		
		return null;
	}
	
	public String[] perks() {
		
		switch (this) {
		case WARRIOR:
			return new String[]{
					Messages.get(HeroClass.class, "warrior_perk1"),
					Messages.get(HeroClass.class, "warrior_perk2"),
					Messages.get(HeroClass.class, "warrior_perk3"),
					Messages.get(HeroClass.class, "warrior_perk4"),
					Messages.get(HeroClass.class, "warrior_perk5"),
			};
		case MAGE:
			return new String[]{
					Messages.get(HeroClass.class, "mage_perk1"),
					Messages.get(HeroClass.class, "mage_perk2"),
					Messages.get(HeroClass.class, "mage_perk3"),
					Messages.get(HeroClass.class, "mage_perk4"),
					Messages.get(HeroClass.class, "mage_perk5"),
			};
		case ROGUE:
			return new String[]{
					Messages.get(HeroClass.class, "rogue_perk1"),
					Messages.get(HeroClass.class, "rogue_perk2"),
					Messages.get(HeroClass.class, "rogue_perk3"),
					Messages.get(HeroClass.class, "rogue_perk4"),
					Messages.get(HeroClass.class, "rogue_perk5"),
					Messages.get(HeroClass.class, "rogue_perk6"),
			};
		case HUNTRESS:
			return new String[]{
					Messages.get(HeroClass.class, "huntress_perk1"),
					Messages.get(HeroClass.class, "huntress_perk2"),
					Messages.get(HeroClass.class, "huntress_perk3"),
					Messages.get(HeroClass.class, "huntress_perk4"),
					Messages.get(HeroClass.class, "huntress_perk5"),
			};
		}
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}
}
