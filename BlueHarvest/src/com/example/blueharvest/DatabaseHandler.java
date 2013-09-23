package com.example.blueharvest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class DatabaseHandler extends SQLiteOpenHelper {
		
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "spinnerExample";
 
    // Labels table name
    private static final String TABLE_LABELS = "labels";
    private static final String TABLE_CHRG_DATA = "chrgData";
    private static final String TABLE_SETTINGS = "settings";
    private static final String TABLE_WHSE = "warehouse";
    private static final String TABLE_ITEM = "cost_item";
    private static final String TABLE_TYPE = "cost_type";
    // --Commented out by Inspection (5/15/13 12:04 PM):public static String _tableName = TABLE_CHRG_DATA;
     
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_UPC = "upc";
    private static final String COLUMN_COMMENT = "comment";
    private static final String COLUMN_KEY = "id";
    private static final String COLUMN_ACT_NAME = "account_name";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FROM = "from_email";
    private static final String COLUMN_TO = "to_email";
    private static final String COLUMN_SUBJECT = "subject";
    private static final String COLUMN_BODY = "body";
    private static final String COLUMN_WHSE = "warehouse";
    private static final String COLUMN_ITEM = "cost_item";
    private static final String COLUMN_TYPE = "cost_type";
    
    // Table create strings
    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";
    
    private static final String CREATE_CHRGDATA_TABLE = "CREATE TABLE " + TABLE_CHRG_DATA + "("
    		+ COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT,"
    		+ COLUMN_DATE + " TEXT," + COLUMN_UPC + " TEXT," + COLUMN_COMMENT + " TEXT)";
    
    private static final String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
    		+ COLUMN_KEY + " INTEGER PRIMARY KEY," + COLUMN_ACT_NAME + " TEXT,"
    		+ COLUMN_PASSWORD + " TEXT," + COLUMN_FROM + " TEXT," + COLUMN_TO + " TEXT,"
    		+ COLUMN_SUBJECT + " TEXT," + COLUMN_BODY + " TEXT)";

    private static final String CREATE_WHSE_TABLE = "CREATE TABLE " + TABLE_WHSE + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + COLUMN_WHSE + " TEXT)";

    private static final String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + COLUMN_ITEM + " TEXT)";

    private static final String CREATE_TYPE_TABLE = "CREATE TABLE " + TABLE_TYPE + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + COLUMN_TYPE + " TEXT)";

    /**
     * 
     * @param context   context
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    /**
     * call execSQL to create the databases.
     * 
     * @param db 	SQLite database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables    	
        db.execSQL(CREATE_CATEGORIES_TABLE);        
        db.execSQL(CREATE_CHRGDATA_TABLE);
        db.execSQL(CREATE_SETTINGS_TABLE);
        db.execSQL(CREATE_WHSE_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_TYPE_TABLE);
        setDefaultLabel(db);
    }
    
    /**
     * @param db    database
     * 
     */

    void setDefaultLabel(SQLiteDatabase db) {
    	// create default label
    	//SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(KEY_NAME, "Default");
    	db.insert(TABLE_LABELS, null, values);
    }
    
    /**
     * if version of database changes we need to update.
     * 
     * @param db			SQLite database
     * @param oldVersion	old version of database
     * @param newVersion	new version of database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHRG_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WHSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Insert new label into labels table.
     * 
     * @param label     label
     */
    public void insertLabel(String label){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, label);
 
        // Inserting Row
        db.insert(TABLE_LABELS, null, values);
        db.close(); // Closing database connection
    }
    
    /**
     * Save all fields to the database.
     * 
     * @param _name     name
     * @param _upc      upc
     * @param _date     date
     * @param _comment  comment
     * @param context   context
     */
    public void saveToDb(String _name, String _upc, String _date, String _comment, Context context){
    	//
    	SQLiteDatabase db = this.getWritableDatabase();
    	//*
    	ContentValues values = new ContentValues();
    	values.put(COLUMN_NAME, _name);
    	values.put(COLUMN_DATE, _date);
    	values.put(COLUMN_UPC, _upc);
    	values.put(COLUMN_COMMENT, _comment);
    	
    	// Insert row
    	db.insert(TABLE_CHRG_DATA, null, values);
    	//*/
    	makeText(context, new StringBuilder()
                .append(context.getString(R.string.toast_wrote_to_db_message))
                .append(values).toString(), LENGTH_LONG)
                .show();

    	db.close();
    }
    
    /**
     * @param _actName      email account name
     * @param _password     email account password
     * @param _from         email from address
     * @param _to           email list of to addresses [array]
     * @param _subject      email subject
     * @param _body         email body
     * @param context       context
     */
    public void saveToDb(String _actName, String _password, String _from, String _to, String _subject, String _body, Context context){
    	//
    	SQLiteDatabase db = this.getWritableDatabase();
    	//*
    	ContentValues values = new ContentValues();
    	values.put(COLUMN_ACT_NAME, _actName);
    	values.put(COLUMN_PASSWORD, _password);
    	values.put(COLUMN_FROM, _from);
    	values.put(COLUMN_TO, _to);
    	values.put(COLUMN_SUBJECT, _subject);
    	values.put(COLUMN_BODY, _body);
    	
    	// Insert row
    	db.insert(TABLE_SETTINGS, null, values);
    	//*/
    	makeText(context, new StringBuilder()
                .append(context.getString(R.string.toast_wrote_to_db_message))
                .append(values).toString(), LENGTH_LONG)
                .show();
    	
    	db.close();
    }
    
