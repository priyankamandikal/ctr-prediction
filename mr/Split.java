package mr;

import java.util.ArrayList;
import java.util.List;

//keeps a tab on the attributes that have already been split
public class Split implements Cloneable{
	public List attr_index;
	public List attr_value;
	double entophy;
	String classLabel;
	Split()
	{
		 this.attr_index= new ArrayList<Integer>(); //creates an arraylist for attr_index
		 this.attr_value = new ArrayList<String>(); ////creates an arraylist for attr_value
	}
	Split(List attr_index,List attr_value)
	{
		this.attr_index=attr_index;
		this.attr_value=attr_value;
		
	}
	
	void add(Split obj)
	{
		this.add(obj);
	}
	

}
