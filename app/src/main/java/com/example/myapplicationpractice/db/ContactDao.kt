package com.example.myapplicationpractice.db

import android.content.ContentValues
import android.content.Context
import com.example.myapplicationpractice.models.Contact
import com.example.myapplicationpractice.models.ContactGroup

/**
 * Data Access Object for emergency_contacts and contact_groups tables.
 * Implements full CRUD (F3) and dynamic SQL queries with LIKE / ORDER BY (F5).
 *
 * All public methods are synchronous — callers must invoke them
 * from a background thread (Dispatchers.IO) to satisfy the Global Constraint.
 */
class ContactDao(context: Context) {

    private val dbHelper = LastCallDbHelper(context)

    // ══════════════════════════════════════════════════════
    //  GROUPS — Read / Insert
    // ══════════════════════════════════════════════════════

    /** Return all contact groups, ordered by id. */
    fun getAllGroups(): List<ContactGroup> {
        val list = mutableListOf<ContactGroup>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${LastCallDbHelper.TABLE_GROUPS} ORDER BY ${LastCallDbHelper.COL_GROUP_ID}",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    ContactGroup(
                        id = it.getLong(it.getColumnIndexOrThrow(LastCallDbHelper.COL_GROUP_ID)),
                        groupName = it.getString(it.getColumnIndexOrThrow(LastCallDbHelper.COL_GROUP_NAME)),
                        description = it.getString(it.getColumnIndexOrThrow(LastCallDbHelper.COL_GROUP_DESC)) ?: "",
                        createdAt = it.getString(it.getColumnIndexOrThrow(LastCallDbHelper.COL_GROUP_CREATED)) ?: ""
                    )
                )
            }
        }
        return list
    }

    /** Insert a new group and return its row id. */
    fun insertGroup(group: ContactGroup): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(LastCallDbHelper.COL_GROUP_NAME, group.groupName)
            put(LastCallDbHelper.COL_GROUP_DESC, group.description)
        }
        return db.insert(LastCallDbHelper.TABLE_GROUPS, null, cv)
    }

    // ══════════════════════════════════════════════════════
    //  CONTACTS — Full CRUD  (F3)
    // ══════════════════════════════════════════════════════

    /** CREATE — Insert a new contact. Returns the new row id. */
    fun insertContact(contact: Contact): Long {
        val db = dbHelper.writableDatabase
        val cv = contactToContentValues(contact)
        return db.insert(LastCallDbHelper.TABLE_CONTACTS, null, cv)
    }

    /** READ — Get all contacts joined with their group name. */
    fun getAllContacts(): List<Contact> {
        return queryContacts(selection = null, selectionArgs = null, orderBy = "${LastCallDbHelper.COL_CONTACT_PRIORITY} ASC")
    }

    /** READ — Get a single contact by id. */
    fun getContactById(id: Long): Contact? {
        val list = queryContacts(
            selection = "c.${LastCallDbHelper.COL_CONTACT_ID} = ?",
            selectionArgs = arrayOf(id.toString())
        )
        return list.firstOrNull()
    }

    /** READ — Get contacts belonging to a specific group. */
    fun getContactsByGroup(groupId: Long): List<Contact> {
        return queryContacts(
            selection = "c.${LastCallDbHelper.COL_CONTACT_GROUP_ID} = ?",
            selectionArgs = arrayOf(groupId.toString())
        )
    }

    /** UPDATE — Update an existing contact. Returns rows affected. */
    fun updateContact(contact: Contact): Int {
        val db = dbHelper.writableDatabase
        val cv = contactToContentValues(contact)
        return db.update(
            LastCallDbHelper.TABLE_CONTACTS,
            cv,
            "${LastCallDbHelper.COL_CONTACT_ID} = ?",
            arrayOf(contact.id.toString())
        )
    }

    /** DELETE — Remove a contact by id. Returns rows deleted. */
    fun deleteContact(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            LastCallDbHelper.TABLE_CONTACTS,
            "${LastCallDbHelper.COL_CONTACT_ID} = ?",
            arrayOf(id.toString())
        )
    }

    // ══════════════════════════════════════════════════════
    //  DYNAMIC SQL QUERIES  (F5)
    // ══════════════════════════════════════════════════════

    /**
     * Search contacts by name or relationship using SQL LIKE (F5).
     */
    fun searchContacts(query: String): List<Contact> {
        val like = "%$query%"
        return queryContacts(
            selection = "c.${LastCallDbHelper.COL_CONTACT_NAME} LIKE ? OR c.${LastCallDbHelper.COL_CONTACT_RELATIONSHIP} LIKE ?",
            selectionArgs = arrayOf(like, like)
        )
    }

    /**
     * Get contacts sorted by a given column using ORDER BY (F5).
     * @param orderBy e.g. "name ASC", "priority ASC", "created_at DESC"
     */
    fun getContactsSorted(orderBy: String): List<Contact> {
        return queryContacts(selection = null, selectionArgs = null, orderBy = orderBy)
    }

    // ══════════════════════════════════════════════════════
    //  PRIVATE HELPERS
    // ══════════════════════════════════════════════════════

    /**
     * Central query method that JOINs emergency_contacts with contact_groups.
     */
    private fun queryContacts(
        selection: String?,
        selectionArgs: Array<String>? = null,
        orderBy: String = "${LastCallDbHelper.COL_CONTACT_PRIORITY} ASC"
    ): List<Contact> {
        val list = mutableListOf<Contact>()
        val db = dbHelper.readableDatabase

        val sql = buildString {
            append("SELECT c.*, g.${LastCallDbHelper.COL_GROUP_NAME} AS group_name_label ")
            append("FROM ${LastCallDbHelper.TABLE_CONTACTS} c ")
            append("LEFT JOIN ${LastCallDbHelper.TABLE_GROUPS} g ")
            append("ON c.${LastCallDbHelper.COL_CONTACT_GROUP_ID} = g.${LastCallDbHelper.COL_GROUP_ID}")
            if (!selection.isNullOrBlank()) {
                append(" WHERE $selection")
            }
            append(" ORDER BY $orderBy")
        }

        val cursor = db.rawQuery(sql, selectionArgs)
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    Contact(
                        id = it.getLong(it.getColumnIndexOrThrow(LastCallDbHelper.COL_CONTACT_ID)),
                        name = it.getString(it.getColumnIndexOrThrow(LastCallDbHelper.COL_CONTACT_NAME)),
                        relationship = it.getString(it.getColumnIndexOrThrow(LastCallDbHelper.COL_CONTACT_RELATIONSHIP)) ?: "",
                        phone = it.getString(it.getColumnIndexOrThrow(LastCallDbHelper.COL_CONTACT_PHONE)),
                        priority = it.getInt(it.getColumnIndexOrThrow(LastCallDbHelper.COL_CONTACT_PRIORITY)),
                        canAccessVault = it.getInt(it.getColumnIndexOrThrow(LastCallDbHelper.COL_CONTACT_VAULT)) == 1,
                        notifyOnCheckIn = it.getInt(it.getColumnIndexOrThrow(LastCallDbHelper.COL_CONTACT_NOTIFY_CHECKIN)) == 1,
                        notifyOnSos = it.getInt(it.getColumnIndexOrThrow(LastCallDbHelper.COL_CONTACT_NOTIFY_SOS)) == 1,
                        groupId = it.getLong(it.getColumnIndexOrThrow(LastCallDbHelper.COL_CONTACT_GROUP_ID)),
                        groupName = it.getString(it.getColumnIndexOrThrow("group_name_label")) ?: ""
                    )
                )
            }
        }
        return list
    }

    /** Convert a Contact to ContentValues for INSERT / UPDATE. */
    private fun contactToContentValues(contact: Contact): ContentValues {
        return ContentValues().apply {
            put(LastCallDbHelper.COL_CONTACT_NAME, contact.name)
            put(LastCallDbHelper.COL_CONTACT_RELATIONSHIP, contact.relationship)
            put(LastCallDbHelper.COL_CONTACT_PHONE, contact.phone)
            put(LastCallDbHelper.COL_CONTACT_PRIORITY, contact.priority)
            put(LastCallDbHelper.COL_CONTACT_VAULT, if (contact.canAccessVault) 1 else 0)
            put(LastCallDbHelper.COL_CONTACT_NOTIFY_CHECKIN, if (contact.notifyOnCheckIn) 1 else 0)
            put(LastCallDbHelper.COL_CONTACT_NOTIFY_SOS, if (contact.notifyOnSos) 1 else 0)
            put(LastCallDbHelper.COL_CONTACT_GROUP_ID, contact.groupId)
        }
    }
}
