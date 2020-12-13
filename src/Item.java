package src;

import java.util.*;

public class Item extends Displayable
{
    private String name;
    private int room;
    private int serial;
    private Creature owner;
    private ArrayList <ItemAction> itemAction = new ArrayList<ItemAction>();

    public Item(String _name, int _room, int _serial )
    {
        name = _name;
        room = _room;
        serial = _serial;
    }


    ////////////////////////////////////////////////////

    // Getter and setter for constructor

    public void setName(String _name)
    {
        name = _name;
    }

    public String getName()
    {
        return name;
    }

    public void setID(int _room, int _serial)
    {
        room = _room;
        serial = _serial;
    }

    public int getRoom()
    {
        return room;
    }

    public int getSerial()
    {
        return serial;
    }

    ////////////////////////////////////////////////////

    public void setOwner(Creature _owner )
    {
        owner = _owner;
    }

    public Creature getOwner()
    {
        return owner;
    }
    
    ////////////////////////////////////////////////////

    public void setItemAction(ItemAction action) {
        itemAction.add(action);
    }

    public ArrayList <ItemAction> getItemAction() {
        return itemAction;
    }

    public String toString() {
        String str = "";
        str += name + " Room " + room + " Serial " + serial + "\n\tItem Actions:\n\t\t";
        for (ItemAction action : itemAction) {
            str += action.getName() + " ";
        }
        return str;
    }

}