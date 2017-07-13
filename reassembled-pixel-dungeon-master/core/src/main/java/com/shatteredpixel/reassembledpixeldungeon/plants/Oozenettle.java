package com.shatteredpixel.reassembledpixeldungeon.plants;

import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Actor;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.reassembledpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.reassembledpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.reassembledpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.reassembledpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.reassembledpixeldungeon.items.Bomb;
import com.shatteredpixel.reassembledpixeldungeon.items.Heap;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfOoze;
import com.shatteredpixel.reassembledpixeldungeon.levels.Level;
import com.shatteredpixel.reassembledpixeldungeon.scenes.GameScene;
import com.shatteredpixel.reassembledpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.reassembledpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;

/**
 * Created by Pangur on 5/23/2017.
 */

public class Oozenettle extends Plant {

    {
        image = 14;
    }

    @Override
    public void activate() {
        Char ch = Actor.findChar(pos);

        if (ch != null) {
            Buff.affect( ch, Ooze.class );
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.get( pos ).burst(GooSprite.GooParticle.FACTORY, 5 );
        }

        if (Level.flamable[pos]) {
            Dungeon.level.destroy( pos );
            GameScene.updateMap( pos );
            Dungeon.observe();
        }

        for (int offset : PathFinder.NEIGHBOURS9){
            if (Level.meltable[pos+offset]) {
                Dungeon.level.destroy( pos+offset );
                GameScene.updateMap( pos+offset );
                Dungeon.observe();
                CellEmitter.get(pos+offset).burst(GooSprite.GooParticle.FACTORY, 2);

            }
        }

        Heap heap = Dungeon.level.heaps.get( pos );
        if(heap != null)
            heap.explode();
    }

    public static class Seed extends Plant.Seed {
        {
            image = ItemSpriteSheet.SEED_OOZENETTLE;

            plantClass = Oozenettle.class;
            alchemyClass = PotionOfOoze.class;
        }
    }
}
