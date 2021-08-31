

JAVAC=/usr/bin/javac
.SUFFIXES: .java.class
SRCDIR =src
BINDIR =bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<
	
CLASSES =BinaryTreeNode.class Student.class  BTQueueNode.class BTQueue.class BinaryTree.class BinarySearchTree.class AccessArrayApp.class AccessBSTApp.class 

CLASS_FILES = $(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)
clean:
	rm $(BINDIR)/*.class
	
	
runaccess: $(CLASS_FILES)
	java -cp $(BINDIR) AccessArrayApp
	
runbst : $(CLASS_FILES)
	java -cp $(BINDIR) AccessBSTApp