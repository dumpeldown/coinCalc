kill $(ps aux | grep 'java -jar target\/coinCalc-' | grep -v grep | awk '{print $2}')
