package edu.isu.indus.owl;

import java.util.StringTokenizer;

class Triple
{
    String subject;
    String predicate;
    String object;

    public Triple(String Subject, String Predicate, String Object)
    {
        this.subject = Subject;
        this.predicate = Predicate;
        this.object = Object;
    }

    public Triple(String Subject)
    {
        this.subject = Subject;
        this.predicate = "";
        this.object = "";
    }

    public String toString()
    {
        return subject + " " + predicate + " " + object;
    }

    public Triple(String triple, boolean isTriple)
    {
        StringTokenizer st = new StringTokenizer(triple);
        String str[] = new String[4];
        int i = 0;
        while (st.hasMoreTokens())
        {
            str[i] = st.nextToken();
            i++;
        }

        if (i == 1)
        {
            this.subject = str[0];
        }
        else if (i == 3)
        {
            this.subject = str[0];
            this.predicate = str[1];
            this.object = str[2];
        }
    }
}
