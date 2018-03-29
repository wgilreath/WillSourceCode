package com.williamgilreath;
/*
 * @(#)HostCompiler.java	1.20   2008-10-05
 *
 * Title: HostCompiler - Compiler Interface.
 *
 * Description: Host compiler (Javac) interface to compiler Java
 * source in string programmatically.
 *
 * Author: William F. Gilreath (will@williamgilreath.com)
 *
 * Copyright (C) October 05 2008; All Rights Reserved.
 *
 * License:  This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * You	must accept the terms of the GNU General Public License
 * (GPL) license agreement to use this software.
 *
 */

import java.lang.reflect.*;
import java.io.*;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.util.*;
import com.sun.tools.javac.main.Main;

public final class HostCompiler
{
    //create class with static CompilerConstant??
    private final static String EOLN   = System.getProperty("line.separator");
    private final static Locale LOCALE = Locale.ENGLISH;

    private final StringBuffer errors = new StringBuffer();

    public HostCompiler()
    {
    }//end constructor

    public final boolean compileJavac(final File pathOutput, final File pathSource)
    {
        final StringWriter sw = new StringWriter();
        //final PrintWriter out = new PrintWriter(sw);

        final Main javac = new Main("javac", new PrintWriter(sw)); //pass PrintWriter for diagnostics

        //create command-line arguments using pathOutput, pathSource
        final String[] args = new String[]{
                                            "-cp", pathOutput.toString(),
                                            "-d",  pathOutput.toString(),
                                            pathSource.toString()
                                          };


        final int status = javac.compile(args); //pass created args

        //errors = sw.toString();
        errors.append(sw.toString());

        //System.out.println("Errors: "+errors);


        return 0 == status;

    }//end compileJavac

    //#compile creates directory from name if multiple identifiers
    //#    set output directory of host compiler  to created directory
    public final boolean compileJapic(final ICompiler icomp, final File pathOutput) //#String[] args -- passed host options
    {
        final Name   name = icomp.getName();
        final String code = icomp.getCode();

        final JavaCompiler comp = ToolProvider.getSystemJavaCompiler();

        //System.out.printf("HostCompiler.compile() Output Path = %s \n\r", path.toString());

        if(comp == null)
        {
            errors.append("Internal Error: Host Compiler is not available.");
            return false;
        }//end if

        //#option to pass host classpath to compiler, or automatically check build directory for compiled classes ??
        final Iterable<String> opts = null; //Arrays.asList(new String[]{ "-verbose" });

        final DiagnosticCollector<JavaFileObject> diag = new DiagnosticCollector<JavaFileObject>();

        final JavaFileObject  file = new JavaSourceFromString(name.toString(), code);

        final Iterable<? extends JavaFileObject> unit = Arrays.asList(file);

        //final JavaFileManager manager = comp.getStandardFileManager(diag, locale, charset);

        final StandardJavaFileManager manager = comp.getStandardFileManager(diag, null, null);

        if(pathOutput != null)
        {
            try
            {
                manager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(pathOutput));

                manager.setLocation(StandardLocation.CLASS_PATH, Collections.singleton(pathOutput));;

            }
            catch(Exception ex)
            {
                //throw new RuntimeException("Host Compiler Error: "+ex.getMessage());
                errors.append("Host Compiler Error: "+ex.getMessage());
            }//end try

        }//end if

        final CompilationTask task = comp.getTask(null, manager, diag, opts, null, unit);

        if(!task.call())
        {
            setDiagnostics(diag, LOCALE);
            return false;
        }//end if

        return true;

    }//end compile

    public final String getDiagnostics()
    {
        //return errors;  //StringBuffer use append, return errors.toString() ??
        return errors.toString();
    }//end getDiagnostics

    private final void setDiagnostics(final DiagnosticCollector<JavaFileObject> diag, final Locale locale)
    {
        final StringBuffer str = new StringBuffer();

        for( Diagnostic diagnostic : diag.getDiagnostics() )
        {
            str.append("Host Compiler Error!");
            /*

            str.append(" Code: "    +diagnostic.getCode()         +";");
            str.append(" Kind: "    +diagnostic.getKind()         +";");
            str.append(" Position: "+diagnostic.getPosition()     +";");
            str.append(" Start: "   +diagnostic.getStartPosition()+";");
            str.append(" Close: "   +diagnostic.getEndPosition()  +";");
            str.append(" Column: "  +diagnostic.getColumnNumber() +";");
            str.append(" Source: "  +diagnostic.getSource()       +";");
            str.append(" Message: " +diagnostic.getMessage(locale)+".");
            //*/
            str.append(" "+diagnostic.toString()+".");
            str.append(EOLN);

        }//end for

        //errors = str.toString();
        errors.append(str.toString());

    }//end setDiagnostics

    /*
    public static void main(String args[]) throws IOException
    {
        final String name = "com.williamgilreath.HelloWorld";

        final String code = "package com.williamgilreath;                " +
                            "                                            " +
                            "public class HelloWorld {                   " +
                            "    public static void main(String args[]) {" +
                            "    System.out.println(\"Hello, World\");   " +
                            "    System.exit(0;                         " +
                            "  }                                         " +
                            "}                                           " +
                            "                                            " ;

        HostCompiler.clear();

        if(HostCompiler.compile(name, code))
        {
            System.out.println("Compilation Success");
        }
        else
        {
            System.out.println(HostCompiler.getDiagnostics());
            System.exit(1);
        }//end if

        System.exit(0);

    }//end main
    //*/

}//end class HostCompiler
