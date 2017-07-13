package com.shatteredpixel.reassembledpixeldungeon.items.scrolls;

import com.shatteredpixel.reassembledpixeldungeon.Assets;
import com.shatteredpixel.reassembledpixeldungeon.Badges;
import com.shatteredpixel.reassembledpixeldungeon.Dungeon;
import com.shatteredpixel.reassembledpixeldungeon.effects.Enchanting;
import com.shatteredpixel.reassembledpixeldungeon.effects.Speck;
import com.shatteredpixel.reassembledpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.reassembledpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.reassembledpixeldungeon.items.Item;
import com.shatteredpixel.reassembledpixeldungeon.items.armor.Armor;
import com.shatteredpixel.reassembledpixeldungeon.items.armor.Armor.Glyph;
import com.shatteredpixel.reassembledpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.Weapon.Enchantment;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.reassembledpixeldungeon.items.weapon.melee.NewShortsword;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.scenes.GameScene;
import com.shatteredpixel.reassembledpixeldungeon.utils.GLog;
import com.shatteredpixel.reassembledpixeldungeon.windows.WndBag;
import com.shatteredpixel.reassembledpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Pangur on 7/11/2017.
 */

public class ScrollOfMagicalBlessing extends InventoryScroll{

    {
        initials = 12;
        mode = WndBag.Mode.ENCHANTABLE;

        bones = true;

        unique = true;
    }

    public int enchantOptions = 10;
    public int glyphOptions = 3;

    @Override
    protected void onItemSelected(Item item) {
        Badges.validateItemLevelAquired(item);
        if(item instanceof Armor){
            armorInscribe(item);
        }
        else{
            meleeWeaponEnchant(item);
        }

    }

    //FIXME!!!
    private void meleeWeaponEnchant(Item item) {
        ((Weapon)item).upgrade();
        final Weapon weapon = (Weapon) item;
        ArrayList<Enchantment> enchants = new ArrayList<>();
        ArrayList<String> enchantsNames = new ArrayList<>();
        for (int x=0 ; x < enchantOptions; x++){
            //One of my ugliest solutions to date. There's definitely a better way, but I can't think of it right now.
            Enchantment ench = new NewShortsword().enchant().enchantment;

            if(enchantsNames.contains(Messages.get(ench, "name", "").substring(0, 1).toUpperCase() + Messages.get(ench, "name", "").substring(1))) {
                x--;
            }
            else{
                enchants.add(ench);
                enchantsNames.add(Messages.get(ench, "name", "").substring(0, 1).toUpperCase() + Messages.get(ench, "name", "").substring(1));
            }
        }
        final ArrayList<Enchantment> enchs = enchants;
        GameScene.show(
                new WndOptions( Messages.get(ScrollOfMagicalBlessing.class, "titlew"),
                        Messages.get(ScrollOfMagicalBlessing.class, "weapon"), enchantsNames
                        ){
                    @Override
                    protected void onSelect(int index) {
                        weapon.enchant(enchs.get(index));
                        Enchanting.show(curUser, weapon);
                        curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
                    };
                }
        );

    }

    //FIXME!!!
    private void armorInscribe(Item item) {
        ((Armor)item).upgrade();
        final Armor armor = (Armor) item;
        ArrayList<Glyph> glyphs = new ArrayList();
        ArrayList<String> glyphNames = new ArrayList<>();
        for (int x=0 ; x < glyphOptions; x++){
            Glyph gly = new ClothArmor().inscribe().glyph;

            if(glyphNames.contains(Messages.get(gly, "name", "").substring(4, 5).toUpperCase() + Messages.get(gly, "name", "").substring(5))){
                x--;

            }
            else {
                glyphs.add(gly);
                glyphNames.add(Messages.get(gly, "name", "").substring(4, 5).toUpperCase() + Messages.get(gly, "name", "").substring(5));
            }
        }
        final ArrayList<Glyph> glfs = glyphs;
        GameScene.show(
                new WndOptions( Messages.get(ScrollOfMagicalBlessing.class, "titlea"),
                        Messages.get(ScrollOfMagicalBlessing.class, "armor"), glyphNames
                ){
                    @Override
                    protected void onSelect(int index) {
                        armor.inscribe(glfs.get(index));
                        Enchanting.show(curUser,armor);
                        curUser.sprite.centerEmitter().start(PurpleParticle.BURST, 0.05f, 10);
                        Sample.INSTANCE.play(Assets.SND_BURNING);

                    };
                }
        );
    }

    @Override
    public int price() {
        return isKnown() ? 200 * quantity : super.price();
    }

}
