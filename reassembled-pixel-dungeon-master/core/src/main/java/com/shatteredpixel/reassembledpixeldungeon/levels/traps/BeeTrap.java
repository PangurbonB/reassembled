package com.shatteredpixel.reassembledpixeldungeon.levels.traps;

import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Actor;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.KillerBee;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.reassembledpixeldungeon.levels.Level;
import com.shatteredpixel.reassembledpixeldungeon.scenes.GameScene;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by bzonick5979 on 4/14/2017.
 */


//Add honey slow debuff. Icon is honeycombs

public class BeeTrap extends Trap{


    private static final float DELAY = 2f;
    {
        shape = WAVES;
        color = GREY;
    }
    @Override
    public void activate() {

        if (Dungeon.bossLevel()) {
            return;
        }

        int nMobs = 1;
        if (Random.Int( 2 ) == 0) {
            nMobs++;
            if (Random.Int( 2 ) == 0) {
                nMobs++;
            }
        }

        ArrayList<Integer> candidates = new ArrayList<>();

        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
            int p = pos + PathFinder.NEIGHBOURS8[i];
            if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
                candidates.add( p );
            }
        }

        ArrayList<Integer> respawnPoints = new ArrayList<>();

        while (nMobs > 0 && candidates.size() > 0) {
            int index = Random.index( candidates );

            respawnPoints.add( candidates.remove( index ) );
            nMobs--;
        }

        ArrayList<Mob> mobs = new ArrayList<>();

        for (Integer point : respawnPoints) {
            KillerBee killerBee = new KillerBee();
            killerBee.state = killerBee.WANDERING;
            killerBee.pos = point;
            GameScene.add( killerBee, DELAY );
            mobs.add( killerBee );

        }

        //important to process the visuals and pressing of cells last, so spawned mobs have a chance to occupy cells first
        for (Mob mob : mobs){
            ScrollOfTeleportation.appear(mob, mob.pos);
        }
    }
}
