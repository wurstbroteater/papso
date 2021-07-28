reset
set xrange [-5:5]
set yrange [-5:5]
set isosample 250, 250
set table 'test.dat'
splot "landscape.tsv" matrix using 1:2:3
unset table

set contour base
set cntrparam level incremental -3, 0.0005, 3
unset surface
set table 'cont.dat'
splot "landscape.tsv" matrix using 1:2:3
unset table