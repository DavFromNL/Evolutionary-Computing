import java.lang.Math;
import java.util.*;

//import sun.security.util.Length;

/* YOU NEED TO ADD YOUR CODE TO THIS CLASS, REMOVING ALL DUMMY CODE
 *
 * DO NOT CHANGE THE NAME OR SIGNATURE OF ANY OF THE EXISTING METHODS
 * (Signature includes parameter types, return types and being static)
 *
 * You can add private methods to this class if it makes your code cleaner,
 * but this class MUST work with the UNMODIFIED Tester2.java class.
 *
 * This is the ONLY class that you can submit for your assignment.
 *
 * MH October 2020
 */
public class Queens2
{
    private static int boardSize = 12;
    

    // ************ A. RANK A SET OF VALUES ***********************************

    
    // Finished
    public static double[] rank(Integer values[])
    {
        int valuesLength = values.length; 
        double rank[] = new double[valuesLength];
        
        Integer valuesSorted[] = Arrays.copyOf(values, valuesLength);
        Arrays.sort(valuesSorted);

        for(int i = 0; i < valuesLength; i++){
            int r = valuesSorted[i];
            int k = i;
            double valueRank = k;
            int counter = 1;
            while(valuesSorted[k] == valuesSorted[(k+1)%(valuesLength)]){
                valueRank += k+1;
                k++;
                counter++;
            }
            valueRank /= counter;
            i += counter - 1;
            for(int j = 0; j< valuesLength; j++){
                if(r == values[j]){
                    rank[j] = valueRank;
                }
            }
            
        }
        
        return rank;
    }

    
    // ************ B. CALCULATE LINEAR RANKING PROBABILITY *******************
    
    // Finished
    public static double linearRankingProb(double rank, double s, int populationSize){
        
        // YOUR CODE GOES HERE
        // DUMMY CODE TO REMOVE:
        double probability = (2-s) +((2*rank)*(s-1)/(populationSize-1));
        double normalizedProbability =  probability/populationSize;
        // END OF YOUR CODE
        
        return normalizedProbability;
    }

    // ************ C. PERFORM LINEAR RANKING PARENT SELECTION ****************
    
    /* performs linear ranking parent selection (see class slides & the guide in the course shell)
     * s is the 'selective pressure' parameter from the P[i] equation
     */
    public static Integer[][] linearRankingSelect(Integer[][] population, double s)
    {
        // The first three parts of this method have been written for you...
        // But the fourth part - selecting the two parents - is up to you!
        
        Integer [][] parents = new Integer [2][boardSize]; // will hold the selected parents
        int popSize = population.length;
        
        // 1. determine the fitness of each member of the population
        Integer [] fitness = getFitnesses(population);
        
        // 2. hence determine the ranking of each member by its fitness
        double [] rank = rank(fitness);
        
        // 3. determine the cumulative probability of selecting each member, using the linear ranking formula
        double [] cumulative = new double [popSize];
        cumulative[0] = linearRankingProb(rank[0], s, popSize);
        for (int index = 1; index < popSize; index ++)
        {
            cumulative[index] = cumulative[index-1] + linearRankingProb(rank[index], s, popSize);
        }
        
        // 4. finally, select two parents, based on the cumulative probabilities
        // see the pseudocode in RESOURCES > Evolutionary Computation General >
        // Linear Ranking: How to use cumulative probability to select parents
        
        // YOUR CODE GOES HERE
        double rand1 = Math.random();
        int first = 0;
        for(int i = 0; i<cumulative.length; i++){

            if(rand1 < cumulative[i]){
                first = i;
                parents[0] = population[i];
                break;
            }
        }

        int second = Integer.valueOf(first);
        double rand2 = Math.random();
        while(first == second){
            rand2 = Math.random();
            for(int j = 0; j<cumulative.length; j++){

                if(rand2 < cumulative[j]){
                        second = j;
                        parents[1] = population[j];
                        break;
                    }
            }
        }
        // END OF YOUR CODE
        
        return parents;
    }
    

    // ************ D. (λ, μ) SURVIVOR SELECTION ******************************

    
    /* creates a new population through (λ, μ) survivor selection
     * given a required population of size n, and a set of children of size 2n,
     * this method will return the n fittest children as the new population
     */
    public static Integer[][] survivorSelection(Integer[][] children, int n)
    {
        Integer [][] newPop = new Integer [n][boardSize];
        Integer values[] = getFitnesses(children);

        Integer sortedValues[] = Arrays.copyOf(values, values.length);
        Arrays.sort(sortedValues);
        int selectedChildren = 0;
        for(int i = values.length-1; i> -1+n; i--){
            int soughtValue = sortedValues[i];
            for(int j = 0; j<values.length; j++){
                if(values[j] == soughtValue){
                    newPop[selectedChildren] = children[j];
                    selectedChildren++;
                    break;
                }
            }
        }
        

        // END OF YOUR CODE
        
        return newPop;
    }
    

    // ************ E. FITNESS DIVERSITY **************************************

    
    // Finished?
    public static int fitnessDiversity(Integer[][] population)
    {
        // YOUR CODE GOES HERE
        // DUMMY CODE TO REMOVE:
        Integer values[] = getFitnesses(population);
        int uniqueFitnessValues = values.length;
        for(int i = 0; i<values.length-1; i++){
            for(int j = i+1; j<values.length; j++){
                if(values[i] == values[j]){
                    uniqueFitnessValues--;
                    break;
                }
            }
        }

        // END OF YOUR CODE
        
        return uniqueFitnessValues;
    }
    

    // ************ F.  INVERSION MUTATION ************************************
    
    // Finished
    public static Integer[] inversionMutate(Integer[] genotype, double p)
    {
        Random rand = new Random();
        double rand1 = Math.random();
        if(p < rand1){
            return genotype;
        } 
        int randLower = rand.nextInt(genotype.length);
        int randUpper = rand.nextInt(genotype.length);
        while(randLower == randUpper){
            randUpper = rand.nextInt(genotype.length);
        }
        if(randLower > randUpper){
            int temp = randUpper;
            randUpper = randLower;
            randLower = temp;
        }
        // YOUR CODE GOES HERE
        // DUMMY CODE TO REMOVE:
        Integer swapped[] = new Integer[randUpper-randLower + 1];
        int counter = 0;
        for(int i = randLower; i <= randUpper; i++){
            swapped[counter] = genotype[i];
            counter++;
        }

        List<Integer> intList = Arrays.asList(swapped);
		Collections.shuffle(intList);
        intList.toArray(swapped);
        
        counter = 0;
        for(int i = randLower; i <= randUpper; i++){
            genotype[i] = swapped[counter];
            counter++;
        }
        // END OF YOUR CODE
        
        return genotype;
    }

    private static Integer[] getFitnesses(Integer [][] population)
    {
        int popSize = population.length;
        Integer [] fitness = new Integer [popSize];
        
        for (int index = 0; index < popSize; index ++)
        {
            fitness[index] = Queens.measureFitness(population[index]);
        }
        
        return fitness;
    }
}
