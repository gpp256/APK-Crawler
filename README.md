APK-Crawler
============

APK-Crawler is a tool for collecting apk files.

Quick Start
------------

To start, run the following commands.

    e.g.
    $ make
    $ vi getapp.conf
    $ cat getapp.conf
    {
    "userid": "hoge@gmail.com",
    "passwd": "hogepassword",
    "deviceid": "0000011111222220",
    "pkgdir": "pkgs/"
    }
    $ sh getpkg.sh
    Usage: getpkg.sh getinfo query-str offset num
           getpkg.sh getpkg pkgid
    $ sh getpkg.sh getinfo game 0 10
    $ sh getpkg.sh getpkg v2:com.hoge.tool:1:23

Links
--------

Jackson: http://jackson.codehaus.org/  
android-market-api: https://code.google.com/p/android-market-api/  

License
----------

You are bound to the license agreement included in respective files.

