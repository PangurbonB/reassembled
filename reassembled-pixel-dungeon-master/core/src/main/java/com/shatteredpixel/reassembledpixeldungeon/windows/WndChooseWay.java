/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.reassembledpixeldungeon.windows;

import com.shatteredpixel.reassembledpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.reassembledpixeldungeon.items.TomeOfMastery;
import com.shatteredpixel.reassembledpixeldungeon.messages.Messages;
import com.shatteredpixel.reassembledpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.reassembledpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.reassembledpixeldungeon.ui.RedButton;
import com.shatteredpixel.reassembledpixeldungeon.ui.RenderedTextMultiline;
import com.shatteredpixel.reassembledpixeldungeon.ui.Window;

public class WndChooseWay extends Window {
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 18;
	private static final float GAP		= 2;
	
	public WndChooseWay( final TomeOfMastery tome, final HeroSubClass way1, final HeroSubClass way2 ) {
		
		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( tome.image(), null ) );
		titlebar.label( tome.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		RenderedTextMultiline hl = PixelScene.renderMultiline( 6 );
		hl.text( way1.desc() + "\n\n" + way2.desc() + "\n\n" + Messages.get(this, "message"), WIDTH );
		hl.setPos( titlebar.left(), titlebar.bottom() + GAP );
		add( hl );
		
		RedButton btnWay1 = new RedButton( way1.title().toUpperCase() ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way1 );
			}
		};
		btnWay1.setRect( 0, hl.bottom() + GAP, (WIDTH - GAP) / 2, BTN_HEIGHT );
		add( btnWay1 );
		
		RedButton btnWay2 = new RedButton( way2.title().toUpperCase() ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way2 );
			}
		};
		btnWay2.setRect( btnWay1.right() + GAP, btnWay1.top(), btnWay1.width(), BTN_HEIGHT );
		add( btnWay2 );
		
		RedButton btnCancel = new RedButton( Messages.get(this, "cancel") ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, btnWay2.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}

	public WndChooseWay( final TomeOfMastery tome, final HeroSubClass way1, final HeroSubClass way2, final HeroSubClass way3 ) {

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( tome.image(), null ) );
		titlebar.label( tome.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		RenderedTextMultiline hl = PixelScene.renderMultiline( 6 );
		hl.text( way1.desc() + "\n\n" + way2.desc() + "\n\n" + way3.desc() + "\n\n" + Messages.get(this, "message"), WIDTH );
		hl.setPos( titlebar.left(), titlebar.bottom() + GAP );
		add( hl );

		RedButton btnWay1 = new RedButton( way1.title().toUpperCase() ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way1 );
			}
		};
		btnWay1.setRect( 0, hl.bottom() + GAP, (WIDTH - GAP*2) / 3, BTN_HEIGHT );
		add( btnWay1 );

		RedButton btnWay2 = new RedButton( way2.title().toUpperCase() ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way2 );
			}
		};
		btnWay2.setRect( btnWay1.right() + GAP, btnWay1.top(), btnWay1.width(), BTN_HEIGHT );
		add( btnWay2 );

		RedButton btnWay3 = new RedButton( way3.title().toUpperCase() ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way3 );
			}
		};
		btnWay3.setRect( btnWay2.right() + GAP, btnWay2.top(), btnWay2.width(), BTN_HEIGHT );
		add( btnWay3 );

		RedButton btnCancel = new RedButton( Messages.get(this, "cancel") ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, btnWay2.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnCancel );

		resize( WIDTH, (int)btnCancel.bottom() );
	}
}
