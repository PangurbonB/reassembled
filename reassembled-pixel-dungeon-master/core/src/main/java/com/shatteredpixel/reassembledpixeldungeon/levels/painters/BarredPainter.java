package com.shatteredpixel.reassembledpixeldungeon.levels.painters;

import com.shatteredpixel.reassembledpixeldungeon.items.Generator;
import com.shatteredpixel.reassembledpixeldungeon.items.Item;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.reassembledpixeldungeon.items.potions.PotionOfOoze;
import com.shatteredpixel.reassembledpixeldungeon.levels.Level;
import com.shatteredpixel.reassembledpixeldungeon.levels.Room;
import com.shatteredpixel.reassembledpixeldungeon.levels.Terrain;
import com.shatteredpixel.reassembledpixeldungeon.plants.Oozenettle;
import com.watabou.utils.Random;

/**
 * Created by Pangur on 5/23/2017.
 */

public class BarredPainter extends Painter {

    public static void paint(Level level, Room room ) {
        final int floor = Terrain.EMPTY_SP;

        fill(level, room, Terrain.WALL);
        fill(level, room, 1, floor);

        Room.Door door = room.entrance();
        room.entrance().set( Room.Door.Type.BARS);

        if (Math.random() > .8){
            level.addItemToSpawn(new PotionOfOoze());
        }
        else{
            level.addItemToSpawn(new Oozenettle.Seed());
        }

        int x = -1;
        int y = -1;

        if(door.x == room.left || door.x == room.right){
            y = door.y;
            x = room.left+room.width()/2;
        }
        else if(door.y == room.top || door.y == room.bottom){
            x = door.x;
            y = room.top+room.height()/2;
        }
        int pos = x + y * level.width();
        set( level, pos, Terrain.PEDESTAL );
        level.drop( prizeMain(), pos );
    }

    private static Item prizeMain() {
        return Generator.random( Random.oneOf(
                Generator.Category.WAND,
                Generator.Category.RING,
                Generator.Category.ARTIFACT
        ) );
    }
}
