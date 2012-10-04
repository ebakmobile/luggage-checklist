
CREATE TABLE IF NOT EXISTS Trip(
	trip_id INTEGER PRIMARY KEY AUTOINCREMENT,
	trip_name TEXT NOT NULL);
	
CREATE TABLE IF NOT EXISTS Suitcase(
	suitcase_id INTEGER PRIMARY KEY AUTOINCREMENT,
	suitcase_name TEXT(100) NOT NULL,
	trip_id INTEGER REFERENCES Trip (trip_id),
	UNIQUE (suitcase_name, trip_id));

CREATE TABLE IF NOT EXISTS Item(
	item_id INTEGER PRIMARY KEY AUTOINCREMENT,
	item_name TEXT NOT NULL,
	quantity INTEGER NOT NULL,
	suitcase_id INTEGER REFERENCES Suitcase(suitcase_id),
	UNIQUE (item_name, suitcase_id)) ;
	
CREATE TABLE IF NOT EXISTS Read(
	read TEXT PRIMARY KEY);