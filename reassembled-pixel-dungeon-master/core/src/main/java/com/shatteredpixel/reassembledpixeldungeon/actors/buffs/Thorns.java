package com.shatteredpixel.reassembledpixeldungeon.actors.buffs;

import com.shatteredpixel.reassembledpixeldungeon.actors.Char;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.ui.BuffIndicator;

/**
 * Created by bzonick5979 on 5/31/2017.
 */

public class Thorns extends FlavourBuff {

    {
        type = buffType.POSITIVE;
    }

    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.BARKSKIN;
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

}
