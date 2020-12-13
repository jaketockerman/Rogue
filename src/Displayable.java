package src;

import java.util.ArrayList;

public class Displayable
{

    private boolean visible;
    
    
    private int value;
    private int width;
    private int height;
    
    private char type;

    private ArrayList<Integer> x = new ArrayList<Integer>();
    private ArrayList<Integer> y = new ArrayList<Integer>();

    public Displayable () {}

    public void setInvisible()
    {
        visible = false;
    }

    public void setVisible()
    {
        visible = true;
    }

    public boolean getVisible()
    {
        return visible;
    }



    ////////////////////////////////////////////////////

    public void setType(char _type)
    {
        type = _type;
    }

    public char getType()
    {
        return type;
    }

    ////////////////////////////////////////////////////

    public void setIntValue(int _value)
    {
        value = _value;
    }

    public int getIntValue()
    {
        return value;
    }

    ////////////////////////////////////////////////////

    public void setPosX(int _x)
    {
        x.add(_x);
    }

    public void removePosX(int pos)
    {
        x.remove(pos);
    }

    public ArrayList <Integer> getPosX()
    {
        return x;
    }

    ////////////////////////////////////////////////////

    public void setPosY(int _y)
    {
        y.add(_y);
    }

    public void removePosY(int pos)
    {
        y.remove(pos);
    }

    public ArrayList<Integer> getPosY()
    {
        return y;
    }

    ////////////////////////////////////////////////////

    public void setWidth(int _width)
    {
        width = _width;
    }

    public int getWidth()
    {
        return width;
    }

    ////////////////////////////////////////////////////

    public void setHeight(int _height)
    {
        height = _height;
    }

    public int getHeight()
    {
        return height;
    }

    ////////////////////////////////////////////////////


}