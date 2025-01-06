JC = javac
JVM = java
SRCDIRS = src
BINDIR = bin
LIBDIR = libs
MAIN = Main 
SOURCES = $(foreach DIR, $(SRCDIRS), $(wildcard $(DIR)/*.java))
JLINE_JAR = $(LIBDIR)/jline.jar
CLASSPATH = $(BINDIR):$(JLINE_JAR)

compile: 
	@$(JC) -d $(BINDIR) -cp $(CLASSPATH) $(SOURCES)

run: compile
ifeq ($(file),)
	@$(JVM) -cp $(CLASSPATH) $(MAIN)
else
	@$(JVM) -cp $(CLASSPATH) $(MAIN) $(file)
endif

clean:
	@rm -rf $(BINDIR)/*.class
