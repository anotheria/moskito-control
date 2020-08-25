package org.moskito.control.data.processors;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ProcessorsTest {

    private static HashMap<String, String> data = new HashMap<>();

    @BeforeClass
    public static void init() {
        data.put("pE", "");
        data.put("p-1", "-1");
        data.put("p-2", "-2");
        data.put("p-3", "-3");
        data.put("p0", "0");
        data.put("p1", "1");
        data.put("p2", "2");
        data.put("p3", "3");
        data.put("p4", "4");
        data.put("p5", "5");
        data.put("pt1", "nonParseble");
    }

    @Test
    public void SumTest() {
        Map<String, String> res;

        DataProcessor pr = new SumProcessor();
        res = pr.process(data);
        assertNotNull(res);
        assertEquals(res.size(), 0);

        pr = new SumProcessor();
        pr.configure("test", "pE, pt1, noneExist");
        assertEquals(pr.process(data).size(), 0);

        pr = new SumProcessor();
        pr.configure("test", "p0");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(0d).toString());

        pr = new SumProcessor();
        pr.configure("test", "pE, p1, noneExist");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(1d).toString());

        pr = new SumProcessor();
        pr.configure("test", "p-3, p2, p1");
        assertEquals(pr.process(data).get("test"), Double.valueOf((-3d + 2 + 1)).toString());
        assertEquals(pr.process(data).get("test"), Double.valueOf((-3d + 2 + 1)).toString());

        pr = new SumProcessor();
        pr.configure("test", "p1, p2, p-3");
        assertEquals(pr.process(data).get("test"), Double.valueOf((1d + 2 + -3)).toString());

        pr = new SumProcessor();
        pr.configure("test", "pE, p1, p-2, p5, noneExist");
        assertEquals(pr.process(data).get("test"), Double.valueOf(1d - 2 + 5).toString());
    }

    @Test
    public void SubTest() {
        Map<String, String> res;

        DataProcessor pr = new SubProcessor();
        res = pr.process(data);
        assertNotNull(res);
        assertEquals(res.size(), 0);

        pr = new SubProcessor();
        pr.configure("test", "pE, pt1, noneExist");
        assertEquals(pr.process(data).size(), 0);

        pr = new SubProcessor();
        pr.configure("test", "p0");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(0d).toString());

        pr = new SubProcessor();
        pr.configure("test", "pE, p1, noneExist");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(1d).toString());

        pr = new SubProcessor();
        pr.configure("test", "p-3, p2, p1");
        assertEquals(pr.process(data).get("test"), Double.valueOf((-3d - 2 - 1)).toString());
        assertEquals(pr.process(data).get("test"), Double.valueOf((-3d - 2 - 1)).toString());

        pr = new SubProcessor();
        pr.configure("test", "p2, p1, p-3");
        assertEquals(pr.process(data).get("test"), Double.valueOf((2d - 1 - -3)).toString());

        pr = new SubProcessor();
        pr.configure("test", "pE, p5, p-2, p1, noneExist");
        assertEquals(pr.process(data).get("test"), Double.valueOf(5d - -2 - 1).toString());
    }


    @Test
    public void AvgTest() {
        Map<String, String> res;

        DataProcessor pr = new AvgProcessor();
        res = pr.process(data);
        assertNotNull(res);
        assertEquals(res.size(), 0);

        pr = new AvgProcessor();
        pr.configure("test", "pE, pt1, noneExist");
        assertEquals(pr.process(data).size(), 0);

        pr = new AvgProcessor();
        pr.configure("test", "p0");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(0d).toString());

        pr = new AvgProcessor();
        pr.configure("test", "pE, p1, noneExist");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(1d).toString());

        pr = new AvgProcessor();
        pr.configure("test", "p-3, p2, p1");
        assertEquals(pr.process(data).get("test"), Double.valueOf((-3d + 2 + 1) / 3).toString());
        assertEquals(pr.process(data).get("test"), Double.valueOf((-3d + 2 + 1) / 3).toString());

        pr = new AvgProcessor();
        pr.configure("test", "pE, p1, p-2, p5, noneExist");
        assertEquals(pr.process(data).get("test"), Double.valueOf((1d - 2 + 5) / 3).toString());

        pr = new AvgProcessor();
        pr.configure("test", "pE, p0, p1, p-2, p5, noneExist");
        assertEquals(pr.process(data).get("test"), Double.valueOf((0 + 1d - 2 + 5) / 4).toString());
    }

    @Test
    public void MulTest() {
        Map<String, String> res;

        DataProcessor pr = new MulProcessor();
        res = pr.process(data);
        assertNotNull(res);
        assertEquals(res.size(), 0);

        pr = new MulProcessor();
        pr.configure("test", "pE, pt1, noneExist");
        assertEquals(pr.process(data).size(), 0);

        pr = new MulProcessor();
        pr.configure("test", "p0");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(0d).toString());

        pr = new MulProcessor();
        pr.configure("test", "pE, p1, noneExist");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(1d).toString());

        pr = new MulProcessor();
        pr.configure("test", "p-3, p2, p1");
        assertEquals(pr.process(data).get("test"), Double.valueOf((-3d * 2 * 1)).toString());
        assertEquals(pr.process(data).get("test"), Double.valueOf((-3d * 2 * 1)).toString());

        pr = new MulProcessor();
        pr.configure("test", "p1, p2, p-3");
        assertEquals(pr.process(data).get("test"), Double.valueOf((1d * 2 * -3)).toString());

        pr.configure("test", "pE, p0, p1, p-2, p5, noneExist");
        assertEquals(pr.process(data).get("test"), Double.valueOf(0 * 1d * -2 * 5).toString());
    }

    @Test
    public void DivTest() {
        Map<String, String> res;

        DataProcessor pr = new DivProcessor();
        res = pr.process(data);
        assertNotNull(res);
        assertEquals(res.size(), 0);

        try {
            pr = new DivProcessor();
            pr.configure("test", "p0");
            fail("Expected IllegalArgumentException");
        } catch (Exception e) {/*ignore*/}

        try {
            pr = new DivProcessor();
            pr.configure("test", "p0,p1,p3");
            fail("Expected IllegalArgumentException");
        } catch (Exception e) {/*ignore*/}

        pr = new DivProcessor();
        pr.configure("test", "pE, pt1");
        assertEquals(pr.process(data).size(), 0);

        pr = new DivProcessor();
        pr.configure("test", "pE, noneExist");
        assertEquals(pr.process(data).size(), 0);

        pr = new DivProcessor();
        pr.configure("test", "p2, p1");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(2d / 1).toString());
        assertEquals(pr.process(data).get("test"), Double.valueOf(2d / 1).toString());

        pr = new DivProcessor();
        pr.configure("test", "p1, p2");
        assertEquals(pr.process(data).get("test"), Double.valueOf(1d / 2).toString());
    }

    @Test
    public void MinTest() {
        Map<String, String> res;

        DataProcessor pr = new MinProcessor();
        res = pr.process(data);
        assertNotNull(res);
        assertEquals(res.size(), 0);

        pr = new MinProcessor();
        pr.configure("test", "pE, pt1, noneExist");
        assertEquals(pr.process(data).size(), 0);

        pr = new MinProcessor();
        pr.configure("test", "p0");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(0d).toString());

        pr = new MinProcessor();
        pr.configure("test", "pE, p1, noneExist");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(1d).toString());

        pr = new MinProcessor();
        pr.configure("test", "p-3, p2, p1");
        assertEquals(pr.process(data).get("test"), Double.valueOf(-3d).toString());
        assertEquals(pr.process(data).get("test"), Double.valueOf(-3d).toString());

        pr = new MinProcessor();
        pr.configure("test", "p1, p-1, p0");
        assertEquals(pr.process(data).get("test"), Double.valueOf(-1d).toString());

        pr = new MinProcessor();
        pr.configure("test", "p2, p1");
        assertEquals(pr.process(data).get("test"), Double.valueOf(1d).toString());

        pr = new MinProcessor();
        pr.configure("test", "pE, p1, p2, p5, noneExist");
        assertEquals(pr.process(data).get("test"), Double.valueOf(1d).toString());
    }

    @Test
    public void MaxTest() {
        Map<String, String> res;

        DataProcessor pr = new MaxProcessor();
        res = pr.process(data);
        assertNotNull(res);
        assertEquals(res.size(), 0);

        pr = new MaxProcessor();
        pr.configure("test", "pE, pt1, noneExist");
        assertEquals(pr.process(data).size(), 0);

        pr = new MaxProcessor();
        pr.configure("test", "p0");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(0d).toString());

        pr = new MaxProcessor();
        pr.configure("test", "pE, p1, noneExist");
        res = pr.process(data);
        assertEquals(res.size(), 1);
        assertNotNull(res.get("test"));
        assertEquals(res.get("test"), Double.valueOf(1d).toString());

        pr = new MaxProcessor();
        pr.configure("test", "p-3, p2, p1");
        assertEquals(pr.process(data).get("test"), Double.valueOf(2d).toString());
        assertEquals(pr.process(data).get("test"), Double.valueOf(2d).toString());

        pr = new MaxProcessor();
        pr.configure("test", "p1, p-1, p0");
        assertEquals(pr.process(data).get("test"), Double.valueOf(1d).toString());

        pr = new MaxProcessor();
        pr.configure("test", "p2, p1");
        assertEquals(pr.process(data).get("test"), Double.valueOf(2d).toString());

        pr = new MaxProcessor();
        pr.configure("test", "pE, p1, p-2, p5, noneExist");
        assertEquals(pr.process(data).get("test"), Double.valueOf(5).toString());
    }
}