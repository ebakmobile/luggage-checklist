HOW TO USE THE DATABASE FOLDER
Instead of hard-coding SQL statements into our Java classes, we will write our statements into .sql files. These files can contain one or more SQL statements. The only syntax that must be followed is that all statements must end with ONE AND ONLY ONE semicolon character(;). This will allow our Java code to parse the contents of the entire file into multiple String statements.

Place all database creation files into the /database/create directory.
	database upgrade/migration files into the /database/upgrade directory.
	database downgrade/rollback files into the /database/downgrade directory.
	
Although I would have preferred to use a floating point number instead, we have to use integers as our version numbers for database files. The initial version will be 1. Our file names will be as follows:
- Create scripts: schema-X.sql. All create scripts must be able to create a database that conforms to the latest version, notwithstanding any other changes that we made in previous upgrades. Hence, we must always have an up-to-date create script.
- Upgrade scripts: upgrade-X.sql
- Downgrade: downgrade-X.sql
Where X is the version number.

Generally, when we release a version of the app, we will have a db.properties file in the database directory. The property "current.version" will contain the current version of the database. When the user turns on the app, one of the following two scenarios will happen:
- If this is the first time the user is ever using our app, the create script matching the current.version will execute.
- If this is not the first time the user has ever used the app, we will check if there was a database upgrade/downgrade since the last time the app was booted. Then, all upgrade/downgrade scripts from the old version until the latest version will execute.

FUTURE CONSIDERATION: What happens if an app update requries both database downgrades and upgrades? Hopefully, we can avoid this situation and never require production versions of our app to need a downgrade. Downgrade should only be for development purposes. 