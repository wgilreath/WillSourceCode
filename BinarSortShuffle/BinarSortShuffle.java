/**
 *
 * Title:        Binar Sort/Shuffle - binar shuffle and binar sort algorithms.
 * Description:  Binar sort and shuffle data using a bit pattern into different sub-arrays.
 * Copyright:    Copyright (C) 2008
 * @author       William F. Gilreath
 * @version      1.0
 *
 * This file is an implementation of the binar sort and binar shuffle, described in the research
 * monographs:
 *
 * "Binar Sort: A Linear Generalized Sorting Algorithm" available at https://arxiv.org/abs/0811.3448
 * "Binar Shuffle: Shuffling Bit by Bit" available at https://arxiv.org/abs/0811.3449 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */
 
public final class BinarSortShuffle
{

    public final static int bitInWord(final int num, final int pos)
    {
        //check if pos*n >= num => error ??

        final int n = 1;  //bit-size = 1
        final int w = 32; //word   size = 32
        final int p = w-1-(n*pos);

        int result = (num >> (p-n+1) & ~(~0 << n));

        return result;

    }//end bitInWord
    
    public final static void binar_shuffle(int[] data, final int start, final int close, final int[] bit_sched, final int pos)
    {
        if (pos == bit_sched.length || close - start == 1) return;

        int lo = start;
        int hi = close;

        while (lo < hi + 1)
        {
            //final int bit = (array[lo] << pos) & 0x80000000;
            final int bit = bitInWord(data[lo], bit_sched[pos]);
            
            if (bit == 0)
            {
                lo++;
            }
            else
            {
                int temp = data[hi];
                data[hi] = data[lo];
                data[lo] = temp;
                hi--;
            }//end if

        }//end while

        if(lo == close + 1)
        {
            binar_shuffle(data, start, close, bit_sched, pos + 1);
        }
        else
        {
            binar_shuffle(data, start, lo - 1, bit_sched, pos + 1);
            binar_shuffle(data, lo, close, bit_sched, pos + 1);
        }//end if
                
    }//end binar_shuffle

    public final static void inverse_binar_shuffle(int[] data, final int start, final int close, final int[] bit_sched, final int pos)
    {
        
        if (pos < 0 || close - start == 1) return;

        int lo = start;
        int hi = close;

        while (lo < hi + 1)
        {
            //final int bit = (array[lo] << pos) & 0x80000000;
            final int bit = bitInWord(data[lo], bit_sched[pos]);
            
            if (bit == 0)
            {
                lo++;
            }
            else
            {
                int temp = data[hi];
                data[hi] = data[lo];
                data[lo] = temp;
                hi--;
            }//end if

        }//end while

        if(lo == close + 1)
        {
            binar_shuffle(data, start, close, bit_sched, pos - 1);
        }
        else
        {
            binar_shuffle(data, start, lo - 1, bit_sched, pos - 1);
            binar_shuffle(data, lo, close, bit_sched, pos - 1);
        }//end if
                
    }//end inverse_binar_shuffle
    
    public final void binarSort(final int lo_bound, final int hi_bound, final int pos, int[] array)
    {
        if (pos == 32 || hi_bound < lo_bound + 1) return;

        int lo = lo_bound;
        int hi = hi_bound;

        while (lo < hi + 1)
        {
            final int bit = (array[lo] << pos) & 0x80000000;

            if (bit == 0)
            {
                lo++;
            }
            else
            {
                int temp = array[hi];
                array[hi] = array[lo];
                array[lo] = temp;
                hi--;
            }//end if

        }//end while

        if(lo == hi_bound + 1)
        {
            binarSort(lo_bound, hi_bound, pos + 1, array);
        }
        else
        {
            binarSort(lo_bound, lo - 1, pos + 1, array);
            binarSort(lo, hi_bound, pos + 1, array);
        }//end if

    }//end binarSort
    
    
    public final static void binar_shuffle(int[] data, int[] bit_sched)
    {
            binar_shuffle(data, 0, data.length-1, bit_sched, 0);
        
    }//end binar_shuffle

    public final static void inverse_binar_shuffle(int[] data, int[] bit_sched)
    {
            inverse_binar_shuffle(data, 0, data.length-1, bit_sched, bit_sched.length-1);
        
    }//end binar_shuffle
    
    public final static void printArray(int[] array)
    {
        //System.out.println();
        System.out.printf("Array = [");
        for(int x=0;x<array.length;x++)
        {
            System.out.printf(" %2d ", array[x]);
        }//end for
        
        System.out.println(" ]");
        System.out.println();
        
    }//end printArray

    public static void main(String[] args)
    {
        /*
        int test = 255;
        
        for(int x=24;x<32;x++) //bits 24..31
            
            //MSB=0, LSB=31
            System.out.printf("%d ", bitInWord(test, x));   //bitInWord(num, bit_sched[pos])
        
        System.out.println();
        //*/
    
/*
        System.out.println();
        
        int[] data = { 0, 1, 2, 3, 4, 5, 6, 7 };
        int[] bits = { 31, 30, 29 };
        
        System.out.printf("Begin forward binar shuffle ");
        printArray(data);
        
        binar_shuffle(data, bits);

        System.out.printf("After forward binar shuffle ");        
        printArray(data);
        
        inverse_binar_shuffle(data, bits);
                
        System.out.printf("After inverse binar shuffle ");

        printArray(data);
//*/        
        System.out.println();
        
        //int[] data = { 0, 1, 2, 3, 4, 3, 5, 6, 7 };
        int[] data = { 0,   4,   2,   6,   1,   5,   3,   3,   7  };
        //int[] bits = { 31, 30, 29 };//inverse binar shuffle--sort descending/reverse
        
        int[] bits = { 29, 30, 31 };  //binar shuffle--sort ascending/forward
        //int[] data = { 4, 5, 6, 7, 0, 1, 2, 3 };
        
        System.out.printf("Begin forward binar shuffle ");
        printArray(data);
        
        //inverse_binar_shuffle(data, bits);
        binar_shuffle(data, bits);

        System.out.printf("After forward binar shuffle ");        
        printArray(data);
        
        System.out.println();
    
        System.exit(0);
    }//end main
    
}//end class BinarSortShuffle
