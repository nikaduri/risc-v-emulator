import java.util.*;

public class RiscEmulator {

    // For registers I just use a fixed size array
    private static int[] registers;
    private static final int REGISTER_COUNT = 32;
    private String fileName;
    private static FileInterpreter file;

    // To emulate Stack I use Byte Vector
    private static ArrayList<Byte> stack;
    private static HashMap<String, Integer> labels;

    private static boolean programDone;


    // This class executes the code right after it gets created.
    public RiscEmulator(String fileName) {
        registers = new int[REGISTER_COUNT];
        stack = new ArrayList<>();
        programDone = false;
        // x0 register
        registers[0] = 0;
        // x1 ra register, initializing as -1 because the program stops if ra is negative and ret is called.
        registers[1] = -1;
        // x2 sp register
        registers[2] = 0;
        this.fileName = fileName;
        file = new FileInterpreter(fileName);
        labels = new HashMap<>();
        getLabels();
    }

    ///////////////////////////////////////
    ///////// CODE EXECUTION PART /////////
    ///////////////////////////////////////


    public void executeCode() {
        String instruction;
        while (!(instruction = file.nextInstruction()).isEmpty()) {
            executeInstruction(instruction);
            if (programDone) {
                break;
            }
        }
    }


    // This method runs before the code execution starts and puts all the label names
    // and the line counts where they are at in the hashmap.
    private static void getLabels() {
        String label;
        while ((label = file.nextLabel()) != null) {
            label = label.trim();
            labels.put(label.substring(0, label.length() - 1), file.getProgramCounter());
        }
        file.setProgramCounter(0);
    }


    // This function executes a single instruction
    // It checks if it is ALU/STORE/LOAD type of operation or something else like ret
    // It uses Tokenizer to give needed args as an Arraylist to instruction methods.
    // It first executes the instruction and the resizes the stack if stack pointer was adjusted.
    private static void executeInstruction(String instruction) {
        instruction = instruction.trim();
        instruction = removeComments(instruction);
        if (instruction.isEmpty()) {
            return;
        }
        StringTokenizer tok = new StringTokenizer(instruction, ", ");
        String nextToken = tok.nextToken();
        if (tok.countTokens() == 0) {
            if (nextToken.equals("ret")) {
                // If ra is negative, that means the program should stop.
                if (registers[1] < 0) {
                    programDone = true;
                } else {
                    file.setProgramCounter(registers[1]);
                }
            } else if (!labels.containsKey(instruction.substring(0,instruction.length() - 1))){
                // If the instruction is 1 word and not ret or a label, it's ecall
                ecall();
            }
            return;
        }
        ArrayList<String> args = new ArrayList<>();
        while (tok.hasMoreTokens()) {
            args.add(tok.nextToken());
        }
        callFunction(nextToken, args);
        resizeStack();
    }

    // This just makes a function call from the name of the function as a String.
    // It is basically a list of all the instructions this emulator knows
    private static void callFunction(String operation, ArrayList<String> args) {
        switch (operation) {
            case "addi":
                addi(args);
                return;
            case "add":
                add(args);
                return;
            case "sub":
                sub(args);
                return;
            case "div":
                div(args);
                return;
            case "mul":
                mul(args);
                return;
            case "li":
                li(args);
                return;
            case "lw":
                lw(args);
                return;
            case "lh":
                lh(args);
                return;
            case "lb":
                lb(args);
                return;
            case "sw":
                sw(args);
                return;
            case "sh":
                sh(args);
                return;
            case "sb":
                sb(args);
                return;
            case "j":
                jump(args);
                return;
            case "bge":
                bge(args);
                return;
            case "bgt":
                bgt(args);
                return;
            case "ble":
                ble(args);
                return;
            case "blt":
                blt(args);
                return;
            case "beq":
                beq(args);
                return;
            case "bne":
                bne(args);
                return;
            case "call":
                call(args);
                return;
            case "jalr":
                jalr(args);
                return;
            case "mv":
                mv(args);
                return;
        }
    }


    // This method resizes the stack only if the stack pointer was adjusted.
    private static void resizeStack() {
        int requiredSize = Math.abs(registers[2]);

        while (stack.size() < requiredSize) {
            stack.add((byte) 0);
        }

        while (stack.size() > requiredSize) {
            stack.remove(stack.size() - 1);
        }
    }


