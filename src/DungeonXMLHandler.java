package src;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Stack;

public class DungeonXMLHandler extends DefaultHandler {

    // the two lines that follow declare a DEBUG flag to control
    // debug print statements and to allow the class to be easily
    // printed out.  These are not necessary for the parser.
    private static final int DEBUG = 1;
    private static final String CLASSID = "DungeonXMLHandler";

    // data can be called anything, but it is the variables that
    // contains information found while parsing the xml file
    private StringBuilder data = null;

    // When the parser parses the file it will add references to
    // Student objects to this array so that it has a list of 
    // all specified students.  Had we covered containers at the
    // time I put this file on the web page I would have made this
    // an ArrayList of Students (ArrayList<Student>) and not needed
    // to keep tract of the length and maxStudents.  You should use
    // an ArrayList in your project.
    private Dungeon dungeon;
  

    // The XML file contains a list of Students, and within each 
    // Student a list of activities (clubs and classes) that the
    // student participates in.  When the XML file initially
    // defines a student, many of the fields of the object have
    // not been filled in.  Additional lines in the XML file 
    // give the values of the fields.  Having access to the 
    // current Student and Activity allows setters on those 
    // objects to be called to initialize those fields.
    private Room roomBeingParsed = null;
    private Passage passageBeingParsed = null;
    private Player playerBeingParsed = null;
    private Monster monsterBeingParsed = null;
    private Scroll scrollBeingParsed = null;
    private Armor armorBeingParsed = null;
    private Sword swordBeingParsed = null;
    private CreatureAction creatureActionBeingParsed = null;
    private ItemAction itemActionBeingParsed = null;


    private Stack <String> stackType = new Stack <String> ();
    

    // The bX fields here indicate that at corresponding field is
    // having a value defined in the XML file.  In particular, a
    // line in the xml file might be:
    // <instructor>Brook Parke</instructor> 
    // The startElement method (below) is called when <instructor>
    // is seen, and there we would set bInstructor.  The endElement
    // method (below) is called when </instructor> is found, and
    // in that code we check if bInstructor is set.  If it is,
    // we can extract a string representing the instructor name 
    // from the data variable above.
    private boolean bVisible = false;
    private boolean bPosX = false;
    private boolean bPosY = false;
    private boolean bWidth = false;
    private boolean bHeight = false;
    private boolean bHp = false;
    private boolean bMaxHit = false;
    private boolean bHpMoves = false;
    private boolean bType = false;
    private boolean bItemIntValue = false;
    private boolean bActionIntValue = false;
    private boolean bActionCharValue = false;
    private boolean bActionMessage = false;
    

    // Used by code outside the class to get the list of Student objects
    // that have been constructed.
    public Dungeon getDungeon() {
        return dungeon;
    }

    // A constructor for this class.  It makes an implicit call to the
    // DefaultHandler zero arg constructor, which does the real work
    // DefaultHandler is defined in org.xml.sax.helpers.DefaultHandler;
    // imported above, and we don't need to write it.  We get its 
    // functionality by deriving from it!
    public DungeonXMLHandler() {
    }

     // The important methods to override are startElement(), endElement() and characters().

