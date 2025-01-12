# lightweight Scripting Language Interpreter
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

## Project Structure and Components
```
.
├── libs/                  # External dependencies
│   └── jline.jar          # JLine for better REPL experience
├── src/                   # Source code
│   ├── Main.java          # Entry point
│   ├── Tokenizer.java     # Converts text into tokens 
│   ├── Parser.java        # Converts tokens into AST
│   ├── Scope/             # Variables Map and the Parent Scope
│   ├── ExprNode/          # An interface defines evaluate function it is for *Node
│   ├── StmtNode/          # An interface defines evaluate function it is for *Stmt
│   ├── *Node/             # AST nodes that implements ExprNode
│   ├── *Stmt/             # AST nodes that implements StmtNode
│   ├── MyCallable/        # interface for stuff that can be called like functions
│   ├── MyFunction/        # implements MyCallable interface
│   └── ...               
├── tests/                 # Test files
│   └── ...                # Test programs
└── Makefile               # Build automation
```

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Makefile (for building)

### Building the Project
Clone the repository and build using Make:

```bash
git clone [repository-url]
cd [project-name]
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

fun factorial(n) {

  if (n == 0) {
    return 1;
  }

  return n * factorial(n - 1);
}

print factorial(5);  // Output: 120

```

## Development

### Adding New Features
1. Add the new grammar
2. Add relevant token types in `TokenType.java`
3. Implement new AST nodes if needed
4. Update the parser to handle new syntax
5. Add evaluation logic
6. Add tests in the `tests/` directory

## Testing
Test programs are in `tests/` directory and run using:
```bash
make run file=tests/your_test_file
```

## Contributing
Contributions are not welcome! Please don't submit a Pull Request.

## Acknowledgments
- [Crafting Interpreters](https://craftinginterpreters.com/) by Robert Nystrom helped me a lot throw this project
