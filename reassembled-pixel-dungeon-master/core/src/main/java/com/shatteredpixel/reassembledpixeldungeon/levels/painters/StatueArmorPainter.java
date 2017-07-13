package com.shatteredpixel.reassembledpixeldungeon.levels.painters;

import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.actors.mobs.StatueArmor;
import com.shatteredpixel.reassembledpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.reassembledpixeldungeon.levels.Level;
import com.shatteredpixel.reassembledpixeldungeon.levels.Room;
import com.shatteredpixel.reassembledpixeldungeon.levels.Terrain;
import com.watabou.utils.Point;

/**
 * Created by dhoang9775 on 6/8/2017.
 */

public class StatueArmorPainter extends Painter {
    public static void paint(Level level, Room room ) {

        fill( level, room, Terrain.WALL );
        fill( level, room, 1, Terrain.EMPTY );

        Point c = room.center();
        int cx = c.x;
        int cy = c.y;

        Room.Door door = room.entrance();

        door.set( Room.Door.Type.LOCKED );
        level.addItemToSpawn( new IronKey( Dungeon.depth ) );

        if (door.x == room.left) {

            fill( level, room.right - 1, room.top + 1, 1, room.height() - 1 , Terrain.STATUE );
            cx = room.right - 2;

        } else if (door.x == room.right) {

            fill( level, room.left + 1, room.top + 1, 1, room.height() - 1 , Terrain.STATUE );
            cx = room.left + 2;

        } else if (door.y == room.top) {

            fill( level, room.left + 1, room.bottom - 1, room.width() - 1, 1 , Terrain.STATUE );
            cy = room.bottom - 2;

        } else if (door.y == room.bottom) {

            fill( level, room.left + 1, room.top + 1, room.width() - 1, 1 , Terrain.STATUE );
            cy = room.top + 2;

        }

        StatueArmor statue = new StatueArmor();
        statue.pos = cx + cy * level.width();
        level.mobs.add( statue );
    }
}
