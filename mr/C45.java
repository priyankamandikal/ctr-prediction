package mr;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



public class C45 extends Configured implements Tool {


    public static Split currentsplit=new Split(); //create object of class Split
    public static List <Split> splitted=new ArrayList<Split>();
    public static int current_index=0;

	public static void main(String[] args) throws Exception {	
	 	MapClass mp = new MapClass();
	 	splitted.add(currentsplit); //adding one element to splitted => size(splitted) = 1 initially
	  	
	  	int res=0;
	  	double bestGain=0;
		boolean stop=true;
		boolean outerStop=true;
		int split_index=0;
		double gainratio=0;
		double best_gainratio=0;
		double entropy=0;
		String classLabel=null;
		int total_attributes=mp.no_Attr;
		total_attributes=18;				//EDIT THIS????
		int split_size=splitted.size();
		GainRatio gainObj;
		Split newnode;

	  	while(split_size > current_index) //1>0 initially, so enters loop
      	{
	  		System.out.println(" IN WHILE IN MAIN CLASS ");
	  		
   			currentsplit=(Split) splitted.get(current_index); 
    		gainObj=new GainRatio();
		    res = ToolRunner.run(new Configuration(), new C45(), args); //calls the run class at the end of this class
		    //after map and reduce finish, control returns here
		    System.out.println("\nCurrent  NODE INDEX . ::" + current_index);
	    	
		    int j=0;
		    int temp_size;
		    gainObj.getcount(); //getcount() method of GainRatio is called
		    entropy = gainObj.currNodeEntophy(); //currNodeEntropy() method of GainRatio is called
		    classLabel=gainObj.majorityLabel(); //returns majority class label 
		    currentsplit.classLabel=classLabel;

    		if(entropy!=0.0 && currentsplit.attr_index.size()!=total_attributes)
	    	{
	    		System.out.println("");
	    		//System.out.println("Entropy  NOTT zero   SPLIT INDEX::    " + entropy);
	    	
	    		best_gainratio=0;
	 
				for(j=0;j<total_attributes;j++)		//Finding the gain of each attribute
				{    	
					if(currentsplit.attr_index.contains(j))  // Splitting all ready done with this attribute, so do nothing
					{
						System.out.println("Splitting all ready done with  index  "+j);
					}	
					else //splitting not done on this attr, so enter
					{
						gainratio=gainObj.gainratio(j,entropy);
						if(gainratio >= best_gainratio)
						{
							split_index=j;
							best_gainratio=gainratio; //setting new best gainratio
						}
					}
				}
				System.out.println(" Split index = " + split_index);
			    String attr_values_split = gainObj.getvalues(split_index);
			    StringTokenizer attrs = new StringTokenizer(attr_values_split);
			    int number_splits = attrs.countTokens(); //number of splits possible with  attribute selected
			    String red="";
			    int tred=-1;
			    
			    System.out.println(" INDEX ::  " + split_index);
			    System.out.println(" SPLITTING VALUES  " + attr_values_split);
	    
			    for(int splitnumber=1; splitnumber <=number_splits ; splitnumber++)
			    {
			    	
			    	temp_size = currentsplit.attr_index.size();
			    	System.out.println("--- > slpitnumber = " + splitnumber + "  tempsize =" + temp_size);
			    	newnode = new Split(); 
			    	for(int y=0;y<temp_size;y++)   // CLONING OBJECT CURRENT NODE
			    	{
			    		System.out.println("for y = " + y + " currentsplit.attr_index.get" + y + " = " + currentsplit.attr_index.get(y));
			    		System.out.println("for y = " + y + " currentsplit.attr_value.get" + y + " = " + currentsplit.attr_value.get(y));
			    		newnode.attr_index.add(currentsplit.attr_index.get(y));
			    		newnode.attr_value.add(currentsplit.attr_value.get(y));
			    	}
			    	red=attrs.nextToken();
			    	System.out.println("red ---> " + red);
			    	newnode.attr_index.add(split_index);
			    	newnode.attr_value.add(red);
			    	splitted.add(newnode);
			    }
	    	}
		    else
		    {
		    	System.out.println("");
		    	String rule="";
		    	temp_size=currentsplit.attr_index.size();
		    	for(int val=0;val<temp_size;val++)  
		    	{
		    		rule=rule+" "+currentsplit.attr_index.get(val)+" "+currentsplit.attr_value.get(val);
		    	}
		    	rule=rule+" "+currentsplit.classLabel;
		    	writeRuleToFile(rule);
		    	if(entropy!=0.0)
		    		System.out.println("Enter rule in file:: "+rule);
		    	else
		    		System.out.println("Enter rule in file Entropy zero ::   "+rule);
		    }
	    
		    split_size=splitted.size();
		    System.out.println("TOTAL NODES::    "+split_size); 
		    current_index++; //increment current index
        }
	  
	  	System.out.println("COMPLEEEEEEEETEEEEEEEEEE");
	    System.exit(res);
  }

