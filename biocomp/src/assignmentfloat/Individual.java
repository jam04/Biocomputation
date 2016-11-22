/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentfloat;

import java.util.Random;

public class Individual {
    
    public static Random random = new Random();
    private float[] genes;
    private int fitness;
    final public static int RULE_SIZE = 13;
    final public static int TRAIN_RULE_SIZE = 7;
    final public static int NO_OF_TRAIN_RULES = 1000;

    public Individual(float[] genes) {
        this.genes = genes;
        this.fitness = 0;
    }
    
    public void populateGenes(){
        for(int i =0; i < this.genes.length; i++){
        	if(((i+1) % RULE_SIZE) == 0){
        		this.genes[i] = (float) random.nextInt(2);
        	}
        	else{
        		this.genes[i] = random.nextFloat();
        	}
        }
    }
    
    public static Individual clone(Individual individualToCopy){
    	float[] copiedGenes = new float[individualToCopy.getGenes().length];
    	for(int i = 0; i < copiedGenes.length; i++){
    		copiedGenes[i] = individualToCopy.getGenes()[i];
    	}
    	Individual twin = new Individual(copiedGenes);
    	twin.fitness = individualToCopy.fitness;
    	
    	return twin;
    }
    
    public int getFitness() {
        return this.fitness;
    }
    
    //Fitness is determined by how many rules in a candidate matches the rules in the training data  
    public void evaluateFitness (float[] trainingData){
    	
    	int matchCondition = 0; //keeps track on how much of a rule matches
    	int trainingIndex = 0;
    	int individualIndex;
    	int individualRange1;
    	int individualRange2;
    	
    	this.fitness = 0;
    	for(int i = 0; i < NO_OF_TRAIN_RULES; i++){ //for every training rule
    		for(int j = 0; j < RunMe.geneSize/RULE_SIZE; j++ ){ //for every candidate rule
    			matchCondition = 0;
    			for(int k = 0; k < TRAIN_RULE_SIZE -1; k++){ //loop through only conditions of training rule
    				
    				trainingIndex = (i * TRAIN_RULE_SIZE) + k;
    				individualRange1 = (j * RULE_SIZE) + (k*2);
    				individualRange2 = (j * RULE_SIZE) + ((k*2) + 1);
    				
    				if(trainingData[trainingIndex] >= this.genes[individualRange1]){
    					if(trainingData[trainingIndex] <= this.genes[individualRange2]){
    						matchCondition++;
    					}
    				}
    				else{
    					if(trainingData[trainingIndex] >= this.genes[individualRange2]){
    						if(trainingData[trainingIndex] <= this.genes[individualRange2]){
    							matchCondition++;
    							}
    						}
    					}
    			}
    			
    			trainingIndex = (i * TRAIN_RULE_SIZE) + TRAIN_RULE_SIZE -1; // indexes are pointing to last bit in rule (the outcome)
				individualIndex = (j * RULE_SIZE) + RULE_SIZE -1;
    			
    			if(matchCondition == TRAIN_RULE_SIZE -1){ //if all conditions match
    				if((this.genes[individualIndex] == trainingData[trainingIndex])){
    					this.fitness++;
    				}
    				break;
    			}
    			
    		}
    	}
        
    }
    
    public float[] getGenes() {
        return this.genes;
    }

    public void setGenes(float[] gene) {
        this.genes = gene;
    }
    
    public void setGene(float geneValue, int index){
    	this.genes[index] = geneValue;
    }

}
