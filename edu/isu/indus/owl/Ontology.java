package edu.isu.indus.owl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * The internal model for ontology : a list of triple
 *
 * @author Jie Bao
 * @version 1.0 2004-02-08
 */
public class Ontology
{

    protected Vector classList = new Vector();

    public Triple getAt(int i)
    {
        return (Triple) classList.elementAt(i);
    }

    public void add(String Subject)
    {
        classList.add(new Triple(Subject));
    }

    public void add(String Subject, String Predicate, String Object)
    {
        classList.add(new Triple(Subject, Predicate, Object));
    }

    /**
     * retrun a HTML string for all classes
     *
     * @return String
     * @version 2004-02-06
     */
    public String getClassList()
    {
        if (classList == null)
        {
            return "";
        }

        sort();
        StringBuffer buffer = new StringBuffer();
        buffer.append("<ul>");
        for (int i = 0; i < classList.size(); i++)
        {
            buffer.append(classList.elementAt(i) + "<br>");
        }
        buffer.append("</ul>");
        return buffer.toString();
    }

    public void sort()
    {
        Collections.sort(classList, new Sorter(true));
    }

    /**
     * Save the ontology in a file
     * @version 2004-02-07
     */
    public void save(String fileName)
    {
        if (fileName == null)
        {
            return;
        }
        try
        {
            File saveFile = new File(fileName);

            FileWriter t_fwWriter = new FileWriter(saveFile);
            PrintWriter t_pwWriter = new PrintWriter(t_fwWriter);

            for (int i = 0; i < classList.size(); i++)
            {
                try
                {
                    // save
                    t_pwWriter.println(classList.elementAt(i) + "\n");
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            t_pwWriter.close();
            t_fwWriter.close();
        }
        catch (Exception ex1)
        {
            ex1.printStackTrace();
        }

    }

    /**
     * Load the ontology from a file
     * @version 2004-02-08
     */
    public void load(String fileName)
    {
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            clearList();
            String line = in.readLine();
            while (line != null)
            {
                if (line.length() > 0)
                {
                    classList.add(new Triple(line, true));
                }
                line = in.readLine();
            }
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Return the number of concepts
     *
     * @return int
     * @version 2004-02-08
     */
    public int getConceptNumber()
    {
        if (classList == null)
        {
            return 0;
        }
        else
        {
            return classList.size();
        }
    }

    /**
     * Delete all elements in class list
     * @version 2004-02-06
     *
     */
    public void clearList()
    {
        if (classList != null)
        {
            classList.removeAll(classList);
        }
    }

    /**
     * remove specified class
     * @param str String
     * @version 2004-02-06
     */
    public void delete(String str)
    {
        if (classList == null)
        {
            return;
        }

        Vector todelete = new Vector();

        for (int i = 0; i < classList.size(); i++)
        {
            if (str.equalsIgnoreCase(getAt(i).subject))
            {
                todelete.add(classList.elementAt(i));
                classList.removeAll(todelete);
                break;
            }
        }
    }
}

/**
 * This comparator is used to sort vectors of data
 *
 */
class Sorter
    implements Comparator
{
    boolean ascending;
    Sorter(boolean ascending)
    {
        this.ascending = ascending;
    }

    public int compare(Object a, Object b)
    {
        Object o1 = a;
        Object o2 = b;

        // Treat empty strains like nulls
        if (o1 instanceof String && ( (String) o1).length() == 0)
        {
            o1 = null;
        }
        if (o2 instanceof String && ( (String) o2).length() == 0)
        {
            o2 = null;
        }

        // Sort nulls so they appear last, regardless
        // of sort order
        if (o1 == null && o2 == null)
        {
            return 0;
        }
        else if (o1 == null)
        {
            return 1;
        }
        else if (o2 == null)
        {
            return -1;
        }
        else if (o1 instanceof Comparable)
        {
            if (ascending)
            {
                return ( (Comparable) o1).compareTo(o2);
            }
            else
            {
                return ( (Comparable) o2).compareTo(o1);
            }
        }
        else
        {
            if (ascending)
            {
                return o1.toString().compareTo(o2.toString());
            }
            else
            {
                return o2.toString().compareTo(o1.toString());
            }
        }
    }
}