	public static void writeRuleToFile(String text) {
		try {
			System.out.println("WRITING THIS RULE TO FULE --- > " + text);
			
			

			Configuration conf = new Configuration();
	    	FileSystem fs = FileSystem.get( conf ); 
			//Path path = new Path( "/home/anurag/eclipse_mars_workspace/mapreduce/rule.txt" );
			Path path = new Path( "hdfs://lazy:54310/output_dm/rule.txt" );
			
			
	    	if( fs.exists( path ) ) 
	    	{
	    	    //Path tmpPath = new Path( "/home/anurag/eclipse_mars_workspace/mapreduce/output/temp_rule.txt" );
	    	    Path tmpPath = new Path( "hdfs://lazy:54310/output_dm/temp_rule.txt" );
	    	        	    
	    	    BufferedReader br = new BufferedReader( new InputStreamReader( fs.open( path ) ) );  // Open old file for reading
	    	    BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( fs.create( tmpPath , true ) ) ); 

	    	    String line = br.readLine(); // Read first line of file
	    	    while ( line != null )
	    	    {
	    	        bw.write( line ); // Write line from old file to new file
	    	        bw.newLine();  // Writes a line separator.
	    	        line = br.readLine(); // Read next line of the file
	    	     }
	    	    //bw.write( "\n" );  // Append new line into new file
	    	    //bw.newLine(); // Writes a line separator
	    	    bw.write( text );  // Append new line into new file

	    	    br.close(); // Closes the stream and releases any system resources associated with it.
	    	    bw.close(); // Closes the stream and releases any system resources associated with it.
	    	    fs.delete( path, true ); // Delete old file
	    	    fs.rename( tmpPath , path );  // Rename new file to old file name
	    	}
	    	else {
	            FSDataOutputStream os = fs.create(new Path("hdfs://lazy:54310/output_dm/rule.txt"));
		    	//FSDataOutputStream os = fs.create(new Path("/home/anurag/eclipse_mars_workspace/mapreduce/rule.txt"));
		    	
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));    
				bw.write(text);
				bw.newLine();
				bw.close();
	    	}
			//fs.close();
		} 
		catch (Exception e) {
		}
	}
  
  
  
	public int run(String[] args) throws Exception {
		
		System.out.println("IN RUN");
		
		JobConf conf = new JobConf(getConf(),C45.class);
		conf.setJobName("Id3");

		// the keys are words (strings)
		conf.setOutputKeyClass(Text.class);
		// the values are counts (ints)
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(MapClass.class);
		conf.setReducerClass(Reduce.class);

		//set your input file path below
		
		//FileInputFormat.setInputPaths(conf, "/home/anurag/eclipse_mars_workspace/mapreduce/test_final.txt");
		//FileOutputFormat.setOutputPath(conf, new Path("/home/anurag/eclipse_mars_workspace/mapreduce/1/output"+current_index));

		FileInputFormat.setInputPaths(conf, "hdfs://lazy:54310/output_dm/test_final.txt");
		FileOutputFormat.setOutputPath(conf, new Path("hdfs://lazy:54310/output_dm/1/output"+current_index));

		JobClient.runJob(conf);
		return 0;
	}
}
