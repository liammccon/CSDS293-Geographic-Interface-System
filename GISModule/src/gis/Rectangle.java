package gis;

import java.math.BigDecimal;
import java.util.Objects;

public record Rectangle(Coordinate bottomLeft, Coordinate topRight) {

    /**
     * Ensures the rectangle is valid. Will first check that no fields are null.
     * Then checks that Bottom Left is below and/or to the left of topRight.
     * Horizontal and vertical lines are valid but a point is not.
     * @return the valid rectangle
     */
    public static final Rectangle validate(Rectangle rectangle) {

        Coordinate bottomLeft = rectangle.bottomLeft;
        Coordinate topRight = rectangle.topRight;
        Objects.requireNonNull(rectangle, "Rectangle can not be null");
        Objects.requireNonNull(bottomLeft, "Bottom left coordinate can not be null");
        Objects.requireNonNull(topRight, "Top right coordinate can not be null");

        boolean equalPoints = bottomLeft.compareTo(topRight) == 0;
        boolean horizontalInvalid = bottomLeft.x().compareTo(topRight.x()) > 0;
        boolean verticalInvalid = bottomLeft.y().compareTo(topRight.y()) > 0;

        if (equalPoints || horizontalInvalid || verticalInvalid ){
            throw new IllegalArgumentException("bottomLeft must be below and to the left of topRight");
        }
        return rectangle;
    }

    /**
     * Ensures the rectangle is valid. Will first check that no fields are null.
     * Then check that Bottom Left is either to the left of topRight
     * or along the same horizontal but with bottomLeft below topRight.
     * @return the valid rectangle
     */
    public final Rectangle validate(){
        return Rectangle.validate(this);
    }

    public BigDecimal left() {
        validate();
        return bottomLeft.x();
    }

    public BigDecimal right() {
        validate();
        return topRight.x();
    }

    public BigDecimal bottom() {
        validate();
        return bottomLeft.y();
    }

    public BigDecimal top(){
        validate();
        return topRight.y();
    }

    public String toString(){
        return "Rectangle with bottom left at " + bottomLeft.toSimpleString() + " and top right at " + topRight.toSimpleString();
    }

}
