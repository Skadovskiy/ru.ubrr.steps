.DEFAULT_GOAL := compile-run

compile-run: compile run

compile: clean
	javac -d ./target/classes ./src/main/java/games/Slot.java

run:
	java -jar ./target/ru.ubrr.steps.jar

clean:
	rm -rf ./target

build: compile
	jar cfe ./target/ru.ubrr.steps.jar games.Slot -C ./target/classes .