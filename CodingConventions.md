Most multi-programmer projects have coding standards to keep them uniform. So we ask you to please follow the project coding conventions below, as well as fix violations when you see them:
  * Do not hard-code class names in strings and comments, because they change and refactoring tools will not identify them. When referring to a class in a javadoc comment, always use the `{@link ...}` tag, and when adding a class name in a String, use
```
String message = "Object of class '" + SomeClass.class.getSimpleName() +
                 "' expected but was a " + object.getClass().getSimpleName() + " instead";
```
instead of
```
String message = "Object of class 'SomeClass' expected but was a " + object.getClass().getSimpleName() + " instead";
```
You may want to omit `getSimpleName()` if you need full class names.
  * Code blocks (in `while` and `if` statements, for instance) should be surrounded by curly braces (`{}`), even if they are a single statement. This makes it easier to add and remove statements, since one does not need to be concerned with the braces. The initial curly brace should be at the end of the line preceding the block (`while (...) {`).
  * `else` should be placed in a new line from the braces of the previous then block. The reason is that this form allows for quick commenting out of conditions and their blocks, for example:
```
if (happy) {
    doHappyThings();
}
else if (kindOfHappy) {
    doSomewhatHappyThings();
}
else {
    cryALittle();
}

instead of:

if (happy) {
    doHappyThings();
} else if (kindOfHappy) {   // else in the same line as closing } !
    doSomewhatHappyThings();
} else {                    // and here too !
    cryALittle();
}
```
  * Identifiers should have non-abbreviated names, even if that makes them long. The idea is that the reader knows what it means without having to look it up somewhere.
  * The code should produce no compiler warnings when committed to the repository. This way, when a programmer gets a warning, they know it is something that really needs to be attended to.
  * We use unit testing, so the code should be passing all tests when committed to the repository.
  * There should not be much nesting in methods and method invocations. A method should ideally have a single loop at most. Arguments to methods should not be method invocations themselves; when that is needed, we can always create a local variable, which makes things less nested, with the added advantage of a name for that temporary result, which eases reading and debugging.
  * The `return` statement should not be on a method invocation. When this presents itself, we should create a local variable with the result, typically named `result`. This is particularly useful during debugging, allowing us to see the value about to be returned.
  * Most concepts should be interfaces; methods should have parameters of interface types, so they can be invoked even with future implementations of those interfaces. Usually there is a default implementation of an interface, which is prefixed by `Default` (for example, `DefaultRewritingProcess` for the interface `RewritingProcess`). If multiple implementations of an interface are likely to use the same code for some of their methods, an abstract implementation of the functionality is provided (for example, `AbstractRewriter` defines some common methods used by implementations of the `Rewriter` interface).
  * Use `LinkedHashMap` and `LinkedHashSet` rather than `HashMap` and `HashSet` whenever possible, as the former make code more predictable and stable, but use interfaces `Map` and `Set` when defining return types, types of variables and parameters.