JC = javac
JVM = java

SRC_DIR = src
BIN_DIR = bin
LIB_DIR = libs

MAIN = Main 

SOURCES = $(wildcard $(SRC_DIR)/*.java)

JLINE_JAR = $(LIB_DIR)/jline.jar
CLASS_PATH = $(BIN_DIR):$(JLINE_JAR)

compile: 
	@$(JC) -d $(BIN_DIR) -cp $(CLASS_PATH) $(SOURCES)

clean:
	@rm -r $(BIN_DIR)
