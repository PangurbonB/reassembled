package com.shatteredpixel.reassembledpixeldungeon.actors.mobs;

import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.reassembledpixeldungeon.sprites.KillerBeeSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

/**
 * Created by bzonick5979 on 4/20/2017.
 */

public class KillerBee extends Mob {
    private int level;
    {
        spriteClass = KillerBeeSprite.class;

        HP = HT = 10 + Dungeon.depth * 5;
        flying = true;
        defenseSkill = 3;
    }

    @Override
    public int attackSkill( Char target ) {
        return 8;
    }

    @Override
    public int damageRoll() {return Random.NormalIntRange( Dungeon.depth, 4 + Dungeon.depth * 2 );}
    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 1);
    }
}
