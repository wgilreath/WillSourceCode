package com.williamgilreath;
/*
 * @(#)JavaSourceFromString	1.01   2008-09-14
 *
 * Title: JavaSourceFromString - Javac Source Object.
 *
 * Description: Javac Source object to compile code
 *     using javax.tools in string programmatically.
 *
 * Author: William F. Gilreath (will@williamgilreath.com)
 *
 * Copyright (C) September 14 2008; All Rights Reserved.
 *
 * License: This software is subject to the terms of the
 * GNU General Public License  (GPL)  available  at  the
 * following link: http://www.gnu.org/copyleft/gpl.html.
 *
 * You	must accept the terms of the GNU General  Public
 * License license agreement to	use this software.
 *
 */

import javax.tools.*;
import java.net.*;

public final class JavaSourceFromString extends SimpleJavaFileObject
{
    private final String code;
    private final String name;

    public JavaSourceFromString(final String name, final String code)
    {   
        super(URI.create("string:///" 
                        + name.replace('.','/') 
                        + Kind.SOURCE.extension)
                        , Kind.SOURCE);
        
        this.name = name;
        this.code = code;
    }//end constructor

    @Override
    public final CharSequence getCharContent(final boolean ignoreEncodingErrors)
    {
      return this.code;
    }//end getCharContent

}//end class JavaSourceFromString
