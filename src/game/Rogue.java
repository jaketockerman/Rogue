package src.game;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import src.DungeonXMLHandler;
import src.Dungeon;



public class Rogue {


    public Dungeon parserGetDungeon(String fileName)
    {
        // Create a saxParserFactory, that will allow use to create a parser
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        Dungeon dungeon = null;

        try 
        {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DungeonXMLHandler handler = new DungeonXMLHandler();
            saxParser.parse(new File(fileName), handler);
            dungeon = handler.getDungeon();
        } 
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace(System.out);
        }

        return dungeon;
    }

    

    public static void main(String[] args) throws Exception {

        // check if a filename is passed in.  If not, print a usage message.
        // If it is, open the file
        String fileName = null;
        switch (args.length) {
        case 1:
            fileName = args[0];
            break;
        default:
            System.out.println("java src.Test <xmlfilename>");
            return;
        }

        Rogue rogue = new Rogue();

        //get the dungeon
        Dungeon dungeon = rogue.parserGetDungeon(fileName);
        
        if(dungeon == null)
        {
            System.out.println("Something went wrong while parsing");
            return;
        }

        ObjectDisplayGrid displayGrid = ObjectDisplayGrid.getObjectDisplayGrid(dungeon.getGameHeight(),dungeon.getWidth(), dungeon.getTopHeight(),dungeon.getBottomHeight());
        
        
        Thread keyStrokeCommandThread = new Thread(new KeyStrokeCommand(displayGrid));
        keyStrokeCommandThread.start();

        keyStrokeCommandThread.join();


    }
	
    
}
