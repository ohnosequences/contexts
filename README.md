# Local Imports

A Scala **2.12.x** compiler plugin for locally scoped imports. You can write

``` scala
inside(x,y) {

  // arbitrary code
}
```

and this will be rewritten to

``` scala
{
  val inside_local_0 = x
  val inside_local_1 = y
  import inside_local_0._
  import inside_local_1._

  // arbitrary code
}
```

## Usage

Add to your build

```scala
addCompilerPlugin("ohnosequences" %% "local-imports" % version)
```

## Credits

This plugin is a simple modification of [mpilquist/local-implicits](https://github.com/mpilquist/local-implicits).