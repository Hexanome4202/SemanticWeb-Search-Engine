Procédure à suivre pour utiliser le projet :
===========================================

## Prérequis :
- Eclipse installé avec les plugins J2EE
- Tomcat installé

## Installation
- Placer le projet dans le Workspace d'Eclipse
- Dans Eclipse, avec la perspective Java EE : 
	-> Nouveau "Dynamic Web Project" (Ctrl+N -> Web -> Dynamic...)
	-> Décocher "Use default location" et choisir le repertoire dans lequel se trouve le projet
	-> Choisir un nom pour le projet
	-> Terminer la configuration du projet simplement (Next... Finish)

- Dans la vue Project Explorer
	-> clic droit sur le projet
	-> Run As -> Run on server
	-> Sélectionnez "Manually define a new server"
	-> Type : Tomcat v6.0 Server
	-> Next, Finish
	
- Si une popup vous indique que les ports que le serveur veut utiliser sont déjà occupés, changez-les.
	-> Vue Servers, double clic sur le serveur
	-> Ports : changer les ports posant problème

- Ajoutez le contenu du dossier "js" à l'intérieur de votre dossier Eclipse

	
- Lancez le serveur depuis le menu contextuel obtenu après un clic droit sur ledit serveur.

- Ouvrez dans un navigateur l'adresse suivante : localhost:[Port utilisé]/[Nom du projet]

- Enjoy.

