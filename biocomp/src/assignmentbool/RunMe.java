package assignmentbool;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    	int temp;
    	for(int i = 0; i < population.length-1; i+=2){   		
    		for(int j = 0; j < population[i].getGenes().length /2; j++){
    			temp = population[i].getGenes()[j];
    			population[i].setGene(population[i+1].getGenes()[j],j);
    			population[i+1].setGene(temp, j);
    		}
    	}
    }
    
    public static void mutatePopulation(float mutationRate, Individual[] population){
    	for(int i =0; i < population.length; i++){
    		for(int j = 0; j < population[i].getGenes().length; j++){
    			if(random.nextFloat() <= mutationRate){
    				if((j % Individual.RULE_SIZE) == Individual.RULE_SIZE -1){
	    				population[i].setGene(((population[i].getGenes()[j]+1) % 2), j);
    				}
    				else{
    					population[i].setGene(((population[i].getGenes()[j]+1) % 3), j);
    				}
    			}
    		}
    	}
    }
    
    public static void evaluateFitnessOfPopulation(Individual[] population, int[] trainingData){
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
     public static int populationSize = 600;
     public static int geneSize = 35;
     public static float mutationRate = (float) 0.025;
     public static int generations = 1000;
     public static String fileName = "data2.txt";
    
    public static void main(String[] args) throws IOException {


        int[] trainingData = null;
        Individual bestIndividual;
        
        
		//Read in training Data
		 try {
	            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
	            String allText = "";
	            String currentLine = null;
	            while((currentLine = bufferedReader.readLine()) != null) {
	            	currentLine = currentLine.replaceAll("\\s+","");
	            	allText = allText + currentLine;   
	            }
	            int lines = allText.length() / 6;
	            System.out.println(allText.length() + " " + lines);
	            trainingData = new int[allText.length()];
	            for(int i = 0; i < allText.length(); i++){
	            	trainingData[i] = allText.charAt(i) - '0';
	            }
	            bufferedReader.close();         
	        }
	        catch(FileNotFoundException ex) {
	            System.out.println( "Unable to open file '" + fileName + "'");                
	        }
	        catch(IOException ex) {
	            ex.printStackTrace();
	        }
        
        
        
        //initialising genes of each individual in a population 
        Individual[] population = new Individual[populationSize];
        for(int i = 0 ; i < population.length; i++){
            population[i] = new Individual(new int[geneSize]);
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
		FileWriter fileWriter = new FileWriter("output5.csv");
		fileWriter.append("GENERATION,MEAN FITNESS,BEST FITNESS\n");
		fileWriter.append("0" + "," + calcMeanFitness(population) + "," + findBestIndividual(population).getFitness() + "\n");
		
		
        
        //Main Evolution Loop
        for (int g = 0; g < generations; g++) {
        	
			Individual[] parentPop = selectParents(population);
	
		
			//Make Babies
			singlePointCrossover(parentPop);
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
			System.out.println("In offspring no " + pog + ", the average fitness is: " + calcMeanFitness(population));
			System.out.println("and the best is: " + findBestIndividual(population).getFitness());
			fileWriter.append(pog + "," + calcMeanFitness(population) + "," + findBestIndividual(population).getFitness() + "\n");
			bestIndividual = Individual.clone(findBestIndividual(population));
		}     
    	
        for(int i = 0; i < bestIndividual.getGenes().length; i++){
        	if((i % Individual.RULE_SIZE) == 0){
        		System.out.println("");
        	}
        	if((i % Individual.RULE_SIZE) == (Individual.RULE_SIZE -1)){
        		System.out.print(" ");
        	}
        	if(bestIndividual.getGenes()[i] == 2){
        		System.out.print("#");
        	}else{
        		System.out.print(bestIndividual.getGenes()[i]);
        	}
        	
        }
        
        fileWriter.flush();
        fileWriter.close();
        
    }
    
}
