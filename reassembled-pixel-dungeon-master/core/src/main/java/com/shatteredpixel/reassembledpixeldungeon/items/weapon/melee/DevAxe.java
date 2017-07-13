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

import com.shatteredpixel.reassembledpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.reassembledpixeldungeon.sprites.ItemSpriteSheet;

public class DevAxe extends MeleeWeapon {
    //This axe is intended solely for use in development.
    //Makes my life a lot easier! --Pangur
    {
        image = ItemSpriteSheet.DEVAXE;

        tier = 5;
        DLY = 0.1f; //10x speed
        ACC = 2f; //200% boost to accuracy
    }

    @Override
    public int defenseFactor(Hero hero) {
        return 10000000;
    }

    @Override
    public int max(int lvl) {
        return 10000000;
    }

    @Override
    public int min(){
        return 10000000;
    }

    @Override
    public int STRReq(int lvl) { return 1; }

}
