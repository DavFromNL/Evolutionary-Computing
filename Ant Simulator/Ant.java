import java.lang.Math;
import java.util.*;

public class Ant
{
    private AntSim sim;           // simulation that this ant belongs to
    private int [] tour;          // list of nodes in the order they were visited
    private int tourLength;       // length of tour
    private int numNodes;         // size of problem
    

    public ArrayList<Integer> notYetVisited(boolean visited[])
    {
        ArrayList<Integer> notVisited = new ArrayList<Integer>();

        for(int i = 0; i < visited.length; i++){
            if(!(visited[i])){
                notVisited.add(i);
            }
        }
        return notVisited;
    }
    

    public ArrayList<Integer> edgeLengths(int currentNode, ArrayList<Integer> notVisited)
    {
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        
        // YOUR CODE GOES HERE - REPLACE THIS DUMMY CODE
        for(int i = 0; i < notVisited.size(); i++){
            int len = sim.getEdgeLength(currentNode, notVisited.get(i));
            lengths.add(len);
        }
        
        return lengths;
    }
    

    public ArrayList<Double> pheromoneLevels(int currentNode, ArrayList<Integer> notVisited)
    {
        ArrayList<Double> levels = new ArrayList<Double>();
        
        for(int i = 0; i < notVisited.size(); i++){
            double len = sim.getPheromoneLevel(currentNode, notVisited.get(i));
            levels.add(len);
        }
        
        return levels;
    }
    
    public ArrayList<Double> edgeProbs(ArrayList<Double> levels, ArrayList<Integer> lengths)
    {
        ArrayList<Double> probabilities = new ArrayList<Double>();
        
        double tau = 0;
        double nau = 0;
        double cumSum = 0; 
        double num = 0;

        double alpha = 1;
        double beta = 2;

        for(int i = 0; i < levels.size(); i++){
            tau = levels.get(i);
            nau = lengths.get(i);
            num = Math.pow(tau, alpha)/Math.pow(nau, beta);
            cumSum += num;
            probabilities.add(num);
        }

        double temp = 0;
        for(int j = 0; j < probabilities.size(); j++){
            temp = probabilities.get(j);
            probabilities.set(j, temp/cumSum);
        }        
        return probabilities;
    }
    

    public ArrayList<Double> cumulativeProbs(ArrayList<Double> probabilities)
    {
        ArrayList<Double> cumulative = new ArrayList<Double>();
        
        double rand1 = Math.random();
        double newSum = 0;
        for(int i = 0; i<probabilities.size(); i++ ){
            newSum += probabilities.get(i);
            cumulative.add(newSum);
        }

        
        return cumulative;
    }
    

    public int selectNextNode(ArrayList<Double> cumulative)
    {
        double rand1 = Math.random();
        for(int i = 0; i<cumulative.size(); i++){
            if(rand1 < cumulative.get(i)){
                return(i);
            }
        }
        return 0;
    }
    
    
    public Ant(AntSim sim, int numNodes)
    {
        this.sim = sim;
        this.numNodes = numNodes;
        tour = new int [numNodes];
    }
    
    public int tour()
    {
        // place this ant on a starting node, chosen at random
        int currentNode = (int) (Math.random() * numNodes);
        
        // initialise the recording of the tour
        boolean [] visited = new boolean [numNodes];
        visited [currentNode] = true;
        tour [0] = currentNode;
        tourLength = 0;
        for (int index = 0; index < numNodes; index ++)
        {
            if (index != currentNode) { visited [index] = false; }
        }
        
        int nodeCount = 1; // how many nodes have been visited so far
        
        // keep going until the tour is complete:
        while (nodeCount < numNodes)
        {
            // determine which nodes have not yet been visited
            ArrayList<Integer> notVisited = notYetVisited(visited);
            
            // for each of those nodes, get the lengths and pheromone levels of the edges that lead to them
            ArrayList<Integer> lengths = edgeLengths(currentNode, notVisited);
            ArrayList<Double> levels = pheromoneLevels(currentNode, notVisited);
            
            // ...hence get the probability of choosing each edge
            ArrayList<Double> probabilities = edgeProbs( levels, lengths);
            
            // ...hence get the cumulative probabilties
            ArrayList<Double> cumulative = cumulativeProbs(probabilities);
            
            // ...and finally choose the next node to visit using roulette wheel selection
            int chosenIndex = selectNextNode(cumulative);
            int chosen = notVisited.get(chosenIndex);
            
            // update the tour records
            visited[chosen] = true;
            tourLength += sim.getEdgeLength(currentNode, chosen);
            currentNode = chosen;
            tour[nodeCount] = chosen;
            nodeCount ++;
        }
        // there is one final edge to visit, which takes the ant back to its starting node
        tourLength += sim.getEdgeLength(tour[numNodes - 1], 0);
        
        return tourLength;
    }
    
    // lay pheromone inversely proportional to the length of this ant's tour
    public void layPheromone()
    {
        // how much pheromone to lay on each edge
        double perEdge = (double) (sim.getTotalPheromone() / tourLength);
        
        // re-trace this ants tour and evenly spread pheromone on each edge
        int previous = 0;
        for (int index = 1; index < numNodes + 1; index ++)
        {
            int current = tour[index % numNodes];
            sim.addPheromone(previous, current, perEdge);
            previous = current;
        }
    }
    
    // return the length of this ant's tour
    public int getTourLength()  { return tourLength; }
    
    // print the tour details to console
    public void printTour()
    {
        System.out.println("Tour length of " + tourLength +
                           ", visiting the nodes in this order:");
        for (int index = 0; index < tour.length - 1; index ++)
        {
            System.out.print(tour [index] + ", ");
        }
        System.out.println(tour [tour.length - 1] + ".");
    }
}
