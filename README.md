# MusicPlayerJava dokumentace

*Autoři: Jan Kalisz a Jan Fryšták*

## Využité nástroje

SDK: `openjdk-22 java version "22.0.1"` <br />
Language level: `X - Experimental features` <br />
Vývojářské prostředí: `IntelliJ IDEA 2023.3.6. (Community Edition); Build #IC-233.15026.9` <br />

## Jak aplikaci zprovoznit

1. Naklonujte projekt do IntelliJ
2. Spusťte metodu `main` ve třídě `MusicPlayerForm`
3. Skrz `Add folder` v menu můžete přidat zdroje ze kterých má aplikace čerpat soubory, druhým tlačítkem `Delte all`se tento seznam dá smazat
4. Jednoduchým kliknutím na položku v tabulce se zpustí hudba a s tou se dá pomocí, tlačítek a sliderů ve spodní polovině manipulovat (stopnout, znova zpustit, upravit hlasitost, posunout se ve skladbě, nebo přepínat mezi soubory tlačítky doleva a doprava)

## Obecný přehled a popis aplikace

Jedná se o hudební přehrávač s grafickým uživatelským rozhraním (GUI) napsaný v Javě. Do tzv. playlistu mohou uživatelé přidávat audio soubory formátu **WAV** a **OGG**. Tento playlist je zobrazen v tabulce, která je v horní části grafického rozhraní. S hrajícím audio souborem se dá manipulovat různými způsoby (stopnout, znova zpustit, upravit hlasitost, posunout se ve skladbě, nebo přepínat mezi soubory tlačítky doleva a doprava)

## Třídy a metody umožňující fungování

### MusicPlayer
Obsahuje metody na samotné přehrávání `play()`, stopování `stop()`. Obecně načítá hudbu do paměti počítače `load`. Umožňuje tzv loopování `ìsLoop` a `setLoop`. Nachází se v ní i logika na ovládání hlasitosti hudby `setVolume`.

### MusicPlayerForm
V této tříde jsou všechny komponenty grafického rozhraní a jejich propojení s logikou třídy `MusicPlayer`. Jsou zde také metody pro načítání seznamu skladeb do aplikace `filesChooser()` a `processFolder`, jejich načtení do seznamu `readMemory()` a také ukládání do souboru, ze kterého bude aplikace načítat skladby po zavření a znovu-otevření aplikace `writeToMemory`.

### MySong
Třída určená na číslování a získávání jmen skladeb a tudíž pro jejich zobrazení v tabulce `songTable`.
