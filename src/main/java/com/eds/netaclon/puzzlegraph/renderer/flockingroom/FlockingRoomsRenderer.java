package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

import com.eds.netaclon.graphics.IntPosition;
import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.Room;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.operator.DepthCalculator;
import com.eds.netaclon.puzzlegraph.renderer.ImageShow;
import com.eds.netaclon.puzzlegraph.renderer.PosMapCalculator;
import com.eds.netaclon.puzzlegraph.renderer.Visualizer;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.TickWiseOperator;
import com.eds.netaclon.puzzlegraph.util.BreadthFirstExplorer;
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

/**
 * Created by emanuelesan on 30/03/16.
 */
public class FlockingRoomsRenderer implements Visualizer {
    private static final int LONG_SIDE_PIC = 800;

    private static final Logger logger = Logger.getLogger("logger");
    private final BufferedImage image;
    private final Graphics2D g;
    private final Puzzle puz;
    private Rectangle view;
    private double height,width;
    private  Map<String, Rectangle> rectsByRoom;
    private final Queue<TickWiseOperator> operators;


    public FlockingRoomsRenderer(Puzzle puz,FlockingRoomsPositioner flockingRoomsPositioner,TickWiseOperator corridorConnector, TickWiseOperator doorCreator) {
        this.puz = puz;
        rectsByRoom=flockingRoomsPositioner.getRectsByRoom();
        operators = new LinkedList<>();
        operators.add(flockingRoomsPositioner);
        operators.add(corridorConnector);
        operators.add(doorCreator);

        this. view=determineView();
        determineViewDimensions();

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


                if (operators.peek().isDone())
                {
                    logger.info("removed operator, welcome new operator! ");
                    try {
                        String jsonMap = new ObjectMapper().writeValueAsString(rectsByRoom);
                        logger.info("map: " + jsonMap);

                        String jsonpuz = new ObjectMapper().writeValueAsString(puz);
                        logger.info("puz: " + jsonpuz);
                    }catch (Exception e)
                    {logger.info(e.getMessage());}
                    operators.poll();
                }
                operators.peek().tick();

                if (System.currentTimeMillis()>lastRender+16) {
                    centerCamera();
                    g.clearRect(0, 0, (int) width, (int) height);

                    drawGrid();

                    rectsByRoom.entrySet().forEach(entry -> drawRectangle(g, entry.getValue(), colorOf(entry.getKey())));
                    g.setColor(Color.ORANGE);
                    puz.getDoorMap().values()
                            .forEach(door -> drawDoor(g, door));
                    imageShow.repaint();
                    lastRender=System.currentTimeMillis();
                }


            }

        } catch (IOException  e) {
            System.out.println("colors");
        }

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
        return (int)value;
    }

    private void determineViewDimensions() {

         height = view.height();
         width = view.width();
        double hwRatio = height / width;
        if (hwRatio < 0) {
            width = LONG_SIDE_PIC;
            height = LONG_SIDE_PIC * hwRatio;

        } else {
            height = LONG_SIDE_PIC;
            width = LONG_SIDE_PIC / hwRatio;
        }

    }

    private  Rectangle determineView() {
        Rectangle view = null;
        for (Rectangle rec : rectsByRoom.values()) {
            if (view == null) {
                view = rec;
            } else {
                view = view.bbox(rec);
            }
        }
        return view;
    }




    private void drawGrid() {
        g.setColor(Color.darkGray);
        double xStart = Math.round(view.xMin());
        double xEnd = Math.round(view.xMax());
        for (double col = xStart;col<=xEnd;col++)
        {
            double transCol = transformX(col);
            g.drawLine(rnd(transCol),0,rnd(transCol),rnd(height));
        }

        double yStart = Math.round(view.yMin());
        double yEnd = Math.round(view.yMax());
        for (double row = yStart;row<=yEnd;row++)
        {
            double transRow = transformY(row);
            g.drawLine(0,rnd(transRow),rnd(width),rnd(transRow));
        }

    }

    private Color colorOf(String roomName) {
        if( roomName.equalsIgnoreCase("start"))
            return Color.GREEN;
        if( roomName.equalsIgnoreCase("end"))
            return Color.RED;

        return Color.WHITE;

    }

    private void centerCamera() {
        Vector2 center = rectsByRoom
                .values()
                .stream()
                .map(rect -> new Vector2(rect.x(), rect.y()))
                .reduce((vec1, vec2) -> vec1.plus(vec2))
                .get()
                .times(1D / rectsByRoom.size());
        view.push(center.minus(new Vector2(view.x(), view.y())).times(.1));
        view.move();
        Rectangle bBox = determineView();
        double widthRatio =   (bBox.width()+4)/view.width();
        double heightRation =   (bBox.height()+4)/view.height();
        double ratio = widthRatio<heightRation?heightRation:widthRatio;
        view.scale((3+ratio)/4);

    }

    private void drawDoor(Graphics2D g, Door door) {
        Rectangle rec1 = rectsByRoom.get(door.getInRoomName());
        Rectangle rec2 = rectsByRoom.get(door.getOutRoomName());
        g.drawLine(rnd(transformX(rec1.x()))
                ,rnd(transformY(rec1.y()))
                ,rnd(transformX(rec2.x()))
                ,rnd(transformY(rec2.y())));
    }


}
