# Algorithms

Algorithms and applications implemented in java. They use libraries found [here](http://algs4.cs.princeton.edu/code/).

## Usage

Navigate to bin directories for each project and run the commands described below in the terminal.

### SeamCarving

Implemented using graph searching algorithms to find shortest paths, this program resizes images without changing the scale of their most important features. it finds seams in the image that can be removed with a minimal effect on the appearance of the image.

```sh
java ResizeDemo ../test/beach_ball.png 250 0
# this will create a version of this image that is 250 pixels wider and 150 pixels shorter


java ResizeDemo ../test/ipanema_surfers.jpg 100 150
# this will create a version of this image that is 100 pixels wider and 150 pixels taller


java ResizeDemo ../test/beach_ball.png 0 0 230 20 255 45
# this will not resize the image, but it will remove the rectangular region from (230, 20) to (255, 45). this tool is available in photoshop =)
```

### WordSearch

Implemented using a very cool data structure called a ternary search trie, which is ideal for string searching and string completion.

```sh
java Boggle 4 ../web2.txt
# creates a 4x4 grid of letters and then find all of the english words in this grid according to the rules of Boggle

java Boggle 40 ../web2.txt
# creates a 40x40 grid of letters and then find all of the english words in this grid according to the rules of Boggle

java WordSearch 40 ../web2.txt
# creates a 40x40 grid of letters and then find all of the english words in this grid according to the rules of word search (sopa de letras). there are _MUCH_ fewer possibilities with these rules
```

### 8Puzzle

Solves the famous 8 puzzle and animates the solution. implemented with a heap-based priority queue.

`java AnimatedSolver ../test/puzzle35.txt`

### MarkovRecognition

This program builds 2 markov models from text files, and then analyzes a third text file to determine if it is more similar to the first model or to the second model, using bayesian analysis. it accepts 4 arguments -- the first is the order of the markov model, the second is the text file used to build model 1, the third is the text file used to build model 2, and the fourth is the text file being compared to the models

```sh
java BestModel 5 ../test/obama1+2.txt ../test/mccain1+2.txt ../test/obama3-00.txt
# Model 1 : ../test/obama1+2.txt
# Model 2 : ../test/mccain1+2.txt

../test/obama3-00.txt  -2.903367  -3.1755292  0.2721622
# Input more similar to model 1

java BestModel 5 ../test/obama1+2.txt ../test/kerry1+2.txt ../test/kerry3-00.txt
# Model 1 : ../test/obama1+2.txt
# Model 2 : ../test/kerry1+2.txt

../test/kerry3-00.txt  -3.2623205  -3.075262  0.18705845
# Input more similar to model 2
```

### Practice

This folder contains implementations of many algorithms. one interesting example is IntegerToString. this program accepts an integer input and converts it to an English language string

```sh
java IntegerToString 80039910019317
# eighty trillion, thirty nine billion, nine hundred ten million, nineteen thousand, three hundred seventeen

# another example is CoinSumsPrint. this algorithm uses memoization to find all of the unique ways to sum a collection of coins to a target value. for example
java CoinSumsPrint 1 5 10 25 2173
# will find all the ways get $21.73 from pennies (1 cent), nickels (5 cents), dimes (10 cents) and quarters (25 cents). there are 1405206
```

### BurrowsWheeler

This program has several classes that perform different kinds of encoding on an input file. for english language text, this type of encoding works very well for compressing files. **You must type the following text** into the terminal for it to work correctly -- you can't copy and paste it into the terminal. This text takes the input file and performs Burrows Wheeler encoding, Move to Front encoding, and Huffman encoding on it. It compresses Moby Dick by about 66%

`java BurrowsWheeler - < ../mobydick.txt | java MoveToFront - | java Huffman - > ../mobydick.txt.bwt.mtf.huf`

To uncompress the file, decode the file using the reverse procedure that was used to encode it. Type the following code into the terminal. First it will be Huffman decoded, then Move to Front decoded, then Burrows Wheeler decoded, and the output file will be identical to the input file.

`java Huffman + < ../mobydick.txt.bwt.mtf.huf | java MoveToFront + | java BurrowsWheeler + > ../mobydick_uncompressed.txt`

Try using the unix utility "diff" to compare the files, and one sees that they are identical.

`diff mobydick.txt mobydick_uncompressed.txt`

### RSA

This program is made possible by the classes XP and XPA, which are exntended precision integers. these integers can be initiliazed to have thousands of digits, and various mathematical operations (most importantly modular exponentiation) can be performed on them.

```sh
# encrypt
# open the file input.txt and insert your message before the first 5 commas ,,,,,
java RSAA - < input.txt > encrypted.txt

# decrypt
# 1) copy the text from encrypted.txt
# 2) open the file cypher.txt and paste the copied text from encrypted.txt before the 5 five commas,,,,,
java RSAA + < cypher.txt > decrypted.txt

```

It is crucial not to change any of the integers or commas in input.txt and cypher.txt. The commas are for separating the values so that the RSAA can parse the integers, and the big integers work together to allow secure encryption and decryption.

If you follow the above steps, you will notice that the decrypted.txt matches the original message that was placed in input.txt before the first 5 commas.
