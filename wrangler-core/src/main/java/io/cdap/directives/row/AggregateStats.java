package io.cdap.directives.row;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.Bytesize;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.Executor;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AggregateStats is a custom executor that calculates the total byte size and time duration
 * from rows and outputs a single row with aggregated values.
 */
public class AggregateStats implements Executor<List<Row>, List<Row>> {

    private String sizeCol;
    private String timeCol;
    private String outputSizeCol;
    private String outputTimeCol;

    public void initialize(Arguments arguments) {
        sizeCol = arguments.value("sizeCol");
        timeCol = arguments.value("timeCol");
        outputSizeCol = arguments.value("outputSizeCol");
        outputTimeCol = arguments.value("outputTimeCol");
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) {
        long totalSizeBytes = 0;
        long totalTimeMs = 0;

        for (Row row : rows) {
            Object sizeObj = row.getValue(sizeCol);
            Object timeObj = row.getValue(timeCol);

            if (sizeObj != null) {
                try {
                    Bytesize bs = new Bytesize(sizeObj.toString());
                    totalSizeBytes += bs.getBytes();
                } catch (Exception e) {
                    // Handle parse error or ignore
                }
            }

            if (timeObj != null) {
                try {
                    TimeDuration td = new TimeDuration(timeObj.toString());
                    totalTimeMs += td.getMilliseconds();
                } catch (Exception e) {
                    // Handle parse error or ignore
                }
            }
        }

        Row outputRow = new Row();
        outputRow.add(outputSizeCol, totalSizeBytes);    // or convert to MB if needed
        outputRow.add(outputTimeCol, totalTimeMs);       // or convert to seconds if needed

        return Collections.singletonList(outputRow);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'destroy'");
    }
}
