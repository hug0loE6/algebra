# Compte rendu du TP noté HAI702I IASD - LOPEZ Hugo

## Exercice 1: Calcul des features pour chaque tracé

Le calcul des 13 features proposées dans l’article de Rubine se trouve dans la fonction d’initialisation d’un tracé :
[initFeatures() de Trace](src/geste/Trace.java#L37). Je les ai toutes copiées à la lettre de l’article, à quelques exceptions près : 

- Pour les features 1 et 2, si la distance entre les trois premiers points sont égal à 0, j'ai rajouté une condition pour gérer la division par 0 ([les features auront pour valeur 0](src/geste/Trace.java#L39))

- Pour les [features 6 et 7](src/geste/Trace.java#L80), c'est plus au moins le même problème, si la distance entre le premier et dernier point est égal à 0, alors les features seront égal à 0.

- Pour les features 9,10 et 11 il est nécessaire de calculer l'angle entre deux vecteurs consécutifs noté "$\theta_p$", Dans le cas où le dénominateur est égal à 0, cela signifie que l’angle entre ces deux vecteurs est égal à un sinus de 1 ou -1 (selon que le numérateur soit positif ou négatif). L’article de Rubine ne traite pas ce cas, donc mon calcul de [$\theta_p$](src/geste/Trace.java#L78) diffère dans mon code.

- Pour la [feature 4](src/geste/Trace.java#L67), c'est le même problème que précédemment, donc la feature est égal à sinus de 1 ou -1 si le premier et le dernier point partage le même abscisse.

Et ensuite on a une méthode permettant de retourner ces 13 valeurs dans un vecteur :
[getFeatureVector()](src/geste/Trace.java#L119)

## Exercice 2: Calcul de l'espérance et de la covariance pour chaque geste, en utilisant les données d'entrainement

Nous avons, pour chaque geste, plusieurs tracés. Nous effectuons une initialisation des features sur chacun de ces tracés. Ensuite, nous calculons l’espérance en faisant la moyenne de chaque feature. Nous calculons également la covariance à l’aide de la méthode covariance de la classe Matrice. Le tout est réalisé dans une seule méthode [d'initialisation de la classe Geste.](src/geste/Geste.java#24)

J'ai aussi rajouté des accesseurs pour l'espérance et la covariance des gestes pour pouvoir les avoir plus tard pour le calcul des estimateurs.

## Exercice 3: Calcul des estimateurs

Avant de calculer l'estimateur de covariance, nous initialisons les gestes du lexique. Cela nous permet d’obtenir l’ensemble des matrices de covariance de tous nos gestes afin de calculer cet estimateur. Nous calculons également son inverse, le tout étant réalisé dans la [méthode d'initialisation de la classe Rubine](src/classifieur/Rubine.java#L20)

Pour compléter l’implémentation de l'interface Estimable pour la classe Geste, nous avons une méthode [initEstimators()](src/geste/Geste.java#75) qui calcule le vecteur de poids et le biais de chaque geste (les méthodes de Vecteur et Matrice nous facilitent la tâche pour les produits scalaires et les multiplications).

Enfin, les accesseurs de ces estimateurs ont été ajoutés, ce qui complète l’implémentation.

## Exercice 4: Implémentation du classifieur

La méthode [squaredMahalanobis](src/classifieur/Rubine.java#66) a été réalisé avec plusieurs difficultés. Tout d’abord, la classe Matrice ne gère que les matrices carrées, ce qui rend impossible la transposition d’un vecteur dans un objet de type Matrice afin d’utiliser la méthode de produit matriciel. J’ai donc effectué les calculs du produit directement dans la méthode. En revanche, un second problème est apparu : la méthode get() de la classe Matrice était privée. Cette classe n’étant pas une partie du travail à rendre, **il faut donc rendre la méthode get() publique pour que la méthode squaredMahalanobis() fonctionne**.  Malgré ces problèmes résolus, je ne suis pas sur du résultat de ma méthode et je n'arrive pas à savoir quelle est la partie qui fait fausser le calcule.

En revanche la méthode [recognize](src/classifieur/Rubine.java#50) fonctionne à l’exception du fait que la méthode ne retourne pas *null* si aucun geste n’a été reconnu, car on prend le maximum du résultat du calcul pour chaque geste. Donc, sauf erreur de l’énoncé, je me suis peut-être trompé sur son fonctionnement, mais les résultats sont positifs.

Enfin les méthode [testGeste](src/classifieur/Rubine.java#97) et [testLexique](src/classifieur/Rubine.java#124) ont été réalisées sans trop de soucis.

## Exercice 5: Tuning

A part les modifications réalisées dans les features qui peuvent réaliser des divisions par 0 (Compte rendu exercice 1), je n'ai pas réussi à trouver d'autre moyen pour réaliser le calcul des features plus rapidement. 

Pour ce qui est des features optionnelles, j’ai réussi à enlever la feature n°3 (longueur de la diagonale du rectangle englobant) et j’ai continué à obtenir des résultats positifs avec la méthode **recognize**. Cette feature semble donc inutile, puisqu’un tracé peut être plus grand ou plus petit que le geste ; s’il en conserve la même forme, on peut considérer qu’il est équivalent. En revanche, je ne sais pas pourquoi, mais retirer la feature n°12 (temps total mis pour réaliser le tracé) me donne des résultats négatifs, alors qu’un tracé plus lent que le geste, tout en étant équivalent en forme, devrait être reconnu selon moi.


# Origine du code

Une source extérieure a été utilisée pour m’aider à réaliser ce code : une IA générative (plus précisément l’autocomplétion de Copilot). Je l’ai principalement utilisé pour compléter des parties répétitives, notamment le calcul des features.

# Résumé des résultats 

J'ai décidé de tout de même garder toutes les features de Rubine car malgré selon moi l'inutilité des features 3 et 12, la possibilité de résultat négatif que je n'arrive pas à comprendre existe et donc j'ai laisser toutes les features.

Pour tester la précision de la méthode **recognize**, on effectue de nombreux tests en prenant un geste aléatoire et un tracé aléatoire de ce geste, puis on vérifie si le geste reconnu à partir du tracé est correct. On obtient ainsi une précision de 90 %.

Enfin, pour le test du lexique (avec la méthode *testLexique*), j’obtiens un taux moyen de reconnaissance des gestes de 28,6 %. Cette valeur est peut-être incorrecte à cause du mauvais calcul de la distance de Mahalanobis.
