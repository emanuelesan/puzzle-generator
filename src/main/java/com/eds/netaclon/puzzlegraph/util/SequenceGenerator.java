package com.eds.netaclon.puzzlegraph.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by emanuelesan on 31/03/16.
 */
public class SequenceGenerator {
    private static Map<String,AtomicInteger> map = new HashMap<>();

    public static synchronized int incrementAndGet(String s)
    {
        return map.computeIfAbsent(s,(s1)->new AtomicInteger(0)).getAndIncrement();
    }

}
