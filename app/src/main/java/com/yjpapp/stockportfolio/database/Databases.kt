package com.yjpapp.stockportfolio.database

class Databases {
    companion object CreateDB{
        /**
         * table name
         */
        private const val TABLE_DATA: String = "data_table"

        /**
         * proceed_list_data_table columns
         */
        internal const val COL_ID: String = "id"
        internal const val COL_DATA_PROCEED: String = "proceed"
        internal const val CREATE_DATA_TABLE: String =
            "create table " + TABLE_DATA + "(" +
                    COL_ID + " integer primary key autoincrement, "+
                    COL_DATA_PROCEED

    }
}