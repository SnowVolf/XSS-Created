package com.trablone.csscreated;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.*;

public class NewsPagerFragment extends Fragment
{
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
    private String[] urls = {"editor", "topic", "news", "gms", "lessons"};
    private String[] title = {"Редактор", "Топик", "Новость", "Qms", "Уроки"};

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.news_list_pager, container, false);
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		final ActionBar actionBar = getActivity().getActionBar();
		mViewPager.setOffscreenPageLimit(urls.length);
		
		mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(0);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				mViewPager.setCurrentItem(tab.getPosition());
				
			}

			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
			}

			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
			}
		};

		if(savedInstanceState == null){
		if(actionBar.getTabCount() > 0)
			actionBar.removeAllTabs();
		for (int i = 0; i < urls.length; i++) {
			actionBar.addTab(
				actionBar.newTab()
				.setText(title[i])
				.setTabListener(tabListener));
		}
		}
		
			

		mViewPager.setOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {

				private int oldposition=0;
				@Override
                public void onPageSelected(int position) {
                    getActivity().getActionBar().setSelectedNavigationItem(position);
				
					FragmentLifecycle fragmentToShow = (FragmentLifecycle)mPagerAdapter.getItem(position);
					fragmentToShow.onResumeFragment();
					FragmentLifecycle fragmentToPause = (FragmentLifecycle)mPagerAdapter.getItem(oldposition);
					fragmentToPause.onPauseFragment();
					oldposition = position;
				
                }
            });	
		
	}
	public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

	public PagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
				fragments.add(new EditorFragment());
				fragments.add(new TopicsFragment());
				fragments.add(new NewsFragment());
				fragments.add(new QmsFragment());
				fragments.add(new LessonFragment());
	}

    @Override
    public Fragment getItem(int i) {
      return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}

	
}
