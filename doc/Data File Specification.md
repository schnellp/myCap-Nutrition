# Data file format (.mccsv) specification

myCap data files (.mccsv) are plain text files using a csv-like syntax.
These files are used for data import and export functions, and are designed so that data from csv files may be easily extracted and inserted.

myCap data files are made up of the following *tables*, in order:

  1. Package
  2. Food
  3. Unit
  4. Ingredient
  5. Record
  6. BodyMass

Tables must appear in the above sequence.
This constraint ensures that when a row in table B refers to by key to a row in table A, the relevant row in table A has already been processed.

A table is started by a row containing only the table name (e.g., Record), followed by a row containing comma-separated column names.
Data rows are comma-separated values, with double quotes enclosing string entries.
To include double quotes in a string, replace the double quote with two double quotes.
Newlines in strings are not supported.
Do not insert spaces after commas.
A table is ended by an empty line.
Example:

    Record
    id,active,date,time,food_id,unit_id,quantity_cents
    2,1,"2016-06-02","18:30",1,1,1000
    
    BodyMass
    id,active,date,time,mass_g
    1,1,"2016-06-02","18:30",70000

## Package

The Package table contains the following columns:

  - id (integer): A unique (within file) id for the package.
  - active (integer): Visible in standard queries.
  - name (string): Package name.
  - description (string): Package description.

## Food

The Food table contains the following columns:

  - id (integer): A unique (within file) id for the food.
  - active (integer): Should the food be visible in the app? (1 = yes, 0 = no). This field provides pseudo-delete functionality.
  - package (integer): References a row in the Package table by id.
  - type (integer): Simple food = 0, recipe = 1.
  - name (string): The name of the food.
  - notes (string): Not used.
  - recipe_servings (integer): Number of servings made by sum of all ingredients.
  - reference_serving_mg (integer): Serving, in mg, for which nutritional information is specified.
  - kcal (integer): Calories (kcal) per reference serving.
  - carb_mg (integer): Carbohydrates, in mg, per reference serving.
  - fat_mg (integer): Fat, in mg, per reference serving.
  - protein_mg (integer): Protein, in mg, per reference serving.
  - last_used (integer): Time (in seconds since Epoch) of last creation of a record referencing this food directly (not as an ingredient).

## Unit

The Unit table contains the following columns:

  - id (integer): A unique (within file) id for the unit.
  - active (integer): Should the unit be visible in the app? (1 = yes, 0 = no). This field provides pseudo-delete functionality.
  - food_id (integer): References a row in the Food table by id.
  - name (string): the name of the unit.
  - amount_mg (integer): The mass, in mg, of the food referenced by food_id which corresponds to a single unit.

## Ingredient

The Ingredient table contains the following columns:

  - id (integer): A unique (within file) id for the unit.
  - active (integer): Visible in standard queries.
  - recipe_id (integer): References a row in the Food table by id. The recipe for which this row is an ingredient.
  - food_id (integer): References a row in the Food table by id. The food which comprises this ingredient.
  - unit_id (integer): References a row in the Unit table by id. Unit in which the amount of food in this ingredient is specified.
  - quantity_cents (integer): Amount, in hundredths of specified unit, of the specified food comprising this ingredient.

## Record

The Record table contains the following columns:

  - id (integer): A unique (within file) id for the record.
  - active (integer): Should the record be visible in the app? (1 = yes, 0 = no). This field provides pseudo-delete functionality.
  - date (string): The date of the record, in format yyyy-MM-dd. Ex: 2016-06-04 refers to June 4, 2016.
  - time (string): The time of the record, in format HH:mm. Ex: 18:30 refers to 6:30 pm.
  - food_id (integer): References a row in the Food table by id.
  - unit_id (integer): References a row in the Unit table by id. If null, unit is gram.
  - quantity_cents (integer): Number (in hundredths) of units of food.

## BodyMass

The BodyMass table contains the following columns:

  - id (integer): A unique (within file) id for the record.
  - active (integer): Visible in standard queries.
  - date (string): The date of the record, in format yyyy-MM-dd. Ex: 2016-06-04 refers to June 4, 2016.
  - time (string): The time of the record, in format HH:mm. Ex: 18:30 refers to 6:30 pm.
  - mass_g (integer): Bodyweight, in g.
  