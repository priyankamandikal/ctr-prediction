# Prediction of Advertisement Click-Through-Rate

In online advertising, click-through rate (CTR) is a very important metric for evaluating ad performance. As a result, click prediction systems are essential. The are also widely used for sponsored search and real-time bidding. The dataset has 11 days worth of a company's (Avazu) data to build and test prediction models. 
The training set is 10 days of click-through data, ordered chronologically. Non-clicks and clicks are sub-sampled according to different strategies. 
The Test set is one day of ads for testing model predictions. 

There are 24 data fields as follows:		
		
**id:**	ad identifier	</br>
**click:**	0/1 for non-click/click	</br>
**hour:** 	format is YYMMDDHH	</br>
**C1:**	 anonymized categorical variable	</br>
**banner_pos:**	 (0/1) position of banner	</br>
**site_id:**	 (alphanumeric)	</br>
**site_domain:** 	categoryof site's domain (alphanumeric)	</br>
**site_category:**	category of site (alphanumeric)	</br>
**app_id:**	(alphanumeric)	</br>
**app_domain:**	categorization of application domain (alphanumeric)	</br>
**app_category:** 	categorization of application (alphanumeric)	</br>
**device_id:**	(alphanumeric)	</br>
**device_ip:**	ip address of the device	</br>
**device_model:**	model of the device	</br>
**device_type:**	type of device (1/0)	</br>
**device_conn_type:**	device connection type (0/2/5)	</br>
**C14-C21	anonymized:** categorical variables	</br>
		
**Number of attributres:**	24	
		
**Number of instances:**	70 Lakh	
		
**Number of class labels:**	2	 (0 and 1)
		
**Link to dataset:**	https://www.kaggle.com/c/avazu-ctr-prediction/data	
		
**Algorithm:**	C4.5	
		
**Data Preprocessing:**	We will filter out certain features based on their relevance and importance to our decison tree construction.	
		
## Implementation of the algorithm:		
		
The C4.5 algorithm is a decision-tree algorithm which makes use of entropy of  a split to calculate gain-ratio.		
Gain Ratio is Information gain divided by SplitInfo. The split with the best gain-ratio is taken to be the final split.		
This process is recursively performed until there are no more attributes left to fit or the number of instances is too low.		
		
To implement this algorithm in map-reduce form, we make use of 6 classes:		
1. C45		
2. MapClass		
3. Reduce		
4. GainRatio		
5. Split		
6. Test		
		
###Overview:		
		
The base class C45 calls MapClass which takes input from the data files and generates key value pairs for each line of the input file.		
This key value pairs are then passed onto the ReduceClass.		
The ReduceClass then aggregates these key value pairs and then writes  these pairs to an intermediate file.		
These files are read by the GainRatio class which calculates entropy and gain ratio for each attribute.		
The best gain ratio attribute is then stored into a static variable in the C45 class which keeps a count of the splitted attributes using the Split class.		
		
###Details:		
		
**C45**		
This is the  main class which instantiates and calls the other class objects.		
It is responsible for iterating over the input file as long as there are attributes to split on.		
It makes sure that the same attribute is not split on in a subtree.		
		
**MapClass**		
The map class reads in the input file and creates key-value pairs from it.		
The key in our case is the attribute index, attribute value and class label.		
The value is 1		
These key-value pairs are passed as input to the reduce class.		
		
For example, in our dataset,		
If the attribute app_domain with column index 2 can take the values A, B and C,		
then the map funtion associates a value 1 to every '2,app_domain,0'  key to create key-value pairs.		
It also associates a value 1 to every '2,app_domain,1'  key to create key-value pairs.		
This process is done for every attribute in the dataset.		
		
**ReduceClass**
This class creates an intermediate file displaying attribute-value and their respective class-label counts.		
It does it in the following manner:		
Whenever it sees a key, it looks through the input to fnd how many times the same key occurs and sums over the values (in our case,1)		
Eg. if ('2,A,0', 1) occurs 10 times in the input from map, then reduce computes this count and writes it into an intermediate file as (2, A, 0, 10)		
This process is repeated for every unique key-value pair in the input from map.		
		
**GainRatio**
This class has methods to compute the entropy of a split, obtain the information gain and hence compute GainRatio.		
We will be implementing the following methods:		
		
1. currentNodeEntry()	calculates the count of 0 and 1 class labels and calls the entropy class	
2. entropy()	"returns entropy from the class label counts
entropy(p1, p2,...pn) = -p1*log(p1) – p2*log(p2)...-pN*log(pN)
Where p1 = n1/sum(n1 + n2 + ...nN)"	
3. getcount()	takes a line from the intermediate file and initializes the count array	
4. gainratio()	calculates the gain ratio	
5. getvalues()	gets the attribute values 	
		
**Split**		
This class keeps a record of the attributes which have already been splitted on.		
		
**Test**		
This class tests the data based on the generated decison tree. 		
We will use accuracy, and F-measure to find out how well our model works.		
