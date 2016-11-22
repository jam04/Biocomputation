/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga;

import java.util.Random;

public class Individual {
    
    public static Random random = new Random();

    public Individual(int[] genes) {
        this.genes = genes;
        this.fitness = 0;
    }
    
    public void populateGenes(){
        for(int i =0; i < this.genes.length; i++){
            this.genes[i] = random.nextInt(2);
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
    
    
    private int[] genes;
    private int fitness;

    public int getFitness() {
        return this.fitness;
    }
    
    public void evaluateFitness (){
        this.fitness = 0;
        for(int i = 0; i < this.genes.length; i++){
            if(this.genes[i] == 1){
                this.fitness++;
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
