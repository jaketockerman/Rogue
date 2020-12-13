 package src.game;


import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import asciiPanel.AsciiPanel;
import src.*;



/**
* <h2>ObjectDisplayGrid</h2>
* The program will initialize the dungeon by adding
* Char to AsciiPanel and display on GUI
* <p>
*
* @author  Chok Yip Lau
* @version 1.1
* @since   2020-10-31
*/


public class ObjectDisplayGrid extends JFrame implements KeyListener, InputSubject
{

    private static final int DEBUG = 0;
    private static final String CLASSID = ".ObjectDisplayGrid";

    private static ObjectDisplayGrid uniqueObjectDisplayGrid; //the unique ObjectDisplayGrid
    private int width;
    private int gameHeight;
    private int topHeight;
    private int bottomHeight;

    // added for step 2
    private static AsciiPanel gameWindow;
    // private List <List<Stack<Char>>> objectGrid = new ArrayList<>();
    private List <List<Stack<Char>>> objectGrid = new ArrayList<>();
    private List <List<Displayable>> roomsPassagePointer = new ArrayList<>();
    private Dungeon dungeon;
    private List<InputObserver> inputObservers = new ArrayList<>();

    

    // added for step 3
    private HashSet<Character> monstersSet = new HashSet<>();
    private Player player;
    int core = 0;

    // added for step 4
    private Set<Character> usedCharacterSet = new HashSet<>();
    private boolean isHallucinated = false;
    Random random = new Random();


    private ObjectDisplayGrid(int _gameHeight, int _width, int _topHeight, int _bottomHeight)
    {   
        gameHeight = _gameHeight;
        width = _width;
        topHeight = _topHeight;
        bottomHeight = _bottomHeight;

        gameWindow = new AsciiPanel(width, (topHeight + gameHeight + bottomHeight));

        // put in dumb value, should be initialized at this point
        dungeon = Dungeon.getDungeon("dumbVal", 0, 0);


        for(int i = 0; i < width; i++)
        {
            List <Displayable> displayableRow = new ArrayList<>();
            List <Stack<Char>> charRow = new ArrayList<>();

            for(int j = 0; j < (topHeight + gameHeight+ bottomHeight) ; j++)
            {
                charRow.add(new Stack<Char>());
                displayableRow.add(new Displayable());
            }

            objectGrid.add(charRow);
            roomsPassagePointer.add(displayableRow);
        }

        initializeDisplay();

         
        super.add(gameWindow);
        super.setSize(width * 9, (topHeight + gameHeight + bottomHeight) * 17);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
        super.setTitle(dungeon.getName());
        gameWindow.setVisible(true);
        super.addKeyListener(this);
        super.repaint();

    }

    //singleton function
    public static ObjectDisplayGrid getObjectDisplayGrid(int _gameHeight, int _width, int _topHeight, int _bottomHeight)
    {   
        if(uniqueObjectDisplayGrid == null)
        {
            uniqueObjectDisplayGrid = new ObjectDisplayGrid(_gameHeight,_width,_topHeight, _bottomHeight);
        }
        
        return uniqueObjectDisplayGrid;
        
    }

    public void setTopMessageHeight(int _topHeight)
    {
        topHeight = _topHeight ;
    }


