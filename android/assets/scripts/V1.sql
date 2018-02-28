CREATE TABLE 'StoredGame' (
    'Id' INTEGER PRIMARY KEY NOT NULL,
    'PlaceId' INTEGER NOT NULL,
    'CharacterJson' TEXT NOT NULL,
    'CreationDate' INTEGER NOT NULL,
    'ProgressPercentage' FLOAT NOT NULL,
    'CharacterName' NVARCHAR (20) NOT NULL,
    'CurrentLifePoints' INTEGER NOT NULL,
    'MaximumLifePoints' INTEGER NOT NULL,
    'CurrentPowerPoints' INTEGER NOT NULL,
    'MaximumPowerPoints' INTEGER NOT NULL,
    'CharacterPortrait' VARCHAR(20) NOT NULL,
    'AdventureTitleTranslated' NVARCHAR(100) NOT NULL,
    'PendingInventoryItemsCount' INTEGER NOT NULL
);
