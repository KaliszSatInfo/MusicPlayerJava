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
4. Jednoduchým kliknutím na položku v tabulce se zpustí hudba a s tou se dá pomocí, tlačítek a sliderů ve spodní polovině manipulovat (stopnout, znova zpustit, upravit hlasitost, posunout se ve skladbě, nebo přepínat skladby tlačítky doleva a doprava)

## Obecný přehled a popis aplikace

Jedná se o hudební přehrávač s grafickým uživatelským rozhraním (GUI) napsaný v Javě. Do tzv. playlistu mohou uživatelé přidávat audio soubory formátu **WAV** a **OGG**. Tento playlist je zobrazen v tabulce, která je zobrazena v horní části grafického rozhraní. Aplikace samozřejmě umožňuje přehrávat, zastavovat, opakovat, měnit hlasitost a také se pohybovat v rámci daného audio souboru.

## Popsání metod a jak co funguje (teď nemám čas, dodělám potom)

Ĺ̴͉̠̀ö̷̧̳́r̸̠͉̔ȩ̴̓̌m̸̜͛ ̷̑͜i̷̢̗͊̑ṗ̸͓͔s̸̟̓̚u̵͈̝̇͝m̸̖̋ ̴̩̬͌̆d̶̢̹͛o̸͎͆͂l̸͚̯̆̒o̸͇̽͊r̸͚̠̄͂ ̸̙̏s̵̼̓̉͜ī̸̜t̸͕̪̂ ̴͈̾ȃ̶̧̀m̸͍̎ẻ̴͍t̷̩̞̉,̶̭͎͂ ̷͖̒̿c̴̹̬̒̐o̶̳̽̕n̵̞̊̐ş̶̥̓̌ë̶̞́͠c̸͉͗t̷̩̽́e̴̞͐͊t̴̢̠͌ǔ̴̬͝ṛ̷̓ ̶͕̕ằ̵̗ͅd̸̡̗̔ï̴̹͠p̷̀ͅi̶̹̕s̷͖͌c̵͚͊i̵͎͋̿n̵̢͝g̴̛̥̈ ̸̬̮̄̉é̴͇̍l̴̙͇͛͌i̶̜͘t̸͈̿͂.̷̥̅̚ ̷̢̓͝D̸̯̪͛ù̶̦̺ī̸̢͗͜s̴̺̒ ̴̭́͋q̷͍̎̅u̵͉̜͋́i̶̮̺͑s̸̲̺̋͝ ̸̳͊n̴̡̉u̶̘͆l̵̤̽l̶̦̈̍ạ̷̿́ ̵̩̏p̶̞̿͗ǔ̸̠̔l̷͍͈͑v̷͍̹̔͐i̸͇̯̋n̴̯̓ą̴̯͐̏ṙ̴͎͎,̶̢̼̌̕ ̵̧̧͆͘ǵ̸̲r̴͉̀̊à̵̱͠v̵͇͙͋i̸̞̦̓d̵͓̣̅à̶̲͑ͅ ̴̮̯̄̈j̶̪̕ú̶̯͒s̶͉͝t̴̰͘ò̶͖̚ ̵̤͛͝ḛ̴̿ļ̷̈́ë̷͉̼́m̴͈̌e̸̖̠̊n̷͓̉ṭ̴͍̈u̶̫͗͛m̴̨̺͘͝,̴̡͍͂ ̸̮͉̎d̷̗͎͐ḁ̵̍͗p̶̘̠̔ì̶̹͛b̵̭̺͑̇u̸͇͐͌s̵͉̄̑ ̸͍̓͂n̷̡͌i̵̧̋s̷̮̙̿î̶̖͓.̶̭͐ ̸̗̂O̷̡̗̎r̵̦͖̀̑ĉ̷͍͗ȋ̵̮̗ ̵̭̤̇͊v̸̩́ả̷̤r̶͈̞̾i̸̪͌̎u̸̺͝s̶̤̪͊͝ ̷̧̛̪̓ņ̸͠å̷̺̜̍t̶͈͗o̶̥̓͐q̶̘̂̂u̷͎͓̾e̸̺͐̈ ̸͉͑̌p̶̺̬̍̃e̸͉͎͊n̷̦̓a̷̢̓t̶̤̯̏i̶̡̼̊͝b̸̼̱͛͗u̴̯̇ṡ̷̢̃ ̸̮͎̃e̷͍͓̔̕t̷̢͉̾ ̸͉̟̍m̷̗͓̔̎a̵̹̮͘g̶̱̊̈́n̷̙̎i̵̥̫̇͌s̵̹̩̓̔ ̶̤̎͐ḍ̴̜̚î̷͙s̷͎̬̾͝ ̵͈̿p̵̠͕͊a̴̰͓͌r̸̨͊̈ẗ̶͔̱u̸̗͋r̸͉̍̊i̸̼̙͆̈́e̵͍͆͜n̵͚̑t̷̗͒̊ ̷̮͒m̶̲̹̃o̴̱͙̐n̴̬̈́̎t̷̤̱̅ē̴̙̬s̷̙̥̐͌,̶̬̏ ̶̼̂̀n̸̻̏́͜ǎ̸̡͎ş̶̳̒̋c̴̺͒ẻ̴͙͌t̶̤͝u̷̖͈͂ṟ̴̉̓ ̵͎̣̒̇r̴̪̎i̵͖̙̓̈́d̶͙͌í̴͚̓c̵̼͍̊͘ư̸̝͓̽l̸̗̻̐̐u̷͕͈̒s̸̭̜̎ ̸̯̓̕m̵̼͎͑u̸͔͚͛s̶͉̕.̵̛͖̗̿ Ṕ̸̧̨̅h̴͙͂̚ặ̶s̸͓̣̉̋e̴̪̻͛̐l̴͎̄l̵̦̍u̴̢̕ş̸̀ ̷̏̐ͅd̴̰̚ő̴̼ḽ̶͇͐ô̸͚r̴̺̉́ ̵͔͓̀v̷͙̦͛̏e̸͇̝͋́l̸̦̬̉i̴̥͒̀t̵̬͚̓,̶̼̾͠ͅ ̶̜̙̑͠v̶̛͓ė̷̬̻̀s̵͓̺͐̋ţ̵̟͌í̷̩̼b̵̰̗͛u̷̳̍̓ļ̵̈́u̵͍͇̿͊m̸͓͎̔ ̶͍̺̅̏n̵̪͈͘͠e̷̥̚͜c̵̟̝̆̀ ̵͈̦̃̉ẻ̸̖̒g̴͙̓͌è̴̳͘ͅs̷̗̎͋t̵͉͛a̴͔͒̿s̶͕̀ ̸͕̃̀q̴͈͈̈̐u̴͙̐͘ì̶̢̭̆s̵͍͔͂,̵̥̉̔ ̴̗͎́c̶̟̳̄o̶̤̜̚̚ñ̶̮̠s̵͓̽́ĕ̵̺͝ć̴̮t̷̢̟̾̐ẹ̴͓͐͂t̸̻͋u̸̯̒̇ř̵̡͕ ̵̛̩a̵̙̐t̵̬̿ ̴̽̚͜n̶̹̘͛i̶͙̝͂s̶̝̔i̸̊͜.̴̤̄̃
