package com.williamgilreath;

/**
 * <p>Title: Interface to Compiler</p>
 * <p>Description: ICompiler - Interface to JavaCC Boz Compiler methods</p>
 * <p>Copyright: Copyright (c) Sep 17, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.1
 */

public interface ICompiler
{
    public boolean compile();
    public String  getCode();
    public Name    getName();
    public String  getErrors();
    public boolean isUnitClass();

}//end interface
