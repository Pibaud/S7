#!/bin/bash
unset GTK_PATH GIO_MODULE_DIR GSETTINGS_SCHEMA_DIR GTK_IM_MODULE_FILE XDG_DATA_DIRS LD_LIBRARY_PATH LD_PRELOAD

# Vérifier que le fichier contenant les commandes existe
COMMANDS_FILE="commands.txt"
if [ ! -f "$COMMANDS_FILE" ]; then
  echo "Erreur : Le fichier $COMMANDS_FILE n'existe pas."
  exit 1
fi

# Lire et exécuter chaque commande dans un terminal séparé
while IFS= read -r command; do
  echo "Exécution de : $command"
  gnome-terminal -- bash -c "$command; exec bash"
done < "$COMMANDS_FILE"

echo "Tous les clients ont été lancés."