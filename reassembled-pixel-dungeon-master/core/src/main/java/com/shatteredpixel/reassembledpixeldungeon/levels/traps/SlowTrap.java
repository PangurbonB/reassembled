package com.shatteredpixel.reassembledpixeldungeon.levels.traps;

import com.shatteredpixel.reassembledpixeldungeon.Assets;
import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Actor;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.reassembledpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.reassembledpixeldungeon.effects.particles.ShadowParticle;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

/**
 * Created by bzonick5979 on 4/10/2017.
 */

public class SlowTrap extends Trap{

    {
        shape = STARS;
        color = GREY;
    }

    public void activate(){
        if (Dungeon.visible[pos]){
            Sample.INSTANCE.play(Assets.SND_HIT);
            CellEmitter.get(pos).burst(ShadowParticle.UP, 10);
        }
        Char ch = Actor.findChar( pos);
        if (ch != null && !ch.flying){
            Buff.affect(ch, Slow.class);
            Slow.prolong(ch, Slow.class, 5f + Random.Int(Dungeon.depth));
        }
    }
}
