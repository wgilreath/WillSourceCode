/**
 * <p>Title: Boolean context data structure for Boz compiler.</p>
 * <p>Description: Boolean true of false within context of block.</p>
 * <p>Copyright: Copyright (c) July 06, 2009 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.1
 *
 */

package com.williamgilreath;

public final class BoolContext extends Object implements Cloneable
{
    private final static int DELTA = 4;

    private Boolean[] element;
    private int       count;

    public BoolContext()
    {
        this.element = new Boolean[DELTA];  //modify so use named constant ?? 8/15/2005??
        this.count   = 0;
    }//end BoolContext

    //boolean variable specific at top of context
    private final void False()
    {
        this.add(0, Boolean.FALSE);
    }//end newFalse

    private final void True()
    {
        this.add(0, Boolean.TRUE);
    }//end newTrue

    public final void setFalse()
    {
        if(this.count > 0) this.remove(0);
        this.add(0, Boolean.FALSE);

        //System.out.println("setFalse() "+this.toString());
    }//end setFalse

    public final void setTrue()
    {
        if(this.count > 0) this.remove(0);
        this.add(0, Boolean.TRUE);
        //System.out.println("setTrue() "+this.toString());

    }//end setTrue

    public final boolean flag() //variable of context boolean
    {
        //System.out.println("flag() "+this.toString());

        return this.get(0).booleanValue();
    }//end flag

    public final void start() 
    {
        this.False();
        //System.out.println("start() "+this.toString());

    }//end start

    public final void close() 
    {
        this.remove(0);
        //System.out.println("close() "+this.toString());

    }//end close

    public void clear()
    {
        this.removeAllElements();
    }//end clear

    private synchronized void removeAllElements()
    {
        for (int i = 0; i < count; i++)
        {
            element[i] = null;
        }//end for

        count = 0;
    }//end removeAllElements

	protected final Object clone()
	{
	    try
	    {
	        BoolContext clone = (BoolContext)super.clone();

	        clone.element = (Boolean[]) this.element.clone();
	        return clone;
	    }
	    catch (CloneNotSupportedException ex)
	    {
	        // Impossible to get here.
	        throw new InternalError(ex.toString());
	    }//end try

	}//end clone

    private final BoolContext copy()
    {
        return (BoolContext) this.clone();
    }//end copy

    private final void ensureCapacity(final int capacity)
    {
        if (element.length >= capacity)
        {
            return;
        }//end if

        int  newCapacity   = element.length + DELTA;
        Boolean[] newArray = new Boolean[Math.max(newCapacity, capacity)];

        System.arraycopy(element, 0, newArray, 0, count);
        element = newArray;
    }//end ensureCapacity

    public final int size()
    {
        return count;
    }//end size

    public final boolean isEmpty()
    {
        return (count == 0);
    }//end isEmpty

    private final Boolean get(final int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        }//end if

        //return (Object) element[index];
        return element[index];
    }//end get

    private final void add(final int index, final Boolean var)
    {
        if (index > count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " > " + count);
        }//end if

        if (count == element.length)
        {
            ensureCapacity(count + 1);
        }//end if

        System.arraycopy(element, index, element, index + 1, count - index);
        count++;
        element[index] = var;
    }//end add

    private final void add(final Boolean var)
    {
        if (count == element.length)
        {
            ensureCapacity(count + 1);
        }//end if

        element[count++] = var;
    }//end add

    private final Boolean[] toArray()
    {
        Boolean[] newArray = new Boolean[count];
        System.arraycopy(element, 0, newArray, 0, count);

        return newArray;
    }//end toArray

    private final Boolean remove(final int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        }//end if

        Boolean temp = (Boolean) element[index];

        count--;
        if (index < count)
        {
            System.arraycopy(element, index + 1, element, index,count - index);
        }//end if

        element[count] = null;
        return temp;
    }//end remove

    public final String toString()
    {
        StringBuffer str = new StringBuffer();

        str.append("[ ");
        for(int x=0;x<element.length;x++)
        {
            if(element[x] != null)
            {
                str.append(element[x].toString());
                str.append(" ");
            }//end if

        }//end for

        str.append("]");
        return str.toString();

    }//end toString

}//end class BoolContext
