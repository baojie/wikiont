package edu.isu.indus.owl;

import edu.isu.indus.owl.jena.tree.*;

public class Name
    implements Comparable
{
    String name;
    DefaultNode node;

    public Name(String name, DefaultNode node)
    {
        this.name = name;
        this.node = node;
    }

    public String toString()
    {
        return name;
    }

    public int compareTo(Object o)
    {
        return this.toString().compareTo(o.toString());
    }
}
