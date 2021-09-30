package gis.test;

import gis.userInterface.Option;

public enum Marker implements Option {
    CLASSROOM, SCHOOL, HOME, WORK, DUPLICATE;

    @Override
    public String message() {
        return toString();
    }
}
