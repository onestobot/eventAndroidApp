package com.events;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.events.calendar.CalendarView;
import com.events.custom.CustomActivity;
import com.events.custom.CustomFragment;
import com.events.model.Data;
import com.events.ui.About;
import com.events.ui.FeedList;
import com.events.ui.LeftNavAdapter;
import com.events.ui.More;
import com.events.ui.MyTickets;
import com.events.ui.Programs;
import com.events.utils.Const;
import com.events.utils.StaticData;

/**
 * The Class MainActivity is the base activity class of the application. This
 * activity is launched after the Splash and it holds all the Fragments used in
 * the app. It also creates the Navigation Drawer on left side.
 */
public class MainActivity extends CustomActivity
{

	/** The drawer layout. */
	private DrawerLayout drawerLayout;

	/** ListView for left side drawer. */
	private ListView drawerLeft;

	/** The drawer toggle. */
	private ActionBarDrawerToggle drawerToggle;

	/** The left navigation list adapter. */
	private LeftNavAdapter adapter;

	/** The tab. */
	private View tab;

	/* (non-Javadoc)
	 * @see com.newsfeeder.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tab = setClick(R.id.tab1);
		setClick(R.id.tab2);
		setClick(R.id.tab3);

		setupContainer();
		setupDrawer();
	}

	/**
	 * Setup the drawer layout. This method also includes the method calls for
	 * setting up the Left side drawer.
	 */
	private void setupDrawer()
	{
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view)
			{
				setActionBarTitle();
			}

			@Override
			public void onDrawerOpened(View drawerView)
			{
				getActionBar().setTitle(R.string.app_name);
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		drawerLayout.closeDrawers();

		setupLeftNavDrawer();
	}

	/**
	 * Setup the left navigation drawer/slider. You can add your logic to load
	 * the contents to be displayed on the left side drawer. You can also setup
	 * the Header and Footer contents of left drawer if you need them.
	 */
	private void setupLeftNavDrawer()
	{
		drawerLeft = (ListView) findViewById(R.id.left_drawer);

		adapter = new LeftNavAdapter(this, getDummyLeftNavItems());
		drawerLeft.setAdapter(adapter);
		drawerLeft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3)
			{
				drawerLayout.closeDrawers();
				launchFragment(pos);
			}
		});

	}

	/**
	 * This method returns a list of dummy items for left navigation slider. You
	 * can write or replace this method with the actual implementation for list
	 * items.
	 * 
	 * @return the dummy items
	 */
	private ArrayList<Data> getDummyLeftNavItems()
	{
		ArrayList<Data> al = new ArrayList<Data>();
		al.add(new Data("Program", R.drawable.ic_nav1, R.drawable.ic_nav1_sel));
		al.add(new Data("Feed", R.drawable.ic_nav2, R.drawable.ic_nav2_sel));
		al.add(new Data("My Program", R.drawable.ic_nav3,
				R.drawable.ic_nav3_sel));
		al.add(new Data("More", R.drawable.ic_nav4, R.drawable.ic_nav4_sel));
		al.add(new Data("About", R.drawable.ic_nav5, R.drawable.ic_nav5_sel));
		al.add(new Data("Rate this app", R.drawable.ic_nav6,
				R.drawable.ic_nav6_sel));
		return al;
	}

	/**
	 * This method can be used to attach Fragment on activity view for a
	 * particular tab position. You can customize this method as per your need.
	 * 
	 * @param pos
	 *            the position of tab selected.
	 */
	public void launchFragment(int pos)
	{
		CustomFragment f = null;
		String title = null;
		if (pos == 0)
		{
			title = getString(R.string.programs);
			f = new Programs();
			f.setArg(null);
		}
		else if (pos == 1)
		{
			title = getString(R.string.feed);
			f = new FeedList();
		}
		else if (pos == 2)
		{
			tab.setEnabled(true);
			tab = findViewById(R.id.tab1);
			tab.setEnabled(false);

			title = getString(R.string.my_prog);
			f = new CalendarView();
		}
		else if (pos == 21)
		{
			if (tab != null)
				tab.setEnabled(true);
			tab = findViewById(R.id.tab2);
			tab.setEnabled(false);
			title = getString(R.string.my_prog);
			f = new Programs();
			f.setArg(new Bundle());
			pos = 2;
		}
		else if (pos == 22)
		{
			title = getString(R.string.my_prog);
			f = new MyTickets();
			pos = 2;
		}
		else if (pos == 3)
		{
			title = getString(R.string.more);
			f = new More();
		}
		else if (pos == 4)
		{
			title = getString(R.string.about);
			f = new About();
		}
		else if (pos == 5)
		{
			Intent i = new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=" + getPackageName()));
			startActivity(Intent.createChooser(i, "Open with"));
		}

		findViewById(R.id.vTabs).setVisibility(
				pos == 2 ? View.VISIBLE : View.GONE);

		if (f != null)
		{
			while (getSupportFragmentManager().getBackStackEntryCount() > 0)
			{
				getSupportFragmentManager().popBackStackImmediate();
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, f).addToBackStack(title)
					.commit();
			if (adapter != null)
				adapter.setSelection(pos);
		}
	}

	/**
	 * Setup the container fragment for drawer layout. The current
	 * implementation of this method simply calls launchFragment method for tab
	 * position 0. You can customize this method as per your need to display
	 * specific content.
	 */
	private void setupContainer()
	{
		getSupportFragmentManager().addOnBackStackChangedListener(
				new OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged()
					{
						setActionBarTitle();
					}
				});
		launchFragment(0);
	}

	/**
	 * Set the action bar title text.
	 */
	private void setActionBarTitle()
	{
		if (drawerLayout.isDrawerOpen(drawerLeft))
		{
			getActionBar().setTitle(R.string.app_name);
			return;
		}
		if (getSupportFragmentManager().getBackStackEntryCount() == 0)
			return;
		String title = getSupportFragmentManager().getBackStackEntryAt(
				getSupportFragmentManager().getBackStackEntryCount() - 1)
				.getName();
		getActionBar().setTitle(title);
		getActionBar().setLogo(R.drawable.icon);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		drawerToggle.onConfigurationChanged(newConfig);
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}*/

	/* (non-Javadoc)
	 * @see com.newsfeeder.custom.CustomActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (drawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (getSupportFragmentManager().getBackStackEntryCount() > 1)
			{
				getSupportFragmentManager().popBackStackImmediate();
			}
			else
				finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/** The temp tab view. */
	private View temp;

	/* (non-Javadoc)
	 * @see com.events.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.tab1 || v.getId() == R.id.tab2
				|| v.getId() == R.id.tab3)
		{
			temp = tab;
			tab.setEnabled(true);
			tab = v;
			tab.setEnabled(false);
			if (v.getId() == R.id.tab1)
				launchFragment(2);
			else if (v.getId() == R.id.tab2)
				launchFragment(21);
			else
			{
				if (!StaticData.pref.contains(Const.USER_ID))
				{
					startActivityForResult(new Intent(THIS, Login.class),
							Const.REQ_LOGIN);
					tab.setEnabled(true);
					temp.setEnabled(false);
				}
				else
					launchFragment(22);
			}
		}

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		StaticData.clear();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Const.REQ_LOGIN && resultCode == Activity.RESULT_OK)
		{
			temp.setEnabled(true);
			tab.setEnabled(false);
			launchFragment(22);
		}
		else
			tab = temp;
	}
}
