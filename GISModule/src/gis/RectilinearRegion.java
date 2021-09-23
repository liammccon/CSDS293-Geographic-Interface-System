package gis;

import java.math.BigDecimal;
import java.util.*;

/**
 * An area of a 2 dimensional map delimited by multiple rectangles.
 */
public final class RectilinearRegion {

    private final Set<Rectangle> rectangles = new HashSet<>();

    /**
     * Private constructor that verifies each rectangle from the argument
     * then copies it to the private field rectangles
     */
    private RectilinearRegion(Set<Rectangle> rectangles) {
        validate(rectangles);
        this.rectangles.addAll(rectangles);
    }

    private static Set<Rectangle> validate(Set<Rectangle> rectangles){
        Objects.requireNonNull(rectangles);
        for (Rectangle rectangle : rectangles) {
            rectangle.validate();
        }
        return rectangles;
    }

    public Set<Rectangle> getRectangles () {
        return rectangles;
    }

    /**
     * @return a BiDimensionalMap of rectangles in which a rectangle appears in
     * all the points within the rectangle (left and bottom inclusive, top and bottom exclusive)
     */
    public BiDimensionalMap<Rectangle> rectangleMap() {
        validate(rectangles);

        BiDimensionalMap<Rectangle> grid = mapFromRectangles(rectangles);

        /*For each rectangle, get all coordinates from grid in which you will put the rectangle*/
        for (Rectangle rectangle : rectangles){
            List<Coordinate> coordinatesSlice = grid.slice(rectangle).coordinateSet();

            for (Coordinate coordinate : coordinatesSlice){
                BiDimensionalMap<Rectangle>.Updater updater = grid.getUpdater();
                updater.setCoordinate(coordinate);
                updater.addValue(rectangle);
                updater.add();
            }
        }
        return grid;
    }

    /**
     * @return a new map with an empty collection at each coordinate with x values of xCoord and y values of yCoord
     * such that xCoord is a list of all left and right values from {@code rectangles} and
     * yCoord is a list of all bottom and top values
     */
    private static BiDimensionalMap<Rectangle> mapFromRectangles(Set<Rectangle> rectangles){
        Collection<BigDecimal> xCoord = new ArrayList<>();
        Collection<BigDecimal> yCoord = new ArrayList<>();

        for (Rectangle rectangle : rectangles){
            xCoord.add(rectangle.left());
            xCoord.add(rectangle.right());
            yCoord.add(rectangle.bottom());
            yCoord.add(rectangle.top());
        }

        return new BiDimensionalMap<>(xCoord, yCoord);
    }

    /**
     *
     * @return true if there are overlapping rectangles in the RectilinearRegion
     */
    public boolean isOverlapping(){
        if (rectangles.isEmpty()){
            throw new IllegalStateException("No rectangles in the rectilinear region");
        }
        boolean overlap = false;
        RECTANGLE_LOOP:
        for (Collection<Rectangle> rectangleCollection : rectangleMap().collectionList()){
            if (rectangleCollection.size() > 1) {
                /*
                When there are two or more rectangles stored in the same point
                in the grid of rectangles, it means they are overlapping
                 */
                overlap = true;
                break RECTANGLE_LOOP;
            }
        }
        return overlap;
    }

    /**
     * @param rectangles The rectangles that delimit a rectilinear region
     * @return a valid rectilinear region in which no rectangle is null and there are no overlapping rectangles.
     * */
    public static RectilinearRegion of(Set<Rectangle> rectangles) {

        RectilinearRegion rectilinearRegion =  new RectilinearRegion(rectangles);
        if (rectangles.isEmpty()) throw new IllegalArgumentException("Rectangle set can not be empty");
        if (rectilinearRegion.isOverlapping()) throw new IllegalArgumentException("Rectangles can not be overlapping");
        return rectilinearRegion;
    }
}