// --Commented out by Inspection START (5/15/13 12:04 PM):
//    /**
//     *
//     */
//    public void fromDB() {
//    	//SQLiteDatabase db = this.getReadableDatabase();
//
//    	ContentValues values = new ContentValues();
//
//    	values.get(COLUMN_ACT_NAME);
//    	values.get(COLUMN_PASSWORD);
//    	values.get(COLUMN_FROM);
//    	values.get(COLUMN_TO);
//    	values.get(COLUMN_SUBJECT);
//    	values.get(COLUMN_BODY);
//
//    }
// --Commented out by Inspection STOP (5/15/13 12:04 PM)

    /**
     * populate all the fields in the settings activity
     */
    public void populateFields() {
    	
    	//List<String> settingsFields = new ArrayList<String>();
    	SQLiteDatabase db = this.getReadableDatabase();

    	Cursor cursor = db.query(TABLE_SETTINGS, null, null, null, null, null, null);
    	
    	while (cursor.moveToNext()) {
    		String _act_name = cursor.getString(cursor.getColumnIndex(COLUMN_ACT_NAME));
    		String _password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
    		String _from = cursor.getString(cursor.getColumnIndex(COLUMN_FROM));
    		String _to = cursor.getString(cursor.getColumnIndex(COLUMN_TO));
    		String _subject = cursor.getString(cursor.getColumnIndex(COLUMN_SUBJECT));
    		String _body = cursor.getString(cursor.getColumnIndex(COLUMN_BODY));
    		
    		SettingsActivity sa = new SettingsActivity();
    		
    		sa.setActName(_act_name);
    		sa.setPassword(_password);
    		sa.setFrom(_from);
    		sa.setTo(_to);
    		sa.setSubject(_subject);
    		sa.setBody(_body);
    	}
    }
    /*
    // db to file for sending
    public void dbToFile() {
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	final String dbfile = "COPY TABLE_CHRG_DATA TO 'chrgDataFile.txt' (DELIMITER ',')";
		db.execSQL(dbfile);
		db.close();
    }
    //*/    
    /*    
	@SuppressLint("SimpleDateFormat")
	public void setDateTime() {
    	// TODO add DateTime to filename
    	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EST"));
    	Date currentLocalTime = cal.getTime();
    	SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");   
    	date.setTimeZone(TimeZone.getTimeZone("EST")); 
    	String currentDateTime = date.format(currentLocalTime);
    	
    	setFileName(currentDateTime, getBaseContext());
    }
    //*/
    /*
    public void setFileName(String currentDateTime, Context context) {
    	//
    	_filename = currentDateTime + "_upload.csv";
    	
    	Toast.makeText(context, "The fileName is: " + _filename, Toast.LENGTH_LONG).show();
    }    
    //*/
    /**
     * Export the database to a csv file.
     * @param context   context
     */
    public void exportDb(Context context, String _filename) {

    	SQLiteDatabase _db = this.getReadableDatabase();
        //File exportDir = new File(Environment.getExternalStorageDirectory().getPath(), "");
        File exportDir = getDir(context);
        
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, _filename);
        //File file = new File(exportDir, "csvname.csv");
        try
        {
            file.createNewFile();               
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor curCSV = db.rawQuery(new StringBuilder()
                    .append("SELECT * FROM ")
                    .append(TABLE_CHRG_DATA).toString(),null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
               //Which column you want to export
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2), curCSV.getString(3), curCSV.getString(4)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
        _db.close();
    }
    
    /**
     * get the directory path to the upload file
     *
     * @param context   context
     * @return          exportDir
     */
    File getDir(Context context) {
    	File exportDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            exportDir = new File(android.os.Environment.getExternalStorageDirectory(),"");
        } else {
            exportDir = context.getCacheDir();
        }                
        return exportDir;
    }
    
    /**
     * removes label from labels table
     * 
     * @param label     label
     */
    public void removeLabel(String label) {
    	SQLiteDatabase db = this.getWritableDatabase();

    	ContentValues values = new ContentValues();
    	values.put(KEY_NAME, label);
    	
    	// removing row
    	db.delete(TABLE_LABELS, KEY_NAME + "=?", new String[] {label});
    	db.close();
    }
    
// --Commented out by Inspection START (5/15/13 12:04 PM):
//    /**
//     * Remove all data from Table chrgData
//     */
//    public void purgeTable(String _tableName){
//    	SQLiteDatabase dbWritable = this.getWritableDatabase();
//    	dbWritable.delete(_tableName, null, null);
//    	dbWritable.close();
//    }
// --Commented out by Inspection STOP (5/15/13 12:04 PM)

    /**
     * purge the charge data table.
     *
     */
    public void purgeChrgData() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_CHRG_DATA, null, null);
    	db.close();
    }

    /**
     * purge the settings table
     */
    public void purgeSettings() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_SETTINGS, null, null);
    	db.close();
    }
        
    /**
     * returns list of labels.
     * 
     * @return	returns list of labels.
     */
    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(new StringBuilder()
                .append("SELECT  * FROM ")
                .append(TABLE_LABELS).toString(), null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
 
        // closing connection
        cursor.close();
        db.close();
 
        // returning labels
        return labels;
    }
}
