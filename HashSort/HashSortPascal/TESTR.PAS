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
program testr (input,output);

const
  maxdim=110;
type
  matrix=array[0..maxdim-1,0..maxdim-1] of integer;
  number_list=array[0..(maxdim*maxdim)-1] of integer;
var
  size:integer;
  m:matrix;
  list:number_list;
  minimum:integer;
  
procedure initialize_matrix(var m:matrix;size:integer);
var
  x,y:integer;
begin
  write('Initializing array...');
  randomize;
  for x:=0 to size-1 do
    for y:=0 to size-1 do
    begin
      m[x,y]:=y+(x * size)+minimum;
    end;
  writeln(' done.');
end;

procedure initialize_list(var list:number_list;size:integer);
var
  x,y:integer;
begin
  write('Initializing data list... ');
  for x:=0 to size*size-1 do
  begin
      list[x]:=(x)+minimum;
      {writeln('List[',x,'] = ',x+minimum);}
  end;
  writeln(' done.');
end;

procedure scramble_list(var list:number_list;size:integer);
var
  temp,r,x:integer;
begin
  write('Scrambling list...');
  randomize;
  for x:=0 to size*size-1 do
  begin
    r:=random(size*size);
    temp:=list[x];
    list[x]:=list[r];
    list[r]:=temp;
  end;
  writeln(' Done.');
end;

procedure scramble_matrix(var m:matrix; size:integer);
var
  x,y:integer;
  p,q:integer;
  temp,j:integer;
begin
  write('Scrambling array...');
  randomize;
  for x:=0 to size-1 do
    for y:=0 to size-1 do
    begin
      p:=random(size);
      q:=random(size);
      temp:=m[x,y];
      m[x,y]:=m[p,q];
      m[p,q]:=temp;
    end;

  for j:=1 to size do
  begin
    p:=random(size);
    q:=random(size);
    x:=random(size);
    y:=random(size);
    temp:=m[p,q];
    m[p,q]:=m[x,y];
    m[x,y]:=temp;
  end;
  writeln(' done.');
end;

procedure sort_matrix(var m:matrix;size:integer);
var
  xc,hc:integer;
  map_value,where:integer;
  value,n:integer;
begin
  xc:=0;
  hc:=0;
  n:=size*size;
  where:=0;

  while ((xc < n) AND (hc < n)) do
  begin
    value:=m[where div size, where mod size];
    map_value:=value-minimum;
    if (map_value=where) then
    begin
      where:=where+1;
      hc:=hc+1;
    end
    else
    begin
      m[where div size, where mod size]:=m[map_value div size,map_value mod size];
      m[map_value div size, map_value mod size]:=value;
      xc:=xc+1;
    end;
  end;
  writeln('Total hystersis count of ',hc);
  writeln('Total exchange count of  ',xc);
  readln;
end;

procedure write_matrix(m:matrix;size:integer);
var 
  x,y:integer;
begin
  for x:=0 to size-1 do
  begin
    write('[ ');
    for y:=0 to size-1 do
    begin
      if (m[x,y]< 10) then
        write('0');
      write(m[x,y],' ');
    end;  
    writeln(']');
  end;
  writeln;      
end;

procedure write_list(list:number_list;size:integer);
var
  x,y:integer;
begin
  for x:=0 to size-1 do
  begin
    write('[');
    for y:=0 to size-1 do
    begin
      write(list[y+x*size]);
      write(' ');
    end;
    writeln(']');
  end;
writeln;
end;

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
  sort(0,size);
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
    for x:=0 to size do
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

begin
  write('Enter the size of the matrix n x n: ');
  readln(size);
  if (size>maxdim) then
    size:=maxdim;
  writeln;
  write('Enter the minimum value for the range: ');
  readln(minimum);
  if (minimum < size*10) then
    minimum:=size*10;
  initialize_matrix(m,size);
  writeln('Matrix size = ',size,' x ',size,'.');
  writeln('Minimum val = ',minimum);
  write('Press ENTER to continue...');
  readln;
  writeln;
  write_matrix(m,size);
  write('Press ENTER to continue...');
  readln;
  writeln;
  scramble_matrix(m,size);
  write_matrix(m,size);
  write('Press ENTER to continue...');
  readln;
  writeln;
  sort_matrix(m,size);
  write_matrix(m,size);
  write('Press ENTER to continue...');
  readln;
  writeln;

  initialize_list(list,size);
  write_list(list,size);
  readln;
  scramble_list(list,size);
  write_list(list,size);
  readln;
  bubblesort(list,size*size-2);
  quicksort(list,size*size-1);
  write_list(list,size);
  readln;
end.
