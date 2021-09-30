package gis.userInterface;

public enum Markers implements Option{
    CLASSROOM, SCHOOL, HOME, WORK, LAB;

    @Override
    public String message() {
        return toString();
    }
}
