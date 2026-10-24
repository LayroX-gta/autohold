# AutoHold (Fabric 26.2, client)

- **K** : ouvre le panneau (avancer auto / maintenir clic gauche)
- **G** : active/désactive l'avance auto
- **H** : active/désactive le clic gauche maintenu
(touches modifiables dans Options > Commandes > AutoHold)

## Obtenir le .jar
**GitHub (sans rien installer)** : crée un dépôt, envoie tout ce dossier,
onglet *Actions* → *build* → télécharge l'artefact `autohold-jar`
(prends le fichier `autohold-1.0.0.jar`, pas `-sources`).
**Local** : Java 25 + Gradle 9.5.1 → `gradle build` → `build/libs/`.

Mets le .jar dans `.minecraft/mods` avec Fabric Loader 0.19.3+ et Fabric API pour 26.2.
