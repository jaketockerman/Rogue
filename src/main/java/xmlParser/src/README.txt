The code in the src directory demonstrates how to use the SAX XML parser with
java. The Test and StudentXMLHandler java files have comments explaining what
the code does, as well.

Club, Student, Course and Activity are classes which the XML parser creates
objects for as it parses the file.  What they do is not important, other than
that they have setters that the parser can call to set fields as they are
discovered, and they have constructors that the parser can call to create the
objects.  

StudentXMLHandler.java contains the actual parser.  See the comments in the 
code for details.

Test.java contains the driver code and main function.  The actions performed 
in this code I included in the main method of my Rogue implementation.
Comments in the file give details.

xmlfiles contains a single XML file that is parsed by the StudentXMLHandler.
