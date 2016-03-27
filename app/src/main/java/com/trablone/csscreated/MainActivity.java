package com.trablone.csscreated;

import android.app.Activity;
import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import android.os.*;
import android.widget.*;
import android.view.View.*;

import android.content.res.*;
import android.view.*;
import android.content.*;
import java.util.*;

import android.preference.PreferenceManager;
import android.util.*;
import android.support.v4.app.*;
import android.database.*;
import com.trablone.csscreated.database.*;
import android.provider.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.*;
import java.io.*;

public class MainActivity extends FragmentActivity
{
	public static ArrayList<Item> menus;
	ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private FrameLayout frameDrawer;
	public static CharSequence mTitle ;
	private MenuAdapter mAdapter;
	private Uri mUri;
	public static boolean menu = true;
	private String patch = null;
	private Handler handler;
	private String[] styles ={"white.css", "black.css"};

	public static void setMenu(boolean menu)
	{
		MainActivity.menu = menu;
	}

	public static boolean isMenu()
	{
		return menu;
	}

	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setTheme(getAppTheme());
		setContentView(R.layout.content_frame_drawer);
		if (savedInstanceState == null)
			setContent(new NewsPagerFragment());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		menus = new ArrayList<Item>();
		mAdapter = new MenuAdapter(this);


		mTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R .id. drawer_layout);
		mDrawerList = (ListView) findViewById(R .id. left_drawer);
		frameDrawer = (FrameLayout)findViewById(R.id.frameDraver);


		mDrawerList.setAdapter(mAdapter);

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		handler = new Handler();
		handler.post(new Runnable(){

				@Override
				public void run()
				{
					firstLoad();
				}
			});

		mDrawerList.setItemChecked(0, true);
		mDrawerList.setItemsCanFocus(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout ,
												  R .drawable.ic_drawer , R.string.menu , R.string.app_name) {
			public void onDrawerClosed(View view)
			{
				getActionBar(). setTitle(mTitle);
				//	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
				mAdapter.notifyDataSetChanged();

			}

			public void onDrawerOpened(View drawerView)
			{
				getActionBar().setTitle(R.string.menu);
				//	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setLongClickable(true);
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		Intent i = getIntent();
		String action = i.getAction();
		String type = i.getType();

		if (Intent.ACTION_VIEW.equals(action))
		{
			mUri = i.getData();
			patch = mUri.getPath();

			if (patch != null)
			{
			/*	handler = new Handler();
				handler.post(new Runnable(){

						@Override
						public void run()
						{*/
							openNamedFile(patch);
			/*			}
					});*/
			}
		}

    }

	private int getAppTheme()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		return prefs.getInt("app_theme", 0) == 0 ? R.style.Theme_White : R.style.Theme_Black;
	}

	private void setAppTheme()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor e = prefs.edit();
		e.putInt("app_theme", prefs.getInt("app_theme", 0) == 0 ? 1 : 0);
		e.commit();
		Toast.makeText(this, "Ок, а сей час эта хрень закроется!", Toast.LENGTH_LONG).show();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable(){

				@Override
				public void run()
				{
					finish();
				}
			}, 2000);
	}

	private void firstLoad()
	{

		Cursor cursor = getContentResolver().query(Contract.Styles.CONTENT_URI, null, null, null, Contract.Styles.DEFAULT_SORT_ORDER);
		if (!cursor.moveToFirst())
		{
			getDefaultStyle();
		}
		cursor.close();
		getMenu();
	}

	private void getDefaultStyle()
	{
		for (int i = 0; i < styles.length; i++)
		{
			ContentValues cv = new ContentValues();
			cv.put(Contract.Styles.data, FileUtils.LoadData(this, styles[i]));
			cv.put(Contract.Styles.title, styles[i]);
			getContentResolver().insert(Contract.Styles.CONTENT_URI, cv);
		}
		getMenu();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle . syncState();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener
	{

		public void onItemClick(AdapterView <?> parent , View view , int position, long id)
		{
			Item item = menus.get(position);


			if (mTitle.equals(item.title))
			{
				mDrawerLayout.closeDrawer(frameDrawer);
				mDrawerList.setItemChecked(position, true);
			}
			else
			{
				EditorFragment.saveOldStyle(MainActivity.this);
				selectItem(position, item);
				getHandleMenu();
			}
		}

	}

	private void getHandleMenu()
	{
		handler = new Handler();
		handler.post(new Runnable(){

				@Override
				public void run()
				{
					getMenu();
				}
			});
	}

	private void setDefaultItem(int position)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor e = prefs.edit();
		e.putInt("_position", position).commit();
	}


	private void setContent(Fragment fragment)
	{
		getSupportFragmentManager().beginTransaction()
			.add(R.id.content_frame , fragment)
			.commit();

	}

	private void selectItem(final int position, final Item item)
	{
		mDrawerLayout.closeDrawer(frameDrawer);
		setTitle(item.title);
		mDrawerList.setItemChecked(position, true);
		handler = new Handler();
		handler.postDelayed(new Runnable(){

				@Override
				public void run()
				{
					setDefaultItem(position);
					EditorFragment.setEditText(item.data, item.title, item.id);
				}
			}, 1000);


	}

	@Override
	public void setTitle(CharSequence title)
	{
		mTitle = title ;
		getActionBar().setTitle(mTitle);
	}



	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);
		if (isMenu())
		{
			menu.setGroupVisible(R.id.editor_group, true);
			menu.setGroupVisible(R.id.webview_group, false);
		}
		else
		{
			menu.setGroupVisible(R.id.editor_group, false);
			menu.setGroupVisible(R.id.webview_group, true);

		}
		return true;
	}




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
       	if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		switch (item.getItemId())
		{
			case R.id.item:
				showNavigateDialog();
				break;
			case R.id.delete:
				showDeleteDialog();
				break;
			case R.id.theme:
				setAppTheme();
				break;
			case R.id.default_style:
				getDefaultStyle();
				break;
			case R.id.rename:
				showRenameDialog();
				break;
		}

        return super.onOptionsItemSelected(item);
    }

	/**
	 *Адаптер NavigationDrawer
	 */

	public ArrayList<Item> getMenu()
	{
		menus.clear();
		Cursor cursor = getContentResolver().query(Contract.Styles.CONTENT_URI, null, null, null, Contract.Styles.DEFAULT_SORT_ORDER);
		if (cursor.moveToFirst())
		{
			do{
				Item item = new Item();
				item.title = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Styles.title));
				item.data = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Styles.data));
				item.id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
				menus.add(item);
			}while(cursor.moveToNext());
		}
		else
			firstLoad();

		mAdapter.notifyDataSetChanged();
		cursor.close();
		return menus;
	}

	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		super.onBackPressed();
	}


	class MenuAdapter extends BaseAdapter
    {

		@Override
		public int getCount()
		{
			// TODO: Implement this method
			return menus.size();
		}

		@Override
		public Object getItem(int p1)
		{
			// TODO: Implement this method
			return menus.get(p1);
		}

		@Override
		public long getItemId(int p1)
		{
			// TODO: Implement this method
			return p1;
		}

        final LayoutInflater inflater;
		private Context context;


        public MenuAdapter(Context context)
		{

			this.context = context;
            inflater = LayoutInflater.from(context);		
		}


        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final ViewHolder holder;

            if (convertView == null)
			{
                convertView = inflater.inflate(R.layout.row, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.row_title);
				convertView.setTag(holder);

            }
			else
			{
                holder = (ViewHolder) convertView.getTag();
            }

			Item item = menus.get(position);
			holder.text.setText(item.title);
			return convertView;

        }
		public class ViewHolder
		{
			public TextView text;
		}
    }

	private void saveStyleToBase(String title, String data)
	{
		ContentValues cv = new ContentValues();
		cv.put(Contract.Styles.title, title);
		cv.put(Contract.Styles.data, data);
		Uri uri = getContentResolver().insert(Contract.Styles.CONTENT_URI, cv);
		getMenu();
		setDefaultItem(0);
		EditorFragment.setEditText(data, title, uri);

	}

	private void renameStyleInBase(final String title, final String data, final long id)
	{
		ContentValues cv = new ContentValues();
		cv.put(Contract.Styles.title, title);
		getContentResolver().update(ContentUris.withAppendedId(Contract.Styles.CONTENT_URI, id), cv, null, null);
		getMenu();
		setDefaultItem(0);
		setTitle(title);
		EditorFragment.setEditText(data, title, null);
	}
	
	private void deleteStyleInBase(long id){
		getContentResolver().delete(ContentUris.withAppendedId(Contract.Styles.CONTENT_URI, id), null, null);
		Item item = getMenu().get(0);
		setTitle(item.title);
		setDefaultItem(0);
		EditorFragment.setEditText(item.data, item.title, item.id);
		
	}

	void showRenameDialog()
	{
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
		{
            ft.remove(prev);
        }
        ft.addToBackStack(null);


        DialogFragment dialogFragment = new RenameDialogFragment();
		Bundle args = new Bundle();
		args.putString("_title", EditorFragment.oldtitle);
		args.putLong("_id", EditorFragment.oldid);
		args.putString("_data", EditorFragment.getEditText()); 
		dialogFragment.setArguments(args);

        dialogFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }




    public class RenameDialogFragment extends DialogFragment
	{


		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater layoutInflater = getActivity().getLayoutInflater();

			View view = layoutInflater.inflate(R.layout.dialog_rename, null);
			final TextView textView = (TextView) view.findViewById(R.id.name);
			textView.setText(getArguments().getString("_title"));
			builder.setMessage("Переименьвать")
				.setView(view)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						handler = new Handler();
						handler.postDelayed(new Runnable(){

								@Override
								public void run()
								{
									renameStyleInBase(textView.getText().toString(), getArguments().getString("_data"), getArguments().getLong("_id"));
								}
							}, 1000);

					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						RenameDialogFragment.this.getDialog().cancel();
					}
				});
			return builder.create();
		}
    }

    void showNavigateDialog()
	{
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
		{
            ft.remove(prev);
        }
        ft.addToBackStack(null);


        DialogFragment dialogFrag = new NewCssDialogFragment();
        dialogFrag.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }




    public class NewCssDialogFragment extends DialogFragment
	{


		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater layoutInflater = getActivity().getLayoutInflater();

			View view = layoutInflater.inflate(R.layout.dialog_rename, null);
			final TextView textView = (TextView) view.findViewById(R.id.name);

			builder.setMessage("Новый стиль")
				.setView(view)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						handler = new Handler();
						handler.postDelayed(new Runnable(){

								@Override
								public void run()
								{
									saveStyleToBase(textView.getText().toString(), "");
								}
							}, 1000);

					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						NewCssDialogFragment.this.getDialog().cancel();
					}
				});
			return builder.create();
		}
    }

	void showDeleteDialog()
	{
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
		{
            ft.remove(prev);
        }
        ft.addToBackStack(null);
		DialogFragment dialogFragment = new DeleteDialogFragment();
		Bundle args = new Bundle();
		args.putString("_title", EditorFragment.oldtitle);
		args.putLong("_id", EditorFragment.oldid);
		dialogFragment.setArguments(args);

        dialogFragment.show(getSupportFragmentManager().beginTransaction(), "dialog");
    }

	public class DeleteDialogFragment extends DialogFragment
	{


		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setMessage("Удалить?")
				.setTitle(getArguments().getString("_title"))
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						handler = new Handler();
						handler.postDelayed(new Runnable(){

								@Override
								public void run()
								{
									deleteStyleInBase(getArguments().getLong("_id"));
								}
							}, 1000);
						}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						DeleteDialogFragment.this.getDialog().cancel();
					}
				});
			return builder.create();
		}
    }

	protected void openNamedFile(String patch)
	{
		try
		{
			File f = new File(patch);
			FileInputStream fis = new FileInputStream(f);

			long size = f.length();


			DataInputStream dis = new DataInputStream(fis);
			byte[] b = new byte[(int) size];
			int length = dis.read(b, 0, (int) size);

			dis.close();
			fis.close();

			String olddata = new String(b, 0, length, "UTF-8");

			saveStyleToBase(f.getName(), olddata);

		}
		catch (FileNotFoundException e)
		{

		}
		catch (IOException e)
		{

		}
	}
}
