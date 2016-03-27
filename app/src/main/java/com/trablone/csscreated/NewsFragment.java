package com.trablone.csscreated;
import android.support.v4.app.*;
import android.webkit.*;
import android.view.*;
import android.os.*;
import android.widget.*;

public class NewsFragment extends BaseFragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		View v = inflater.inflate(R.layout.webview_layout, container, false);
		return initialiseUi(v);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		showContent();
	}

	@Override
	protected void onResumeFrag()
	{
		// TODO: Implement this method
		super.onResumeFrag();
		MainActivity.setMenu(false);
		getWebView().clearCache(true);
		
		getTask();
	}
	
	@Override
	protected void onPauseFrag()
	{
		// TODO: Implement this method
		super.onPauseFrag();
		inPostExecute("");
	}
	
	@Override
	protected String inBackground()
	{
		// TODO: Implement this method
		super.inBackground();
		return FileUtils.LoadData(getActivity(),"news.html");
	}

	@Override
	protected void inPostExecute(String result)
	{
		// TODO: Implement this method
		super.inPostExecute(result);
		reload(result);
	}
	
	
}
