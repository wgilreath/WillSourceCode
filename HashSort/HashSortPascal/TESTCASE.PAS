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
program testcase_PC (input,output);

const
    maxsize=10000;
    max=32000;
    min=100;
type
    number_list=array[1..maxsize] of integer;
    intfile=file of integer;
var
    list:number_list;
    size,sublists,submax,sublistsize:integer;
    x,y,loc:integer;
    sorted,ascend:boolean;
    name:string;
    outfile:intfile;
    choice:string;
    seed:real;
    value:integer;

FUNCTION StringInt(S:string):integer;
{Converts command line parameters to integer values}
VAR
  value,x,maxval:integer;
  total:integer;
BEGIN
  maxval:=1;
  for x:=1 to length(S)-1 do
    maxval:=maxval*10;

  total:=0;
  for x:=1 to length(S) do
  begin
    value:=ord(S[x]);
    if (value<58) AND (value>47) then
    begin
      value:=value-48;
      total:=total+value*maxval;
      maxval:=maxval div 10;
    end
    else
    begin
      total:=0;
      maxval:=0;
    end;
  end;
  StringInt:=total;
END;

FUNCTION random_number(limit:integer):integer;
{Taken from Advanced Programming, A Practical Course. D.W. Barron,
    J.M. Bishop, John Wiley & Sons, New York 1984. pp. 26-27. }

BEGIN
    seed:=seed * 27.182813 +31.415917;
    seed:=seed - trunc(seed);
    random_number:=trunc(seed*limit)+1;
END;

begin
    seed:=1.23456789;
    sorted:=false;
    ascend:=true;
    if paramcount>0 then
      size:=StringInt(ParamStr(1))
    else
    begin
      write('Enter the size of the number list to generate: ');
      readln(size);
    end;
    if size>maxsize then
    begin
        size:=maxsize;
        writeln('Size greater than maximum. Defaulting to maximum.');
    end;
    if paramcount>1 then
      choice:=ParamStr(2)
    else
    begin
      writeln;
      write('Is the list to be sorted or unsorted (S/U): ');
      readln(choice);
    end;

    case choice[1] of
    's','S':sorted:=true;
    'u','U':sorted:=false;
    else
       writeln('Invalid input. Defaulting to unsorted list.');
       writeln;
    end;

    if sorted then
    begin
      if ParamCount>3 then
        sublists:=StringInt(paramstr(4))
      else
      begin
        write('Enter the number of sublists within the main list: ');
        readln(sublists);
        if sublists<0 then
        begin
            writeln('Invalid input. Defaulting to sublist size of 1.');
            sublists:=1;
        end;
      end;
    end
    else
        sublists:=1;
    if sorted then
    begin
        if paramcount>4 then
          choice:=paramstr(5)
        else
        begin
          write('Enter if the lists are to be ascending or descending (A/D): ');
          readln(choice);
        end;

        case choice[1] of
           'a','A':ascend:=true;
           'd','D':ascend:=false;
        else
            writeln('Invalid input. Defaulting to ascending list.');
        end;
        writeln;
    end;

    sublistsize:=size div sublists;
    submax:=(max-min) div sublists;

    for x:=1 to size do
        list[x]:=-1;

    for x:=1 to sublists do
        for y:=1 to sublistsize do
        begin
            loc:=(x-1)*sublistsize+y;
            if not(sorted) then
                list[loc]:=random_number(max+1)+min
            else
            begin
              value:=random_number(submax+1)+min+(x-1)*submax;
              if ascend then
                list[loc]:=value
              else
                list[size-loc]:=value;
            end;
        end;
    if paramcount>2 then
      name:=Paramstr(3)
    else
    begin
      write('Enter the name of the data file to save list to: ');
      readln(name);
      writeln;
    end;
    assign(outfile,name);
    rewrite(outfile);
    for x:=1 to size do
    begin
        value:=list[x];
        write(outfile,value);
    end;
    close(outfile);
end.