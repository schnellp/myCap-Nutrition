# Data file format (.mccsv) specification

MyCap data files (.mccsv) are plain text files using a csv-like syntax.
These files are used for data import and export functions, and are designed so that data from csv files may be easily extracted and inserted.

MyCap data files are made up of one to three *tables*, in order:

  1. Food
  2. Unit
  3. Record

Tables must appear in the above sequence, and if one table is excluded, all subsequent tables must also be excluded.
This constraint ensures that when a row in table B refers to by key to a row in table A, the relevant row in table A has already been processed.

A table is started by a row containing only the table name (e.g., Record), followed by a row containing comma-separated column names.
Data rows are comma-separated values, with double quotes enclosing string entries.
To include double quotes in a string, replace the double quote with two double quotes.
Newlines in strings are not supported.
Do not insert spaces after commas.
A table is ended by an empty line.
Example:

    Record
    id,active,date,food_id,unit_id,quantity_cents
    2,0,"2016-06-02",1,1,1000

## Food

The Food table contains the following columns:

  - id (integer): A unique (within file) id for the food.
  - active (integer): Should the food be visible in the app? (1 = yes, 0 = no). This field provides pseudo-delete functionality.
  - name (string): The name of the food.
  - reference_serving_mg (integer): Mass, in mg, of a reference serving.
  - kcal (integer): Calories (kcal) per reference serving.
  - carb_mg (integer): Carbohydrates, in mg, per reference serving.
  - fat_mg (integer): Fat, in mg, per reference serving.
  - protein_mg (integer): Protein, in mg, per reference serving.

## Unit

The Unit table contains the following columns:

  - id (integer): A unique (within file) id for the unit.
  - active (integer): Should the unit be visible in the app? (1 = yes, 0 = no). This field provides pseudo-delete functionality.
  - food_id (integer): References a row in the Food table by id.
  - name (string): the name of the unit.
  - amount_mg (integer): The mass, in mg, of the food referenced by food_id which corresponds to a single unit.

## Record

The Record table contains the following columns:
id,active,date,food_id,unit_id,quantity_cents

  - id (integer): A unique (within file) id for the record.
  - active (integer): Should the record be visible in the app? (1 = yes, 0 = no). This field provides pseudo-delete functionality.
  - date (string): The date of the record, in format yyyy-mm-dd. Ex: 2016-06-04 refers to June 4, 2016.
  - food_id (integer): References a row in the Food table by id.
  - unit_id (integer): References a row in the Unit table by id. If null, unit is gram.
  - quantity_cents (integer): Number (in hundredths) of units of food.
