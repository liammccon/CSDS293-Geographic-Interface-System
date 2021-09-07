package gis;

/**
 *Stores a landmark comprised of a coordinate pair and marker
 * @param <M> The type of the marker
 * @param coordinate The coordinate where the interest point is located
 * @param marker Marks what is at the coordinate
 */
public record InterestPoint<M>(Coordinate coordinate, M marker) {

/**Validates that the interest point it is called on has no null values
 * @throws NullPointerException if the interest point, coordinate, coordinate components, or marker is null
 * @return The InterestPoint it was called on
 */
    public final InterestPoint validate(){
        Coordinate.validate(coordinate);
        if (marker == null) {
            throw new NullPointerException("Marker cannot be null");
        } else {
            return this;
        }
    }

    /**Validates that the interest point passed as argument has no null values
     *  @throws NullPointerException if the interest point, coordinate, coordinate components, or marker is null
     * @return The InterestPoint passed as argument
     */
    public static final InterestPoint validate(InterestPoint interestPoint){
        if (interestPoint == null) {
            throw new NullPointerException("InterestPoint cannot be null");
        } else {
            interestPoint.validate();
            return interestPoint;
        }
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
