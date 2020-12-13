package src;


public class ItemAction implements Action {

    private String message;
    private int value;
    private char c;
    private String name;
    private String type;

    ItemAction(String _name, String _type) {
        name = _name;
        type = _type;
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

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

}