package com.oscode.gatherthis;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class FragmentManager extends FragmentActivity {

	ViewFlipper vf;
	private ViewPager viewpager;
	private String[] drawerItems = { "Life Counter", "Card Search",

	"Rulebook" };
	private DrawerLayout mDrawerLayout;
	private LinearLayout mDrawerList;
	private final int CARD_SEARCH_FRAGMENT = 1;
	private final int LIFE_COUNTER_FRAGMENT = 0;
	private final int RULEBOOK_FRAGMENT = 2;
	private final int NUM_FRAGMENTS = 3;
	private Fragment[] fragments = new Fragment[NUM_FRAGMENTS];
	private ActionBarDrawerToggle mDrawerToggle;
	
	static float scale;
	private DrawerItem lifeCounterItem;
	private DrawerItem cardSearchItem;
	private DrawerItem rulebookItem;
	
	private static int currentFragment = 1;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		scale = getResources().getDisplayMetrics().density;
		setContentView(R.layout.drawer_layout);
		
		class Divider extends View {

			public Divider(Context context, float size) {
				super(context);
				LayoutParams dlps = new LayoutParams(LayoutParams.WRAP_CONTENT, (int) (size * scale));
				setBackgroundColor(getResources().getColor(R.color.light_gray));
				setLayoutParams(dlps);
			}
			
		}
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (LinearLayout) findViewById(R.id.left_drawer);
		lifeCounterItem = new DrawerItem(getApplicationContext(), "Life Counter", R.drawable.lcounter, R.drawable.lcounter_light);
		cardSearchItem = new DrawerItem(getApplicationContext(), "Card Search", R.drawable.search_dark, R.drawable.search_light);
		rulebookItem = new DrawerItem(getApplicationContext(), "Rulebook", R.drawable.book, R.drawable.book_light);
		TextView header = new TextView(getApplicationContext());
		header.setText("MTG TOOLS");
		header.setTypeface(null, Typeface.BOLD);
		header.setTextColor(getResources().getColor(R.color.black));
		header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		header.setPadding((int) (scale * 20), (int) (scale * 20), 0, 0);
		mDrawerList.addView(header);
		mDrawerList.addView(new Divider(getApplicationContext(), 2));
		mDrawerList.addView(lifeCounterItem);
		mDrawerList.addView(new Divider(getApplicationContext(), 0.5f));
		mDrawerList.addView(cardSearchItem);
		mDrawerList.addView(new Divider(getApplicationContext(), 0.5f));
		mDrawerList.addView(rulebookItem);
		
		lifeCounterItem.setColorMode(DrawerItem.colorMode.BLUE);
		
		lifeCounterItem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cardSearchItem.setColorMode(DrawerItem.colorMode.WHITE);
				rulebookItem.setColorMode(DrawerItem.colorMode.WHITE);
				lifeCounterItem.setColorMode(DrawerItem.colorMode.BLUE);
				showFragment(LIFE_COUNTER_FRAGMENT);
				currentFragment = 1;
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		});
		
		cardSearchItem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cardSearchItem.setColorMode(DrawerItem.colorMode.BLUE);
				rulebookItem.setColorMode(DrawerItem.colorMode.WHITE);
				lifeCounterItem.setColorMode(DrawerItem.colorMode.WHITE);
				showFragment(CARD_SEARCH_FRAGMENT);
				currentFragment = 2;
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		});
		rulebookItem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cardSearchItem.setColorMode(DrawerItem.colorMode.WHITE);
				rulebookItem.setColorMode(DrawerItem.colorMode.BLUE);
				lifeCounterItem.setColorMode(DrawerItem.colorMode.WHITE);
				showFragment(RULEBOOK_FRAGMENT);
				currentFragment = 3;
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		});
//		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, drawerItems) {
//
//					@Override
//					public View getView(int position, View convertView,
//							ViewGroup parent) {
//						// TODO write this method
//						return super.getView(position, convertView, parent);
//					}
//			
//		});
		fragments[CARD_SEARCH_FRAGMENT] = new Search();
		fragments[LIFE_COUNTER_FRAGMENT] = new MainActivity();
		fragments[RULEBOOK_FRAGMENT] = new Rules();
		showFragment(LIFE_COUNTER_FRAGMENT);

