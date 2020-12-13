package src;


public interface Action
{
    public void setMessage(String msg);
    public void setIntValue(int v);
    public void setCharValue(char c);
    public String getMessage();
    public int getIntValue();
    public char getCharValue();
}