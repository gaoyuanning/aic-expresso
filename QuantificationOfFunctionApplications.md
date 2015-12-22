In logic, quantification can typically be done on variables only. We allow function applications to be quantified as well. For example, the expression corresponding to:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="sum\_{f(10)} f(1) + f(10)" height="25"/>

computes a sum over all possible interpretations of `f(10)` while leaving the interpretation of other "positions" of `f` untouched. For example, if:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="A = {1, 3, 10}, B = {1, 3, 5, 7}" height="25"/>

and:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="f : A -> B" height="25"/>

then there are a total of 4<sup>3</sup> = 64 possible interpretations. If we take it `f` is mapped to the interpretation:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="{(1,1),(3,3),(10,5)}" height="25"/>

then the previous summation would be iterated over 4 times, which would result in the following expanded out summation:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="(1+1) + (1+3) + (1+5) + (1+7) = 20" height="25"/>

In other words, when
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="sum\_{f(10)} ..." height="25"/>

is written, `f` is a new "locally" scoped symbol that agrees with the outside `f`'s interpretation on everything that is not `f(10)`. So it will iterate over only 4 interpretations, the ones identical to the outside one everywhere but `f(10)`, and then varying `f(10)` through `B`. Consider a variant of this where there is a free variable `y` in the expression:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="sum\_{f(10)} f(y) + f(10)" height="25"/>

If `y = 1` we would get the same value 20 as before. However, if `y = 10` then we would end up with `f(10) + f(10)` which would be equivalent to:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="sum\_{f(10)} 2 xx f(10) = 2 xx 16 = 32" height="25"/>

i.e. the original expanded out summation would have been:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="(1+1) + (3+3) + (5+5) + (7+7) = 32" height="25"/>

We can also move the free variable `y` into the index of the summation, for example:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="sum\_{f(y)} f(1) + f(10)" height="25"/>

consider the cases where `y = 1`, `y = 10`, and `y = 3`. The outside `f` is mapped to the same interpretation as before (i.e. `{(1,1),(3,3),(10,5)}`) then in case 1 where `y = 1`, we are iterating over `f(1)` giving us:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="(1+5) + (3+5) + (5+5) + (7+5) = 36" height="25"/>

in case 2 where `y = 10`, it iterates over `f(10)` giving us:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="(1+1) + (1+3) + (1+5) + (1+7) = 20" height="25"/>

and in case 3 where `y = 3`, it iterates over `f(3)`, even though it is not in the expression, giving us:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="(1+5) + (1+5) + (1+5) + (1+5) = 24" height="25"/>

The main point here is that, even though `y` appears in the index of the summation, it is not a "new" locally scoped symbol. This indexing of function applications only makes the "functor" be a new locally scoped symbol. The arguments are a normal expression, to be evaluated so the we know which "position" of `f` is being iterated. For example, it is fine to write:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="sum\_{f(1+9)} f(1) + f(10)" height="25"/>

or even:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="sum\_{f(y+9)} f(1) + f(10)" height="25"/>

Quantification of function applications may also be used with intensional set notation, for example:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="{f(1) + f(10)}\_{f(1) in {3,5}}" height="25"/>

would, using our prior interpretation for `f = {(1,1),(3,3),(10,5)}`, give us:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="{3+5, 5+5} = {8, 10}" height="25"/>

Here,
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="f(1) in {3,5}" height="25"/>

refers to the codomain of `f(1)` meaning the value of `f(1)` is going to take the values 3 and 5 in successive iterations. You can see this by analogy with the single variable case. If you write:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="{i}\_{i in {3,5}}" height="25"/>

you expect `i` to take on the values 3 and 5. A single variable can actually be seen as a function from an empty domain to a codomain, so in this case `{3,5}` had to be the codomain since there is no domain. Another example in which this is more clear is that it is perfectly fine to write something like:
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="{f(1)}\_{f(1) in {'hi', 'hello', 'ciao'}}" height="25"/>

that is, cases in which the set inside is not even of the same type as the domain of the function.