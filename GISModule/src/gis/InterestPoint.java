package gis;

import java.util.Objects;

/**
 *Stores a landmark comprised of a coordinate pair and marker
 * @param <M> The type of the marker
 * @param coordinate The coordinate where the interest point is located
 * @param marker Marks what is at the coordinate
 */
public record InterestPoint<M>(Coordinate coordinate, M marker) {

    public static final InterestPoint validate(InterestPoint interestPoint){
        Objects.requireNonNull(interestPoint, "InterestPoint cannot be null");
        Objects.requireNonNull(interestPoint.marker, "marker cannot be null");
        interestPoint.coordinate.validate();
        return interestPoint;
    }

    public final InterestPoint validate(){
        return InterestPoint.validate(this);
    }

    public boolean hasMarker(M marker){
        validate();
        return this.marker.equals(marker);
    }

    public String toString(){
        validate();
        return marker + " is located at " + this.coordinate.toSimpleString();
    }
}
