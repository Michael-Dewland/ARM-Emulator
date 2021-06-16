package com.michaelcode;

import java.util.*;

public class Emulator {

    // 64 lines of main memory are reserved for instructions
    // next 16 lines are reserved for
    private ArrayList<String> programMemory = new ArrayList<>();
    private ArrayList<Integer> mainMemory = new ArrayList<>();
    private ArrayList<Integer> registers = new ArrayList<>();
    private boolean halted = false;

    private int PC = 0;
    private String SR = "";

    public static void main(String[] args) {
        // no initialisation needed
    }

    public void initialise() {
        clear_main_memory();
        setup_registers();
    }

    private void setup_registers() {
        for (int i = 0; i < 13; i++) {
            registers.add(0);
        }
    }
    public void display_registers() {
        System.out.println("Reg   Val");
        for (int i = 0; i < registers.size(); i++) {
            System.out.print("R" + i);
            for (int j = 0; j < 2 - Integer.toString(i).length(); j++) {
                System.out.print(" ");
            }
            System.out.println(" :   " + registers.get(i));
        }
    }

    public void increment() {
        PC++;
    }

    public void set_instructions(ArrayList<String> instructions) {
        clear_main_memory();
        programMemory = instructions;
    }

    public void clear_main_memory() {
        programMemory.clear();
    }

    public void execute_next() {
        // current_instruction not converted to lower as branch instructions are case-sensitive
        String current_instruction = programMemory.get(PC).trim();

        if (current_instruction.equals("")) {
            return;
        }

        System.out.println("Running instruction [" + current_instruction.toLowerCase() + "]");

        // see if branch operation

        if (current_instruction.toLowerCase().startsWith("b ")) {
            B(current_instruction.toLowerCase().substring(2));
        } else if (current_instruction.toLowerCase().charAt(0) == 'b') {

            switch (current_instruction.toLowerCase().substring(0, 4)) {
                case "beq " -> B("eq", current_instruction.substring(4));
                case "bne " -> B("ne", current_instruction.substring(4));
                case "bgt " -> B("gt", current_instruction.substring(4));
                case "blt " -> B("lt", current_instruction.substring(4));
            }

        } else {

            // normal instructions

            String[] operands = current_instruction.toLowerCase().substring(4).split(",");

            for (int i = 0; i < operands.length; i++) {
                operands[i] = operands[i].trim().toLowerCase();
            }

            switch (current_instruction.toLowerCase().substring(0, 4)) {
                case "ldr " -> LDR(get_value(operands[0]), get_value(operands[1]));
                case "str " -> STR(get_value(operands[0]), get_value(operands[1]));

                case "add " -> ADD(get_register_num(operands[0]), get_register_num(operands[1]), operands[2]);
                case "sub " -> SUB(get_register_num(operands[0]), get_register_num(operands[1]), operands[2]);

                case "mov " -> MOV(get_register_num(operands[0]), operands[1]);
                case "cmp " -> CMP(get_register_num(operands[0]), operands[1]);

                case "and " -> AND(get_register_num(operands[0]), get_register_num(operands[1]), operands[2]);
                case "orr " -> ORR(get_register_num(operands[0]), get_register_num(operands[1]), operands[2]);
                case "eor " -> EOR(get_register_num(operands[0]), get_register_num(operands[1]), operands[2]);
                case "mvn " -> MVN(get_register_num(operands[0]), operands[1]);

                case "lsl " -> LSL(get_register_num(operands[0]), get_register_num(operands[1]), operands[2]);
                case "lsr " -> LSR(get_register_num(operands[0]), get_register_num(operands[1]), operands[2]);
            }

            if (current_instruction.toLowerCase().startsWith("halt ") || current_instruction.toLowerCase().equals("halt")) {
                HALT();
            }
        }
    }

    private int get_value(String operand) {
        // get integer value of #n or Rn
        if (operand.charAt(0) == '#') {
            operand = operand.substring(1);
        } else if (operand.charAt(0) == 'r') {
            operand = Integer.toString(registers.get(Integer.parseInt(operand.substring(1))));
        }

        return Integer.parseInt(operand);
    }

    private int get_register_num(String operand) {
        return Integer.parseInt(operand.trim().substring(1));
    }

    private void jump_to(int line) {
        PC = line;
    }

    // instructions

    private void LDR(int Rd, int memory_ref) {
        registers.set(Rd, mainMemory.get(memory_ref));
    }
    private void STR(int memory_ref, int Rd) {
        mainMemory.set(memory_ref, registers.get(Rd));
    }

    private void MOV(int Rd, String operand2) {
        registers.set(Rd, get_value(operand2));
    }

    private void ADD(int Rd, int Rn, String operand2) {
        registers.set(Rd, registers.get(Rn) + get_value(operand2));
    }
    private void SUB(int Rd, int Rn, String operand2) {
        registers.set(Rd, registers.get(Rn) - get_value(operand2));
    }

    private void CMP(int Rn, String operand2) {
        // base case
        update_sr("gt");

        // compare values
        if (get_value("r" + Rn) == get_value(operand2)) {
            update_sr("eq");
        } else if (get_value("r" + Rn) < get_value(operand2)) {
            update_sr("lt");
        }

    }
    private void update_sr(String status) {
        SR = status;
    }

    private void B(                  String label) {
        jump_to(programMemory.indexOf(label + ":"));
    }
    private void B(String condition, String label) {
        switch (condition) {
            case "eq" -> {if (SR.equals("eq")) {B(label);}}
            case "ne" -> {if (SR.equals("gt") || SR.equals("lt")) {B(label);}}
            case "gt" -> {if (SR.equals("gt")) {B(label);}}
            case "lt" -> {if (SR.equals("lt")) {B(label);}}
        }
    }

    // Logic components
    private void AND(int Rd, int Rn, String operand2) {
        registers.set(Rd, registers.get(Rn) & get_value(operand2));
    } // AND
    private void ORR(int Rd, int Rn, String operand2) {
        registers.set(Rd, registers.get(Rn ) | get_value(operand2));
    } // OR
    private void EOR(int Rd, int Rn, String operand2) {
        registers.set(Rd, registers.get(Rn) ^ get_value(operand2));
    } // XOR
    private void MVN(int Rd,         String operand2) {
        registers.set(Rd, ~get_value(operand2));
    } // NOT

    private void LSL(int Rd, int Rn, String operand1) {
        registers.set(Rd, registers.get(Rn) << get_value(operand1));
    } // LEFT SHIFT
    private void LSR(int Rd, int Rn, String operand1) {
        registers.set(Rd, registers.get(Rn) >> get_value(operand1));
    } // RIGHT SHIFT

    private void HALT() {
        halted = true;
    } // HALT PROGRAM

    public boolean is_halted() {
        return halted;
    }
}