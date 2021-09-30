package gis.userInterface;

import gis.BiDimensionalMap;
import gis.Coordinate;
import gis.InterestPoint;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Main {

    public static void main (String [] args){
        firstScreen();
    }

    //------------------------------------------------------------------------------------------------------------------
    //Screens
    private static void firstScreen(){
        pointMap = newPointMap.get();
        final String FIRST_MESSAGE = "Would you like to make a: ";

        LinkOption[] linkOptions = new LinkOption[] {
                new LinkOption("Map of interest points" , ()-> pointMapScreen()),
                new LinkOption("Rectilinear region", ()-> {
                    System.out.println("No rectilinear regions yet");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    firstScreen();
                }),
                new LinkOption("Exit" , ()-> System.exit(0))
        };

        anyOptionScreen(FIRST_MESSAGE, linkOptions);
    }

    private static void pointMapScreen(){

        final String EMPTY_MAP = "You have an empty map. You can: ";
        final String MAP_WITH_POINTS = "You have a map with interest points: ";

        LinkOption[] linkOptions = new LinkOption[] {
                new LinkOption("Add interest point", ()-> addPointsScreen()),
                new LinkOption("Set interest point", ()-> setPointsScreen()),
                new LinkOption("Clear map", ()->clearMap()),
                new LinkOption("Go back", ()-> firstScreen())
        };

        String message = "";
        if(pointMap.collectionSize() == 0)
            message = EMPTY_MAP;
        else {
            message = MAP_WITH_POINTS + '\n' + toSimpleStringBuilder(pointMap) +
                    "\nHere is the map, showing the number of points at any coordinate" + mapGraph();
        }

        anyOptionScreen(message, linkOptions);

    }

    private static void addPointsScreen() {
        InterestPoint<Markers> point = getPoint();
        System.out.println("\nAdding interest point: " + point);
        BiDimensionalMap.Updater updater = pointMap.getUpdater();
        updater.setCoordinate(point.coordinate()).addValue(point).add();
        pointMapScreen();
    }

    private static void setPointsScreen() {
        InterestPoint<Markers> point = getPoint();
        System.out.println("\nAdding interest point: " + point);
        BiDimensionalMap.Updater updater = pointMap.getUpdater();
        updater.setCoordinate(point.coordinate()).addValue(point).set();
        pointMapScreen();
    }






    //------------------------------------------------------------------------------------------------------------------
    //Helpers
    private final static Supplier<BiDimensionalMap<InterestPoint<Markers>>> newPointMap = BiDimensionalMap::new;
    private static BiDimensionalMap pointMap;


    private final static String CHOOSE_OPTION =  "Enter your option: ";
    private final static String INVALID_OPTION = "That option does not exist, try again: ";
    private final static String INVALID_INPUT ="Invalid input. ";

    final private static int UPPER_COORDINATE_BOUND = 15;

    private static void clearMap() {
        pointMap = newPointMap.get();
        pointMapScreen();
    }

    private record LinkOption(String message, GoTo goTo) implements Option {
        public String toString() {
            return message;
        }
    }

    private static Option[] showOptions(Option[] options){
        for (int i = 0; i < options.length; i++){
            System.out.println( i + ". " + options[i]);
        }
        return options;
    }

    private static Option getChosenOption(Option[] options){
        int choice = scanPositiveInt(options.length-1, CHOOSE_OPTION);

        assert(choice!=-1);
        return options[choice];
    }

    public static int scanPositiveInt(int upperBound, String message){
        System.out.print(message);
        boolean optionChosen = false;
        int choice = -1;

        while (!optionChosen) {
            Scanner scan = new Scanner(System.in);

            try {
                choice = scan.nextInt();

                if (choice < 0 || choice > upperBound)
                    System.out.print("Please enter an integer between " + 0 + " and " + upperBound + ": ");
                else
                    return choice;
            } catch (Exception e) {
                System.out.print(INVALID_INPUT);
                System.out.print(message);
            }
        }
        throw new RuntimeException("No number chosen");
    }

    private static LinkOption getChosenLinkOption(LinkOption[] linkOptions){
        return (LinkOption) getChosenOption(linkOptions);
    }

    private static void anyOptionScreen(String message, LinkOption[] linkOptions) {
        clearScreen();
        System.out.println(message);
        showOptions(linkOptions);
        getChosenLinkOption(linkOptions).goTo.goToScreen();
    }

    private static void clearScreen(){
        final int NUM_OF_LINES = 15;
        IntStream.range(0, NUM_OF_LINES).forEachOrdered(n -> {
            System.out.println();
        });

    }

    private static InterestPoint<Markers> getPoint(){

        final String ADD_COORDINATE_X = "Enter a positive integer for the x location of the marker: ";
        int x = scanPositiveInt(UPPER_COORDINATE_BOUND, ADD_COORDINATE_X);

        final String ADD_COORDINATE_Y = "Enter a positive integer for the y location of the marker: ";
        int y = scanPositiveInt(UPPER_COORDINATE_BOUND, ADD_COORDINATE_Y);

        final String SELECT_MARKER= "Select a marker to add: ";
        Option []  markerOptions = Markers.values();

        clearScreen();
        System.out.println(SELECT_MARKER);
        Markers chosenMarker = (Markers) getChosenOption(showOptions(markerOptions));

        Coordinate coordinate = new Coordinate(new BigDecimal(x), new BigDecimal(y));
        InterestPoint<Markers> point = new InterestPoint<>(coordinate, chosenMarker);
        point.validate();
        return point;
    }

    private static StringBuilder toSimpleStringBuilder(BiDimensionalMap<InterestPoint<Markers>> map) {
        StringBuilder stringBuilder = new StringBuilder();
        map.collectionList().stream()
                .flatMap(Collection::stream)
                .forEach(point -> stringBuilder.append(point.toString()).append(", "));
        if (!stringBuilder.isEmpty())
            stringBuilder.deleteCharAt(stringBuilder.length() - 1); //remove last comma
        return stringBuilder;
    }

    private static StringBuilder mapGraph(){
        StringBuilder mapString = new StringBuilder("\n");

        for (int y = UPPER_COORDINATE_BOUND; y >=0; y--){

            for(int x = 0; x<= UPPER_COORDINATE_BOUND; x++){

                mapString.append(cell(x, y));
            }
            mapString.append("\n");
        }
        return mapString;
    }

    private static int numOfPoints(int x, int y) {
        Coordinate coordinate = new Coordinate(new BigDecimal(x), new BigDecimal(y));
        if (pointMap.coordinateSet().contains(coordinate)){
            return pointMap.get(coordinate).size();
        } else return 0;
    }

    private static char[] cell(int x, int y) {
        char[] chars = new char[2];
        int numOfPoints = numOfPoints(x, y);

        if (numOfPoints > 0) {
            return numberToChars(numOfPoints);
        } else if (isVerticleAxis(x, y)) {
            chars[0] = '|';
            chars[1] = ' ';
        } else if(isHorizontalAxis(x, y)){
            chars[0] = '_';
            chars[1] = '_';
        } else {
            chars[0] = ' ';
            chars[1] = ' ';
        }
        return chars;
    }

    private static char[] numberToChars(Integer num) {
        char[] chars = new char[2];

        chars[0] = num.toString().charAt(0);

        if (num < 10) {
            chars[1] = ' ';
        } else {
            chars[1] = num.toString().charAt(1);
        }
        return chars;
    }

    private static boolean isVerticleAxis(int x, int y) {
        return (x == 0)  && (y!=0);
    }

    private static boolean isHorizontalAxis(int x, int y){
        return (x!=0) && (y==0);
    }
}
