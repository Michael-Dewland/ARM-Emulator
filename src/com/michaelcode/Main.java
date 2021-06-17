package com.michaelcode;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        // B<condition> <label> simplified to B<condition> <line No.>
        // label removed from code

        /*

sample code

    a = 0
    for i = 0 to 10 step 1
        a = a + 1
        if a = 4
            break

    for i = 0 to 3 step 1
        b = b + 1
        if b = 4
            break

translated to:

    main:
        mov r0, #0
        mov r1, #0

    start_for_1:
        add r0, r0, #1

        cmp r0, #4
        beq end_for_1

        add r1, r1, #1
        cmp r1, #9
        blt start_for_1

    end_for_1:
        mov r1, #0
        mov r2, #0
        b start_for_2

    start_for_2:
        add r2, r2, #1

        cmp r2, #4
        beq end_for_2

        add r1, r1, #1
        cmp r1, #2
        blt start_for_2

    end_for_2:
        halt

         */

        String source_code = """

b main

main:
    mov r0, #0
    mov r1, #0
    b start_for_1

end_for_1:
    mov r1, #0
    mov r2, #0
    b start_for_2
    

start_for_1:
    add r0, r0, #1
    
    cmp r0, #4
    beq end_for_1
    
    add r1, r1, #1
    cmp r1, #9
    beq end_for_1
    b start_for_1
    
start_for_2:
    add r2, r2, #1
    
    cmp r2, #4
    beq end_for_2
    
    add r1, r1, #1
    cmp r1, #2
    beq end_for_2
    bgt end_for_2
    b start_for_2
    
end_for_2:
    halt
                """;

        ArrayList<String> instructions = new ArrayList<>(Arrays.asList(source_code.split("\n")));

        // create instance and initialise
        Emulator em = new Emulator();

        em.set_instructions(instructions);

        //em.display_registers();

        while (!em.is_halted()) {
            em.execute_next();
            em.increment();
            //int PC = em.get_program_counter();
            //System.out.println(" PC: " + PC);
        }

        em.display_registers();
        System.out.println();
        em.display_main_memory();
    }
}
