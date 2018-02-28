CREATE TABLE 'PlayerAction' (
    'Id' INTEGER PRIMARY KEY NOT NULL,
    'ActionId' NVARCHAR (30) NOT NULL,
    'CreationDate' INTEGER NOT NULL
);
CREATE TABLE 'Achievement' (
    'Id' INTEGER PRIMARY KEY NOT NULL,
    'AdventureId' INTEGER NOT NULL,
    'NameKey'  NVARCHAR (30) NOT NULL,
    'DescriptionKey' NVARCHAR (30) NOT NULL,
    'ImageName' NVARCHAR (30) NOT NULL,
    'Order' INTEGER NOT NULL,
    'Hidden' INTEGER NOT NULL,
    'Incremental' INTEGER NOT NULL,
    'Unlocked' INTEGER NOT NULL,
    'UnlockDate' INTEGER
);
CREATE TABLE 'AchievementAction' (
    'Id' INTEGER PRIMARY KEY NOT NULL,
    'ActionId' NVARCHAR (30) NOT NULL,
    'AchievementId' INTEGER NOT NULL,
    FOREIGN KEY('AchievementId') REFERENCES 'Achievement'('Id')
);
INSERT INTO 'Achievement' VALUES (
    1,
    1,
    'houdiniName',
    'houdiniDescription',
    'houdini',
    1,
    0,
    0,
    0,
    0
);
INSERT INTO 'AchievementAction' VALUES (
    1,
    'adventure.exit.jail',
    1
);