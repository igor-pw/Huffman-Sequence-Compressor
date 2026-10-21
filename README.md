# Huffman Sequence Compressor

A lossless file compressor written in pure Java that extends classic Huffman coding from single bytes to **variable-length byte sequences** (n-grams of up to 1024 bytes). Every data structure it relies on — the hash map, the priority queue (binary heap) and the bit-level I/O — is implemented from scratch, without `java.util` collections.

Built as a project for the *Algorithms and Data Structures* course at the Faculty of Electrical Engineering, Warsaw University of Technology.

## Features

- Compresses and decompresses **any file type** (text, images, binaries).
- Configurable maximum sequence length `-l` (1–1024). With `-l 1` the program behaves like a standard byte-level Huffman coder; higher values let it encode frequently repeated phrases with a single code.
- Cost-based filtering of sequences: an n-gram is kept in the dictionary only if the bits it saves outweigh the cost of storing it in the header.
- Greedy longest-match encoding of the input stream.
- Compact header: the Huffman tree is serialized bit by bit, so no frequency table has to be stored.
- Streaming I/O with 64 KiB buffers, so large files are never loaded into memory at once.

## How it works

### Compression

1. **Counting** – `FileReader` scans the input and counts every single byte and every sequence of length 2…`l` in a custom open-addressing `HashMap` (double hashing, prime table size). To bound memory use, statistics are gathered from at most the first 10 MiB of input and at most ~1,000,000 distinct multi-byte sequences. If the scan was cut short, all 256 single bytes are added to the dictionary so that any byte can still be encoded.
2. **Filtering** – for each sequence the gain `count × (length − 1)` is compared with a threshold derived from its header cost. Unprofitable sequences are dropped, the table is rebuilt at a smaller prime size, and the counts of single bytes are corrected for the occurrences already covered by longer sequences.
3. **Tree building** – all remaining entries go into a binary-heap `PriorityQueue`, and the Huffman tree is built by repeatedly merging the two least frequent nodes (`Dictionary`).
4. **Code assignment** – a DFS over the tree assigns each leaf its bit code and stores it back into the hash map.
5. **Encoding** – `FileWriter` walks through the input and, at each position, looks for the longest sequence present in the dictionary, then emits its code. Buffer boundaries are handled by merging the tail of the previous block with the head of the next one.

### Decompression

`ByteReader` rebuilds the Huffman tree from the header, then reads the remaining bits and walks the tree from the root, writing out the sequence stored in each leaf it reaches.

### Output file format

| Part | Size | Description |
|------|------|-------------|
| Padding | 3 bits | Number of unused bits in the last byte of the file |
| Tree | variable | Pre-order dump of the tree: `0` = internal node, `1` = leaf followed by the sequence length (`⌊log₂ l⌋ + 1` bits, only when `l > 1`) and the sequence bytes |
| Data | variable | Concatenated Huffman codes of the input |

The value of `l` is **not** stored in the file, so the same `-l` must be passed when decompressing.

## Requirements

- Java 21+
- Maven 3.8+

## Build

```bash
mvn clean package
```

This compiles the project, runs the unit tests and produces an executable jar at `target/AiSD2025ZEx5.jar`.

To skip the tests:

```bash
mvn clean package -DskipTests
```

## Usage

```bash
java -jar target/AiSD2025ZEx5.jar -m <comp|decomp> -s <input> -d <output> [-l <length>]
```

| Flag | Required | Default | Description |
|------|----------|---------|-------------|
| `-m` | no | `comp` | Mode: `comp` (compress) or `decomp` (decompress) |
| `-s` | yes | – | Path to the input file |
| `-d` | yes | – | Path to the output file (the parent directory must exist) |
| `-l` | no | `1` | Maximum length of an encoded byte sequence (1–1024); must be identical for compression and decompression |

### Examples

Compress a text file using sequences of up to 7 bytes:

```bash
java -jar target/AiSD2025ZEx5.jar -m comp -s data/potop.txt -d output/potop7.comp -l 7
```

Decompress it back:

```bash
java -jar target/AiSD2025ZEx5.jar -m decomp -s output/potop7.comp -d output/potop7_decomp.txt -l 7
```

Verify that the round-trip is lossless:

```bash
# Linux / macOS
cmp data/potop.txt output/potop7_decomp.txt

# Windows
fc /b data\potop.txt output\potop7_decomp.txt
```

## Sample results

| Input | Size | `-l` | Compressed | Ratio |
|-------|------|------|------------|-------|
| Polish novel (*Potop*, plain text) | 3.11 MB | 7 | 1.42 MB | 45.6 % |
| Short text | 2.08 KB | 1 | 1.35 KB | 64.7 % |
| Short text | 2.08 KB | 4 | 1.55 KB | 74.7 % |
| JPEG image | 1.04 MB | 3 | 1.04 MB | 99.7 % |

Long sequences pay off on large natural-language texts, where words and phrases repeat often. On small files the header cost dominates, so `-l 1` works best. Already-compressed formats such as JPEG are close to maximum entropy and barely shrink.

## Project structure

```
src/main/java/pl/edu/pw/ee/aisd2025zex5/
├── Program.java          # entry point, compression/decompression pipeline
├── FlagReader.java       # command-line argument parsing and validation
├── MODE.java             # COMP / DECOMP enum
├── FileParser.java       # shared logic for merging buffers across block boundaries
├── FileReader.java       # input scanning and sequence counting
├── FileWriter.java       # greedy longest-match encoding
├── ByteReader.java       # bit-level reader used for decompression
├── ByteWriter.java       # bit-level writer (codes, header, padding)
├── HashMap.java          # open-addressing hash map with double hashing
├── PriorityQueue.java    # binary min-heap of tree nodes
├── Node.java             # Huffman tree node
├── Dictionary.java       # Huffman tree: build, serialize, decode
├── Code.java             # Huffman code representation
└── exception/            # invalid flag type / value exceptions

src/test/java/...         # JUnit 5 tests
data/                     # sample input files (not tracked except test fixtures)
output/                   # compression results (not tracked)
test_data/                # temporary files created by tests (not tracked)
```

## Tests

```bash
mvn test
```

Unit tests (JUnit 5) cover the hash map, the bit reader/writer, the file reader/writer and the Huffman dictionary. A JaCoCo coverage report is generated at `target/site/jacoco/index.html`.

## Author

Project for the Algorithms and Data Structures course (AiSD), winter semester 2025/26, Warsaw University of Technology.
