/*
 * Copyright © 2011-2012 Ejwa Software. All rights reserved.
 *
 * This file is part of Dinja Engine. Dinja Engine is a OpenGLES2
 * 3D engine with physics support developed for the Android platform.
 *
 * Dinja Engine is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Dinja Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with Dinja Engine. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.ejwa.dinja.demo.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.ejwa.dinja.demo.R;

public class DemoActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String[] demos = getResources().getStringArray(R.array.demos);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, demos));

		final ListView listView = getListView();
		listView.setOnItemClickListener(new ListItemController());
	}

	private class ListItemController implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final Class demoToShow;

			switch(position) {
				case 1: demoToShow = DragWorldActivity.class; break;
				case 2: demoToShow = DancingLettersActivity.class; break;
				case 3: demoToShow = RubiksCubeActivity.class; break;
				default: demoToShow = TiltBombActivity.class; break;
			}

			startActivity(new Intent(getApplicationContext(), demoToShow));
		}
	}
}
