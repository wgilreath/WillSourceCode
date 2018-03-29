package com.williamgilreath;

/**
 * <p>Title: Strict Ordered Vector Class      </p>
 * <p>Description: Vector of Elements Partially Ordered</p>
 * <p>Copyright: Copyright (c) December 11 2006    </p>
 *
 * @author William F. Gilreath will@williamgilreath.com
 * @version 1.0
 */

public final class OrderedVector extends Object implements Cloneable
{
    private final static int DELTA = 4; //amount to resize vector

    private Object[]   element;
    private int        count;

    public OrderedVector()
    {
        this.element = new Object[DELTA];
        this.count   = 0;
    }//end OrderedVector

    public void clear()
    {
        this.removeAllElements();
    }//end clear

    private synchronized void removeAllElements()
    {
        for (int i = 0; i < count; i++)
            element[i] = null;

        count = 0;

        //System.gc()??
    }//end removeAllElements

	protected final Object clone()
	{
	    try
	    {
	        OrderedVector clone = (OrderedVector)super.clone();

	        clone.element = (Object[]) this.element.clone();
	        return clone;
	    }
	    catch (CloneNotSupportedException ex)
	    {
	        // Impossible to get here.
	        throw new InternalError(ex.toString());
	    }
	}


    public final OrderedVector copy()
    {

        return (OrderedVector) this.clone();
        //return this; //shallow copy 12-14-2005
    }//end copy

    private final void ensureCapacity(final int capacity)
    {
        if (element.length >= capacity)
        {
            return;
        }

        int newCapacity   = element.length * 2; //multiple, 16,32,64,128
        Object[] newArray = new Object[Math.max(newCapacity, capacity)];

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

    public final Object get(final int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        }//end if

        //return (Object) element[index];
        return element[index];
    }//end get

    public final Object head() //minimal element
    {
        return this.get(0);
    }//end head

    public final Object tail() //maximum element
    {
        return this.get(count-1);
    }//end tail

   public final Object last(final int index)  //0= count-1, 1=count-2, 2=count-3
   {
       return this.get(count - 1 - index);
   }//end last

   private final void add(final int index, final Object obj) //Object=>Object
   {
        //System.out.println("# add(int,Object) = "+index+","+tok.toString());

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
        element[index] = obj;
    }//end add


public final void add(final Object value) //allow duplicates, like bag, but not specifically
{
    if(this.has(value)) return; //no duplicates

    int position = indexOf((Comparable)value);
    this.add(position,value);
}//end add

public final void append(final Object[] obj)
{
    for(int x=0;x<obj.length;x++)
    {
        if(! this.has(obj[x]))
        {
            this.add(obj[x]);
        }//end if
    }//end for

}//end append

public final void append(final OrderedVector vec)
{
    this.append(vec.toArray());
}//end append

public final boolean has(final Object value)
{
    if(this.count == 0) 
        return false;

    final int position = indexOf((Comparable)value);
    return (position < size()) && value.toString().equals(this.get(position).toString());
          
}

    public final Object[] toArray()
    {
        Object[] newArray = new Object[count];
        System.arraycopy(element, 0, newArray, 0, count);

        return newArray;
    }//end toArray


    public final Object remove(final int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        }//end if

        Object temp = (Object) element[index];

        count--;
        if (index < count)
        {
            System.arraycopy(element, index + 1, element, index,count - index);
        }//end if

        element[count] = null;
        return temp;
    }//end remove

    public final OrderedVector subrange(final Object first, final Object last)
    {
        OrderedVector result = null;

        //check if has(first) and has(last) else throw RuntimeException ??
        if(! this.has(first) || ! this.has(last) )
        {
            return null; //throw new RuntimeException("Subrange does not contain a specified element.");
                         //throw new OrderedSetException(" "); ??
        }//end if


        if( ((Comparable)first).compareTo(last) > 0) //first <= last
        {
            return this.subrange(last, first);
        }//end if

        final int firstIndex = this.indexOf((Comparable)first);
        final int lastIndex  = this.indexOf((Comparable) last);
        final int length     = lastIndex - firstIndex + 1;

        result = new OrderedVector();

        System.arraycopy(this.element, firstIndex, result.element, 0, length);
        result.count = length;

        return result;
    }//end subrange

    public final Object remove(final Object value)
    {
        Object target = null;
        if (has(value))
        {
            int position = indexOf((Comparable) value);
            target = this.get(position);
            this.remove(position);
        } //end if

        return target;
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

    public final int indexOf(final Comparable target)
    {
        Comparable midValue;

        int low  = 0;  // lowest possible location
        int high = this.count; // highest possible location

        if(this.count == 0) return 0;

        int mid  = (low + high)/2; // low <= mid <= high
        // mid == high iff low == high
        while (low < high)
        {
            // get median value
            midValue = (Comparable)  this.get(mid);
            // determine on which side median resides:
            if (midValue.compareTo(target) < 0)
            {
                low = mid+1;
            }
            else
            {
                high = mid;
            }
            // low <= high
            // recompute median index
            mid = (low+high)/2;
        }//end while

        return low;
    }//end indexOf

}//end class OrderedVector
