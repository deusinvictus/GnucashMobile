/*
 * Written By: Ngewi Fet <ngewif@gmail.com>
 * Copyright (c) 2012 Ngewi Fet
 *
 * This file is part of Gnucash for Android
 * 
 * Gnucash for Android is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, contact:
 *
 * Free Software Foundation           Voice:  +1-617-542-5942
 * 51 Franklin Street, Fifth Floor    Fax:    +1-617-542-2652
 * Boston, MA  02110-1301,  USA       gnu@gnu.org
 */

package org.gnucash.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Adapter to be used for creating and opening the database for read/write operations.
 * The adapter abstracts several methods for database access and should be subclassed
 * by any other adapters to database-backed data models.
 * @author Ngewi Fet <ngewif@gmail.com>
 *
 */
public class DatabaseAdapter {
	/**
	 * Tag for logging
	 */
	protected static final String TAG = "DatabaseAdapter";
	
	//Column indices for the various columns in the database tables
	//row_id, uid, name and type are common to both tables
	public static final int COLUMN_ROW_ID = 0;
	public static final int COLUMN_UID 	= 1;
	public static final int COLUMN_NAME = 2;
	public static final int COLUMN_TYPE = 3;
	
	public static final int COLUMN_AMOUNT 		= 4;
	public static final int COLUMN_DESCRIPTION 	= 5;
	public static final int COLUMN_TIMESTAMP 	= 6;
	public static final int COLUMN_ACCOUNT_UID 	= 7;
	public static final int COLUMN_EXPORTED 	= 8;
	
	public static final int COLUMN_CURRENCY_CODE = 1;
	public static final int COLUMN_ACCOUNT_CURRENCY_CODE = 4;
	
	/**
	 * {@link DatabaseHelper} for creating and opening the database
	 */
	protected DatabaseHelper mDbHelper;
	
	/**
	 * SQLite database
	 */
	protected SQLiteDatabase mDb;
	
	/**
	 * Application context
	 */
	protected Context mContext;
	
	/**
	 * Opens (or creates if it doesn't exist) the database for reading and writing
	 * @param context Application context to be used for opening database
	 */
	public DatabaseAdapter(Context context) {
		mDbHelper = new DatabaseHelper(context);
		mContext = context.getApplicationContext();
		open();
	}

	/**
	 * Opens/creates database to be used for reading or writing. 
	 * @return Reference to self for database manipulation
	 */
	public DatabaseAdapter open(){
		try {
			mDb = mDbHelper.getWritableDatabase();
		} catch (SQLException e) {
			Log.e(TAG, "Error getting database: " + e.getMessage());
			mDb = mDbHelper.getReadableDatabase();
		}
		
		return this;
	}
	
	/**
	 * Close the database
	 */
	public void close(){
		mDbHelper.close();
		mDb.close();
	}
	
	/**
	 * Retrieves record with id <code>rowId</code> from table <code>tableName</code>
	 * @param tableName Name of table where record is found
	 * @param rowId ID of record to be retrieved
	 * @return {@link Cursor} to record retrieved
	 */
	public Cursor fetchRecord(String tableName, long rowId){
		return mDb.query(tableName, null, DatabaseHelper.KEY_ROW_ID + "=" + rowId, 
				null, null, null, null);
	}
	
	/**
	 * Retrieves all records from database table <code>tableName</code>
	 * @param tableName Name of table in database
	 * @return {@link Cursor} to all records in table <code>tableName</code>
	 */
	public Cursor fetchAllRecords(String tableName){
		return mDb.query(tableName, 
        		null, null, null, null, null, null);
	}

	/**
	 * Deletes record with ID <code>rowID</code> from database table <code>tableName</code> 
	 * @param tableName Name of table in database
	 * @param rowId ID of record to be deleted
	 * @return <code>true</code> if deletion was successful, <code>false</code> otherwise
	 */
	public boolean deleteRecord(String tableName, long rowId){
		return mDb.delete(tableName, DatabaseHelper.KEY_ROW_ID + "=" + rowId, null) > 0;
	}

}
