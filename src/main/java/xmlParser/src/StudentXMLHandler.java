
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class StudentXMLHandler extends DefaultHandler {

    // the two lines that follow declare a DEBUG flag to control
    // debug print statements and to allow the class to be easily
    // printed out.  These are not necessary for the parser.
    private static final int DEBUG = 1;
    private static final String CLASSID = "StudentXMLHandler";

    // data can be called anything, but it is the variables that
    // contains information found while parsing the xml file
    private StringBuilder data = null;

    // When the parser parses the file it will add references to
    // Student objects to this array so that it has a list of 
    // all specified students.  Had we covered containers at the
    // time I put this file on the web page I would have made this
    // an ArrayList of Students (ArrayList<Student>) and not needed
    // to keep tract of the length and maxStudents.  You should use
    // an ArrayList in your project.
    private Student[] students;
    private int maxStudents = 0;
    private int studentCount = 0;

    // The XML file contains a list of Students, and within each 
    // Student a list of activities (clubs and classes) that the
    // student participates in.  When the XML file initially
    // defines a student, many of the fields of the object have
    // not been filled in.  Additional lines in the XML file 
    // give the values of the fields.  Having access to the 
    // current Student and Activity allows setters on those 
    // objects to be called to initialize those fields.
    private Student studentBeingParsed = null;
    private Activity activityBeingParsed = null;

    // The bX fields here indicate that at corresponding field is
    // having a value defined in the XML file.  In particular, a
    // line in the xml file might be:
    // <instructor>Brook Parke</instructor> 
    // The startElement method (below) is called when <instructor>
    // is seen, and there we would set bInstructor.  The endElement
    // method (below) is called when </instructor> is found, and
    // in that code we check if bInstructor is set.  If it is,
    // we can extract a string representing the instructor name 
    // from the data variable above.
    private boolean bInstructor = false;
    private boolean bCredit = false;
    private boolean bName = false;
    private boolean bNumber = false;
    private boolean bLocation = false;
    private boolean bMeetingTime = false;
    private boolean bMeetingDay = false;

    // Used by code outside the class to get the list of Student objects
    // that have been constructed.
    public Student[] getStudents() {
        return students;
    }

    // A constructor for this class.  It makes an implicit call to the
    // DefaultHandler zero arg constructor, which does the real work
    // DefaultHandler is defined in org.xml.sax.helpers.DefaultHandler;
    // imported above, and we don't need to write it.  We get its 
    // functionality by deriving from it!
    public StudentXMLHandler() {
    }

    // startElement is called when a <some element> is called as part of 
    // <some element> ... </some element> start and end tags.
    // Rather than explain everything, look at the xml file in one screen
    // and the code below in another, and see how the different xml elements
    // are handled.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (DEBUG > 1) {
            System.out.println(CLASSID + ".startElement qName: " + qName);
        }

        if (qName.equalsIgnoreCase("Students")) {
            maxStudents = Integer.parseInt(attributes.getValue("count"));
            students = new Student[maxStudents];
        } else if (qName.equalsIgnoreCase("Student")) {
            int numActivities = Integer.parseInt(attributes.getValue("numActivities"));
            String name = attributes.getValue("name");
            Student student = new Student(name, numActivities);
            addStudent(student);
            studentBeingParsed = student;
        } else if (qName.equalsIgnoreCase("Activity")) {
            String type = attributes.getValue("type");
            Activity activity = null;
            switch (type) {
                case "course":
                    activity = new Course();
                    break;
                case "club":
                    activity = new Club();
                    break;
                default:
                    System.out.println("Unknown activity: " + type);
                    break;
            }
            activityBeingParsed = activity;
            studentBeingParsed.addActivity(activity);
        } else if (qName.equalsIgnoreCase("instructor")) {
            bInstructor = true;
        } else if (qName.equalsIgnoreCase("credit")) {
            bCredit = true;
        } else if (qName.equalsIgnoreCase("name")) {
            bName = true;
        } else if (qName.equalsIgnoreCase("meetingTime")) {
            bMeetingTime = true;
        } else if (qName.equalsIgnoreCase("meetingDay")) {
            bMeetingDay = true;
        } else if (qName.equalsIgnoreCase("number")) {
            bNumber = true;
        } else if (qName.equalsIgnoreCase("location")) {
            bLocation = true;
        } else {
            System.out.println("Unknown qname: " + qName);
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Course course;
        if (bInstructor) {
            course = (Course) activityBeingParsed;
            course.setInstructor(data.toString());
            bInstructor = false;
        } else if (bCredit) {
            course = (Course) activityBeingParsed;
            course.setCredit(Integer.parseInt(data.toString()));
            bCredit = false;
        } else if (bName) {
            activityBeingParsed.setName(data.toString());
            bName = false;
        } else if (bNumber) {
            course = (Course) activityBeingParsed;
            course.setNumber(data.toString());
            bNumber = false;
        } else if (bLocation) {
            activityBeingParsed.setLocation(data.toString());
            bLocation = false;
        } else if (bMeetingTime) {
            activityBeingParsed.setMeetingTime(data.toString());
            bMeetingTime = false;
        } else if (bMeetingDay) {
            activityBeingParsed.setMeetingDay(data.toString());
            bMeetingDay = false;
        }

        if (qName.equalsIgnoreCase("Students")) {
            if (studentCount != maxStudents) {
                System.out.println("wrong number of students parsed, should be " + maxStudents + ", is " + studentCount);
            }
        } else if (qName.equalsIgnoreCase("Student")) {
            studentBeingParsed = null;
        } else if (qName.equalsIgnoreCase("Activity")) {
            activityBeingParsed = null;
        }
    }

    private void addStudent(Student student) {
        students[studentCount++] = student;
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
        if (DEBUG > 1) {
            System.out.println(CLASSID + ".characters: " + new String(ch, start, length));
            System.out.flush();
        }
    }

    @Override
    public String toString() {
        String str = "StudentsXMLHandler\n";
        str += "   maxStudents: " + maxStudents + "\n";
        str += "   studentCount: " + studentCount + "\n";
        for (Student student : students) {
            str += student.toString() + "\n";
        }
        str += "   studentBeingParsed: " + studentBeingParsed.toString() + "\n";
        str += "   activityBeingParsed: " + activityBeingParsed.toString() + "\n";
        str += "   bInstructor: " + bInstructor + "\n";
        str += "   bCredit: " + bInstructor + "\n";
        str += "   bName: " + bInstructor + "\n";
        str += "   bNumber: " + bInstructor + "\n";
        str += "   bLocation: " + bInstructor + "\n";
        str += "   bMeetingTime: " + bInstructor + "\n";
        str += "   bMeetingDay: " + bInstructor + "\n";
        return str;
    }
}
