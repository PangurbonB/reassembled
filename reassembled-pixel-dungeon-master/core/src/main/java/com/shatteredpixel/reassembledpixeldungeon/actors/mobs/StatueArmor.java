package com.shatteredpixel.reassembledpixeldungeon.actors.mobs;

import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.Journal;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.reassembledpixeldungeon.items.Generator;
import com.shatteredpixel.reassembledpixeldungeon.items.armor.Armor;
import com.shatteredpixel.reassembledpixeldungeon.items.armor.glyphs.Stone;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.reassembledpixeldungeon.levels.Terrain;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.sprites.StatueArmorSprite;
import com.shatteredpixel.reassembledpixeldungeon.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

/**
 * Created by dhoang9775 on 6/8/2017.
 */
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

public class StatueArmor extends Mob {
    {
        spriteClass = StatueArmorSprite.class;

        EXP = 0;
        state = PASSIVE;
    }

    public StatueArmor() {
        super();

        do {
            armor = (Armor) Generator.random( Generator.Category.ARMOR );
        } while (!(armor instanceof Armor)||armor.cursed);

        armor.identify();
        while(!armor.hasGlyph(Stone.class)){
            armor.inscribe( Armor.Glyph.random() );
        }


        HP = HT = 15 + Dungeon.depth * 5;
        defenseSkill = 4 + Dungeon.depth;
    }

    private static final String ARMOR	= "armor";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( ARMOR, armor );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        armor = (Armor) bundle.get( ARMOR );
    }

    @Override
    protected boolean act() {
        if (Dungeon.visible[pos]) {
            Journal.add( Journal.Feature.STATUE );
        }
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( Dungeon.depth, 4 + Dungeon.depth * 2 );
    }

    @Override
    public int attackSkill( Char target ) {
        return (20 + Dungeon.depth * 2);
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange( armor.DRMin(), armor.DRMax());
    }

    @Override
    public void damage( int dmg, Object src ) {

        if (state == PASSIVE) {
            state = HUNTING;
        }

        super.damage( dmg, src );
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {
        return armor.proc( enemy, this, damage );
    }

    @Override
    public void beckon( int cell ) {
        // Do nothing
    }

    @Override
    public void die( Object cause ) {
        Dungeon.level.drop( armor, pos ).sprite.drop();
        super.die( cause );
    }

    @Override
    public void destroy() {
        Journal.remove( Journal.Feature.STATUE );
        super.destroy();
    }

    @Override
    public boolean reset() {
        state = PASSIVE;
        return true;
    }

    @Override
    public String description() {
        return Messages.get(this, "desc", armor.name());
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    static {
        RESISTANCES.add( ToxicGas.class );
        RESISTANCES.add( Poison.class );
        RESISTANCES.add( Grim.class );
        IMMUNITIES.add( Vampiric.class );
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