    /////////////////////////////////////////////////////////
    ///////////////// INSTRUCTION METHODS ///////////////////
    /////////////////////////////////////////////////////////


    /////////////////
    ////// ALU //////
    /////////////////

    private static void addi(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        int value = Integer.parseInt(args.get(2));
        registers[Integer.parseInt(register1.substring(1))] = registers[Integer.parseInt(register2.substring(1))] + value;
    }

    private static void add(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        String register3 = args.get(2);
        registers[Integer.parseInt(register1.substring(1))] = registers[Integer.parseInt(register2.substring(1))] + registers[Integer.parseInt(register3.substring(1))];
    }

    private static void div(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        String register3 = args.get(2);
        registers[Integer.parseInt(register1.substring(1))] = registers[Integer.parseInt(register2.substring(1))] / registers[Integer.parseInt(register3.substring(1))];
    }

    private static void sub(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        String register3 = args.get(2);
        registers[Integer.parseInt(register1.substring(1))] = registers[Integer.parseInt(register2.substring(1))] - registers[Integer.parseInt(register3.substring(1))];
    }

    private static void mul(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        String register3 = args.get(2);
        registers[Integer.parseInt(register1.substring(1))] = registers[Integer.parseInt(register2.substring(1))] * registers[Integer.parseInt(register3.substring(1))];
    }


    //////////////////
    //// BRANCHES ////
    //////////////////
    private static void bne(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        String label = args.get(2);
        if (registers[Integer.parseInt(register1.substring(1))] != registers[Integer.parseInt(register2.substring(1))]) {
            file.setProgramCounter(labels.get(label));
        }
    }

    private static void beq(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        String label = args.get(2);
        if (registers[Integer.parseInt(register1.substring(1))] == registers[Integer.parseInt(register2.substring(1))]) {
            file.setProgramCounter(labels.get(label));
        }
    }

    private static void blt(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        String label = args.get(2);
        if (registers[Integer.parseInt(register1.substring(1))] < registers[Integer.parseInt(register2.substring(1))]) {
            file.setProgramCounter(labels.get(label));
        }
    }

    private static void ble(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        String label = args.get(2);
        if (registers[Integer.parseInt(register1.substring(1))] <= registers[Integer.parseInt(register2.substring(1))]) {
            file.setProgramCounter(labels.get(label));
        }
    }

    private static void bgt(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        String label = args.get(2);
        if (registers[Integer.parseInt(register1.substring(1))] > registers[Integer.parseInt(register2.substring(1))]) {
            file.setProgramCounter(labels.get(label));
        }

    }

    private static void bge(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        String label = args.get(2);
        if (registers[Integer.parseInt(register1.substring(1))] >= registers[Integer.parseInt(register2.substring(1))]) {
            file.setProgramCounter(labels.get(label));
        }
    }


    /////////////////
    ///// STORE /////
    /////////////////
    private static void sw(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);

        // Tokenizer for separating the offset and register
        StringTokenizer tok = new StringTokenizer(register2, "()");
        int offset = Integer.parseInt(tok.nextToken());
        register2 = tok.nextToken();

        int baseRegister = registers[Integer.parseInt(register2.substring(1))];

        int word = registers[Integer.parseInt(register1.substring(1))];

        // Store 4 bytes in the stack from the register

        int address = Math.abs(baseRegister) - offset - 1;


