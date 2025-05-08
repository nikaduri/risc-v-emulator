.text
	main:
		# Allocate stack space
		addi sp, sp, -8       # Make space for 2 variables
		li x10, 97            # ASCII 'a'
		li x11, 123           # ASCII after 'z' (ASCII '{')
		sw x10, 0(sp)         # Store start character on stack
		sw x11, 4(sp)         # Store end character on stack

	print_chars:
		lw x11, 0(sp)         # Load current character
		li x10, 11            # ECALL 11 - print_char
		ecall
		lw x10, 0(sp)         # Load current character again
		addi x10, x10, 1      # Increment ASCII value
		sw x10, 0(sp)         # Store updated character
		lw x11, 4(sp)         # Load end character
		blt x10, x11, print_chars  # If not past 'z', repeat

		# Print numbers '0' to '9' using print_int
		li x10, 0             # Start at 0
		li x11, 10            # Stop at 10 (one past 9)
		sw x10, 0(sp)         # Store start number
		sw x11, 4(sp)         # Store end number

	print_numbers:
		lw x11, 0(sp)         # Load current number
		li x10, 1             # ECALL 1 - print_int
		ecall
		lw x10, 0(sp)         # Load current number again
		addi x10, x10, 1      # Increment number
		sw x10, 0(sp)         # Store updated number
		lw x11, 4(sp)         # Load end number
		blt x10, x11, print_numbers  # If not past 9, repeat

		# Deallocate stack space
		addi sp, sp, 8

		# Exit program
		li x10, 10            # ECALL 10 - exit
		ecall
