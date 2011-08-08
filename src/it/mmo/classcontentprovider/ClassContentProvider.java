package it.mmo.classcontentprovider;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import it.mmo.classcontentprovider.annotations.Authority;
import it.mmo.classcontentprovider.annotations.row.Autoincrement;
import it.mmo.classcontentprovider.annotations.row.PrimaryKey;
import it.mmo.classcontentprovider.annotations.row.Row;
import it.mmo.classcontentprovider.annotations.table.MimeType;
import it.mmo.classcontentprovider.annotations.table.Table;
import it.mmo.classcontentprovider.model.RowModel;
import it.mmo.classcontentprovider.model.TableModel;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ClassContentProvider extends ContentProvider {

	private static HashMap<String, TableModel> _tables = new HashMap<String, TableModel>();

	private static UriMatcher _uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	private static String db_name;

	private static int ALLROWS = 0;
	private static int SINGLE_ROW = 1;

	private SQLiteDatabase database;

	private static int max = 0;
	private static HashMap<Integer, String> _uris = new HashMap<Integer, String>();
	private static HashMap<String, String> _fks = new HashMap<String, String>();

	protected String provideClass(Class<?> clazz) {
		Class<?> t = clazz;
		Table table = t.getAnnotation(Table.class);
		TableModel tab = new TableModel();
		Package p = t.getPackage();
		db_name = p.toString() + ".db";
		tab.setName(table.name());
		for (Field f : t.getFields()) {
			Row row = f.getAnnotation(Row.class);
			if (row != null) {
				if (f.getType().getPackage().equals(p)) {
					String fk = this.provideClass(f.getType());
					_fks.put(tab.getName(), fk);
				}
				RowModel r = new RowModel();
				r.setName(row.name());
				r.setDatatype(f.getType());
				if (f.getAnnotation(PrimaryKey.class) != null)
					r.setPk(true);
				if (f.getAnnotation(Autoincrement.class) != null)
					r.setAi(true);
				tab.addRow(r);
			}
		}
		String company = t.getAnnotation(MimeType.class).company();
		tab.setAll_rows_mime(company, t.getName().toLowerCase());
		_tables.put(tab.getName(), tab);

		String auth = t.getAnnotation(Authority.class).name();
		_uriMatcher.addURI(auth, tab.getName(), max + ALLROWS);
		_uriMatcher.addURI(auth, tab.getName() + "/#", max + SINGLE_ROW);
		_uris.put(max + ALLROWS, tab.getName());
		_uris.put(max + SINGLE_ROW, tab.getName());
		max += 2;
		return tab.getName();
	}
	
	public List<String> getColumns(String table_name) {
		TableModel t = _tables.get(table_name);
		List<String> ret = new LinkedList<String>();
		for (RowModel r : t.getRows()) {
			ret.add(r.getName());
		}
		return ret;
	}

	private String getCreationString() {
		StringBuilder b = new StringBuilder();
		for (TableModel t : _tables.values()) {
			b.append(t.toSQL());
		}
		return b.toString();
	}

	private String getDestructionString() {
		StringBuilder b = new StringBuilder();
		for (TableModel t : _tables.values()) {
			b.append("drop table if exists ");
			b.append(t.getName());
			b.append(";");
		}
		return b.toString();
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DBHelper h = new DBHelper(context, db_name, null, 1);
		database = h.getWritableDatabase();
		return (database == null) ? false : true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		if (_uriMatcher.match(uri) % 2 == 0)
			if (_uris.containsKey(_uriMatcher.match(uri)))
				return _tables.get(_uris.get(_uriMatcher.match(uri)))
						.getAll_rows_mime();
		if (_uriMatcher.match(uri) % 2 == 1)
			if (_uris.containsKey(_uriMatcher.match(uri)))
				return _tables.get(_uris.get(_uriMatcher.match(uri)))
						.getSingle_row_mime();
		throw new IllegalArgumentException("Unsupported URI: " + uri);

	}

	private String getTables(Uri uri) {
		StringBuilder b = new StringBuilder();
		String table = getTableName(uri);
		b.append(table);
		while (!(_fks.get(table).equals(table))){
			b.append(',');
			table = _fks.get(table);
			b.append(table);
		}
		return b.toString();
	}

	private String getTableName(Uri uri) {
		if (_uris.containsKey(_uriMatcher.match(uri)))
			return _tables.get(_uris.get(_uriMatcher.match(uri))).getName();
		throw new IllegalArgumentException("Unsupported URI: " + uri);
	}
	
	private TableModel getTable(Uri uri){
		if (_uris.containsKey(_uriMatcher.match(uri)))
			return _tables.get(_uris.get(_uriMatcher.match(uri)));
		throw new IllegalArgumentException("Unsupported URI: " + uri);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(getTables(uri));
		if (_uriMatcher.match(uri) % 2 == 1)
			if (_uris.containsKey(_uriMatcher.match(uri)))
				qb.appendWhere(getTable(uri).getPK()+"="+uri.getPathSegments().get(1));
		Cursor c = qb.query(database, projection, selection, selectionArgs, null,null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;		
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	protected class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(getCreationString());

		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int old_ver, int new_ver) {
			_db.execSQL(getDestructionString());
			onCreate(_db);

		}

	}
}
