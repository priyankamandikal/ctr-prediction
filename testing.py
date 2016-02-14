from __future__ import division
import numpy as np
import os

ruleFileName = 'rule_1lakh.txt'
ruleFile = open(ruleFileName, 'r+')

testsetName = 'test_final10l.txt'
testFile = open(testsetName, 'r+')

newsetName = 'newsetzz.txt'
newsetFile = open(newsetName, 'wb')

tp = 0
tn = 0
fp = 0
fn = 0

#parse over each record
for record in testFile:
	recordList = record.split()
	#print 'record:',record
	match = False

	#parse over every rule till you find a match
	for rule in ruleFile:
		rule = rule.split()
		#print 'rule:',rule
		indices = rule[0::2][:-1] #getting the indices
		values = rule[1::2] #getting the attribute values
		#print indices
		j =0 
		for i in indices:
			i = int(i)
			if recordList[i] == values[j]:
				#print 'matching index:',i
				if i == int(indices[-1]):
					#print 'last index'
					match = True
					if recordList[-1] == rule[-1]:
						if recordList[-1] == '1':
							#print 'True positive :)'
							tp = tp+1
						else:
							#print 'True negative :)'
							tn = tn+1
					else:
						if recordList[-1] == '1':
							#print 'False negative :('
							fn = fn+1
						else:
							#print 'False positive :('
							fp = fp+1
				j = j+1
			else:
				break
		if match == True:
			print 'Done!'
			newsetFile.write(record) #write matched record onto a file
			break
	if match ==False:
		print "Couldn't match any rule"	
	ruleFile.seek(0)

'''accuracy = (tp+tn)/(tp+fp+tn+fn)
precision = tp/(tp+fp)
recall = tp/(tp+fn)
fmeasure = (2*precision*recall)/(precision + recall)'''

print 'True positives:', tp
print 'True negatives:', tn
print 'False positives:', fp
print 'False negatives:', fn
'''print '\nAccuracy = ', accuracy
print 'Precision = ', precision
print 'Recall = ', recall
print 'F-measure = ', fmeasure'''

ruleFile.close()
testFile.close()
newsetFile.close()
