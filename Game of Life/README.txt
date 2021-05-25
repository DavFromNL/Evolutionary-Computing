Instructions:
> compile in prompt/terminal/console with:

		javac *.java


> run the program with java Life {# of iterations} {size of grid} {desired pattern}
> example:
		java Life 500 50 R

=====================================================================================
Important:

> Works well on windows 10 but I added photos of it working just in case it doesn't like Linux
=====================================================================================
Functionality:

> takes number of iterations
> takes size of square grid
> takes 'R', 'L', or 'P'
> runs game of life
> wraps around
=====================================================================================
Notes:

> will break if you don't enter an integer in the first place
> will break if you don't enter a counting number in the second place
> will default to random configuration if you don't enter "P" or "L"
> will break if you choose "P" or "L" and don't enter a big enough grid size
> sleep function added between every refresh to make it easier to follow
=====================================================================================
Funky things:

> Loads a random configuration with squares 1/10th the size at the beginning. Increase the sleep if you want to see it
=====================================================================================












