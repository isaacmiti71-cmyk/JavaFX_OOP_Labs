import random
number = (random.randint(1,50))
guess = 0
attempts = 0
print("Guess the number between 1 and 50")
while guess != number:
    guess = int(input("Enter your guess:"))
    attempts += 1
    if guess < number:
        print("Too high!")
    elif guess > number:
        print("Too high!")
    else:
        print(f"Correct! You guessed it in{attempts} tries.")