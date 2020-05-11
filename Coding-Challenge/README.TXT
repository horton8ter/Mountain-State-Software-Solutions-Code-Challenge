------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all he/she needs to know. The comments should usually include at least:
------------------------------------------------------------------------

PROJECT TITLE: Mountain State Software Solutions Coding Challenge

PURPOSE OF PROJECT: Process Csv File input into a database with 10 columns, discard the entries that have less than or 
 greater than 10 fields into a file called <filename>-bad.csv, record the number of valid formatted db entries, the number of invalid entries, and total number of entries to a log file

 
VERSION or DATE: 5/10/2020
HOW TO START THIS PROJECT: place the sqlite folder in c:/ directory.
open bluej. In Tools>>Preferences select library then add C:\sqlite\java\connect\sqlite-jdbc-3.30.1.jar


AUTHORS: Paul Edwards


USER INSTRUCTIONS:

Open the Coding-Challenge project in bluej.

Right click on the csvChallenge class and click on void main(String[] args)


Methods of csvChallenge

[createNewTable]
Checks to see if the table already exists, if it doesn't creates the table with 10 text fields A-J in the sqlite/db/ folder


[csvProcess]
Given the filename and timeID input, reads from the file in sqlite folder line by line. The read line is split on commas, but ignores any characters between commas and keeps trailing empty strings
Then iterates through the string array to check for empty strings or length>10 (which would signify more than 10 entry fields).
if it finds either of those cases are true, the data is passed into the [csvWriter] method for the bad data. Otherwise it is passed to the [databaseWriter] method 
after all the lines have been read, a call to [logWriter] is made to store the values corresponding to #total entries,good entries, and bad entriesl

[databaseWriter]

given filename and String Data array, makes up the insert query to place the data into the filename database using prepared statement and a for loop to construct the query

[csvWriter]
given the filename and string data array, iterates through the data array while appending the values together, seperated by commas
the last data entry of each array is followed by a newline character for formatting

[logWriter]
given the filename and 3 integer values, writes the 3 values into a log file
each value is followed by a return and newline for formatting