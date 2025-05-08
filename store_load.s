.text

	# Initialize stack pointer
	addi sp, sp, -4

	# Store a word on the stack
	li x10, 305419896  # Load decimal value 305419896 (0x12345678 in hex) into x10
	sw x10, 0(sp)      # Store x10 at address 0(sp)

	# Load a half-word from the stack
	lh x11, 0(sp)      # Load lower half-word (22136 in decimal, 0x5678 in hex)
	lh x12, 2(sp)      # Load upper half-word (4660 in decimal, 0x1234 in hex)

	# Load a byte from the stack
	lb x13, 0(sp)      # Load lower byte (120 in decimal, 0x78 in hex) 
	lb x14, 1(sp)      # Load next byte (86 in decimal, 0x56 in hex)
	lb x15, 2(sp)      # Load next byte (52 in decimal, 0x34 in hex)
	lb x16, 3(sp)      # Load upper byte (18 in decimal, 0x12 in hex)

	# Restore stack pointer
	addi sp, sp, 4

	# Expected results:
	# x11 = 22136  (0x5678 in hex)
	# x12 = 4660   (0x1234 in hex)
	# x13 = 120    (0x78 in hex)
	# x14 = 86     (0x56 in hex)
	# x15 = 52     (0x34 in hex)
	# x16 = 18     (0x12 in hex)
