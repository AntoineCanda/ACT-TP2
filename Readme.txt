CANDA Antoine
VERSCHAEVE Théo
TP2 ACT : réduction de palette

Excusez nous pour le retard. J'ai eu un probleme de connexion ce samedi 29 octobre. 
Cela ne se reproduira plus.

La reduction de palette fonctionne. 
On a testé notamment sur le babouin. 

La commande pour executer : 

java -jar reduction.jar <chemin image> < nombre couleur final> <nom image resultat> 



1 - Implémentation des fonctions de base

On a implanté les fonctions de base de notre projet en premier lieu. 

Dans une classe ImageAction, on a reuni les methodes manipulant directement l'image que l'on souhaite modifier. Elle possede 3 methodes. 
On a une fonction qui va récupérer la liste des couples valeurs/poids pour chaque couleur de l'image qui est triée en ordre croissant: getHistogramFromImage.
On a une fonction qui créer un tableau à deux dimensions de l'image pour avoir la couleur associe a un pixel en position (x,y). Ce tableau servira pour la reconstruction de l'image:
loadPixelsFromImage
On a une fonction qui va recreer l'image avec les nouvelles couleurs: createNewImage.
Ces methodes ont ete testees avec l'image du babouin et semble coherente. 

On a une classe Interval qui definit un interval c'est a dire un indice de debut et de fin ainsi que deux accesseurs. 
On a une classe Couple qui definit un couple valeur/poids d'un pixel avec notamment des accesseurs.

On a une classe NiveauxGris qui représente le coeur de l'algorithme.
On a implanté les fonctions meilleurGris et distanceMin comme présenté dans le TP. 
On a pu tester avec les données issues du TP (réduction de la palette de 7 couleurs en une notamment avec le calcul de la 2e couleur). 
On a bien eu les résultats attendus.
On a egalement creer la fonction getCouleurFinal qui a partir d'une liste d'interval renvoye la liste des valeurs des couleurs resultats en utilisant meilleurGris. 
Avec cette methode on a pu creer la methode createNewPaletteGris qui a partir de la liste des couples de pixel, des intervals et des des couleurs resultantes permet de creer
une table de hachage qui associe la valeur de la couleur du pixel de depart a la valeur de la couleur resultat. On utilise cette map dans la creation de l'image resultat. 
Ces fonctions ont ete testees en utilisant des valeurs d'intervals de maniere aleatoire afin de voir si il y avait bien une modification des pixels de l'image de base. 
En reutilisant sur l'image resultat la methode permettant d'avoir l'histogramme des couleurs on a bien le nombre de couleurs issues de la reduction de palette. 

2 - Implementation du coeur de l'algorithme 

La methode distanceTotalMin(int debut, int nombreCouleur) est la methode permettant de calculer la distance globale minimale. 
Elle effectue un appel recursif.
Elle utilisera deux tableaux : tab[taille palette][taille palette] et tabDistImageMin[taille palette][nombreCouleur] qui sont des variables globale

pseudo code: 

Entree : tab et tabDistImageMin deux tableaux initialisé à -1 et deux entiers nombreCouleur et debut 
Sortie : entier res 

	int min = + infini (on prend un entier plus grand que 256*256*nombrePixel)

	// Cas de base 
	Si(nombreCouleur == 1 ){
		Si tab[debut][taille palette - 1 ] == -1{
			gris = meilleurGris(debut, taille palette -1);
			tab[debut][taille palette -1 ] = distanceMin(debut, taille palette -1 , gris)
		}
		return tab[debut][taille palette -1];
	}
	Pour(j = debut; j < taille palette - (nombreCouleur -1)); j++){
		
		Si (tab[debut][j]== -1){
			// on le calculer
			gris = meilleurGris(debut,j);
			tab[debut][j] = distanceMin(debut, j, gris);
		}
		Si( tabDistImageMin[j+1][nombreCouleur-1] == -1){
			// on calculer et on utilise pour cela la recursion et on reduit le probleme
			tabDistImageMin[j+1][nombreCouleur-1] = distanceTotalMin(j+1, nombreCouleur-1);
		}
		
		int res = tabDistImageMin[j+1][k-1] + tab[debut][j];
		
		Si(min> res){
			min = res
		}
	}
	return min ; 
	
	Complexite : remplissage des tableaux = O(n²).
	Cas de base :  O(1)
	Complexite : O(n²)
	
