package src.game;

import src.*;
import java.util.*;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.HashSet;




public class KeyStrokeCommand implements InputObserver, Runnable {

    private static int DEBUG = 0;
    private static String CLASSID = "KeyStrokeCommand";
    private static Queue<Character> inputQueue = null;
    private ObjectDisplayGrid displayGrid;

    //added for step 3
    private HashSet<Character> monstersSet = new HashSet<>();
    private Dungeon dungeon;
    private Player player;
    // added for step 4
    private int hallucinatedStep = 0;
    private int movesSinceLastHp;
    private static boolean working;

    public KeyStrokeCommand(ObjectDisplayGrid grid) {
        inputQueue = new ConcurrentLinkedQueue<>();
        displayGrid = grid;
        dungeon = grid.getDungeon();
        player = grid.getPlayer();
        movesSinceLastHp = 0;
    }

    @Override
    public void observerUpdate(char ch) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".observerUpdate receiving character " + ch);
        }
        inputQueue.add(ch);
    }

    private void rest() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean processInput() {

        char ch;

        boolean processing = true;
        while (processing) {
            if (inputQueue.peek() == null) {
                processing = false;
            } else {
                ch = inputQueue.poll();
                if (DEBUG > 1) {
                    System.out.println(CLASSID + ".processInput peek is " + ch);
                }
                if (ch == 'h') {
                    //go left
                    movePlayerHelper("left");                  
                }
                else if (ch == 'l') {
                    //go right
                    movePlayerHelper("right");
                }
                else if (ch == 'k') {
                    //go up
                    movePlayerHelper("up");
                }
                else if (ch == 'j') {
                    //go down
                    movePlayerHelper("down");
                }
                else if (ch == 'i') {
                    // inventory -- show pack contents
                    printOutItemsInPack();
                }
                else if (ch == 'c') {
                    // take off / change armor 
                    takeOffArmor();                   
                }
                else if (ch == 'd') {
                    while(inputQueue.peek() == null); //waiting for user input
                    char charAfterD = inputQueue.poll();
                    int itemNum = charAfterD - '0';
                    if(itemNum < 0 || itemNum >= player.getItemList().size()) {
                        updateInfo("Invalid Item Number");
                    }
                    else {
                        dropItem(itemNum);
                    }
                }
                else if(ch == 'p') {
                    pickupItem();
                }
                else if(ch == 'r') {
                    // read the scroll which is the item number <item in pack number> in pack
                    while(inputQueue.peek() == null); //waiting for user input
                    char charAfterR = inputQueue.poll();
                    int scrollIndex = Character.getNumericValue(charAfterR);
                    readScrollAndPerformItemAction(scrollIndex);
                }
                else if(ch == 't') {
                    // take out the weapon from pack
                    while(inputQueue.peek() == null); //waiting for user input
                    char charAfterT = inputQueue.poll();
                    int swordIndex = Character.getNumericValue(charAfterT);
                    takeOutSword(swordIndex);
                }
                else if(ch == 'w') {
                    // take out the armor which is item number < iteam number in pack> in the pack and put it on
                    while(inputQueue.peek() == null); //waiting for user input
                    char charAfterW = inputQueue.poll();
                    int armorIndex = Character.getNumericValue(charAfterW);
                    wearArmor(armorIndex);
                }
                else if (ch == 'H') {
                    while(inputQueue.peek() == null); //waiting for user input
                    char charAfterH = inputQueue.poll();
                    if(processCharAfterH(charAfterH) == false)
                    {
                        updateInfo("Invalid character was entered after H");
                    }
                }
                 
                else if (ch == 'E') {
                    while(inputQueue.peek() == null); //waiting for user input
                    char charAfterE = inputQueue.poll();
                    if(charAfterE == 'y' || charAfterE == 'Y')
                    {
                       updateInfo("Exited the game. Goodbye");
                       return false; 
                    }
                    else
                    {
                        updateInfo("Please put Y or y to exit the game");
                    }
                }
                else if(ch == '?') {
                    updateInfo("h,l,k,j,i,?,H,c,d,p,r,t,w,E. H <cmd> for more info");
                } 
                else {
                    updateInfo("Unknown character " + ch + " was detected. Type ? for more info");
                }
            }
        }
        if (player.getHp() <= 0) {
            ArrayList <CreatureAction> deathActions = player.getDeathAction();
            int posX = player.getPosX().get(0);
            int posY = player.getPosY().get(0);
            for (CreatureAction action : deathActions) {
                if (action.getName().equals("YouWin")) { //@Todo: Score?
                    updateInfo(action.getMessage());
                }
                else if (action.getName().equals("EndGame")) {
                    updateInfo(action.getMessage());
                    KeyStrokeCommand.working = false;
                }
                else if (action.getName().equals("Remove")) {
                    displayGrid.removeCharAt(posX, posY);
                }
                else if (action.getName().equals("ChangeDisplayedType")) {
                    displayGrid.removeCharAt(posX, posY);
                    displayGrid.addObjectToDisplay(new Char(action.getCharValue()), posX, posY);
                }
                else if (action.getName().equals("UpdateDisplay")) {// @TODO: not sure what to do here

                }
            }
        }
        return player.getHp() > 0;
    }

    /**
     * Move player helper function
     * 
     * @param direction
     */
    private void movePlayerHelper(String direction)
    {
        if(displayGrid.isHallucinated() && hallucinatedStep > 0)
        {
            if(movePlayer(direction)) 
            {
                displayGrid.popRandomCharToDisplayGrid();
                displayGrid.pushRandomCharToDisplayGrid();
                //Every step takes will decrease the effect
                hallucinatedStep--;
                if(hallucinatedStep == 0) 
                {
                    displayGrid.popRandomCharToDisplayGrid();
                    displayGrid.setHallucinated(false);
                }
                   
            }
        }
        else
        {
            movePlayer(direction);
        }   
    }

    /**
     * When a player move, this function will be called
     * 
     * 
     */
    private boolean movePlayer (String direction) {
        int posX = displayGrid.getPlayerCurrentPosX();
        int posY = displayGrid.getPlayerCurrentPosY();

        // get the monster set
        monstersSet = displayGrid.getMonstersSet();

        switch (direction) {
            case "left":
                if (displayGrid.getCharAt(posX - 1, posY) != '\u0000' && displayGrid.getCharAt(posX - 1, posY) != '|' && displayGrid.getCharAt(posX - 1, posY) != '-') {
                    movesSinceLastHp++;
                    if(movesSinceLastHp == player.getHpMoves()) {
                        movesSinceLastHp = 0;
                        updateHp(player.getHp() + 1);
                    }
                    if(monstersSet.contains(displayGrid.getCharAt(posX - 1, posY)))
                    {
                        // found a monster 
                        hitMonster(posX - 1, posY);
                    }
                    else
                    {   
                        displayGrid.setPlayerCurrentPosX(posX - 1);
                        if(displayGrid.isHallucinated())
                        {
                            displayGrid.addObjectToDisplayWhenHallucinated(new Char('@'), posX - 1, posY);
                        }
                        else
                        {
                            displayGrid.addObjectToDisplay(new Char('@'), posX - 1, posY);
                        }
                        displayGrid.removeCharAt(posX, posY);
                    }

                    return true;
                }
                break;
            case "right":
                if (displayGrid.getCharAt(posX + 1, posY) != '\u0000' && displayGrid.getCharAt(posX + 1, posY) != '|' && displayGrid.getCharAt(posX + 1, posY) != '-') {
                    movesSinceLastHp++;
                    if(movesSinceLastHp == player.getHpMoves()) {
                        movesSinceLastHp = 0;
                        updateHp(player.getHp() + 1);
                    }
                    if(monstersSet.contains(displayGrid.getCharAt(posX + 1, posY)))
                    {
                        // found a monster 
                        hitMonster(posX + 1, posY);
                    }
                    else
                    {
                        displayGrid.setPlayerCurrentPosX(posX + 1);
                        if(displayGrid.isHallucinated())
                        {
                            displayGrid.addObjectToDisplayWhenHallucinated(new Char('@'), posX + 1, posY);
                        }
                        else
                        {
                            displayGrid.addObjectToDisplay(new Char('@'), posX + 1, posY);
                        }
                        displayGrid.removeCharAt(posX, posY);
                    }
                   
                    return true;
                }
                break;
            case "up":
                if (displayGrid.getCharAt(posX, posY + 1) != '\u0000' && displayGrid.getCharAt(posX , posY + 1) != '|' && displayGrid.getCharAt(posX , posY + 1) != '-') {
                    movesSinceLastHp++;
                    if(movesSinceLastHp == player.getHpMoves()) {
                        movesSinceLastHp = 0;
                        updateHp(player.getHp() + 1);
                    }
                    if(monstersSet.contains(displayGrid.getCharAt(posX, posY+1)))
                    {
                        // found a monster 
                        hitMonster(posX, posY + 1);
                    }
                    else
                    {
                        displayGrid.setPlayerCurrentPosY(posY + 1);
                        if(displayGrid.isHallucinated())
                        {
                            displayGrid.addObjectToDisplayWhenHallucinated(new Char('@'), posX , posY + 1);
                        }
                        else
                        {
                            displayGrid.addObjectToDisplay(new Char('@'), posX, posY + 1);
                        }
                        displayGrid.removeCharAt(posX, posY);
                    }

                    return true;
                }
                break;
            case "down":
                if (displayGrid.getCharAt(posX, posY - 1) != '\u0000' && displayGrid.getCharAt(posX, posY - 1) != '|' && displayGrid.getCharAt(posX, posY - 1) != '-') {
                    movesSinceLastHp++;
                    if(movesSinceLastHp == player.getHpMoves()) {
                        movesSinceLastHp = 0;
                        updateHp(player.getHp() + 1);
                    }
                    if(monstersSet.contains(displayGrid.getCharAt(posX, posY-1)))
                    {
                         // found a monster 
                         hitMonster(posX, posY -1 );
                    }
                    else
                    {
                        displayGrid.setPlayerCurrentPosY(posY - 1);
                        if(displayGrid.isHallucinated())
                        {
                            displayGrid.addObjectToDisplayWhenHallucinated(new Char('@'), posX , posY - 1);
                        }
                        else
                        {
                            displayGrid.addObjectToDisplay(new Char('@'), posX, posY - 1);
                        }
                        displayGrid.removeCharAt(posX, posY);
                    }
                   
                    return true;
                }
                break;
        }


        return false;
    }

    /**
     * This method will execute the hitActions
     * @param action, room, monster
     * @return None
     */
    private void creatureHitActions(int monsterPosX, int monsterPosY, Room room, Creature creature, char monsterChar) {
        
        
        ArrayList <CreatureAction> hitActions;
        
        if(creature instanceof Player)
            hitActions = creature.getHitAction();
        else
            hitActions = creature.getHitAction();

        for (CreatureAction action : hitActions) {

            if (action.getName().equals("DropPack")) {
                dropItem(0);
            }
            else if (action.getName().equals("EmptyPack")) {
                while (player.getItemList().size() != 0) {
                    dropItem(0);
                }
            }
            else if (action.getName().equals("Teleport")) {
                teleport(monsterPosX, monsterPosY, room, (Monster)creature, monsterChar);
            }
            else if (action.getName().equals("Print")) { // @TODO: not sure what to do here
            }
        }
    }

    /**
     * This method will generate a randomly selected coordinate and place the monster there
     *
     * @param room, monster
     * @return None
     */
    private void teleport (int monsterPosX, int monsterPosY, Room room, Monster monster, char monsterChar) {
        Random random = new Random();
        int posX = random.nextInt(dungeon.getWidth());
        int posY = random.nextInt(dungeon.getBottomHeight());
        while (displayGrid.getCharAt(posX, posY) != '.') {
            posX = random.nextInt(dungeon.getWidth());
            posY = random.nextInt(dungeon.getBottomHeight());
        }
        room.getCreatures().remove(monster);
        displayGrid.removeCharAt(monsterPosX, monsterPosY);
        Room newRoom = (Room) displayGrid.returnRoomOrPassage(posX, posY);
        monster.getPosX().set(0, posX - newRoom.getPosX().get(0));
        monster.getPosY().set(0, posY - newRoom.getPosY().get(0) - dungeon.getTopHeight());
        newRoom.getCreatures().add(monster);
        if (displayGrid.isHallucinated()) {
            displayGrid.addObjectToDisplayWhenHallucinated(new Char(monsterChar), posX, posY);
        }
        else {
            displayGrid.addObjectToDisplay(new Char(monsterChar), posX, posY);
        }
    }


    /**
     * This method will execute monster death actions
     * @param monsterPosX, monsterPosY, room, monster
     * @return None
     */
    private void monsterDeathActions(int monsterPosX, int monsterPosY, Room room, Monster monster) {
        ArrayList<CreatureAction> deathActions = monster.getDeathAction();
        for (CreatureAction action : deathActions) {
            if (action.getName().equals("YouWin")) {// @TODO: Score?
                updateInfo(action.getMessage());
            }
            else if (action.getName().equals("EndGame")) {
                updateInfo(action.getMessage());
                KeyStrokeCommand.working = false;
            }
            else if (action.getName().equals("Remove")) {
                room.getCreatures().remove(monster);
                displayGrid.removeCharAt(monsterPosX, monsterPosY);
            }
            else if (action.getName().equals("ChangeDisplayedType")) {
                displayGrid.removeCharAt(monsterPosX, monsterPosY);
                displayGrid.addObjectToDisplay(new Char(action.getCharValue()), monsterPosX, monsterPosY);
            }
            else if (action.getName().equals("UpdateDisplay")) {// @TODO: not sure what to do here
            }
        }
    }

    /**
     * This method will generate a randomly selected value when player bump intoa monster 
     * and display the damage from each hit into the into
     * 
     * @param Nothing
     * @return the 
     */
    private void hitMonster(int monsterPosX, int monsterPosY )
    {
        char c = displayGrid.getCharAt(monsterPosX, monsterPosY);
        int playerMaxHit = player.getMaxHit();
        Displayable roomOrPassage = displayGrid.returnRoomOrPassage(monsterPosX, monsterPosY);
        Random random = new Random();
        
        int playerHitVal = random.nextInt(playerMaxHit+1);
        int monsterHitVal;
        int armorVal = (player.isWearingArmor()) ? player.getArmorBeingWorn().getIntValue() : 0;
        int swordVal =  (player.isWearingSword()) ? player.getSwordBeingWorn().getIntValue() : 0;

        if(roomOrPassage instanceof Room) 
        {
            Room room = (Room) roomOrPassage;
            ArrayList <Creature> creatures = room.getCreatures();
            ArrayList <Monster> monsters = new ArrayList<> ();
            for(Creature creature : creatures) {
                if (creature instanceof Monster) {
                    monsters.add((Monster) creature);
                }
            }
            if(c == 'T')
            {
                for (Monster monster : monsters) {
                    if (monster.getName().equals("Troll") && monster.getPosX().get(0) == monsterPosX - room.getPosX().get(0) && monster.getPosY().get(0) == monsterPosY - room.getPosY().get(0) - dungeon.getTopHeight()) {
                        if(monster.getHp() > 0) {
                            if(monster.getMaxHit() != 0)
                                monsterHitVal = random.nextInt(monster.getMaxHit()+1);
                            else
                                monsterHitVal = 0;
                            monster.setHp(monster.getHp() - playerHitVal - swordVal);
                            monsterHitVal = ((monsterHitVal - armorVal) <= 0 ) ? 0  : (monsterHitVal - armorVal);
                            updateHp(player.getHp() - monsterHitVal);

                            creatureHitActions(monsterPosX, monsterPosY, room, player, '@');

                            if(monster.getHp() <= 0) {
                                if (monster.getDeathAction().isEmpty()) {
                                    updateInfo("Monster Hit: " + monsterHitVal + " Player Hit: " + playerHitVal + " Monster Hp: " + monster.getHp());
                                }
                                else {
                                    monsterDeathActions(monsterPosX, monsterPosY, room, monster);
                                }
                            }
                            else {
                                creatureHitActions(monsterPosX, monsterPosY, room, monster, 'T');
                                updateInfo("Monster Hit: " + monsterHitVal + " Player Hit: " + playerHitVal + " Monster Hp: " + monster.getHp());
                            }
                        }
                    }
                }
                
            }
            else if(c == 'H')
            {
                for (Monster monster : monsters) {
                    if (monster.getName().equals("Hobgoblin") && monster.getPosX().get(0) == monsterPosX - room.getPosX().get(0) && monster.getPosY().get(0) == monsterPosY - room.getPosY().get(0) - dungeon.getTopHeight()) {
                        if(monster.getHp() > 0) {
                            if(monster.getMaxHit() != 0)
                                monsterHitVal = random.nextInt(monster.getMaxHit()+1);
                            else
                                monsterHitVal = 0;
                            
                                monster.setHp(monster.getHp() - playerHitVal - swordVal);
                                monsterHitVal = ((monsterHitVal - armorVal) <= 0 ) ? 0  : (monsterHitVal - armorVal);
                                updateHp(player.getHp() - monsterHitVal);
                            
                                creatureHitActions(monsterPosX, monsterPosY, room, player, '@');

                            if(monster.getHp() <= 0) {
                                if (monster.getDeathAction().isEmpty()) {
                                    updateInfo("Monster Hit: " + monsterHitVal + " Player Hit: " + playerHitVal + " Monster Hp: " + monster.getHp());
                                }
                                else {
                                    monsterDeathActions(monsterPosX, monsterPosY, room, monster);
                                }
                            }
                            else {
                                creatureHitActions(monsterPosX, monsterPosY, room, monster, 'H');
                                updateInfo("Monster Hit: " +  monsterHitVal + " Player Hit: " + playerHitVal + " Monster Hp: " + monster.getHp());
                            }
                        }
                    }
                }
                
            }
            else if(c =='S')
            {
                for (Monster monster: monsters) {
                    if (monster.getName().equals("Snake")  && monster.getPosX().get(0) == monsterPosX - room.getPosX().get(0) && monster.getPosY().get(0) == monsterPosY - room.getPosY().get(0) - dungeon.getTopHeight()) {
                        if(monster.getHp() > 0) {
                            if(monster.getMaxHit() != 0)
                                monsterHitVal = random.nextInt(monster.getMaxHit()+1);
                            else
                                monsterHitVal = 0;

                            monster.setHp(monster.getHp() - playerHitVal - swordVal);
                            monsterHitVal = ((monsterHitVal - armorVal) <= 0 ) ? 0  : (monsterHitVal - armorVal);
                            updateHp(player.getHp() - monsterHitVal);

                            creatureHitActions(monsterPosX, monsterPosY, room, player, '@');

                            if(monster.getHp() <= 0) {
                                if (monster.getDeathAction().isEmpty()) {
                                    updateInfo("Monster Hit: " + monsterHitVal + " Player Hit: " + playerHitVal + " Monster Hp: " + monster.getHp());
                                }
                                else {
                                    monsterDeathActions(monsterPosX, monsterPosY, room, monster);
                                }
                            }
                            else {
                                creatureHitActions(monsterPosX, monsterPosY, room, monster, 'S');
                                updateInfo("Monster Hit: " + monsterHitVal + " Player Hit: " + playerHitVal + " Monster Hp: " + monster.getHp());
                            }
                        }
                    }
                }
            }
        } 
    }

    /**
     * This method will update hp to desired value
     * @param hp
     * @return None
     */
    private void updateHp(int hp)
    {
        String playerHpStr;
        
        // clear the Hp first
        playerHpStr = String.valueOf(displayGrid.getPlayerHp());
        for(int i = 0; i < playerHpStr.length(); i++)
        {
            displayGrid.removeCharAt(4 + i, 0);
        }
        
        //then update
        displayGrid.setPlayerHp(hp);
        playerHpStr = String.valueOf(hp);
        for(int i = 0; i < playerHpStr.length(); i++)
        {
            Char charHp = new Char(playerHpStr.charAt(i));
            displayGrid.addObjectToDisplay(charHp, 4 + i, 0);
        }

    }

    /**
     * This method will update core to desired value
     * @param core
     * @return None
     */
    private void updateScore(int core)
    {
        String coreStr;
        // clear the core first
        coreStr = String.valueOf(displayGrid.getCore());
        for(int i = 0; i < coreStr.length(); i++)
        {
            displayGrid.removeCharAt(16 + i, 0);
        }

        displayGrid.setCore(core);
        coreStr = String.valueOf(core);
        for(int i = 0; i < coreStr.length(); i++)
        {
            Char charCore = new Char(coreStr.charAt(i));
            displayGrid.addObjectToDisplay(charCore, 16 + i, 0);
        }

    }

    /**
     * This method will update Pack messages
     * @param msg (String)
     * @return None
     */
    private void updatePack(String msg)
    {
        int i = 0;
        int j = 0;
        int strLen = msg.length();
        int msgOffset = 6;
        int strOffset = 0;
        int numCharInEachRow = dungeon.getWidth() - 6;
        int numRow;
        int packPosY = dungeon.getTopHeight() + dungeon.getGameHeight();
 
        numRow = (msg.length() / numCharInEachRow) + 1;

        for(i = 0 ; i < numRow; i++)
        {
            if(numRow > dungeon.getBottomHeight() -1) break;
            for( j = 0 ; strLen > 0 && j < (dungeon.getWidth() - msgOffset); j++)
            {
                displayGrid.removeCharAt(j + msgOffset, i + packPosY);
                Char msgChar = new Char(msg.charAt(strOffset + j));
                displayGrid.addObjectToDisplay(msgChar, j+ msgOffset , i + packPosY);
                strLen--;
            }
            strOffset += numCharInEachRow;
        }

        numRow = dungeon.getBottomHeight() - 1;
        // clear the first Row and first columns
        i--; // dirty hack
        for( ; j < (dungeon.getWidth() - msgOffset); j++)
        {
                displayGrid.removeCharAt( j+ msgOffset , i + packPosY);
        }
        
        i++; // start clearing next row
        for(; i < numRow; i++)
        {
            for(j = 0 ; j < (dungeon.getWidth() - msgOffset); j++)
            {
                    displayGrid.removeCharAt( j+ msgOffset , i + packPosY);
            }
        }



    }


    /**
     * This method will update Info messages
     * @param msg (String)
     * @return None
     */
    private void updateInfo(String msg)
    {
        int i;
        int msgOffset = 6;

        int infoPosY = dungeon.getTopHeight() + dungeon.getGameHeight() + dungeon.getBottomHeight() - 1;

        for(i = 0; i < msg.length(); i++)
        {
            Char msgChar = new Char(msg.charAt(i));
            displayGrid.removeCharAt(msgOffset+i, infoPosY);
            displayGrid.addObjectToDisplay(msgChar, msgOffset+i, infoPosY);
        }

        //Overwrite the rest with null
        while(displayGrid.getCharAt(msgOffset + i, infoPosY) != 0 && i < dungeon.getWidth())
        {
            displayGrid.removeCharAt(msgOffset+i, infoPosY);
            i++;
        }
    }

    /**
     * This method will process player pack and print out the item in the pack
     * Have to transverse backwards!! because we are using ArrayList (last item added at the end)
     * @param None
     * @return None
     */
    //@TODO: add sword strength
    private void printOutItemsInPack()
    {
       List <Item> itemList = player.getItemList();
       String msg = "";
       int j = 0; // use to index the item
       
       if(DEBUG > 0)
       {
           System.out.println("The player has " + itemList.size() + " items");
       }

       if(itemList.size() == 0)
       {
           updatePack("The player has no item in the pack!");
           return;
       }

       for(int i = 0; i < itemList.size() && i < (dungeon.getWidth()); i++)
       {
           Item eachItem = itemList.get(i);
           String itemName = eachItem.getName();

           // process the message
           msg += j + ":" + itemName + " ";
           j++;
       }

       // update the pack
       updatePack(msg);
    }


    /**
     * This method will process the character after H. 
     * It will return true if the character can be processed and display the message
     * on ObjectDisplayGrid
     * @param charAfterH
     * @return boolean
     */
    private boolean processCharAfterH(char charAfterH)
    {
        switch(charAfterH)
        {
            case 'h':
                updateInfo("move left 1 space.");
                return true;
            case 'l':
                updateInfo("move right 1 space.");
                return true;  
            case 'k':
                updateInfo("move up 1 space");
                return true;
            case 'j':
                updateInfo("move down 1 space");
                return true;
            case 'i':
                updateInfo("inventory -- show pack contents");
                return true;
            case '?':
                updateInfo("display help command");
                return true;
            case 'H':
                updateInfo("H< next input char> gives more detailed information about the command.");
                return true;
            case 'c':
                updateInfo("take off/change armor");
                return true;
            case 'd':
                updateInfo("drop <item number> item from pack");
                return true;
            case 'p':
                updateInfo("pick up item under player and put into the pack");
                return true;
            case 'r':
                updateInfo("read the scroll which is item number <item in pack number> in pack");
                return true;
            case 't':
                updateInfo("take out weapon from pack");
                return true;
            case 'w':
                updateInfo("take out the armor <item number in pack> in the pack and put it on.");
                return true;
            case 'E':
                updateInfo("If <next input character> is a 'Y' or 'y', the game will be ended.");
                return true;
        }


        return false;
    }

    /**
     * This method will drop the item in the pack specified by itemNum 
     * then add it to the room or passage where the player currently stands.
     * @param itemNum
     * @return None
     */
    public void dropItem(int itemNum) {
        int posX = displayGrid.getPlayerCurrentPosX();
        int posY = displayGrid.getPlayerCurrentPosY();
        // drop <item number> iteam from pack 
        // item gets player position
        // add item to room at position
        // redraw

        if(!player.getItemList().isEmpty())
        {
            Item temp = player.getItemList().get(itemNum);
            player.getItemList().remove(itemNum);
            Displayable roomOrPassage = displayGrid.returnRoomOrPassage(posX, posY);

            if(roomOrPassage instanceof Room) {
            Room room = (Room) roomOrPassage;
            if(temp.getPosX().size() > 0) { 
                    temp.getPosX().set(0, (displayGrid.getPlayerCurrentPosX() - room.getPosX().get(0)));
                    temp.getPosY().set(0, displayGrid.getPlayerCurrentPosY() - room.getPosY().get(0) - dungeon.getTopHeight());
                }
                else {
                    temp.getPosX().add((displayGrid.getPlayerCurrentPosX() - room.getPosX().get(0)));
                    temp.getPosY().add(displayGrid.getPlayerCurrentPosY() - room.getPosY().get(0) - dungeon.getTopHeight());
                }
                room.addItemToList(temp);
            }
            else if (roomOrPassage instanceof Passage) {
                Passage passage = (Passage) roomOrPassage;
                if(temp.getPosX().size() > 0) { 
                    temp.getPosX().set(0, displayGrid.getPlayerCurrentPosX());
                    temp.getPosY().set(0, displayGrid.getPlayerCurrentPosY());
                }
                else {
                    temp.getPosX().add(displayGrid.getPlayerCurrentPosX());
                    temp.getPosY().add(displayGrid.getPlayerCurrentPosY());
                }
                passage.addItemToList(temp);
            } 
            if(temp instanceof Scroll) {
                displayGrid.removeCharAt(displayGrid.getPlayerCurrentPosX(), displayGrid.getPlayerCurrentPosY());
                displayGrid.addObjectToDisplay(new Char('?'), displayGrid.getPlayerCurrentPosX(), displayGrid.getPlayerCurrentPosY());
                displayGrid.addObjectToDisplay(new Char('@'), displayGrid.getPlayerCurrentPosX(), displayGrid.getPlayerCurrentPosY());
            }
            else if (temp instanceof Armor) {
                displayGrid.removeCharAt(displayGrid.getPlayerCurrentPosX(), displayGrid.getPlayerCurrentPosY());
                displayGrid.addObjectToDisplay(new Char(']'), displayGrid.getPlayerCurrentPosX(), displayGrid.getPlayerCurrentPosY());
                displayGrid.addObjectToDisplay(new Char('@'), displayGrid.getPlayerCurrentPosX(), displayGrid.getPlayerCurrentPosY());
            }
            else if (temp instanceof Sword) {
                displayGrid.removeCharAt(displayGrid.getPlayerCurrentPosX(), displayGrid.getPlayerCurrentPosY());
                displayGrid.addObjectToDisplay(new Char(')'), displayGrid.getPlayerCurrentPosX(), displayGrid.getPlayerCurrentPosY());
                displayGrid.addObjectToDisplay(new Char('@'), displayGrid.getPlayerCurrentPosX(), displayGrid.getPlayerCurrentPosY());
                /*
                    Addded for step 4, when the player drop the sword in the pack it will drop the sword for the player
                */
                if(player.isWearingSword()) {
                    player.takeOffSword();
                }
            }

            printOutItemsInPack();
        }

    }

    /**
     * This method will pickup the item under the player and add it to the pack
     * @param None
     * @return None
     */
    public void pickupItem() {
        // pick up item under player and put into the pack
        int posX = displayGrid.getPlayerCurrentPosX();
        int posY = displayGrid.getPlayerCurrentPosY();
        displayGrid.removeCharAt(posX, posY);
        char c = displayGrid.getCharAt(posX, posY);
        Displayable roomOrPassage = displayGrid.returnRoomOrPassage(posX, posY);
        Item remove = null;
        if(roomOrPassage instanceof Room) {
            Room room = (Room) roomOrPassage;
            ArrayList <Item> roomItems = room.getItemList();
            if(c == '?')
            {
                for (int i = roomItems.size() - 1; i >= 0 && remove == null; i--) {
                    Item eachItem = roomItems.get(i);
                    if (eachItem instanceof Scroll && eachItem.getPosX().get(0) == posX - room.getPosX().get(0) && eachItem.getPosY().get(0) == posY - room.getPosY().get(0) - dungeon.getTopHeight()) {
                        remove = eachItem;
                        player.addItemToList(eachItem);
                    }
                }
                
            }
            else if(c == ']')
            {
                for (int i = roomItems.size() - 1; i >= 0 && remove == null; i--) {
                    Item eachItem = roomItems.get(i);
                    if (eachItem instanceof Armor && eachItem.getPosX().get(0) == posX - room.getPosX().get(0) && eachItem.getPosY().get(0) == posY - room.getPosY().get(0) - dungeon.getTopHeight()) {
                        remove = eachItem;
                        player.addItemToList(eachItem);
                    }
                }
                
            }
            else if(c ==')')
            {
                for (int i = roomItems.size() - 1; i >= 0 && remove == null; i--) {
                    Item eachItem = roomItems.get(i);
                    if (eachItem instanceof Sword && eachItem.getPosX().get(0) == posX - room.getPosX().get(0) && eachItem.getPosY().get(0) == posY - room.getPosY().get(0) - dungeon.getTopHeight()) {
                        remove = eachItem;
                        player.addItemToList(eachItem);
                    }
                }
                
            }
            if(remove != null) {
                roomItems.remove(remove);
                displayGrid.removeCharAt(posX, posY);
                updateInfo("");  
            }
            else {
                updateInfo("Nothing to pick up");
            }
        }
        else if (roomOrPassage instanceof Passage) {
            Passage passage = (Passage) roomOrPassage;
            ArrayList <Item> passageItems = passage.getItemList();
            if(c == '?')
            {
                for (Item eachItem : passageItems) {
                    if (eachItem instanceof Scroll && eachItem.getPosX().get(0) == posX && eachItem.getPosY().get(0) == posY) {
                        remove = eachItem;
                        player.addItemToList(eachItem);
                    }
                }
                
            }
            else if(c == ']')
            {
                for (Item eachItem : passageItems) {
                    if (eachItem instanceof Armor && eachItem.getPosX().get(0) == posX && eachItem.getPosY().get(0) == posY) {
                        remove = eachItem;
                        player.addItemToList(eachItem);
                    }
                }
                
            }
            else if(c ==')')
            {
                for (Item eachItem : passageItems) {
                    if (eachItem instanceof Sword && eachItem.getPosX().get(0) == posX && eachItem.getPosY().get(0) == posY) {
                        remove = eachItem;
                        player.addItemToList(eachItem);

                    }
                }
                
            }
            if(remove != null) {
                passageItems.remove(remove);
                displayGrid.removeCharAt(posX, posY);    
                updateInfo("");  
            }
            else {
                updateInfo("Nothing to pick up");
            }
        }
        displayGrid.addObjectToDisplay(new Char('@'), posX, posY);         
        printOutItemsInPack();
    }


    //////////////////////////////////////////////////////////
    // Step 4 and Step 5
    //
    //////////////////////////////////////////////////////////

    /**
     * This method will change or take off armor
     * 
     * @param None
     * @return: None
     */
    private void takeOffArmor()
    {
        if(!player.isWearingArmor())
        {
            updateInfo("Cannot take off armor because the player is not wearing one!");
            return;
        }

        player.takeOffArmor();
        updateInfo("Armor was taken Off");
    }


    /**
     *  This method will take the armor identified by <integer> 
     *  from the pack container and make it the
     *  armor being worn.
     * 
     * @param armorIndex (int)
     * @return None
     */
    private void wearArmor(int armorIndex)
    {
        if(!player.wearArmor(armorIndex))
        {
            updateInfo("Armor cannot be worn!");
            return;
        }
        updateInfo("Successfully wore an armor");
    }


    /**
     * This method will take the sword identified by <integer> in the pack container and
     *  have the player wield it.
     * 
     * @param swordIndex (int)
     * @return None
     */
    private void takeOutSword(int swordIndex)
    {
        if(!player.wearSword(swordIndex))
        {
            updateInfo("Sword cannot be taken out!");
            return;
        }
        updateInfo("Successfully took out a sword");
    }


    /**
     * This method will return the scroll if the scrollIndex is Scroll
     * if the index is not a scroll or errors occur return null
     * @param scrollIndex (int)
     * @return reference to the Scroll
     */
    private void readScrollAndPerformItemAction(int scrollIndex)
    {
        int numItem = player.getItemList().size();

        if(scrollIndex >= numItem || scrollIndex < 0) 
        {
            updateInfo("The scroll Index is not valid");
            return;
        }

        // make sure it's a scroll
        Item itemInList = player.getItemList().get(scrollIndex);
        if(!(itemInList instanceof Scroll)) 
        {
            updateInfo("The item is not a scroll");
            return;
        }

        Scroll scroll = (Scroll) itemInList;
        processItemAction(scroll.getItemAction().get(0));
        
        //After performing remove the item from the pack
        player.getItemList().remove(scrollIndex);
    }


    private void bless (ItemAction action) {
        if (action.getCharValue() == 'a') {
            if (player.isWearingArmor()) {
                int armorVal = player.getArmorBeingWorn().getIntValue() + action.getIntValue();
                player.getArmorBeingWorn().setIntValue(armorVal);
                player.getArmorBeingWorn().updateName();
                updateInfo("armor cursed! " + action.getIntValue() + " taken from its effectiveness");
            }
            else {
                updateInfo("scroll of cursing does nothing because armor not being used");
            }
        }
        else if (action.getCharValue() == 'w') {
            if (player.isWearingSword()) {
                int swordVal = player.getSwordBeingWorn().getIntValue() + action.getIntValue();
                player.getSwordBeingWorn().setIntValue(swordVal);
                player.getSwordBeingWorn().updateName();
                updateInfo("sword cursed! " + action.getIntValue() + " taken from its effectiveness");
            }
            else {
                updateInfo("scroll of cursing does nothing because sword not being used");
            }
        }
    }

    /**
     * This method will process the itemAction
     * (Working on it....)
     * 
     */
    private void processItemAction(ItemAction itemAction)
    {
        String itemName = itemAction.getName();

        switch (itemName)
        {
            case "Hallucinate":
                displayGrid.setHallucinated(true);
                hallucinatedStep = itemAction.getIntValue();
                displayGrid.pushRandomCharToDisplayGrid();
                break;
            case "BlessArmor" :
                bless(itemAction);
        }

    }



    /**
     * Method that will overwrite Runnable run
     */
    @Override
    public void run() {
        displayGrid.registerInputObserver(this);
        working = true;
        while (working) {
            rest();
            working = (processInput( ));
        }
        //@TODO: 11/8/2020 Discuss EndGame
        //updateInfo("Game Over");
    }


}



