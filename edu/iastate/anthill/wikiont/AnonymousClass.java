package edu.iastate.anthill.wikiont;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnonymousClass
{
    int number;
    String body;

    public AnonymousClass(String body, int number)
    {
        this.body = body;
        this.number = number;
    }

    public String toString()
    {
        return body + number + "|" + number;
    }

}
