.text
	
	addi sp, sp, -8
	li x11, 7  # To calculate n!, write n here.
	sw x11, 0(sp)
	sw ra, 4(sp)
	call factorial
	lw ra, 4(sp)
	addi sp, sp, 8
	ret

	factorial:

	lw x10, 0(sp)
	li x11, 1
	beq x10, x11, base_case
	beq x10, x0, base_case

	lw x11, 0(sp) #n
	addi sp, sp, -8
	addi x13, x11, -1
	sw x13, 0(sp)
	sw ra, 4(sp)
	call factorial
	lw ra 4(sp)
	addi sp, sp, 8
	lw x11, 0(sp) #n
	mul x10, x10, x11
	ret

	base_case:
	addi x10, x0, 1
	ret
