package com.leaflogistics.service;

import static com.leaflogistics.service.serialization.SerializeUtils.deserialize;
import static com.leaflogistics.service.serialization.SerializeUtils.serialize;
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

import com.leaflogistics.service.serialization.MatrixAttrs;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = InPlaceTransposeService.class)
class SerializationTests {
    private static final Logger log = LoggerFactory.getLogger(SerializationTests.class);


    @Test
    public void testVectorDeserialization() {
        String jsonMatrix = "[1,2,4,5,7,8]";

        MatrixAttrs attrs = deserialize(jsonMatrix);
        assertEquals(1, attrs.getRows());
        assertEquals(6, attrs.getCols());

        int[] arry = attrs.getMatrix();
        assertEquals(0, Arrays.compare(new int[] {1,2,4,5,7,8}, arry));
    }

    @Test
    public void testDeserialization() {
        String jsonMatrix = "[[1,2,3],[4,5,6],[7,8,9]]";

        MatrixAttrs attrs = deserialize(jsonMatrix);
        assertEquals(3, attrs.getRows());
        assertEquals(3, attrs.getCols());

        int[] arry = attrs.getMatrix();
        assertEquals(0, Arrays.compare(new int[] {1,2,3,4,5,6,7,8,9}, arry));
    }

    @Test
    public void testDeserializationMalformed() {
        this.assertBadInput("[[1,2,3],[4,5,6],[7,8,9]");  // missing trailing bracket
        this.assertBadInput("[[1,2,3], [4,5]]");          // column counts differ
        this.assertBadInput("[[1,2,3], [4,5,]]");         // column counts differ
        this.assertBadInput("[[1,2 3], [4,5,6]]");        // column counts differ
    }

    @Test
    public void testIntegerSizeFails() {
        this.assertBadInput("[ -1000000001 ]");
        this.assertBadInput("[ 1000000001 ]");
    }

    private void assertBadInput(String badInput) {
        try {
            deserialize(badInput);
            fail("malformed json string should have raised exception: " + badInput);
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testColumnVectorSerialization() {
        int[] arry = {1, 2, 3, 4, 5};

        String json = serialize(arry);
        log.info("result: {}", json);

        assertEquals("[1,2,3,4,5]", deleteWhitespace(json));
    }

    @Test
    public void testMatrixSerialization() {
        int[] arry = {1, 2, 3, 4, 5, 6};

        String json = serialize(arry, 2, 3);
        log.info("result: {}", json);

        assertEquals("[[1,2,3],[4,5,6]]", deleteWhitespace(json));
    }


}
