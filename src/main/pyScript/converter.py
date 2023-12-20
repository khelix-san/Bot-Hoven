import os
import sys

# Accessing command-line arguments
arguments = sys.argv

fileName = "src/main/fileOutput/"+arguments[1]

try:
    os.system("timidity "+fileName+".mid -Ow -o "+fileName+".wav")
    os.system("ffmpeg -i "+fileName+".wav -y -vn -ar 44100 -ac 2 -ab 192k "+fileName+".mp3")
    os.system("rm "+fileName+".mid;rm "+fileName+".wav")
except Exception as e:
    print("An error occurred:",e)