3 - Remontée de table pour construire les intervals

	On utilise pour cela la fonction getInterval qui permet de construire les intervals des fusion de couleurs. 
	Pour cela on va additionner le resultat de distanceMin pour un interval donne avec le resultat TabDistanceImageMin qui correspond aux intervals suivants 
	Si on a une correspondance avec le resultat a trouver on prend l'interval correspondant a distanceMin et on change le resultat par la valeur de TabDistanceImageMin qui est le reste a trouver.
	On fait une boucle jusqu'à avoir 1 seul couleur qui correspond alors au reste des couleurs a fusionner soit debut à taille palette -1
	
	Pseudo code: 
	
	Entree : entier nombre de couleur, resultat global a trouver. On va reutiliser les deux tableaux pour cela
	Sortie : liste des intervales
	
	getInterval(nombreCouleur, res){
		liste interval = new liste;
		
		debut = 0;
		resultat = res;
		
		Pour(int i = nombreCouleur -1; i>0; i--){
			Pour(int j = debut +1; j < taille palette - 1; j++){
				Si(tab[debut][j-1] + tabDistImageMin [j][i] == resultat){
					Interval interval = nouveau Interval(debut, j-1);
					on ajoute interval a la liste des interval;
					resultat = tabDistImageMin[j][i];
					debut = j;
					break;
				}
			}
		}
		Interval interval = nouveau Interval(debut, taille palette - 1);
		ajoute interval à la liste des interval;
		
		return la liste des interval;
	}
	
	A partir de cette liste des intervals on va pouvoir utiliser meilleurGris sur chacun d'eux pour avoir la liste des gris resultat
	On le fait dans la fonction simple getCouleurFinal.
	A partir de cette liste des couleurs on va pouvoir associer les anciennes couleurs aux nouvelles. 
	On utilise pour cela la fonction createNewPaletteGris qui prend en parametre la liste des interval la liste des gris final et la liste des couples initiaux representant la palette de base.
	Pour chaque interval de la liste des intervals, on va faire une boucle allant de debut de l'interval a fin de l'interval et on y associe la j-ieme couleur de la liste des couleurs finals a chaque couleur presente dans la palette intiale 
	comprise dans l'itnervale. 

4 - Reconstruction de l'image

Cette partie avait ete traitee dans les fonctions de base car c'est l'assemblage de toute les etapes precedentes. 

5 - Justification 

On souhaite justifier le fait que les couleurs voisines sont nécessaire pour avoir la distance minimale entre les images de depart et d'arrivee. 

Pour cela on a besoin de justifier distanceMin. 

distanceMin a besoin de meilleurGris pour calculer la moyenne ponderee des differents gris fusionne en un gris.
distanceMin a comme algorithme valeurAbsolue(valeur de la couleur de depart - valeur de la couleur d'arrivee)² * poids de la valeur de depart. 

Imaginons que les valeurs des couleurs ne soient pas triees. On fusionne les couleurs au hasard. Si on tombe sur deux couleurs aux extrimites par exemple 0 et 255 avec respectivement p1 et p2  pixels.

Disons que p1 = p2 = p. 

on aura meilleurGris = 127 ou 128 selon l'arrondi. 

Quand on calculera distanceMin on aura (-128)²*p1 + (127)²*p2. Soit ((-128)²+(127)²)*p ~ 32500*p. 

Le probleme se trouve au niveau du calcul de distanceMin. 

Soit v1<v2<v3<v4 et vG1 = meilleurGris() a partir des pixels de v1 et v2. vG2 = meilleurGris a partir des pixels de v1 et v4 avec p1,p2,p3,p4 > 0 .
On considere que p1 ~ p2 ~ p3 ~ p4. 

	|(v1-vG1)| < |(v1-vG2)|  	et 	|(v2-vG1)| < |(v4-vG2)|
	|(v1-vG1)|² < |(v1-vG2)|²	et 	|(v2-vG1)|² < |(v4-vG2)|²
	|(v1-vG1)|² * p1 < |(v1-vG2)|² * p1 	et 	|(v2-vG1)|² * p2 < |(v4-vG2)|² * p4

	|(v1-vG1)|² * p1 + |(v2-vG1)|² * p2  < |(v1-vG2)|² * p1 + |(v4-vG2)|² * p4
	


