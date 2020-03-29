# BMP Negative Image Converter 

> Scenario: The BlockApps CTO has a bunch of scanned photo negatives (from the 90s, of course), all stored in BMP format.... (see https://en.wikipedia.org/wiki/BMP_file_format)
> Also, due to an unfortunately timed solar flare, all BMP image software across the globe has been destroyed forever.
> Task: Please help by writing some code that will convert these photo negatives back to the original photos.
> Details:
> - You have 4 days to complete this task, timing matters! After day 4, another solar flare will destroy the photo negatives, > and the code will become useless
> - Write up documentation on how to use the tool
> - Cut corners to get this working, but discuss with me personally first, so that we know that this will work properly for the use case and
> - Please only submit in a statically typed language (preferably a functional language) 

## Getting Started 

### Installation (OS X)



[OpenCV4+Java](https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html#introduction-to-opencv-for-java)

### Usage example 

- Command line arguments:
	- Required & Ordered
		- InputDir 
		- OutputDir
	- Optional & Unordered
		- "-noAlpha"
			- remove alpha channel
		- "-noDepth"
			- convert image color depth to 8 bit
		- "-t8"
			- use 8 threads to work simultaneously


```
/Library/Java/JavaVirtualMachines/jdk-13.0.2.jdk/Contents/Home/bin/java -Djava.library.path=/Users/yinuo/opencv4:/Users/yinuo/opencv4 -Dfile.encoding=UTF-8 -p /Users/yinuo/opencv4/opencv-420.jar -classpath /Users/yinuo/eclipse-workspace/BmpSaver/bin:/Users/yinuo/opencv4/opencv-420.jar DirTraverse inputDir outputDir -noAlpha -noDepth -t8
```

## Authors 

* **Yinuo Du** - *Initial work* - [Yinuo Du](http://www.linkedin.com/in/yinuo-du-sde-intern)


