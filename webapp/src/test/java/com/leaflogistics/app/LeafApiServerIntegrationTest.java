package com.leaflogistics.app;

import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.leaflogistics.LeafApiServerApplication;
import com.leaflogistics.service.ITransposeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * end to end integration test.  this test will spin up the application server, generate a JSON input array
 * and send that payload over the wire via HTTP.  by default, this test is disabled, since it takes quite a bit
 * of time (relatively speaking) to execute.  for this test to run, you can set the environment variable
 * LEAF_INTEGRATION to any non-empty value in your IDE, or execute the following commands from the terminal:
 *
 * ./gradlew clean
 * LEAF_INTEGRATION=true ./gradlew :webapp:test --tests "com.leaflogistics.app.LeafApiServerIntegrationTest.testMaxInput"
 *
 * there is also a convenience script which will do this for you:  PROJECT_ROOT/scripts/run-integration.sh
 */
@EnabledIfEnvironmentVariable(named = "LEAF_INTEGRATION", matches = ".+")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LeafApiServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LeafApiServerIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(LeafApiServerIntegrationTest.class);

    @Autowired
    private ITransposeService service;


	@Test
	public void testMaxInput() {
        String maxInput = this.generateHugeArray(250, 400);  // 250 * 400 == 100,000  (1 <= m * n <= 10^5)
        log.info("first 1000 chars: {}", maxInput.substring(0, 1000));

        String respJson = post(maxInput);
        this.writeToFile(respJson);  // just in case we want to see/investigate the full response

        // if we transpose the result, it should equal the original input
        String checkJson = service.transpose(respJson);
        assertEquals(deleteWhitespace(maxInput), deleteWhitespace(checkJson));

        String inputTooLarge = this.generateHugeArray(400, 251);  // matrix size 100400 should raise exception
        try {
            post(inputTooLarge);
            fail("matrix size > 100000 should have raised exception");
        }
        catch (Exception e) {
            // no op
        }
    }

    private String post(String jsonInput) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonInput, headers);

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/api/transpose",
                HttpMethod.POST, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("api call failed: " + response.getBody());
        }

        return response.getBody();
    }


    private String generateHugeArray(int rows, int cols) {
        // -10^9 <= matrix[i][j] <= 10^9
        int negBillion = -1000000000;  // use the max integer size for the matrix constraint

        StringBuilder sb = new StringBuilder("[ ");

        for (int ii=0; ii < rows; ii++) {
            sb.append("[");
            for (int jj=0; jj < cols; jj++){
                int val = negBillion + (ii + jj);
                sb.append(val);
                if (jj < cols - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            if (ii < rows - 1) {
                sb.append(",");
            }
        }
        sb.append(" ]");

        return sb.toString();
    }

    private void writeToFile(String data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/tmp/output.txt"));
            writer.write(data);

            writer.close();
        } catch (Exception e) {
            log.error("error", e);
        }
    }
}
