package net.schnellp.mycapnutrition.Model;

import android.provider.BaseColumns;

public final class DBContract {
    private DBContract() {}

    public static final String _ID = "id";
    public static final String _ACTIVE = "active";

    public static abstract class PackageEntry implements BaseColumns {
        public static final String TABLE_NAME = "Package";
        public static final String _ID = DBContract._ID;
        public static final String _ACTIVE = DBContract._ACTIVE;
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

    public static abstract class FoodEntry implements BaseColumns {
        public static final String TABLE_NAME = "Food";
        public static final String _ID = DBContract._ID;
        public static final String _ACTIVE = DBContract._ACTIVE;
        public static final String COLUMN_NAME_PACKAGE = "package";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_REF_SERVING_MG = "reference_serving_mg";
        public static final String COLUMN_NAME_KCAL = "kcal";
        public static final String COLUMN_NAME_CARB_MG = "carb_mg";
        public static final String COLUMN_NAME_FAT_MG = "fat_mg";
        public static final String COLUMN_NAME_PROTEIN_MG = "protein_mg";
        public static final String COLUMN_NAME_LAST_USED = "last_used";
    }

    public static abstract class UnitEntry implements BaseColumns {
        public static final String TABLE_NAME = "Unit";
        public static final String _ID = DBContract._ID;
        public static final String _ACTIVE = DBContract._ACTIVE;
        public static final String COLUMN_NAME_FOOD_ID = "food_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_AMOUNT_MG = "amount_mg";
    }

    public static abstract class RecipeFoodEntry implements BaseColumns {
        public static final String TABLE_NAME = "RecipeFood";
        public static final String _ID = DBContract._ID;
        public static final String COLUMN_NAME_RECIPE_ID = "recipe_id";
        public static final String COLUMN_NAME_FOOD_ID = "food_id";
        public static final String COLUMN_NAME_UNIT_ID = "unit_id";
        public static final String COLUMN_NAME_FOOD_QUANTITY_CENTS = "food_quantity_cents";
    }

    public static abstract class RecordEntry implements BaseColumns {
        public static final String TABLE_NAME = "Record";
        public static final String _ID = DBContract._ID;
        public static final String _ACTIVE = DBContract._ACTIVE;
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_FOOD_ID = "food_id";
        public static final String COLUMN_NAME_UNIT_ID = "unit_id";
        public static final String COLUMN_NAME_QUANTITY_CENTS = "quantity_cents";
    }
}
