package com.jonasSoftware.blueharvest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * @author Brad Bass
 * @version 1
 */

public class DatabaseHandler extends SQLiteOpenHelper {
		
    // Database Version
    private static final int DATABASE_VERSION = 12;
 
    // Database Name
    private static final String DATABASE_NAME = "jonasScanner";
 
    // Labels table name
    private static final String TABLE_LABELS = "labels";
    private static final String TABLE_CHRG_DATA = "chrgData";
    private static final String TABLE_UPLOAD_DATA = "uploadData";
    private static final String TABLE_SETTINGS = "settings";
    private static final String TABLE_WHSE = "warehouse";
    private static final String TABLE_ITEM = "cost_item";
    private static final String TABLE_TYPE = "cost_type";
    // --Commented out by Inspection (5/15/13 12:04 PM):public static String _tableName = TABLE_CHRG_DATA;
    private static String selectQuery;
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    //private static final String COLUMN_ID = "id";
    //private static final String COLUMN_NAME = "name";
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
    private static final String COLUMN_JOB_WO_NUM = "job_wo_num";
    private static final String COLUMN_SERIAL = "serial";
    private static final String COLUMN_QUANTITY = "quantity";
    //
    static int _recordNum;
    static Boolean _existingRec = false;
    final SQLiteDatabase _dbr = this.getReadableDatabase();
    final SQLiteDatabase _dbw = this.getWritableDatabase();
    // Login table name
    private static final String TABLE_LOGIN = "login";
    // Login Table Columns names
    //private static final String KEY_ID = "id";
    //private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    
    // Table create strings
    private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT)";
    
    private static final String CREATE_CHRGDATA_TABLE = "CREATE TABLE " + TABLE_CHRG_DATA + "("
            + COLUMN_KEY + " INTEGER PRIMARY KEY,"
            + COLUMN_WHSE + " TEXT,"
            + COLUMN_JOB_WO_NUM + " TEXT,"
            + COLUMN_ITEM + " TEXT,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_UPC + " TEXT,"
            + COLUMN_QUANTITY + " TEXT,"
            + COLUMN_SERIAL + " TEXT,"
            + COLUMN_COMMENT + " TEXT,"
            + COLUMN_DATE + " TEXT)";

    private static final String CREATE_UPLOADDATA_TABLE = "CREATE TABLE " + TABLE_UPLOAD_DATA + "("
            + COLUMN_KEY + " INTEGER PRIMARY KEY,"
            + COLUMN_WHSE + " TEXT,"
            + COLUMN_UPC + " TEXT,"
            + COLUMN_QUANTITY + " TEXT)";
    
    private static final String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
    		+ COLUMN_KEY + " INTEGER PRIMARY KEY,"
            + COLUMN_ACT_NAME + " TEXT,"
    		+ COLUMN_PASSWORD + " TEXT,"
            + COLUMN_FROM + " TEXT,"
            + COLUMN_TO + " TEXT,"
    		+ COLUMN_SUBJECT + " TEXT,"
            + COLUMN_BODY + " TEXT)";

    private static final String CREATE_WHSE_TABLE = "CREATE TABLE " + TABLE_WHSE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_WHSE + " TEXT)";

    private static final String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_ITEM + " TEXT)";

    private static final String CREATE_TYPE_TABLE = "CREATE TABLE " + TABLE_TYPE + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TYPE + " TEXT)";

    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_USERNAME + " TEXT UNIQUE,"
            + KEY_UID + " TEXT,"
            + KEY_CREATED_AT + " TEXT" + ")";

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
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_UPLOADDATA_TABLE);
        setDefaultLabel(db);
        //insertBlankRow();
    }
    
    /**
     * @param db    database
     * 
     */
    void setDefaultLabel(SQLiteDatabase db) {
    	// creates default label
    	//SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(KEY_NAME, "WHSE");
        db.insert(TABLE_LABELS, null, values);
        values.put(KEY_NAME, "TYPE");
        db.insert(TABLE_LABELS, null, values);
        values.put(KEY_NAME, "ITEM");
    	db.insert(TABLE_LABELS, null, values);
        //*
        ContentValues valuesWhse = new ContentValues();
        valuesWhse.put(COLUMN_WHSE, "");
        db.insert(TABLE_WHSE, null, valuesWhse);
        ContentValues valuesType = new ContentValues();
        valuesType.put(COLUMN_TYPE, "");
        db.insert(TABLE_TYPE, null, valuesType);
        ContentValues valuesItem = new ContentValues();
        valuesItem.put(COLUMN_ITEM, "");
        db.insert(TABLE_ITEM, null, valuesItem);
        //*/
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPLOAD_DATA);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Insert new label into labels table.
     * 
     * @param label     label
     */
    public void insertLabel(int table, String label){
        //
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, label);

        switch (table) {
            case 1:
                values.put(COLUMN_WHSE, label);
                assert db != null;
                db.insert(TABLE_WHSE, null, values);
                break;
            case 2:
                values.put(COLUMN_ITEM, label);
                assert db != null;
                db.insert(TABLE_ITEM, null, values);
                break;
            case 3:
                values.put(COLUMN_TYPE, label);
                assert db != null;
                db.insert(TABLE_TYPE, null, values);
        }

        if (db != null) {
            db.close(); // Closing database connection
        }
    }

    /*/
    private void insertBlankRow() {
        //insert a blank row into WHSE, ITEM and TYPE tables
        for (int i=1;i<4;i++) {
            insertLabel(i," ");
        }
    }//*/
    
    /**
     * Save all fields to the database.
     *
     */
    public void saveToDb(String _whse, String _wo, String _costItem,
                         String _costType, String _upc, String _quantity, String _serial,
                         String _comment, String _date, Context context, Boolean save){
    	// save fields to db with new fields
    	//SQLiteDatabase db = this.getWritableDatabase();
    	//*
    	ContentValues values = new ContentValues();
    	values.put(COLUMN_WHSE, _whse);
        values.put(COLUMN_JOB_WO_NUM, _wo);
        values.put(COLUMN_ITEM, _costItem);
        values.put(COLUMN_TYPE, _costType);
        values.put(COLUMN_UPC, _upc);
        values.put(COLUMN_QUANTITY, _quantity);
        values.put(COLUMN_SERIAL, _serial);
        values.put(COLUMN_COMMENT, _comment);
        values.put(COLUMN_DATE, _date);
    	
    	// Insert row
        assert _dbw != null;
        if (_existingRec == false) {
            _dbw.insert(TABLE_CHRG_DATA, null, values);
        } else {
            // TODO - fix update existing record.
            _dbw.update(TABLE_CHRG_DATA, values, "id=?", new String[] {Long.toString(_recordNum)});
        }
        //*/
    	makeText(context, context.getString(R.string.toast_wrote_to_db_message)
                + values, LENGTH_LONG)
                .show();

    	//_dbw.close();
        _existingRec = false;
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
    public void saveToDb(String _actName, String _password, String _from, String _to,
                         String _subject, String _body, Context context){
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
        assert db != null;
        db.insert(TABLE_SETTINGS, null, values);
    	//*/
    	makeText(context, context.getString(R.string.toast_wrote_to_db_message)
                + values, LENGTH_LONG)
                .show();
    	
    	db.close();
    }

    public void saveToDb(String whse, String upc, String quantity, Context context) {
        //do stuff
        SQLiteDatabase db = this.getWritableDatabase();
        //*
        // Insert row
        ContentValues values = new ContentValues();
        values.put(COLUMN_WHSE, whse);
        values.put(COLUMN_UPC, upc);
        values.put(COLUMN_QUANTITY, quantity);

        assert db != null;
        db.insert(TABLE_CHRG_DATA, null, values);
        //*/
        makeText(context, context.getString(R.string.toast_wrote_to_db_message)
                + values, LENGTH_LONG)
                .show();

        //db.close();
    }

    /**
     * populate all the fields in the settings activity
     */
    public void populateFields() {
    	
    	//List<String> settingsFields = new ArrayList<String>();
    	SQLiteDatabase db = this.getReadableDatabase();

        assert db != null;
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

    /**
     * populate the fields in the charge activity when using the navigation buttons
     */
    public void populateFields(int recordNum) {

        // populate the fields using the cursor position
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        assert db != null;
        try {
            cursor = db.query(TABLE_CHRG_DATA, null, null, null, null, null, null);
            cursor.moveToPosition(recordNum);

            String _upc = cursor.getString(cursor.getColumnIndex(COLUMN_UPC));
            String _date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            String _wo = cursor.getString(cursor.getColumnIndex(COLUMN_JOB_WO_NUM));
            String _whse = cursor.getString(cursor.getColumnIndex(COLUMN_WHSE));
            String _item = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM));
            String _type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
            String _qty = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));
            String _serial = cursor.getString(cursor.getColumnIndex(COLUMN_SERIAL));
            String _comment = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT));

            ChargeActivity ca = new ChargeActivity();

            ca.setUPC(_upc);
            ca.setDate(_date);
            ca.setWO(_wo);
            ca.setWHSE(_whse);
            ca.setItem(_item);
            ca.setType(_type);
            ca.setQty(_qty);
            ca.setSerial(_serial);
            ca.setComment(_comment);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Export the database to a csv file.
     * @param context   context
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void exportDb(Context context, String _filename) {
        //
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
            assert db != null;
            Cursor curCSV = db.rawQuery("SELECT * FROM " + TABLE_CHRG_DATA,null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
               //Which column you want to export
                String arrStr[] ={curCSV.getString(1), curCSV.getString(2),
                        curCSV.getString(3), curCSV.getString(4), curCSV.getString(5), curCSV.getString(6),
                        curCSV.getString(7), curCSV.getString(8), curCSV.getString(9)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("HomeActivity", sqlEx.getMessage(), sqlEx);
        }
        if (_db != null) {
            _db.close();
        }
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
    public void removeLabel(int table, String label) {
        //
    	SQLiteDatabase db = this.getWritableDatabase();

    	ContentValues values = new ContentValues();
    	values.put(KEY_NAME, label);

        switch (table) {
            case 1:
                assert db != null;
                db.delete(TABLE_WHSE, COLUMN_WHSE + "=?", new String[] {label});
                break;
            case 2:
                assert db != null;
                db.delete(TABLE_ITEM, COLUMN_ITEM + "=?", new String[] {label});
                break;
            case 3:
                assert db != null;
                db.delete(TABLE_TYPE, COLUMN_TYPE + "=?", new String[] {label});
        }

    	// removing row
    	//db.delete(TABLE_LABELS, KEY_NAME + "=?", new String[] {label});
        if (db != null) {
            db.close();
        }
    }

    /**
     * purge the charge data table.
     *
     */
    public void purgeChrgData() {
    	SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(TABLE_CHRG_DATA, null, null);
    	db.close();
    }

    /**
     * purge the settings table
     */
    public void purgeSettings() {
    	SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(TABLE_SETTINGS, null, null);
    	db.close();
    }
        
    /**
     * returns list of labels.
     * 
     * @return	returns list of labels.
     */
    public List<String> getAllLabels(String table){
        //
        List<String> labels = new ArrayList<String>();

        if (table.equals("1")) {
            selectQuery = "SELECT * FROM " + TABLE_WHSE;
        } else if (table.equals("2")) {
            selectQuery = "SELECT * FROM " + TABLE_ITEM;
        } else if (table.equals("3")) {
            selectQuery = "SELECT * FROM " + TABLE_TYPE;
        } else if (table.equals("0")) {
            selectQuery = "SELECT * FROM " + TABLE_LABELS;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        assert db != null;
        Cursor cursor = db.rawQuery(selectQuery, null);
 
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

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_USERNAME, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        assert db != null;
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        assert db != null;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        assert db != null;
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        assert db != null;
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

    public void moveToFirst(String dbName) {
        //go to the first record in the db
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = _dbr.query(dbName, null, null, null, null, null, null);
        cursor.moveToFirst();
        _recordNum = cursor.getPosition();
        populateFields(_recordNum);
        _existingRec = true;
    }

    public void moveToLast(String dbName) {
        //go to last record in the db
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = _dbr.query(dbName, null, null, null, null, null, null);
        cursor.moveToLast();
        _recordNum = cursor.getPosition();
        populateFields(_recordNum);
        _existingRec = true;
    }

    public void moveToNext(String dbName, Context context) {
        //go to the next record
        Cursor cursor = _dbr.query(dbName, null, null, null, null, null, null);
        cursor.moveToPosition(_recordNum);
        cursor.moveToNext();
        int currentPos = _recordNum;
        _recordNum = cursor.getPosition();
        //*
        if (currentPos == _recordNum) {
            makeText(context, context.getString(R.string.onLastRecord), LENGTH_LONG).show();
        }
        //*/
        populateFields(_recordNum);
        _existingRec = true;
    }

    public void moveToPrevious(String dbName, Context context) {
        //go to previous record
        Cursor cursor = _dbr.query(dbName, null, null, null, null, null, null);
        cursor.moveToPosition(_recordNum);
        cursor.moveToPrevious();
        int currentPos = _recordNum;
        _recordNum = cursor.getPosition();
        //*
        if (currentPos == _recordNum) {
            makeText(context, context.getString(R.string.onFirstRecord), LENGTH_LONG).show();
        }//*/
        populateFields(_recordNum);
        _existingRec = true;
    }

    public void deleteAll(String dbName) {
        //delete all records in db
        _dbw.delete(dbName, null, null);
    }

    public void deleteOne(String dbName) {
        // TODO - delete one record - use _recordNum
    }
}
