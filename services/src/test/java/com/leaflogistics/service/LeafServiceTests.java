package com.leaflogistics.service;

import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes= InPlaceTransposeService.class)
class LeafServiceTests {
    private static final Logger log = LoggerFactory.getLogger(LeafServiceTests.class);


    @Autowired
    private ITransposeService svc;


    @Test
    public void testTransposeColumnVector() {
        // 1
        // 2
        // 3
        // 4
        // 5
        // 6
        String json = "[1, 2, 3, 4, 5, 6]";
        log.info("input json: {}", json);

        String resp = this.svc.transpose(json);
        log.info("resp json: {} \n", resp);

        assertEquals("[1,2,3,4,5,6]", deleteWhitespace(resp));
    }

    @Test
    public void testTransposeRowVector() {
        // 1 2 3 4 5 6
        String json = "[[1, 2, 3, 4, 5, 6]]";
        log.info("input json: {}", json);

        String resp = this.svc.transpose(json);
        log.info("resp json: {} \n", resp);

        // 1
        // 2
        // 3
        // 4
        // 5
        // 6
        assertEquals("[[1],[2],[3],[4],[5],[6]]", deleteWhitespace(resp));
    }

    @Test
    public void testTransposeSquareMatrix() {
        // 1 2 3
        // 4 5 6
        // 7 8 9
        String json = "[[1, 2, 3], [4, 5, 6], [7, 8, 9]]";
        log.info("input json: {}", json);

        String resp = this.svc.transpose(json);
        log.info("resp json: {} \n", resp);

        // 1 4 7
        // 2 5 8
        // 3 6 9
        assertEquals("[[1,4,7],[2,5,8],[3,6,9]]", deleteWhitespace(resp));
    }

	@Test
	public void testTransposeRectangularMatrix() {
        // 1 2 3
        // 4 5 6
        String json = "[[1, 2, 3], [4, 5, 6]]";
        log.info("input json: {}", json);

        String resp = this.svc.transpose(json);
        log.info("resp json: {} \n", resp);

        // 1 4
        // 2 5
        // 3 6
        assertEquals("[[1,4],[2,5],[3,6]]", deleteWhitespace(resp));
	}

    @Test
    public void testLargeIntegers() {
        // 1398473 -283823729 39837
        // 4827373 87274737 -8372745
        String json = "[[1398473, -283823729, 39837], [4827373, 87274737, -8372745]]";
        log.info("input json: {}", json);

        String resp = this.svc.transpose(json);
        log.info("resp json: {} \n", resp);

        // 1398473      4827373
        // -283823729  87274737
        // 39837       -8372745
        assertEquals("[[1398473,4827373],[-283823729,87274737],[39837,-8372745]]", deleteWhitespace(resp));
    }

    @Test
    public void testSingleElement() {
        String json = "[[1]]";
        log.info("input json: {}", json);

        String resp = this.svc.transpose(json);
        log.info("resp json: {} \n", resp);

        assertEquals("[[1]]", deleteWhitespace(resp));
    }

}
