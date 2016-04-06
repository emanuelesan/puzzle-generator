package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Vector2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * creates corridors, which are new rooms actually, added to the puzzle,
 * between rooms that have a door connecting them but are not adjacent.
 */
public class CorridorConnector implements TickWiseOperator {
    private final Map<String, Rectangle> rectsByRoom;
    private final Puzzle puz;

    public CorridorConnector(Puzzle puz, Map<String, Rectangle> rectsByRoom) {
        this.rectsByRoom = rectsByRoom;
        this.puz = puz;
    }

    @Override
    public void tick() {
        List<Door> corridorsNeeded = getCorridorsNeeded();
        corridorsNeeded.stream().forEach(door ->
                {
                    Rectangle corridor = createCorridor(door);
                    Room room = puz.addRoom(door);
                    rectsByRoom.put(room.getName(), corridor);
                }
        );
    }

    private Rectangle createCorridor(Door door) {
        Rectangle inRect = rectsByRoom.get(door.getInRoom(puz).getName());
        Rectangle outRect = rectsByRoom.get(door.getOutRoom(puz).getName());
        Rectangle corridor = inRect.inBetween(outRect);

        for (Rectangle rec : rectsByRoom.values()) {
            if (rec != outRect && inRect != rec) {
                corridor = subtract(corridor, rec);
            }
        }
        return corridor;
    }

    private Rectangle subtract(Rectangle corridor, Rectangle rec) {

        return corridor;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    private List<Door> getCorridorsNeeded() {
        return rectsByRoom
                .keySet()
                .stream()
                .flatMap(roomName ->puz.getRoom( roomName).getDoors().stream())
                .distinct()
                .filter(this::areRoomsTooFar)
                .collect(Collectors.toList());
    }

    private boolean areRoomsTooFar(Door door) {
        Rectangle inRect = rectsByRoom.get(door.getInRoom(puz).getName());
        Rectangle outRect = rectsByRoom.get(door.getOutRoom(puz).getName());
        Vector2 roomDims = new Vector2(
                (inRect.width() + outRect.width()) / 2D,
                (inRect.height() + outRect.height()) / 2D);
        Vector2 distance =
                new Vector2(
                        Math.abs(inRect.x() - outRect.x()),
                        Math.abs(inRect.y() - outRect.y()));
        Vector2 deficit = distance.minus(roomDims);
        return (Math.round(deficit.x) > 0 && Math.round(deficit.y) > 0);
    }

}
