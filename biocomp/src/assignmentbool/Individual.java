/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentbool;

import java.util.Random;

public class Individual {
    
    public static Random random = new Random();
    private int[] genes;
    private int fitness;
    final public static int RULE_SIZE = 7;
    final public static int TRAIN_RULE_SIZE = 64;

    public Individual(int[] genes) {
        this.genes = genes;
        this.fitness = 0;
    }
    
    public void populateGenes(){
        for(int i =0; i < this.genes.length; i++){
        	if(((i+1) % RULE_SIZE) == 0){
        		this.genes[i] = random.nextInt(2);
        	}
        	else{
        		this.genes[i] = random.nextInt(3);
        	}
        }
    }
    
    public static Individual clone(Individual individualToCopy){
    	int[] copiedGenes = new int[individualToCopy.getGenes().length];
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
    public void evaluateFitness (int[] trainingData){
    	
    	int matchCondition = 0; //keeps track on how much of a rule matches
    	int trainingIndex = 0;
    	int individualIndex = 0;
    	
    	this.fitness = 0;
    	for(int i = 0; i < TRAIN_RULE_SIZE; i++){ //for every training rule
    		for(int j = 0; j < RunMe.geneSize/RULE_SIZE; j++ ){ //for every candidate rule
    			matchCondition = 0;
    			for(int k = 0; k < RULE_SIZE -1; k++){ //loop through only conditions of rule
    				
    				trainingIndex = (i * RULE_SIZE) + k;
    				individualIndex = (j * RULE_SIZE) + k;
    				
    				if((this.genes[individualIndex] == trainingData[trainingIndex]) || (this.genes[individualIndex] == 2)){
    					matchCondition++;
    				}
    				
    			}
    			
    			trainingIndex = (i * RULE_SIZE) + RULE_SIZE -1; // indexes are pointing to last bit in rule (the outcome)
				individualIndex = (j * RULE_SIZE) + RULE_SIZE -1;
    			
    			if(matchCondition == RULE_SIZE -1){ //if all conditions match
    				if((this.genes[individualIndex] == trainingData[trainingIndex])){
    					this.fitness++;
    				}
    				break;
    			}
    			
    		}
    	}
        
    }
    
    public int[] getGenes() {
        return this.genes;
    }

    public void setGenes(int[] gene) {
        this.genes = gene;
    }
    
    public void setGene(int geneValue, int index){
    	this.genes[index] = geneValue;
    }

}
