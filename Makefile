all:
	javac src/game/Rogue.java

badScroll.xml: all
	java src.game.Rogue src/xmlFiles/badScroll.xml

death.xml: all
	java src.game.Rogue src/xmlFiles/death.xml

dropPack.xml: all
	java src.game.Rogue src/xmlFiles/dropPack.xml

dungeon.xml: all
	java src.game.Rogue src/xmlFiles/dungeon.xml

hallucinate.xml: all
	java src.game.Rogue src/xmlFiles/hallucinate.xml

testDrawing.xml: all
	java src.game.Rogue src/xmlFiles/testDrawing.xml

wear.xml: all
	java src.game.Rogue src/xmlFiles/wear.xml


.PHONY: clean
clean:
	rm -f  asciiPanel/*.class src/*.class src/game/*.class Step2Example/asciipanelexample/*.class
