package gis;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An area of a 2 dimensional map delimited by multiple rectangles.
 */
public final class RectilinearRegion {

    private final Set<Rectangle> rectangles ;

    /**
     * Private constructor that verifies each rectangle from the argument
     * then copies it to the private field rectangles
     */
    private RectilinearRegion(Set<Rectangle> rectangles) {
        validate(rectangles);
        this.rectangles = new HashSet<>(rectangles);
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

        for (Rectangle rectangle : rectangles){
            //todo added public method addEverywhere(coordinates, value) in BiDimensionalMap
            grid.addEverywhere(grid.slice(rectangle).coordinateSet(), rectangle);

        }

        /*todo remove
        //For each rectangle, get all coordinates from grid in which you will put the rectangle
        for (Rectangle rectangle : rectangles){
            List<Coordinate> coordinatesSlice = grid.slice(rectangle).coordinateSet();
            for (Coordinate coordinate : coordinatesSlice){
                BiDimensionalMap<Rectangle>.Updater updater = grid.getUpdater();
                updater.setCoordinate(coordinate).addValue(rectangle).add();
            }
        }*/
        return grid;
    }


    /**
     * @return a new map with an empty collection at each coordinate with x values of xCoord and y values of yCoord
     * such that xCoord is a list of all left and right values from {@code rectangles} and
     * yCoord is a list of all bottom and top values
     */
    private static BiDimensionalMap<Rectangle> mapFromRectangles(Set<Rectangle> rectangles){

        Collection<BigDecimal> xCoord = rectangles.stream()
                .flatMap(rectangle -> Stream.of(rectangle.left(), rectangle.right()))
                .collect(Collectors.toList());

        Collection<BigDecimal> yCoord = rectangles.stream()
                .flatMap(rectangle -> Stream.of(rectangle.bottom(), rectangle.top()))
                .collect(Collectors.toList());

        return new BiDimensionalMap<>(xCoord, yCoord);
    }

    /**
     *
     * @return true if there are overlapping rectangles in the RectilinearRegion
     */
    public boolean isOverlapping(){

        return rectangleMap().collectionList().stream()
                .anyMatch(rectangleCollection -> rectangleCollection.size() > 1);
    }

    /**
     * @param rectangles The rectangles that delimit a rectilinear region
     * @return a valid rectilinear region in which no rectangle is null and there are no overlapping rectangles.
     * */
    public static RectilinearRegion of(Set<Rectangle> rectangles) {
        RectilinearRegion rectilinearRegion =  new RectilinearRegion(rectangles);
        if (rectangles.isEmpty()) throw new IllegalArgumentException();
        if (rectilinearRegion.isOverlapping()) throw new IllegalArgumentException();
        return rectilinearRegion;
    }
}
