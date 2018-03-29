package com.williamgilreath;

/**
 * <p>Title: Associative Array Class - associate key to value</p>
 * <p>Description: Associative Array - use ordered array to create mapping</p>
 * <p>Copyright: Copyright (c) May 08, 2008 </p>
 *
 * @author William F. Gilreath (will@williamgilreath.com)
 * @version 1.0
 */

public final class AssociativeArray
{
    private final static int SIZE = 4; //resize factor

    private Comparable key[] = null;
    private Object     val[] = null;
    private int        count = -1;

    public AssociativeArray()
    {
        this.key   = new Comparable[SIZE];
        this.val   = new Object[SIZE];
        this.count = 0;

    }//end constructor

    public final Object getValueAt(final int index)  //getAt ??
    {
        if(index >= this.count)
        {
            return null;
        }//end if

        return this.val[index];
    }//end getValueAt

    public final Comparable getKeyAt(final int index)
    {
        if(index >= this.count)
        {
            return null;
        }//end if

        return this.key[index];
    }//end getKeyAt

    public final void clear()
    {
        for(int x=0;x<count;x++)
        {
           this.key[x] = null;
           this.val[x] = null;
        }//end for

        this.count = 0;
        //System.gc(); ??
    }//end clear

    public final Object get(final Comparable key)
    {
        Object result = null;

        if(this.has(key))
        {
            result = val[this.find(key)];
        }//end if

        return result;

    }//end get

    public final int size()
    {
        return this.count;
    }//end size

    private final int find(final Comparable target)
    {
        Comparable midValue;

        int low  = 0;
        int high = this.count;

        if(this.count == 0) return 0;

        int mid  = (low + high) / 2;

        while (low < high)
        {
            //midValue = (Comparable) this.key[mid];
            midValue = this.key[mid];

            if (midValue.compareTo(target) < 0)
            {
                low = mid+1;
            }
            else
            {
                high = mid;
            }//end if
            mid = (low+high) / 2;
        }//end while

        return low;

    }//end find

    public final boolean has(final Comparable key)
    {
        if(this.count == 0) return false;

        final int index = this.find(key);

        return (index < this.count) && this.key[index].equals(key);

    }//end has

    public final Object remove(final Comparable key)
    {
        Object result = null;

        if(this.has(key))
        {
            result = this.remove(this.find(key));
        }//end if

        return result;

    }//end remove

    public final Object remove(final int index)
    {
        if (index >= count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + count);
        }//end if

        Object temp = val[index];

        count--;
        if (index < count)
        {
            System.arraycopy(key, index + 1, key, index, count - index);
            System.arraycopy(val, index + 1, val, index, count - index);
        }//end if

        key[count] = null;
        val[count] = null;

        return temp;

    }//end remove

    public final void add(final Comparable key, final Object value)
    {
        //check if already have key in associative array, change value

        final int index = this.find(key);
        this.add(index, key, value);

    }//end add

    public final void add(final int index, final Comparable key, final Object value)
    {
        if (index > count)
        {
            throw new ArrayIndexOutOfBoundsException(index + " > " + count);
        }//end if

        if (count == this.key.length)
        {
            ensureCapacity(count + 1);
        }//end if

        System.arraycopy(this.key, index, this.key, index + 1, count - index);
        System.arraycopy(this.val, index, this.val, index + 1, count - index);

        this.key[index] = key;
        this.val[index] = value;
        count++;

    }//end add

    private final void ensureCapacity(final int resize)
    {
        if (key.length >= resize) return;

        //System.out.println("ensureCapcity length = "+key.length);

        final int    newSize     = key.length + SIZE;
        Comparable[] newKeyArray = new Comparable[newSize];
        Object[]     newValArray = new Object[newSize];

        System.arraycopy(key, 0, newKeyArray, 0, count);
        System.arraycopy(val, 0, newValArray, 0, count);

        key = newKeyArray;
        val = newValArray;

    }//end ensureCapacity

    public final Object[] toArray()
    {
        Object[] result = new Object[2*count];

        for(int x=0;x<count;x++)
        {
            result[2*x]   = this.key[x];
            result[2*x+1] = this.val[x];
        }//end for

        return result;

    }//end toArray

    public final String toString()
    {
        StringBuffer str = new StringBuffer();

        str.append("[ ");

        for(int x=0;x<this.count;x++)
        {
            str.append("("+this.key[x]);
            str.append(",");
            str.append(this.val[x]+") ");
        }//end for

        str.append("]");

        return str.toString();

    }//end toString
/*
    public static void main(String[] args)
    {
        AssociativeArray aa = new AssociativeArray();

        System.out.println("find = "+aa.find("hello"));
        aa.add("hello", "hello_key");
        System.out.println("find = "+aa.find("hello"));

        System.out.println(aa);

        aa.add("aaa", "aaa_key");
        aa.add("bbb", "bbb_key");
        aa.add("ccc", "ccc_key");
        aa.add("xxx", "xxx_key");
        aa.add("zzz", "zzz_key");

        System.out.println(aa);
        System.out.println("has = "+aa.has("nullity"));
        System.out.println("has = "+aa.has("hello"));

        aa.remove("nullity");
        aa.remove("ccc");

        System.out.println(aa);

        Object value = aa.get("hello");
        System.out.println("hello => "+value);
        System.out.println();

        Object[] obj = aa.toArray();

        for(int x=0;x<obj.length;x++)
        {
            System.out.println("Object["+x+"] = "+obj[x].toString());
        }//end for

        System.exit(0);

    }//end main
//*/
  
 
}//end class AssociativeArray
