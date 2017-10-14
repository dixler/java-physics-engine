all: Window.class

Window.class: Env.java Landscape.java PhysObj.java Window.java
	javac Window.java

test:
	java Window

clean:
	rm *.class
