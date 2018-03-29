/**
 * <p>Title: Code Class - String Class for Code Synthesis</p>
 * <p>Description: Class with prefix, suffix, concat string operations.</p>
 * <p>Copyright: Copyright (c) May 13, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.1
 */

package com.williamgilreath;

public final class Code 
{
    private final static String EOL  = System.getProperty("line.separator");
    private final static String SPC  = new String(" ");
    private final StringBuffer  code = new StringBuffer();
    
    public Code()
    {
        this("");
    }//end constructor
    
    public Code(final String str)
    {
        code.append(str);
    }//end constructor
    
    public final void cat(int num, String str)
    {
    	this.spc(num);
    	this.cat(str);
    }//end cat
    
    public final void cat(final String str)
    {
        code.append(str);
        //code.append(" ");
        code.append(EOL);
    }//end cat
    
    public final void eol()
    { 
    	code.append(EOL);  
    }//end eol

    public final void eol(int num)
    {
    	for(int x=0;x<num;x++)
    	{
    	    code.append(EOL);
     	}//end for
     	
    }//end eol
    
    public final void spc(int num)
    {
    	for(int x=0;x<num;x++)
    	{
    	    code.append(SPC);
     	}//end for
    	
    }//end spc
    
    public final void add(final String str)
    {
        code.append(str);
    }//end add

    public final void clear()
    {
        code.delete(0, code.length());
    }//end clear
    
    public final void concat(final String lstr, final String rstr)
    {
        this.prefix(lstr);
        this.suffix(rstr);
    }//end concat
    
    public final int length()
    {
        return this.code.length();
    }//end length

    public final void prefix(final String str)
    {
        code.insert(0, str);
    }//end prefix
    
    public final void suffix(final String str)
    {
        code.insert(code.length(), str);
    }//end suffix
        
    @Override
    public final String toString()
    {
        return this.code.toString();
    }//end toString
        
}//end class Code
