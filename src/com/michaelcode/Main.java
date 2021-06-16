package com.michaelcode;

import java.util.*;

public class Main {
    public static void main(String[] args) {



        // B<condition> <label> simplified to B<condition> <line No.>
        // label removed from code
        String source_code = """
B branch_to
mov r0, #1
branch_to:
mov r0, #5
halt
                """;

        ArrayList<String> instructions = new ArrayList<>(Arrays.asList(source_code.split("\n")));

        Emulator em = new Emulator();

        em.initialise();
        em.set_instructions(instructions);

        //em.display_registers();

        while (!em.is_halted()) {
            em.execute_next();
            em.increment();
        }

        em.display_registers();
        em.display_main_memory();

    }
}
