package edu.iastate.anthill.wikiont;

// a simple data structure for triples
public class Triple
{
    private String subject;
    private String predicate;
    private String object;

    private boolean literalObject;

    public Triple(String Subject, String Predicate, String Object,
                  boolean literalObject)
    {
        this.subject = Subject;
        this.predicate = Predicate;
        this.object = Object;
        this.literalObject = literalObject;
    }

    public Triple(String Subject, String Predicate, String Object)
    {
        this.subject = Subject;
        this.predicate = Predicate;
        this.object = Object;
        this.literalObject = false;
    }

    public String toString()
    {
        if (literalObject)
        {
            return "[" + subject + "] [" + predicate + "] " + object;
        }
        else
        {
            return "[" + subject + "] [" + predicate + "] [" + object + "]";
        }
    }

    public boolean isLiteralObject()
    {
        return literalObject;
    }

    public String getObject()
    {
        return object;
    }

    public String getPredicate()
    {
        return predicate;
    }

    public String getSubject()
    {
        return subject;
    }

}
