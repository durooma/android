#!/bin/sh
#---------------------------------------------------------------
# Given an xxhdpi image or an App Icon (launcher), this script
# creates different dpis resources
#
# Place this script, as well as the source image, inside res
# folder and execute it passing the image filename as argument
#
# Example:
# ./drawables_dpis_creation.sh ic_launcher.png
# OR
# ./drawables_dpis_creation.sh my_cool_xxhdpi_image.png
#---------------------------------------------------------------

echo " Creating different dimensions (dips) of "$1" ..."

filename=$(basename "$1")

if [ filename = "ic_launcher.png" ]; then
    echo "  App icon detected"

    convert ic_launcher.png -resize 144x144 app/src/main/res/drawable-xxhdpi/ic_launcher.png
    convert ic_launcher.png -resize 96x96 app/src/main/res/drawable-xhdpi/ic_launcher.png
    convert ic_launcher.png -resize 72x72 app/src/main/res/drawable-hdpi/ic_launcher.png
    convert ic_launcher.png -resize 48x48 app/src/main/res/drawable-mdpi/ic_launcher.png
    rm -i $1
else

    convert $1 -resize 75% "app/src/main/res/drawable-xxhdpi/$filename"
    convert $1 -resize 50% "app/src/main/res/drawable-xhdpi/$filename"
    convert $1 -antialias -resize 38% "app/src/main/res/drawable-hdpi/$filename"
    convert $1 -resize 25% "app/src/main/res/drawable-mdpi/$filename"
    cp $1 "app/src/main/res/drawable-xxxhdpi/$filename"

fi

echo " Done"