package com.trablone.csscreated.database;

import android.net.Uri;
import android.provider.*;

public final class Contract
{
    private Contract()
    {}

    public static final String AUTHORITY = "com.trablone.csscreated.database";
    
    public static final class Styles
    {
		private Styles()
		{}

		public static final String data = "data";
		public static final String title = "title";
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/Styles");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".Styles";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + ".Styles";

		public static final String DEFAULT_SORT_ORDER = BaseColumns._ID + " DESC";

    }
	
}
