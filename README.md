# GraphicalNMF
Graphical NMF est une application qui permet l'utilisation de Neo Modeling Framework (NMF) 
avec une interface graphique.

Pour en apprendre plus sur Neo Modeling Framework : https://github.com/geodes-sms/NeoModelingFramework

## Installation
Aucun jar n'est disponible pour l'instant.

### Prérequis
1. Une base de donnée Neo4j avec le plugin APOC. Voir https://neo4j.com/download pour l'installation
et https://github.com/neo4j-contrib/neo4j-apoc-procedures pour la configuration du plugin APOC.
2. Java 11
3. Compilateur Kotlin (version 1.5) : https://kotlinlang.org/docs/command-line.html

### Configuration du projet
Le projet peut être téléchargé directement de github. J'utilise IntelliJ comme IDE, mais
n'importe quel IDE qui supporte l'utilisation de Gradle devrait faire l'affaire.

L'application peut être lancer à partir de la class `Main` ou `view.GraphicalNMFApplication`.

## Utilisation de l'application
Avant d'utiliser l'application, assurez-vous d'avoir une base de donnée Neo4j ouverte.

Au lancement de l'application, la première fenêtre qui apparait permet de choisir le
métamodèle à utiliser pour l'application.
Cliquer sur le bouton *Open from Metamodel* (c'est la seule option qui fonctionne pour
l'instant). <br>
Dans la fenêtre qui apparait, choisir :
- Le fichier **.ecore** qui correspond au métamodèle
- Le dossier dans lequel placer les classes générées (doit être vide)
- Le dossier dans lequel placer les classes compilées (doit être vide)
Cliquer sur *ok* pour lancer la génération du code.

Une fois le code généré, une nouvelle s'ouvre. Cette fenêtre permet de voir les éléments
ouvert dans l'application. Une boite de dialogue s'ouvre automatiquement pour demander
la connexion à la base de donnée.

Une fois connecté à la base de données, il est possible de créer et d'ouvrir des éléments
à partir du menu *Edit*. Les éléments ouverts sont affichés dans la partie gauche de l'application.
Un clique droit sur un élément affiche le menu contextuel de cet élément.

Pour afficher les informations d'un élément, il faut faire *Clique droit > Edit [name]* ou
cliquer deux fois sur l'élément. La fenêtre qui s'ouvre permet de modifier les attributs de
l'éléments. Assurez-vous d'appuyer sur *Enter* pour que l'application reconnaisse les 
modifications. <br>
Les modifications sont enregistrées automatiquement, donc les boutons *Save and Quit* et *Save*
ne servent à rien.

Pour charger un modèle dans la base de donnée, il faut aller dans le menu *File* et cliquer sur
*Load Model*.
