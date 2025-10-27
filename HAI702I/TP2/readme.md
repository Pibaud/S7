# TP2 — Reconnaissance de gestes (Rubine)

Compte rendu du TP Noté sur Reconnaissance de gestes par le classifieur de Rubine. À l'origine, ce TP a été réalisé avec les premières données qui nous ont été fournies. Par la suite, afin de pouvoir faire des tests plus avancés, je me suis servi du nouveau jeu de données que vous nous avez donné, car, avec si peu de données à l'origine il était très compliqué de faire une séparation train/test, et je trouvais dommage de rester sur une évaluation des performances du classifieur sur ses propres données d'entraînement. Bon nombre de fichiers et méthodes ont été réadaptés en conséquence, ce fichier readme représente en quelques sortes la transition entre ces deux phases.

Je me suis rendu sur divers forums, sites internet pour obtenir des informations, j'ai de plus échangé avec mon camarade Élouand Portes. Il sera précisé dans ce readme pour quelles informations j'ai du y faire recours.

## Résumé du travail rendu

- `Trace.java`
    - `initFeatures()` :
        - `f1` et `f2` : Je me suis servi de `Math.hypot(x, y)` qui s'est révélé très utile car cette méthode m'a permis de retourner facilement `sqrt(x**2 + y**2)`. Cette méthode me serait restée inconnue si je ne m'étais pas rendu sur internet et en particulier stackOverflow. J'ai commencé par implémenter directement la version de l'article à savoir le sinus et cosinus entre `p0` et `p1`, mais lors de la phase de Tuning de l'exercice 5, je me suis rendu compte que ce qui était écrit dans le diapo du cours à savoir le sinus et cosinus entre `p1` et l'axe x était plus pertinent car il m'offre une augmentation de la précision de 2% environ.

        - `f3` et `f4` : J'ai simplement itéré dans tous les points du tracé pour repérer les minimums et maximums, en utilisant sur ces extremums `Math.hypot(x, y)` pour `f3` et `Math.atan2(numérateur, dénominateur)` pour `f4`, ce qui m'a aussi simplifié la tâche.

        - `f5` : Le calcul de la distance entre le premier et le dernier point ne pouvant être optimisé, je l'ai implémenté sans modification.

        - `f6` et `f7` : De la même manière, je n'ai pas touché à la formule décrite dans l'article de Rubine.

        - `f8` : Je me suis de nouveau servi de `Math.hypot` pour incrémenter à chaque itération la longueur totale.

        - `f9`, `f10` et `f11` : En une seule boucle, j'incrémente la variable de la somme des Theta (`f9`) en utilisant `Math.atan2`, pour `f10`, je fais la même chose mais avec la valeur absolue de cet angle grâce à `Math.abs` et pour `f11` j'ai fait de même mais en incrémentant une autre variable qui enregistre la somme des carrés des theta. Tous ces calculs étant très similaires, j'ai pu les combiner.

        - `f12` et `f13` : À l'origine, j'avais implémenté ces fonctions respectivement de vitesse et de durée comme l'indiquait en calculant pour `f12` la plus grande vitesse enregistrée entre deux points et pour `f13` la durée totale en faisant le timestamp du dernier moins celui du premier.  
          Mais lors de la phase de tuning, je me suis rendu compte, grâce à la visualisation que vous nous avez donnée, que les variations de vitesse des tracés d'un dossier `data-x` à un autre sont très importantes. En effet, on peut constater que certains sont composés de peu de points (vitesse très élevée) et d'autres en ont beaucoup moins (vitesse plus faible). Parallèlement, les gestes rapides durent inévitablement moins de temps que ses homologues. Me rendant compte que ces features pouvaient être moins utiles que les autres voire trop discriminantes, j'ai décidé de les retirer. Dans la section résultats, vous constaterez que ce retrait a non seulement permis de classifier les gestes avec moins de features mais cela a en plus augmenté la précision du classifieur d'environ **0.7%**

    - `getFeatureVector()` : renvoie l'attribut privé correspondant et appelle `initFeatures()` si une trace n'a pas encore été initialisée.

