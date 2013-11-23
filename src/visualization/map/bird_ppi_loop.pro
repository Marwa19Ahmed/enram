;PLOT multiple radars' ppi's

;SET the radar ids of the radars to be processed
radar_ids = ['all']
;radar_ids = []
radar_ids = radar_names(radar_ids)

gifdir='loop_gif/Hans/'
; SPECIFY the time interval to process.
;
dates='20110902'

scan=1
grid=1

plot_lon=0
plot_lat=45
;
;limit=[51,0,56,8]
limit=[41,-15,71,40]

fringe=0
rain=1

minutes=STRING(FORMAT='(i0,"0")',INDGEN(6))
hours=STRING(FORMAT='(i02)',INDGEN(8))

;minutes='00'
;hours='17'

FILE_MKDIR,gifdir
;
FOR ihour=0,N_ELEMENTS(hours)-1 DO BEGIN

  FOR iminute=0,N_ELEMENTS(minutes)-1 DO BEGIN

    date=dates[0]
    time=hours[ihour]+minutes[iminute]

    thething = KEYWORD_SET(rain) ? 'rain' : 'bird'
    print, FORMAT='("Plotting ",a," ppi on ",a,":",a)',thething,date,time

    find_files,files,date,time,radar_ids;,directory=directory

    IF KEYWORD_SET(definitions) THEN dummy=TEMPORARY(definitions)
    FOR id=0,N_ELEMENTS(radar_ids)-1 DO BEGIN
      radar_definitions,radar_ids[id],radar_definition
      definitions= KEYWORD_SET(definitions) ? [definitions,radar_definition] : radar_definition
    ENDFOR

    ;print, files
    ;stop

    psfile=thething+'_ppi_'+date+time
    giffile=psfile+'.gif'
    psfile+='.ps'

    map_bird_ppi,files,limit,$
    date=date,time=time,$
    definitions=definitions,$
    fringe=fringe,$
    rain=rain,$
    coast=0,$
    bg=1,$
    plot_lon=plot_lon,$
    plot_lat=plot_lat,$
    grid=grid,$
    psfile=psfile,$
    radar_cross=0,$
    radar_title=0

    PRINT
    h5_close

;    pstracker,psfile
    SPAWN, 'convert '+psfile+' '+gifdir+giffile
    SPAWN, 'rm -f '+psfile

  ENDFOR
ENDFOR

;exit

END