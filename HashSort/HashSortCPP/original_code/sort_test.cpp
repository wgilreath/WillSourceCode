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

#ifndef WIN32
   #define UNIX 1
#endif

#ifndef _NOOUT
   #define OUT(x) cout << x << endl;
#endif

#ifdef WIN32
   #define RAND() rand()
   #define SRAND(x) srand(x)
#endif

#ifdef UNIX
   #include <stdlib.h>
   #define RAND() random()
   #define SRAND(x) srandom(x)
#endif

#include "sort_test.h"

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
   min = RAND();
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
	
	
		//get the start time
		#ifdef WIN32
			start = clock();
		#endif
		
		#ifdef UNIX
			gettimeofday(&start, NULL);
		#endif
		
		//call the routine to quicksort
		quicksort(v, size);
		
		//get the stop time
		#ifdef WIN32
			stop = clock();
		#endif
		
		#ifdef UNIX
			gettimeofday(&stop, NULL);;
		#endif

		//compute qtime
		qtime = duration(start, stop);

		//normalize increasing size n^2 with square root
		#ifdef WIN32
			out << " qsort = " << sqrt(qtime*1000);
			csv << "qsort,"    << sqrt(qtime*1000);
		#endif
		
		#ifdef UNIX
			out << " qsort = " << sqrt(qtime);
			csv << " qsort,"   << sqrt(qtime);
		#endif

		out << " qsort^2 = " << (long)qtime;

		//deallocate space
		delete[] v; 

		hashsort(m);
   
		//compute htime
		htime = duration(start, stop);

		//normalize increasing size n^2 with square root
		#ifdef WIN32
			out << " hsort = " << sqrt(htime*1000);
			csv << " hsort"   << sqrt(htime*1000);
		#endif
		
		#ifdef UNIX
			out << " hsort = " << sqrt(htime);
			csv << " hsort," << sqrt(htime);
		#endif
		out << " hsort^2 = " << (long)htime;
		csv << "hsort^2," << (long)htime;
   
   		#ifdef WIN32
		out << " mSec";
		#endif
		
		#ifdef UNIX
		out << " uSec";
		#endif
		
		out << endl;
		csv << endl;
   }//end for n
   
   out << endl;
   csv << endl;

   out.close();
   csv.close();
   return 0;
}

 
