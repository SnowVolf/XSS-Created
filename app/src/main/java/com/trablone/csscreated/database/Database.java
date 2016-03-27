package com.trablone.csscreated.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.database.*;
import java.util.*;
import android.content.*;

public class Database extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "style.db";
    private static final int DATABASE_VERSION = 1;
	
	static final String STYLE_TABLE = "Styles";
    
    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

		db.execSQL("CREATE TABLE " + STYLE_TABLE + " ("
				   + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				   + Contract.Styles.data + " TEXT,"
				   + Contract.Styles.title + " TEXT"
				   + ")");
				   
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
		db.execSQL("DROP TABLE IF EXISTS " + STYLE_TABLE);
		onCreate(db);

    }
	
	
	
}
