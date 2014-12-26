package com.cht.chihua.database;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class SDCardSQLiteOpenHelper extends SQLiteOpenHelper{
	private static final int miDatabaseVersion = 1;
	
	public static final String msDataTable = "data_table";
	public static final String msMappingTable= "mapping_table";
	private static final String msDatabaseName = "mobileinfo.db";
	
	private File mcDatabaseFile = null;
	private SQLiteDatabase mcDatabase = null;
	private boolean mbIsInitializing = false;
	
	public SDCardSQLiteOpenHelper(Context mcContext) {
        super(mcContext, msDatabaseName, null, miDatabaseVersion);
    }

	@Override
	public void onCreate(SQLiteDatabase mcDatabase) {
		mcDatabase.beginTransaction();
		try {
			String sql = "CREATE TABLE " + msDataTable + " ("
					+ "ID INTEGER PRIMARY KEY" 
					+", cell_1 DOUBLE"
					+", cell_2 DOUBLE"
					+", cell_3 DOUBLE"
					+", cell_4 DOUBLE"
					+", cell_5 DOUBLE"
					+", cell_6 DOUBLE"
					+", cell_7 DOUBLE"
					+", cell_8 DOUBLE"
					+", cell_9 DOUBLE"
					+", cell_10 DOUBLE"
					+", cell_11 DOUBLE"
					+", cell_12 DOUBLE"
					+", cell_13 DOUBLE"
					+", cell_14 DOUBLE"
					+", cell_15 DOUBLE"
					+", cell_16 DOUBLE"
					+", cell_17 DOUBLE"
					+", cell_18 DOUBLE"
					+", cell_19 DOUBLE"
					+", cell_20 DOUBLE"
					+", cell_21 DOUBLE"
					+", cell_22 DOUBLE"
					+", cell_23 DOUBLE"
					+", cell_24 DOUBLE"
					+", cell_25 DOUBLE"
					+", cell_26 DOUBLE"
					+", cell_27 DOUBLE"
					+", cell_28 DOUBLE"
					+", cell_29 DOUBLE"
					+", cell_30 DOUBLE"
					+", cell_31 DOUBLE"
					+", cell_32 DOUBLE"
					+", cell_33 DOUBLE"
					+", cell_34 DOUBLE"
					+", cell_35 DOUBLE"
					+", cell_36 DOUBLE"
					+", cell_37 DOUBLE"
					+", cell_38 DOUBLE"
					+", cell_39 DOUBLE"
					+", cell_40 DOUBLE"
					+", cell_41 DOUBLE"
					+", cell_42 DOUBLE"
					+", cell_43 DOUBLE"
					+", cell_44 DOUBLE"
					+", cell_45 DOUBLE"
					+", cell_46 DOUBLE"
					+", cell_47 DOUBLE"
					+", cell_48 DOUBLE"
					+", cell_49 DOUBLE"
					+", cell_50 DOUBLE"
					+", cell_51 DOUBLE"
					+", cell_52 DOUBLE"
					+", cell_53 DOUBLE"
					+", cell_54 DOUBLE"
					+", cell_55 DOUBLE"
					+", cell_56 DOUBLE"
					+", cell_57 DOUBLE"
					+", cell_58 DOUBLE"
					+", cell_59 DOUBLE"
					+", cell_60 DOUBLE"
					+", cell_61 DOUBLE"
					+", cell_62 DOUBLE"
					+", cell_63 DOUBLE"
					+", cell_64 DOUBLE"
					+", cell_65 DOUBLE"
					+", cell_66 DOUBLE"
					+", cell_67 DOUBLE"
					+", cell_68 DOUBLE"
					+", cell_69 DOUBLE"
					+", cell_70 DOUBLE"
					+", cell_71 DOUBLE"
					+", cell_72 DOUBLE"
					+", cell_73 DOUBLE"
					+", cell_74 DOUBLE"
					+", cell_75 DOUBLE"
					+", cell_76 DOUBLE"
					+", cell_77 DOUBLE"
					+", cell_78 DOUBLE"
					+", cell_79 DOUBLE"
					+", cell_80 DOUBLE"
					+", cell_81 DOUBLE"
					+", cell_82 DOUBLE"
					+", cell_83 DOUBLE"
					+", cell_84 DOUBLE"
					+", cell_85 DOUBLE"
					+", cell_86 DOUBLE"
					+", cell_87 DOUBLE"
					+", cell_88 DOUBLE"
					+", cell_89 DOUBLE"
					+", cell_90 DOUBLE"
					+", cell_91 DOUBLE"
					+", cell_92 DOUBLE"
					+", cell_93 DOUBLE"
					+", cell_94 DOUBLE"
					+", cell_95 DOUBLE"
					+", cell_96 DOUBLE"
					+", cell_97 DOUBLE"
					+", cell_98 DOUBLE"
					+", cell_99 DOUBLE"
					+", cell_100 DOUBLE"
					+", cell_101 DOUBLE"
					+", cell_102 DOUBLE"
					+", cell_103 DOUBLE"
					+", cell_104 DOUBLE"
					+", cell_105 DOUBLE"
					+", cell_106 DOUBLE"
					+", cell_107 DOUBLE"
					+", cell_108 DOUBLE"
					+", cell_109 DOUBLE"
					+", cell_110 DOUBLE"
					+", cell_111 DOUBLE"
					+", cell_112 DOUBLE"
					+", cell_113 DOUBLE"
					+", cell_114 DOUBLE"
					+", cell_115 DOUBLE"
					+", cell_116 DOUBLE"
					+", cell_117 DOUBLE"
					+", cell_118 DOUBLE"
					+", cell_119 DOUBLE"
					+", cell_120 DOUBLE"
					+", cell_121 DOUBLE"
					+", cell_122 DOUBLE"
					+", cell_123 DOUBLE"
					+", cell_124 DOUBLE"
					+", cell_125 DOUBLE"
					+", cell_126 DOUBLE"
					+", cell_127 DOUBLE"
					+", cell_128 DOUBLE"
					+", cell_129 DOUBLE"
					+", cell_130 DOUBLE"
					+", cell_131 DOUBLE"
					+", cell_132 DOUBLE"
					+", cell_133 DOUBLE"
					+", cell_134 DOUBLE"
					+", cell_135 DOUBLE"
					+", cell_136 DOUBLE"
					+", cell_137 DOUBLE"
					+", cell_138 DOUBLE"
					+", cell_139 DOUBLE"
					+", cell_140 DOUBLE"
					+", cell_141 DOUBLE"
					+", cell_142 DOUBLE"
					+", cell_143 DOUBLE"
					+", cell_144 DOUBLE"
					+", cell_145 DOUBLE"
					+", cell_146 DOUBLE"
					+", cell_147 DOUBLE"
					+", cell_148 DOUBLE"
					+", cell_149 DOUBLE"
					+", cell_150 DOUBLE"
					+", cell_151 DOUBLE"
					+", cell_152 DOUBLE"
					+", cell_153 DOUBLE"
					+", cell_154 DOUBLE"
					+", cell_155 DOUBLE"
					+", cell_156 DOUBLE"
					+", cell_157 DOUBLE"
					+", cell_158 DOUBLE"
					+", cell_159 DOUBLE"
					+", cell_160 DOUBLE"
					+", cell_161 DOUBLE"
					+", cell_162 DOUBLE"
					+", cell_163 DOUBLE"
					+", cell_164 DOUBLE"
					+", cell_165 DOUBLE"
					+", cell_166 DOUBLE"
					+", cell_167 DOUBLE"
					+", cell_168 DOUBLE"
					+", cell_169 DOUBLE"
					+", cell_170 DOUBLE"
					+", cell_171 DOUBLE"
					+", cell_172 DOUBLE"
					+", cell_173 DOUBLE"
					+", cell_174 DOUBLE"
					+", cell_175 DOUBLE"
					+", cell_176 DOUBLE"
					+", cell_177 DOUBLE"
					+", cell_178 DOUBLE"
					+", cell_179 DOUBLE"
					+", cell_180 DOUBLE"
					+", cell_181 DOUBLE"
					+", cell_182 DOUBLE"
					+", cell_183 DOUBLE"
					+", cell_184 DOUBLE"
					+", cell_185 DOUBLE"
					+", cell_186 DOUBLE"
					+", cell_187 DOUBLE"
					+", cell_188 DOUBLE"
					+", cell_189 DOUBLE"
					+", cell_190 DOUBLE"
					+", cell_191 DOUBLE"
					+", cell_192 DOUBLE"
					+", cell_193 DOUBLE"
					+", cell_194 DOUBLE"
					+", cell_195 DOUBLE"
					+", cell_196 DOUBLE"
					+", cell_197 DOUBLE"
					+", cell_198 DOUBLE"
					+", cell_199 DOUBLE"
					+", cell_200 DOUBLE"
					+", cell_201 DOUBLE"
					+", cell_202 DOUBLE"
					+", cell_203 DOUBLE"
					+", cell_204 DOUBLE"
					+", cell_205 DOUBLE"
					+", cell_206 DOUBLE"
					+", cell_207 DOUBLE"
					+", cell_208 DOUBLE"
					+", cell_209 DOUBLE"
					+", cell_210 DOUBLE"
					+", cell_211 DOUBLE"
					+", cell_212 DOUBLE"
					+", cell_213 DOUBLE"
					+", cell_214 DOUBLE"
					+", cell_215 DOUBLE"
					+", cell_216 DOUBLE"
					+", cell_217 DOUBLE"
					+", cell_218 DOUBLE"
					+", cell_219 DOUBLE"
					+", cell_220 DOUBLE"
					+", cell_221 DOUBLE"
					+", cell_222 DOUBLE"
					+", cell_223 DOUBLE"
					+", cell_224 DOUBLE"
					+", cell_225 DOUBLE"
					+", cell_226 DOUBLE"
					+", cell_227 DOUBLE"
					+", cell_228 DOUBLE"
					+", cell_229 DOUBLE"
					+", cell_230 DOUBLE"
					+", cell_231 DOUBLE"
					+", cell_232 DOUBLE"
					+", cell_233 DOUBLE"
					+", cell_234 DOUBLE"
					+", cell_235 DOUBLE"
					+", cell_236 DOUBLE"
					+", cell_237 DOUBLE"
					+", cell_238 DOUBLE"
					+", cell_239 DOUBLE"
					+", cell_240 DOUBLE"
					+", cell_241 DOUBLE"
					+", cell_242 DOUBLE"
					+", cell_243 DOUBLE"
					+", cell_244 DOUBLE"
					+", cell_245 DOUBLE"
					+", cell_246 DOUBLE"
					+", cell_247 DOUBLE"
					+", cell_248 DOUBLE"
					+", cell_249 DOUBLE"
					+", cell_250 DOUBLE"
					+", cell_251 DOUBLE"
					+", cell_252 DOUBLE"
					+", cell_253 DOUBLE"
					+", cell_254 DOUBLE"
					+ ");";
			mcDatabase.execSQL(sql);
			sql = "CREATE TABLE " + msMappingTable + " ("
					+ "ID INTEGER PRIMARY KEY" 
					+ ", col_name TEXT"
					+ ", ap_cell TEXT" + ");";
			mcDatabase.execSQL(sql);
			mcDatabase.setTransactionSuccessful();
		} finally {
			mcDatabase.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase mcDatabase, int miOldVersion, int miNewVersion) {
		String sql = "DROP TABLE IF EXISTS " + msDataTable;
		mcDatabase.execSQL(sql);
		sql = "DROP TABLE IF EXISTS " + msMappingTable;
		mcDatabase.execSQL(sql);
		onCreate(mcDatabase);
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		if (mcDatabase != null && mcDatabase.isOpen() && !mcDatabase.isReadOnly()) {
			return mcDatabase;
		}
		if (mbIsInitializing) {
			throw new IllegalStateException("getWritableDatabase called recursively");
		}
		
		boolean mbSuccess = false;
		SQLiteDatabase mcSQLiteDatabase = null;
		try {
			mbIsInitializing = true;
			if (this.msDatabaseName == null) {
				mcSQLiteDatabase = SQLiteDatabase.create(null);
			} else {
				File mcRootPath = Environment.getExternalStorageDirectory();
				mcDatabaseFile = new File(mcRootPath.getParent() + "/" + mcRootPath.getName() + "/mobileInfo/" + msDatabaseName);
				mcSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(mcDatabaseFile, null);
			}

			int miVersion = mcSQLiteDatabase.getVersion();
			if (miVersion != miDatabaseVersion) {
				mcSQLiteDatabase.beginTransaction();
				try {
					if (miVersion == 0) {
						onCreate(mcSQLiteDatabase);
					} else {
						onUpgrade(mcSQLiteDatabase, miVersion, miDatabaseVersion);
					}
					mcSQLiteDatabase.setVersion(miDatabaseVersion);
					mcSQLiteDatabase.setTransactionSuccessful();
				} finally {
					mcSQLiteDatabase.endTransaction();
				}
			}

			onOpen(mcSQLiteDatabase);
			mbSuccess = true;
			return mcSQLiteDatabase;
		} finally {
			mbIsInitializing = false;
			if (mbSuccess) {
				if (mcDatabase != null) {
					try {
						mcDatabase.close();
					} catch (Exception e) {}
				}
				mcDatabase = mcSQLiteDatabase;
			} else {
				if (mcSQLiteDatabase != null)
					mcSQLiteDatabase.close();
			}
		}
	}
	
	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
        if (mcDatabase != null && mcDatabase.isOpen()) {
            return mcDatabase;
        }
        if (mbIsInitializing) {
            throw new IllegalStateException("getReadableDatabase called recursively");
        }
        try {
            return getWritableDatabase();
        } catch (Exception e) {}
 
        SQLiteDatabase mcSQLiteDatabase = null;
        try {
            mbIsInitializing = true;
            File mcRootPath = Environment.getExternalStorageDirectory();
			mcDatabaseFile = new File(mcRootPath.getParent() + "/" + mcRootPath.getName() + "/mobileInfo/" + msDatabaseName);
			mcSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(mcDatabaseFile, null);
            onOpen(mcSQLiteDatabase);
            mcDatabase = mcSQLiteDatabase;
            return mcDatabase;
        } finally {
            mbIsInitializing = false;
            if (mcSQLiteDatabase != null && mcSQLiteDatabase != mcDatabase)
            	mcSQLiteDatabase.close();
        }
    }

	public Cursor select(String msSQL) {
		SQLiteDatabase mcDatabase = this.getReadableDatabase();
		Cursor cursor = mcDatabase.rawQuery(msSQL, null);
		return cursor;
	}

	public void delete(String msSQL) {
		SQLiteDatabase mcDatabase = this.getWritableDatabase();
		mcDatabase.execSQL(msSQL);
	}

	public void insert(String msSQL) {
		SQLiteDatabase mcDatabase = this.getWritableDatabase();
		mcDatabase.execSQL(msSQL);
	}

	public void update(String msSQL) {
		SQLiteDatabase mcDatabase = this.getWritableDatabase();
		mcDatabase.execSQL(msSQL);
	}
}
