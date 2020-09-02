SELECT COUNT(item) FROM grp JOIN storeinout ON grp.date = storeinout.serial 
WHERE name = 'chicken' AND date = 20200820 AND state = 'breakfast'

no of groups
SELECT COUNT(name) FROM grp WHERE date = 20200820 AND state = 'breakfast'
SELECT name FROM grp WHERE date = 20200820 AND state = 'breakfast'


SELECT COUNT(nonstoreditem.name) FROM grp JOIN nonstoreditem ON grp.date = nonstoreditem.serial
WHERE grp.name = 'chicken' AND grp.date = 20200820 AND grp.state = 'breakfast'


SELECT G.name, G.state, G.serial, S.serial, COUNT(S.item) FROM grp G JOIN storeinout S 
WHERE G.serial = S.bfgrp
AND G.date = S.serial
AND G.state = 'breakfast'
GROUP BY G.name = 'chicken'


SELECT grp.name, date, COUNT(item), COUNT(DISTINCT nonstoreditem.name)  FROM grp 
JOIN storeinout ON grp.date = storeinout.serial 
JOIN nonstoreditem ON grp.date = nonstoreditem.serial 
AND grp.state = nonstoreditem.state
WHERE grp.state = 'breakfast' 
AND bfgrp = grp.serial 
AND nonstoreditem.grp = grp.serial
GROUP BY grp.name, date 
ORDER BY date


SELECT grp.name, date, COUNT(item), COUNT(DISTINCT nonstoreditem.name)  FROM grp 
JOIN storeinout ON grp.date = storeinout.serial 
JOIN nonstoreditem ON grp.date = nonstoreditem.serial 
AND grp.state = nonstoreditem.state
WHERE grp.state = 'lunch' 
AND lunchgrp = grp.serial 
AND nonstoreditem.grp = grp.serial
GROUP BY grp.name, date 
ORDER BY date.


SELECT date, COUNT(DISTINCT nonstoreditem.name) FROM grp JOIN nonstoreditem ON grp.date = nonstoreditem.serial AND grp.state = nonstoreditem.state WHERE grp.date = 20200819 AND grp.state = 'breakfast' AND nonstoreditem.grp = 1

SELECT item, bf, unit FROM storeinout sio JOIN storeditem si ON sio.item = si.name WHERE sio.serial = 20200820 and bfgrp = 2

SELECT name, amount, unit FROM nonstoreditem nsi JOIN nonstoreditemlist nsil USING(name) WHERE nsi.serial = 20200819 and grp = 1





