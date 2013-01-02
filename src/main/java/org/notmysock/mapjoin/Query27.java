package org.notmysock.mapjoin;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.mapreduce.lib.reduce.*;


import java.io.*;
import java.nio.*;
import java.util.*;

import org.notmysock.mapjoin.Types.*;
import org.notmysock.mapjoin.Tables.*;


public class Query27 extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.setBoolean("mapreduce.job.committer.task.cleanup.needed", false);
        int res = ToolRunner.run(conf, new Query27(), args);
        System.exit(res);
    }

    private Path in;
    private Path out;

    @Override
    public int run(String[] args) throws Exception {
        String[] remainingArgs = new GenericOptionsParser(getConf(), args).getRemainingArgs();

        if (remainingArgs.length < 2) {
            System.err.println("Usage: Query27 <in> <out>");
            ToolRunner.printGenericCommandUsage(System.err);
            return 1;
        }

        Configuration conf = getConf();

        Job job = new Job(conf, "Query27");
        job.setJarByClass(getClass());

        job.setMapperClass(Mapjoin.class);
        job.setReducerClass(ReduceAverager.class);
        job.setNumReduceTasks(1);
        job.setMapOutputKeyClass(Stage_1_k.class);
        job.setMapOutputValueClass(Stage_1_v.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        in = new Path(remainingArgs[0]);
        out = new Path(remainingArgs[1]);

        job.setInputFormatClass(store_sales.InputFormat.class);

        FileInputFormat.addInputPath(job, new Path(in,"store_sales"));
        FileOutputFormat.setOutputPath(job, out);

        runLocal();

        long t1 = System.currentTimeMillis();
        boolean success = job.waitForCompletion(true);
        long t2 = System.currentTimeMillis();
        System.out.println("Run took " + (t2 - t1) + " milliseconds");
        //System.out.println("We did "+hashes+" getHashCodes over-all");        
        return success ? 0 : 1;
    }

    static final class Mapjoin extends Mapper<LongWritable,store_sales, Stage_1_k, Stage_1_v> {
      Stage_1_k k = new Stage_1_k();
      Stage_1_v v = new Stage_1_v();
      protected void setup(Context context) throws IOException {
      }
      protected void map(LongWritable offset, store_sales value, Mapper.Context context) 
        throws IOException, InterruptedException {
      }
    }

    static final class ReduceAverager extends Reducer<Stage_1_k, Stage_1_v, Text, Text> {
      protected void reduce(Stage_1_k key, Iterator<Stage_1_v> values, Reducer.Context context) 
        throws IOException, InterruptedException {
      }
    }
}
