(*
 *   Hash Sort Algorithm in Pascal. Source code for algorithm described in
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
 *)
Program TestSort_PC (input,output);
uses
    dos;
const
    maxsize=10000;
type
    number_list=array[1..maxsize] of integer;
    fileint=file of integer;
var
    list:number_list;
    size:integer;
    name:string;
    h,smin,ssec,ssec100:word;
    emin,esec,esec100:word;
    min,sec,sec100:word;

Procedure QuickSort(var list:number_list;size:integer);
{ From the book Data Structures + Algorithms = Programs Wirth, 1976,
  p. 79, adapted to fit the data structures, variables (interface mostly)
  for this program. William Gilreath June 1997.
}
  Procedure Sort(l,r:integer);
  Var
    i,j:integer;
    x,w:integer;
  Begin
    i:=l;
    j:=r;
    x:=list[(l+r) div 2];
    repeat
      while list[i]<x do
        i:=i+1;
      while x < list[j] do
        j:=j-1;
      if i<=j then
      begin
        w:=list[i];
        list[i]:=list[j];
        list[j]:=w;
        i:=i+1;
        j:=j-1;
      end;
    until i>j;
    if l<j then
      sort(l,j);
    if i<r then
      sort(i,r);
  End;

Begin
  sort(1,size);
End;

Procedure BubbleSort(var list:number_list;size:integer);
{ From the book Computing an Introduction to Structured Problem
  Solving Using Pascal Dyck, Lawson, Smith, Beach, 1982
  p. 336, from pseudo-code adapted to Turbo Pascal
  for this program.

  Other book Introduction to Computer Programming by
  Hartling, Druffel, Hilbing, 1983, p. 274, modified from Fortran
  code to Turbo Pascal. William Gilreath June 1997.
}
var
  temp,x:word;
  interchanged:boolean;
BEGIN
  interchanged:=true;
  while interchanged do
  begin
    interchanged:=false;
    for x:=1 to size do
    begin
      if list[x]>list[x+1] then
      begin
        temp:=list[x+1];
        list[x+1]:=list[x];
        list[x]:=temp;
        interchanged:=true;
      end;
    end;
  end;
END;

Procedure Compute_Time;
BEGIN
   min:=emin-smin;
   if esec>ssec then
       sec:=esec-ssec
   else
       sec:=ssec-esec;
   if esec100>ssec100 then
       sec100:=esec100-ssec100
   else
       sec100:=ssec100-esec100;
END;

Procedure Read_Data;
VAR
  infile:fileint;
  temp:integer;
BEGIN
  if ParamCount>0 then
    name:=ParamStr(1)
  else
  begin
    Write('Enter the name of the data file for input: ');
    Readln(Name);
  end;
  Assign(infile,name);
  Size:=1;
  Reset(infile);
  read(infile,temp);
  While not eof(infile) do
  Begin
    List[size]:=temp;
    Size:=Size+1;
    Read(infile,temp);
  End;
  Close(infile);
END;

Procedure Write_Data;
VAR
  outfile:text;
  temp,x:integer;
BEGIN
  if paramcount>1 then
    name:=paramstr(2)
  else
  begin
    Write('Enter the name of the data file for output: ');
    Readln(Name);
  end;
  Assign(outfile,name);
  Rewrite(outfile);
  For x:=1 to size do
  Begin
    Temp:=List[x];
    Writeln(outfile,temp);
  End;
  Close(outfile);
END;

Procedure Sort_Data;
BEGIN
  gettime(h,smin,ssec,ssec100);
  {BubbleSort(list,size);}
  QuickSort(list,size);
  gettime(h,emin,esec,esec100);
END;

BEGIN
  Read_Data;
  Sort_Data;
  Compute_Time;
  Write_Data;
  Writeln(min*60000+sec*1000+sec100*10,' milliseconds');
END.

