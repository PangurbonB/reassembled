package com.shatteredpixel.reassembledpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.reassembledpixeldungeon.sprites.ItemSprite;

/**
 * Created by Pangur on 6/14/2017.
 */

public class Ethereal extends Weapon.Enchantment{

    private static ItemSprite.Glowing LAVENDER = new ItemSprite.Glowing( 0x7FFFD4 );

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        return 1548000;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return LAVENDER;
    }
}
