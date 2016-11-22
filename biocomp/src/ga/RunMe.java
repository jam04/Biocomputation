package ga;
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
    				if(population[i].getGenes()[j] == 0){
    					population[i].setGene(1, j);
    				}
    				else{
    					population[i].setGene(0, j);
    				}
    			}
    		}
    	}
    }
    
    public static void evaluateFitnessOfPopulation(Individual[] population){
        for (int i = 0; i < population.length; i++) {
            population[i].evaluateFitness();
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
    
    public static void main(String[] args) throws IOException {

        //Params      
        int populationSize = 50;
        int geneSize = 100;
        float mutationRate = (float) 0.01;
        int generations = 120;
        
        Individual bestIndividual;
        
        
        
        //initialising genes of each individual in a population 
        Individual[] population = new Individual[populationSize];
        for(int i = 0 ; i < population.length; i++){
            population[i] = new Individual(new int[geneSize]);
        }
        for(int i =0; i < populationSize; i++){
            population[i].populateGenes();                       
        }
        evaluateFitnessOfPopulation(population);
        System.out.println("In the intial population, the average fitness is: " + calcMeanFitness(population));
		System.out.println("and the best is: " + findBestIndividual(population).getFitness());
		//grabbing best individual to use to replace worst individual in next generation
		bestIndividual = Individual.clone(findBestIndividual(population));
		
		
		//CSV Stuff
		FileWriter fileWriter = new FileWriter("output.csv");
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
			evaluateFitnessOfPopulation(offspringPop);
			
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
    	
        fileWriter.flush(); 
        fileWriter.close(); 

        
    }
    
}
