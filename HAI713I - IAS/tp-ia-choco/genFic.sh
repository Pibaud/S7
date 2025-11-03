#!/bin/bash

if [ "$#" -ne 7 ]; then
    echo "Erreur : 7 arguments sont requis."
    echo "Usage: $0 nbVars tailleDom nbConstr nbRes tuplesStart tuplesEnd tuplesStep"
    # Affiche un exemple basé sur le document ExpeCSP.pdf
    echo "Exemple: $0 35 17 249 10 211 178 -3"
    exit 1 # Quitte le script avec un code d'erreur
fi

NB_VARS=$1
TAILLE_DOM=$2
NB_CONSTR=$3
NB_RES=$4

TUPLES_START=$5
TUPLES_END=$6
TUPLES_STEP=$7

echo "--- Lancement de la génération du benchmark ---"
echo "Paramètres: "
echo "  Variables     = $NB_VARS"
echo "  Taille Domaine  = $TAILLE_DOM"
echo "  Contraintes   = $NB_CONSTR"
echo "  Réseaux / Fich = $NB_RES"
echo "Séquence 'nbTuples' (dureté) :"
echo "  Début: $TUPLES_START, Fin: $TUPLES_END, Pas: $TUPLES_STEP"
echo "------------------------------------------------"

for (( i=$TUPLES_START; i>=$TUPLES_END; i+=$TUPLES_STEP ))
do
    ./urbcsp $NB_VARS $TAILLE_DOM $NB_CONSTR $i $NB_RES > csp${NB_VARS}-${TAILLE_DOM}-${NB_CONSTR}-${i}-${NB_RES}.txt

    echo "Fichier généré : csp${i}.txt (avec $i tuples)"
done

echo "--- Génération terminée ---"