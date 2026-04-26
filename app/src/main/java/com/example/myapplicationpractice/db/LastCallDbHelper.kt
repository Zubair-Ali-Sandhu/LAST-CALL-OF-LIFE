package com.example.myapplicationpractice.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * SQLiteOpenHelper for the Last Call of Life database (F2).
 *
 * Schema:
 *   contact_groups   — PK _id AUTOINCREMENT
 *   emergency_contacts — PK _id AUTOINCREMENT, FK group_id → contact_groups._id
 *
 * Seeds 4 default groups in onCreate().
 */
class LastCallDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "last_call.db"
        const val DATABASE_VERSION = 1

        // ── contact_groups table ─────────────────────────
        const val TABLE_GROUPS = "contact_groups"
        const val COL_GROUP_ID = "_id"
        const val COL_GROUP_NAME = "group_name"
        const val COL_GROUP_DESC = "description"
        const val COL_GROUP_CREATED = "created_at"

        // ── emergency_contacts table ─────────────────────
        const val TABLE_CONTACTS = "emergency_contacts"
        const val COL_CONTACT_ID = "_id"
        const val COL_CONTACT_NAME = "name"
        const val COL_CONTACT_RELATIONSHIP = "relationship"
        const val COL_CONTACT_PHONE = "phone"
        const val COL_CONTACT_PRIORITY = "priority"
        const val COL_CONTACT_VAULT = "can_access_vault"
        const val COL_CONTACT_NOTIFY_CHECKIN = "notify_on_checkin"
        const val COL_CONTACT_NOTIFY_SOS = "notify_on_sos"
        const val COL_CONTACT_GROUP_ID = "group_id"
        const val COL_CONTACT_CREATED = "created_at"

        private const val CREATE_TABLE_GROUPS = """
            CREATE TABLE $TABLE_GROUPS (
                $COL_GROUP_ID       INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_GROUP_NAME     TEXT    NOT NULL,
                $COL_GROUP_DESC     TEXT,
                $COL_GROUP_CREATED  TEXT    DEFAULT (datetime('now'))
            )
        """

        private const val CREATE_TABLE_CONTACTS = """
            CREATE TABLE $TABLE_CONTACTS (
                $COL_CONTACT_ID             INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CONTACT_NAME           TEXT    NOT NULL,
                $COL_CONTACT_RELATIONSHIP   TEXT,
                $COL_CONTACT_PHONE          TEXT    NOT NULL,
                $COL_CONTACT_PRIORITY       INTEGER DEFAULT 1,
                $COL_CONTACT_VAULT          INTEGER DEFAULT 0,
                $COL_CONTACT_NOTIFY_CHECKIN  INTEGER DEFAULT 0,
                $COL_CONTACT_NOTIFY_SOS     INTEGER DEFAULT 1,
                $COL_CONTACT_GROUP_ID       INTEGER NOT NULL DEFAULT 1,
                $COL_CONTACT_CREATED        TEXT    DEFAULT (datetime('now')),
                FOREIGN KEY ($COL_CONTACT_GROUP_ID)
                    REFERENCES $TABLE_GROUPS($COL_GROUP_ID)
                    ON DELETE CASCADE
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create tables
        db.execSQL(CREATE_TABLE_GROUPS)
        db.execSQL(CREATE_TABLE_CONTACTS)

        // Seed default groups
        seedDefaultGroups(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GROUPS")
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        // Enable foreign key enforcement
        db.execSQL("PRAGMA foreign_keys = ON")
    }

    /**
     * Insert 4 default contact groups so the FK always has valid targets.
     */
    private fun seedDefaultGroups(db: SQLiteDatabase) {
        val defaults = listOf(
            "Family" to "Immediate family members",
            "Legal" to "Lawyers, executors, and legal advisors",
            "Medical" to "Doctors, therapists, and medical contacts",
            "Friends" to "Trusted friends and colleagues"
        )
        for ((name, desc) in defaults) {
            val cv = ContentValues().apply {
                put(COL_GROUP_NAME, name)
                put(COL_GROUP_DESC, desc)
            }
            db.insert(TABLE_GROUPS, null, cv)
        }
    }
}
