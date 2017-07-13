package com.shatteredpixel.reassembledpixeldungeon.items.potions;

import com.shatteredpixel.reassembledpixeldungeon.Assets;
import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Actor;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.reassembledpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.reassembledpixeldungeon.items.Heap;
import com.shatteredpixel.reassembledpixeldungeon.levels.Level;
import com.shatteredpixel.reassembledpixeldungeon.scenes.GameScene;
import com.shatteredpixel.reassembledpixeldungeon.sprites.GooSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

/**
 * Created by Pangur on 5/23/2017.
 */

public class PotionOfOoze extends Potion {

    {
        initials = 12;
    }

    @Override
    public void shatter( int cell ) {

        if (Dungeon.visible[cell]) {
            setKnown();

            splash( cell );
            Sample.INSTANCE.play( Assets.SND_SHATTER );
        }

        for (int offset : PathFinder.NEIGHBOURS9){
            if (Level.flamable[cell+offset]
                    || Dungeon.level.heaps.get(cell+offset) != null
                    || Level.meltable[cell+offset]) {

                Dungeon.level.destroy( cell+offset );
                GameScene.updateMap( cell+offset );
                Dungeon.observe();

                Heap heap = Dungeon.level.heaps.get( cell+offset );
                if(heap != null)
                    heap.explode();



                CellEmitter.get(cell+offset).burst(GooSprite.GooParticle.FACTORY, 2);

            } else {

                CellEmitter.get(cell+offset).burst(GooSprite.GooParticle.FACTORY, 2);

            }
            if(Actor.findChar(cell+offset) != null){
                Char ch = Actor.findChar(cell+offset);
                Buff.affect(ch, Ooze.class);
            }
        }
    }

    @Override
    public int price() {
        return isKnown() ? 30 * quantity : super.price();
    }
}
