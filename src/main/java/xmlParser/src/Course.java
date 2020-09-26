/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author smidkiff
 */
public class Course extends Activity {
    private String instructor;
    private int credit;
    private String courseNumber;
    
    public void setInstructor(String _instructor) {
        instructor = _instructor;
    }
          
    public void setNumber(String number) {
        courseNumber = number;
    }
    
    public void setCredit(int _credit) {
        credit = _credit;
    }
    
    @Override
    public String toString( ) {
        String str = "Course: \n";
        str += super.toString( );
        str += "   instructor: " + instructor + "\n";
        str += "   credit: " + credit + "\n";
        str += "   courseNumber: " + courseNumber + "\n";
        return str;
    }
}
