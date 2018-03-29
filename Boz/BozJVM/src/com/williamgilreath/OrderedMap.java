package com.williamgilreath;

/**
 * <p>Title: Strict Ordered Map Class                   </p>
 * <p>Description: Map of Elements Ordered Key to Value </p>
 * <p>Copyright: Copyright (c) February 1 2008          </p>
 *
 * @author William F. Gilreath will@williamgilreath.com
 * @version 1.15
 *
 * Updated May 27 2009; added method val(int) : Object
 * Updated May 29 2009; added method ensureCapacity() : void
 *
 */

public final class OrderedMap extends Object implements Cloneable
{
    private final static int DELTA = 16; //amount to resize vector

    private Comparable[] keys;   //keys    image    
    private Object[]     vals; //vals  range
    private int          count;

    public OrderedMap()
    {
        this.keys  = new Comparable[DELTA];
        this.vals  = new Object[DELTA];
        this.count = 0;
        
    }//end OrderedMap

    private void clear()
    {
        for (int i = 0; i < count; i++)
        {
            keys[i] = null;
        }//end for
        
        count = 0;

        //System.gc()??
    }//end clear

	protected final Object clone()
	{
	    try
	    {
	        OrderedMap clone = (OrderedMap) super.clone();

	        clone.keys   = (Comparable[]) this.keys.clone();
	        clone.vals = this.vals.clone();
	        
	        return clone;
	    }
	    catch (CloneNotSupportedException ex)
	    {
	        throw new InternalError(ex.toString());
	    }//end try

	}//end clone

    private final int fnvhash(final String str)
    {
        int    seed = 0x811c9dc5;
        byte[] buf  = str.getBytes();

        for (int i = 0; i < str.length(); i++)
        {
          seed += (seed << 1) + (seed << 4) + (seed << 7) + (seed << 8) + (seed << 24);
          seed ^= buf[i];
        }//end for

        return seed;

    }//end fnvhash

    public final int hashCode()
    {
    	return this.fnvhash(this.toString());
    
    }//end hashCode

    public final boolean equals(Object obj)
    {
    	if(obj instanceof OrderedMap)
    	{
    		return this.hashCode() == obj.hashCode();
    	}
    	else
    	{
    		return false;
    	}//end if
    	
    }//end equals

    public final OrderedMap copy()
    {
        return (OrderedMap) this.clone();
    }//end copy

    /*
    private final void ensureCapacity(final int capacity)
    {
        if (keys.length >= capacity)
        {
            return;
        }//end if

        final int newCapacity   = keys.length * 2; //multiple, 16,32,64,128
        
        Comparable[] newArray = new Comparable[Math.max(newCapacity, capacity)];

        System.arraycopy(keys, 0, newArray, 0, count);
        keys = newArray;

        newArray = new Comparable[Math.max(newCapacity, capacity)];

        System.arraycopy(vals, 0, newArray, 0, count);
        vals = newArray;
        
    }//end ensureCapacity
    //*/

    private final void ensureCapacity()
    {
        //if(keys.length > count) return;

        final int newCapacity   = keys.length + DELTA;

        Comparable[] newArray = new Comparable[newCapacity];

        System.arraycopy(keys, 0, newArray, 0, count);
        keys = newArray;

        newArray = new Comparable[newCapacity];

        System.arraycopy(vals, 0, newArray, 0, count);
        vals = newArray;

    }//end ensureCapacity

    public final int size()
    {
        return count;
    }//end size

    public final boolean isEmpty()
    {
        return (count == 0);
    }//end isEmpty

    
    public final Object get(final Comparable key)
    {
        if(this.has(key))
        {
            return this.get(this.indexOf(key));
        }//end if

        return null;
        
    }//end get

    public final Object get(final int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        }//end if

