/*
 *   Hash Sort Algorithm in C++. Source code for algorithm described in
 *   "Hash sort: A linear time complexity multiple-dimensional sort algorithm."
 *   Research monograph at http://arxiv.org/abs/cs/0408040
 *      
 *   Copyright 2015 William F. Gilreath.  All Rights Reserved.
 *   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

#ifndef _NOOUT
   #define OUT(x) cout << x << endl;
#endif

#define RAND() rand()
#define SRAND(x) srand(x)

#include <iostream>
#include <iomanip>
#include <fstream>
#include <time.h>
#include <math.h>
#include <stdlib.h>

using namespace std;

//total time for each algorithm
double qtime, htime;

//time variables used
	clock_t start, stop;


//function to compute the elapsed time
double duration(const clock_t first, const clock_t last)
{
	return(double)(last - first) / CLOCKS_PER_SEC;
}

// downloaded and inserted verbatim from 
// http://http://www.brokersys.com/snippets/RGIQSORT.C 
// February 2000
// 
/*
** quicksort.c -- quicksort integer array
**
** public domain by Raymond Gardner     12/91
*/

static void swap(int *a, int *b)
{
      register int t;

      t = *a;
      *a = *b;
      *b = t;
}

void quicksort(int v[], unsigned n)
{
      unsigned i, j, ln, rn;

      while (n > 1)
      {
            swap(&v[0], &v[n/2]);
            for (i = 0, j = n; ; )
            {
                  do
                        --j;
                  while (v[j] > v[0]);
                  do
                        ++i;
                  while (i < j && v[i] < v[0]);
                  if (i >= j)
                        break;
                  swap(&v[i], &v[j]);
            }
            swap(&v[j], &v[0]);
            ln = j;
            rn = n - ++j;
            if (ln < rn)
            {
                  quicksort(v, ln);
                  v += j;
                  n = rn;
            }
            else
            {
                  quicksort(v + j, rn);
                  n = ln;
            }
      }
}

void hashsort(const int n)
{

	int x,y; //loop variables
	
	//counts of exchanges and collisions
	int exchange_count = 0, hysteresis_count = 0;
	//locus of where exchanges occur
	int where = 0;
	int value, norm_value;
	int row, col;
	int nrow, ncol;

	int i,j;
	bool cont;

	long *mx = NULL;

	int min = 0;
	mx = new long[n*n];

	//seed the random generator
	SRAND(987654321);

	//mark all locations with -1 
	for(x=0;x<n;x++)
	{	
		for(y=0;y<n;y++)
		{			
		     //m(x,y) = -1;
		     mx[(n*x + y)] = -1;
		}
	}

	//randomly initialize a location with increasing values
	for(x=0;x<n;x++)
	{	
		for(y=0;y<n;y++)
		{	
				do {
					i = RAND() % n;
					j = RAND() % n;
				    if( mx[(n*x + y)]== -1)
					{
					    //m(i,j)= x*n+y;
					    mx[(n*x + y)] = x*n+y;
					    cont = false;
					}
					else
					    cont = true;

				} while(cont);
		}
		
	}

	//add the minimum to all locations
	for(x=0;x<n;x++)
	{	
		for(y=0;y<n;y++)
		{	
			  mx[(n*x + y)]+= min;
		}
	}

	start = clock();

	while((exchange_count < n*n) && (hysteresis_count < n*n))
	{
		//get the value from the where location
		row = where / n;
		col = where % n;
		value = mx[(n*row + col)];

		//normalize within the range
		norm_value = value - min;
	
		//if the normalized value equals where location
		//value maps back to itself...hash clash/hysteresis
		if(norm_value == where)
		{
			where++;			//move where location
			hysteresis_count++; //increment hysteresis count
		}
		else
		{  
			//determine the location where the value belongs
			nrow = (norm_value / n);
			ncol = (norm_value % n);

			//swap the value with current where location
			mx[(n*row + col)]       = mx[(n*nrow + ncol)];

			//put the value in its correct location
			mx[(n*nrow + ncol)] = value;
			
			exchange_count++;	//increment exchange count

		}//end if
	}

	stop = clock();
	
	delete[] mx;
}

int main()
{
	ofstream out;	//log file of the time

	ofstream csv;   //create CSV data file

	//dynamic array created for sorting
	int *v;
	
	int m, n, size;
	int x;
	

	cout << "Sizeof long = " << sizeof(long) << " bytes." << endl;

	out.open("sorttest.log");
	csv.open("hashsort.csv");

   OUT("Seeding random number generator")
   //seed random number generator
   SRAND(123456789);
   
   //generate minimum value for all values >= min
   int min = RAND();
   out << setprecision(5);

   csv << "n,n^2,qsort,qsort^2,hsort,hsort^2" << endl;


   //loop from 10000 to 2500000000
   for(n = 0; n <= 500;n++)
   {

	    cout << n << endl;
	    m = (100*n)+100;
	    out <<  "n = " << m ;
		csv <<  "n,"   << m ;
		//compute size 
		size = m * m;

	    out <<  " n^2 = " << size ;
		csv <<  " n^2,"   << size ;

		//allocate space
		v = new int[size];
		
		//initialize the array
		for(x = 0;x < size;x++)
		{	
			v[x] = (RAND() % size) + min;
		}
		
		//call the routine to quicksort
	
		//get the start time
		start = clock();
		
		quicksort(v, size);
		
		//get the stop time
		stop = clock();

		//compute qtime
		qtime = duration(start, stop);

		//normalize increasing size n^2 with square root
			out << " qsort = " << sqrt(qtime*1000);
			csv << "qsort,"    << sqrt(qtime*1000);

		out << " qsort^2 = " << (long)qtime;

		//deallocate space
		delete[] v; 

		hashsort(m);
   
		//compute htime
		htime = duration(start, stop);

		//normalize increasing size n^2 with square root
		out << " hsort = " << sqrt(htime*1000);
		csv << " hsort"   << sqrt(htime*1000);
		
		out << " hsort^2 = " << (long)htime;
		csv << "hsort^2," << (long)htime;
   
		out << " mSec";
		
		out << endl;
		csv << endl;
   }
   
   out << endl;
   csv << endl;

   out.close();
   csv.close();
   return 0;
}

 
