package com.shatteredpixel.reassembledpixeldungeon.levels.traps;

import com.shatteredpixel.reassembledpixeldungeon.Assets;
import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Actor;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.effects.Wound;
import com.shatteredpixel.reassembledpixeldungeon.levels.Level;
import com.shatteredpixel.reassembledpixeldungeon.levels.Terrain;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

/**
 * Created by bzonick5979 on 4/7/2017.
 */

public class GoldSpearTrap extends Trap {

    {
        color = YELLOW;
        shape = DOTS;
    }

    @Override
    public void trigger() {
        if (Dungeon.visible[pos]){
            Sample.INSTANCE.play(Assets.SND_TRAP);
        }
        //this trap is not disarmed by being triggered
        reveal();
        Level.set(pos, Terrain.TRAP);
        activate();
    }

    @Override
    public void activate() {
        if (Dungeon.visible[pos]){
            Sample.INSTANCE.play(Assets.SND_HIT);
            Wound.hit(pos);
        }

        Char ch = Actor.findChar( pos);
        if (ch != null && !ch.flying){
            int damage = Random.NormalIntRange(Dungeon.depth, Dungeon.depth*2);
            damage -= ch.drRoll();
            ch.damage( Math.max(damage, 0) , this);
            if (!ch.isAlive() && ch == Dungeon.hero){
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "ondeath") );
            }
        }
    }
}
