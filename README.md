# Scripting Language Interpreter
A lightweight, scripting language interpreter written in Java.
This project implements a dynamically-typed language
with modern syntax features

## Features

- variable declarations with `let` and `const`
- Basic data types: numbers, strings, booleans, and `none` for nothing
- Control flow if's and loops
- you can define functions
- Built-in print statement
- good enough error handling
- It supports classes, but it doesn’t have inheritance.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Makefile (for building)
- I think you have to have any flavor of linux

### Building the Project
Clone the repository and build using Make:

```bash
git clone https://github.com/Ahmed0427/my-own-interpreter.git
cd my-own-interpreter
make compile
```

### Running the Interpreter

#### REPL Mode
To start the interactive REPL:
```bash
make run
```

#### File Mode
To run a specific source file:
```bash
make run file=path/to/your/file.txt
```

## Language Syntax

### Basic Examples
```javascript
// Variables and constants
let age = 25;
const PI = 3.14159;

// Arithmetic operations
let sum = 10 + 5;
let diff = 10 - 5;
let prod = 10 * 5;
let quot = 10 / 5;

// String operations
let name = "Hello " + "World";

let isTrue = 10 > 5;

// if statments 

let x = 0;
let y = 0;

if (x < 5 and y > 10) {
    print "x < 5 and y > 10";
}
else if (x == 5 or y == 0) {
    if (x == 5) print "x == 5";
    else print "y == 0";
}
else {
    print "only in Ohio";
}

// print odd numbers less than 10

let i = 0;

while (i < 10) {
    if (i % 2) {
        print i;
    }
    i = i + 1;
}

// program to print the first 21 elements in the Fibonacci sequence

let previous;
let current = 0;

for (let next = 1; current < 10000; next = previous + next) {
  print current;
  previous = current;
  current = next;
}

// trying functions and return statement

fun fib(n) {
  if (n <= 1) return n;
  return fib(n - 2) + fib(n - 1);
}

for (let i = 0; i < 20; i = i + 1) {
  print fib(i);
}

fun factorial(n) {

  if (n == 0) {
    return 1;
  }

  return n * factorial(n - 1);
}

print factorial(5);  // Output: 120

// testing closures

fun makeCounter() {
  let i = 0;
  fun count() {
    i = i + 1;
    print i;
  }

  return count;
}

let counter = makeCounter();
counter(); // "1".
counter(); // "2".

// testing anonymous functions

fun thrice(fn) {
  for (let i = 1; i <= 3; i = i + 1) {
    fn(i);
  }
}

thrice(fun (a) {
  print a;
});

// "1".
// "2".
// "3".

class Bacon {
  eat() {
    print "Crunch crunch crunch!";
  }
}

Bacon().eat(); // Prints "Crunch crunch crunch!".

```

## Contributing
Contributions are not welcome! Please don't submit a Pull Request. (Just kidding!)

## Acknowledgments
- [Crafting Interpreters](https://craftinginterpreters.com/) by Robert Nystrom was a huge help throughout this project.
