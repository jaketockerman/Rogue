package src;
import java.util.ArrayList;

public class Player extends Creature
{
    private Armor armorBeingWorn;
    private Sword swordBeingWorn;
    private ArrayList <Item> itemList = new ArrayList<Item>();

    public Player(String _name, int _room, int _serial)
    {
        super(_name, _room,_serial);
    }

    ////////////////////////////////////////////////////

    public void addItemToList(Item _item)
    {
        itemList.add(_item);
    }

    public ArrayList<Item> getItemList()
    {
        return itemList;
    }

    ////////////////////////////////////////////////////

    public boolean wearArmor(int armorIndex)
    {
        int numItem = itemList.size();
        int itemIndex = 0;

        for(int i = 0; i < armorIndex; i++) itemIndex++;

        //if there is no item in my pack
        if(numItem == 0) return false;
        if(itemIndex >= numItem) return false;
        
        //get the reference of the item
        Item itemInItemList = itemList.get(itemIndex);
        if(!(itemInItemList instanceof Armor)) return false;

        // remove the Armor from the list
        Armor armorToBeWore = (Armor) itemList.get(itemIndex);
        
        // if we are already wearing an armor, put it back
        if(armorBeingWorn != null) return false;

        // Wear it here
        armorToBeWore.setName(armorToBeWore.getName() + " (a)");
        armorBeingWorn = armorToBeWore;
        return true;
    }

    public void takeOffArmor()
    {   
        armorBeingWorn.setName(armorBeingWorn.getName().substring(0, armorBeingWorn.getName().length() - 4));
        armorBeingWorn = null;
    }

    public boolean isWearingArmor()
    {
        return (armorBeingWorn != null) ? true : false; 
    }

    public Armor getArmorBeingWorn() {
        return armorBeingWorn;
    }

    ////////////////////////////////////////////////////

    public boolean wearSword(int swordIndex)
    {
        int numItem = itemList.size();
        int itemIndex = 0;

        for(int i = 0; i < swordIndex; i++) itemIndex++;

        //if there is no item in my pack
        if(numItem == 0) return false;
        if(itemIndex >= numItem) return false;
        
        //get the reference of the item
        Item itemInItemList = itemList.get(itemIndex);
        if(!(itemInItemList instanceof Sword)) return false;

        //Convert it to sword
        Sword swordToBeWore = (Sword) itemList.get(itemIndex);

        // Wear it here
        swordToBeWore.setName(swordToBeWore.getName() + " (w)");
        swordBeingWorn = swordToBeWore;
        return true;
    }

    public void takeOffSword()
    {
        // sword remains in the pack when wielding so 
        // we don't need to add it back to the pack
        swordBeingWorn.setName(swordBeingWorn.getName().substring(0, swordBeingWorn.getName().length() - 4));
        swordBeingWorn = null;
    }

    public boolean isWearingSword()
    {
        return (swordBeingWorn != null) ? true : false; 
    }

    public Sword getSwordBeingWorn() {
        return swordBeingWorn;
    }
    ////////////////////////////////////////////////////

    @Override
    public String toString() {
        ArrayList <CreatureAction> hitAction = this.getHitAction();
        ArrayList <CreatureAction> deathAction = this.getDeathAction();

        String str = "";
        str += getName() + " Room " + getRoom() + " Serial " + getSerial() + " hpMoves " + getHpMoves() + " maxHit " + getMaxHit() + " hp " + getHp();
        str += "\n\tHit Actions:\n\t\t";
        for(CreatureAction action : hitAction) {
            str += action.getName() + " ";
        }
        str += "\n\tDeath Actions:\n\t\t";
        for(CreatureAction action : deathAction){
            str += action.getName() + " ";
        }
        
        for(Item eachItem: itemList) {
            str += "\n\t " + eachItem.getName() + eachItem.toString() + "\n";
        }

        return str;
    }
}