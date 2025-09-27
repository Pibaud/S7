# Documentation des méthodes

Ce fichier README documente les méthodes qui étaient à implémenter dans l'énoncé du TP pour les classes `Vecteur`, `Vecteur2D` et `Matrice`.

Toutes ces méthodes peuvent être testées en lançant le fichier `Main.java`.

## Vecteur

- **produitScalaire(Vecteur v)**
Retourne le produit scalaire entre le vecteur appelant et le vecteur v. Une somme part de zéro puis il lui est ajouté, à chaque itération dans la dimension des vecteurs, le produit de ces derniers.

- **normeCarre()**
Retourne la norme au carré du vecteur appelant. Pour chaque terme du vecteur, on le met au carré et on ajoute ce résultat à une somme finale.

- **norme()**
Retourne la norme du vecteur appelant

---

## Vecteur2D

- **det(Vecteur2D v)**
Retourne le déterminant entre le vecteur appelant et le vecteur v. Utilise la formule det(u, v) = u.x * v.y - u.y * v.x. Même si cette méthode n'était pas demandée dans l'énoncé, elle est très utile pour les prochaines méthodes.

- **sinus()**
Retourne le sinus de l'angle formé par le vecteur appelant avec l'axe des abscisses (i). Utilise la formule qui divise le déterminant entre le vecteur appelant et i par le produit des normes des vecteurs appelant et i.

- **cosinus()**
Retourne le cosinus de l'angle formé par le vecteur appelant avec l'axe des abscisses (i). Utilise la formule qui divise le produit scalaire entre le vecteur appelant et i par le produit des normes des vecteurs appelant et i.

- **tangente()**
Retourne la tangente de l'angle formé par le vecteur appelant avec l'axe des abscisses (i). Il vérifie d'abord que le vecteur n'est pas vertical (cosinus différent de zéro) pour ne pas diviser par zéro.
Utilise la formule qui divise le sinus de l'angle par le cosinus de l'angle.

- **angle()**
Retourne la mesure de l'angle en radian entre le vecteur appelant et le vecteur i, vecteur des abscisses. Utilise la méthode Math.acos() sur le cosinus de l'angle.

- **sinus(Vecteur2D v)**
Retourne le sinus de l'angle formé par le vecteur appelant avec le vecteur v. Utilise la formule qui divise le déterminant entre le vecteur appelant et v par le produit des normes des vecteurs appelant et v.

- **cosinus(Vecteur2D v)**
Retourne le cosinus de l'angle formé par le vecteur appelant avec le vecteur v. Utilise la formule qui divise le produit scalaire entre le vecteur appelant et v par le produit des normes des vecteurs appelant et v.

- **tangente(Vecteur2D v)**
Retourne la tangente de l'angle formé par le vecteur appelant avec le vecteur v. Il vérifie d'abord que le vecteur n'est pas vertical (cosinus différent de zéro) pour ne pas diviser par zéro.
Utilise la formule qui divise le sinus de l'angle par le cosinus de l'angle, cette fois entre le vecteur appelant et v.

- **angle(Vecteur2D v)**
Retourne la mesure de l'angle en radian entre le vecteur appelant et le vecteur v. Utilise la méthode Math.acos() sur le cosinus de l'angle.

---

## Matrice

- **inverse(Matrice mat)**
Retourne l'inverse de la matrice mat en utilisant le pivot de Gauss. Si un élément diagonal est nul, la matrice n'est pas inversible et on retourne null.

Pour chaque colonne i, on cherche à rendre tous les éléments de la colonne (sauf le pivot diagonal) égaux à zéro.
Le pivot est l'élément diagonal ma.get(i, i).

Deuxième boucle (sur j)

Pour chaque ligne j différente de la ligne du pivot i, on élimine l'élément ma.get(j, i) en soustrayant une combinaison linéaire de la ligne du pivot.
Le ratio calcule combien de fois il faut soustraire la ligne du pivot pour annuler l'élément de la colonne courante.
Troisième boucle (sur k)

On applique la transformation à chaque élément de la ligne, pour la matrice ma et la matrice identité id.
Normalisation

Après avoir annulé les éléments hors diagonale, on divise chaque ligne par le pivot diagonal pour que le pivot devienne 1 (matrice identité).
On applique cette division à la matrice identité, qui devient alors l'inverse de la matrice initiale.

- **mult(Vecteur v)**
Retourne le produit de la matrice appelante avec le vecteur v. 
Une liste de coords est créée pour stocker le résultat.
Pour chaque ligne de la matrice, on calcule le produit scalaire entre cette ligne et le vecteur v, et on stocke ce résultat dans la liste de coords.
Enfin, on retourne un nouveau vecteur construit avec cette liste de coords.

- **mult(Matrice mat)**
Retourne le produit de la matrice appelante avec la matrice mat.
On crée une liste de listes pour stocker les résultats.
Pour chaque ligne i de la matrice appelante et chaque colonne j de la matrice mat, on initialise une somme à zéro.
Pour chque k, on ajoute à la somme le produit de l'élément (i, k) de la matrice appelante avec l'élément (k, j) de la matrice mat.
Après avoir calculé cette somme, on la stocke dans la liste de listes à la position (i, j).
Enfin, on retourne une nouvelle matrice construite avec cette liste de listes

- **static esperance(ArrayList<Matrice> vecteurs)**
Retourne la matrice espérance des matrices dans la liste vecteurs.
D'abord, on crée la matrice résultat de taille dim x dim, initialisée à zéro. (En fait, il faut mettre à zéro chaque élément diagonal de la matrice identité, que crée `new Matrice(dim)`).
La deuxième boucle parcourt toutes les matrices de la liste, et pour chaque élément (j, k) de chaque matrice, on ajoute cet élément à l'élément (j, k) de la matrice résultat.
Enfin, on divise chaque élément de la matrice résultat par le nombre de matrices dans la liste pour obtenir la moyenne.

- **transposee(Matrice mat)**
Retourne la transposée de la matrice mat. On crée une liste de listes pour stocker les éléments transposés.
La première boucle parcourt les colonnes de la matrice mat, et la deuxième boucle parcourt les lignes.
Pour chaque élément (i, j) de la matrice mat, on ajoute cet élément à la liste de listes à la position (j, i).
Enfin, on retourne une nouvelle matrice construite avec cette liste de listes.
De même, cette méthode n'était pas demandée dans l'énoncé, mais elle est très utile pour la méthode suivante.

- **static covariance(ArrayList<Vecteur> l)**
Retourne la matrice de covariance des vecteurs dans la liste l.
On crée une liste de listes pour stocker les données des vecteurs.
Chaque vecteur sera considéré comme une ligne de la matrice de données.
On remplit cette matrice de données avec les coordonnées des vecteurs.
Ensuite, on centre chaque colonne (variable) en soustrayant la moyenne de chaque colonne à chaque élément de cette colonne.
Enfin, on calcule la matrice de covariance en multipliant la transposée de la matrice de données par la matrice de données elle-même, puis en divisant chaque élément par (n - 1), où n est le nombre de vecteurs.