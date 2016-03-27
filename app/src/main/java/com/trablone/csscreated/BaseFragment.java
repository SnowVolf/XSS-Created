package com.trablone.csscreated;
import android.support.v4.app.*;
import java.io.*;
import android.os.*;
import android.webkit.*;
import android.content.*;
import android.net.*;
import android.widget.*;
import android.view.*;

public class BaseFragment extends Fragment implements FragmentLifecycle
{
	
	private Task mTask;
	private WebView wv;
	private LinearLayout progress;

	@Override
	public void onResumeFragment()
	{
		onResumeFrag();
		refreshMenu.refreshActionBarMenu(getActivity());
	}

	@Override
	public void onPauseFragment()
	{
		onPauseFrag();
	}
	
	public WebView getWebView(){
		return wv;
	}
	
	protected void onPauseFrag(){}
	
	protected void onResumeFrag(){}
	
	protected String inBackground(){return null;}
	
	protected void inPostExecute(String result){}

	public void getTask(){
		mTask = new Task();
		mTask.execute();
	}
	
	public View initialiseUi(View v){
		wv = (WebView) v.findViewById(R.id.webView);
		progress = (LinearLayout)v.findViewById(R.id.linearProgress);
		return v;
	}
	
	public static class refreshMenu
	{
		public static void refreshActionBarMenu(FragmentActivity activity)
		{
			activity.invalidateOptionsMenu();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()){
			case R.id.refresh:
				getWebView().clearCache(true);
				getTask();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	

	public class Task extends AsyncTask<Void, Void, String>
	{

		@Override
		protected void onPreExecute()
		{
			// TODO: Implement this method
			super.onPreExecute();
			setProgress(true);
		}

		
		@Override
		protected String doInBackground(Void[] p1)
		{
			EditorFragment.setCustomCss();
			return inBackground();
		}

		@Override
		protected void onPostExecute(String result)
		{
			// TODO: Implement this method
			super.onPostExecute(result);
			inPostExecute(result);
			setProgress(false);
		}
	}
	
	public void setProgress(boolean hide){
		progress.setVisibility(!hide ? View.GONE : View.VISIBLE);
	}
	
	public void reload(String result){
		result = result.replace("%custom_css%", "file:///" + FileUtils.patch + "/" + FileUtils.name);
		wv.loadDataWithBaseURL("http://4pda.ru/forum/", result, "text/html", "UTF-8", null);
	}
	
	public void showContent()
	{
		getWebView().getSettings().setSupportZoom(true);
		getWebView().getSettings().setBuiltInZoomControls(true);
		getWebView().getSettings().setDisplayZoomControls(false);
		getWebView().getSettings().setJavaScriptEnabled(true);
		getWebView().getSettings().setAppCacheEnabled(true);
		getWebView().clearCache(true);
		getWebView().getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

		getWebView().setBackgroundColor(0x00000000);
		getWebView().setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		
		getWebView().setWebViewClient(new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url)
				{
					super.shouldOverrideUrlLoading(view, url);
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					getActivity().startActivity(intent);
					return 	true;
				}

			});
	}
}
