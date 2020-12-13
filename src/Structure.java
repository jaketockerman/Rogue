package src;
import java.util.ArrayList;

public abstract class Structure extends Displayable
{

    private ArrayList <Item> itemList = new ArrayList<Item>();

    ////////////////////////////////////////////////////

    public void addItemToList (Item _item)
    {
        itemList.add(_item);
    }

    public ArrayList<Item> getItemList()
    {
        return itemList;
    }

    ////////////////////////////////////////////////////
  
}