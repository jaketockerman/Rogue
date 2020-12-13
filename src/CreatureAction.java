package src;

public class CreatureAction implements Action {

    private String message;
    private int value;
    private char c;
    private String name;
    private  String type;

    CreatureAction(String _name, String _type) {
        type = _type;
        name = _name;
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public void setMessage(String msg) {
        message = msg;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void setIntValue(int v) {
        value = v;
    }

    public int getIntValue() {
        return value;
    }

    @Override
    public void setCharValue(char _c) {
        c = _c;
    }

    @Override
    public char getCharValue() {
        return c;
    }

}