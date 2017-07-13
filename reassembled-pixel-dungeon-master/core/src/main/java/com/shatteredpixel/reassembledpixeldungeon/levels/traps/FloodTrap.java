package com.shatteredpixel.reassembledpixeldungeon.levels.traps;

import com.shatteredpixel.reassembledpixeldungeon.Assets;
import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Actor;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.reassembledpixeldungeon.actors.blobs.Regrowth;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.reassembledpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.reassembledpixeldungeon.effects.Flare;
import com.shatteredpixel.reassembledpixeldungeon.effects.Speck;
import com.shatteredpixel.reassembledpixeldungeon.levels.Level;
import com.shatteredpixel.reassembledpixeldungeon.levels.Terrain;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.scenes.GameScene;
import com.shatteredpixel.reassembledpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

/**
 * Created by bzonick5979 on 4/26/2017.
 */

public class FloodTrap extends Trap{
    {
        color = WHITE;
        shape = CROSSHAIR;
    }

    @Override
    public void activate() {
        for (int i = 0; i < Dungeon.level.length(); i++){
            int c = Dungeon.level.map[i];
            if (c == Terrain.EMPTY ||
                    c == Terrain.EMBERS ||
                    c == Terrain.EMPTY_DECO ||
                    c == Terrain.GRASS ||
                    c == Terrain.HIGH_GRASS ||
                    c == Terrain.DOOR) {
                Level.set(i, Terrain.WATER);
                GameScene.updateMap( i );
            }
        }

        if (Dungeon.bossLevel()) {
            return;
        }

        int nMobs = Dungeon.depth/5;

        for (Mob mob : Dungeon.level.mobs) {
            mob.beckon( pos );
        }

        CellEmitter.center(pos).start( Speck.factory(Speck.SCREAM), 0.3f, 3 );

        Sample.INSTANCE.play( Assets.SND_ALERT );

        for (int i = 0; i <= (Dungeon.depth - 5)/5; i++){
            Piranha piranha = new Piranha();
            piranha.state = piranha.WANDERING;
            while(piranha.pos == 0){
                int z = Dungeon.level.randomRespawnCell();
                if(Dungeon.level.map[z] == Terrain.WATER && Actor.findChar( z ) == null && (Level.passable[z] || Level.avoid[z])){
                    piranha.pos = z;
                }
            }
            GameScene.add(piranha);
            piranha.beckon(Dungeon.hero.pos );
        }



        GLog.n( Messages.get(this, "msg"));
        GLog.n( Messages.get(this, "msg2"));
    }


}