    // SAXParser starts parsing the document, when any start element is found, startElement() method is called. 
    // We are overriding this method to set boolean variables that will be used to identify the element.
    // We are also using this method to create a new object every time object start element is found. 
    // Check how id attribute is read here to set the Employee Object id field.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (DEBUG > 1) {
            System.out.println(CLASSID + ".startElement qName: " + qName);
        }

        if(qName.equalsIgnoreCase("Dungeon"))
        {
            String name = attributes.getValue("name");
            int width = Integer.parseInt(attributes.getValue("width"));
            int topHeight = Integer.parseInt(attributes.getValue("topHeight"));
            int gameHeight = Integer.parseInt(attributes.getValue("gameHeight"));
            int bottomHeight = Integer.parseInt(attributes.getValue("bottomHeight"));

            dungeon =  Dungeon.getDungeon(name, width, gameHeight);
            dungeon.setTopHeight(topHeight);
            dungeon.setBottomHeight(bottomHeight);

            stackType.push("Dungeon");
        }
        else if (qName.equalsIgnoreCase("Rooms"))
        {
            // not sure? 
        }
        else if (qName.equalsIgnoreCase("Room"))
        {
            int roomID = Integer.parseInt(attributes.getValue("room"));
            
            Room room = new Room(roomID);
            roomBeingParsed = room;

            stackType.push("Room");
        }
        else if (qName.equalsIgnoreCase("Passages"))
        {
            // not sure
        }
        else if (qName.equalsIgnoreCase("Passage"))
        {
            int room1 = Integer.parseInt(attributes.getValue("room1"));
            int room2 = Integer.parseInt(attributes.getValue("room2"));

            Passage passage = new Passage(room1, room2);
            passageBeingParsed = passage;

            stackType.push("Passage");
        }
        else if (qName.equalsIgnoreCase("Player"))
        {
            String name = attributes.getValue("name");
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));

            Player player = new Player(name, room, serial);
            playerBeingParsed = player;

            stackType.push("Player");
        }
        else if (qName.equalsIgnoreCase("Monster"))
        {
            String name = attributes.getValue("name");
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            
            Monster monster = new Monster(name,room,serial);
            monsterBeingParsed = monster;

            stackType.push("Monster");
        }
        else if (qName.equalsIgnoreCase("Scroll"))
        {
            String name = attributes.getValue("name");
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            
            Scroll scroll = new Scroll(name,room,serial);
            scrollBeingParsed = scroll;

            stackType.push("Scroll");
        }
        else if (qName.equalsIgnoreCase("Sword"))
        {
            String name = attributes.getValue("name");
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            
            Sword sword = new Sword(name,room,serial);
            swordBeingParsed = sword;

            stackType.push("Sword");
        }
        else if (qName.equalsIgnoreCase("Armor"))
        {
            String name = attributes.getValue("name");
            int room = Integer.parseInt(attributes.getValue("room"));
            int serial = Integer.parseInt(attributes.getValue("serial"));
            
            Armor armor = new Armor(name,room,serial);
            armorBeingParsed = armor;

            stackType.push("Armor");
        }

        else if (qName.equalsIgnoreCase("CreatureAction"))
        {
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            CreatureAction creatureAction = new CreatureAction(name, type);
            switch(name) {
                case "Teleport":
                    creatureAction = new Teleport(name,type);
                    break;
                case "Remove":
                    creatureAction = new Remove(name,type);
                    break;
                case "YouWin":
                    creatureAction = new YouWin(name,type);
                    break;
                case "UpdateDisplay":
                    creatureAction = new UpdateDisplay(name,type);
                    break;
                case "EndGame":
                    creatureAction = new EndGame(name,type);
                    break;
                case "ChangeDisplayedType":
                    creatureAction = new ChangeDisplayedType(name,type);
                    break;
                case "DropPack":
                    creatureAction = new DropPack(name,type);
                    break;
            }
            creatureActionBeingParsed = creatureAction;


            stackType.push("CreatureAction");
        }

        else if (qName.equalsIgnoreCase("ItemAction"))
        {
            String name = attributes.getValue("name");
            String type = attributes.getValue("type");
            ItemAction itemAction = new ItemAction(name, type);
            switch(name) {
                case "BlessCurseOwner":
                    itemAction = new BlessCurseOwner(name, type);
                    break;
                case "Hallucinate":
                    itemAction = new Hallucinate(name, type);
            }
            itemActionBeingParsed = itemAction;
            
            stackType.push("ItemAction");
        }
     
     
     
        else if (qName.equalsIgnoreCase("visible"))         bVisible        = true;
        else if (qName.equalsIgnoreCase("posX"))            bPosX           = true;
        else if (qName.equalsIgnoreCase("posY"))            bPosY           = true;
        else if (qName.equalsIgnoreCase("width"))           bWidth          = true;
        else if (qName.equalsIgnoreCase("height"))          bHeight         = true;
        else if (qName.equalsIgnoreCase("hp"))              bHp             = true;
        else if (qName.equalsIgnoreCase("maxhit"))          bMaxHit         = true;
        else if (qName.equalsIgnoreCase("hpMoves"))         bHpMoves        = true;
        else if (qName.equalsIgnoreCase("type"))            bType           = true;
        else if (qName.equalsIgnoreCase("ItemIntValue"))    bItemIntValue   = true;
        else if (qName.equalsIgnoreCase("actionIntValue"))  bActionIntValue = true;
        else if (qName.equalsIgnoreCase("actionCharValue")) bActionCharValue= true;
        else if (qName.equalsIgnoreCase("actionMessage"))   bActionMessage  = true;

        else {
            System.out.println("Unknown qname: " + qName);
        }


        data = new StringBuilder();
        
    }

    // The endElement() is the place where we use the StringBuilder data to set employee object properties 
    // and add Employee object to the list whenever we found Employee end element tag.
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // Course course;
        
        if(bVisible)
        {
            //peep the stack
            switch(stackType.peek()) 
            {
                case "Room":
                    roomBeingParsed.setVisible();
                    break;
                case "Passage":
                    passageBeingParsed.setVisible();
                    break; 
                case "Player":
                    playerBeingParsed.setVisible();
                    break;
                case "Monster":
                    monsterBeingParsed.setVisible();
                    break;
                case "Scroll":
                    scrollBeingParsed.setVisible();
                    break;
                case "Armor":
                    armorBeingParsed.setVisible();
                    break;
                case "Sword":
                    swordBeingParsed.setVisible();
                    break;
            }
            bVisible = false;
        }

        else if(bPosX)
        {
            switch(stackType.peek()) 
            {
                case "Room":
                    roomBeingParsed.setPosX(Integer.parseInt(data.toString()));
                    break;
                case "Passage":
                    passageBeingParsed.setPosX(Integer.parseInt(data.toString()));
                    break; 
                case "Player":
                    playerBeingParsed.setPosX(Integer.parseInt(data.toString()));
                    break;
                case "Monster":
                    monsterBeingParsed.setPosX(Integer.parseInt(data.toString()));
                    break;
                case "Scroll":
                    scrollBeingParsed.setPosX(Integer.parseInt(data.toString()));
                    break;
                case "Armor":
                    armorBeingParsed.setPosX(Integer.parseInt(data.toString()));
                    break;
                case "Sword":
                    swordBeingParsed.setPosX(Integer.parseInt(data.toString()));
                    break;
            }
            bPosX = false;
        }
        else if(bPosY) 
        {
            switch(stackType.peek()) 
            {
                case "Room":
                    roomBeingParsed.setPosY(Integer.parseInt(data.toString()));
                    break;
                case "Passage":
                    passageBeingParsed.setPosY(Integer.parseInt(data.toString()));
                    break; 
                case "Player":
                    playerBeingParsed.setPosY(Integer.parseInt(data.toString()));
                    break;
                case "Monster":
                    monsterBeingParsed.setPosY(Integer.parseInt(data.toString()));
                    break;
                case "Scroll":
                    scrollBeingParsed.setPosY(Integer.parseInt(data.toString()));
                    break;
                case "Armor":
                    armorBeingParsed.setPosY(Integer.parseInt(data.toString()));
                    break;
                case "Sword":
                    swordBeingParsed.setPosY(Integer.parseInt(data.toString()));
                    break;
            }
            bPosY = false;
        }
        else if(bWidth)
        {
            switch(stackType.peek()) 
            {
                case "Room":
                    roomBeingParsed.setWidth(Integer.parseInt(data.toString()));
                    break;
                case "Passage":
                    passageBeingParsed.setWidth(Integer.parseInt(data.toString()));
                    break; 
                case "Player":
                    playerBeingParsed.setWidth(Integer.parseInt(data.toString()));
                    break;
                case "Monster":
                    monsterBeingParsed.setWidth(Integer.parseInt(data.toString()));
                    break;
                case "Scroll":
                    scrollBeingParsed.setWidth(Integer.parseInt(data.toString()));
                    break;
                case "Armor":
                    armorBeingParsed.setWidth(Integer.parseInt(data.toString()));
                    break;
                case "Sword":
                    swordBeingParsed.setWidth(Integer.parseInt(data.toString()));
                    break;
            }
            bWidth = false;
        }
        else if(bHeight)
        {
            switch(stackType.peek()) 
            {
                case "Room":
                    roomBeingParsed.setHeight(Integer.parseInt(data.toString()));
                    break;
                case "Passage":
                    passageBeingParsed.setHeight(Integer.parseInt(data.toString()));
                    break; 
                case "Player":
                    playerBeingParsed.setHeight(Integer.parseInt(data.toString()));
                    break;
                case "Monster":
                    monsterBeingParsed.setHeight(Integer.parseInt(data.toString()));
                    break;
                case "Scroll":
                    scrollBeingParsed.setHeight(Integer.parseInt(data.toString()));
                    break;
                case "Armor":
                    armorBeingParsed.setHeight(Integer.parseInt(data.toString()));
                    break;
                case "Sword":
                    swordBeingParsed.setHeight(Integer.parseInt(data.toString()));
                    break;
            }
            bHeight = false;
        }
        else if(bHp)
        {
            switch(stackType.peek()) 
            {
                case "Player":
                    playerBeingParsed.setHp(Integer.parseInt(data.toString()));
                    break;
                case "Monster":
                    monsterBeingParsed.setHp(Integer.parseInt(data.toString()));
                    break;
            }
            bHp = false;
        }

        else if(bMaxHit)
        {
            switch(stackType.peek()) 
            {
                case "Player":
                    playerBeingParsed.setMaxHit(Integer.parseInt(data.toString()));
                    break;
                case "Monster":
                    monsterBeingParsed.setMaxHit(Integer.parseInt(data.toString()));
                    break;
            }
            bMaxHit = false;
        }
        
        else if(bHpMoves)
        {
            switch(stackType.peek()) 
            {
                case "Player":
                    playerBeingParsed.setHpMoves(Integer.parseInt(data.toString()));
                    break;
                case "Monster":
                    monsterBeingParsed.setHpMoves(Integer.parseInt(data.toString()));
                    break;
            }

            bHpMoves = false;
        }
        else if(bType)
        {
            switch(stackType.peek()) 
            {
                case "Room":
                    roomBeingParsed.setType((data.toString().charAt(0)));
                    break;
                case "Passage":
                    passageBeingParsed.setType((data.toString().charAt(0)));
                    break; 
                case "Player":
                    playerBeingParsed.setType((data.toString().charAt(0)));
                    break;
                case "Monster":
                    monsterBeingParsed.setType((data.toString().charAt(0)));
                    break;
                case "Scroll":
                    scrollBeingParsed.setType((data.toString().charAt(0)));
                    break;
                case "Armor":
                    armorBeingParsed.setType((data.toString().charAt(0)));
                    break;
                case "Sword":
                    swordBeingParsed.setType((data.toString().charAt(0)));
                    break;
            }

            bType = false;
        }
        else if(bItemIntValue)
        {
            switch(stackType.peek()) 
            {
                case "Scroll":
                    scrollBeingParsed.setIntValue(Integer.parseInt(data.toString()));
                    break;
                case "Armor":
                    armorBeingParsed.setIntValue(Integer.parseInt(data.toString()));
                    break;
                case "Sword":
                    swordBeingParsed.setIntValue(Integer.parseInt(data.toString()));
                    break;
            }
            bItemIntValue = false;
        }
        else if(bActionIntValue)
        {
            switch(stackType.peek()) 
            {
                case "CreatureAction":
                    creatureActionBeingParsed.setIntValue(Integer.parseInt(data.toString()));
                    break;
                case "ItemAction":
                    itemActionBeingParsed.setIntValue(Integer.parseInt(data.toString()));
                    break;
            }
            bActionIntValue = false;
        }
        else if(bActionCharValue)
        {
            switch(stackType.peek()) 
            {
                case "CreatureAction":
                    creatureActionBeingParsed.setCharValue((data.toString().charAt(0)));
                    break;
                case "ItemAction":
                    itemActionBeingParsed.setCharValue((data.toString().charAt(0)));
                    break;
            }
            bActionCharValue = false;
        }
        else if(bActionMessage)
        {
            switch(stackType.peek()) 
            {
                case "CreatureAction":
                    creatureActionBeingParsed.setMessage(data.toString());
                    break;
                case "ItemAction":
                    itemActionBeingParsed.setMessage(data.toString());
                    break;
            }
            bActionMessage = false;
        }

        if(qName.equalsIgnoreCase("Rooms"))
        {
            //ignore
        }
        else if(qName.equalsIgnoreCase("Room"))
        {
            stackType.pop();
            dungeon.addRoom(roomBeingParsed);
            roomBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Passages"))
        {
            //ignore
        }
        else if(qName.equalsIgnoreCase("Passage"))
        {
            stackType.pop();
            dungeon.addPassage(passageBeingParsed);
            passageBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Player"))
        {
            stackType.pop();
            roomBeingParsed.setCreature(playerBeingParsed);
            playerBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Monster"))
        {
            stackType.pop();
            roomBeingParsed.setCreature(monsterBeingParsed);
            monsterBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Scroll"))
        {
            stackType.pop();
            roomBeingParsed.addItemToList(scrollBeingParsed);
            scrollBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Armor"))
        {
            stackType.pop();
            switch(stackType.peek()) {
                case "Player":
                    playerBeingParsed.addItemToList(armorBeingParsed);
                    break;
                case "Room":
                    roomBeingParsed.addItemToList(armorBeingParsed);
            }
            armorBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("Sword"))
        {
            stackType.pop();
            switch(stackType.peek()) {
                case "Player":
                    playerBeingParsed.addItemToList(swordBeingParsed);
                    break;
                case "Room":
                    roomBeingParsed.addItemToList(swordBeingParsed);
            }
            swordBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("CreatureAction"))
        {
            stackType.pop();
            switch (stackType.peek()) {
                case "Monster":
                    if(creatureActionBeingParsed.getType().equals("hit")) {
                        monsterBeingParsed.setHitAction(creatureActionBeingParsed);
                    }
                    else {
                        monsterBeingParsed.setDeathAction(creatureActionBeingParsed);
                    }
                break;
                case "Player":
                    if(creatureActionBeingParsed.getType().equals("hit")) {
                       playerBeingParsed.setHitAction(creatureActionBeingParsed);
                    }
                    else {
                        playerBeingParsed.setDeathAction(creatureActionBeingParsed);
                    }
                break;

            }
            creatureActionBeingParsed = null;
        }
        else if(qName.equalsIgnoreCase("ItemAction"))
        {
            stackType.pop();
            switch (stackType.peek()) {
                case "Scroll":
                scrollBeingParsed.setItemAction(itemActionBeingParsed);
                break;
            }
            itemActionBeingParsed = null;
        }
        
    
    }


    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
        if (DEBUG > 1) {
            System.out.println(CLASSID + ".characters: " + new String(ch, start, length));
            System.out.flush();
        }
    }

}
