package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.math.Partition;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.math.PartitionCollection;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * every cycle takes O(n*k) time, where k is the number of possible positions for each room (8*6)
 * each cycle can call k * n subcycles, making it a bruteforce on a problem with an exponentially increasing
 * solution domain ((k*n) to the nth power).
 * <p>
 * at 20, this gives good results in good time (2 failures every 10000 seeds, in 90 seconds on my machine).
 * <p>
 * TODO (or at least,to try) : sub problems optimal solution can be combined to form (*dynamic programming hint*)
 * optimal solution for whole problem.
 */
public class RecursivePositioner implements TickWiseOperator {
    private static final Logger logger = Logger.getLogger("logger");

    private static final int maxX = 16, maxY = 12;
    private static final int minX = 6, minY = 6;
    private final Random random;
    private final Set<String> allRoomNames;

    private GraphicPuzzle graphicPuzzle;
    private boolean done = false;
    private boolean valid = true;
    private AtomicInteger stepCounter = new AtomicInteger(0);

    private Map<String, PartitionCollection> freedoms = new HashMap<>();
    private Map<String, Rectangle> roomShapeByName;
    private MyStack<RecursionStepParam> stack = new MyStack<>();

    public RecursivePositioner(GraphicPuzzle graphicPuzzle) {
        this.graphicPuzzle = graphicPuzzle;
        this.random = new Random(0);
        roomShapeByName = graphicPuzzle.getRectsByRoom();
        allRoomNames = graphicPuzzle.getPuzzle().allRooms().stream().map(Room::getName).collect(Collectors.toSet());
        Room startingDoor = graphicPuzzle.getPuzzle().getStart();
        stack.add(new RecursionStepParam(null, startingDoor));

    }

    @Override
    public void tick() {
        if (done) {
            return;
        }
        stepCounter.incrementAndGet();

        build();
//        logger.info("success? " + build);
        valid = graphicPuzzle.getRectsByRoom().values().size() == graphicPuzzle.getPuzzle().allRooms().size();
//        logger.info("valid? " + valid);
//        logger.info("steps : " + stepCounter.get());
        done = stack.isEmpty();
    }

    /**
     * add room to map, if possible. otherwise return false;
     */
    private void build() {
        RecursionStepParam params = stack.peek();
        if (params.possibleRects == null) {
            initStep(params);
        }
        params.roomsToBePut.forEach(freedoms::remove);
        params.roomsToBePut.forEach(roomShapeByName::remove);
        if (!params.possibleRects.hasNext()) {   //BAD MOJO! back to the fyuchar!
            stack.removeAfter(params.parent);
        } else {
            Rectangle newRec = params.possibleRects.next();
            roomShapeByName.put(params.currentRoom.getName(), newRec);
            PartitionCollection partitionCollection = partitionCollectionFrom(newRec);
            freedoms.put(params.currentRoom.getName(), partitionCollection);

            List<Room> siblingRooms = params.connectedRooms
                    .stream()
                    .filter(room -> !roomShapeByName.containsKey(room))
                    .map(nextRoom -> graphicPuzzle.getPuzzle().getRoom(nextRoom)).collect(Collectors.toList());

            if (siblingRooms.isEmpty()) {   //SUCCESS!!
                RecursionStepParam current = params;
                stack.remove(current);
                /*
                    recursively remove steps from stack!
                */
                while (current.parent != null && stack.indexOf(current.parent) == stack.size() - 1) { //parent is ok!
                    stack.remove(current.parent);
                    current = current.parent;
                }
            } else
                siblingRooms.forEach(nextRoom ->
                        stack.push(new RecursionStepParam(params, nextRoom)
                        ));


        }


    }

    private void initStep(RecursionStepParam params) {
        params.connectedRooms = graphicPuzzle
                .getPuzzle()
                .getDoors(params.currentRoom)
                .stream()
                .map(door -> door.getOtherRoom(params.currentRoom, graphicPuzzle.getPuzzle()))
                .map(Room::getName)
                .collect(Collectors.toList());


        params.possibleRects = createPossiblePositions(params);

        params.roomsToBePut = new HashSet<>(allRoomNames);
        params.roomsToBePut.removeAll(roomShapeByName.keySet());
    }

