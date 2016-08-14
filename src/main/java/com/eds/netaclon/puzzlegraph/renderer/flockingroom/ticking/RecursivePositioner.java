package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.math.Partition;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.math.PartitionCollection;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by emanuelesan on 07/08/16.
 */
public class RecursivePositioner implements TickWiseOperator {
    private static final Logger logger = Logger.getLogger("logger");

    private static final int maxX = 8, maxY = 6;
    private static final int minX = 6, minY = 4;
    private final Random random;
    private final Set<String> allRoomNames;

    private GraphicPuzzle graphicPuzzle;
    boolean done = false;
    private boolean valid = true;

    public RecursivePositioner(GraphicPuzzle graphicPuzzle) {
        this.graphicPuzzle = graphicPuzzle;
        this.random = new Random(0);
        allRoomNames = graphicPuzzle.getPuzzle().allRooms().stream().map(Room::getName).collect(Collectors.toSet());
    }

    @Override
    public void tick() {
        if (done) {
            return;
        }
        Puzzle puzzle = graphicPuzzle.getPuzzle();

        Map<String, Rectangle> roomShapeByName = new HashMap<>();
        Room startingDoor = puzzle.getStart();
        boolean build = build(roomShapeByName, new HashMap<>(), startingDoor);
//        logger.info("success? " + build);
        graphicPuzzle.getRectsByRoom().putAll(roomShapeByName);
        valid = graphicPuzzle.getRectsByRoom().values().size() == puzzle.allRooms().size();
//        logger.info("valid? " + valid);
        done = true;
    }

    /**
     * add room to map, if possible. otherwise return false;
     */
    private boolean build(Map<String, Rectangle> roomShapeByName, Map<String, PartitionCollection> freedoms, Room r) {
        List<String> otherRooms = graphicPuzzle
                .getPuzzle()
                .getDoors(r)
                .stream()
                .map(door -> door.getOtherRoom(r, graphicPuzzle.getPuzzle()))
                .map(Room::getName)
                .collect(Collectors.toList());

        List<PartitionCollection> possibleAreas = otherRooms
                .stream()
                .filter(freedoms::containsKey)
                .map(freedoms::get)
                .collect(Collectors.toList());
        List<Rectangle> possibleRects;

        if (possibleAreas.isEmpty()) {
            possibleRects = Collections.singletonList(new Rectangle(0, 0, maxX, maxY));
        } else {
            logger.finer("possible areas: " + possibleAreas);

            PartitionCollection intersection = possibleAreas.stream().reduce(PartitionCollection::intersect).get();
            possibleRects = intersection.partitions().stream()
                    .map(part -> excluderooms(part, roomShapeByName))
                    .filter(part -> !part.isDegenerate())
                    .map(sp -> sp.complete(generateX(), generateY()))
                    .filter(RecursivePositioner::isWideAndHighEnough)
                    .collect(Collectors.toList());
        }

        Set<String> roomsToBePut = new HashSet<>(allRoomNames);
        roomsToBePut.removeAll(roomShapeByName.keySet());
        logger.finer("there are " + possibleRects.size() + "possible positions for this room");
        for (Rectangle newRec : possibleRects) {
            roomShapeByName.put(r.getName(), newRec);

            PartitionCollection partitionCollection = partitionCollectionFrom(newRec);
            freedoms.put(r.getName(), partitionCollection);

            boolean success = otherRooms
                    .stream()
                    .filter(room -> !roomShapeByName.containsKey(room))
                    .map(nextRoom ->
                            build(roomShapeByName, freedoms, graphicPuzzle.getPuzzle().getRoom(nextRoom))
                    ).reduce(Boolean::logicalAnd).orElse(true);

            if (success) {
                return true;
            }
            roomsToBePut.forEach(freedoms::remove);
            roomsToBePut.forEach(roomShapeByName::remove);
            //if not, maybe next rectangle could be the good one!
            freedoms.remove(r.getName());
            roomShapeByName.remove(r.getName());
        }
        return false;
    }

    private static boolean isWideAndHighEnough(Rectangle rectangle) {
        return rectangle.width() >= minX && rectangle.width() <= maxX
                && rectangle.height() >= minY && rectangle.height() <= maxY;
    }

    private float generateY() {
        return random.nextInt(maxY - minY) + minY;
    }

    private int generateX() {
        return random.nextInt(maxX - minX) + minX;

    }

    private boolean overlapsRooms(Rectangle rect, Map<String, Rectangle> roomShapeByName) {
        return roomShapeByName.values().stream().map(rect::intersectionArea).filter(area -> area >= 1).count() > 0;
    }

    private Partition excluderooms(Partition part, Map<String, Rectangle> roomShapeByName) {
        Partition part1 = part;
        for (Rectangle rect : roomShapeByName.values()) {
            part1 = part1.exclude(rect);
        }
        return part1;
    }


    private PartitionCollection partitionCollectionFrom(Rectangle rect) {
        Partition bottom = new Partition(rect.xMin(), Float.NaN, rect.xMax(), rect.yMin());
        Partition top = new Partition(rect.xMin(), rect.yMax(), rect.xMax(), Float.NaN);

        Partition right = new Partition(rect.xMax(), rect.yMin(), Float.NaN, rect.yMax());
        Partition left = new Partition(Float.NaN, rect.yMin(), rect.xMin(), rect.yMax());

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
}
