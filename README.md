# IotButtonProject
Utilisation d'un bouton AWS comme télécommande.

La partie Server représente le code qui sera déployé sur un serveur externe( Amazon par exemple).
Cette partie permet de récupérer les données que le bouton AWS envoie lors d'un click et de retransmettre ces données directement aux clients connectés.

La partie Client est déployé sur l'ordinateur de l'utilisateur.
Lorsqu'un client reçoit un message du serveur, il exécute des commandes en fonction du type de message. 

Le code Client est utilisable seulement sur Linux car nous utilisons "xdotool" pour simuler l'appui de touche.


