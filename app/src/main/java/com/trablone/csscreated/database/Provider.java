package com.trablone.csscreated.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class Provider extends ContentProvider
{

   	private static final int STYLE = 1;
	private static final int STYLE_ID = 2;
	
    private static final UriMatcher sUriMatcher;
    private Database mOpenHelper;

    @Override
    public boolean onCreate()
	{
        mOpenHelper = new Database(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
        String groupBy = null;
        String defaultSortOrder;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri))
		{
			case STYLE:
				qb.setTables(Database.STYLE_TABLE);
				defaultSortOrder = Contract.Styles.DEFAULT_SORT_ORDER;
				break;
			case STYLE_ID:
				qb.setTables(Database.STYLE_TABLE);
				defaultSortOrder = Contract.Styles.DEFAULT_SORT_ORDER;
				qb.appendWhere("_id=" + uri.getLastPathSegment());
				break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        String orderBy;
        if (TextUtils.isEmpty(sortOrder))
		{
            orderBy = defaultSortOrder;
        }
		else
		{
            orderBy = sortOrder;
        }
        Cursor c = qb.query(mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, groupBy, null, orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri)
	{
        switch (sUriMatcher.match(uri))
		{
           	case STYLE:
				return Contract.Styles.CONTENT_TYPE;
			case STYLE_ID:
				return Contract.Styles.CONTENT_ITEM_TYPE;
				default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
	{
        String tableName;
        switch (sUriMatcher.match(uri))
		{
            
			case STYLE:
				tableName = Database.STYLE_TABLE;
				break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        long rowId = mOpenHelper.getWritableDatabase().insert(tableName, null, values);
        if (rowId > 0)
		{
            Uri itemUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(itemUri, null);
            return itemUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
	{
        String tableName;
        switch (sUriMatcher.match(uri))
		{
			
			case STYLE_ID:
				tableName = Database.STYLE_TABLE;
				selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;
			
			case STYLE:
				tableName = Database.STYLE_TABLE;
				break;
			
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        int count =  mOpenHelper.getWritableDatabase().delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{
        String tableName;
        switch (sUriMatcher.match(uri))
		{
            
			case STYLE_ID:
				tableName = Database.STYLE_TABLE;
				selection = "_id=" + uri.getLastPathSegment() + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
				break;
			
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        int count = mOpenHelper.getWritableDatabase().update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(Contract.AUTHORITY, "Styles", STYLE);
		sUriMatcher.addURI(Contract.AUTHORITY, "Styles/#", STYLE_ID);

    }

}
