package com.eds.netaclon.puzzlegraph.renderer.flockingroom;

import com.eds.netaclon.puzzlegraph.graphic.GraphicPuzzle;
import com.eds.netaclon.puzzlegraph.item.Door;
import com.eds.netaclon.puzzlegraph.renderer.ImageShow;
import com.eds.netaclon.puzzlegraph.renderer.Visualizer;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking.TickWiseOperator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;


public class FlockingRoomsRenderer extends GraphicPuzzleProcessor implements Visualizer {

    private final BufferedImage image;
    private final Graphics2D g;
    private Rectangle view;
    private final float height = 600, width = 800;
    private final Map<String, Rectangle> rectsByRoom;
    private final GraphicPuzzle graphicPuzzle;


    public FlockingRoomsRenderer(GraphicPuzzle gp, TickWiseOperator... operators) {
        super(gp,operators);
        this.rectsByRoom = gp.getRectsByRoom();
        this.graphicPuzzle  =gp;
        //    this.view = new Rectangle(0, 0, width, height);

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
                Thread.sleep(100);
            }

        } catch (Exception e) {
            System.out.println("colors");
        }

    }

    private void updateImage(ImageShow imageShow) {
        centerCamera();
        g.clearRect(0, 0, (int) width, (int) height);

        drawGrid();

        rectsByRoom.entrySet().forEach(entry -> drawRectangle(g, entry.getValue(), colorOf(entry.getKey())));
        graphicPuzzle.getRectsByItem().entrySet().forEach(entry -> drawRectangle(g, entry.getValue(), Color.LIGHT_GRAY));
        g.setColor(Color.ORANGE);
        puz.getDoorMap().values()
                .forEach(door -> drawDoor(g, door));
        graphicPuzzle.getRectsByDoor().values().forEach(room -> drawRectangle(g, room, Color.CYAN));


        drawRoomNames(g);

        imageShow.repaint();
    }

    private void drawRoomNames(Graphics2D g) {
        rectsByRoom.entrySet().forEach(
                entry -> g.drawString(entry.getKey(), transformX(entry.getValue().x()), transformY(entry.getValue().y()))
        );
    }


    private void drawRectangle(Graphics2D g, Rectangle r, Color color) {
        final float xMin = transformX(r.xMin());
        final float xMax = transformX(r.xMax());
        final float yMin = transformY(r.yMin());
        final float yMax = transformY(r.yMax());
        g.setColor(color);
        g.drawRect(rnd(xMin), rnd(yMin), Math.max(rnd(xMax - xMin), 1), Math.max(rnd(yMax - yMin), 1));
    }

    private float transformY(float y) {
        return (y - view.yMin()) / (view.yMax() - view.yMin()) * height;
    }

    private float transformX(float x) {
        return (x - view.xMin()) / (view.xMax() - view.xMin()) * width;
    }

    private int rnd(float value) {
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
        float viewRatio = view.width() / view.height();
        float windowRatio = width / height;
        if (viewRatio < windowRatio) {   //width must become larger
            view.scale(windowRatio / viewRatio, 1);
        } else {   //height must become larger
            view.scale(1, windowRatio / viewRatio);
        }

        return view;
    }


    private void drawGrid() {
        g.setColor(Color.darkGray);
        float xStart = Math.round(view.xMin());
        float xEnd = Math.round(view.xMax());
        for (float col = xStart; col <= xEnd; col++) {
            float transCol = transformX(col);
            g.drawLine(rnd(transCol), 0, rnd(transCol), rnd(height));
        }

        float yStart = Math.round(view.yMin());
        float yEnd = Math.round(view.yMax());
        for (float row = yStart; row <= yEnd; row++) {
            float transRow = transformY(row);
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
        Rectangle center = determineView();
        if (view == null) {
            view = Rectangle.fromCenter(center.x(), center.y(), center.width(), center.height());
        } else {
            view.push(new Vector2(center.x(), center.y()).minus(new Vector2(view.x(), view.y())).times(.2f));
            view.move();
        }
        float widthRatio = (center.width() + 10) / view.width();
        float heightRation = (center.height() + 10) / view.height();
        float ratio = widthRatio < heightRation ? heightRation : widthRatio;
        view.scale(ratio / 5f + 4 / 5f);
    }

    private void drawDoor(Graphics2D g, Door door) {
        Rectangle rec1 = rectsByRoom.get(door.getInRoomName());
        Rectangle rec2 = rectsByRoom.get(door.getOutRoomName());
        if (rec1 != null && rec2 != null)
        g.drawLine(rnd(transformX(rec1.x()))
                , rnd(transformY(rec1.y()))
                , rnd(transformX(rec2.x()))
                , rnd(transformY(rec2.y())));
    }

}