    private Iterator<Rectangle> createPossiblePositions(RecursionStepParam params) {
        List<String> connectedRooms = params.connectedRooms;
        Stream<Rectangle> possibleRects;

        List<PartitionCollection> possibleAreas = connectedRooms
                .stream()
                .filter(freedoms::containsKey)
                .map(freedoms::get)
                .collect(Collectors.toList());

        if (possibleAreas.isEmpty()) {
            possibleRects = Stream.of(new Rectangle(0, 0, maxX, maxY));
        } else {
            logger.finer("possible areas: " + possibleAreas);

            PartitionCollection intersection = possibleAreas.stream().reduce(PartitionCollection::intersect).get();
            possibleRects = intersection.partitions().stream()
                    .map(part -> excluderooms(part, roomShapeByName))
                    .filter(part -> !part.isDegenerate())
                    .flatMap(sp ->
                            {
                                if (params.currentRoom.getItemNames().size() > 0)
                                    return sp.complete(generateX(), generateY());
                                return random.nextBoolean() ? sp.complete(minX, maxY) : sp.complete(maxY, minX);
                            }
                    )
                    .filter(RecursivePositioner::isWideAndHighEnough)
                    .sorted((r1, r2) -> random.nextInt(2) - 1);
        }
        //   logger.finer("there are " + possibleRects.size() + "possible positions for this room");

        return possibleRects.iterator();
    }

    private static boolean isWideAndHighEnough(Rectangle rectangle) {
        return rectangle.width() >= minX && rectangle.width() <= maxX
                && rectangle.height() >= minY && rectangle.height() <= maxY;
    }

    private int generateY() {
        return random.nextInt(maxY - minY) + minY;
    }

    private int generateX() {

        return random.nextInt(maxX - minX) + minX;

    }

    /**
     * O(n) complexity
     */
    private Partition excluderooms(Partition part, Map<String, Rectangle> roomShapeByName) {
        Partition part1 = part;
        for (Rectangle rect : roomShapeByName.values()) {
            part1 = part1.exclude(rect);
            if (part.isDegenerate()) return part;
        }
        return part1;
    }


    /**
     * for each side of the rectangle, create a space partition which a little larger than the given side.
     * this allows potentially to place two rooms on the same side.
     */
    private PartitionCollection partitionCollectionFrom(Rectangle rect) {
        //extra space causes narrower space to place a door, leading to positioning doors to angles.
        // this is empirically the best fit for the value.
        int halfWidth = (int) Math.floor(minX / 3);
        int halfHeight = (int) Math.floor(minY / 3);

        int minXBound = rect.xMin() - halfWidth;
        int maxXBound = rect.xMax() + halfWidth;
        int minYBound = rect.yMin() - halfHeight;
        int maxYBound = rect.yMax() + halfHeight;

        Partition bottom = new Partition(minXBound, Partition.A_LOT, maxXBound, rect.yMin());
        Partition top = new Partition(minXBound, rect.yMax(), maxXBound, Partition.A_LOT);

        Partition right = new Partition(rect.xMax(), minYBound, Partition.A_LOT, maxYBound);
        Partition left = new Partition(Partition.A_LOT, minYBound, rect.xMin(), maxYBound);

        return new PartitionCollection(Arrays.asList(bottom, top, right, left));

    }


    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean isPuzzleStillValid() {
        return valid;
    }

    @Override
    public long steps() {
        return stepCounter.longValue();
    }


    private static final class RecursionStepParam {
        final Room currentRoom;
        final RecursionStepParam parent;
        HashSet<String> roomsToBePut = null;
        Iterator<Rectangle> possibleRects = null;
        public List<String> connectedRooms = null;

        public RecursionStepParam(RecursionStepParam parentRoom, Room currentRoom) {
            this.parent = parentRoom;
            this.currentRoom = currentRoom;
        }
    }

    @Override
    public long averageSteps() {
        return (long) Math.pow(1.40, graphicPuzzle.getPuzzle().allRooms().size());
    }
}
