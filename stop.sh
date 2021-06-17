kill $(ps aux | grep coinCalc-1.1 | grep -v grep | awk '{print $2}')
