/*
    Title: Boz Compiler Nexus Class Version 0.99879425

    Author: William F. Gilreath    (will@williamgilreath.com)

    License:  This program is free software: you can redistribute it
    and/or modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.

    You    must accept the terms of the GNU General Public License
    (GPL) license agreement to use this software.

    Copyright (c) July 21 2010; All Rights Reserved.

 */

import com.williamgilreath.ICompiler;
import com.williamgilreath.HostCompiler;
import com.williamgilreath.Name;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.*;

public final class Boz
{
    private Boz(){}

    private final static void printHelp()
    {
        System.out.println();
        System.out.println();
        System.out.println("Syntax: java -jar Boz.jar ( [ option ] file.boz ) | ( -help | -? | -version ) ");
        System.out.println();
        System.out.println("where option includes:  ");
        System.out.println();
        System.out.println("    -compile  Compile source without build. ");
        System.out.println("    -help     Prints usage information.    ");
        System.out.println("    -version  Prints version information.  ");      
        System.out.println("    -?        Prints usage information.    ");
        System.out.println();
        System.out.println("Examples:");
        System.out.println();
        System.out.println("    java -jar Boz.jar -compile MyClass.boz ");
        System.out.println();
        System.out.println("    java -jar Boz.jar MyProg.boz           ");
        System.out.println();
        System.out.println("    java -jar Boz.jar -help                ");
        System.out.println();
        System.out.println();

    }//end printHelp

    public final static void printVersion()
    {
        System.out.println();

        System.out.print("Boz Compiler v");
        System.out.print(Boz.VERSION);
        System.out.print(" ");
        System.out.print(Boz.COPYRIGHT);
        System.out.print(" ");
        System.out.print(Boz.RELEASE);
        System.out.print(" Release ");
        System.out.print(Boz.PLATFORM);
        System.out.println();

        System.out.print("Copyright  (C)  William F. Gilreath.  ");
        System.out.print("All Rights Reserved.");
        System.out.println();
        System.out.println();

    }//end printVersion

    private static ICompiler comp = null;

    private final static boolean compileBoz(final String fileName)
    {
        try
        {
            Boz.comp = BozCompiler.initialize(new FileInputStream(fileName));
        }
        catch (FileNotFoundException fe)
        {
            reportError("File " + fileName + " not found.");
            return false;
        }//end try

        return comp.compile();

    }//end compileSource

    private final static String HOST_SUFFIX = ".java";

    private final static String RELEASE     = "Poirot";
    private final static String PLATFORM    = "JVM";
    private final static String VERSION     = "0.99879515-nb";
    private final static String COPYRIGHT   = "27 Nov 2010";

    private final static void exportHost(final Name name, final String code)
    {

        //final String fileName  = name.toString()+HOST_SUFFIX;
        final String fileName  = name.suffix()+HOST_SUFFIX;

        //System.out.println("Prefix = "+name.substring(name.lastIndexOf(".")+1,name.length()));
        //System.out.println("Suffix = "+name.substring(0, name.lastIndexOf(".")));

        ///System.out.println("Prefix = "+name.prefix());
        ///System.out.println("Suffix = "+name.suffix());

        //System.out.println("#exportHost name = "+fileName.toString());
        File pckgFile = null;
        String pckgPath = "";
        if(name.prefix().length() > 0)
        {
            pckgFile = Boz.hasMakeDir(Boz.DIR_OUT_SOURCE, name.prefix().replace(".", File.separator));
            pckgPath = pckgFile.getAbsolutePath();
        }
        else
        {
            pckgPath = Boz.DIR_OUT_SOURCE;
        }//end if

        //System.out.println("#exportHost Package path = "+pckgPath);

        FileWriter   fileWrite = null;

        //String       srcFilePath = pckgPath+File.separator+fileName;
        //System.out.println("#exportHost source file path = "+srcFilePath);

        try
        {
            Boz.pathSrcOut = new File(pckgPath+File.separator+fileName);
            fileWrite = new FileWriter(Boz.pathSrcOut);
            //fileWrite = new FileWriter(pckgPath+File.separator+fileName);

            //fileWrite = new FileWriter(Boz.dirRaw+File.separator+fileName);
            //fileWrite = new FileWriter(fileName);

            fileWrite.write(code);
            fileWrite.flush();
            fileWrite.close();

        }
        catch(IOException io)
        {
            reportError(io.getMessage());
        }//end try

        //post-check file exists ??

    }//end exportHost

