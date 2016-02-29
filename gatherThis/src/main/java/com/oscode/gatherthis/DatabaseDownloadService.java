package com.oscode.gatherthis;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;


public class DatabaseDownloadService extends IntentService {

	public final class Constants {
	    
	    // Defines a custom Intent action
	    public static final String BROADCAST_ACTION =
	        "com.oscode.gatherthis.BROADCAST";
	    
	    // Defines the key for the status "extra" in an Intent
	    public static final String EXTENDED_DATA_STATUS =
	        "com.oscode.gatherthis.STATUS";
	    
	}
	
	DB openHelper;
	private SQLiteDatabase database;
	
	public DatabaseDownloadService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public DatabaseDownloadService() {
		super("GatherThisDatabaseDownloadService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		openHelper = new DB(this,
				Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
		database = openHelper.getWritableDatabase();
		new GetCards(new Database(database), this).execute();
	}

}

