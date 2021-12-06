package Problem3;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.TypeDescriptors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;


public class MovieTagCount {



    private static final String CSV_HEADER = "tag,userId,movieId" +
            "Account_Created,Last_Login,Latitude,Longitude,US Zip";

    public static void main(String[] args) {

        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline pipeline = Pipeline.create(options);

        pipeline.apply("Read-Lines", TextIO.read().from("src/main/resources/movie_tags - movie_tags.csv"))
                .apply("Filter-Header", ParDo.of(new FilterHeaderFn(CSV_HEADER)))
                .apply("payment-extractor",MapElements
                        .into(TypeDescriptors.kvs(TypeDescriptors.strings(), TypeDescriptors.strings()))
                        .via((String line) -> {
                            String[] tokens = line.split(",");
                            return KV.of(tokens[0],  (tokens[2]));
                        }))
                .apply("count-aggregation", Count.perElement())
                .apply("Format-result", MapElements
                        .into(TypeDescriptors.strings())
                        .via(typeCount -> typeCount.getKey() + "," + typeCount.getValue()))
                .apply("WriteResult", TextIO.write()
                        .to("src/main/resources/Result/MovieCount")
                        .withoutSharding()
                        .withSuffix(".csv")
                        .withHeader("userId,movie_tag,tag_count"));


        pipeline.run();
    }

    //TODO: keep it in another class not as inner class.
    private static class FilterHeaderFn extends DoFn<String, String> {
        private static final Logger LOGGER = LoggerFactory.getLogger(FilterHeaderFn.class);

        private final String header;

        FilterHeaderFn(String header) {
            this.header = header;
        }

        @ProcessElement
        public void processElement(ProcessContext processContext) {
            String row = processContext.element();
            if (!row.isEmpty() && !row.contains(header))
                processContext.output(row);
            else
                LOGGER.info("Filtered out the header of the csv file: [{}]", row);

        }
    }
}
