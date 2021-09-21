package gis;

import org.w3c.dom.css.Rect;

import java.math.BigDecimal;
import java.text.Bidi;
import java.util.*;

/**
 * An area of a 2 dimensional map delimited by multiple rectangles.
 */
public final class RectilinearRegion {

    private final Set<Rectangle> rectangles = new HashSet<>(); //todo that right? doesnt say what to initialize as

    public Set<Rectangle> getRectangles () {
        return rectangles;
    }

    /**
     * Private constructor that verifies each rectangle from the argument
     * then copies it to the private field rectangles
     */
    private RectilinearRegion(Set<Rectangle> rectangles) {
        validateRectangles(rectangles);
        this.rectangles.addAll(rectangles);
    }

    private static Set<Rectangle> validateRectangles (Set<Rectangle> rectangles){
        Objects.requireNonNull(rectangles);
        rectangles.stream()
                .forEach(rectangle -> {
                    rectangle.validate();
                });
        return rectangles;
    }

    /**
     * @return a BiDimensionalMap of rectangles in which a rectangle appears in
     * all the points within the rectangle (left and bottom inclusive, top and bottom exclusive)
     */
    public BiDimensionalMap<Rectangle> rectangleMap() {
        validateRectangles(rectangles); //todo not needed? and test

        BiDimensionalMap<Rectangle> grid = mapFromRectangles(rectangles);

        for (Rectangle rectangle : rectangles){
            List<Coordinate> coordinatesSlice = grid.slice(rectangle).coordinateSet();

            for (Coordinate coordinate : coordinatesSlice){
                BiDimensionalMap.Updater updater = grid.getUpdater();
                updater.setCoordinate(coordinate);
                updater.addValue(rectangle);
                updater.add();
            }
        }
        return grid;
    }

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
        //todo test
        if (rectangles.isEmpty()){
            throw new IllegalStateException("No rectangles in the rectilinear region");
        }
        boolean overlap = false;
        for (Collection<Rectangle> rectangleCollection : rectangleMap().collectionList()){
            if (rectangleCollection.size() > 1){
                /*
                When there are two or more rectangles stored in the same point
                in the grid of rectangles, it means they are overlapping
                 */
                overlap = true;
            }
        }
        return overlap;
    }

    /**
     * @param rectangles The rectangles that delimit a rectilinear region
     * @return a valid rectilinear region in which no rectangle is null and there are no overlapping rectangles.
     * */
    public static RectilinearRegion of(Set<Rectangle> rectangles) {
        //todo test, change return null to throw exception?
        //Ensures rectangles is not null and the rectangles inside are valid todo just call validate?
        RectilinearRegion rectilinearRegion =  new RectilinearRegion(rectangles); //Constructor does the validation
        if (rectangles.isEmpty()) return null;
        if (rectilinearRegion.isOverlapping()) return null;
        return rectilinearRegion;
    }


}
