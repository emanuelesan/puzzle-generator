package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

import com.eds.netaclon.graphics.IntPosition;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.operator.DepthCalculator;
import com.eds.netaclon.puzzlegraph.renderer.ImageShow;
import com.eds.netaclon.puzzlegraph.renderer.PosMapCalculator;
import com.eds.netaclon.puzzlegraph.renderer.Visualizer;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.TickWiseOperator;
import com.eds.netaclon.puzzlegraph.util.BreadthFirstExplorer;
import com.google.gson.GsonBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import sun.jvm.hotspot.debugger.cdbg.VoidType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by emanuelesan on 30/03/16.
 */
public class FlockingRoomsRenderer implements Visualizer {
    private static final int LONG_SIDE_PIC = 800;

    private static final Logger logger = Logger.getLogger("logger");
    private final BufferedImage image;
    private final Graphics2D g;
    private final Puzzle puz;
    private final GraphicPuzzle graphicPuzzle;
    private final Rectangle view;
    private final double height = 600, width = 800;
    private Map<String, Rectangle> rectsByRoom;
    private final Queue<TickWiseOperator> operators;


    public FlockingRoomsRenderer(GraphicPuzzle gp, TickWiseOperator... operators) {
        this.graphicPuzzle = gp;
        this.puz = gp.getPuzzle();
        rectsByRoom = gp.getRectsByRoom();
        this.operators = new LinkedList<>();
        Stream.of(operators).forEachOrdered(operator -> this.operators.add(operator));

        this.view = new Rectangle(0, 0, width, height);

        image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D) image.getGraphics();
        g.setBackground(Color.black);
        g.setColor(Color.white);

    }

    @Override
    public void show() {

        try {
            ImageShow imageShow = new ImageShow(image);
            imageShow.show();
            long lastRender = 0L;
            while (true) {


                processingStep();

                if (System.currentTimeMillis() > lastRender + 16) {
                    updateImage(imageShow);
                    lastRender = System.currentTimeMillis();
                }


            }

        } catch (IOException e) {
            System.out.println("colors");
        }

    }

    private void processingStep() {
        operators.peek().tick();
        if (operators.peek().isDone() && operators.size()>1) {
            logger.info("removed operator, welcome new operator! ");
            try {
                String jsonMap = new GsonBuilder().create().toJson(graphicPuzzle);

                logger.info("puz--> " + jsonMap);
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            if (operators.peek().isPuzzleStillValid())
                operators.poll();
            else
                throw new RuntimeException("puzzle went bad.");
        }
    }

    private void updateImage(ImageShow imageShow) {
        centerCamera();
        g.clearRect(0, 0, (int) width, (int) height);

        drawGrid();

        rectsByRoom.entrySet().forEach(entry -> drawRectangle(g, entry.getValue(), colorOf(entry.getKey())));
        g.setColor(Color.ORANGE);
        puz.getDoorMap().values()
                .forEach(door -> drawDoor(g, door));
        imageShow.repaint();
    }


    private void drawRectangle(Graphics2D g, Rectangle r, Color color) {
        final double xMin = transformX(r.xMin());
        final double xMax = transformX(r.xMax());
        final double yMin = transformY(r.yMin());
        final double yMax = transformY(r.yMax());
        g.setColor(color);
        g.drawRect(rnd(xMin), rnd(yMin), Math.max(rnd(xMax - xMin), 1), Math.max(rnd(yMax - yMin), 1));
    }

    private double transformY(Double y) {
        return (y - view.yMin()) / (view.yMax() - view.yMin()) * height;
    }

    private double transformX(Double x) {
        return (x - view.xMin()) / (view.xMax() - view.xMin()) * width;
    }

    private int rnd(double value) {
        return (int) value;
    }


    private Rectangle determineView() {
        Rectangle view = null;
        for (Rectangle rec : rectsByRoom.values()) {
            if (view == null) {
                view = rec;
            } else {
                view = view.bbox(rec);
            }
        }
        //view must have the same aspect ratio of the window.
        double viewRatio = view.width() / view.height();
        double windowRatio = width / height;
        if (viewRatio < windowRatio) {   //width must become larger
            view.scale(windowRatio / viewRatio, 1);
        } else {   //height must become larger
            view.scale(1, windowRatio / viewRatio);
        }

        return view;
    }


    private void drawGrid() {
        g.setColor(Color.darkGray);
        double xStart = Math.round(view.xMin());
        double xEnd = Math.round(view.xMax());
        for (double col = xStart; col <= xEnd; col++) {
            double transCol = transformX(col);
            g.drawLine(rnd(transCol), 0, rnd(transCol), rnd(height));
        }

        double yStart = Math.round(view.yMin());
        double yEnd = Math.round(view.yMax());
        for (double row = yStart; row <= yEnd; row++) {
            double transRow = transformY(row);
            g.drawLine(0, rnd(transRow), rnd(width), rnd(transRow));
        }

    }

    private Color colorOf(String roomName) {
        if (roomName.equalsIgnoreCase("start"))
            return Color.GREEN;
        if (roomName.equalsIgnoreCase("end"))
            return Color.RED;
        return Color.WHITE;
    }

    private void centerCamera() {
//        Vector2 center = rectsByRoom
//                .values()
//                .stream()
//                .map(rect -> new Vector2(rect.x(), rect.y()))
//                .reduce((vec1, vec2) -> vec1.plus(vec2))
//                .get()
//                .times(1D / rectsByRoom.size());
        Rectangle center = determineView();

        view.push(new Vector2(center.x(), center.y()).minus(new Vector2(view.x(), view.y())).times(.1));
        view.move();
        double widthRatio = (center.width() + 4) / view.width();
        double heightRation = (center.height() + 4) / view.height();
        double ratio = widthRatio < heightRation ? heightRation : widthRatio;
        view.scale(ratio);
    }

    private void drawDoor(Graphics2D g, Door door) {
        Rectangle rec1 = rectsByRoom.get(door.getInRoomName());
        Rectangle rec2 = rectsByRoom.get(door.getOutRoomName());
        g.drawLine(rnd(transformX(rec1.x()))
                , rnd(transformY(rec1.y()))
                , rnd(transformX(rec2.x()))
                , rnd(transformY(rec2.y())));
    }

}