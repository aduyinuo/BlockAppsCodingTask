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
Example command:

/Library/Java/JavaVirtualMachines/jdk-13.0.2.jdk/Contents/Home/bin/java -Djava.library.path=/Users/yinuo/opencv4:/Users/yinuo/opencv4 -Dfile.encoding=UTF-8 -p /Users/yinuo/opencv4/opencv-420.jar -classpath /Users/yinuo/eclipse-workspace/BmpSaver/bin:/Users/yinuo/opencv4/opencv-420.jar DirTraverse inputDir outputDir -noAlpha -noDepth -t8