#!/bin/bash

# next line needed for ShellEd Bash debugger for Eclipse:
. _DEBUG.sh

# script requires that ENRAM_* environment variables have been set

#Czech Republic 
#RUN for id='T_PAHZ60' and destination=CZ_brd
#AND     id='T_PAHZ50' and destination=CZ_ska

: ${ENRAM_RAW_DATA:?"script requires ENRAM_RAW_DATA to have been set."}
: ${ENRAM_CONVERTED_DATA:?"script requires ENRAM_CONVERTED_DATA to have been set."}
: ${ENRAM_TMP:?"script requires ENRAM_TMP to have been set."}

# set source and destination
source=$ENRAM_RAW_DATA'CZ/czech*'
destination=$ENRAM_CONVERTED_DATA'CZ_'

files=`ls -1 $source`
 
thisdir=`pwd`

echo $thisdir


#initialize counter
i=`expr 0`
for file in $files
do

  name=${file%.tar.gz}      #strip file extension
  date=${name:6}            #get only the date indication, use for path
  $stationname
  echo -en '\r' `date`": Unpacking $date"
  #
  tar --directory $ENRAM_TMP -xzf $file
  #
  #Find the different system files and redistribute them
  cd 'hdf5/'$date
  #
  allfiles=`ls -1 $source*.hdf`
  number_of_files=`ls -1 $source*.hdf | wc -l`
  #
  #PROCESS all files
  for ifile in $allfiles
  do 
    #
    name=${ifile%.hdf}
    stationcode=${name:6:2} 
    #
    #REDISTRIBUTE according to station name
    if [ $stationcode == 60 ] 
    then
      code='ska'
    fi
    if [ $stationcode == 50 ] 
    then
      code='brd'
    fi
    #
    #create destination directory name
    thisdestination=$destination$code"/"$date
    #
    mkdir -p $thisdestination
    mv $ifile  $thisdestination
    #
    let i=i+1
  done   
  #
  cd $thisdir
  #
done 

#remove unpack directory
rm -fr 'hdf5'

echo -en '\r' `date`": Moved $i files of id $id to $destination"
echo

exit 0
