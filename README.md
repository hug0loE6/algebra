# Compte rendu du TP noté HAI702I IASD - LOPEZ Hugo
src ennoncé = https://www.lirmm.fr/~mountaz/Ens/Alg2025/tp2/
## Exercice 1: Calcul des features pour chaque tracé

Le calcul des 13 features proposé dans l'article de Rubine sont dans la fonction d'initialisation d'un tracé : (pour calcul $\theta_p$ préciser changement //TODO)
[initFeatures() de Trace](src/geste/Trace.java#L37)

Et une méthode permettant de retourner ces 13 valeurs :
[getFeatureVector()](src/geste/Trace.java#L119)

## Exercice 2: Calcul de l'espérance et de la covariance pour chaque geste, en utilisant les données d'entrainement
