package com.eds.netaclon.puzzlegraph.renderer.flockingroom.ticking;

import com.eds.netaclon.puzzlegraph.Puzzle;
import com.eds.netaclon.puzzlegraph.renderer.flockingroom.Rectangle;
import org.codehaus.jackson.annotate.JacksonAnnotation;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by emanuelesan on 06/04/16.
 */
public class CorridorConnectorTest {

    @org.junit.Test
    public void testTickVertical() throws Exception {
        String mapString = "{\"Start\":{\"x\":1.0,\"y\":64.0,\"xMin\":-3.0,\"yMin\":62.0,\"xMax\":5.0,\"yMax\":65.0},\"End\":{\"x\":1.0,\"y\":68.0,\"xMin\":-2.0,\"yMin\":66.0,\"xMax\":4.0,\"yMax\":70.0}}";
        String puzzleString = "{\"roomMap\":{\"Start\":{\"pathToEndName\":\"D0\",\"doorNames\":[\"D0\"],\"itemNames\":[],\"depth\":0,\"name\":\"Start\"},\"End\":{\"pathToEndName\":null,\"doorNames\":[\"D0\"],\"itemNames\":[],\"depth\":1,\"name\":\"End\"}},\"keyMap\":{},\"doorMap\":{\"D0\":{\"keyNames\":[],\"outRoomName\":\"Start\",\"inRoomName\":\"End\",\"name\":\"D0\"}},\"containerMap\":{},\"start\":{\"pathToEndName\":\"D0\",\"doorNames\":[\"D0\"],\"itemNames\":[],\"depth\":0,\"name\":\"Start\"},\"end\":{\"pathToEndName\":null,\"doorNames\":[\"D0\"],\"itemNames\":[],\"depth\":1,\"name\":\"End\"}}";

        ObjectMapper objectMapper = new ObjectMapper();
        Puzzle puzzle = objectMapper.readValue(puzzleString, Puzzle.class);
        TypeReference<Map<String,Rectangle>> typeRef
                = new TypeReference<Map<String,Rectangle>>() {};
        Map<String,Rectangle> map = objectMapper.readValue(mapString, typeRef);


        CorridorConnector corridorConnector = new CorridorConnector(puzzle, map);

        corridorConnector.tick();
    }


    @org.junit.Test
    public void testTickHori() throws Exception {
        String mapString = "{\"Start\":{\"x\":1.0,\"y\":64.0,\"xMin\":-3.0,\"yMin\":62.0,\"xMax\":5.0,\"yMax\":66.0}," +
                "\"End\":{\"x\":8.0,\"y\":64.0,\"xMin\":6.0,\"yMin\":62.0,\"xMax\":10.0,\"yMax\":66.0}}";
        String puzzleString = "{\"roomMap\":{\"Start\":{\"pathToEndName\":\"D0\",\"doorNames\":[\"D0\"],\"itemNames\":[],\"depth\":0,\"name\":\"Start\"},\"End\":{\"pathToEndName\":null,\"doorNames\":[\"D0\"],\"itemNames\":[],\"depth\":1,\"name\":\"End\"}},\"keyMap\":{},\"doorMap\":{\"D0\":{\"keyNames\":[],\"outRoomName\":\"Start\",\"inRoomName\":\"End\",\"name\":\"D0\"}},\"containerMap\":{},\"start\":{\"pathToEndName\":\"D0\",\"doorNames\":[\"D0\"],\"itemNames\":[],\"depth\":0,\"name\":\"Start\"},\"end\":{\"pathToEndName\":null,\"doorNames\":[\"D0\"],\"itemNames\":[],\"depth\":1,\"name\":\"End\"}}";

        ObjectMapper objectMapper = new ObjectMapper();
        Puzzle puzzle = objectMapper.readValue(puzzleString, Puzzle.class);
        TypeReference<Map<String,Rectangle>> typeRef
                = new TypeReference<Map<String,Rectangle>>() {};
        Map<String,Rectangle> map = objectMapper.readValue(mapString, typeRef);


        CorridorConnector corridorConnector = new CorridorConnector(puzzle, map);

        corridorConnector.tick();
    }

}