/**
 *
 * Title: Binar Sort - binar sort algorithm. Description: Sort data using a bit
 * pattern into different sub-arrays. Copyright: Copyright (C) 2008
 *
 * @author William F. Gilreath
 * @version 1.0
 *
 * This file is an implementation of the binar sort and binar shuffle, described
 * in the research monographs:
 *
 * "Binar Sort: A Linear Generalized Sorting Algorithm" available at
 * https://arxiv.org/abs/0811.3448
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 */
class BinarSort {

    public final static void sort(int lower, int upper, int pos, int[] array) {

        //check for termination of recursion to return without partitioning 
        if (pos == 33 || upper < lower + 1) {
            return; //retain original array boundaries for later use in the code. 
        }
        int lo = lower; //array lower bound position
        int hi = upper; //array upper bound position

        //while boundaries do not crossover, continue partition iteration 
        //lo increases to hi, hi decrease to lo, crossover with one element 
        while (lo < hi + 1) {

            //extract the bit from the element, at the lower index position 
            //shift element by bit position, and use bit-mask to get bit 
            int bit = (array[lo] << pos) & 0x80000000;

            //based upon bit put element into lower or upper sub-array 
            if (bit == 0) //bit is 0 move element in lower sub-array
            {
                //element in lower sub-array, extend boundary encompass it
                lo++;
            } else //bit is non-0 element in upper sub-array 
            {
                //exchange element by swap to lower index position 
                int temp = array[hi];
                array[hi] = array[lo];
                array[lo] = temp;
                //element in upper sub-array, extend boundary to encompass it 
                hi--;
            }//end if

        }//end while

        //check if sub-array encompasses entire original array, pass through 
        if (lo == upper + 1) {
            //pass through, use array bounds at next bit position to continue
            sort(lower, upper, pos + 1, array);
        } else {
            //for partition, pass sub-arrays at next bit position 
            sort(lower, lo - 1, pos + 1, array); //pass lower sub-array 
            sort(lo, upper, pos + 1, array);     //pass upper sub-array
        }//end if 

    }//end sort

}//end class BinarSort