//		mDrawerList.setOnItemClickListener(new DrawerItemClickListener() {
//
//		});
		mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        getActionBar().setDisplayHomeAsUpEnabled(true);
//        mDrawerList.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				view.setBackgroundColor(getResources().getColor(R.color.gather_blue));
//				
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				
//				
//			}
//        	
//        });
		// viewpager = (ViewPager) findViewById(R.id.pager);
		// viewpager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// vf = (ViewFlipper) findViewById(R.id.ruleflipper);
		//
		// viewpager.setOnPageChangeListener(new OnPageChangeListener() {
		//
		// @Override
		// public void onPageScrollStateChanged(int arg0) {
		// 
		//
		// }
		//
		// @Override
		// public void onPageScrolled(int arg0, float arg1, int arg2) {
		// 
		//
		// }
		//
		// @Override
		// public void onPageSelected(int arg0) {
		// switch (arg0) {
		// case 0:
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// break;
		// default:
		// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// }
		//
		// }});
	}

	private class PagerAdapter extends FragmentPagerAdapter {

		public PagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				return new MainActivity();
			case 1:
				return new Search();
			case 2:
				return new Rules();
			}
			return null;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Life Counter";
			case 1:
				return "Card Search";
			case 2:
				return "Rulebook";
			}
			return super.getPageTitle(position);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
	          return true;
	        }
		
		switch (item.getItemId()) {

		case R.id.reset:
			MainActivity.reset();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		switch (currentFragment) {
		case 2:
			Search.backPressed();
			break;
		case 3:
			Rules.backPressed();
			break;
		default:
			super.onBackPressed();
		}

	}

	public void showFragment(int fragmentIndex) {
		android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = fm
				.beginTransaction();
		transaction.replace(R.id.content_frame, fragments[fragmentIndex]);
		transaction.commit();
	}

//	private class DrawerItemClickListener implements
//			ListView.OnItemClickListener {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			mDrawerList.setItemChecked(position, true);
//			mDrawerList.setSelection(position);
//			showFragment(position);
//			mDrawerLayout.closeDrawer(mDrawerList);
//			view.setBackgroundColor(getResources().getColor(R.color.gather_blue));
//		}
//	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private static class DrawerItem extends LinearLayout {

    	private TextView itemName;
    	private ImageView itemIcon;
    	private int iconId_dark;
    	private int iconId_light;
    	enum colorMode {
    		BLUE, WHITE
    	};
    	
    	private colorMode cm = colorMode.WHITE;
    	
		public DrawerItem(Context context, String name, int iconId_dark, int iconId_light) {
			super(context);
			this.iconId_dark = iconId_dark;
			this.iconId_light = iconId_light;
			
			itemName = new TextView(context);
			itemName.setText(name);
			itemName.setTextColor(getResources().getColor(R.color.black));
			itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			itemIcon = new ImageView(context);
			itemIcon.setImageResource(iconId_dark);
			itemIcon.setScaleType(ScaleType.CENTER_INSIDE);
			
			setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (48 * scale));
			int padAmount = (int) (8 * scale);
			setLayoutParams(lps);
			setPadding(padAmount, padAmount, padAmount, padAmount);
			int iconSize = (int) (32 * scale);
			LayoutParams iconLayoutParams = new LayoutParams(iconSize, iconSize);
			iconLayoutParams.setMargins(0, 0, (int) (12 * scale), 0);
			itemIcon.setLayoutParams(iconLayoutParams);
			
			LayoutParams nameLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			nameLayoutParams.gravity = Gravity.CENTER_VERTICAL;
			itemName.setLayoutParams(nameLayoutParams);
			addView(itemIcon);
			addView(itemName);
			
		}
    	
		void changeIcon(int icon) {
			itemIcon.setImageResource(icon);
		}
		
		colorMode getColorMode() {
			return cm;
		}
		
		void setColorMode(colorMode c) {
			cm = c;
			switch (cm) {
			case BLUE:
				setBackgroundColor(getResources().getColor(R.color.gather_blue));
				itemName.setTextColor(getResources().getColor(R.color.white));
				itemIcon.setImageResource(iconId_light);
				break;
			case WHITE:
				setBackgroundColor(getResources().getColor(R.color.white));
				itemName.setTextColor(getResources().getColor(R.color.black));
				itemIcon.setImageResource(iconId_dark);
				break;
			}
		}
    }

}
