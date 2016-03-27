package com.trablone.csscreated;
import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.content.*;
import com.trablone.csscreated.database.*;
import android.preference.*;
import android.app.Dialog;
import android.app.AlertDialog;
import android.net.*;
import java.io.*;
import com.trablone.csscreated.widgets.*;


public class EditorFragment extends BaseFragment
{
	private static EditText editText;
	public static long oldid = 0;
	public static String olddata;
	public static String oldtitle;
	public static Uri mUri = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.editor_layout, container, false);
		editText = (EditText)v.findViewById(R.id.editText);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
		
		getDefaultItem();
		
	}

	@Override
	protected void onResumeFrag()
	{
		// TODO: Implement this method
		super.onResumeFrag();
		MainActivity.setMenu(true);
		}
	
	private void getDefaultItem(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		int p = prefs.getInt("_position", 0);
		Item item = MainActivity.menus.get(p);
		MainActivity.mTitle = item.title;
		getActivity().getActionBar().setTitle(item.title);
		setEditText(item.data,item.title, item.id);
	}
	
	public static String getEditText(){
		return editText.getText().toString();
	}
	
	public static void setEditText(String data,String title, long lid){
		mUri = null;
		oldid = lid;
		oldtitle = title;
		olddata = data;
		setText(data );
	}
	
	private static void setText(String data){
		editText.setText(data);
	}
	
	public static void setEditText(String data, String title, Uri uri){
		if(uri != null)
		mUri = uri;
		oldtitle = title;
		olddata = data;
		setText(data);
	}
	
	public static void setCustomCss(){
		FileUtils.saveNamedFile(FileUtils.patch, editText.getText().toString(),FileUtils.name);
	}
	
	public static void saveOldStyle(Context context){
		
		ContentValues cv = new ContentValues();
		cv.put(Contract.Styles.data, editText.getText().toString());
		cv.put(Contract.Styles.title, oldtitle);
		if(mUri != null)
			context.getContentResolver().update(mUri, cv, null, null);
		else
			context.getContentResolver().update(ContentUris.withAppendedId(Contract.Styles.CONTENT_URI, oldid),cv, null,null);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch(item.getItemId()){
			case R.id.save:
				showExportDialog();
				break;
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class ExportDialogFragment extends DialogFragment {


		private String patch = Environment.getExternalStorageDirectory() + "/data/4pdaclient/styles/white/";
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater layoutInflater = getActivity().getLayoutInflater();

			View view = layoutInflater.inflate(R.layout.save_dialog, null);
			final EditText editName = (EditText) view.findViewById(R.id.editTextName);
			final EditText editPatch = (EditText) view.findViewById(R.id.editTextPatch);
			editName.setText(oldtitle + ".css");
			editPatch.setText(patch);
			builder.setMessage("Експорт")
				.setView(view)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						FileUtils.saveNamedFile(patch, editText.getText().toString(), editName.getText().toString());
					}
				})
				.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ExportDialogFragment.this.getDialog().cancel();
					}
				});
			return builder.create();
		}
    }

	void showExportDialog() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
		DialogFragment dialogFragment = new ExportDialogFragment();

        dialogFragment.show(getActivity().getSupportFragmentManager().beginTransaction(), "dialog");
    }

	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		saveOldStyle(getActivity());
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		// TODO: Implement this method
		super.onSaveInstanceState(outState);
		saveOldStyle(getActivity());
	}
	
	

	@Override
	public void onResume()
	{
		// TODO: Implement this method
		super.onResume();
	}
	
	
}