    @Override
    public void registerInputObserver(InputObserver observer) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".registerInputObserver " + observer.toString());
        }
        inputObservers.add(observer);
    }

    private void notifyInputObservers(char ch) {
        for (InputObserver observer : inputObservers) {
            observer.observerUpdate(ch);
            if (DEBUG > 0) {
                System.out.println(CLASSID + ".notifyInputObserver " + ch);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".keyTyped entered" + e.toString());
        }
        KeyEvent keypress = (KeyEvent) e;
        notifyInputObservers(keypress.getKeyChar());
    }

     // we have to override, but we don't use this
     @Override
     public void keyPressed(KeyEvent e) {
     }
 
     // we have to override, but we don't use this
     @Override
     public void keyReleased(KeyEvent e) {
     }


     /*
        This function will draw out the room and creatures inside room
     */
     private void drawOutRoom()
     {
         // put in dumb value, should be initialized at this point
        Char horizontalLineChar = new Char('-');
        Char verticalLineChar = new Char('|');
        Char dotChar = new Char('.');
        
        usedCharacterSet.add(horizontalLineChar.getChar());
        usedCharacterSet.add(verticalLineChar.getChar());
        usedCharacterSet.add(dotChar.getChar());


        // draw out each room
        for( Room eachRoom : dungeon.getRoom() )
        {
            int roomAbsX = eachRoom.getPosX().get(0);
            int roomAbsY = eachRoom.getPosY().get(0) + topHeight;
            int roomWidth = eachRoom.getWidth();
            int roomHeight = eachRoom.getHeight();


            for( int y = roomAbsY; y < roomAbsY + roomHeight; y++)
            {
                for(int x = roomAbsX ; x < roomAbsX + roomWidth ; x++ )
                {
                    addObjectToDisplay(dotChar, x, y); 
                    roomsPassagePointer.get(x).set(y, eachRoom);
                }
            }

            // draw out height first
            for( int i = 0 ; i < roomHeight; i++)
            {
                addObjectToDisplay(verticalLineChar, roomAbsX,                 roomAbsY + i); 
                addObjectToDisplay(verticalLineChar, roomAbsX + roomWidth - 1, roomAbsY + i); 
            }

            // now draw out horizontal line
            for( int i = 0 ; i < roomWidth ; i++)
            {
                addObjectToDisplay(horizontalLineChar, roomAbsX + i , roomAbsY ); 
                addObjectToDisplay(horizontalLineChar, roomAbsX + i,  roomAbsY + roomHeight -1 ); 
            }
            
      
            ////////////////////////////////////////////////////
            //      Draw out Creature
            //
            ////////////////////////////////////////////////////
            for(Creature eachCreature : eachRoom.getCreatures())
            {
                if(eachCreature.getName().equals("Player"))
                {
                    int playerPosX = eachCreature.getPosX().get(0);
                    int playerPosY = eachCreature.getPosY().get(0);
                    Char atChar = new Char('@');
                    usedCharacterSet.add(atChar.getChar());
                    addObjectToDisplay(atChar, playerPosX + roomAbsX , playerPosY + roomAbsY);


                    // allocate player for this class
                    player = (Player) eachCreature;
                    setPlayerCurrentPosX(playerPosX + roomAbsX);
                    setPlayerCurrentPosY(playerPosY + roomAbsY);
                }
                else
                {
                    int monsterPosX = eachCreature.getPosX().get(0);
                    int monsterPosY = eachCreature.getPosY().get(0);
                    Char typeChar = new Char(eachCreature.getType());
                    usedCharacterSet.add(typeChar.getChar());
                    addObjectToDisplay(typeChar, monsterPosX + roomAbsX , monsterPosY + roomAbsY);

                    monstersSet.add(typeChar.getChar());
                }
            }

            ////////////////////////////////////////////////////
            //      Draw out Item
            //
            ////////////////////////////////////////////////////
            for (Item eachItem : eachRoom.getItemList())
            {
                if(eachItem instanceof Scroll)
                {
                    int scrollPosX = eachItem.getPosX().get(0);
                    int scrollPosY = eachItem.getPosY().get(0);
                    Char scrollChar = new Char('?');
                    usedCharacterSet.add(scrollChar.getChar());
                    addObjectToDisplay(scrollChar, scrollPosX + roomAbsX , scrollPosY + roomAbsY);
                }
                else if(eachItem instanceof Armor)
                {
                    int armorPosX = eachItem.getPosX().get(0);
                    int armorPosY = eachItem.getPosY().get(0);
                    Char armorChar = new Char(']');
                    usedCharacterSet.add(armorChar.getChar());
                    addObjectToDisplay(armorChar, armorPosX + roomAbsX , armorPosY + roomAbsY);
                }
                else if(eachItem instanceof Sword)
                {
                    int swordPosX = eachItem.getPosX().get(0);
                    int swordPosY = eachItem.getPosY().get(0);
                    Char swordChar = new Char(')');
                    usedCharacterSet.add(swordChar.getChar());
                    addObjectToDisplay(swordChar, swordPosX + roomAbsX , swordPosY + roomAbsY);
                }
                
            }

        }
     }


     /*
        This function will draw out the Passage inside the room
     */
     private void drawPassage()
    {
        // put in dumb value, should be initialized at this point

        Char plusChar = new Char('+');
        Char poundChar = new Char('#');

        usedCharacterSet.add(plusChar.getChar());
        usedCharacterSet.add(poundChar.getChar());
        // draw out each Passage
        for (Passage eachPassage : dungeon.getPassage() )
        {
            int posXSize = eachPassage.getPosX().size();
            int passageOldPosX = eachPassage.getPosX().get(0);
            int passageOldPosY = eachPassage.getPosY().get(0) + topHeight;

            for( int i = 1; i < posXSize; i++)
            {
                int passageNewPosX = eachPassage.getPosX().get(i);
                int passageNewPosY = eachPassage.getPosY().get(i) + topHeight;

                addObjectToDisplay(poundChar, passageOldPosX, passageOldPosY);

                if(passageOldPosX != passageNewPosX)
                {
                    if(passageNewPosX > passageOldPosX)
                    {
                        while(passageOldPosX <= passageNewPosX)
                        {
                            addObjectToDisplay(poundChar, passageOldPosX, passageOldPosY);
                            roomsPassagePointer.get(passageOldPosX).set(passageOldPosY, eachPassage);

                            passageOldPosX++;
                        }
                    }
                    else
                    {
                        while(passageOldPosX >= passageNewPosX)
                        {
                            addObjectToDisplay(poundChar, passageOldPosX, passageOldPosY);
                            roomsPassagePointer.get(passageOldPosX).set(passageOldPosY, eachPassage);

                            passageOldPosX--;
                        }
                    }
                }

                if(passageOldPosY != passageNewPosY)
                {
                    if(passageNewPosY > passageOldPosY)
                    {
                        while(passageOldPosY <= passageNewPosY)
                        {
                            addObjectToDisplay(poundChar, passageOldPosX, passageOldPosY);
                            roomsPassagePointer.get(passageOldPosX).set(passageOldPosY, eachPassage);
                            passageOldPosY++;
                        }
                    }
                    else
                    {
                        while(passageOldPosY >= passageNewPosY)
                        {
                            addObjectToDisplay(poundChar, passageOldPosX, passageOldPosY);
                            roomsPassagePointer.get(passageOldPosX).set(passageOldPosY, eachPassage);
                            passageOldPosY--;
                        }
                    }
                }

                //overwrite passageOldPosX and passageOldPosY
                passageOldPosX = passageNewPosX;
                passageOldPosY = passageNewPosY;
            }
            // change the first and last to #
            int passageFirstPosX = eachPassage.getPosX().get(0);
            int passageFirstPosY = eachPassage.getPosY().get(0) + topHeight;
            addObjectToDisplay(plusChar, passageFirstPosX, passageFirstPosY);
            int passageLastPosX = eachPassage.getPosX().get(posXSize - 1);
            int passageLastPosY = eachPassage.getPosY().get(posXSize - 1) + topHeight;
            addObjectToDisplay(plusChar, passageLastPosX, passageLastPosY);
           
        }
    }

    /**
     * This method will initialize Hp and player score
     * @param None
     * @return None
     */
    private void drawHpAndCore()
    {
        // HP:
        String hp = "HP:";
        for(int i = 0; i < hp.length(); i++)
        {
            Char hpSemicolon = new Char(hp.charAt(i));
            addObjectToDisplay(hpSemicolon, i, 0);
        }

        // playerHp start at (4,0)
        String playerHpStr = String.valueOf(player.getHp());
        for(int i = 0; i < playerHpStr.length(); i++)
        {
            Char charHp = new Char(playerHpStr.charAt(i));
            addObjectToDisplay(charHp, 4 + i, 0);
        }

        // core: start at (9,0)
        String core = "score:";

        for(int i = 0; i < core.length(); i++)
        {
            // start at position 9
            Char coreSemicolon = new Char(core.charAt(i));
            addObjectToDisplay(coreSemicolon, 9 + i, 0);
            coreSemicolon = null;
        }

        //now add zero
        Char zero = new Char('0');
        addObjectToDisplay(zero, 16, 0);
       
    }


    /**
     * This method will initialize Pack and Info character
     * @param None
     * @return None
     */
    private void drawPackandInfo()
    {
        // pack will locate at topHeight + gameHeight
        String pack = "Pack:";
        for(int i = 0; i < pack.length(); i++)
        {
            Char packSemicolon = new Char(pack.charAt(i));
            addObjectToDisplay(packSemicolon, i, topHeight + gameHeight);
        }

        // info will locate at topHeight + gameHeight + bottomHeight -1
        String info = "Info:";
        for(int i = 0; i < info.length(); i++)
        {
            Char infoSemicolon = new Char(info.charAt(i));
            addObjectToDisplay(infoSemicolon, i, topHeight + gameHeight + (bottomHeight -1 ));
        }
    }


    /*
        This function will initialize the Dungeon, Room, Creatures, Item....
    */
    private final void initializeDisplay() {
        drawOutRoom();
        drawPassage();
        drawHpAndCore();
        drawPackandInfo();
        gameWindow.repaint();
    }

    /*
        This function will add Char into the display
    */
    public void addObjectToDisplay(Char ch, int x, int y) {
        
        if ((0 <= x) && (x < objectGrid.size())) {
            if ((0 <= y) && (y < objectGrid.get(0).size())) {
                objectGrid.get(x).get(y).push(ch);
                writeToTerminal(x, y);
            }
        }

    }

    /*
        This function will add Char into the display
    */
    private void writeToTerminal(int x, int y) {
        char ch = objectGrid.get(x).get(y).peek().getChar();
        gameWindow.write(ch, x, y);
        gameWindow.repaint();

    }

    /**
     * This function will remove the character in the display
     * if the object
     */
    public void removeCharAt(int x, int y)
    {

        if(!objectGrid.get(x).get(y).isEmpty()) {

            // if it's hallucinated then we would want to remove the layer on the bottom
            if(isHallucinated)
            {
                Char popChar = objectGrid.get(x).get(y).pop();
                // have to consider if it's a score or Hp or Info or Pack
                // if it's score of Hp nothing is down there , just write 0
                if(objectGrid.get(x).get(y).isEmpty())
                {
                    gameWindow.write('\0', x, y);
                    gameWindow.repaint();
                    return;
                }
 
                objectGrid.get(x).get(y).pop();
                objectGrid.get(x).get(y).push(popChar);
                this.writeToTerminal( x,y);
            }
            else
            {
               
                objectGrid.get(x).get(y).pop();
                if(!objectGrid.get(x).get(y).isEmpty())
                {
                    this.writeToTerminal( x,y);
                }
                else
                {
                    gameWindow.write('\0', x, y);
                }
            }
        }
        else
        {
            gameWindow.write('\0', x, y);
        }

        gameWindow.repaint();
    }


    /**
     * Little hack, if we are hallucinated, pop twice to find the characters
     * @param x
     * @param y
     * @return char
     */
    public char getCharAt(int x, int y) {
        if(!objectGrid.get(x).get(y).isEmpty()) {

            if(isHallucinated)
            {
                Char popChar = objectGrid.get(x).get(y).pop();

                // should not be empty but check it anyways
                if(!objectGrid.get(x).get(y).isEmpty())
                {
                    Char returnChar = objectGrid.get(x).get(y).peek();
                    // push it back
                    objectGrid.get(x).get(y).push(popChar);

                    return returnChar.getChar();
                }
                else return '\0';

            }
            return objectGrid.get(x).get(y).peek().getChar();
        }
        else return '\0';
    }


    public void setPlayerCurrentPosX(int x)
    {
        player.removePosX(0);
        player.setPosX(x);
    }

    public void setPlayerCurrentPosY(int y)
    {
        player.removePosY(0);
        player.setPosY(y);
    }

    public int getPlayerCurrentPosX()
    {
        return player.getPosX().get(0);
    }

    public int getPlayerCurrentPosY()
    {
        return player.getPosY().get(0);
    }



    ////////////////////////////////////////////////
    //        methods for Step 3
    //
    ////////////////////////////////////////////////
    public HashSet<Character> getMonstersSet()
    {
        return monstersSet;
    }

    public Displayable returnRoomOrPassage(int absX, int absY)
    {
        return roomsPassagePointer.get(absX).get(absY);
    }

    /**
     * This method will return the hp of the player in ObjectDisplayGrid
     * 
     * @param None
     * @return playerHp (int)
     */
    public int getPlayerHp()
    {
        return player.getHp();
    }

    /**
     * This method will set the hp of the player in ObjectDisplayGrid
     * 
     * @param (int) hp
     * @return None
     */
    public void setPlayerHp(int hp)
    {
        player.setHp(hp);
    }

    /**
     * This method will return the core in ObjectDisplayGrid
     * 
     * @param None
     * @return core (int)
     */
    public int getCore()
    {
        return core;
    }

    /**
     * This method will set the core in ObjectDisplayGrid
     * 
     * @param (int) core
     * @return None
     */
    public void setCore(int _core)
    {
        core = _core;
    }

    /**
     * This method will return the Dungoen reference in ObjectDisplayGrid
     * 
     * @param None
     * @return dungeon (Dungeon)
     */
    public Dungeon getDungeon()
    {
        return dungeon;
    }

    /**
     * This method will return the player reference in ObjectDisplayGrid
     * 
     * @param None
     * @return player (Player)
     */
    public Player getPlayer()
    {
        return player;
    }


    ////////////////////////////////////////////////
    //        methods for Step 3
    //
    ////////////////////////////////////////////////

    /**
     * Special method to add character to object display grid when
     * player is hallucinated
     * 
     */
    public void addObjectToDisplayWhenHallucinated(Char ch, int x, int y) {
        //make sure the player is hallucinated
        if(!isHallucinated) return;

        if ((0 <= x) && (x < objectGrid.size())) {
            if ((0 <= y) && (y < objectGrid.get(0).size())) {
                // instead of adding to the top, adding it on the layer below
                if(!objectGrid.isEmpty())
                {
                    Char topChar = objectGrid.get(x).get(y).pop();
                    objectGrid.get(x).get(y).push(ch);
                    objectGrid.get(x).get(y).push(topChar);
                    writeToTerminal(x, y);
                }

            }
        }

    }

    /**
     * Set if the player is hallucinated
     * @return nothing
     */
    public void setHallucinated(boolean _isHallucinated)
    {
        isHallucinated = _isHallucinated;
    }
    
    /**
     * return true if the player is hallucinated
     * @return nothing
     */
    public boolean isHallucinated()
    {
        return isHallucinated;
    }

    

    /**
     * return random characters from the Set
     * @param Nothing
     * @return None
     */
    private char getRandomCharFromSet()
    {
        if(usedCharacterSet.size() == 0) return 0;
        int randomIndex = random.nextInt(usedCharacterSet.size());

        List<Character> characterList = new ArrayList<>(usedCharacterSet);
        char returnChar = characterList.get(randomIndex);
        characterList = null;

        return returnChar;
    }

    /**
     * push random characters to the room
     * 
     * @param None
     * @return None
     */
    private void pushRandomCharToRoom()
    {

        // draw out each room
        for( Room eachRoom : dungeon.getRoom() )
        {
            int roomAbsX = eachRoom.getPosX().get(0);
            int roomAbsY = eachRoom.getPosY().get(0) + topHeight;
            int roomWidth = eachRoom.getWidth();
            int roomHeight = eachRoom.getHeight();


            for( int y = roomAbsY; y < roomAbsY + roomHeight; y++)
            {
                for(int x = roomAbsX ; x < roomAbsX + roomWidth ; x++ )
                {
                    addObjectToDisplay(new Char(getRandomCharFromSet()), x, y); 
                }
            }
        }

    }

    /**
     * pop random characters from room
     * 
     * @param None
     * @return None
     */
    private void popRandomCharToRoom()
    {

        // draw out each room
        for( Room eachRoom : dungeon.getRoom() )
        {
            int roomAbsX = eachRoom.getPosX().get(0);
            int roomAbsY = eachRoom.getPosY().get(0) + topHeight;
            int roomWidth = eachRoom.getWidth();
            int roomHeight = eachRoom.getHeight();

            for( int y = roomAbsY; y < roomAbsY + roomHeight; y++)
            {
                for(int x = roomAbsX ; x < roomAbsX + roomWidth ; x++ )
                {
                    objectGrid.get(x).get(y).pop();
                    writeToTerminal(x, y);
                        
                }
            }
        }

    }

    /**
     * push random characters to the passage
     * 
     * @param None
     * @return None
     */
    private void pushRandomCharToPassage()
    {

        for (Passage eachPassage : dungeon.getPassage() )
        {
            int posXSize = eachPassage.getPosX().size();
            int passageOldPosX = eachPassage.getPosX().get(0);
            int passageOldPosY = eachPassage.getPosY().get(0) + topHeight;

            for( int i = 1; i < posXSize; i++)
            {
                int passageNewPosX = eachPassage.getPosX().get(i);
                int passageNewPosY = eachPassage.getPosY().get(i) + topHeight;

                addObjectToDisplay(new Char(getRandomCharFromSet()), passageOldPosX, passageOldPosY);

                if(passageOldPosX != passageNewPosX)
                {
                    if(passageNewPosX > passageOldPosX)
                    {
                        while(passageOldPosX <= passageNewPosX)
                        {
                            addObjectToDisplay(new Char(getRandomCharFromSet()), passageOldPosX, passageOldPosY);
                            passageOldPosX++;
                        }
                    }
                    else
                    {
                        while(passageOldPosX >= passageNewPosX)
                        {
                            addObjectToDisplay(new Char(getRandomCharFromSet()), passageOldPosX, passageOldPosY);
                            passageOldPosX--;
                        }
                    }
                }

                if(passageOldPosY != passageNewPosY)
                {
                    if(passageNewPosY > passageOldPosY)
                    {
                        while(passageOldPosY <= passageNewPosY)
                        {
                            addObjectToDisplay(new Char(getRandomCharFromSet()), passageOldPosX, passageOldPosY);
                            passageOldPosY++;
                        }
                    }
                    else
                    {
                        while(passageOldPosY >= passageNewPosY)
                        {
                            addObjectToDisplay(new Char(getRandomCharFromSet()), passageOldPosX, passageOldPosY);
                            passageOldPosY--;
                        }
                    }
                }

                //overwrite passageOldPosX and passageOldPosY
                passageOldPosX = passageNewPosX;
                passageOldPosY = passageNewPosY;
            }
        }

    }

        /**
     * push random characters to the passage
     * 
     * @param None
     * @return None
     */
    private void popRandomCharToPassage()
    {

        for (Passage eachPassage : dungeon.getPassage() )
        {
            int posXSize = eachPassage.getPosX().size();
            int passageOldPosX = eachPassage.getPosX().get(0);
            int passageOldPosY = eachPassage.getPosY().get(0) + topHeight;

            for( int i = 1; i < posXSize; i++)
            {
                int passageNewPosX = eachPassage.getPosX().get(i);
                int passageNewPosY = eachPassage.getPosY().get(i) + topHeight;

                if(!objectGrid.get(passageOldPosX).get(passageOldPosY).isEmpty())
                {
                    objectGrid.get(passageOldPosX).get(passageOldPosY).pop();
                    writeToTerminal(passageOldPosX, passageOldPosY);
                }

                if(passageOldPosX != passageNewPosX)
                {
                    if(passageNewPosX > passageOldPosX)
                    {
                        while(passageOldPosX <= passageNewPosX)
                        {
                            if(!objectGrid.get(passageOldPosX).get(passageOldPosY).isEmpty())
                            {
                                objectGrid.get(passageOldPosX).get(passageOldPosY).pop();
                                writeToTerminal(passageOldPosX, passageOldPosY);
                            }
                                
                            passageOldPosX++;
                        }
                    }
                    else
                    {
                        while(passageOldPosX >= passageNewPosX)
                        {
                            if(!objectGrid.get(passageOldPosX).get(passageOldPosY).isEmpty())
                            {
                                objectGrid.get(passageOldPosX).get(passageOldPosY).pop();
                                writeToTerminal(passageOldPosX, passageOldPosY);
                            }
                            passageOldPosX--;
                        }
                    }
                }

                if(passageOldPosY != passageNewPosY)
                {
                    if(passageNewPosY > passageOldPosY)
                    {
                        while(passageOldPosY <= passageNewPosY)
                        {
                            if(!objectGrid.get(passageOldPosX).get(passageOldPosY).isEmpty())
                            {
                                objectGrid.get(passageOldPosX).get(passageOldPosY).pop();
                                writeToTerminal(passageOldPosX, passageOldPosY);
                            }
                            passageOldPosY++;
                        }
                    }
                    else
                    {
                        while(passageOldPosY >= passageNewPosY)
                        {
                            if(!objectGrid.get(passageOldPosX).get(passageOldPosY).isEmpty())
                            {
                                objectGrid.get(passageOldPosX).get(passageOldPosY).pop();
                                writeToTerminal(passageOldPosX, passageOldPosY);
                            }
                            passageOldPosY--;
                        }
                    }
                }

                //overwrite passageOldPosX and passageOldPosY
                passageOldPosX = passageNewPosX;
                passageOldPosY = passageNewPosY;
            }
        }

    }

    /**
     * Pop the characters to the displayGird based on the inputStep given
     * 
     * @param None
     * @return None
     */
    public void pushRandomCharToDisplayGrid()
    {
        pushRandomCharToRoom();
        pushRandomCharToPassage();
        try {
            Thread.sleep(1);
        } catch (Exception e) {
            //TODO: handle exception
        }   
        gameWindow.repaint();
 
    }

    /**
     * Push the characters to the displayGird based on the inputStep given
     * 
     * @param None
     * @return None
     */
    public void popRandomCharToDisplayGrid()
    {
        popRandomCharToRoom();
        popRandomCharToPassage();
        try {
            Thread.sleep(1);
        } catch (Exception e) {
            //TODO: handle exception
        }
        gameWindow.repaint();
    }


}

