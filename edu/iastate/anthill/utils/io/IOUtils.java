/*
                        Utility classes.

    Copyright (C) 2002  Jose San Leandro Armendáriz
                        jsanleandro@yahoo.es
                        chousz@yahoo.com

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


    Thanks to ACM S.L. for distributing this library under the LGPL license.
    Contact info: jsr000@terra.es
    Postal Address: c/Playa de Lagoa, 1
                    Urb. Valdecabañas
                    Boadilla del monte
                    28660 Madrid
                    Spain

    This library uses an external API to retrieve version information at
    runtime.
    So far I haven't released such API as a project itself, but you should be
    able to download it from the web page where you got this source code.

 ******************************************************************************
 *
 * Filename: $RCSfile: IOUtils.java,v $
 *
 * Author: Jose San Leandro Armendáriz
 *
 * Description: Provides some commonly used static methods related to
 *              java.io classes.
 *
 * File version: $Revision: 1.1 $
 *
 * Project version: $Name:  $
 *                  ("Name" means no concrete version has been checked out)
 *
 * $Id: IOUtils.java,v 1.1 2004/07/11 01:29:01 baojie Exp $
 *
 */
package edu.iastate.anthill.utils.io;

/*
 * Importing some JDK1.3 classes.
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuffer;
import edu.iastate.anthill.utils.Utility;

/**
 * Provides some commonly used static methods related to java.io classes.
 * @author <a href="mailto:jsanleandro@yahoo.es"
           >Jose San Leandro Armendáriz</a>
 * @version $Revision: 1.1 $
 */
public abstract class IOUtils
    //    implements  Utils
{
    /**
     * Private constructor to avoid accidental instantiation.
     */
    private IOUtils()   {};

    /**
     * Block size used when reading the input stream.
     */
    private static final int BLOCK_SIZE = 1024;

    /**
     * Reads given input stream into a String.
     * @param inputStream the input stream to read.
     * @param contentLength the length of the content.
     * @return the input stream contents, or an empty string if the operation
     * fails.
     */
    public static String read(InputStream inputStream, int contentLength)
    {
        StringBuffer t_sbResult = new StringBuffer();

        if  (inputStream != null)
        {
            if  (contentLength > 0)
            {
                try
                {
                    InputStreamReader t_isrReader =
                        new InputStreamReader(inputStream);

                    char[] t_acContents = new char[contentLength];

                    if  (t_isrReader.ready())
                    {
                        t_isrReader.read(t_acContents);
                    }

                    t_sbResult.append(t_acContents);
                }
                catch   (IOException ioException)
                {
                    /*
                     * Exception management is missing.
                     */
                }
            }
            else
            {
                try
                {
                    InputStreamReader t_isrReader =
                        new InputStreamReader(inputStream);

                    char[] t_acContents = new char[BLOCK_SIZE];

                    while(t_isrReader.ready())
                    {
                        int t_iCharsRead = t_isrReader.read(t_acContents);

                        char[] t_acCharsRead =
                            (t_iCharsRead == BLOCK_SIZE)
                            ?   t_acContents
                            :   Utility.subBuffer(
                                    t_acContents, 0, t_iCharsRead);

                        t_sbResult.append(t_acCharsRead);
                    }
                }
                catch   (IOException ioException)
                {
                    /*
                     * Exception management is missing.
                     */
                }
            }
        }

        return t_sbResult.toString();
    }

    /**
     * Reads an input stream and returns its contents.
     * @param input the input stream to be read.
     * @return the contents of the stream.
     * @throws IOException whenever the operation cannot be accomplished.
     */
    public static String read(InputStream input)
        throws  IOException
    {
        StringBuffer t_sbResult = new StringBuffer();

        if  (input != null)
        {
            /*
             * Instantiating an InputStreamReader object to read the contents.
             */
            InputStreamReader t_isrReader = new InputStreamReader(input);

            /*
             * It's faster to use BufferedReader class.
             */
            BufferedReader t_brBufferedReader =
                new BufferedReader(t_isrReader);

            String t_strLine = t_brBufferedReader.readLine();

            while  (t_strLine != null)
            {
                t_sbResult.append(t_strLine + "\n");

                t_strLine = t_brBufferedReader.readLine();
            }
        }

        /*
         * End of the method.
         */
        return t_sbResult.toString();
    }

    /**
     * Reads the contents of an input stream and returns its contents, if
     * possible. If some exception occurs, returns an empty String.
     * @param input the input stream to be read.
     * @return the contents of the stream, or empty if reading cannot be
               accomplished.
     */
    public static String readIfPossible(InputStream input)
    {
        String result = new String();

        try
        {
            result = read(input);
        }
        catch	(IOException ioException)
        {
            /*
             * We have chosen not to notify of exceptions, so this
             * block of code is only descriptive.
             */
        }

        return result;
    }

}
