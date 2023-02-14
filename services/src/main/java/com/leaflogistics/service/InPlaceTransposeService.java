package com.leaflogistics.service;

import static com.leaflogistics.service.serialization.SerializeUtils.serialize;
import static com.leaflogistics.service.serialization.SerializeUtils.deserialize;

import com.leaflogistics.service.serialization.MatrixAttrs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;


/**
 * transpose matrix without creating auxillary array
 */
@Primary
@Service
public class InPlaceTransposeService implements ITransposeService {
    private static final Logger log = LoggerFactory.getLogger(InPlaceTransposeService.class);


    @Override
    public String transpose(String matrixStr) {
        MatrixAttrs attrs = deserialize(matrixStr);
        int rows = attrs.getRows();
        int cols = attrs.getCols();
        int[] arry = attrs.getMatrix();

        // if we are dealing with a column vector, transpose is a no-op
        if (attrs.isColumnVector()) {
            log.info("transposing column vector with row count: {} and column count: {}", rows, cols);
            return serialize(arry);
        }

        log.info("transposing matrix with row count: {} and column count: {}", rows, cols);

        int size = rows * cols - 1;  // skip the last element since it will never move
        int accessedNdxBitmask = 1 | (1 << size);

        int ii = 1;  // skip the first element since it will never move
        while (ii < size) {
            int nextUnaccessedNdx = ii;
            int curElement = arry[ii];

            do {
                int next = (ii * rows) % size;
                int temp = arry[next];
                arry[next] = curElement;
                curElement = temp;


                accessedNdxBitmask |= (1 << ii);  // record this index in the bitmask
                ii = next;
            } while (ii != nextUnaccessedNdx);

            // start from the beginning of the array and determine the first unaccessed array element
            for (ii = 1; ii < size && ((accessedNdxBitmask & (1 << ii)) != 0); ii++) {
                ;
            }
        }

        return serialize(arry, cols, rows);
    }

}