- `Geste.java`
    - `init()` : Pour chaque tracé du geste, on initialise ses features, on stocke dans une liste son vecteur de features puis on stocke l'espérence et la matrice de covariance résultant de ces vecteurs.

    - `initEstimators(Matrice)` : calcule `weightVector` et `bias` (ak et bk) et conserve ces estimateurs tel que décrit dans l'énoncé du TP. Les accesseurs `getWeightVector()` et `getBias()` ont également été implémentés.

- `Rubine.java`
    - `init(Lexique)` : Calcule la covariance commune (EOTCCM) et son inverse puis appelle `initEstimators` sur chaque geste. Je me suis rendu compte qu'il fallait faire deux fois la même boucle pour itérer dans les gestes car lors de la première, il nous est encore impossible de connaître l'inverse de EOTCCM, étant calculée après. La seconde boucle permet donc d'initialiser les estimateurs, ce qui nécessite cette matrice inverse.

    - `test(Lexique)` : Cette méthode m'a permis d'obtenir les scores de reconnaissance pour chacun des gestes du lexique. En effet, elle mesure la précision (taux de reconnaissance) pour un geste donné, sur l'ensemble de ses exemples, soit tous les tracés contenus.

    - `f(Vecteur x, Geste k)` : Il n'était pas demandé de faire cette méthode à part mais elle m'a permis de rendre mon code plus lisible. Elle sert pour la méthode `squaredMahalanobis` en renvoyant un score, qui, s'il est maximal pour `x`, indique que le geste reconnu est `k`.

    - `squaredMahalanobis(Vecteur t, Vecteur g)` : Permet de connaître la distance entre les deux vecteurs, selon l'inverse de la EOTCCM. À l'origine, la formule de la distance carrée de Mahalanobis est la suivante : `(t - g)^T * EOTCCM^-1 * (t - g)`. Le problème étant que la classe vecteur ne disposait pas de fonction pour calculer la transposée d'un vecteur, j'ai utilisé l'associativité du produit matriciel. Ainsi j'ai d'abord calculé `EOTCCM^-1 * (t - g)` puis j'ai fait un produit scalaire entre cette quantité et `(t - g)`. En effet, je fais un produit scalaire car à ce stade `EOTCCM^-1 * (t - g)` et `(t - g)` sont des vecteurs donc ramené à des vecteurs, un produit matriciel correspond à un produit scalaire. J'ai quand même vérifié sur différents sites de mathématiques si l'associativité était bien appropriée dans ce cas-là.

    - `recognize(Trace)` : calcul des scores fk(g) et sélection du geste max avec rejet par Mahalanobis. Dans cette méthode, le point central a été de déterminer quel est le bon seuil de rejet. Dans les diapos du cours, pour être accepté, la distance entre l'espérance du geste et le vecteur de features d'un tracé doit être inférieur à `0.5*F^2` avec F la dimension des vecteurs de features (`F^2` car la distance de Mahalanobis est elle aussi au carré). Cependant, après plusieurs tests successifs, ce rejet était bien trop restricitf (Rubine indique lui même dans son article qu'il peut produire de nombreux faux négatifs), j'ai donc augmenté la tolérance à `2*F^2`, ce qui m'a valu une augmentation d'environ **5%** de la précision de reconnaissance du jeu de test

    - `testGeste(Geste)` : Retourne la moyenne des tracés reconnus pour un geste. Avant de passer sur le nouveau jeu de données et donc sur un split train/test, je comparais le geste reconnu grâce à `recognize` avec un `.equals` avecle geste en entrée. Mais après séparation entre ces deux splits, la comparaison des instances n'est plus valable, il m'a donc fallu comparer les noms des gestes en question.

    - `testLexique(Lexique)` : retourne la moyenne des taux de reconnaissance des gestes en appelant la méthode `testGeste` décrite juste au dessus sur tous les gestes du lexique.

- `Lexique.java` : Bien que le sujet ne demandait pas de le modifier, j'ai quand même du lui apporter des modifications pour que mon implémentation avec les nouvelles données que vous nous avez fournies fonctionne. Il a fallu revoir toute la logique d'exploration de fichiers pour initialiser modèles, gestes et tracés.

    - `initData(String split)` : Permet d'initialiser les gestes grâce à tous les tracés au format `.csv`. La première chose à remarquer est que j'ai changé la signature de cette fonction pour lui ajouter un paramètre `split` qui peut valoir `train` ou `test` (étant les noms de nouveaux dossiers que j'ai créés) selon les données que l'on veut obtenir. Dans le premier jeu de données, le chemin des données était le suivant : `data/rawData/trace-x`. Désormais, il est le suivant : `data/train(ou test)/data-x/rawData/trace-x` il m'a donc fallu changer les boucles pour itérer dans chaque dossier `data-x` puis dans dans les `trace-x`, rajoutant une boucle imbriquée au procédé initial. Pour chaque `trace-x` prend le premier `*-model.csv` comme modèle, charge les autres CSV comme traces.

- `Main.java` : Logiquement, le main a du être modifié depuis la première version pour s'aligner avec la nouvelle méthode de séparation des données. Ainsi, elle fait `initData("train")` puis `initData("test")`, initialise un classifieur de Rubine puis utilise ce classifieur pour tester respectivement ces deux lexiques.

## Résultats observés
- Scores de reconnaissance obtenus pour chacun des gestes du lexique proposé.
    - Avant le nouveau jeu de donnés :  
      Malheureusment étant encore au début du TP, je n'avais pas encore implémenté l'affichage du taux de chaque geste mais mon taux de reconnaissance sur train (train étant l'ensemble de mes données, n'ayant pas la capacité de faire un split) était de **73.45%**

    - Avec le nouveau jeu de données :  
      Moyenne des taux de reconnaissance sur **train : 89.99999999999999 %**  
      Moyenne des taux de reconnaissance sur **test  : 27.013888888888886 %**  
      -------Pourcentage de reconnaissance par geste--------  
      geste |    train |     test  
      trace-01 |  100,00% |   40,00%  
      trace-02 |  100,00% |   50,00%  
      trace-03 |   90,00% |   40,00%  
      trace-04 |   60,00% |    0,00%  
      trace-05 |   70,00% |    0,00%  
      trace-06 |  100,00% |   22,22%  
      trace-07 |  100,00% |   22,22%  
      trace-08 |  100,00% |   70,00%  
      trace-09 |   60,00% |   50,00%  
      trace-10 |   90,00% |    0,00%  
      trace-11 |  100,00% |    0,00%  
      trace-12 |   80,00% |    0,00%  
      trace-13 |  100,00% |  100,00%  
      trace-14 |  100,00% |   10,00%  
      trace-15 |  100,00% |   70,00%  
      trace-16 |   90,00% |    0,00%


- Avec le nouveau jeu de données et avec tuning en enlevant f12 et f13 :  
  Moyenne des taux de reconnaissance sur **train : 90.62499999999999 %**  
  Moyenne des taux de reconnaissance sur **test  : 31.11111111111111 %**  
  -------Pourcentage de reconnaissance par geste--------  
  geste |    train |     test  
  trace-01 |  100,00% |   50,00%  
  trace-02 |   90,00% |   60,00%  
  trace-03 |  100,00% |   40,00%  
  trace-04 |   50,00% |    0,00%  
  trace-05 |   80,00% |    0,00%  
  trace-06 |   80,00% |    0,00%  
  trace-07 |  100,00% |   55,56%  
  trace-08 |  100,00% |   70,00%  
  trace-09 |   80,00% |   40,00%  
  trace-10 |   90,00% |    0,00%  
  trace-11 |   90,00% |    0,00%  
  trace-12 |  100,00% |    0,00%  
  trace-13 |  100,00% |  100,00%  
  trace-14 |   90,00% |   10,00%  
  trace-15 |  100,00% |  100,00%  
  trace-16 |  100,00% |    0,00%


## Difficultés rencontrées et solutions apportées

- Le nouveau défi venant avec ces données complémentaires était de répartir astucieusement les données d'apprentissage et de tests. J'ai opté pour une séparations 80/20 car c'est la plus courante. En ouvrant l'archive j'ai vu qu'elle ne contenait que des dossiers `data-x`. La somme des tailles de ces dossiers étant de 1 693 437 octets, j'ai créé un dossier `data/train` contenant environ 80% des données en prenant les dossiers `data-1`, `data-2`, `data-3` et `data-5` (soit 1 351 424 octets) et un dossier `data/test` avec les 20% restants des dossiers `data-0` et `data-4` (316 808 octets) pour tirer parti des ressources que vous nous avez données en plus. Cette séparation aurait surement été moins pertinente si j'avais du faire ce split sur un jeu de données plus petit.

- Un autre problème survenu avec ces nouvelles données est le format CSV des `model` qui diffère : les fichiers  `*-model.csv` utilisent une 3ème colonne non numérique (`p` en l'occurrence) au lieu d'un timestamp. Mon code cherchait à parser ce caractère en Long, ce qui créait un bug. La solution que j'ai apportée dans `ReadWritePoint.java` distingue deux cas :
    - Si cette troisième colonne est numérique (donc un timestamp de tracé) : le comportement ne change pas
    - Sinon c'est un `model`. Dans ce cas je rajoute à ce model un timestamp valant `i*10L`, avec `i` le numéro de la ligne dans le `csv` et `10L` un timestamp espacé toutes les 10ms. C'est arbitraire mais cela permet d'avoir un tracé régulier potentiellement assez proche des autres plus "humains". Même si `f12` a été retiré, il était nécessaire de faire ce changement.

- Dernier problème en lien avec le jeu de données complémentaires : il manquait un champ `label` et la méthode `setLabel()` dans `StampedCoord.java`, je les ai rajoutés.

- NullPointerException : certaines traces n'avaient pas leurs features initialisés avant `getFeatureVector()`, ce qui n'était pas un problème dans la première version avec les anciennes données, donc j'appelle `initFeatures()` dans la méthode si nécessaire.

- Problème critique découvert très tard; calcul incorrect de l'espérance d'un vecteur : pendant quasiment tout le TP, rien n'indiquait un problème dans les résultats. En effet, en me penchant sur la classe `src/algebre/Vecteur.java`, je me suis rendu compte que la méthode `esperance(ArrayList<Vecteur>)` additionnait correctement les composantes puis divisait par la dimension du vecteur au lieu de diviser par le nombre de vecteurs (`lv.size()`). Ainsi, toutes les moyennes de gestes utilisées pour entraîner le classifieur étaient biaisées. J'ai donc remplacé la division pour utiliser `lv.size()` (nombre de vecteurs) comme dénominateur.

## Ce que je n'ai pas fait / améliorations possibles

- Meilleure stratégie pour les timestamps avec quelque chose de plus organique. Pour l'instant, remplace `"p"` par la valeur arbitraire de 10ms peut nuire à la précision du classifieur. Une approche plus organique qui ajusterait harmonieusement les timestamps entre les points permettrait d'avoir des `model` qui ressembleraient plus aux tracés testés.
- Gestion avancée du seuil de rejet (pour l'instant, c'est un choix empirique de seuil Mahalanobis). On pourrait automatiser le choix du seuil. Mon camarade Élouand m'a parlé de la fonction chi2. Il m'a indiqué que si les données suivent une loi gaussienne, alors la fonction khi-deux permettrait de remplacer le seuil empirique par un seuil probabiliste. Au lieu de le fixer "au hasard", on peut décider de fixer un niveau de confiance (ex: 95%). La fonction donnerait la valeur exacte du seuil qui garantirait que 95% des points appartenant à la classe seront acceptés.
- Enfin, une dernière amélioration possible serait d'avoir plus de données pour un meilleur entraînement. On pourrait, à partir du dataset existant, faire des mutations très légères sur nos tracés pour en générer de nouveaux similaires.
