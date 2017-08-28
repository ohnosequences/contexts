# Contexts

A Scala **2.12.x** compiler plugin for contexts. You can write

``` scala
x ⊢ { // arbitrary code }
```

and this will be rewritten to

``` scala
{
  val inside_local_0 = x
  import inside_local_0._
  // arbitrary code
}
```

## Usage

Add to your build

```scala
addCompilerPlugin("ohnosequences" %% "contexts" % version)
```

## Credits

This plugin is a simple modification of [mpilquist/local-implicits](https://github.com/mpilquist/local-implicits).