    private final static void compileHost()
    {
        HostCompiler host = new HostCompiler();

        //final Name   hostName = Boz.comp.getName();
        //final String hostCode = Boz.comp.getCode();

        //compile creates directory from name if multiple identifiers in hostName
        //    set output directory of host source  to created directory

        //export host code to external file -- raw host source
        //exportHost(hostName, hostCode);
        exportHost(Boz.comp.getName(), Boz.comp.getCode());

        //String outPath = Boz.DIR_OUT_BINARY + File.separator + hostName.prefix().replace(".", File.separator);

        //System.out.println("compileHost() compile output path = "+outPath);
        //File path = Boz.hasMakeDir(Boz.DIR_OUT_BINARY, hostName.prefix().replace(".", File.separator));
        //File path = Boz.dirOut;
        //System.out.println("compile out path = "+path.toString());

        //long start = System.currentTimeMillis();

        //final boolean successFlag = host.compileJapic(Boz.comp, Boz.pathDirOut); //Java API Compiler

        final boolean successFlag = host.compileJavac(Boz.pathDirOut, Boz.pathSrcOut);


        //System.out.println("[host total "+(System.currentTimeMillis() - start)+" ms]");
        //System.out.printf("[host total %-4d ms]\n\r", (System.currentTimeMillis() - start) );

        //if(HostCompiler.compile(hostName, hostCode, Boz.dirOut))
        if(successFlag)
        {
            System.out.println();
            System.out.println("Compilation Success!");
            System.out.println();
        }
        else
        {
            reportError(host.getDiagnostics());
        }//end if

    }//end compileHost

    //private static boolean checkFlag   = false; //check classpath for org.bozlang.RT
    private static boolean helpFlag    = false;
    private static boolean compileFlag = false;  //default is to compile host source
    private static boolean noFilesFlag = false;  //have file specified on command-line
    private static boolean versionFlag = false;
    
    private static String  fileName    = null;

    private final static void processArgs(final String[] args)
    {
        noFilesFlag = true; //presume no files specified for error reporting
        
        for(int index=0;index<args.length;index++)
        {
            final String opt = args[index];

            if(opt.equals("-compile"))
            {
                compileFlag = true; //compile-only, no build of host source code
            }
            else
            if(opt.equals("-help") || opt.equals("-?"))
            {
                helpFlag = true;
            }
            else
            if(opt.equals("-version"))
            {
                versionFlag = true;
            }
            else
            if(opt.substring(0,1).equals("-"))
            {
                reportError("Unknown option: "+opt);
                System.exit(1);
            }
            else
            {
                fileName    = opt;
                noFilesFlag = false;
            }//end if

        }//end while
        
    }//end processArgs

    public final static void reportError(final String error)
    {
        System.err.println();
        System.err.println("Error: "+error);
        System.err.println();
    }//end reportError

    //compiler constants - output file directories
    public final static String DIR_OUT_BINARY = "out";
    public final static String DIR_OUT_SOURCE = "raw";
    public final static int    DIR_MAKE_LIMIT = 100;

    public final static void initDirs()
    {
        ///System.out.println("#initializeDir()");

        final String homeDir = System.getProperty("user.dir"); //boz.home??

        ///System.out.println("#homeDir = "+homeDir);

        Boz.pathDirOut = hasMakeDir(homeDir,DIR_OUT_BINARY);
        ///System.out.println("#dirOut = "+dirOut.toString());
        Boz.pathDirRaw = hasMakeDir(homeDir,DIR_OUT_SOURCE);
        ///System.out.println("#dirRaw = "+dirRaw.toString());

    }//end initDirs

    private static File pathDirOut = null;
    private static File pathDirRaw = null;
    private static File pathSrcOut = null;

    private final static File hasMakeDir(final String homeDir, final String subDir)
    {
        final String fileName  = homeDir+File.separator+subDir;
        final File   directory = new File(fileName);

        if(directory.exists())
        {
             return directory;
        }
        else
        {
            for(int x=0;x<DIR_MAKE_LIMIT;x++)
            {
                directory.mkdirs();
                //wait so many milliseconds for directory creation by OS??
                if(directory.exists()) return directory;
                else continue;

            }//end for

        }//end if

        //throw new CompilerTrap("Unable to create compiler directory path: "+ fileName);
        reportError("Initialize Directory; Unable to create compiler directory path: "+ fileName);

        return directory;

    }//end existCreateDir

    private final static void printErrors()
    {
        if(Boz.comp != null) 
            reportError(Boz.comp.getErrors());
        
    }//end printErrors

    public final static void main(final String[] args)
    {
        if(args.length == 0)
        {
            Boz.reportError("No file or options specified. Use -? or -help for help.");
        }
        else
        {
            Boz.processArgs(args);

            if(Boz.helpFlag)
            {
                Boz.printHelp();
            }
            else
            if(Boz.versionFlag)
            {
                Boz.printVersion();
            }
            else
            {     
                if(Boz.noFilesFlag)
                {
                    reportError("No input files");
                    System.exit(1);
                }//end if
                
                //if(Boz.fileName != null && Boz.compileBoz(Boz.fileName))
                if(Boz.compileBoz(Boz.fileName))
                {
                    Boz.printVersion();
                    
                    if(Boz.compileFlag)
                    {
                        System.out.println();
                        System.out.println("Compile without Build Success!");
                        System.out.println();
                        System.exit(0);
                    }
                    else
                    {                    
                        Boz.initDirs();
                        Boz.compileHost();
                    }//end if
                }
                else
                {
                    Boz.printErrors();
                    System.exit(1);
                }//end if

            }//end if

        }//end if

        System.exit(0);

    }//end main

}//end class Boz
