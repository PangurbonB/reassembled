package com.shatteredpixel.reassembledpixeldungeon.items.wands;

import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.Actor;
import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.effects.Beam;
import com.shatteredpixel.reassembledpixeldungeon.items.Heap;
import com.shatteredpixel.reassembledpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.reassembledpixeldungeon.levels.Terrain;
import com.shatteredpixel.reassembledpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.scenes.GameScene;
import com.shatteredpixel.reassembledpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.reassembledpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.reassembledpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;


import java.lang.annotation.Target;

import static com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfTeleportation.appear;
import static com.shatteredpixel.reassembledpixeldungeon.items.scrolls.ScrollOfTeleportation.teleportHero;

/**
 * Created by bzonick5979 on 4/28/2017.
 */

public class WandOfTranslocation extends Wand{
    {
        image = ItemSpriteSheet.WAND_TRANSLOCATION;
    }

    @Override
    protected void onZap(Ballistica beam) {
        boolean aff = false;
        for (int c : beam.subPath(1, beam.dist)) {
            if(!aff) {
                Char ch = Actor.findChar(c);
                if (ch != null) {
                    int chPos = ch.pos;
                    processSoulMark(ch, chargesPerCast());
                    affectTarget(ch, chPos);
                    Dungeon.observe();
                }
                Heap heap = Dungeon.level.heaps.get(c);
                if (heap != null) {
                    int heapPos = heap.pos;
                    affectItem(heap, heapPos);
                    aff = true;
                    Dungeon.observe();
                }
            }
        }
    }

    protected void affectItem(Heap heap, int heapPos){
        Heap.Type ty = heap.type;
        if(ty == Heap.Type.HEAP || ty == Heap.Type.CHEST) {
            for (int x = 0; x < heap.items.size(); x++) {
                Dungeon.level.drop(heap.items.get(x), curUser.pos);
            }
            appear(curUser, heap.pos);
            heap.destroy();
            Dungeon.level.press(heapPos, null);
            Dungeon.level.press(heap.pos, curUser);
        }


    }

    protected void affectTarget(Char ch, int chPos){
        int savPos = chPos;
        appear(ch, curUser.pos);
        appear(curUser,savPos);
        if(Dungeon.level.map[chPos] == Terrain.DOOR){

        }
        Dungeon.level.press(chPos, curUser);
        Dungeon.level.press(savPos, curUser);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {

        int count = 10;
        int pos;
        do {
            pos = Dungeon.level.randomRespawnCell();
            if (count-- <= 0) {
                break;
            }
        } while (pos == -1);

        if (pos == -1 || Dungeon.bossLevel()) {

            GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );

        }
        else if (defender.properties().contains(Char.Property.IMMOVABLE)) {

            GLog.w( Messages.get(LloydsBeacon.class, "tele_fail") );
        }

    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( Random.Int( 0x1000000 ) );
        particle.am = 0.5f;
        particle.setLifespan(1f);
        particle.speed.polar(Random.Float(PointF.PI2), 2f);
        particle.setSize( 1f, 2f);
        particle.radiateXY( 0.5f);
    }

    @Override
    protected void fx( Ballistica beam, Callback callback ) {
        curUser.sprite.parent.add(
                new Beam.LightRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
        callback.call();
    }
}
