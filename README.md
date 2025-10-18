# Compte rendu du TP noté HAI702I IASD - LOPEZ Hugo
src ennoncé = https://www.lirmm.fr/~mountaz/Ens/Alg2025/tp2/
## Exercice 1: Calcul des features pour chaque tracé

Le calcul des 13 features proposées dans l’article de Rubine se trouve dans la fonction d’initialisation d’un tracé :
[initFeatures() de Trace](src/geste/Trace.java#L37). Je les ai toutes copiées à la lettre de l’article, à une exception près.

Pour les features 9,10 et 11 il est nécessaire de calculer l'angle entre deux vecteurs consécutifs noté "$\theta_p$", Dans le cas où le dénominateur est égal à 0, cela signifie que l’angle entre ces deux vecteurs est égal à un sinus de 1 ou -1 (selon que le numérateur soit positif ou négatif). L’article de Rubine ne traite pas ce cas, donc mon calcul de [$\theta_p$](src/geste/Trace.java#L78) diffère dans mon code.

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

