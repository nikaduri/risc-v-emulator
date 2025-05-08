.text

	addi sp sp -8
	li x11 99  # The code is summing numbers from 1 to whatever you write in x11 here.
	sw x11 0(sp)
	sw ra 4(sp)
	call sum_iter
	lw ra 4(sp)
	addi sp sp 8
	ret

	sum_iter:
	
	addi sp sp -8
	li x10 1
	sw x10 0(sp) # int i = 1
	sw x0 4(sp) # int ans = 0
	
	for_loop:
	
	lw x10 8(sp) #arg
	lw x11 0(sp) #int i
	bgt x11 x10 ending
	lw x12 4(sp)
	lw x11 0(sp)
	add x12 x12 x11
	sw x12 4(sp)
	addi x11 x11 1
	sw x11 0(sp)
	
	j for_loop
	
	ending:
	
	lw x11 4(sp)
	add x10 x0 x11
	addi sp sp 8
	ret
	
	
	
	