        stack.set(address, (byte) (word & 0xFF));
        stack.set(address - 1, (byte) ((word >> 8) & 0xFF));
        stack.set(address - 2, (byte) ((word >> 16) & 0xFF));
        stack.set(address - 3, (byte) ((word >> 24) & 0xFF));
    }

    // Same as sw but only store 2 bytes.
    private static void sh(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);

        StringTokenizer tok = new StringTokenizer(register2, "()");
        int offset = Integer.parseInt(tok.nextToken());
        register2 = tok.nextToken();

        int baseRegister = registers[Integer.parseInt(register2.substring(1))];

        int word = registers[Integer.parseInt(register1.substring(1))];

        int address = Math.abs(baseRegister) - offset - 1;


        stack.set(address, (byte) (word & 0xFF));
        stack.set(address - 1, (byte) ((word >> 8) & 0xFF));
    }

    // Same as sw but only store 1 byte.
    private static void sb(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);

        StringTokenizer tok = new StringTokenizer(register2, "()");
        int offset = Integer.parseInt(tok.nextToken());
        register2 = tok.nextToken();

        int baseRegister = registers[Integer.parseInt(register2.substring(1))];

        int word = registers[Integer.parseInt(register1.substring(1))];

        int address = Math.abs(baseRegister) - offset - 1;


        stack.set(address, (byte) (word & 0xFF));
    }


    /////////////////
    ////// LOAD /////
    /////////////////
    private static void li(ArrayList<String> args) {
        String register = args.get(0);
        int value = Integer.parseInt(args.get(1));
        registers[Integer.parseInt(register.substring(1))] = value;
    }

    private static void lw(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);

        // Using tokenizer to get the offset and register
        StringTokenizer tok = new StringTokenizer(register2, "()");
        int offset = Integer.parseInt(tok.nextToken());
        register2 = tok.nextToken();

        int baseRegister = registers[Integer.parseInt(register2.substring(1))];

        // Get the value from the stack
        int address = Math.abs(baseRegister) - offset - 1;

        int word = 0;
        for (int j = 0; j < 4; j++) {
            word |= (stack.get(address - j) & 0xFF) << (j * 8);
        }

        registers[Integer.parseInt(register1.substring(1))] = word;
    }

    // Same as lw but only gets two bytes.
    private static void lh(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);

        StringTokenizer tok = new StringTokenizer(register2, "()");
        int offset = Integer.parseInt(tok.nextToken());
        register2 = tok.nextToken();

        int baseRegister = registers[Integer.parseInt(register2.substring(1))];

        int address = Math.abs(baseRegister) - offset - 1;

        int word = 0;
        for (int j = 0; j < 2; j++) {
            word |= (stack.get(address - j) & 0xFF) << (j * 8);
        }

        registers[Integer.parseInt(register1.substring(1))] = word;
    }

    // Same as lw but only gets one byte.
    private static void lb(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);

        StringTokenizer tok = new StringTokenizer(register2, "()");
        int offset = Integer.parseInt(tok.nextToken());
        register2 = tok.nextToken();

        int baseRegister = registers[Integer.parseInt(register2.substring(1))];

        int address = Math.abs(baseRegister) - offset - 1;

        int word = stack.get(address);

        registers[Integer.parseInt(register1.substring(1))] = word;
    }

    // Move Instruction
    private static void mv(ArrayList<String> args) {
        String register1 = args.get(0);
        String register2 = args.get(1);
        registers[Integer.parseInt(register1.substring(1))] =
                registers[Integer.parseInt(register2.substring(1))];
    }


    // This just makes the FileInterpreter jump to another line.
    private static void jump(ArrayList<String> args) {
        file.setProgramCounter(labels.get(args.get(0)));
    }


    // Call works exactly like it should. It overwrites the ra and then jumps.
    private static void call(ArrayList<String> args) {
        registers[1] = file.getProgramCounter();
        jump(args);
    }

    // This is just call but the function is directly the address of the label
    // instead of the label name
    private static void jalr(ArrayList<String> args) {
        registers[1] = file.getProgramCounter();
        String register = args.get(0).substring(1);
        int address = registers[Integer.parseInt(register)];
        file.setProgramCounter(address);
    }

    // ecall logic
    private static void ecall() {
        switch (registers[10]) {
            case 1:
                printInt();
                return;
            case 10:
                programDone = true;
                return;
            case 11:
                printChar();
                return;
        }
    }

    private static void printChar() {
        System.out.print((char) registers[11]);
    }


    private static void printInt() {
        System.out.print(registers[11]);
    }

    public static String removeComments(String a) {
        int idx = a.indexOf("#");
        if (idx == -1) {
            return a;
        }
        return a.substring(0, idx);
    }

    public int registerValue(int register) {
        return registers[register];
    }

    public int spValue() {
        return registers[2];
    }

    public int raValue() {
        return registers[1];
    }

    public int pcValue() {
        return file.getProgramCounter();
    }
}