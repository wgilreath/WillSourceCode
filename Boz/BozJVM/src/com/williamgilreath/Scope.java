package com.williamgilreath;

import java.util.Vector;

/**
 * <p>Title: Scope Class - Vector of OrderedVectors     </p>
 * <p>Description: Vector of Elements and Scope Functionality</p>
 * <p>Copyright: Copyright (c) September 03 2008    </p>
 *
 * @author William F. Gilreath will@williamgilreath.com
 * @version 1.1
 * 
 * Added 11-15-2008 drop method.
 * 
 */

public final class Scope
{
    private final static Scope scope = new Scope();
    
    private final Vector          stack = new Vector();
    private       OrderedVector   top   = null;
    
    private Scope()
    {
    }//end Scope

    public final String toString()
    {
        return stack.toString();
    }//end toString

    public final static Scope getScope()
    {
        return Scope.scope;
    }//end getScope

    public final int getDepth()
    {
        return stack.size();       
    }//end getDepth

    public final void begin()
    {
        //System.out.println(">>> Scope.begin depth = "+this.getDepth()+" <<<");
        top = new OrderedVector();
        stack.add(0, top);
        
        //System.out.println(">>> Scope.begin depth = "+this.getDepth()+" <<<");
        //System.out.println();
    }//end begin

    public final void close()
    {
        //System.out.println(">>> Scope.close depth = "+this.getDepth()+" <<<");
        stack.remove(0);
        if(stack.size() > 0)
            top = (OrderedVector) stack.get(0);
        else
            top = null;
        //System.out.println(">>> Scope.close depth = "+this.getDepth()+" <<<");
        //System.out.println();

    }//end close

    public final void add(final String id)
    {
        top.add(id);
    }//end add
    
    //drop variable identifier in current scope context
    public final void drop(final String id)
    {
        if(top.has(id))
        {
            top.remove(id);    
        }
        else
        {   
            throw new java.util.NoSuchElementException("Identifier not found in current scope to remove: "+id);
        }//end if
        
    }//end drop
    
    public final boolean has(final String id)
    {
        OrderedVector idx = null;
        for(int x=0;x<stack.size();x++)
        {
            idx = (OrderedVector) stack.get(x);
            if(idx.has(id)) return true;
        }//end for
        
        return false;
    }//end has

}//end class Scope
