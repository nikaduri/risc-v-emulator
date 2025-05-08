# RISC-V Emulator in Java

## Project Description

This project is a lightweight RISC-V emulator implemented in Java. It simulates a RISC-V processor with a subset of the instruction set architecture, providing a virtual environment to run and test RISC-V assembly programs.

The emulator features:

- 32 general-purpose registers (x0-x31)
- Special registers:
    - x0 (hardwired zero)
    - x1 (ra - return address)
    - x2 (sp - stack pointer)
- Stack memory implementation
- Basic I/O operations through ecall

## Supported Instructions

**Arithmetic Logic Unit (ALU) Operations:**

- `add`, `addi`, `sub`, `div`, `mul`

**Load Operations:**

- `lw` (load word)
- `lh` (load half-word)
- `lb` (load byte)
- `li` (load immediate)

**Store Operations:**

- `sw` (store word)
- `sh` (store half-word)
- `sb` (store byte)

**Branch Operations:**

- `bne` (branch not equal)
- `beq` (branch equal)
- `bgt` (branch greater than)
- `bge` (branch greater or equal)
- `blt` (branch less than)
- `ble` (branch less or equal)

**Function Calls:**

- `call`, `ret`, `jalr`, `j`

**System Calls (ecall):**

- `print_int` (ecall 1)
- `print_char` (ecall 11)
- `exit` (ecall 10)

## Example Programs

The project includes several test programs that demonstrate the emulator's capabilities:

- `sum_iter.s` - Calculates the sum of numbers from 1 to n (iterative)
- `recursive_factorial.s` - Calculates factorial recursively
- `store_load.s` - Demonstrates various load/store operations
- `fib.s` - Calculates Fibonacci numbers recursively
- `abc.s` - Prints the alphabet and numbers using ecall

## How to Run

### Prerequisites

- Java Development Kit (JDK) installed
- All `.java` and `.s` files in the same directory

### Compilation

Compile all Java files with:

```bash
javac *.java
```
### Running the Program

```bash
java Main.java (name of the .s file)
```
