import java.io.*;
import java.util.*;

/**
 * Created by Pavan Kanekal on 6/6/16.
 */

public class Graph {
    static HashMap<String, Person> people = new HashMap<>();
    static List<Integer> generations = new ArrayList<>();

    /**
     * Requires one argument which is location of input.tsv
     */
    public static void main (String [] args) {

        List<String> lineList = readFile(args[0]);
        constructPeople(lineList);

        try {
            graphPeople();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void constructPeople(List<String> lineList) {

        for (String s : lineList) {
            String[] cols = s.split("\t");

            if (cols.length == 3) {
                Person p;

                if (!people.containsKey(cols[0])) {
                    p = new Person(cols[0].trim());
                } else {
                    p = people.get(cols[0].trim());
                }

                // set littles
                String[] littles = cols[1].split(",");
                for (String l : littles) {
                    l = l.trim();
                    // Find person & add to littles list
                    if (people.containsKey((l))) {
                        // get person and add to list
                        p.littles.add(people.get(l));
                    } else {
                        // create person and add as big
                        Person p2 = new Person(l);
                        p.littles.add(p2);
                        people.put(l, p2);
                    }
                }
                int gen = Integer.parseInt(cols[2]);
                p.generation = gen;

                if (!generations.contains(gen))
                    generations.add(gen);
                people.put(cols[0], p);
            }
        }
    }

    public static List<String> readFile (String fileName) {
        List<String> lineList = new ArrayList<>();
        boolean ignoreFirstLine = false;
        try {
            File fin = new File(fileName);

            BufferedReader br = new BufferedReader(new FileReader(fin));

            String line;
            while ((line = br.readLine()) != null) {
                if (!ignoreFirstLine) {
                    ignoreFirstLine = true;
                }
                else {
                    lineList.add(line);
                }
            }

            br.close();
        }
        catch (Exception ignored) {

        }
        return lineList;
    }

    public static void graphPeople() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("drp-family-tree.dot", "UTF-8");

        //tree config settings
        writer.println("digraph DRP {\n"
			                  //+ "concentrate=true;\n"
                  		  + "center=true;\n"
                  			+ "ranksep=.75;\n"
                  			+ "\tedge [arrowsize=2.0];\n"
                  			+ "\tnode [color=lightblue2, fontsize=32, style=filled];" );

        //sort the array list
        Collections.sort(generations);

	      //find the year of current generation
	      int current = generations.get(generations.size()-1);

        //add each year of DRP generations as nodes
        for (int gen : generations) {
            if( gen <= current) {
		          int step = gen + 1;
            	writer.println("\"" + gen + "\" -> \"" + step + "\"");
	          }
        }

        // Print relations
        for (String name : people.keySet()) {
            Person p = people.get(name);
            for (Person little : p.littles) {
                if (little.name.equals("")) continue;
                writer.println("\"" + p.name + "\" -> \"" + little.name + "\"");
            }
        }

        // rank people of same generations on same level
        for (int gen : generations) {
            writer.print("{rank=same");
            writer.print(" \"" + gen + "\"");
            for (String name : people.keySet()) {
                Person p = people.get(name);
                if (p.generation == gen) {
                    writer.print(" \"" + p.name + "\"");
                }
            }
            writer.println("}");
        }

        // Make Rohan the highest node since we didn't do bigs/littles until he was captain.
        writer.println("{rank=source \"Rohan\" \"2009\"}");
        writer.println("}");
        writer.close();

    }
}
