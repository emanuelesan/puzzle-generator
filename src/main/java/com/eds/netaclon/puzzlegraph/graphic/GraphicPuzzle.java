package com.eds.netaclon.puzzlegraph.graphic;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by emanuelesan on 10/04/16.
 */
public class GraphicPuzzle {
    @JsonProperty
    private Puzzle puzzle;
    @JsonProperty
    private Map<String, Rectangle> rectsByRoom;

    public GraphicPuzzle()
    {}

    public GraphicPuzzle(Puzzle puz) {
      this();
        this.puzzle=puz;
        rectsByRoom = new HashMap<>();
    }

    public Map<String, Rectangle> getRectsByRoom() {
        return rectsByRoom;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }
}