        return keys[index];

    }//end get

    public final Object val(final int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        }//end if

        return vals[index];
    	
    }//end val

    public final Object val(final Comparable key)
    {
    	return this.val(this.indexOf(key));
    }//end val

    public final Object head() //minimal keys
    {
        return this.get(0);
    }//end head

    public final Object tail() //maximum keys
    {
        return this.get(count-1);
    }//end tail

   public final Object last(final int index)  //0= count-1, 1=count-2, 2=count-3
   {
       return this.get(count - 1 - index);
   }//end last

   private final void add(final int index, final Comparable key, final Object obj) //Comparable => Object
   {
        //System.out.println("# add(int,Object) = "+index+","+tok.toString());

        if (index > count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " > " + count);
        }//end if

        if (count == keys.length)
        {
            //ensureCapacity(count + 1);
            ensureCapacity();
        }//end if

        System.arraycopy(keys, index, keys, index + 1, count - index);
        System.arraycopy(vals, index, vals, index + 1, count - index);
        count++;
        
        keys[index] = key;
        vals[index] = obj;
        
    }//end add

    public final void add(final Comparable key, final Object value)
    {
        int position = indexOf(key);
        this.add(position, key, value);
        
    }//end add

    public final boolean has(final Comparable value)
    {
        final int position = indexOf((Comparable)value);
        
        return (position < this.size()) && this.get(position).equals(value);
        
    }//end has

/*
    //create 2-dim array of Object[0][0] = key, val ??
 
    //toArrayKey() : Object[]
    //toArrayVal() : Object[]

    public final Object[] toArray()
    {
        Object[] newArray = new Object[count];
        System.arraycopy(keys, 0, newArray, 0, count);

        return newArray;
        
    }//end toArray
*/
    public final Object remove(final int index)
    {
    	//System.out.println("remove index = "+index);
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        }//end if

        Comparable tempKey   = keys[index];
        Object     tempValue = vals[index];
        
        count--;
        
        if (index < count)
        {
            System.arraycopy(keys, index + 1, keys, index, count - index);
            System.arraycopy(vals, index + 1, vals, index, count - index);
        }//end if

        keys[count] = null;
        vals[count] = null;
        
        return tempValue;
        
    }//end remove

    public final Object remove(final Comparable key)
    {
        Object obj = null;
        int    pos = -1;

        if (has(key))
        {
            pos = indexOf(key);
            obj = remove(pos);
        } //end if

        return obj;
        
    }//end remove

    public final String toString()
    {
        StringBuffer str = new StringBuffer();

        str.append("[ ");
        for(int x=0;x<keys.length;x++)
        {
            if(keys[x] != null)
            {
            	str.append("< ");
                str.append(keys[x].toString());
                str.append(" => ");
                str.append(vals[x].toString());
                str.append("> ");
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

    public final static void main(final String[] args)
    {
        OrderedMap map = new OrderedMap();
        
        System.out.println(map.toString());

        map.add("one","1");
        
        System.out.println(map.toString());


        map.add("two","2");
        
        System.out.println(map.toString());

        map.add("three","3");
        
        System.out.println(map.toString());

        map.add("four","4");
        
        System.out.println(map.toString());

        map.add("five","5");
        
        System.out.println(map.toString());

        map.add("six","6");
        
        System.out.println(map.toString());

        map.add("seven","7");
        
        System.out.println(map.toString());

        map.add("eight","8");
        
        System.out.println(map.toString());

        map.add("nine","9");
        
        System.out.println(map.toString());

        map.add("ten","10");
        
        System.out.println(map.toString());

        map.add("eleven","11");
        
        System.out.println(map.toString());

        for(int x=0;x<15;x++)
        {
            map.add(Integer.toString(x), new Integer(x));
            System.out.println(map.toString());

        }//end for


        System.out.println("has "+map.has("one"));

        System.out.println("has "+map.has("zero"));


        //System.out.println(map.remove("one"));

        System.out.println("head = "+map.head());
        System.out.println("tail = "+map.tail());

        OrderedMap om = (OrderedMap) map.clone();

        System.out.println("Origin map = "+map.toString());
        System.out.println("Cloned map = "+om.toString());
        
        System.out.println("Equals : "+map.equals(om));
        System.out.println("Equals : "+om.equals(map));

        om.add("zero","0");

        System.out.println("Origin map = "+map.toString());
        System.out.println("Cloned map = "+om.toString());

        System.out.println("Equals : "+map.equals(om));
        System.out.println("Equals : "+om.equals(map));
        
        for(int x=0;x<map.size();x++)
        {
        	System.out.println(x+" value = "+map.val(x));
        	System.out.println(x+" key   = "+map.get(x));
        }//end for
        System.out.println();
        
        System.exit(0);

    }//end main

}//end class OrderedMap
