package com.michaelcode;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        String source_code = """
                b loop
                
                end:
                mov r1, #69420
                halt
                
                loop:
                
                add r0, r0, #1
                
                cmp r0, #5
                blt loop
                b end
                """;

        ArrayList<String> instructions = new ArrayList<>(Arrays.asList(source_code.split("\n")));

        Emulator em = new Emulator();

        while (instructions.size() < 64) {
            instructions.add(" ");
        }

        em.initialise();
        em.set_instructions(instructions);

        em.display_registers();

        while (!em.is_halted()) {
            em.execute_next();
            em.increment();
        }

        em.display_registers();
    }
}
