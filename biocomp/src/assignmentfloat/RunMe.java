package assignmentfloat;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class RunMe {

    public static Random random = new Random();
    
    public static Individual[] selectParents(Individual[] population){
        
        Individual[] parentPop =  new Individual[population.length];
        
        for (int i = 0; i < population.length; i++) {
                        
            Individual parent1 = Individual.clone(population[random.nextInt(population.length)]);
            Individual parent2 = Individual.clone(population[random.nextInt(population.length)]);
            
            if(parent1.getFitness() >= parent2.getFitness()){
                parentPop[i] = parent1;
                
                
            }
            else{
                parentPop[i] = parent2; 
            }
        }
        return parentPop;
    }
    
    public static void singlePointCrossover(Individual[] population){
    	float temp;
    	for(int i = 0; i < population.length-1; i+=2){   		
    		for(int j = 0; j < population[i].getGenes().length /2; j++){
    			temp = population[i].getGenes()[j];
    			population[i].setGene(population[i+1].getGenes()[j],j);
    			population[i+1].setGene(temp, j);
    		}
    	}
    }
    
    public static void blendCrossover(Individual[] population){
    	float newValue;
    	for(int i = 0; i < population.length-1; i+=2){
    		for(int j = 0; j < population[i].getGenes().length; j++ ){
    			newValue = (population[i].getGenes()[j] + population[i+1].getGenes()[j]) /2;
    			if(j % Individual.RULE_SIZE == Individual.RULE_SIZE -1 ){
    				if(population[i].getGenes()[population[i].getGenes().length-1] == population[i+1].getGenes()[population[i+1].getGenes().length-1]){
    	    			//values stay the same
    	    		}else{
    	    			if(random.nextFloat() <0.5){
    	    				population[i].setGene((float) 1.0,population[i].getGenes().length-1);
    	        			population[i+1].setGene((float) 1.0, population[i+1].getGenes().length-1);
    	    			}else{
    	    				population[i].setGene((float) 0.0,population[i].getGenes().length-1);
    	        			population[i+1].setGene((float) 0.0, population[i+1].getGenes().length-1);
    	    			}
    	    		}
    			}else{
    			population[i].setGene(newValue,j);
    			population[i+1].setGene(newValue, j);
    			}
    		}
    		
    	}
    }
    
    public static void mutatePopulation(float mutationRate, Individual[] population){
    	float creepingMutation;
    	for(int i =0; i < population.length; i++){
    		for(int j = 0; j < population[i].getGenes().length; j++){
    			if(random.nextFloat() <= mutationRate){
    				if((j % Individual.RULE_SIZE) == Individual.RULE_SIZE -1){
	    				population[i].setGene(((population[i].getGenes()[j]+1) % 2), j); //flips bit of outcome value
    				}
    				else{
    					creepingMutation = random.nextFloat() / 3; //max of 0.3* creep
    					
    					if(population[i].getGenes()[j] + creepingMutation > 1){
    						population[i].setGene(population[i].getGenes()[j] - creepingMutation, j); //subtract if adding goes over 1
    					}
    					else{
    						if(population[i].getGenes()[j] - creepingMutation < 0){
    							population[i].setGene(population[i].getGenes()[j] + creepingMutation, j); //add if subtracting goes under 0
    						}
    						else{
    							if(random.nextFloat() <= 0.5){ //else its 50% chance to add or subtract
    	    						population[i].setGene(population[i].getGenes()[j] + creepingMutation, j); 
    	    					}
    	    					else{
    	    						population[i].setGene(population[i].getGenes()[j] - creepingMutation, j);
    	    						}
    							}
    						}
    					}
    			}
    		}
    	}
    }
    
    public static void evaluateFitnessOfPopulation(Individual[] population, float[] trainingData){
        for (int i = 0; i < population.length; i++) {
            population[i].evaluateFitness(trainingData);
        }
    }
    
    public static int calcMeanFitness(Individual[] population){
    	int totalFitness = 0;
    	int populationSize = population.length;
    	int meanFitness;
    	
    	for(int i = 0; i < populationSize; i++){
    		totalFitness = totalFitness + population[i].getFitness();
    	}
    	meanFitness = totalFitness / populationSize;
    	
    	return meanFitness;
    }
    
    public static Individual findBestIndividual(Individual[] population){
    	Individual bestIndividual = population[0];
    	for(int i = 1; i < population.length; i++){
    		if(bestIndividual.getFitness() < population[i].getFitness()){
    			bestIndividual = population[i];
    		}
    	}
    	return bestIndividual;
    }  
    
    public static void replaceLastBestWithWorst(Individual best, Individual[] population){
    	int indexOfWorst = 0;
    	for(int i = 0; i < population.length; i++){
    		if(population[indexOfWorst].getFitness() > population[i].getFitness()){
    			indexOfWorst = i;
    		}
    	}
    	
    	population[indexOfWorst] = best;
    }
    
    //Params      
     public static int populationSize = 1000;
     public static int geneSize = (13 * 5);
     public static float mutationRate = (float) 0.008;
     public static int generations = 3000;
     public static String TrainingfileName = "data3TrainingRand.txt";
     public static String ValidationfileName = "data3ValidationRand.txt";
    
    public static void main(String[] args) throws IOException {


        float[] trainingData = null;
        float[] validationData = null;
        Individual bestIndividual;
        Individual individualToValidate = null;
        
        
		//Read in training Data
		 try {
	            BufferedReader bufferedReader = new BufferedReader(new FileReader(TrainingfileName));
	            ArrayList<String> allText =  new ArrayList<String>();
	            String currentLine = null;
	            String[] currentLineArray = null;
	            while((currentLine = bufferedReader.readLine()) != null) {
	            	currentLineArray = currentLine.split("\\s+");
	            	for(int i = 0; i < currentLineArray.length; i++){
	            		allText.add(currentLineArray[i]);
	            	}
	            }
	            trainingData = new float[allText.size()];
	            for(int i = 0; i < allText.size(); i++){
	            	trainingData[i] = Float.parseFloat(allText.get(i));
	            }
	            bufferedReader.close();         
	        }
	        catch(FileNotFoundException ex) {
	            System.out.println( "Unable to open file '" + TrainingfileName + "'");                
	        }
	        catch(IOException ex) {
	            ex.printStackTrace();
	        }
		 
		 //Read in Validation Data
		 try {
	            BufferedReader bufferedReader = new BufferedReader(new FileReader(ValidationfileName));
	            ArrayList<String> allText =  new ArrayList<String>();
	            String currentLine = null;
	            String[] currentLineArray = null;
	            while((currentLine = bufferedReader.readLine()) != null) {
	            	currentLineArray = currentLine.split("\\s+");
	            	for(int i = 0; i < currentLineArray.length; i++){
	            		allText.add(currentLineArray[i]);
	            	}
	            }
	            validationData = new float[allText.size()];
	            for(int i = 0; i < allText.size(); i++){
	            	validationData[i] = Float.parseFloat(allText.get(i));
	            }
	            bufferedReader.close();         
	        }
	        catch(FileNotFoundException ex) {
	            System.out.println( "Unable to open file '" + ValidationfileName + "'");                
	        }
	        catch(IOException ex) {
	            ex.printStackTrace();
	        }
        
        //initialising genes of each individual in a population 
        Individual[] population = new Individual[populationSize];
        for(int i = 0 ; i < population.length; i++){
            population[i] = new Individual(new float[geneSize]);
        }
        for(int i =0; i < populationSize; i++){
            population[i].populateGenes();                       
        }
        evaluateFitnessOfPopulation(population, trainingData);
        System.out.println("In the intial population, the average fitness is: " + calcMeanFitness(population));
		System.out.println("and the best is: " + findBestIndividual(population).getFitness());
		//grabbing best individual to use to replace worst individual in next generation
		bestIndividual = Individual.clone(findBestIndividual(population));
		
		
		

		//CSV Stuff
		FileWriter fileWriter = new FileWriter("outputBlend5.csv");
		fileWriter.append("GENERATION,MEAN FITNESS,BEST FITNESS,VALIDATED FITNESS\n");
		fileWriter.append("0" + "," + calcMeanFitness(population) + "," + findBestIndividual(population).getFitness() + "\n");
		
		
        
        //Main Evolution Loop
        for (int g = 0; g < generations; g++) {
        	
			Individual[] parentPop = selectParents(population);
	
		
			//Make Babies
			//singlePointCrossover(parentPop);
			blendCrossover(parentPop);
			//the offspring is the crossed over and mutated parents
			Individual[] offspringPop = parentPop;
			mutatePopulation(mutationRate, offspringPop);
			evaluateFitnessOfPopulation(offspringPop, trainingData);
			
			//the offspring becomes the population (parents get old and die)
			population = offspringPop;
			
			//replaced worst individual with best from last generation
			replaceLastBestWithWorst(bestIndividual, population);
			
			//printouts
			int pog = g +1; //for display purposes
			fileWriter.append(pog + "," + calcMeanFitness(population) + "," + findBestIndividual(population).getFitness());
			bestIndividual = Individual.clone(findBestIndividual(population));
			
			if((g % 100) == 99){ //every 100 generations, be individual is validated against test data
				individualToValidate = Individual.clone(bestIndividual);
				individualToValidate.evaluateFitness(validationData);
				System.out.println("In offspring no " + pog + ", the fitness of best individual ("+ bestIndividual.getFitness() +") is validated to: " + individualToValidate.getFitness());
				fileWriter.append("," + individualToValidate.getFitness());
			}
			fileWriter.append("\n");
		}
        
        for(int i = 0; i < (geneSize/Individual.RULE_SIZE); i++){
        	for(int j = 0; j < Individual.RULE_SIZE; j++){
        		System.out.printf("%01.2f ",individualToValidate.getGenes()[(i*(Individual.RULE_SIZE))+j]);
        	}
        	System.out.println("");
        }
        
        fileWriter.flush();
        fileWriter.close();
		 
		 
		 
		 
		 
    }
    
}
