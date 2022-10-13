# ICSS

A school project to write a preprocessor transpiler, named ICSS.

# Requirements

Code explanations:

- `PA`: Parsing
- `CH`: Checking
- `TR`: Transforming
- `GE`: Generating
- `EX`: Expansions

| Implemented | Code | Description | Priority |
|-------------|------|-------------|----------|
| ✅          | PA00 | Write own implementation of a `Stack` | Must |
| ✅          | PA01 | Implement grammar + listener which can create AST's for simple ICSS files (`level0.icss`) | Must |
| ✅          | PA02 | Expand grammar + listener with functionality for variables (assignment and usage) (`level1.icss`) | Must |
| ✅          | PA03 | Expand grammar + listener to implement simple calculation (`+`, `-` and `*`) (don't evaluate yet) (`level2.icss`) | Must |
| ❌          | PA04 | Expand grammar + listener with if-else statements (`level3.icss`) | Must |
| ❌          | CH00 | Minimum of four `CH`-requirements need to be implemented | Must |
| ❌          | CH01 | Check for unused variables | Should |
| ❌          | CH02 | Left and right values from calculation operators (`+`, `-` and `*`) must be the same. `4 * 5` and `20% * 3` are allowed, but `2px * 3px` isn't. | Should |
| ❌          | CH03 | Left and right values from calculation operators (`+`, `-` and `*`) cannot be colors (e.g. `#ff0000`). | Should |
| ❌          | CH04 | Check if the types of the value assigned to a property is the correct type for that property (`width: #ff0000` and `color: 12px` aren't correct). | Should |
| ❌          | CH05 | Check if the condition in an if-else expression is a boolean type. | Should |
| ❌          | CH06 | Check if variables are used within the correct scope. | Should |
| ❌          | TR01 | Evaluate expressions (replace variables with their values). | Must |
| ❌          | TR02 | Evaluate if-else expressions. If the expression is `TRUE`, use the body of the if-statement. If the expression is `FALSE`, use the body of the else-statement. If there is no else-statement, use an empty body. | Must |
| ❌          | GE01 | Implement the generator (`nl.han.ica.icss.generator.Generator`) to generate a CSS2-compliant string. | Must |
| ❌          | GE02 | Change the generator to return formatted CSS (indented with 2 spaces). | Must |
