package com.williamgilreath.xml;

import java.lang.*;
import java.io.*;
import java.util.Vector;

/**
 * <p>XML Scanner</p>
 * <p>Description: XML Tokenizer test program<p>
 * @author William F. Gilreath
 * @version 1.0
 *
 * Copyright (C) 2002 William F. Gilreath (will@williamgilreath.com)
 *
 * This file is part of XML Tokenizer software project. XML Tokenizer is
 * free software; you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */

public final class XMLTokCounter
{
    public static void main(String[] args)
    {
        String defaultFilename = "test.xml";

        String filename = null;

        long tokenCounter = 0;

        if(args.length != 1)
        {
            filename = defaultFilename;
        }
        else
        {
            filename = args[0];
        }//end if
        
        FileInputStream  file   = null;
        FileOutputStream err    = null;

        try
        {
            file   = new FileInputStream(filename);
            err    = new FileOutputStream("err");

            XMLTokenizer xs = new XMLTokenizer(file, err);
  
            System.out.println("Start of program");
            System.out.println();

            xs.setVerbose(true);
            xs.setThrowsException(true);

            XMLToken xt = null;

            long start = System.currentTimeMillis();

            while(xs.hasNextToken())
            {
                xt = xs.getNextToken();
                System.out.println("Token: "+tokenCounter+" "+xt.asTuple()); System.out.println();
                tokenCounter++;
            }//end while

            long stop = System.currentTimeMillis();

            System.out.println();
            System.out.println("Total time = "+(stop - start)+" milliseconds.");

            double timeToken = (double)tokenCounter / (stop-start);

            System.out.println("Time per token = "+timeToken+" milliseconds");
            file.close();
            err.close();

        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.err.println("token count = "+tokenCounter);
            System.exit(1);
        }//end try
        
        System.out.println("Scanned "+tokenCounter+" XML tokens.");
        System.out.println();
        System.out.println("End of program");

        System.exit(0);
        
    }//end main

}