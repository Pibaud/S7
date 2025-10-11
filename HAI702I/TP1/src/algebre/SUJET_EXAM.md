# TP NOTÉ - Reconnaissance de Symboles Mathématiques
**Durée : 1h30**  
**Documents autorisés : Non**  
**Calculatrice : Non**

---

## Contexte

Vous disposez d'un système de reconnaissance de symboles mathématiques tracés à la main. 
Chaque symbole est représenté par une **matrice 5×5** contenant des valeurs entre 0 et 1 
(0 = pixel blanc, 1 = pixel noir).

Vous devez implémenter un classifieur simple basé sur la **distance de Frobenius** 
pour reconnaître automatiquement des symboles dessinés.

---

## Données fournies

### Base de référence (symboles parfaits)

**Symbole "PLUS" (+)** :
```
0.0  0.0  1.0  0.0  0.0
0.0  0.0  1.0  0.0  0.0
1.0  1.0  1.0  1.0  1.0
0.0  0.0  1.0  0.0  0.0
0.0  0.0  1.0  0.0  0.0
```

**Symbole "CROIX" (×)** :
```
1.0  0.0  0.0  0.0  1.0
0.0  1.0  0.0  1.0  0.0
0.0  0.0  1.0  0.0  0.0
0.0  1.0  0.0  1.0  0.0
1.0  0.0  0.0  0.0  1.0
```

**Symbole "CARRE" (□)** :
```
1.0  1.0  1.0  1.0  1.0
1.0  0.0  0.0  0.0  1.0
1.0  0.0  0.0  0.0  1.0
1.0  0.0  0.0  0.0  1.0
1.0  1.0  1.0  1.0  1.0
```

### Tracés à reconnaître

**Tracé 1** (à identifier) :
```
0.0  0.0  0.9  0.0  0.0
0.0  0.0  0.8  0.1  0.0
0.9  1.0  1.0  0.9  0.8
0.0  0.1  0.9  0.0  0.0
0.0  0.0  1.0  0.0  0.0
```

**Tracé 2** (à identifier) :
```
0.9  0.1  0.0  0.0  0.8
0.1  0.9  0.0  0.8  0.1
0.0  0.0  0.9  0.1  0.0
0.0  0.8  0.1  1.0  0.0
1.0  0.0  0.0  0.0  0.9
```

**Tracé 3** (à identifier) :
```
1.0  0.9  1.0  0.9  1.0
0.9  0.1  0.0  0.1  1.0
1.0  0.0  0.0  0.0  0.9
0.9  0.0  0.1  0.0  1.0
1.0  1.0  0.9  1.0  1.0
```

---

## Questions

### **Partie 1 : Reconnaissance simple (40 min - 8 points)**

Créez une classe `ReconnaissanceSymbole` avec les méthodes suivantes :

#### Q1.1 (4 points)
Implémentez la méthode :
```java
public static String symbolePlusProche(Matrice trace, 
                                      ArrayList<Matrice> references,
                                      ArrayList<String> nomsSymboles)
```
Cette méthode doit retourner le nom du symbole de référence le plus proche du tracé donné, 
en utilisant la distance de Frobenius.

#### Q1.2 (4 points)
Créez un `main()` qui :
- Initialise les 3 symboles de référence (PLUS, CROIX, CARRE)
- Reconnaît les 3 tracés fournis
- Affiche pour chaque tracé : `"Tracé X reconnu comme : [symbole]"`

---

### **Partie 2 : Analyse des distances (30 min - 6 points)**

#### Q2.1 (3 points)
Implémentez la méthode :
```java
public static void afficherDistances(Matrice trace,
                                    ArrayList<Matrice> references,
                                    ArrayList<String> nomsSymboles)
```
Cette méthode affiche la distance entre le tracé et chaque symbole de référence, 
triée par ordre croissant.

Format attendu :
```
Distances pour Tracé 1 :
  PLUS : 0.524
  CROIX : 2.158
  CARRE : 3.421
```

#### Q2.2 (3 points)
Ajoutez au `main()` l'affichage des distances pour les 3 tracés.

---

### **Partie 3 : Amélioration avec moyennage (25 min - 4 points)**

On dispose maintenant de **plusieurs exemples** pour chaque symbole :

**Exemples supplémentaires de PLUS** :
```
// PLUS_v2 (légèrement bruité)
0.1  0.0  0.9  0.0  0.1
0.0  0.1  1.0  0.0  0.0
0.9  1.0  1.0  1.0  0.9
0.0  0.0  0.9  0.1  0.0
0.0  0.0  1.0  0.0  0.0
```

#### Q3.1 (2 points)
Utilisez la méthode `Matrice.esperance()` pour calculer le symbole PLUS moyen 
à partir de PLUS et PLUS_v2.

#### Q3.2 (2 points)
Comparez la distance entre Tracé 1 et :
- PLUS (référence originale)
- PLUS_moyen (calculé avec esperance)

Affichez les deux distances et commentez le résultat.

---

### **Partie 4 : Normalisation (15 min - 2 points - BONUS)**

#### Q4.1 (2 points)
Modifiez votre reconnaissance pour normaliser les matrices avant de calculer les distances.
Comparez les résultats avec et sans normalisation pour le Tracé 1.

---

## Barème

| Partie | Points |
|--------|--------|
| Partie 1 : Reconnaissance simple | 8 |
| Partie 2 : Analyse des distances | 6 |
| Partie 3 : Moyennage | 4 |
| Partie 4 : Normalisation (bonus) | 2 |
| **TOTAL** | **20** |

---

## Conseils

- Commencez par la Partie 1 pour avoir un programme fonctionnel
- Testez votre code au fur et à mesure
- La méthode `distanceFrobenius()` est déjà implémentée dans la classe `Matrice`
- La méthode `Matrice.esperance()` est déjà disponible
- Pensez à bien initialiser vos matrices avec `new Matrice(tableau2D)`

---

## Rendu attendu

Un fichier `ReconnaissanceSymbole.java` contenant :
- La classe avec toutes les méthodes demandées
- Un `main()` qui exécute tous les tests demandés
- Des affichages clairs et lisibles

**Bon courage !**
