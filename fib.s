.text

	addi sp sp -8
	li x11 10 # fib(n) is calculated and n is passed from here
	sw x11 0(sp)
	sw ra 4(sp)
	call fib
	lw ra 4(sp)
	addi sp sp 8
	ret




	fib:

	lw x11 0(sp)
	li x12 1
	beq x11 x0 base_case1
	beq x11 x12 base_case2

	lw x11 0(sp)
	addi x11 x11 -1
	addi sp sp -8
	sw x11 0(sp)
	sw ra 4(sp)

	call fib

	lw ra 4(sp)
	addi sp sp 4

	sw x10 0(sp) # fib(n-1)

	lw x11 4(sp)
	addi x11 x11 -2
	addi sp sp -8
	sw x11 0(sp)
	sw ra 4(sp)
	call fib
	lw ra 4(sp)
	lw x12 8(sp)
	add x10 x10 x12
	addi sp sp 12

	ret

	base_case1:
	li x10 0
	ret

	base_case2:
	li x10 1
	ret