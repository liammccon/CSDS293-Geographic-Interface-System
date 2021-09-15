package gis;

import java.math.BigDecimal;
import java.util.Objects;

public record Rectangle(Coordinate bottomLeft, Coordinate topRight) {

    /**
     * Ensures the rectangle is valid. Will first check that no fields are null.
     * Then check that Bottom Left is either to the left of topRight
     * or along the same horizontal but with bottomLeft below topRight.
     * @return the valid rectangle
     */
    public static final Rectangle validate(Rectangle rectangle) {
        Coordinate bottomLeft = rectangle.bottomLeft;
        Coordinate topRight = rectangle.topRight;
        Objects.requireNonNull(rectangle, "Rectangle can not be null");
        Objects.requireNonNull(bottomLeft, "Bottom left coordinate can not be null");
        Objects.requireNonNull(topRight, "Top right coordinate can not be null");

        if (bottomLeft.compareTo(topRight) >= 0 ){
            throw new IllegalArgumentException("bottomLeft must either be to the left of topRight\n " +
                    "or along the same horizontal but with bottomLeft below topRight");
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
