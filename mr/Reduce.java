package mr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;



public  class Reduce extends MapReduceBase
implements Reducer<Text, IntWritable, Text, IntWritable> {

//takes in as input key,[values] from the output collector
public void reduce(Text key, Iterator<IntWritable> values,
                   OutputCollector<Text, IntWritable> output,
                   Reporter reporter) throws IOException {
	
	
    int sum = 0;
    String line = key.toString();
    StringTokenizer itr = new StringTokenizer(line);
    while (values.hasNext()) {
       sum += values.next().get(); //computes the sum (frequency) for every unique key and value pair
    }
    //System.out.println(" IN REDUCE CLASS ... before 'output' ");
    output.collect(key, new IntWritable(sum)); //passes this computed sum as output to the output collector
    //System.out.println(" IN REDUCE CLASS ... after 'output' .. key " + line);
    
    int index=Integer.parseInt(itr.nextToken());
    String value=itr.nextToken();
    String classLabel=itr.nextToken();
    int count=sum;

}

}
